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
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * The Class Asn1ComposerServiceImpl.
 *
 * @author sagar.r.patel
 * 
 */
@Service(value="asn1ComposerService")
public class Asn1ComposerServiceImpl implements Asn1ComposerService{
	
	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	/** The composer mapping dao. */
	@Autowired
	ComposerMappingDao composerMappingDao;
	

	/* (non-Javadoc)
	 * @see com.elitecore.sm.composer.service.Asn1ComposerService#getAsn1ComposerMappingById(int)
	 */
	@Transactional(readOnly=true)
	@Override
	public ResponseObject getAsn1ComposerMappingById(int asn1ComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		ASN1ComposerMapping asn1ComposerMapping = composerMappingDao.getAsn1ComposerMappingById(asn1ComposerMappingId);
		
		if(asn1ComposerMapping !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(asn1ComposerMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.composer.service.Asn1ComposerService#updateASN1ComposerMapping(com.elitecore.sm.composer.model.ASN1ComposerMapping)
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class ,ignorePropList= "attributeList,composerWrapper,composerType,mappingType,device,startFormat,recMainAttribute")
	public ResponseObject updateASN1ComposerMapping(ASN1ComposerMapping asn1Composer){
		
		ResponseObject responseObject=new ResponseObject();
		
		ASN1ComposerMapping asn1ComposerMapping = (ASN1ComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, asn1Composer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(asn1Composer.getComposerType().getAlias())){
				asn1ComposerMapping.setComposerType(pluginType);
			}
		}
		
		asn1ComposerMapping.setDestCharset(asn1Composer.getDestCharset());
		asn1ComposerMapping.setDestDateFormat(asn1Composer.getDestDateFormat());
		asn1ComposerMapping.setDateFormatEnum(asn1Composer.getDateFormatEnum());
		asn1ComposerMapping.setDestFileExt(asn1Composer.getDestFileExt());
		asn1ComposerMapping.setLastUpdatedByStaffId(asn1Composer.getLastUpdatedByStaffId());
		asn1ComposerMapping.setLastUpdatedDate(new Date());
		asn1ComposerMapping.setMultiContainerDelimiter(asn1Composer.getMultiContainerDelimiter());
		
		composerMappingDao.merge(asn1ComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.ASN1_COMPOSER_MAPPING_UPDATE_SUCCESS);
		responseObject.setObject(asn1Composer);
		return responseObject;
		
	}

}
