package com.elitecore.sm.rulelookup.validator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;

@Component
public class RuleDataLookUpValidator extends BaseValidator{

	
	@Autowired
	RuleLookupFieldValidator ruleLookupFieldValidator;
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return RuleLookupTableData.class.isAssignableFrom(clazz);
	}
	
	public void validateRuleLookupTable(Object target, Errors errors,String entityName){
		try{
			RuleLookupTableData ruleLookupTableData = (RuleLookupTableData)target;
						
			setErrorObject(errors, false, importErrorList);
			isValidate(SystemParametersConstant.RULE_LOOKUP_TABLE_NAME,ruleLookupTableData.getViewName(),"viewName",entityName,ruleLookupTableData.getViewName(),false);
			
			List<LookupFieldDetailData> fieldList = ruleLookupTableData.getLookUpFieldDetailData();
			List<LookupFieldDetailData> allfields = new LinkedList<>();
			allfields.addAll( fieldList );
			
			Iterator<LookupFieldDetailData> itr = fieldList.iterator();
			int i=0;
			while (itr.hasNext()) {
				++i;
				LookupFieldDetailData fields = itr.next();
				
				isValidate(SystemParametersConstant.RULE_LOOKUP_DISPLAY_NAME, fields.getDisplayName(), "lookUpFieldDetailData["+ i +"]" + "displayName" + i , "displayName", entityName, false);
				
				if(isValidate(SystemParametersConstant.RULE_LOOKUP_FIELD_NAME, fields.getViewFieldName()  , "lookUpFieldDetailData["+ i +"]" + "fieldName" + i ,entityName, fields.getViewFieldName() ,false))
				{
				  	for(int j = i ;j < allfields.size() ; j++){
						
						LookupFieldDetailData otherfield = allfields.get( j );
						if(fields.getViewFieldName().equalsIgnoreCase( otherfield.getViewFieldName() )){
							 errors.rejectValue( "lookUpFieldDetailData["+ i +"]" + "fieldName" + i ,  "lookUpFieldDetailData["+ i +"]" + "fieldName" + i  ,
									 getMessage("LookupFieldDetailData.fieldName.duplicate") );							 
						}
					  }
				}
			}
			
			/**
			isValidate(SystemParametersConstant.RULE_LOOKUP_TABLE_NAME,ruleLookupTableData.getViewName(),"viewName",entityName,ruleLookupTableData.getViewName(),false);
			isValidate(SystemParametersConstant.RULE_LOOKUP_TABLE_DESCRIPTION,ruleLookupTableData.getDescription(),"description",entityName,ruleLookupTableData.getDescription(),false);
			**/
		}catch(Exception e){
			errors.reject("Could not add without fields");
			logger.debug("Cannot cast to RuleLookupTable");
			logger.trace(e);
		}
	}
	
}
