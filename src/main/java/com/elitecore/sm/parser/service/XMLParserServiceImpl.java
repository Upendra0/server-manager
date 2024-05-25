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
import com.elitecore.sm.parser.model.XMLParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author jaidip.trivedi
 *
 */
@Service
public class XMLParserServiceImpl implements XMLParserService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ParserMappingDao parserMappingDao;

	/**
	 * get xml parser mapping detail by id
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getXMLParserMappingById(int xmlParserMappingId) {
		ResponseObject responseObject = new ResponseObject();

		XMLParserMapping xmlParserMapping = parserMappingDao.getXMLParserMappingById(xmlParserMappingId);

		if (xmlParserMapping != null) {

			responseObject.setSuccess(true);
			responseObject.setObject(xmlParserMapping);

		} else {
			logger.debug("Parser Mapping Not Found");
		}

		return responseObject;
	}

	/**
	 * Update XML parser mapping detail
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class ,
	ignorePropList= "parserAttributes,parserWrapper,parserType,mappingType,device,dateFormat,recordWiseXmlFormat,commonFields")
	public ResponseObject updateXMLParserMapping(XMLParserMapping xmlParser) {

		ResponseObject responseObject = new ResponseObject();

		XMLParserMapping xmlParserMapping = (XMLParserMapping) parserMappingDao.findByPrimaryKey(ParserMapping.class,
				xmlParser.getId());
		List<PluginTypeMaster> pluginList = (List<PluginTypeMaster>) MapCache
				.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);

		for (PluginTypeMaster pluginType : pluginList) {
			if (pluginType.getAlias().equals(xmlParser.getParserType().getAlias())) {
				xmlParserMapping.setParserType(pluginType);
			}
		}

		xmlParserMapping.setSrcCharSetName(xmlParser.getSrcCharSetName());
		xmlParserMapping.setSrcDateFormat(xmlParser.getSrcDateFormat());
		xmlParserMapping.setDateFormat(xmlParser.getDateFormat());
		xmlParserMapping.setLastUpdatedByStaffId(xmlParser.getLastUpdatedByStaffId());
		xmlParserMapping.setLastUpdatedDate(new Date());

		parserMappingDao.merge(xmlParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.XML_PARSER_MAPPING_UPDATE_SUCCESS);

		return responseObject;

	}
}
