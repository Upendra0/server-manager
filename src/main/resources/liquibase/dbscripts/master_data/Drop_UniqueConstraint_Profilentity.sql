DECLARE 
statement VARCHAR2(2000);
constr_name VARCHAR2(30);
constr_cnt VARCHAR2(30);
BEGIN
constr_cnt:= 0;
  SELECT count(*) INTO constr_cnt 
   FROM USER_CONSTRAINTS 
   WHERE TABLE_NAME='TBLTPROFILENTITY' AND CONSTRAINT_NAME <> 'TBLTPROFILENTITY_COLS' and CONSTRAINT_TYPE='U';
   IF constr_cnt = 1 then
    SELECT CONSTRAINT_NAME INTO constr_name 
       FROM USER_CONSTRAINTS 
       WHERE TABLE_NAME='TBLTPROFILENTITY' AND CONSTRAINT_NAME <> 'TBLTPROFILENTITY_COLS' and CONSTRAINT_TYPE='U';
   	statement := 'ALTER TABLE TBLTPROFILENTITY DROP CONSTRAINT '|| constr_name;
    DBMS_OUTPUT.PUT_LINE('constraint name => ' || constr_name || ' is dropped');
   EXECUTE IMMEDIATE(statement); 
   END IF;
END;