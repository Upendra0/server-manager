  
DO $$ DECLARE
     
    c_tbltpolicyruleaction  CURSOR FOR SELECT ruleid, actionid FROM tbltpolicyruleaction_his order by ruleid asc;
    rec_tbltpolicyruleaction RECORD;

   c_app_order integer;
   c_prev integer;	
   c_curr integer;
   policyruleactionrel_id integer;
   
BEGIN 

   c_app_order :=0;
   c_prev :=0;	
   c_curr :=0;
   policyruleactionrel_id := 0;
   
   OPEN c_tbltpolicyruleaction ;

   LOOP 

   FETCH c_tbltpolicyruleaction into rec_tbltpolicyruleaction ;
     EXIT WHEN NOT FOUND;

	   c_curr := rec_tbltpolicyruleaction.ruleid;

	IF c_prev !=  c_curr THEN
	   c_app_order :=1;
	  c_prev := c_curr;
	ELSE 
	 c_app_order := c_app_order + 1;
	END IF;
    policyruleactionrel_id := policyruleactionrel_id + 1;
	insert into tbltpolicyruleactionrel ( ID , ruleid , actionid , APPLICATIONORDER ) 
	values ( policyruleactionrel_id , rec_tbltpolicyruleaction.ruleid , 
             rec_tbltpolicyruleaction.actionid , c_app_order );
    
   END LOOP; 

   CLOSE  c_tbltpolicyruleaction ; 
   
END$$ ;