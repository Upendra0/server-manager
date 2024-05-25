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
import com.elitecore.sm.composer.model.TAPComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

@Service(value="tapComposerService")
public class TapComposerServiceImpl implements TapComposerService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ComposerMappingDao composerMappingDao;
	
	
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getTapComposerMappingById(int tapComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		TAPComposerMapping tapComposerMapping = composerMappingDao.getTapComposerMappingById(tapComposerMappingId);
		
		if(tapComposerMapping !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(tapComposerMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class ,ignorePropList= "attributeList,composerWrapper,composerType,mappingType,device,startFormat,recMainAttribute,groupAttributeList")
	public ResponseObject updateTapComposerMapping(TAPComposerMapping tapComposer){
		
		ResponseObject responseObject=new ResponseObject();
		
		TAPComposerMapping tapComposerMapping = (TAPComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, tapComposer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(tapComposer.getComposerType().getAlias())){
				tapComposerMapping.setComposerType(pluginType);
			}
		}
		
		tapComposerMapping.setDestCharset(tapComposer.getDestCharset());
		tapComposerMapping.setDestDateFormat(tapComposer.getDestDateFormat());
		tapComposerMapping.setDateFormatEnum(tapComposer.getDateFormatEnum());
		tapComposerMapping.setDestFileExt(tapComposer.getDestFileExt());
		tapComposerMapping.setLastUpdatedByStaffId(tapComposer.getLastUpdatedByStaffId());
		tapComposerMapping.setLastUpdatedDate(new Date());
		tapComposerMapping.setMultiContainerDelimiter(tapComposer.getMultiContainerDelimiter());
		tapComposerMapping.setComposeAsSingleRecordEnable(tapComposer.isComposeAsSingleRecordEnable());
		
		composerMappingDao.merge(tapComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.TAP_COMPOSER_MAPPING_UPDATE_SUCCESS);
		responseObject.setObject(tapComposer);
		return responseObject;
		
	}
	
}
