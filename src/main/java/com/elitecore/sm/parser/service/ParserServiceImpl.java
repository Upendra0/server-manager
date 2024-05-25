/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.dictionarymanager.dao.DictionaryConfigDao;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.parser.dao.ParserDao;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.PluginTypeDao;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.dao.PartitionParamDao;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value="parserService")
public class ParserServiceImpl  implements ParserService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	@Qualifier(value="PluginTypeDao")
	private PluginTypeDao plugintypeDao;

	@Autowired
	@Qualifier(value="parserDao")
	private ParserDao  parserDao;
	
	@Autowired
	private PartitionParamDao partitionDao;
	
	@Autowired
	private PathListDao pathListDao;
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired
	ParserMappingService parserMappingService;
	
	@Autowired
	DictionaryConfigDao dictionaryConfigDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	@Inject
	private PathListHistoryService pathListHistoryService;

	private String failureLoggerMsg = "Failed to get parser plugin master for id ";
	
	
	/**
	 * Add Plugin type
	 */
	@Override
	@Transactional
	public ResponseObject addPlugintype(PluginTypeMaster pluginType){
		ResponseObject responseObject=new ResponseObject();
		logger.debug ( " inserting plugin master type ");
		plugintypeDao.save(pluginType);
		if(pluginType.getId()!=0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_ADD_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_ADD_FAIL);
		}
		return responseObject;
	}
	
	/**
	 * Fetch plugin type list 
	 */
	@Override
	@Transactional(readOnly=true)
	public List<PluginTypeMaster> getPluginTypeList(String pluginCategory){
		
		List<PluginTypeMaster> liPluginType = null;
		liPluginType = plugintypeDao.getEnablePluginTypeList(pluginCategory);
		return liPluginType;
	}
	

	
	/**
	 * Check parser is exist with same name or not , in case of add  
	 * @param parserName
	 * @return boolean true/false if parser with same name available or not
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean isParserUnique(String parserName){
		
		List<Parser> parserList=parserDao.getParserByName(parserName);
		boolean isUnique=false;
		if(parserList!=null && !parserList.isEmpty()){
			isUnique=false;
		}else { // No pathList found with same name 
			isUnique=true;
		}
		return isUnique;
	}
	
	/**
	 * Check parser is exist with same name or not , in case of update 
	 * @param parserId
	 * @param parserName
	 * @return boolean true/false if parser with same name available or not
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean  isParserUniqueForUpdate(int parserId,String parserName){
		
		List<Parser> parserList=parserDao.getParserByName(parserName);
		boolean isUnique=false;
		if(parserList!=null && !parserList.isEmpty()){
			
			for(Parser parser:parserList){
				//If ID is same , then it is same parser object
				if(parserId == parser.getId()){
					isUnique=true;
				}else{ // It is another parser object , but name is same
					isUnique=false;
				}
			}
		}else if(parserList!=null && parserList.isEmpty()){ // No pathList found with same name 
			isUnique=true;
		}
		return isUnique;
	}
	
	/**
	 * Get count of parser for service
	 * @param serviceId
	 * @return Total parser count for service
	 */
	@Override
	@Transactional(readOnly=true)
	public long getTotalParserCount(String serviceId){
		
		String query ="select par.id from Parser as par left join par.parsingPathList as pathlist"
		+ " left join  pathlist.service as ser where ser.id="+serviceId + " and par.status='"+StateEnum.ACTIVE+"'";
		
		return parserDao.getQueryCountUsingHql(query);
	}
	
	/**
	 * Get count of parser for service
	 * @param serviceId
	 * @return Total parser count for service
	 */
	@Override
	@Transactional(readOnly=true)
	public long getTotalParserCount(){
		
		String query ="select par.id from Parser as par left join par.parsingPathList as pathlist where par.status='"+StateEnum.ACTIVE+"'";
		
		return parserDao.getQueryCountUsingHql(query);
	}
	
	/**
	 * Get paginated parser list for service using Hql query
	 * @param serviceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List of paginated parser for service
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Object[]> getPaginatedParserList(String serviceId, int startIndex, int limit, String sidx, String sord){
				
		String query ="select par.id,par.name,par.parserType.type as type,par.parserType.alias as alias from Parser as par left join par.parsingPathList as pathlist"
		+ " left join  pathlist.service as ser where ser.id="+serviceId + " and par.status='"+StateEnum.ACTIVE+"' order by par."+sidx+ " " + sord;
		
		return parserDao.getPaginatedList(query, startIndex, limit, sidx, sord);
	}
	
	/**
	 * Get paginated parser list for service using Hql query
	 * @param serviceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List of paginated parser for service
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Object[]> getPaginatedParserList(int startIndex, int limit, String sidx, String sord){
				
		String query ="select par.id,par.name,par.parserType.type as type,par.parserType.alias as alias from Parser as par left join par.parsingPathList as pathlist where par.status='"+StateEnum.ACTIVE+"' order by par."+sidx+ " " + sord;
		
		return parserDao.getPaginatedList(query, startIndex, limit, sidx, sord);
	}
	
	/**
	 * Total partition param count for service 
	 * @param serviceId
	 * @return Total count of partition param 
	 */
	@Override
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public long getTotalHashParamCount(String serviceId){
		
		Map<String, Object> conditionsAndAliases = new HashMap<>();

		HashMap<String, String> aliases = new HashMap<>();

		List<Criterion> conditions = new ArrayList<>();

		aliases.put("parsingService", "service");
		conditions.add(Restrictions.eq("service.id",Integer.parseInt(serviceId)));		
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		
		conditionsAndAliases.put("aliases", aliases);
		conditionsAndAliases.put("conditions", conditions);
		
		return (long) partitionDao.getQueryCount(PartitionParam.class,  (List<Criterion>) conditionsAndAliases.get("conditions"),
				(HashMap<String, String>) conditionsAndAliases.get("aliases"));
	}
	
	/**
	 * Return paginated partition param list for service
	 * @param serviceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List of partition param for service
	 */ 
	@Override
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public List<PartitionParam> getPaginatedHashConfigList(String serviceId, int startIndex, int limit, String sidx, String sord){
				
		Map<String, Object> conditionsAndAliases = new HashMap<>();

		HashMap<String, String> aliases = new HashMap<>();

		List<Criterion> conditions = new ArrayList<>();

		aliases.put("parsingService", "service");
		conditions.add(Restrictions.eq("service.id",Integer.parseInt(serviceId)));		
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		
		conditionsAndAliases.put("aliases", aliases);
		conditionsAndAliases.put("conditions", conditions);
		
		return partitionDao.getPaginatedList(PartitionParam.class,  (List<Criterion>) conditionsAndAliases.get("conditions"),
				(HashMap<String, String>) conditionsAndAliases.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	/**
	 * Add parser in database
	 * @param parser
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_PARSER, actionType = BaseConstants.CREATE_ACTION, currentEntity = Parser.class, ignorePropList= "")
	public ResponseObject createParser(Parser parser) {

		ResponseObject responseObject = new ResponseObject();

		if(parserDao.getParserCountByPathList(parser.getParsingPathList().getId()) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_EXISTS_FOR_PATHLIST);
			return responseObject;
		}
		//[MED-4519] : Allowing Same plugin name across service instance
		//if (isParserUnique(parser.getName())) {
			if (parser.getParserType().getId() > 0) {
				PluginTypeMaster pluginMaster = (PluginTypeMaster) MapCache.loadMasterEntityById(parser.getParserType().getId(), SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
				if (pluginMaster != null) {
					parser.setParserType(pluginMaster);
					ParsingPathList pathList = (ParsingPathList) pathListDao.findByPrimaryKey(PathList.class, parser.getParsingPathList().getId());
					pathList.setWriteFilePath(parser.getWriteFilePath());
					pathList.setFileNamePattern(parser.getFileNamePattern());
					pathList.setReadFilenamePrefix(parser.getReadFilenamePrefix());
					pathList.setWriteFilenamePrefix(parser.getWriteFilenamePrefix());
					pathList.setReadFilenameSuffix(parser.getReadFilenameSuffix());
					pathList.setReadFilenameContains(parser.getReadFilenameContains());
					pathList.setReadFilenameExcludeTypes(parser.getReadFilenameExcludeTypes());
					pathList.setCompressInFileEnabled(parser.isCompressInFileEnabled());
					pathList.setCompressOutFileEnabled(parser.isCompressOutFileEnabled());
					pathList.setWriteFileSplit(parser.isWriteFileSplit());
					pathList.setMaxFileCountAlert(parser.getMaxFileCountAlert());
					pathList.setLastUpdatedDate(new Date());
					pathList.setWriteCdrHeaderFooterEnabled(parser.isWriteCdrHeaderFooterEnabled());
					pathList.setWriteCdrDefaultAttributes(parser.isWriteCdrDefaultAttributes());
					parser.setParsingPathList(pathList);
					parser.setParserMapping(null);
					logger.debug("parser1::"+parser);
					parserDao.save(parser);

					if (parser.getId() != 0) {
						logger.debug("parser2::"+parser);
						responseObject.setObject(setParserJSONObject(parser));
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.PARSER_ADD_SUCCESS);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.PARSER_ADD_FAIL);
					}
				} else {
					logger.info(failureLoggerMsg
							+ parser.getParserType().getId());
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
				}
			} else {
				logger.info(failureLoggerMsg + parser.getParserType().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
			}
		/*} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PARSER_NAME);
		}*/
		return responseObject;
	}
	
	
	/**
	 * Method will convert the parser object to the 
	 * @param parser
	 * @return
	 */
	private JSONObject setParserJSONObject(Parser parser){
		logger.debug("Converting Parser ojbect data to JSON Data .");
		JSONObject jsonData = new JSONObject();
			jsonData.put("id", parser.getId());
			jsonData.put("name", parser.getName());
			jsonData.put("parserType.id", parser.getParserType().getId());
			jsonData.put("pluginType", parser.getParserType().getType());
			jsonData.put("pluginTypeAlias", parser.getParserType().getAlias());
			jsonData.put("fileNamePattern", parser.getFileNamePattern());
			jsonData.put("readFilenamePrefix", parser.getReadFilenamePrefix());
			jsonData.put("readFilenameSuffix", parser.getReadFilenameSuffix());
			jsonData.put("readFilenameContains", parser.getReadFilenameContains());
			jsonData.put("readFilenameExcludeTypes", parser.getReadFilenameExcludeTypes());
			jsonData.put("compressInFileEnabled", parser.isCompressInFileEnabled());
			jsonData.put("compressOutFileEnabled", parser.isCompressOutFileEnabled());
			jsonData.put("writeFilePath", parser.getWriteFilePath());
			jsonData.put("writeFilenamePrefix", parser.getWriteFilenamePrefix());
			jsonData.put("writeFileSplit", parser.isWriteFileSplit());
			jsonData.put("maxFileCountAlert", parser.getMaxFileCountAlert());
			jsonData.put("writeCdrHeaderFooterEnabled", parser.isWriteCdrHeaderFooterEnabled());
			jsonData.put("writeCdrDefaultAttributes", parser.isWriteCdrDefaultAttributes());
			
			if(parser.getParserMapping() != null){
				logger.debug("Mapping found for parser object.");
				jsonData.put("parserMapping.id", parser.getParserMapping().getId());
			}else{
				logger.debug("No mapping found for this plugin so setting zero.");
				jsonData.put("parserMapping.id", 0);
			}
		return jsonData;
	}
	
	/**
	 * Update parser detail in database
	 * @param parser
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Parser.class, ignorePropList= "")
	public ResponseObject updateParser(Parser parser){
		
		ResponseObject responseObject = new ResponseObject();
		
		//[MED-4519] : Allowing Same plugin name across service instance
		//if(isParserUniqueForUpdate(parser.getId(),parser.getName())){
			if(parser.getParserType().getId() > 0 ){
				PluginTypeMaster pluginMaster =   (PluginTypeMaster) MapCache.loadMasterEntityById(parser.getParserType().getId(), SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);  
				if(pluginMaster != null ){
					parser.setParserType(pluginMaster);
					
					ParsingPathList pathList = (ParsingPathList) pathListDao.findByPrimaryKey(PathList.class, parser.getParsingPathList().getId());
					pathList.setWriteFilePath(parser.getWriteFilePath());
					pathList.setFileNamePattern(parser.getFileNamePattern());
					pathList.setReadFilenamePrefix(parser.getReadFilenamePrefix());
					pathList.setWriteFilenamePrefix(parser.getWriteFilenamePrefix());
					pathList.setReadFilenameSuffix(parser.getReadFilenameSuffix());
					pathList.setReadFilenameContains(parser.getReadFilenameContains());
					pathList.setReadFilenameExcludeTypes(parser.getReadFilenameExcludeTypes());
					pathList.setCompressInFileEnabled(parser.isCompressInFileEnabled());
					pathList.setCompressOutFileEnabled(parser.isCompressOutFileEnabled());
					pathList.setWriteFileSplit(parser.isWriteFileSplit());
					pathList.setMaxFileCountAlert(parser.getMaxFileCountAlert());
					pathList.setLastUpdatedDate(new Date());
					pathList.setLastUpdatedByStaffId(parser.getLastUpdatedByStaffId());
					pathList.setWriteCdrHeaderFooterEnabled(parser.isWriteCdrHeaderFooterEnabled());
					pathList.setWriteCdrDefaultAttributes(parser.isWriteCdrDefaultAttributes());
					parser.setParsingPathList(pathList);
					
					if(parser.getParserMapping()!=null){
						if(parser.getParserMapping().getId() != 0){
							ParserMapping parserMapping=parserMappingDao.findByPrimaryKey(ParserMapping.class,parser.getParserMapping().getId());
							parser.setParserMapping(parserMapping);
						}else{
							parser.setParserMapping(null);
						}
					}
					parserDao.merge(parser);
					
					if(parser.getId() != 0){
						responseObject.setObject(setParserJSONObject(parser));
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_SUCCESS);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
					}
				}else{
					logger.info(failureLoggerMsg + parser.getParserType().getId());
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
				}
			}else{
				logger.info(failureLoggerMsg + parser.getParserType().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
			}
		/*} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PARSER_NAME);
		}*/
		return responseObject;
	}
	
	/**
	 * Delete parser from database
	 * @param parserIdList
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject deleteParserList(String parserIdList) {
		ResponseObject responseObject = new ResponseObject();
		String[] idArray = parserIdList.split(",");
		ParserService parserServiceImpl = (ParserService) SpringApplicationContext.getBean("parserService");
		for (int index = 0; index < idArray.length; index++) {
			parserServiceImpl.deleteParser(Integer.parseInt(idArray[index]));
		}
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_DELETE_SUCCESS);

		return responseObject;
	}

	/* (non-Javadoc)
	 * @see com.elitecore.sm.parser.service.ParserService#deleteParser(int)
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_PARSER,actionType = BaseConstants.DELETE_ACTION , currentEntity = Parser.class, ignorePropList= "")
	public ResponseObject deleteParser(int parserId) {
		ResponseObject responseObject = new ResponseObject();
		Parser parser = parserDao.findByPrimaryKey(Parser.class, parserId);
		if (parser != null) {
			parser.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,parser.getName()));
			parser.setStatus(StateEnum.DELETED);
			parser.setLastUpdatedDate(new Date());
			parserDao.merge(parser);
		}
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_DELETE_SUCCESS);
		return  responseObject;
	}
	
	
	/**
	 * Method will get parser object by parser id.
	 * @param parserId
	 * @return Parser 
	 */
	@Transactional(readOnly = true)
	@Override
	public Parser getParserById(int parserId) {
		return parserDao.findByPrimaryKey(Parser.class, parserId);
	}

	/**
	 * Method will get Plugin type master by type.
	 * @param parserType
	 * @return ResponseObject
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getPluginByType(String pluginType) {

		ResponseObject responseObject = new ResponseObject();
		PluginTypeMaster pluginTypeMaster =	plugintypeDao.getPluginByType(pluginType);
		if(pluginTypeMaster != null){
			responseObject.setSuccess(true);
			responseObject.setObject(pluginTypeMaster);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
				
	}
	
	/**
	 * Method will associate parser with device mapping. 
	 * @param parser
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject updateParserMapping(Parser parser) {

		ResponseObject responseObject = new ResponseObject();
		
		if(parser != null ){
			
			parserDao.merge(parser);
			
			if(parser.getId() != 0){
				responseObject.setObject(parser);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PARSER_UPDATE_FAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PARSER_NAME);
		}
		
		return responseObject;
	}

	/**
	 * Method will get parser and its associated mapping details by parser id.
	 * @param parserId
	 * @return List<Object[]>
	 */
	@Transactional(readOnly = true)
	@Override
	public Parser getParserMappingDetailsByParserId(int parserId) {
		
		if(parserId > 0){
			Parser parser = parserDao.findByPrimaryKey(Parser.class, parserId);
			if(parser != null && parser.getParserMapping() != null){
				parser.getParserMapping().getId();
				parser.getParserMapping().getDevice().getId();
				parser.getParserMapping().getDevice().getDeviceType().getId();
				parser.getParserMapping().getDevice().getVendorType().getId();
				
				return parser;
			}else{
				return null;
			}
		}
		return null;
	}

	/**
	 * Method will import or delete parser details.
	 * @param parsingPathList
	 * @param
	 * @param
	 */
	@Transactional
	@Override
	public ResponseObject importOrDeleteParserDetails(ParsingPathList parsingPathList,boolean isImport) {
			logger.debug("Import or delete parser details for pathlist " + parsingPathList.getName());
			ResponseObject responseObject = new ResponseObject();
			
			List<Parser> parserList = parsingPathList.getParserWrappers();
			if(parserList != null && !parserList.isEmpty()){
				Parser parser ;
				for (int i = 0; i < parserList.size(); i++) {
					parser = parserList.get(i);
			
					if (StateEnum.ACTIVE.equals(parser.getStatus())) {
						if (isImport) {
							logger.debug("Import parser plugin. ");
							parser.setId(0);
							parser.setParsingPathList(parsingPathList);
							parser.setLastUpdatedByStaffId(parsingPathList.getCreatedByStaffId() );
							parser.setLastUpdatedDate(new Date());
							parser.setParserMapping(null);
							parserDao.save(parser);
							
							if(parser.getId() > 0){
							  responseObject.setSuccess(true);
							} else {
							  responseObject.setSuccess(false);
							}
						} else {
							logger.debug("Delete parser details");
							parser.setStatus(StateEnum.DELETED);
							parser.setLastUpdatedByStaffId(parsingPathList.getCreatedByStaffId());
							parser.setLastUpdatedDate(new Date());
							parser.setParserMapping(null);
							parserDao.merge(parser);
							responseObject.setSuccess(true);
						}
					}
				}
			}else{
				logger.debug("No parser is configurated for the pathlist " + parsingPathList.getName());
				responseObject.setSuccess(true);
				responseObject.setObject(parsingPathList);
			}	
		
		return responseObject;
	}

	@Override
	public void importParserForUpdateMode(Parser dbParser, Parser exportedParser) {
		//set basic parameters in parser
		dbParser.setFileNamePattern(exportedParser.getFileNamePattern());
		dbParser.setReadFilenamePrefix(exportedParser.getReadFilenamePrefix());
		dbParser.setReadFilenameSuffix(exportedParser.getReadFilenameSuffix());
		dbParser.setReadFilenameContains(exportedParser.getReadFilenameContains());
		dbParser.setReadFilenameExcludeTypes(exportedParser.getReadFilenameExcludeTypes());
		dbParser.setWriteFilePath(exportedParser.getWriteFilePath());
		dbParser.setCompressInFileEnabled(exportedParser.isCompressInFileEnabled());
		dbParser.setCompressOutFileEnabled(exportedParser.isCompressOutFileEnabled());
		dbParser.setMaxFileCountAlert(exportedParser.getMaxFileCountAlert());
		dbParser.setWriteFileSplit(exportedParser.isWriteFileSplit());
		dbParser.setWriteFilenamePrefix(exportedParser.getWriteFilenamePrefix());
		dbParser.setLastUpdatedDate(EliteUtils.getDateForImport(false));

		//set parser mapping in parser
		ParserMapping dbParserMapping = dbParser.getParserMapping();
		ParserMapping exportedParserMapping = exportedParser.getParserMapping();
		
		if(exportedParserMapping != null && !exportedParserMapping.getStatus().equals(StateEnum.DELETED)) {
			if(dbParserMapping == null) {
				try {
					dbParserMapping = (ParserMapping) exportedParserMapping.clone();
				} catch (CloneNotSupportedException e) {
					logger.error("Clone not supported", e);;
				}
				dbParser.setParserMapping(dbParserMapping);
				dbParserMapping = dbParser.getParserMapping();
			}
			parserMappingService.importParserMappingForUpdateMode(dbParserMapping, exportedParserMapping);
		}
		
	}
	
	@Override
	public void importParserForAddMode(Parser dbParser, Parser exportedParser) {
		//set parser mapping in parser
		ParserMapping dbParserMapping = dbParser.getParserMapping();
		ParserMapping exportedParserMapping = exportedParser.getParserMapping();
		
		if(exportedParserMapping != null && !exportedParserMapping.getStatus().equals(StateEnum.DELETED)) {
			if(dbParserMapping == null) {
				try {
					dbParserMapping = (ParserMapping) exportedParserMapping.clone();
				} catch (CloneNotSupportedException e) {
					logger.error("Clone not supported", e);;
				}
				dbParser.setParserMapping(dbParserMapping);
				dbParserMapping = dbParser.getParserMapping();
			}
			parserMappingService.importParserMappingForAddMode(dbParserMapping, exportedParserMapping);
		}
		
	}
	
	@Override
	public Parser getParserFromList(List<Parser> parserList, String parserAlias, String parserName) {
		if(!CollectionUtils.isEmpty(parserList)) {
			int length = parserList.size();
			for(int i = length-1; i >= 0; i--) {
				Parser parser = parserList.get(i);
				if(parser != null && !parser.getStatus().equals(StateEnum.DELETED)
						&& parser.getParserType().getAlias().equalsIgnoreCase(parserAlias)
						&& parser.getName().equalsIgnoreCase(parserName)) {
					return parserList.remove(i);
				}
			}
		}
		return null;
	}
		
	/**
	 * Clone parser in database to associate it with different path list
	 * @param parser
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CLONE_PARSER, actionType = BaseConstants.CREATE_ACTION, currentEntity = Parser.class, ignorePropList= "")
	public ResponseObject cloneParser(Parser parser, int serviceId, int pathListId) {

		ResponseObject responseObject = new ResponseObject();	
		ParsingPathList pathList = (ParsingPathList) pathListDao.findByPrimaryKey(PathList.class, pathListId);
		List<Parser> pList=pathList.getParserWrappers();
		if(pathList!=null && pList!=null && !pList.isEmpty() && isAnyActiveParserAvailableForPathList(pList)) {	
			
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_EXISTS_FOR_PATHLIST);
			return responseObject;
		}
		if (parser.getParserType().getId() > 0) {
			PluginTypeMaster pluginMaster = (PluginTypeMaster) MapCache.loadMasterEntityById(parser.getParserType().getId(), SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
			if (pluginMaster != null) {
				parser.setParserType(pluginMaster);	
				//[MED-4224] : Same plugin instance with multiple read paths
				//parser.setParserMapping(null);																	
				logger.debug("parser1::"+parser);
				pathList.setWriteFilePath(parser.getWriteFilePath());
				pathList.setFileNamePattern(parser.getFileNamePattern());
				pathList.setReadFilenamePrefix(parser.getReadFilenamePrefix());
				pathList.setWriteFilenamePrefix(parser.getWriteFilenamePrefix());
				pathList.setReadFilenameSuffix(parser.getReadFilenameSuffix());
				pathList.setReadFilenameContains(parser.getReadFilenameContains());
				pathList.setReadFilenameExcludeTypes(parser.getReadFilenameExcludeTypes());
				pathList.setCompressInFileEnabled(parser.isCompressInFileEnabled());
				pathList.setCompressOutFileEnabled(parser.isCompressOutFileEnabled());
				pathList.setWriteFileSplit(parser.isWriteFileSplit());
				pathList.setMaxFileCountAlert(parser.getMaxFileCountAlert());
				pathList.setLastUpdatedDate(new Date());
				pathList.setLastUpdatedByStaffId(parser.getLastUpdatedByStaffId());
				parser.setParsingPathList(pathList);
				parser.setId(0);
				parserDao.evict(parser);
				parserDao.save(parser);
				if (parser.getId() != 0) {
					logger.debug("parser2::"+parser);
					responseObject.setObject(setParserJSONObject(parser));
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.PARSER_ADD_SUCCESS);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PARSER_ADD_FAIL);
				}
			} else {
				logger.info(failureLoggerMsg
						+ parser.getParserType().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
			}
		} else {
			logger.info(failureLoggerMsg + parser.getParserType().getId());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
		}
		if(responseObject.isSuccess()) {
			pathListHistoryService.save(parser.getParsingPathList(), parser);
		}
		return responseObject;
	}
	
	private boolean isAnyActiveParserAvailableForPathList(List<Parser> list){
		Iterator<Parser> it  = list.iterator();
		boolean isActiveParser = false;
		while(it.hasNext()){
			Parser p = it.next();
			if(p!=null && StateEnum.ACTIVE.equals(p.getStatus()))
				isActiveParser = true; break;
		}
		return isActiveParser;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> getDataDefinitionFileList(int serverInstanceId) {
		Set<String> dictionaryFileSet = new HashSet<String>();
		ServerInstance serverInstance = serverInstanceDao.getServerInstance(serverInstanceId);
		Hibernate.initialize(serverInstance.getServer());
		Server server = serverInstance.getServer();
		if(server!=null) {
			List<DictionaryConfig> dictionaryConfigs = dictionaryConfigDao.getCustomDictionaryConfigList(server.getIpAddress(),server.getUtilityPort());
			for(DictionaryConfig config : dictionaryConfigs) {
				dictionaryFileSet.add(config.getPath() + File.separator + config.getFilename());
			}
		}
		return dictionaryFileSet;
	}
}
