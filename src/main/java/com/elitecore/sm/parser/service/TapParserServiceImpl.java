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
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.util.MapCache;
@Service
public class TapParserServiceImpl implements TapParserService {
private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getTapParserMappingById(int tapParserMappingId) {
		ResponseObject responseObject=new ResponseObject();	
		TapParserMapping tapParserMapping = parserMappingDao.getTapParserMappingById(tapParserMappingId);
		if(tapParserMapping != null){
			responseObject.setSuccess(true);
			responseObject.setObject(tapParserMapping);
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		return responseObject;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = TapParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,groupAttributeList,recMainAttribute,recOffset,removeAddByte,headerOffset")
	public ResponseObject updateTapParserMappings(TapParserMapping tapParser) {
		ResponseObject responseObject=new ResponseObject();
		TapParserMapping tapParserMapping = (TapParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, tapParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(tapParserMapping.getParserType().getAlias())){
				tapParserMapping.setParserType(pluginType);
			}
		}
		tapParserMapping.setSrcDateFormat(tapParser.getSrcDateFormat());
		tapParserMapping.setDateFormat(tapParser.getDateFormat());
		tapParserMapping.setLastUpdatedByStaffId(tapParser.getLastUpdatedByStaffId());
		tapParserMapping.setLastUpdatedDate(new Date());
		parserMappingDao.merge(tapParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.TAP_PARSER_MAPPING_UPDATE_SUCCESS);
		return responseObject;
	}
}
