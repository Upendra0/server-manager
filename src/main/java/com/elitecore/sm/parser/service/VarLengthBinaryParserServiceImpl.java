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
import com.elitecore.sm.parser.dao.VarLengthAsciiDataDefinitionFileDao;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author hardik.loriya
 *
 */
@Service
public class VarLengthBinaryParserServiceImpl implements VarLengthBinaryParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired
	VarLengthAsciiDataDefinitionFileDao varLengthAsciiDataDefinitionFileDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/**
	 * get var length binary parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getVarLengthBinaryParserMappingById(int varLengthBinaryParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		VarLengthBinaryParserMapping varLengthBinaryParserMapping=parserMappingDao.getVarLengthBinaryParserMappingById(varLengthBinaryParserMappingId);
		
		if(varLengthBinaryParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(varLengthBinaryParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update Var Length Binary parser mapping detail 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = VarLengthAsciiParserMapping.class, ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device,fileTypeEnum,fileFooterParser,recordHeaderEnable,recordHeaderSeparator,fileHeaderContainsFields,fieldSeparator,srcDateFormat,recordHeaderIdentifier,excludeCharactersMin,excludeCharactersMax,excludeLinesStart,keyValueSeparator,find,replace,linearKeyValueRecordEnable,recordHeaderLength")	
	public ResponseObject updateVarLengthBinaryParserMapping(VarLengthBinaryParserMapping varLengthBinaryParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		VarLengthBinaryParserMapping varLengthBinaryParserMapping = (VarLengthBinaryParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, varLengthBinaryParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(varLengthBinaryParser.getParserType().getAlias())){
				varLengthBinaryParserMapping.setParserType(pluginType);
			}
		}
		
		varLengthBinaryParserMapping.setSrcCharSetName(varLengthBinaryParser.getSrcCharSetName());
		varLengthBinaryParserMapping.setSrcDateFormat(varLengthBinaryParser.getSrcDateFormat());
		varLengthBinaryParserMapping.setDateFormat(varLengthBinaryParser.getDateFormat());
		varLengthBinaryParserMapping.setLastUpdatedByStaffId(varLengthBinaryParser.getLastUpdatedByStaffId());
		varLengthBinaryParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(varLengthBinaryParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.VAR_LENGTH_ASCII_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}
	
}