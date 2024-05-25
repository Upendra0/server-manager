/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.parser.dao.ParserWrapperDao;
import com.elitecore.sm.parser.model.Parser;

/**
 * @author Ranjitsing Reval
 *
 */
@Service("parserWrapperService")
public class ParserWrapperServiceImpl implements ParserWrapperService{
	
	
	@Autowired
	@Qualifier(value="parserWrapperDao")
	private ParserWrapperDao parserWrapperDao;
	
	/**
	 * Method will get mapping association count.
	 * @param mappingId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getMappingAssociationCount(int mappingId) {
	
		Map<String, Object> conditionList = parserWrapperDao.getMappingAssociationCount(mappingId);
		return parserWrapperDao.getQueryCount(Parser.class, (List<Criterion>) conditionList.get("conditions"),null);
		
	}

}
