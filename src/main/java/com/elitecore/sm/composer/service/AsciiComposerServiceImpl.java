package com.elitecore.sm.composer.service;

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
import com.elitecore.sm.composer.dao.ComposerMappingDao;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Service(value="asciiComposerService")
public class AsciiComposerServiceImpl implements AsciiComposerService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ComposerMappingDao composerMappingDao;
	
	/**
	 * get Ascii composer mapping by id
	 * @param asciiComposerMappingId
	 * @return
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getAsciiComposerMappingById(int asciiComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		ASCIIComposerMapping asciiComposerMapping=composerMappingDao.getAsciiComposerMappingById(asciiComposerMappingId);
		
		if(asciiComposerMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(asciiComposerMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update Ascii composer mapping basic detail 
	 * @param asciiComposer
	 * @return ResponseObject
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, currentEntity =Composer.class , ignorePropList = "attributeList,composerWrapper,device,composerType,name,mappingType,fileHeaderEnable,fileHeaderContainsFields,fieldSeparator")
	public ResponseObject updateAsciiComposerMapping(ASCIIComposerMapping asciiComposer){
		
		ResponseObject responseObject=new ResponseObject();
		
		ASCIIComposerMapping asciiComposerMapping = (ASCIIComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, asciiComposer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(asciiComposer.getComposerType().getAlias())){
				asciiComposerMapping.setComposerType(pluginType);
			}
		}
		
		asciiComposerMapping.setDestCharset(asciiComposer.getDestCharset());
		asciiComposerMapping.setDestDateFormat(asciiComposer.getDestDateFormat());
		asciiComposerMapping.setDateFormatEnum(asciiComposer.getDateFormatEnum());
		asciiComposerMapping.setDestFileExt(asciiComposer.getDestFileExt());
		asciiComposerMapping.setLastUpdatedByStaffId(asciiComposer.getLastUpdatedByStaffId());
		asciiComposerMapping.setLastUpdatedDate(new Date());
		
		composerMappingDao.merge(asciiComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.ASCII_COMPOSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}

}
