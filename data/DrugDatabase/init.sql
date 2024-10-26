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
    schedule TIME[] NOT NULL,
    is_internal_drug BOOLEAN NOT NULL,
    is_enable BOOLEAN NOT NULL
);

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
    drug_allergy TEXT
);
 
CREATE TABLE DRUGGROUP (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    group_name TEXT NOT NULL,
    schedule TIME[] NOT NULL,
    drug_id UUID[],
    is_enable BOOLEAN NOT NULL
);