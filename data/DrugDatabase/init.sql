CREATE TABLE DRUG (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    generic_name TEXT NOT NULL,
    dosage_form TEXT,
    unit TEXT NOT NULL,
    strength TEXT,
    amount FLOAT NOT NULL,
    dose FLOAT NOT NULL,
    taken_amount FLOAT,
    usage_time INT NOT NULL,
    is_internal_drug BOOLEAN NOT NULL
);

CREATE INDEX idx_drug_user_id ON drug (user_id);

CREATE TABLE APP_USER (
    id UUID PRIMARY KEY,
    register_token TEXT,
    first_name TEXT NOT NULL,
    last_name TEXT,
    birth_date DATE NOT NULL,
    "weight" FLOAT,
    height FLOAT,
    gender CHAR NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    health_condition TEXT,
    drug_allergy TEXT,
    food_allergy TEXT
);
 
CREATE TABLE DRUG_GROUP (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    group_name TEXT NOT NULL,
    drug_id UUID[]
);

CREATE INDEX idx_drug_group_user_id ON drug_group (user_id);

CREATE TABLE APPOINTMENT (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title TEXT NOT NULL,
    medic_name TEXT,
    "date" DATE NOT NULL,
    "time" TIME NOT NULL,
    destination TEXT,
    remark TEXT
);

-- 0 is drug
-- 1 is drug group
-- reference_id is either drug_id or drug_group_id
CREATE TABLE SCHEDULE (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    schedule_time TIME NOT NULL,
    "type" INT NOT NULL,
    "name" TEXT NOT NULL,
    reference_id UUID NOT NULL,
    is_enabled BOOLEAN NOT NULL
);

CREATE TABLE HISTORY (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    drug_id UUID NOT NULL,
    group_id UUID,
    "status" TEXT CHECK ("status" IN ('taken', 'missed', 'skipped')),
    taken_at TIMESTAMP,
    notified_at TIMESTAMP NOT NULL,
    count INT NOT NULL DEFAULT 0
);