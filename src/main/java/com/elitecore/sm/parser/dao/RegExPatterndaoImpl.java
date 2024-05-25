package com.elitecore.sm.parser.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RegExPattern;

@Repository(value="regExPatternDao")
public class RegExPatterndaoImpl extends GenericDAOImpl<RegExPattern> implements RegExPatternDao{
	
	@Autowired
	ParserMappingDao parserMappingDao;

	/**
	 * Get regex pattern list by mapping id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegExPattern> getRegExPatternListByMappingId(int parserMappingId){
		
		Criteria criteria=getCurrentSession().createCriteria(RegExPattern.class);
		criteria.add(Restrictions.eq("parserMapping.id", parserMappingId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		
		List<RegExPattern> regexPatternList=(List<RegExPattern>)criteria.list();
		
		return  ((regexPatternList !=null) &&  !regexPatternList.isEmpty()) ?  regexPatternList : null;
		
	}
	
	/**
	 * Fetch pattern name count based on name , for check unique pattern name
	 */
	@Override
	public int getRegexPatternCount(String patternName){
				
		Criteria criteria = getCurrentSession().createCriteria(RegExPattern.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		
		if (patternName != null) {
			criteria.add(Restrictions.eq("patternRegExName", patternName));
		} 
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	/**
	 * Fetch pattern count and object by name 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegExPattern> getPatternListByName(String patternName){
		
		Criteria criteria=getCurrentSession().createCriteria(RegExPattern.class);
		criteria.add(Restrictions.eq("patternRegExName", patternName));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		
		return  (List<RegExPattern>)criteria.list();
		
	}
	
	/**
	 * Method will update pattern and mark flag dirty to all associated entities.
	 * @param regexPattern
	 */
	@Override
	public void update (RegExPattern regexPattern){
		getCurrentSession().update(regexPattern);
		ParserMapping parserMapping = regexPattern.getParserMapping();
		parserMapping.getId();
		
		parserMappingDao.merge(parserMapping);
	}
	
	/**
	 * Method will update pattern and mark flag dirty to all associated entities.
	 * @param regexPattern
	 */
	@Override
	public void merge (RegExPattern regexPattern){
		getCurrentSession().merge(regexPattern);
		ParserMapping parserMapping = regexPattern.getParserMapping();
		parserMapping.getId();
		
		parserMappingDao.merge(parserMapping);		
	}
	
	/** Method will save pattern and mark flag dirty to all associated entities.
	 * @param regexPattern
	 */
	@Override
	public void save(RegExPattern regexPattern){
		getCurrentSession().save(regexPattern);
		ParserMapping parserMapping = regexPattern.getParserMapping();
		parserMapping.getId();
		
		parserMappingDao.merge(parserMapping);
		
	}
}
