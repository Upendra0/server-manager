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
import com.elitecore.sm.parser.model.MTSiemensParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;


@Service
public class MTSiemensParserServiceImpl implements MTSiemensParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getMTSiemensParserMappingById(int mtsiemensParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		MTSiemensParserMapping mtsiemensParserMapping=parserMappingDao.getMTSiemensParserMappingById(mtsiemensParserMappingId);
		
		if(mtsiemensParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(mtsiemensParserMapping);
			
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
	public ResponseObject updateMTSiemensParserMapping(MTSiemensParserMapping mtsiemensParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		MTSiemensParserMapping mtSiemensParserMapping = (MTSiemensParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, mtsiemensParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(mtsiemensParser.getParserType().getAlias())){
				mtSiemensParserMapping.setParserType(pluginType);
			}
		}
		
		mtSiemensParserMapping.setSrcCharSetName(mtsiemensParser.getSrcCharSetName());
		mtSiemensParserMapping.setSrcDateFormat(mtsiemensParser.getSrcDateFormat());
		mtSiemensParserMapping.setDateFormat(mtsiemensParser.getDateFormat());
		mtSiemensParserMapping.setLastUpdatedByStaffId(mtsiemensParser.getLastUpdatedByStaffId());
		mtSiemensParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(mtSiemensParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.MTSIEMENS_BINARY_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}
}
