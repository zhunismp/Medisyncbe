INSERT INTO app_user (
    id, register_token, first_name, last_name, birth_date, weight, height, gender, blood_group, health_condition, drug_allergy, food_allergy, streak
) VALUES 
    ('a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d99','sample_token_12345', 'John', 'Dand', '1990-05-15', 75.0, 180.0, 'M', 'O+', 'None', 'Penicillin', 'Peanuts', 2);

INSERT INTO drug (id, user_id, generic_name, dosage_form, unit, strength, amount, dose, taken_amount, usage_time, is_internal_drug) VALUES
    ('b2876821-1f75-4c1a-999a-46f8f03d7a4f', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d99', 'Paracetamol', 'Tablet', 'mg', '500mg', 20.0, 1.0, 0.0, 1, TRUE);

INSERT INTO SCHEDULE (id, user_id, schedule_time, type, name, reference_id, is_enabled) VALUES 
    ('1d2f7a4c-ccbc-4ef8-9296-8db0de287832', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d99', '2000-01-01 17:19:00', 0, 'Morning Dose', 'b2876821-1f75-4c1a-999a-46f8f03d7a4f', TRUE);

INSERT INTO RELATIONSHIP (
    id, user_id, relative_id, relation, notifiable, readable, create_at
) VALUES
    ('b2876821-1f75-4c1a-999a-46f8f03d7c4f', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d99', '89c16667-85dd-e95b-7108-75e1fe0e8730', 'friend', TRUE, TRUE, '2025-01-19 08:45:00'),
	('b2876821-1f75-4c1a-999a-46f8f03d7c4a', '89c16667-85dd-e95b-7108-75e1fe0e8730', 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d99', 'friend', TRUE, TRUE, '2025-01-19 08:45:00');

SELECT * FROM history WHERE drug_id = 'b2876821-1f75-4c1a-999a-46f8f03d7a4f'