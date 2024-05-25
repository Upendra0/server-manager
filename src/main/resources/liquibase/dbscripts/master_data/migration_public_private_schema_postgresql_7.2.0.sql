DO $$ DECLARE     
   public_tables_list record;
   public_sequence_list record;     
   public_view_list record;
   private_schema_name text;
   temp_sql text;
BEGIN 
	SELECT schema_name INTO private_schema_name FROM information_schema.schemata WHERE schema_name LIKE 'crestelsm%';	
	raise notice ' Migration Schema Name %', private_schema_name;
	
	--Migrating Tables
	FOR public_tables_list IN SELECT tablename FROM pg_tables WHERE schemaname = 'public'
	LOOP
		CASE public_tables_list.tablename
	 	WHEN 'databasechangeloglock' THEN
	    	temp_sql := 'CREATE TABLE ' || private_schema_name || '.databasechangeloglock AS SELECT * FROM public.databasechangeloglock;';
	    	EXECUTE temp_sql;
	    	temp_sql := 'ALTER TABLE ' || private_schema_name || '.databasechangeloglock OWNER TO ' || private_schema_name ||';';
	    	EXECUTE temp_sql;
	    	temp_sql := 'UPDATE '|| private_schema_name || '.databasechangeloglock SET locked =''false'';';
	    	EXECUTE temp_sql;
	 	WHEN 'databasechangelog' THEN
	    	temp_sql := 'CREATE TABLE '|| private_schema_name || '.databasechangelog AS SELECT * FROM public.databasechangelog;';
	    	EXECUTE temp_sql;
	    	temp_sql := 'ALTER TABLE '|| private_schema_name || '.databasechangelog  OWNER TO ' || private_schema_name || ';';
	    	EXECUTE temp_sql;	    	
	 	ELSE
	    	temp_sql := 'ALTER TABLE public.' || quote_ident(public_tables_list.tablename) || ' SET SCHEMA ' || private_schema_name || ';';
	    	EXECUTE temp_sql;
			temp_sql := 'ALTER TABLE ' || private_schema_name || '.' || quote_ident(public_tables_list.tablename) || ' OWNER TO ' || private_schema_name || ';';
			EXECUTE temp_sql;
	 	END CASE;
 	END LOOP;
    --Migrating Views
 	FOR public_view_list IN SELECT viewname FROM pg_catalog.pg_views WHERE schemaname NOT IN ('pg_catalog', 'information_schema') AND schemaname = 'public'
    LOOP
        temp_sql := 'ALTER VIEW public.' || quote_ident(public_view_list.viewname) || ' SET SCHEMA ' || private_schema_name || ';';		
		EXECUTE temp_sql;
		temp_sql := 'ALTER VIEW ' || private_schema_name || '.' || quote_ident(public_view_list.viewname) || ' OWNER TO ' || private_schema_name || ';';
		EXECUTE temp_sql;
    END LOOP;
 	--Migrating Sequences
    FOR public_sequence_list IN SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema='public'
    LOOP
        temp_sql := 'ALTER SEQUENCE public.' || quote_ident(public_sequence_list.sequence_name) || ' SET SCHEMA ' || private_schema_name || ';';		
		EXECUTE temp_sql;
		temp_sql := 'ALTER SEQUENCE ' || private_schema_name || '.' || quote_ident(public_sequence_list.sequence_name) || ' OWNER TO ' || private_schema_name || ';';
		EXECUTE temp_sql;
    END LOOP;

END$$ ;