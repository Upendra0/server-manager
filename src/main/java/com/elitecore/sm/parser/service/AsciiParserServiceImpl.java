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
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Service
public class AsciiParserServiceImpl implements AsciiParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	/**
	 * get ascii parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getAsciiParserMappingById(int asciiParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		AsciiParserMapping asciiParserMapping=parserMappingDao.getAsciiParserMappingById(asciiParserMappingId);
		
		if(asciiParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(asciiParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update Ascii parser mapping detail 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = AsciiParserMapping.class, ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device,fileTypeEnum,fileFooterParser,recordHeaderEnable,recordHeaderSeparator,fileHeaderContainsFields,fieldSeparator,srcDateFormat,recordHeaderIdentifier,excludeCharactersMin,excludeCharactersMax,excludeLinesStart,keyValueSeparator,find,replace,linearKeyValueRecordEnable,recordHeaderLength")	
	public ResponseObject updateAsciiParserMapping(AsciiParserMapping asciiParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		AsciiParserMapping asciiParserMapping = (AsciiParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, asciiParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(asciiParser.getParserType().getAlias())){
				asciiParserMapping.setParserType(pluginType);
			}
		}
		
		asciiParserMapping.setSrcCharSetName(asciiParser.getSrcCharSetName());
		asciiParserMapping.setSrcDateFormat(asciiParser.getSrcDateFormat());
		asciiParserMapping.setDateFormat(asciiParser.getDateFormat());
		asciiParserMapping.setLastUpdatedByStaffId(asciiParser.getLastUpdatedByStaffId());
		asciiParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(asciiParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.ASCII_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}
}
