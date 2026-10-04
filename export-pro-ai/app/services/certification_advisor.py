import json
from openai import AsyncOpenAI
from app.core.config import get_settings
from app.models.certification import (
    CertificationRecommendRequest,
    CertificationRecommendResponse,
    CertificationItem,
)

settings = get_settings()
client = AsyncOpenAI(api_key=settings.openai_api_key)

_LEGISLATION = """
LEGISLAȚIE OBLIGATORIE ȘI CERTIFICĂRI PENTRU EXPORTATORI ALIMENTARI ÎN UE/ROMÂNIA:

1. HACCP — Baza legală: Regulamentul CE 852/2004
   OBLIGATORIU pentru toți producătorii alimentari comerciali.
   Cost: 500–3.000 EUR | Timp: 2–6 luni
   Pași: identificare pericole, puncte critice de control, proceduri monitorizare, documentație

2. ISO 22000:2018 — Sisteme de management siguranță alimentară
   Cerut de marii retaileri (Carrefour, Lidl, Kaufland, Mega Image).
   Cost: 2.000–8.000 EUR | Timp: 6–18 luni

3. ISO 9001:2015 — Sisteme de management al calității
   Cerut de distribuitori și retaileri de nivel mediu-mare.
   Cost: 2.000–6.000 EUR | Timp: 6–12 luni

4. Certificare Bio/Organic — Baza legală: Regulamentul UE 2018/848
   OBLIGATORIU dacă folosești "ecologic/bio/organic" pe etichetă.
   Organisme acreditate în România: Ecocert, ICEA, LACON.
   Cost: 500–2.000 EUR/an | Timp: 24 luni conversie + 6 luni certificare

5. Indicații Geografice DOP/IGP/STG — Baza legală: Regulamentul UE 1151/2012
   Valoros pentru miere, brânzeturi, mezeluri tradiționale, produse zonale.
   Cost: 1.000–5.000 EUR | Timp: 2–5 ani

6. Etichetare obligatorie — Baza legală: Regulamentul CE 1169/2011
   Informații nutritive, alergeni, ingrediente, date valabilitate, origine.
   Eticheta TREBUIE să fie în română pe piața din România.
   Cod EAN — GS1 România: 100–500 EUR/an

7. Autorizație sanitară ANSVSA
   OBLIGATORIU pentru produse de origine animală (lactate, miere, carne, ouă, pește).
   Cost: taxe administrative | Timp: 1–3 luni

8. Autorizație sanitară DSP (Direcția de Sănătate Publică)
   OBLIGATORIU pentru toate unitățile de producție alimentară.
   Necesită spații conforme normelor de igienă.

9. GlobalGAP — pentru producători agricoli primari
   Cerut de supermarketuri internaționale pentru fructe, legume, cereale.
   Cost: 500–2.000 EUR | Timp: 6–12 luni

10. Marcaj oval CE (număr veterinar)
    OBLIGATORIU pentru produse de origine animală destinate pieței UE.
    Eliberat de ANSVSA după inspecție.

REGULI SPECIFICE PE CATEGORII:
- MIERE: Regulamentul CE 2015/1980, declarație de origine obligatorie, analize fizico-chimice
- LACTATE: Regulamentul CE 853/2004, marcaj oval CE, ANSVSA obligatoriu
- PANIFICAȚIE/PATISERIE: HACCP obligatoriu, declarare alergeni, etichetare nutrițională
- CONSERVE: Autorizare ANSVSA, testare pH și stabilitate microbiologică
- PRODUSE PROASPETE: Trasabilitate completă, temperatura lanțului frigorific
"""

_SYSTEM_PROMPT = (
    "Ești un expert în reglementări alimentare și certificări pentru exportatori din România/UE. "
    "Oferă recomandări precise bazate pe legislație. "
    "Prioritizează: REQUIRED=obligatoriu prin lege, RECOMMENDED=cerut de retaileri, OPTIONAL=avantaj competitiv. "
    "Răspunde DOAR cu JSON valid.\n\n"
    f"CUNOȘTINȚE LEGISLATIVE:\n{_LEGISLATION}"
)


async def recommend_certifications(
    request: CertificationRecommendRequest,
) -> CertificationRecommendResponse:
    user_prompt = (
        f"Producător cu:\n"
        f"- Tip produs: {request.product_type}\n"
        f"- Categorie: {request.product_category}\n"
        f"- Produs de origine animală: {'Da' if request.is_animal_product else 'Nu'}\n"
        f"- Produs bio/organic: {'Da' if request.is_organic else 'Nu'}\n"
        f"- Piața țintă: {request.target_country}\n"
        f"- Certificări existente: {', '.join(request.current_certifications) or 'Niciuna'}\n"
        + (f"- Descriere: {request.company_description}\n" if request.company_description else "")
        + "\nCe certificări și documente are nevoie? Oferă pași concreți.\n\n"
        "Returnează JSON:\n"
        '{"certifications": [{"code": "HACCP", "name": "...", "priority": "REQUIRED", '
        '"description": "...", "legal_basis": "...", "estimated_cost": "...", '
        '"estimated_time": "...", "steps": ["..."]}], "summary": "..."}'
    )

    response = await client.chat.completions.create(
        model=settings.ai_model,
        messages=[
            {"role": "system", "content": _SYSTEM_PROMPT},
            {"role": "user", "content": user_prompt},
        ],
        temperature=0.1,
        response_format={"type": "json_object"},
    )

    data = json.loads(response.choices[0].message.content)
    certifications = [CertificationItem(**c) for c in data.get("certifications", [])]

    return CertificationRecommendResponse(
        product_type=request.product_type,
        total_required=sum(1 for c in certifications if c.priority == "REQUIRED"),
        total_recommended=sum(1 for c in certifications if c.priority == "RECOMMENDED"),
        certifications=certifications,
        summary=data.get("summary", ""),
    )
