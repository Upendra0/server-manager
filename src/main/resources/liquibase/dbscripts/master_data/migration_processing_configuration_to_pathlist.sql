DECLARE
    C_SERVICEID                     TBLTPATHLIST.SERVICEID%TYPE; 
    C_ACROSSFILEDUPCDRCACHELIMIT    TBLTPATHLIST.ACROSSFILEDUPCDRCACHELIMIT%TYPE; 
    C_ACROSSFILEDUPLICATEDATEFIELD  TBLTPATHLIST.ACROSSFILEDUPLICATEDATEFIELD%TYPE;  
    C_ACROSSFILEDUPDATEINTERVAL     TBLTPATHLIST.ACROSSFILEDUPDATEINTERVAL%TYPE;  
    C_ACROSSFILEDUPLDATEINTYPE      TBLTPATHLIST.ACROSSFILEDUPLDATEINTYPE%TYPE;  
    C_ALERTDESCRIPTION              TBLTPATHLIST.ALERTDESCRIPTION%TYPE;  
    C_ALERTID                       TBLTPATHLIST.ALERTID%TYPE;  
    C_DUPLICATERECORDPOLICYENABLE   TBLTPATHLIST.DUPLICATERECORDPOLICYENABLE%TYPE;  
    C_DUPLICATERECORDPOLICYTYPE     TBLTPATHLIST.DUPLICATERECORDPOLICYTYPE%TYPE;  
    C_UNIFIEDFILEDS                 TBLTPATHLIST.UNIFIEDFILEDS%TYPE;  
    
   CURSOR C_TBLTPROCESSINGSERVICE IS 
   SELECT SERVICEID, ACROSSFILEDUPCDRCACHELIMIT, ACROSSFILEDUPLICATEDATEFIELD, ACROSSFILEDUPDATEINTERVAL,
            ACROSSFILEDUPLDATEINTYPE, ALERTDESCRIPTION, ALERTID, 
            DUPLICATERECORDPOLICYENABLE, DUPLICATERECORDPOLICYTYPE, UNIFIEDFILEDS
   FROM TBLTPROCESSINGSERVICE 
   WHERE SERVICEID IN (SELECT ID FROM TBLTSERVICE WHERE STATUS <> 'DELETED' 
   AND SVCYPEID = (SELECT ID FROM TBLMSERVICETYPE WHERE ALIAS='PROCESSING_SERVICE'));
BEGIN 

   OPEN C_TBLTPROCESSINGSERVICE ;
   	LOOP 
	    FETCH C_TBLTPROCESSINGSERVICE INTO C_SERVICEID, C_ACROSSFILEDUPCDRCACHELIMIT , C_ACROSSFILEDUPLICATEDATEFIELD, C_ACROSSFILEDUPDATEINTERVAL,
	        C_ACROSSFILEDUPLDATEINTYPE, C_ALERTDESCRIPTION, C_ALERTID, C_DUPLICATERECORDPOLICYENABLE, C_DUPLICATERECORDPOLICYTYPE, C_UNIFIEDFILEDS; 
	    EXIT WHEN C_TBLTPROCESSINGSERVICE%NOTFOUND;

    		UPDATE TBLTPATHLIST SET ACROSSFILEDUPCDRCACHELIMIT=C_ACROSSFILEDUPCDRCACHELIMIT,
                       ACROSSFILEDUPLICATEDATEFIELD=C_ACROSSFILEDUPLICATEDATEFIELD,
                       ACROSSFILEDUPDATEINTERVAL=C_ACROSSFILEDUPDATEINTERVAL,
                       ACROSSFILEDUPLDATEINTYPE=C_ACROSSFILEDUPLDATEINTYPE,
                       ALERTDESCRIPTION=C_ALERTDESCRIPTION,
                       ALERTID=C_ALERTID,
                       DUPLICATERECORDPOLICYENABLE=C_DUPLICATERECORDPOLICYENABLE,
                       DUPLICATERECORDPOLICYTYPE=C_DUPLICATERECORDPOLICYTYPE,
                       UNIFIEDFILEDS=C_UNIFIEDFILEDS 
            WHERE SERVICEID = C_SERVICEID;		   
   	END LOOP; 

   COMMIT;
   
   CLOSE  C_TBLTPROCESSINGSERVICE; 
   
END;