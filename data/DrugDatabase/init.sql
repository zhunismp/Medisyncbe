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
    blood_group TEXT NOT NULL,
    health_condition TEXT,
    drug_allergy TEXT,
    food_allergy TEXT
);

CREATE TABLE RELATIONSHIP_REQUESTED (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    relative_id UUID NOT NULL,
    create_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE RELATIONSHIP (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    relative_id UUID NOT NULL,
    relation TEXT NOT NULL,
    notifiable BOOLEAN DEFAULT FALSE,
    readable BOOLEAN DEFAULT FALSE,
    create_at TIMESTAMP DEFAULT NOW()
);
 
CREATE TABLE DRUG_GROUP (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    group_name TEXT NOT NULL
);

CREATE TABLE DRUG_GROUP_DRUG (
    id UUID PRIMARY KEY,
    drug_id UUID NOT NULL,
    group_id UUID NOT NULL
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
-- schedule_time field have dummy date. Please consider only time
CREATE TABLE SCHEDULE (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    schedule_time TIMESTAMP NOT NULL,
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
    "status" TEXT NOT NULL,
    taken_at TIMESTAMP,
    notified_at TIMESTAMP NOT NULL,
    count INT NOT NULL DEFAULT 0
);

ALTER TABLE RELATIONSHIP
ADD CONSTRAINT fk_relationship_user_id
FOREIGN KEY (user_id) REFERENCES APP_USER(id) ON DELETE CASCADE;

ALTER TABLE RELATIONSHIP
ADD CONSTRAINT fk_relationship_relative_id
FOREIGN KEY (relative_id) REFERENCES APP_USER(id) ON DELETE CASCADE;

ALTER TABLE DRUG
ADD CONSTRAINT fk_drug_user_id
FOREIGN KEY (user_id) REFERENCES APP_USER(id) ON DELETE CASCADE;

ALTER TABLE DRUG_GROUP
ADD CONSTRAINT fk_drug_group_user_id
FOREIGN KEY (user_id) REFERENCES APP_USER(id) ON DELETE CASCADE;

ALTER TABLE DRUG_GROUP_DRUG
ADD CONSTRAINT fk_drug_group_drug_drug_id
FOREIGN KEY (drug_id) REFERENCES DRUG(id) ON DELETE CASCADE;

ALTER TABLE DRUG_GROUP_DRUG
ADD CONSTRAINT fk_drug_group_drug_group_id
FOREIGN KEY (group_id) REFERENCES DRUG_GROUP(id) ON DELETE CASCADE;

ALTER TABLE SCHEDULE
ADD CONSTRAINT fk_schedule_user_id
FOREIGN KEY (user_id) REFERENCES APP_USER(id) ON DELETE CASCADE;

ALTER TABLE HISTORY
ADD CONSTRAINT fk_history_user_id
FOREIGN KEY (user_id) REFERENCES APP_USER(id) ON DELETE CASCADE;

ALTER TABLE HISTORY
ADD CONSTRAINT fk_history_drug_id
FOREIGN KEY (drug_id) REFERENCES DRUG(id) ON DELETE CASCADE;

ALTER TABLE HISTORY
ADD CONSTRAINT fk_history_group_id
FOREIGN KEY (group_id) REFERENCES DRUG_GROUP(id) ON DELETE CASCADE;