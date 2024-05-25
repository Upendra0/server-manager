

DECLARE

  c_rule_id  tbltpolicyrulecondition_his.ruleid%type; 
   c_conditionid  tbltpolicyrulecondition_his.conditionid%type;  

   CURSOR c_tbltpolicyrulecondition IS SELECT ruleid, conditionid FROM tbltpolicyrulecondition_his  order by ruleid asc;

   c_app_order number(10);
   c_prev number(10);	
   c_curr number(10);
   
BEGIN 

   c_app_order :=0;
   c_prev :=0;	
   c_curr :=0;

   OPEN c_tbltpolicyrulecondition ;

   LOOP 

   FETCH c_tbltpolicyrulecondition into c_rule_id , c_conditionid ; 
      EXIT WHEN c_tbltpolicyrulecondition%notfound;

	   c_curr := c_rule_id;

	IF c_prev !=  c_curr THEN
	   c_app_order :=1;
	  c_prev := c_curr;
	ELSE 
	 c_app_order := c_app_order + 1;
	END IF;

	insert into tbltpolicyruleconditionrel 
	( ID , ruleid , conditionid , APPLICATIONORDER ) 
	values ( SEQ_POLICYRULECONDITIONREL.nextval , c_rule_id , c_conditionid , c_app_order );

	commit;
    
   END LOOP; 

   CLOSE  c_tbltpolicyrulecondition ; 
   
END ;

