CREATE TABLE DRUGDATABASE (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    generic_name TEXT NOT NULL,
    dosage_form TEXT,
    unit TEXT NOT NULL,
    strength TEXT,
    amount NUMERIC NOT NULL,
    dose NUMERIC NOT NULL,
    taken_amount NUMERIC,
    usage_time INT NOT NULL,
    schedule TIME[] NOT NULL,
    is_internal_drug BOOLEAN NOT NULL,
    is_enable BOOLEAN NOT NULL
);

CREATE TABLE DRUGGROUP (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    group_name TEXT NOT NULL,
    schedule TIME[] NOT NULL,
    drug_id UUID[],
    is_enable BOOLEAN NOT NULL
);