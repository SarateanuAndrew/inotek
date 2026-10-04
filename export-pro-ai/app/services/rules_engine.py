from app.models.compliance import ProductClassification, RequirementItem, RequirementType, RequirementStatus, SourceReference

RULES: list[dict] = [
    {
        "rule_id": "FOOD-EU-001",
        "conditions": {"destination": ["RO", "EU"], "category": "FOOD"},
        "requirements": [
            {
                "code": "REQ-FOOD-HYGIENE",
                "type": RequirementType.LEGAL,
                "title": "EU Food Hygiene Compliance",
                "status": RequirementStatus.REQUIRED,
                "legal_basis": "Regulation (EC) No 852/2004 on the hygiene of foodstuffs",
                "source_url": "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX%3A32004R0852",
                "reason": "All food products imported into the EU must comply with hygiene regulations.",
                "evidence_required": ["HACCP documentation", "hygiene audit report"],
                "confidence": 0.95
            },
            {
                "code": "REQ-FOOD-LABELLING",
                "type": RequirementType.LABELING,
                "title": "EU Food Information to Consumers",
                "status": RequirementStatus.REQUIRED,
                "legal_basis": "Regulation (EU) No 1169/2011 on food information to consumers",
                "source_url": "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX%3A32011R1169",
                "reason": "Labels must meet EU FIC requirements including language, allergen declaration, and nutritional information.",
                "evidence_required": ["product label draft", "label translation if not in Romanian"],
                "confidence": 0.95
            }
        ]
    },
    {
        "rule_id": "FOOD-HONEY-EU",
        "conditions": {"destination": ["RO", "EU"], "sub_category": "HONEY"},
        "requirements": [
            {
                "code": "REQ-HONEY-MARKETING",
                "type": RequirementType.LEGAL,
                "title": "EU Honey Marketing Standards",
                "status": RequirementStatus.REQUIRED,
                "legal_basis": "Council Directive 2001/110/EC relating to honey",
                "source_url": "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX%3A32001L0110",
                "reason": "Honey must comply with EU composition and labeling standards including country of origin declaration.",
                "evidence_required": ["laboratory analysis report", "country of origin declaration"],
                "confidence": 0.92
            },
            {
                "code": "REQ-HONEY-TRACEABILITY",
                "type": RequirementType.TRACEABILITY,
                "title": "Honey Traceability Documentation",
                "status": RequirementStatus.REQUIRED,
                "legal_basis": "Regulation (EC) No 178/2002 - General Food Law",
                "reason": "Traceability must be ensured from beekeeper to packager.",
                "evidence_required": ["beekeeper registration", "production records", "batch traceability records"],
                "confidence": 0.90
            }
        ]
    },
    {
        "rule_id": "FOOD-DAIRY-EU",
        "conditions": {"destination": ["RO", "EU"], "sub_category": "DAIRY"},
        "requirements": [
            {
                "code": "REQ-DAIRY-ANIMAL-ORIGIN",
                "type": RequirementType.HEALTH_SAFETY,
                "title": "Products of Animal Origin - Health Requirements",
                "status": RequirementStatus.REQUIRED,
                "legal_basis": "Regulation (EC) No 853/2004 - specific hygiene rules for food of animal origin",
                "source_url": "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX%3A32004R0853",
                "reason": "Dairy products are of animal origin and require approved establishment and health certificate.",
                "evidence_required": ["approved establishment certificate", "veterinary health certificate", "pasteurization records"],
                "confidence": 0.93
            }
        ]
    },
    {
        "rule_id": "ORGANIC-EU",
        "conditions": {"destination": ["RO", "EU"], "organic": True},
        "requirements": [
            {
                "code": "REQ-ORGANIC-CERT",
                "type": RequirementType.CERTIFICATION,
                "title": "EU Organic Certification",
                "status": RequirementStatus.REQUIRED,
                "legal_basis": "Regulation (EU) 2018/848 on organic production",
                "source_url": "https://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX%3A32018R0848",
                "reason": "Products labeled as organic must be certified under EU organic regulations.",
                "evidence_required": ["organic certificate from accredited body"],
                "confidence": 0.97
            }
        ]
    }
]


def apply_rules(
    company_country: str,
    target_country: str,
    classification: ProductClassification,
    product_organic: bool
) -> list[RequirementItem]:
    matched: list[RequirementItem] = []
    seen_codes: set[str] = set()

    def matches(conditions: dict) -> bool:
        if "destination" in conditions:
            if target_country.upper() not in [d.upper() for d in conditions["destination"]]:
                return False
        if "category" in conditions:
            if classification.category.upper() != conditions["category"].upper():
                return False
        if "sub_category" in conditions:
            if classification.sub_category.upper() != conditions["sub_category"].upper():
                return False
        if "organic" in conditions:
            if product_organic != conditions["organic"]:
                return False
        return True

    for rule in RULES:
        if matches(rule["conditions"]):
            for req_data in rule["requirements"]:
                if req_data["code"] not in seen_codes:
                    seen_codes.add(req_data["code"])
                    matched.append(RequirementItem(**req_data))

    return matched
