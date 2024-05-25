package com.elitecore.sm.policy.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IDatabaseQueryDao;
import com.elitecore.sm.policy.model.DatabaseQuery;

/**
 * @author Sagar Ghetiya
 *
 */
@Component
public class DatabaseQueryValidator extends BaseValidator
{
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Autowired
	IDatabaseQueryDao databseQueryDao;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return DatabaseQuery.class.isAssignableFrom(clazz);
	}
	
	public void validateDatabaseQuery(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList){
		try{
			DatabaseQuery databaseQuery = (DatabaseQuery) target;
			setErrorObject(errors, validateForImport, importErrorList);
			isValidate(SystemParametersConstant.DATABASE_QUERY_NAME,databaseQuery.getQueryName(),"queryName",entityName,databaseQuery.getQueryName(),validateForImport);
			isValidate(SystemParametersConstant.DATABASE_QUERY_VALUE, databaseQuery.getQueryValue(), "queryValue", entityName,databaseQuery.getQueryValue(),validateForImport);
			isValidate(SystemParametersConstant.DATABASE_QUERY_DESC, databaseQuery.getDescription(), "description", entityName,databaseQuery.getDescription(),validateForImport);
			isValidate(SystemParametersConstant.DATABASE_QUERY_CONDITIONEXPRESSION, databaseQuery.getConditionExpression(),"conditionExpression", entityName, databaseQuery.getConditionExpression(),validateForImport);
		}catch(ClassCastException ce){
			logger.debug("Could not cast to DatabaseQuery");
			logger.trace(ce);
		}
	}

	public void validateNameForUniqueness(DatabaseQuery databaseQuery, Object object,
			List<ImportValidationErrors> importErrorList2, Object object2, boolean b, int serverId) {
		this.importErrorList = importErrorList2;
		long count = databseQueryDao.getDatabaseQueryCountByName(databaseQuery.getQueryName(), serverId);
		if(count > 0){
			isValidate(SystemParametersConstant.POLICY_UNIQUE_FAILED, databaseQuery.getQueryName(), "Name", "Dynamic Query", "", true);
		}
	}
}
