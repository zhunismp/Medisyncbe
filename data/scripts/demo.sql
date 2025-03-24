DO $$ 
DECLARE 
    main_user UUID := '89c16667-85dd-e95b-7108-75e1fe0e8730';
    u1 UUID := 'a6f730d8-8f72-4a9f-bf9c-5a6f9f4b7d99'; 
    u2 UUID := 'b8d5a1c4-2e61-4cbe-943e-2fdb9bfb4c2a'; 
    u3 UUID := 'c3e4d7f1-5b92-42fd-8f7a-6d4e1a3b5f8e'; 
    u4 UUID := 'd2a5f6e9-7c48-456b-bef0-5f7c9a1e3b4d';
    u5 UUID := 'e3f4a7d2-8c59-467a-cfe1-7a8b2d4c5e9f';

    group_id UUID := 'c362f634-e49f-44e9-bbe7-49d94488a6ff';
    drug_id_1 UUID := 'b2876821-1f75-4c1a-999a-46f8f03d7a4a';
    drug_id_2 UUID := 'b2876821-1f75-4c1a-999a-46f8f03d7a4c';
    drug_id_3 UUID := 'b2876821-1f75-4c1a-999a-46f8f03d7a4d';
    u3_drug_id UUID := 'b2876821-1f75-4c1a-999a-46f8f03d7a4e';
    u4_drug_id UUID := 'b2876821-1f75-4c1a-999a-46f8f03d7a4f';

    next_1_minutes TIMESTAMP := DATE_TRUNC('minute', '2000-01-01'::date + (now() AT TIME ZONE 'Asia/Bangkok' + INTERVAL '1 minute')::time);
    tomorrow TIMESTAMP := DATE_TRUNC('minute', now() AT TIME ZONE 'Asia/Bangkok' + INTERVAL '1 day');
    next_five_months TIMESTAMP := DATE_TRUNC('minute', now() AT TIME ZONE 'Asia/Bangkok' + INTERVAL '5 month');

    end_date TIMESTAMP := DATE_TRUNC('minute', now() AT TIME ZONE 'Asia/Bangkok' - INTERVAL '1 day');
    start_date TIMESTAMP := DATE_TRUNC('minute', now() AT TIME ZONE 'Asia/Bangkok' - INTERVAL '1 month');
    interval_step INTERVAL := INTERVAL '1 day';
    status_values TEXT[] := ARRAY['taken', 'missed', 'skipped'];
	
    c_time TIMESTAMP;
    status_value TEXT;
    count_value INT;
    notified_at TIMESTAMP;
    taken_at TIMESTAMP;
	
    user_drugs CONSTANT UUID[] := ARRAY[drug_id_1, drug_id_2, drug_id_3, u3_drug_id, u4_drug_id];
    user_ids CONSTANT UUID[] := ARRAY[main_user, main_user, main_user, u3, u4];
    group_ids CONSTANT UUID[] := ARRAY[group_id, group_id, NULL, NULL, NULL];

