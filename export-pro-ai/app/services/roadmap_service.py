import uuid
import json
from typing import Dict, Any, List
from openai import AsyncOpenAI
from app.core.config import get_settings
from app.models.roadmap import (
    StartRoadmapRequest, StartRoadmapResponse,
    AnswerRequest, AnswerResponse, AxisAdvice,
)

settings = get_settings()
client = AsyncOpenAI(api_key=settings.openai_api_key)

_sessions: Dict[str, Dict[str, Any]] = {}

AXES = ["A", "B", "C", "D", "E", "F", "G"]

AXIS_CONFIG = {
    "A": {
        "label": "Produs și Diferențiere",
        "questions": [
            "Să începem cu produsul tău. Ce produci exact, din ce ingrediente principale, și cât este termenul de valabilitate?",
            "Ce te diferențiază față de produse similare din piață? Ai certificări (organic, PDO, HACCP) sau caracteristici unice de origine sau rețetă?",
        ],
        "rubric": (
            "1=produs generic, ușor de înlocuit, fără certificări; "
            "2=câteva aspecte unice dar neclare; "
            "3=diferențiere clară SAU câteva certificări; "
            "4=diferențiere puternică + certificări; "
            "5=produs unic, certificat, poveste puternică de origine/rețetă, termen valabilitate bun."
        ),
    },
    "B": {
        "label": "Brand și Ambalaj",
        "questions": [
            "Cum arată brandul tău acum? Ai un logo, un nume de brand distinct și o poveste de produs?",
            "Cum este ambalat produsul? Ai etichetă în română cu toate informațiile legale, cod de bare EAN și ambalaj gata de raft?",
        ],
        "rubric": (
            "1=fără brand, vrac sau etichetă improvizată; "
            "2=ceva branding dar incomplet; "
            "3=logo și nume, ambalaj de bază; "
            "4=brand coerent, ambalaj decent, aproape gata de raft; "
            "5=brand profesionist, poveste, ambalaj retail-ready cu EAN și etichetă în română."
        ),
    },
    "C": {
        "label": "Capacitate și Consistență",
        "questions": [
            "Cât produci lunar în prezent și este producția constantă pe tot parcursul anului sau sezonieră?",
            "Dacă un retailer ar dubla o comandă pentru o promoție, poți livra în termen? Ai capacitate de rezervă sau stocuri tampon?",
        ],
        "rubric": (
            "1=volum mic, imprevizibil; "
            "2=mic dar oarecum constant; "
            "3=volum moderat, majoritar constant; "
            "4=volum bun, constant, capacitate de creștere; "
            "5=volum stabil, calitate constantă, poate crește 50%+ pentru promoții."
        ),
    },
    "D": {
        "label": "Preț și Marjă",
        "questions": [
            "Îți cunoști costul exact per unitate de produs (materie primă + manoperă + ambalaj + transport + overhead)?",
            "La ce preț vinzi acum și cum se compară cu produse similare în supermarketurile din România? Există loc pentru marja comerciantului (30-40%) și să rămâi profitabil?",
        ],
        "rubric": (
            "1=nu știe costul per unitate, fără comparație piață; "
            "2=cost aproximativ, fără analiză piață; "
            "3=cunoaște costul, conștient de piață; "
            "4=structură clară de costuri, preț competitiv cu marjă; "
            "5=cost complet cunoscut, preț competitiv, 30-40% marjă disponibilă."
        ),
    },
    "E": {
        "label": "Prezență pe Piață",
        "questions": [
            "Unde vinzi în prezent? (piețe locale, magazine, online, HoReCa, alte județe, export direct)",
            "Ai clienți recurenți sau dovezi clare de vânzare (facturi regulate, recenzii, contracte)? Ai mai exportat înainte?",
        ],
        "rubric": (
            "1=doar vânzări ocazionale locale; "
            "2=câteva canale locale, clienți limitați; "
            "3=canale multiple, câțiva clienți fideli; "
            "4=canale stabilite, clienți recurenți, prezență online; "
            "5=canale multiple, clienți fideli, prezență online, istoric export sau dovezi puternice."
        ),
    },
    "F": {
        "label": "Vânzări și Marketing",
        "questions": [
            "Cine se ocupă de vânzări și negocieri în compania ta? Este o persoană dedicată sau toată lumea face câte puțin?",
            "Ai materiale de prezentare pentru cumpărători (catalog, fișă produs, mostre)? Ai participat la târguri de profil sau ai avut întâlniri cu retaileri?",
        ],
        "rubric": (
            "1=nimeni dedicat, fără materiale; "
            "2=proprietarul se ocupă part-time, materiale minimale; "
            "3=semi-dedicat, materiale de bază; "
            "4=persoană dedicată, materiale bune, experiență târguri; "
            "5=echipă de vânzări, pitch deck profesionist, prezență târguri, experiență cumpărători."
        ),
    },
    "G": {
        "label": "Certificări și Conformitate",
        "questions": [
            "Ce certificări deții în prezent (ex. HACCP, ISO 22000, BIO/Organic, DOP/IGP, GlobalGAP, IFS, BRC)? Menționează fiecare certificare și până când este valabilă.",
            "Ai certificate sanitar-veterinare sau avize DSP/ANSVSA actualizate pentru producție și export? Există certificări pe care le-ai pierdut sau care expiră în următoarele 12 luni?",
        ],
        "rubric": (
            "1=nicio certificare sau toate expirate; "
            "2=HACCP de bază sau o singură certificare, parțial valabilă; "
            "3=HACCP valid + una-două certificări relevante în termen; "
            "4=mai multe certificări valide (ISO, BIO sau echivalent), avize actualizate; "
            "5=portofoliu complet de certificări valide (ISO 22000, BIO, avize export, eventual DOP/IGP), audit recent trecut cu bine."
        ),
    },
}


