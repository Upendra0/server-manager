 
  DECLARE
  
   c_rule_id  TBLTPOLICYRULEACTION_HIS.ruleid%type; 
   c_actionid  TBLTPOLICYRULEACTION_HIS.actionid%type;  

   CURSOR c_tbltpolicyruleaction IS SELECT ruleid, actionid FROM TBLTPOLICYRULEACTION_HIS  order by ruleid asc;

   c_app_order number(10);
   c_prev number(10);	
   c_curr number(10);
   
BEGIN 

   c_app_order :=0;
   c_prev :=0;	
   c_curr :=0;

   OPEN c_tbltpolicyruleaction ;

   LOOP 

   FETCH c_tbltpolicyruleaction into c_rule_id , c_actionid ; 
      EXIT WHEN c_tbltpolicyruleaction%notfound;

	   c_curr := c_rule_id;

	IF c_prev !=  c_curr THEN
	   c_app_order :=1;
	  c_prev := c_curr;
	ELSE 
	 c_app_order := c_app_order + 1;
	END IF;

	insert into tbltpolicyruleactionrel
	( ID , ruleid , actionid , APPLICATIONORDER ) 
	values ( SEQ_POLICYRULEACTIONREL.nextval , c_rule_id , c_actionid , c_app_order );

	commit;
       
   END LOOP; 

   CLOSE c_tbltpolicyruleaction ;
   
   END ;