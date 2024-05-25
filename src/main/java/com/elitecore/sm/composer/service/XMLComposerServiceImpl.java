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
import com.elitecore.sm.composer.model.XMLComposerMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author Mitul.Vora
 *
 */
@Service(value="xmlComposerService")
public class XMLComposerServiceImpl implements XMLComposerService{
	
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
	public ResponseObject getXMLComposerMappingById(int xmlComposerMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		XMLComposerMapping xmlComposerMapping=composerMappingDao.getXMLComposerMappingById(xmlComposerMappingId);
		
		if(xmlComposerMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(xmlComposerMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Update xml composer mapping basic detail 
	 * @param xmlComposer
	 * @return ResponseObject
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_BASIC_MAPPING_DETAIL, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class ,ignorePropList= "attributeList,composerWrapper,composerType,mappingType,device")
	public ResponseObject updateXMLComposerMapping(XMLComposerMapping xmlComposer){
		
		ResponseObject responseObject=new ResponseObject();
		
		XMLComposerMapping xmlComposerMapping = (XMLComposerMapping)composerMappingDao.findByPrimaryKey(ComposerMapping.class, xmlComposer.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(xmlComposer.getComposerType().getAlias())){
				xmlComposerMapping.setComposerType(pluginType);
			}
		}
		
		xmlComposerMapping.setDestCharset(xmlComposer.getDestCharset());
		xmlComposerMapping.setDestDateFormat(xmlComposer.getDestDateFormat());
		xmlComposerMapping.setDateFormatEnum(xmlComposer.getDateFormatEnum());
		xmlComposerMapping.setDestFileExt(xmlComposer.getDestFileExt());
		xmlComposerMapping.setLastUpdatedByStaffId(xmlComposer.getLastUpdatedByStaffId());
		xmlComposerMapping.setLastUpdatedDate(new Date());
		
		composerMappingDao.merge(xmlComposerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.XML_COMPOSER_MAPPING_UPDATE_SUCCESS);
		
		return responseObject;
		
	}

}
