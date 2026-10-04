CREATE TABLE company_roadmaps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    scores_json TEXT NOT NULL,
    advice_json TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_company_roadmaps_company_id ON company_roadmaps(company_id);
