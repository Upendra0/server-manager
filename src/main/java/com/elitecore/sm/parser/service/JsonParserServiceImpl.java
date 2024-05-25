package com.elitecore.sm.parser.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.model.JsonParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author sanket.bhalodia
 *
 */
@Service
public class JsonParserServiceImpl implements JsonParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	/**
	 * get json parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getJsonParserMappingById(int jsonParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		JsonParserMapping jsonParserMapping=parserMappingDao.getJsonParserMappingById(jsonParserMappingId);
		
		if(jsonParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(jsonParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update json parser mapping detail 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = JsonParserMapping.class, ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device,fileTypeEnum,fileFooterParser,recordHeaderEnable,recordHeaderSeparator,fileHeaderContainsFields,fieldSeparator,srcDateFormat,recordHeaderIdentifier,excludeCharactersMin,excludeCharactersMax,excludeLinesStart,keyValueSeparator,find,replace,linearKeyValueRecordEnable,recordHeaderLength")	
	public ResponseObject updateJsonParserMapping(JsonParserMapping jsonParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		JsonParserMapping jsonParserMapping = (JsonParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, jsonParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(jsonParser.getParserType().getAlias())){
				jsonParserMapping.setParserType(pluginType);
			}
		}
		
		jsonParserMapping.setSrcCharSetName(jsonParser.getSrcCharSetName());
		jsonParserMapping.setSrcDateFormat(jsonParser.getSrcDateFormat());
		jsonParserMapping.setDateFormat(jsonParser.getDateFormat());
		jsonParserMapping.setLastUpdatedByStaffId(jsonParser.getLastUpdatedByStaffId());
		jsonParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(jsonParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.JSON_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}
}
