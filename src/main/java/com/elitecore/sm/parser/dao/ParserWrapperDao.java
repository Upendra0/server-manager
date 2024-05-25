/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.Parser;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ParserWrapperDao extends GenericDAO<Parser> {
	
	public Map<String, Object> getMappingAssociationCount(int mappingId);
}