async def _score_axis(axis: str, answers: List[str]) -> int:
    config = AXIS_CONFIG[axis]
    prompt = (
        f'Evaluează răspunsurile unui producător pentru axa "{config["label"]}".\n\n'
        f"Răspuns 1: {answers[0] if answers else 'N/A'}\n"
        f"Răspuns 2: {answers[1] if len(answers) > 1 else 'N/A'}\n\n"
        f"Rubric: {config['rubric']}\n\n"
        f"Returnează DOAR un număr întreg de la 1 la 5."
    )
    try:
        response = await client.chat.completions.create(
            model=settings.ai_model,
            messages=[{"role": "user", "content": prompt}],
            temperature=0,
            max_tokens=5,
        )
        return max(1, min(5, int(response.choices[0].message.content.strip())))
    except Exception:
        return 3


async def _generate_advice(scores: Dict[str, int]) -> List[AxisAdvice]:
    scores_text = "\n".join(
        f'{axis}. {AXIS_CONFIG[axis]["label"]}: {score}/5'
        for axis, score in scores.items()
    )
    prompt = (
        f"Scoruri producător:\n{scores_text}\n\n"
        "Generează sfaturi specifice și acționabile pentru fiecare axă în română. "
        "Pentru axele cu scor 1-2 fii mai detaliat cu pași concreți.\n\n"
        'Returnează JSON cu structura: {"items": [{"axis": "A", "label": "Produs și Diferențiere", "score": 3, "advice": "sfat concret"}]}'
    )
    try:
        response = await client.chat.completions.create(
            model=settings.ai_model,
            messages=[
                {
                    "role": "system",
                    "content": (
                        "Ești un consultant expert în export pentru producători din România/UE. "
                        "Răspunzi DOAR cu JSON valid."
                    ),
                },
                {"role": "user", "content": prompt},
            ],
            temperature=0.3,
            response_format={"type": "json_object"},
        )
        data = json.loads(response.choices[0].message.content)
        items = data.get("items", [])
        return [AxisAdvice(**item) for item in items]
    except Exception:
        return [
            AxisAdvice(
                axis=axis,
                label=AXIS_CONFIG[axis]["label"],
                score=score,
                advice="Concentrează-te pe îmbunătățirea aspectelor din această zonă.",
            )
            for axis, score in scores.items()
        ]


async def start_roadmap(request: StartRoadmapRequest) -> StartRoadmapResponse:
    session_id = str(uuid.uuid4())
    _sessions[session_id] = {
        "company_id": request.company_id,
        "company_name": request.company_name,
        "current_axis_index": 0,
        "current_question_index": 0,
        "axis_answers": {axis: [] for axis in AXES},
        "scores": {},
        "is_complete": False,
    }
    first_axis = AXES[0]
    intro = (
        f"Bună ziua! Sunt asistentul tău de pregătire pentru export. "
        f"Voi evalua pregătirea companiei **{request.company_name}** pe 6 dimensiuni cheie "
        f"și îți voi oferi un raport personalizat cu sfaturi concrete.\n\n"
        f"Să începem cu prima dimensiune: **{AXIS_CONFIG[first_axis]['label']}**\n\n"
        f"{AXIS_CONFIG[first_axis]['questions'][0]}"
    )
    return StartRoadmapResponse(session_id=session_id, message=intro, axis=first_axis)


async def process_answer(request: AnswerRequest) -> AnswerResponse:
    session = _sessions.get(request.session_id)
    if not session:
        return AnswerResponse(
            message="Sesiunea a expirat. Te rog să începi un nou roadmap.",
            is_complete=False,
        )

    axis_index = session["current_axis_index"]
    q_index = session["current_question_index"]
    current_axis = AXES[axis_index]

    session["axis_answers"][current_axis].append(request.answer)

    questions = AXIS_CONFIG[current_axis]["questions"]

    if q_index + 1 < len(questions):
        session["current_question_index"] += 1
        return AnswerResponse(
            message=questions[q_index + 1],
            axis=current_axis,
            is_complete=False,
        )

    score = await _score_axis(current_axis, session["axis_answers"][current_axis])
    session["scores"][current_axis] = score

    next_axis_index = axis_index + 1
    session["current_axis_index"] = next_axis_index
    session["current_question_index"] = 0

    if next_axis_index >= len(AXES):
        session["is_complete"] = True
        advice = await _generate_advice(session["scores"])
        scores_preview = " | ".join(f"{a}: {s}/5" for a, s in session["scores"].items())
        return AnswerResponse(
            message=(
                f"Excelent! Am finalizat evaluarea completă.\n\n"
                f"**Scoruri:** {scores_preview}\n\n"
                "Mai jos găsești raportul tău radar complet cu recomandări personalizate."
            ),
            is_complete=True,
            scores=session["scores"],
            advice=advice,
        )

    next_axis = AXES[next_axis_index]
    return AnswerResponse(
        message=(
            f"Mulțumesc! Am notat răspunsurile pentru **{AXIS_CONFIG[current_axis]['label']}**.\n\n"
            f"Trecem la **{AXIS_CONFIG[next_axis]['label']}**.\n\n"
            f"{AXIS_CONFIG[next_axis]['questions'][0]}"
        ),
        axis=next_axis,
        is_complete=False,
    )
