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
import com.elitecore.sm.parser.model.NRTRDEParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;
@Service
public class NRTRDEParserServiceImpl implements NRTRDEParserService {
    
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getNRTRDEParserMappingById(int nrtrdeParserMappingId) {
		
		ResponseObject responseObject=new ResponseObject();
		
		NRTRDEParserMapping nrtrdeParserMapping = parserMappingDao.getNRTRDEParserMappingById(nrtrdeParserMappingId);
		if(nrtrdeParserMapping != null){
			responseObject.setSuccess(false);
			responseObject.setObject(nrtrdeParserMapping);
		}else{
			logger.debug("Parser Mapping Not Found");	
		}
		return responseObject;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity =NRTRDEParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,name,parserType,groupAttributeList,recMainAttribute,recOffset,removeAddByte,headerOffset")
	public ResponseObject updateNrtrdeParserMapping(NRTRDEParserMapping nrtrdeParser) {
		
		ResponseObject responseObject=new ResponseObject();
		NRTRDEParserMapping nrtrdeParserMapping = (NRTRDEParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, nrtrdeParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(nrtrdeParser.getParserType().getAlias())){
				nrtrdeParserMapping.setParserType(pluginType);
			}
		}
		nrtrdeParserMapping.setSrcDateFormat(nrtrdeParser.getSrcDateFormat());
		nrtrdeParserMapping.setDateFormat(nrtrdeParser.getDateFormat());
		nrtrdeParserMapping.setLastUpdatedByStaffId(nrtrdeParser.getLastUpdatedByStaffId());
		nrtrdeParserMapping.setLastUpdatedDate(new Date());
		parserMappingDao.merge(nrtrdeParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.NRTRDE_PARSER_MAPPING_UPDATE_SUCCESS);
		return responseObject;
	}
}