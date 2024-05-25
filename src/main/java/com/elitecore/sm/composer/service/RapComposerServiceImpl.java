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
import com.elitecore.sm.composer.model.RAPComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

@Service(value="rapComposerService")
public class RapComposerServiceImpl implements RapComposerService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ComposerMappingDao composerMappingDao;
	
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getRapComposerMappingById(int asn1ComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		RAPComposerMapping rapComposerMapping = composerMappingDao.getRapComposerMappingById(asn1ComposerMappingId);
		
		if(rapComposerMapping !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(rapComposerMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, actionType = BaseConstants.UPDATE_ACTION, currentEntity = RAPComposerMapping.class ,ignorePropList= "attributeList,composerWrapper,composerType,mappingType,device,recMainAttribute,groupAttributeList,startFormat")
	public ResponseObject updateRapComposerMapping(RAPComposerMapping rapComposer){
		
		ResponseObject responseObject=new ResponseObject();
		
		RAPComposerMapping rapComposerMapping = (RAPComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, rapComposer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(rapComposer.getComposerType().getAlias())){
				rapComposerMapping.setComposerType(pluginType);
			}
		}
		
		rapComposerMapping.setDestCharset(rapComposer.getDestCharset());
		rapComposerMapping.setDestDateFormat(rapComposer.getDestDateFormat());
		rapComposerMapping.setDateFormatEnum(rapComposer.getDateFormatEnum());
		rapComposerMapping.setDestFileExt(rapComposer.getDestFileExt());
		rapComposerMapping.setLastUpdatedByStaffId(rapComposer.getLastUpdatedByStaffId());
		rapComposerMapping.setLastUpdatedDate(new Date());
		rapComposerMapping.setMultiContainerDelimiter(rapComposer.getMultiContainerDelimiter());
		rapComposerMapping.setComposeAsSingleRecordEnable(rapComposer.isComposeAsSingleRecordEnable());
		
		composerMappingDao.merge(rapComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.RAP_COMPOSER_MAPPING_UPDATE_SUCCESS);
		responseObject.setObject(rapComposer);
		return responseObject;
		
	}
}
