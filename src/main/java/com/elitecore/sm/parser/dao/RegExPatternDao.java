package com.elitecore.sm.parser.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.RegExPattern;

public interface RegExPatternDao extends GenericDAO<RegExPattern>{
	
	List<RegExPattern> getRegExPatternListByMappingId(int parserMappingId);
	
	public int getRegexPatternCount(String patternName);
	
	public List<RegExPattern> getPatternListByName(String patternName);

}
