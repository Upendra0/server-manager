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
import com.elitecore.sm.parser.model.HtmlParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author anupa.shah
 *
 */
@Service
public class HtmlParserServiceImpl implements HtmlParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	/**
	 * get html parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getHtmlParserMappingById(int htmlParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		HtmlParserMapping htmlParserMapping=parserMappingDao.getHtmlParserMappingById(htmlParserMappingId);
		
		if(htmlParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(htmlParserMapping);
			
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
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = HtmlParserMapping.class, 
	ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device")	
	public ResponseObject updateHtmlParserMapping(HtmlParserMapping htmlParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		HtmlParserMapping htmlParserMapping = (HtmlParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, htmlParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(htmlParser.getParserType().getAlias())){
				htmlParserMapping.setParserType(pluginType);
			}
		}
		
		htmlParserMapping.setSrcCharSetName(htmlParser.getSrcCharSetName());
		htmlParserMapping.setSrcDateFormat(htmlParser.getSrcDateFormat());
		htmlParserMapping.setDateFormat(htmlParser.getDateFormat());
		htmlParserMapping.setLastUpdatedByStaffId(htmlParser.getLastUpdatedByStaffId());
		htmlParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(htmlParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.HTML_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
		
	}
}
