/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.util.List;
import java.util.Set;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.services.model.PartitionParam;

/**
 * @author elitecore
 *
 */
public interface ParserService {

	public ResponseObject addPlugintype(PluginTypeMaster pluginType);
	
	public List<PluginTypeMaster> getPluginTypeList(String pluginCategory);
	
	public boolean isParserUnique(String parserName);
	
	public boolean isParserUniqueForUpdate(int parserId,String parserName);
	
	public long getTotalParserCount(String serviceId);
	
	public long getTotalParserCount();
	
	public List<Object[]> getPaginatedParserList(String serviceId, int startIndex, int limit, String sidx, String sord);
	
	public List<Object[]> getPaginatedParserList(int startIndex, int limit, String sidx, String sord);
	
	public long getTotalHashParamCount(String serviceId);	
	
	public List<PartitionParam> getPaginatedHashConfigList(String serviceId, int startIndex, int limit, String sidx, String sord);
	
	public ResponseObject createParser(Parser parser);
	
	public ResponseObject updateParser(Parser parser);
	
	public ResponseObject deleteParserList(String parserIdList);
	
	public ResponseObject deleteParser(int parserId);
	
	public Parser getParserById(int parserId);
	
	public ResponseObject getPluginByType(String type);
	
	public ResponseObject updateParserMapping(Parser parser);
	
	public Parser getParserMappingDetailsByParserId(int parserId);
	
	public ResponseObject importOrDeleteParserDetails(ParsingPathList parsingPathList, boolean isImport);	
	
	public void importParserForUpdateMode(Parser dbParser, Parser exportedParser);

	public ResponseObject cloneParser(Parser parserById,int serviceID,int pathListID);
	
	public Parser getParserFromList(List<Parser> parserList, String parserAlias, String parserName);
	
	public void importParserForAddMode(Parser dbParser, Parser exportedParser);
	
	public Set<String> getDataDefinitionFileList(int serverInstanceId);
	
}