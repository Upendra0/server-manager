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
import com.elitecore.sm.parser.model.XlsParserMapping;
import com.elitecore.sm.util.MapCache;


@Service
public class XlsParserServiceImpl implements XlsParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	/**
	 * get html parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getXlsParserMappingById(int xlsParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		XlsParserMapping xlsParserMapping=parserMappingDao.getXlsParserMappingById(xlsParserMappingId);
		
		if(xlsParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(xlsParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update html parser mapping detail 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = XlsParserMapping.class, 
	ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device")	
	public ResponseObject updateXlsParserMapping(XlsParserMapping xlsParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		XlsParserMapping xlsParserMapping = (XlsParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, xlsParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(xlsParser.getParserType().getAlias())){
				xlsParserMapping.setParserType(pluginType);
			}
		}
		
		xlsParserMapping.setSrcCharSetName(xlsParser.getSrcCharSetName());
		xlsParserMapping.setSrcDateFormat(xlsParser.getSrcDateFormat());
		xlsParserMapping.setDateFormat(xlsParser.getDateFormat());
		xlsParserMapping.setLastUpdatedByStaffId(xlsParser.getLastUpdatedByStaffId());
		xlsParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(xlsParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.XLS_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
		
	}
}
