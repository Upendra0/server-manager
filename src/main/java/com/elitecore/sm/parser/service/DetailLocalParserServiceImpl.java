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
import com.elitecore.sm.parser.model.DetailLocalParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Service
public class DetailLocalParserServiceImpl implements DetailLocalParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	

	/**
	 * get detail local parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getDetailLocalParserMappingById(int detailLocalParserMappingId) {
		// TODO Auto-generated method stub
		ResponseObject responseObject=new ResponseObject();
		
		DetailLocalParserMapping detailLocalParserMapping=parserMappingDao.getDetailLocalParserMappingById(detailLocalParserMappingId);
		
		if(detailLocalParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(detailLocalParserMapping);
			
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
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = AsciiParserMapping.class, ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device,srcDateFormat")	
	public ResponseObject updateDetailLocalParserMapping(DetailLocalParserMapping detailLocalParser) {
		// TODO Auto-generated method stub
		ResponseObject responseObject=new ResponseObject();
		
		DetailLocalParserMapping detailLocalParserMapping = (DetailLocalParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, detailLocalParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(detailLocalParser.getParserType().getAlias())){
				detailLocalParserMapping.setParserType(pluginType);
			}
		}
		
		detailLocalParserMapping.setSrcCharSetName(detailLocalParser.getSrcCharSetName());
		detailLocalParserMapping.setSrcDateFormat(detailLocalParser.getSrcDateFormat());
		detailLocalParserMapping.setDateFormat(detailLocalParser.getDateFormat());
		detailLocalParserMapping.setLastUpdatedByStaffId(detailLocalParser.getLastUpdatedByStaffId());
		detailLocalParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(detailLocalParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.DETAIL_LOCAL_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;

	}
	

}
