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
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * @author Sagar Ghetiya
 *
 */
@Service(value="fixedLengthAsciiComposerService")
public class FixedLengthAsciiComposerServiceImpl implements FixedLengthAsciiComposerService{
	
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
	public ResponseObject getFixedLengthAsciiComposerMappingById(int fixedLengthAsciiComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping = composerMappingDao.getFixedLengthAsciiComposerMappingById(fixedLengthAsciiComposerMappingId);
		
		if(fixedLengthASCIIComposerMapping != null){
			responseObject.setSuccess(true);
			responseObject.setObject(fixedLengthASCIIComposerMapping);
		}else{
			logger.info("Composer Mapping not found");
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
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, actionType = BaseConstants.UPDATE_ACTION, currentEntity = FixedLengthASCIIComposerMapping.class, ignorePropList = "attributeList,composerType,composerWrapper,device,fieldSeparator,mappingType")	
	public ResponseObject updateFixedLengthAsciiComposerMapping(FixedLengthASCIIComposerMapping fixedLengthASCIIComposer){
		
		ResponseObject responseObject=new ResponseObject();
		
		FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping = (FixedLengthASCIIComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, fixedLengthASCIIComposer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(fixedLengthASCIIComposer.getComposerType().getAlias())){
				fixedLengthASCIIComposerMapping.setComposerType(pluginType);
			}
		}
		
		fixedLengthASCIIComposerMapping.setDestCharset(fixedLengthASCIIComposer.getDestCharset());
		fixedLengthASCIIComposerMapping.setDestDateFormat(fixedLengthASCIIComposer.getDestDateFormat());
		fixedLengthASCIIComposerMapping.setDateFormatEnum(fixedLengthASCIIComposer.getDateFormatEnum());
		fixedLengthASCIIComposerMapping.setDestFileExt(fixedLengthASCIIComposer.getDestFileExt());
		fixedLengthASCIIComposerMapping.setLastUpdatedByStaffId(fixedLengthASCIIComposer.getLastUpdatedByStaffId());
		fixedLengthASCIIComposerMapping.setLastUpdatedDate(new Date());
		
		composerMappingDao.merge(fixedLengthASCIIComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.FIXED_LENGTH_ASCII_COMPOSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}

	
}
