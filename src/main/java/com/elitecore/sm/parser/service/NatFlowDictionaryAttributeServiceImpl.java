/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.parser.dao.NatFlowDictionaryAttributeDao;
import com.elitecore.sm.parser.model.NatFlowDictionaryAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value="natFlowDictionaryAttributeService")
public class NatFlowDictionaryAttributeServiceImpl implements NatFlowDictionaryAttributeService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	@Qualifier(value="natFlowDictionaryAttributeDao")
	private NatFlowDictionaryAttributeDao natFlowDictionaryAttributeDao;

	/**
	 * Method will get all default master attributes.(attribute.xml in mediation engine dictionary xml file)
	 * @return List<NatFlowDictionaryAttribute>
	 */
	@Override
	@Transactional
	public HashMap<String,String> getAllDefaultDictionaryAttributes() {
		if(logger.isDebugEnabled()){logger.debug("Going to fetch all default master attributes.");}
		HashMap<String,String> defaultAttributeList = new HashMap<>();
		List<NatFlowDictionaryAttribute> natflowAttributeList = natFlowDictionaryAttributeDao.getAllDefaulAttributeList();
		
		if(logger.isDebugEnabled()){logger.debug("Default attribute list size is :: " + natflowAttributeList.size());}
		
		for (NatFlowDictionaryAttribute natFlowDictionaryAttribute : natflowAttributeList) {
			defaultAttributeList.put(natFlowDictionaryAttribute.getElementId(), natFlowDictionaryAttribute.getName());
		}
		
		return defaultAttributeList;
	}

}
