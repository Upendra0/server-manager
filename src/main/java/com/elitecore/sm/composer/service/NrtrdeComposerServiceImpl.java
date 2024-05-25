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
import com.elitecore.sm.composer.model.NRTRDEComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

@Service(value="nrtrdeComposerService")
public class NrtrdeComposerServiceImpl implements NrtrdeComposerService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ComposerMappingDao composerMappingDao;
	

	@Transactional(readOnly=true)
	@Override
	public ResponseObject getNrtrdeComposerMappingById(int nrtrdeComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		NRTRDEComposerMapping nrtrdeComposerMapping = composerMappingDao.getNrtrdeComposerMappingById(nrtrdeComposerMappingId);
		
		if(nrtrdeComposerMapping !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(nrtrdeComposerMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class ,ignorePropList= "attributeList,composerWrapper,composerType,mappingType,device,startFormat,recMainAttribute,groupAttributeList")
	public ResponseObject updateNrtrdeComposerMapping(NRTRDEComposerMapping nrtrdeComposer){
		
		ResponseObject responseObject=new ResponseObject();
		
		NRTRDEComposerMapping nrtrdeComposerMapping = (NRTRDEComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, nrtrdeComposer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(nrtrdeComposer.getComposerType().getAlias())){
				nrtrdeComposerMapping.setComposerType(pluginType);
			}
		}
		
		nrtrdeComposerMapping.setDestCharset(nrtrdeComposer.getDestCharset());
		nrtrdeComposerMapping.setDestDateFormat(nrtrdeComposer.getDestDateFormat());
		nrtrdeComposerMapping.setDateFormatEnum(nrtrdeComposer.getDateFormatEnum());
		nrtrdeComposerMapping.setDestFileExt(nrtrdeComposer.getDestFileExt());
		nrtrdeComposerMapping.setLastUpdatedByStaffId(nrtrdeComposer.getLastUpdatedByStaffId());
		nrtrdeComposerMapping.setLastUpdatedDate(new Date());
		nrtrdeComposerMapping.setMultiContainerDelimiter(nrtrdeComposer.getMultiContainerDelimiter());
		nrtrdeComposerMapping.setComposeAsSingleRecordEnable(nrtrdeComposer.isComposeAsSingleRecordEnable());
		
		composerMappingDao.merge(nrtrdeComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.NRTRDE_COMPOSER_MAPPING_UPDATE_SUCCESS);
		responseObject.setObject(nrtrdeComposer);
		return responseObject;
		
	}
	
}
