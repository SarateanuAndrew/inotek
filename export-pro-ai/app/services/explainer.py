import json
from openai import AsyncOpenAI
from app.core.config import get_settings
from app.models.compliance import RequirementItem, ProductInfo


async def enrich_requirements_with_explanations(
    requirements: list[RequirementItem],
    product: ProductInfo,
    company_country: str,
    target_country: str
) -> list[RequirementItem]:
    settings = get_settings()
    if not requirements or not settings.openai_api_key:
        return requirements

    client = AsyncOpenAI(api_key=settings.openai_api_key)
    req_summary = "\n".join([f"- {r.code}: {r.title}" for r in requirements])

    prompt = f"""You are a regulatory compliance expert for EU food import requirements.

Product: {product.name} ({product.category})
Origin: {company_country}
Destination: {target_country}

The following requirements have been identified by a rules engine:
{req_summary}

For each requirement, provide a clear, practical explanation of WHY this applies to this specific product
and WHAT the producer needs to do. Keep explanations under 100 words each.

Respond with a JSON object where keys are requirement codes and values are explanations:
{{
  "REQ-CODE": "practical explanation here...",
  ...
}}"""

    try:
        response = await client.chat.completions.create(
            model=settings.ai_model,
            temperature=0.3,
            response_format={"type": "json_object"},
            messages=[{"role": "user", "content": prompt}]
        )
        explanations = json.loads(response.choices[0].message.content)
        for req in requirements:
            if req.code in explanations:
                req.reason = explanations[req.code]
    except Exception:
        pass

    return requirements


async def generate_analysis_summary(
    product: ProductInfo,
    requirements: list[RequirementItem],
    readiness_pct: int
) -> str:
    settings = get_settings()
    if not settings.openai_api_key:
        return f"Analysis complete. {len(requirements)} requirements identified. Readiness: {readiness_pct}%."

    client = AsyncOpenAI(api_key=settings.openai_api_key)
    prompt = f"""Create a brief, encouraging summary (2-3 sentences) for a producer whose product "{product.name}"
has been analyzed for EU market access.
Requirements found: {len(requirements)}.
Current readiness: {readiness_pct}%.
Keep it professional and actionable."""

    try:
        response = await client.chat.completions.create(
            model=settings.ai_model,
            temperature=0.5,
            messages=[{"role": "user", "content": prompt}]
        )
        return response.choices[0].message.content.strip()
    except Exception:
        return f"Your product has {len(requirements)} compliance requirements identified for the EU market. Complete all requirements to reach 100% readiness."
