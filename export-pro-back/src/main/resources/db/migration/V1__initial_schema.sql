-- Users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Companies
CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    country CHAR(3) NOT NULL,
    registration_number VARCHAR(100),
    vat_number VARCHAR(100),
    address TEXT,
    website VARCHAR(255),
    phone_number VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    owner_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Products
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    hs_code VARCHAR(20),
    ingredients TEXT,
    packaging_type VARCHAR(100),
    weight_grams INTEGER,
    organic BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Compliance Cases
CREATE TABLE compliance_cases (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    case_number VARCHAR(100) NOT NULL UNIQUE,
    product_id UUID NOT NULL REFERENCES products(id),
    company_id UUID NOT NULL REFERENCES companies(id),
    origin_country CHAR(3) NOT NULL,
    target_country CHAR(3) NOT NULL,
    status VARCHAR(100) NOT NULL DEFAULT 'DRAFT',
    readiness_score INTEGER NOT NULL DEFAULT 0,
    ai_analysis_summary TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Requirements
CREATE TABLE requirements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    compliance_case_id UUID NOT NULL REFERENCES compliance_cases(id),
    code VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    status VARCHAR(100) NOT NULL DEFAULT 'PENDING',
    legal_basis TEXT,
    source_url VARCHAR(500),
    ai_generated BOOLEAN NOT NULL DEFAULT false,
    ai_confidence DOUBLE PRECISION,
    evidence_required TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Documents
CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    company_id UUID NOT NULL REFERENCES companies(id),
    compliance_case_id UUID REFERENCES compliance_cases(id),
    document_type VARCHAR(100),
    original_filename VARCHAR(255) NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    mime_type VARCHAR(100),
    sha256 VARCHAR(64),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    extracted_text TEXT,
    issuer VARCHAR(255),
    document_number VARCHAR(100),
    issued_date DATE,
    expiry_date DATE,
    ai_confidence DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Questionnaires
CREATE TABLE questionnaires (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    product_category VARCHAR(100),
    questions_json TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Questionnaire Responses
CREATE TABLE questionnaire_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    compliance_case_id UUID NOT NULL REFERENCES compliance_cases(id),
    questionnaire_id UUID NOT NULL REFERENCES questionnaires(id),
    responses_json TEXT NOT NULL,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Marketplace Listings
CREATE TABLE marketplace_listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES products(id),
    company_id UUID NOT NULL REFERENCES companies(id),
    compliance_case_id UUID NOT NULL REFERENCES compliance_cases(id),
    title VARCHAR(500) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    certification_summary TEXT,
    target_markets VARCHAR(255),
    minimum_order_quantity INTEGER,
    production_capacity VARCHAR(255),
    published_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Distributors
CREATE TABLE distributors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    company_name VARCHAR(255) NOT NULL,
    cui VARCHAR(50) NOT NULL UNIQUE,
    vat_number VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'UNVERIFIED',
    rejection_reason TEXT,
    verified_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

-- Audit Events
CREATE TABLE audit_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_id UUID,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,
    event_type VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT now(),
    metadata TEXT
);

-- Indexes
CREATE INDEX idx_compliance_cases_company_id ON compliance_cases(company_id);
CREATE INDEX idx_compliance_cases_status ON compliance_cases(status);
CREATE INDEX idx_requirements_compliance_case_id ON requirements(compliance_case_id);
CREATE INDEX idx_documents_compliance_case_id ON documents(compliance_case_id);
CREATE INDEX idx_documents_company_id ON documents(company_id);
CREATE INDEX idx_marketplace_listings_status ON marketplace_listings(status);
CREATE INDEX idx_audit_events_entity ON audit_events(entity_type, entity_id);
CREATE INDEX idx_audit_events_timestamp ON audit_events(timestamp DESC);
