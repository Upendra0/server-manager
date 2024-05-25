/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.Parser;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ParserDao extends GenericDAO<Parser>{

	
	public NATFlowParserMapping getNatFlowParserById(int id);
	
	public List<Parser> getParserByName(String name);
	
	public int updateNatFlowParserConfiguration(NATFlowParserMapping natFlowParser);
	
	public long getQueryCountUsingHql(String query);
	
	public List<Object[]> getPaginatedList(String query, int startIndex, int limit, String sidx, String sord);

	public long getParserCountByPathList(int pathListId);

	public Parser getParserByPathListId(int id);
	
}