BEGIN 
    -- Insert APP_USER data
    INSERT INTO APP_USER (
        id, register_token, first_name, last_name, birth_date, weight, height, gender, blood_group, health_condition, drug_allergy, food_allergy, streak
    ) VALUES 
        (u1, 'token111', 'Rungruedee', 'Khajornkiat', '1995-05-12', 45.0, 172, 'M', 'A+', 'None', 'Penicillin', 'Seafood', 3),
        (u2, 'token222', 'Surasak', 'Assamasathitskul', '1990-05-15', 78.0, 180, 'M', 'O-', 'Asthma', 'Peanuts', 'None', 5),
        (u3, 'token333', 'Pimchanok', 'Assamasathitskul', '1988-09-23', 40.0, 165, 'F', 'B+', 'Diabetes', 'None', 'Milk', 2),
        (u4, 'token444', 'Jarnmorm', 'Jommarn', '1992-11-30', 82.0, 185, 'M', 'AB-', 'Hypertension', 'Sulfa Drugs', 'Gluten', 4);

    -- Insert RELATIONSHIP_REQUESTED data
    INSERT INTO RELATIONSHIP_REQUESTED (id, user_id, relative_id, create_at) VALUES
        (gen_random_uuid(), main_user, u1, '2025-01-19 08:45:00'),
        (gen_random_uuid(), u2, main_user, '2025-01-19 08:45:00');

    -- Insert RELATIONSHIP data
    INSERT INTO RELATIONSHIP (id, user_id, relative_id, relation, notifiable, readable, create_at) VALUES
        (gen_random_uuid(), main_user, u3, 'friend', TRUE, TRUE, '2025-01-19 08:45:00'),
        (gen_random_uuid(), u3, main_user, 'friend', TRUE, TRUE, '2025-01-19 08:45:00'),
        (gen_random_uuid(), main_user, u4, 'friend', TRUE, TRUE, '2025-01-19 08:45:00'),
        (gen_random_uuid(), u4, main_user, 'friend', TRUE, TRUE, '2025-01-19 08:45:00');

    -- Insert main_user's drugs
    INSERT INTO DRUG (id, user_id, generic_name, dosage_form, unit, strength, amount, dose, taken_amount, usage_time, is_internal_drug) VALUES
        (drug_id_1, main_user, 'Naltrexone', 'Tablet', 'mg', '500mg', 60, 1.0, 31, 1, FALSE),
        (drug_id_2, main_user, 'Jardiance', 'Tablet', 'mg', '500mg', 60, 1.0, 31, 1, FALSE),
        (drug_id_3, main_user, 'Cyclobenzaprine', 'Tablet', 'mg', '500mg', 60, 1.0, 31, 1, FALSE);

    -- Insert user friends' drugs
    INSERT INTO DRUG (id, user_id, generic_name, dosage_form, unit, strength, amount, dose, taken_amount, usage_time, is_internal_drug) VALUES
        (u3_drug_id, u3, 'Doxycycline', 'Tablet', 'mg', '500mg', 60, 1.0, 31, 1, FALSE),
        (u4_drug_id, u4, 'Sublocade', 'Tablet', 'mg', '500mg', 60, 1.0, 31, 1, FALSE);

    -- Insert main_user's drug group
    INSERT INTO DRUG_GROUP (id, user_id, group_name) VALUES 
        (group_id, main_user, 'Painkillers');
    INSERT INTO DRUG_GROUP_DRUG (id, group_id, drug_id) VALUES
        (gen_random_uuid(), group_id, drug_id_1),
        (gen_random_uuid(), group_id, drug_id_2);

    -- Insert main_user's appointments
    INSERT INTO APPOINTMENT (id, user_id, title, medic_name, "datetime", destination, remark) VALUES
        (gen_random_uuid(), main_user, 'Annual Checkup', 'Dr. Saranaphop', next_five_months, 'Hospital', 'Annual checkup for health status'),
        (gen_random_uuid(), main_user, 'Follow up tomcin', 'Dr. Putthipong', tomorrow, 'Hospital', 'Follow up for tomcin from last year');
    
    -- Insert SCHEDULE data for main_user
    INSERT INTO SCHEDULE (id, user_id, schedule_time, type, name, reference_id, is_enabled) VALUES 
        (gen_random_uuid(), main_user, DATE_TRUNC('minute',next_1_minutes), 1, 'Painkillers', group_id, TRUE),
        (gen_random_uuid(), main_user, DATE_TRUNC('minute',next_1_minutes), 0, 'Morning Dose', drug_id_1, FALSE),
        (gen_random_uuid(), main_user, DATE_TRUNC('minute',next_1_minutes), 0, 'Morning Dose', drug_id_2, FALSE),
        (gen_random_uuid(), main_user, DATE_TRUNC('minute',next_1_minutes), 0, 'Morning Dose', drug_id_3, TRUE);

    -- Insert SCHEDULE data for user friends
    INSERT INTO SCHEDULE (id, user_id, schedule_time, type, name, reference_id, is_enabled) VALUES 
        (gen_random_uuid(), u3, DATE_TRUNC('minute',next_1_minutes), 0, 'Doxycycline', u3_drug_id, TRUE),
        (gen_random_uuid(), u4, DATE_TRUNC('minute',next_1_minutes), 0, 'Sublocade', u4_drug_id, FALSE);
    
    -- Loop through user drugs and create history records
    FOR i IN 1..array_length(user_drugs, 1) LOOP
    		c_time := start_date;
        WHILE c_time <= end_date LOOP
            status_value := status_values[floor(random() * 3) + 1];
            count_value := CASE 
                            WHEN status_value = 'missed' THEN 3 
                            ELSE 2 
                          END;

            notified_at := c_time::date + (next_1_minutes)::time;
            taken_at := CASE
                            WHEN status_value = 'missed' THEN NULL
                            ELSE notified_at + INTERVAL '20 minute'
                        END;
            INSERT INTO HISTORY (id, user_id, drug_id, group_id, status, taken_at, notified_at, count)
            VALUES (
                gen_random_uuid(),
                user_ids[i], 
                user_drugs[i], 
                group_ids[i], 
                status_value,
                taken_at,
                notified_at,
                count_value
            );

            c_time := c_time + interval_step;
        END LOOP;
    END LOOP;
END $$;