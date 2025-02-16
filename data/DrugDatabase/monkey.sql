-- Insert user
INSERT INTO APP_USER (
    id, register_token, first_name, last_name, birth_date, weight, height, gender, blood_group, health_condition, drug_allergy, food_allergy
) VALUES 
    ('a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68','sample_token_12345', 'John', 'Dang', '1990-05-15', 75.0, 180.0, 'M', 'O+', 'None', 'Penicillin', 'Peanuts'),
    ('b7e3adf0-29c9-4ef9-b8d7-55c68e3d3b61', 'sample_token_67890', 'Alice', 'Smith', '1992-08-20', 68.0, 165.0, 'F', 'A+', 'Asthma', 'Sulfa drugs', 'Gluten'),
    ('c2a8dfe2-3b6f-44b0-8f0d-9a1c3505c3f3', 'sample_token_11223', 'Bob', 'Brown', '1985-02-10', 80.0, 175.0, 'M', 'B-', 'Hypertension', 'None', 'None'),
    ('d4e6a1f3-5b7d-4c8e-9f10-2a3b5c6d7e8f', 'sample_token_44556', 'Emma', 'Wilson', '1993-11-25', 62.0, 160.0, 'F', 'AB-', 'Diabetes', 'None', 'Shellfish');

INSERT INTO RELATIONSHIP_REQUESTED (
    id, user_id, relative_id, create_at
) VALUES
    ('f1adbb1e-2f9f-4d1c-b8c4-8fa76d31fe13', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'c2a8dfe2-3b6f-44b0-8f0d-9a1c3505c3f3', '2025-01-19 08:45:00'),
    ('f1adbb1e-2f9f-4d1c-b8c4-8fa76d31fe16', 'c2a8dfe2-3b6f-44b0-8f0d-9a1c3505c3f3', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', '2025-01-19 08:45:00'),
    ('a75d56b8-e1e6-4e45-b6db-1953d4778cfd', 'c2a8dfe2-3b6f-44b0-8f0d-9a1c3505c3f3', 'b7e3adf0-29c9-4ef9-b8d7-55c68e3d3b61', '2025-01-19 08:45:00'),
    ('b7a65f7e-fb5d-44d2-82da-6b9a0917c7d7', 'b7e3adf0-29c9-4ef9-b8d7-55c68e3d3b61', 'c2a8dfe2-3b6f-44b0-8f0d-9a1c3505c3f3', '2025-01-19 08:45:00');

INSERT INTO RELATIONSHIP (
    id, user_id, relative_id, relation, notifiable, readable, create_at
) VALUES
    ('d3e72f6a-b50c-43ac-8001-11e9dbd9d861', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'b7e3adf0-29c9-4ef9-b8d7-55c68e3d3b61', 'friend', TRUE, TRUE, '2025-01-19 08:45:00');

-- Insert drug group
INSERT INTO DRUG_GROUP (id, user_id, group_name) VALUES 
    ('c362f634-e49f-44e9-bbe7-49d94488a6ff', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'Painkillers');

-- Insert individual drugs for the user
INSERT INTO DRUG (id, user_id, group_id, generic_name, dosage_form, unit, strength, amount, dose, taken_amount, usage_time, is_internal_drug) VALUES
    ('b2876821-1f75-4c1a-999a-46f8f03d7a4b', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', NULL, 'Paracetamol', 'Tablet', 'mg', '500mg', 20.0, 1.0, 0.0, 1, TRUE),
    ('b2876821-1f75-4c1a-999a-46f8f03d7a4c', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'c362f634-e49f-44e9-bbe7-49d94488a6ff', 'Ibuprofen', 'Tablet', 'mg', '200mg', 30.0, 2.0, 0.0, 1, TRUE),
    ('b2876821-1f75-4c1a-999a-46f8f03d7a4d', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'c362f634-e49f-44e9-bbe7-49d94488a6ff', 'Aspirin', 'Tablet', 'mg', '100mg', 15.0, 1.0, 0.0, 1, TRUE),
    ('b2876821-1f75-4c1a-999a-46f8f03d7a4e', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'c362f634-e49f-44e9-bbe7-49d94488a6ff', 'Diclofenac', 'Tablet', 'mg', '50mg', 25.0, 1.0, 0.0, 1, TRUE);

-- Insert schedules for individual drug and drug group
-- For schedule_time, there is timestamp with dummy date. Need to resolved gorm issue with TIME.
INSERT INTO SCHEDULE (id, user_id, schedule_time, type, name, reference_id, is_enabled) VALUES 
    ('1d2f7a4c-ccbc-4ef8-9296-8db0de287831', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', '2000-01-01 22:43:00', 0, 'Morning Dose', 'b2876821-1f75-4c1a-999a-46f8f03d7a4b', TRUE),
    ('2e4c5a5d-d72a-44a9-bd47-b64a64d97e9c', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', '2000-01-01 22:43:00', 1, 'Painkillers Group Dose', 'c362f634-e49f-44e9-bbe7-49d94488a6ff', TRUE);

-- Insert history for individual drug
INSERT INTO HISTORY (id, user_id, drug_id, group_id, status, taken_at, notified_at, count) VALUES 
    ('9c77574b-b1ab-49a4-8bb3-bddff16718da', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 'b2876821-1f75-4c1a-999a-46f8f03d7a4b', NULL, 'taken', '2025-01-18 09:00:00', '2025-01-18 08:45:00', 1);

INSERT INTO HISTORY (id, user_id, drug_id, group_id, status, taken_at, notified_at, count) VALUES 
    ('9c77574b-b1ab-49a4-8bb3-bddff16718db', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4b', NULL, 'taken', '2025-01-18 09:00:00', '2025-01-18 08:45:00', 1),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718dc', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4c', NULL, 'taken', '2025-01-18 10:00:00', '2025-01-18 09:45:00', 2),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718dd', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4d', NULL, 'taken', '2025-01-18 11:00:00', '2025-01-18 10:45:00', 1),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718de', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4e', NULL, 'taken', '2025-01-18 12:00:00', '2025-01-18 11:45:00', 1),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718df', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4b', NULL, 'missed', '2025-01-19 09:00:00', '2025-01-19 08:45:00', 0),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718e0', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4c', NULL, 'taken', '2025-01-19 10:00:00', '2025-01-19 09:45:00', 1),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718e1', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4d', NULL, 'missed', '2025-01-19 11:00:00', '2025-01-19 10:45:00', 0),
    ('9c77574b-b1ab-49a4-8bb3-bddff16718e2', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d68', 
        'b2876821-1f75-4c1a-999a-46f8f03d7a4e', NULL, 'taken', '2025-01-19 12:00:00', '2025-01-19 11:45:00', 1);