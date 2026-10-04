import json
from openai import AsyncOpenAI
from app.core.config import get_settings
from app.models.compliance import ProductInfo, ProductClassification


async def classify_product(product: ProductInfo) -> ProductClassification:
    settings = get_settings()

    if not settings.openai_api_key:
        return ProductClassification(
            category="FOOD",
            sub_category=product.category.split("_")[-1] if "_" in product.category else "OTHER",
            suggested_hs_code=None,
            confidence=0.5
        )

    client = AsyncOpenAI(api_key=settings.openai_api_key)
    prompt = f"""You are a product classification expert for EU customs and regulatory purposes.

Classify this product and suggest a CN/HS code:

Product name: {product.name}
Category hint: {product.category}
Ingredients: {product.ingredients or 'not specified'}
Packaging: {product.packaging_type or 'not specified'}
Weight: {product.weight_grams}g
Organic: {product.organic}

Respond with a JSON object:
{{
  "category": "FOOD" or "NON_FOOD",
  "sub_category": "HONEY" | "DAIRY" | "BAKERY" | "MEAT" | "VEGETABLES" | "BEVERAGES" | "OTHER",
  "suggested_hs_code": "the most likely HS/CN code (4-6 digits)",
  "confidence": 0.0-1.0,
  "reasoning": "brief explanation"
}}"""

    response = await client.chat.completions.create(
        model=settings.ai_model,
        temperature=settings.ai_temperature,
        response_format={"type": "json_object"},
        messages=[{"role": "user", "content": prompt}]
    )

    data = json.loads(response.choices[0].message.content)
    return ProductClassification(
        category=data.get("category", "FOOD"),
        sub_category=data.get("sub_category", "OTHER"),
        suggested_hs_code=data.get("suggested_hs_code"),
        confidence=float(data.get("confidence", 0.7))
    )
