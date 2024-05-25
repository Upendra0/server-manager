

CREATE PROCEDURE migrate_rule_condition_rel() 
BEGIN 
   DECLARE v_finished INT DEFAULT 0;
   DECLARE c_rule_id  INT ;
   DECLARE c_conditionid  INT;
   DECLARE policyruleconditionrel_id INT DEFAULT 0;
   
   DECLARE c_app_order INT ;
   DECLARE c_prev INT ;	
   DECLARE c_curr INT ;
   
    DECLARE  c_tbltpolicyrulecondition CURSOR FOR SELECT ruleid, conditionid 
   FROM TBLTPOLICYRULECONDITION_HIS ORDER BY RULEID ASC; 
   
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;
    
   SET c_app_order =0;
   SET c_prev =0;	
   SET c_curr =0;
   
   OPEN c_tbltpolicyrulecondition ;   

   read_itr_loop: LOOP 

   FETCH c_tbltpolicyrulecondition into c_rule_id , c_conditionid ;
	IF v_finished = 1 THEN 
		LEAVE read_itr_loop;
	END IF;

	 SET  c_curr = c_rule_id;

	IF c_prev !=  c_curr THEN
	   SET c_app_order =1;
	   SET c_prev = c_curr;
	ELSE 
	 SET c_app_order = c_app_order + 1;
	END IF;

	SET policyruleconditionrel_id = policyruleconditionrel_id + 1;
	insert into TBLTPOLICYRULECONDITIONREL 
	( ID , RULEID , CONDITIONID , APPLICATIONORDER ) 
	values ( policyruleconditionrel_id , c_rule_id , c_conditionid , c_app_order );

	commit;
    
   END LOOP read_itr_loop; 

   CLOSE  c_tbltpolicyrulecondition ; 
   
END ;

