INSERT INTO APP_USER (
    id, register_token, first_name, last_name, birth_date, weight, height, gender, blood_group, health_condition, drug_allergy, food_allergy
) VALUES (
    'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
    'sample_token_12345',
    'John',
    'Doe',
    '1990-05-15',  
    75.0,  
    180.0,  
    'M',  
    'O+',  
    'None', 
    'Penicillin',
    'Peanuts' 
);

INSERT INTO DRUG (
    id, user_id, generic_name, dosage_form, unit, strength, amount, dose, taken_amount, usage_time, is_internal_drug
) VALUES (
    'b2876821-1f75-4c1a-999a-46f8f03d7a4b',  
    'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
    'Paracetamol', 
    'Tablet',
    'mg', 
    '500mg',
    20.0,
    500.0, 
    NULL, 
    1,
    TRUE  
);

INSERT INTO DRUG_GROUP (
    id, user_id, group_name, drug_id
) VALUES (
    'c362f634-e49f-44e9-bbe7-49d94488a6ff',
    'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68',
    'Painkillers',
    ARRAY['b2876821-1f75-4c1a-999a-46f8f03d7a4b']::uuid[]
);


INSERT INTO SCHEDULE (
    id, user_id, schedule_time, type, name, reference_id, is_enabled
) VALUES (
    '1d2f7a4c-ccbc-4ef8-9296-8db0de287831',  
    'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
    '23:35:00',
    0, 
    'Morning Dose',
    'b2876821-1f75-4c1a-999a-46f8f03d7a4b',
    TRUE
);

INSERT INTO HISTORY (
    id, user_id, drug_id, group_id, status, taken_at, notified_at, count
) VALUES (
    '9c77574b-b1ab-49a4-8bb3-bddff16718da', 
    'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68',
    'b2876821-1f75-4c1a-999a-46f8f03d7a4b',
    NULL,
    'taken',
    '2025-01-18 09:00:00',
    '2025-01-18 08:55:00',
    1
);