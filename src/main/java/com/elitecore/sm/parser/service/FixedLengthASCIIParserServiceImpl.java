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
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author Sagar Ghetiya
 *
 */
@Service
public class FixedLengthASCIIParserServiceImpl implements FixedLengthASCIIParserService {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ParserMappingDao parserMappingDao;

	/**
	 * get fixedLengthASCII parser mapping detail by id
	 * 
	 */

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getFixedLengthASCIIParserMappingById(int fixedLengthASCIIParserMappingId) {
		ResponseObject responseObject = new ResponseObject();

		FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping = parserMappingDao
				.getFixedLengthASCIIParserMappingById(fixedLengthASCIIParserMappingId);

		if (fixedLengthASCIIParserMapping != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(fixedLengthASCIIParserMapping);
		} else {
			logger.debug("Parser Mapping Not Found");
		}

		return responseObject;
	}

	/**
	 * update fixedLengthASCII
	 * @param fixedLengthAsciiParser
	 * @return responseObject
	 */
	@SuppressWarnings("unchecked")
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class,
	ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,fileHeaderEnable,fileHeaderContainsFields,dateFormat")
	@Transactional
	@Override
	public ResponseObject updateFixedLengthASCIIParser(FixedLengthASCIIParserMapping fixedLengthASCIIParser) {
		ResponseObject responseObject = new ResponseObject();

		FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping = (FixedLengthASCIIParserMapping) parserMappingDao
				.findByPrimaryKey(ParserMapping.class, fixedLengthASCIIParser.getId());

		List<PluginTypeMaster> pluginList = (List<PluginTypeMaster>) MapCache
				.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);

		for (PluginTypeMaster pluginType : pluginList) {
			if (pluginType.getAlias().equals(fixedLengthASCIIParser.getParserType().getAlias())) {
				fixedLengthASCIIParserMapping.setParserType(pluginType);
			}
		}

		fixedLengthASCIIParserMapping.setSrcCharSetName(fixedLengthASCIIParser.getSrcCharSetName());
		fixedLengthASCIIParserMapping.setSrcDateFormat(fixedLengthASCIIParser.getSrcDateFormat());
		fixedLengthASCIIParserMapping.setDateFormat(fixedLengthASCIIParser.getDateFormat());
		fixedLengthASCIIParserMapping.setLastUpdatedByStaffId(fixedLengthASCIIParser.getLastUpdatedByStaffId());
		fixedLengthASCIIParserMapping.setLastUpdatedDate(new Date());

		parserMappingDao.merge(fixedLengthASCIIParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.FIXED_LENGTH_ASCII_PARSER_MAPPING_SUCESS);
		return responseObject;
	}
}
