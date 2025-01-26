-- Insert user
INSERT INTO APP_USER (
    id, register_token, first_name, last_name, birth_date, weight, height, gender, blood_group, health_condition, drug_allergy, food_allergy
) VALUES (
    'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
    'sample_token_12345', 'John', 'Doe', '1990-05-15', 75.0, 180.0, 'M', 'O+', 'None', 'Penicillin', 'Peanuts'
);

-- Insert individual drugs for the user
INSERT INTO DRUG (id, user_id, generic_name, dosage_form, unit, strength, amount, dose, taken_amount, usage_time, is_internal_drug) VALUES
('b2876821-1f75-4c1a-999a-46f8f03d7a4b', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'Paracetamol', 'Tablet', 'mg', '500mg', 20.0, 500.0, 0.0, 1, TRUE),
('b2876821-1f75-4c1a-999a-46f8f03d7a4c', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'Ibuprofen', 'Tablet', 'mg', '200mg', 30.0, 200.0, 0.0, 1, TRUE),
('b2876821-1f75-4c1a-999a-46f8f03d7a4d', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'Aspirin', 'Tablet', 'mg', '100mg', 15.0, 100.0, 0.0, 1, TRUE),
('b2876821-1f75-4c1a-999a-46f8f03d7a4e', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'Diclofenac', 'Tablet', 'mg', '50mg', 25.0, 50.0, 0.0, 1, TRUE);

-- Insert drug group
INSERT INTO DRUG_GROUP (id, user_id, group_name, drug_id) VALUES (
    'c362f634-e49f-44e9-bbe7-49d94488a6ff', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'Painkillers', 
    ARRAY['b2876821-1f75-4c1a-999a-46f8f03d7a4c', 'b2876821-1f75-4c1a-999a-46f8f03d7a4d', 'b2876821-1f75-4c1a-999a-46f8f03d7a4e']::uuid[]
);

-- Insert schedules for individual drug and drug group
-- For schedule_time, there is timestamp with dummy date. Need to resolved gorm issue with TIME.
INSERT INTO SCHEDULE (id, user_id, schedule_time, type, name, reference_id, is_enabled) VALUES 
('1d2f7a4c-ccbc-4ef8-9296-8db0de287831', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', '2000-01-01 17:15:00', 0, 'Morning Dose', 'b2876821-1f75-4c1a-999a-46f8f03d7a4b', TRUE),
('2e4c5a5d-d72a-44a9-bd47-b64a64d97e9c', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', '2000-01-01 09:00:00', 1, 'Painkillers Group Dose', 'c362f634-e49f-44e9-bbe7-49d94488a6ff', TRUE);

-- Insert history for individual drug
INSERT INTO HISTORY (id, user_id, drug_id, group_id, status, taken_at, notified_at, count) VALUES (
    '9c77574b-b1ab-49a4-8bb3-bddff16718da', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
    'b2876821-1f75-4c1a-999a-46f8f03d7a4b', NULL, 'taken', '2025-01-18 09:00:00', '2025-01-18 08:45:00', 1
);