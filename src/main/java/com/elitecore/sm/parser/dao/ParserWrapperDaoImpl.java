/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.parser.model.Parser;

/**
 * @author Ranjitsinh Reval
 *
 */

@Repository(value="parserWrapperDao")
public class ParserWrapperDaoImpl extends GenericDAOImpl<Parser> implements ParserWrapperDao {

	
	
	/**
	 * Method will fetch mapping association count by mapping id.
	 * @param mappingId
	 * @return Map<String, Object>
	 */
	@Override
	public Map<String, Object> getMappingAssociationCount(int mappingId) {
		
		Map<String, Object> returnMap = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq("status",StateEnum.ACTIVE));
		conditionList.add(Restrictions.eq("parserMapping.id",mappingId));
		
		returnMap.put("conditions", conditionList);
		return returnMap;
	}



}
