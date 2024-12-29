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
--    schedule TIME[] NOT NULL,
    is_internal_drug BOOLEAN NOT NULL,
--    is_enable BOOLEAN NOT NULL
);

CREATE INDEX idx_drug_user_id ON drug (user_id);

CREATE TABLE APP_USER (
    id UUID PRIMARY KEY,
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
--    schedule TIME[],
    drug_id UUID[],
--    is_enable BOOLEAN NOT NULL
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

CREATE TABLE DRUG_SCHEDULE (
    id UUID PRIMARY KEY,
    device_token TEXT NOT NULL,
    user_id UUID NOT NULL, 
    drug_id UUID NOT NULL,
    schedule_time TIME NOT NULL
);

CREATE INDEX idx_drug_schedule_schedule_time ON drug_schedule (schedule_time);

CREATE TABLE DRUG_GROUP_SCHEDULE (
    id UUID PRIMARY KEY,
    device_token TEXT NOT NULL,
    user_id UUID NOT NULL, 
    drug_group_id UUID NOT NULL,
    schedule_time TIME NOT NULL
);

CREATE INDEX idx_drug_group_schedule_schedule_time ON drug_group_schedule (schedule_time);