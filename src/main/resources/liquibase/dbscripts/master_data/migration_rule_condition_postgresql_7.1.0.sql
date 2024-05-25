
DO $$ DECLARE
     
    c_tbltpolicyrulecondition  CURSOR FOR SELECT ruleid, conditionid FROM tbltpolicyrulecondition_his order by ruleid asc;
    rec_tbltpolicyrulecondition RECORD;

   c_app_order integer;
   c_prev integer;	
   c_curr integer;
   policyruleconditionrel_id integer;
   
BEGIN 

   c_app_order :=0;
   c_prev :=0;	
   c_curr :=0;
   policyruleconditionrel_id := 0;
   
   OPEN c_tbltpolicyrulecondition ;

   LOOP 

   FETCH c_tbltpolicyrulecondition into rec_tbltpolicyrulecondition ;
     EXIT WHEN NOT FOUND;

	   c_curr := rec_tbltpolicyrulecondition.ruleid;

	IF c_prev !=  c_curr THEN
	   c_app_order :=1;
	  c_prev := c_curr;
	ELSE 
	 c_app_order := c_app_order + 1;
	END IF;
    policyruleconditionrel_id := policyruleconditionrel_id + 1;
	insert into tbltpolicyruleconditionrel ( ID , ruleid , conditionid , APPLICATIONORDER ) 
	values ( policyruleconditionrel_id , rec_tbltpolicyrulecondition.ruleid , 
             rec_tbltpolicyrulecondition.conditionid , c_app_order );
    
   END LOOP; 

   CLOSE  c_tbltpolicyrulecondition ; 
   
END$$ ;