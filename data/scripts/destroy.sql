SET session_replication_role = replica;

DO $$ 
DECLARE
    r RECORD;
BEGIN
    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
        EXECUTE 'TRUNCATE TABLE public.' || r.tablename || ' CASCADE';
    END LOOP;
END $$;

SET session_replication_role = origin;