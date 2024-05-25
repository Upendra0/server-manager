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
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

@Service
public class Asn1ParserServiceImpl implements Asn1ParserService {
	

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getAsn1ParserMappingById(int asn1ParserMappingId) {
		ResponseObject responseObject=new ResponseObject();
		
		ASN1ParserMapping asn1ParserMapping = parserMappingDao.getAsn1ParserMappingById(asn1ParserMappingId);
		
		if(asn1ParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(asn1ParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ASN1ParserMapping.class, ignorePropList = "parserAttributes,parserType,parserWrapper,device,recMainAttribute,srcDateFormat")	
	public ResponseObject updateAsn1ParserMapping(ASN1ParserMapping asn1Parser) {
		
		ResponseObject responseObject=new ResponseObject();
		
		ASN1ParserMapping asn1ParserMapping = (ASN1ParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, asn1Parser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(asn1Parser.getParserType().getAlias())){
				asn1ParserMapping.setParserType(pluginType);
			}
		}
		asn1ParserMapping.setSrcDateFormat(asn1Parser.getSrcDateFormat());
		asn1ParserMapping.setDateFormat(asn1Parser.getDateFormat());
		asn1ParserMapping.setLastUpdatedByStaffId(asn1Parser.getLastUpdatedByStaffId());
		asn1ParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(asn1ParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.ASN1_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
	}

}
