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
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;
@Service
public class FixedLengthBinaryParserServiceImpl implements FixedLengthBinaryParserService {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ParserMappingDao parserMappingDao;

	/**
	 * get fixedLengthBinary parser mapping detail by id
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getFixedLengthBinaryParserMappingById(int fixedLengthBinaryParserMappingId) {
		
		ResponseObject responseObject=new ResponseObject();
		
		FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping =parserMappingDao.getFixedLengthBinaryParserMappingById(fixedLengthBinaryParserMappingId);
		if(fixedLengthBinaryParserMapping!=null){
			responseObject.setSuccess(true);
			responseObject.setObject(fixedLengthBinaryParserMapping);
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		return responseObject;
	}

	/**
	 * update fixedLengthASCII
	 * 
	 * @param fixedLengthAsciiParser
	 * @return responseObject
	 */
	@SuppressWarnings("unchecked")
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class, ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat")
	@Transactional
	@Override
	public ResponseObject updateFixedLengthBinaryParser(FixedLengthBinaryParserMapping fixedLengthBinaryParser) {
		ResponseObject responseObject = new ResponseObject();

		FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping = (FixedLengthBinaryParserMapping) parserMappingDao
				.findByPrimaryKey(ParserMapping.class, fixedLengthBinaryParser.getId());
		
		List<PluginTypeMaster> pluginList = (List<PluginTypeMaster>) MapCache
				.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for (PluginTypeMaster pluginType : pluginList) {
			if (pluginType.getAlias().equals(fixedLengthBinaryParser.getParserType().getAlias())) {
				fixedLengthBinaryParserMapping.setParserType(pluginType);
			}
		}
		
		fixedLengthBinaryParserMapping.setSrcCharSetName(fixedLengthBinaryParser.getSrcCharSetName());
		fixedLengthBinaryParserMapping.setRecordLength(fixedLengthBinaryParser.getRecordLength());
		fixedLengthBinaryParserMapping.setSrcDateFormat(fixedLengthBinaryParser.getSrcDateFormat());
		fixedLengthBinaryParserMapping.setDateFormat(fixedLengthBinaryParser.getDateFormat());
		fixedLengthBinaryParserMapping.setLastUpdatedByStaffId(fixedLengthBinaryParser.getLastUpdatedByStaffId());
		fixedLengthBinaryParserMapping.setLastUpdatedDate(new Date());
		parserMappingDao.merge(fixedLengthBinaryParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.FIXED_LENGTH_BINARY_PARSER_MAPPING_SUCESS);
		return responseObject;
	}

	
	

}
