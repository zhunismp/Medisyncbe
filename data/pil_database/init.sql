-- TODO: Split Strength and unit
CREATE TABLE PILDATABASE (
    id UUID PRIMARY KEY,
    generic_name TEXT,
    dosage_form TEXT,
    strength_unit TEXT,
    related_link TEXT,
    during_use TEXT[],
    storage TEXT[],
    usage TEXT[],
    warning TEXT[],
    avoidance TEXT[],
    forgetting TEXT[],
    overdose TEXT[],
    danger_side_effect TEXT[],
    side_effect TEXT[]
);

COPY PILDATABASE (id, generic_name, dosage_form, strength_unit, related_link, during_use, storage, usage, warning, avoidance, forgetting, overdose, danger_side_effect, side_effect)
FROM '/docker-entrypoint-initdb.d/pildata.csv'
DELIMITER ','
CSV HEADER;