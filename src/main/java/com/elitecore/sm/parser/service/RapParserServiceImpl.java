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
import com.elitecore.sm.parser.model.RapParserMapping;
import com.elitecore.sm.util.MapCache;
@Service
public class RapParserServiceImpl implements RapParserService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;

	@Override
	@Transactional(readOnly=true)
	public ResponseObject getRapParserMappingById(int rapParserMappingId) {
		
		ResponseObject responseObject=new ResponseObject();
		
		RapParserMapping rapParserMapping = parserMappingDao.getRapParserMappingById(rapParserMappingId);
		if(rapParserMapping !=null){
			
			responseObject.setSuccess(true);
			
			responseObject.setObject(rapParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = RapParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,groupAttributeList,recMainAttribute,recOffset,removeAddByte,headerOffset")
	@Override
	@Transactional
	public ResponseObject updateRapParserMappings(RapParserMapping rapParser) {
		ResponseObject responseObject=new ResponseObject();
		RapParserMapping rapParserMapping = (RapParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, rapParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(rapParserMapping.getParserType().getAlias())){
				rapParserMapping.setParserType(pluginType);
			}
		}
		rapParserMapping.setSrcDateFormat(rapParser.getSrcDateFormat());
		rapParserMapping.setDateFormat(rapParser.getDateFormat());
		rapParserMapping.setLastUpdatedByStaffId(rapParser.getLastUpdatedByStaffId());
		rapParserMapping.setLastUpdatedDate(new Date());
		parserMappingDao.merge(rapParserMapping);
		responseObject.setSuccess(true);
		//responseObject.setObject(rapParser);
		responseObject.setResponseCode(ResponseCode.RAP_PARSER_MAPPING_UPDATE_SUCCESS);
		return responseObject;
	}

}
