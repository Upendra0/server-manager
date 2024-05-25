package com.elitecore.sm.parser.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.elitecore.core.util.Constant;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.VarLengthAsciiDataDefinitionFileDao;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.VarLengthAsciiDataDefinitionFile;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author hardik.loriya
 *
 */
@Service
public class VarLengthAsciiParserServiceImpl implements VarLengthAsciiParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired
	VarLengthAsciiDataDefinitionFileDao varLengthAsciiDataDefinitionFileDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/**
	 * get var length ascii parser mapping detail by id
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getVarLengthAsciiParserMappingById(int varLengthAsciiParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		VarLengthAsciiParserMapping varLengthasciiParserMapping=parserMappingDao.getVarLengthAsciiParserMappingById(varLengthAsciiParserMappingId);
		
		if(varLengthasciiParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(varLengthasciiParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update Var Length Ascii parser mapping detail 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_MAPPING_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = VarLengthAsciiParserMapping.class, ignorePropList = "groupAttributeList,parserAttributes,parserType,parserWrapper,device,fileTypeEnum,fileFooterParser,recordHeaderEnable,recordHeaderSeparator,fileHeaderContainsFields,fieldSeparator,srcDateFormat,recordHeaderIdentifier,excludeCharactersMin,excludeCharactersMax,excludeLinesStart,keyValueSeparator,find,replace,linearKeyValueRecordEnable,recordHeaderLength")	
	public ResponseObject updateVarLengthAsciiParserMapping(VarLengthAsciiParserMapping varLengthAsciiParser){
		
		ResponseObject responseObject=new ResponseObject();
		
		VarLengthAsciiParserMapping varLengthAsciiParserMapping = (VarLengthAsciiParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, varLengthAsciiParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(varLengthAsciiParser.getParserType().getAlias())){
				varLengthAsciiParserMapping.setParserType(pluginType);
			}
		}
		
		varLengthAsciiParserMapping.setSrcCharSetName(varLengthAsciiParser.getSrcCharSetName());
		varLengthAsciiParserMapping.setSrcDateFormat(varLengthAsciiParser.getSrcDateFormat());
		varLengthAsciiParserMapping.setDateFormat(varLengthAsciiParser.getDateFormat());
		varLengthAsciiParserMapping.setLastUpdatedByStaffId(varLengthAsciiParser.getLastUpdatedByStaffId());
		varLengthAsciiParserMapping.setLastUpdatedDate(new Date());
		
		parserMappingDao.merge(varLengthAsciiParserMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.VAR_LENGTH_ASCII_PARSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}
	
	@Override
	@Transactional
	public ResponseObject uploadDataDefinitionFile(MultipartFile file, int mappingId, int staffId, int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();
		String fileName = file.getOriginalFilename();
		VarLengthAsciiParserMapping mapping = null;
		try {
			boolean isDataDefinitionFileExist = varLengthAsciiDataDefinitionFileDao.isDataDefinitionFileExist(fileName);
			ServerInstance serverInstance = serverInstanceDao.getServerInstance(serverInstanceId);
			if(!isDataDefinitionFileExist){
				VarLengthAsciiDataDefinitionFile definitionFile = varLengthAsciiDataDefinitionFileDao.getDataDefinitionByMappingId(mappingId);
				if(definitionFile!=null) {
					definitionFile.setDataDefinitionFileName(fileName);
					definitionFile.setDataDefinitionFile(new javax.sql.rowset.serial.SerialBlob(file.getBytes()));
					mapping = definitionFile.getMapping();
					mapping.setDataDefinitionPath(serverInstance.getServerHome() + File.separator + Constant.MODULES + File.separator + Constant.MEDIATION + File.separator + Constant.DICTIONARY + File.separator + Constant.VARLENGTHASCII + File.separator + fileName);
					definitionFile.setMapping(mapping);
					definitionFile.setLastUpdatedByStaffId(staffId);
					definitionFile.setLastUpdatedDate(new Date());
					varLengthAsciiDataDefinitionFileDao.merge(definitionFile);
					parserMappingDao.merge(mapping);
				}else {
					definitionFile = new VarLengthAsciiDataDefinitionFile();
					definitionFile.setDataDefinitionFileName(fileName);
					definitionFile.setDataDefinitionFile(new javax.sql.rowset.serial.SerialBlob(file.getBytes()));
					mapping =parserMappingDao.getVarLengthAsciiParserMappingById(mappingId);
					mapping.setDataDefinitionPath(serverInstance.getServerHome() + File.separator + Constant.MODULES + File.separator + Constant.MEDIATION + File.separator + Constant.DICTIONARY + File.separator + Constant.VARLENGTHASCII + File.separator + fileName);
					definitionFile.setMapping(mapping);
					definitionFile.setCreatedByStaffId(staffId);
					definitionFile.setLastUpdatedByStaffId(staffId);
					varLengthAsciiDataDefinitionFileDao.save(definitionFile);
					parserMappingDao.merge(mapping);
				}
				responseObject.setObject(definitionFile.getDataDefinitionFileName());
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.VAR_LENGTH_ASCII_PARSER_MAPPING_UPLOAD_DATA_FILE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.VAR_LENGTH_ASCII_PARSER_MAPPING_DATA_FILE_EXIST);
			}
		} catch (Exception e) {//NOSONAR
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.VAR_LENGTH_ASCII_PARSER_MAPPING_UPLOAD_DATA_FILE_FAIL);
		}
		return responseObject;
	}
	
	@Override
	@Transactional(readOnly = true)
	public String getDataDefinitionFileNameById(int mappingId) {
		VarLengthAsciiDataDefinitionFile definitionFile = varLengthAsciiDataDefinitionFileDao.getDataDefinitionByMappingId(mappingId);
		String dataDefinitionFilename = null;
		if(definitionFile!=null){
			dataDefinitionFilename = definitionFile.getDataDefinitionFileName();
		}
		return dataDefinitionFilename;
	}
	
}