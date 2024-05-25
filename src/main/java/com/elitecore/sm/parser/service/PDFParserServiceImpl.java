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
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;
@Service
public class PDFParserServiceImpl implements PDFParserService {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ParserMappingDao parserMappingDao;

	/**
	 * get pdf parser mapping detail by id
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getPDFParserMappingById(int pdfParserMappingId) {
		
		ResponseObject responseObject=new ResponseObject();
		
		PDFParserMapping pdfParserMapping =parserMappingDao.getPDFParserMappingById(pdfParserMappingId);
		if(pdfParserMapping!=null){
			responseObject.setSuccess(true);
			responseObject.setObject(pdfParserMapping);
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		return responseObject;
	}

	/**
	 * update pdfParser
	 * 
	 * @param pdfParser
	 * @return responseObject
	 */
	@SuppressWarnings("unchecked")
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ParserMapping.class, ignorePropList = "parserAttributes,parserWrapper,device,parserType,name,dateFormat")
	@Transactional
	@Override
	public ResponseObject updatePDFParser(PDFParserMapping pdfParser) {
		ResponseObject responseObject = new ResponseObject();

		PDFParserMapping pdfParserMapping = (PDFParserMapping) parserMappingDao
				.findByPrimaryKey(ParserMapping.class, pdfParser.getId());
		
		List<PluginTypeMaster> pluginList = (List<PluginTypeMaster>) MapCache
				.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for (PluginTypeMaster pluginType : pluginList) {
			if (pluginType.getAlias().equals(pdfParser.getParserType().getAlias())) {
				pdfParserMapping.setParserType(pluginType);
			}
		}
		
		pdfParserMapping.setSrcCharSetName(pdfParser.getSrcCharSetName());
		pdfParserMapping.setFileParsed(pdfParser.isFileParsed());
		pdfParserMapping.setRecordWisePdfFormat(pdfParser.isRecordWisePdfFormat());
		pdfParserMapping.setMultiInvoice(pdfParser.isMultiInvoice());
		pdfParserMapping.setMultiPages(pdfParser.isMultiPages());
		pdfParserMapping.setSrcDateFormat(pdfParser.getSrcDateFormat());
		pdfParserMapping.setDateFormat(pdfParser.getDateFormat());
		pdfParserMapping.setLastUpdatedByStaffId(pdfParser.getLastUpdatedByStaffId());
		pdfParserMapping.setLastUpdatedDate(new Date());
		parserMappingDao.merge(pdfParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PDF_PARSER_MAPPING_SUCESS);
		return responseObject;
	}
}