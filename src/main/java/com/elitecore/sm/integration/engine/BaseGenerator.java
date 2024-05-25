/**
 * 
 */
package com.elitecore.sm.integration.engine;

import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;

/**
 * @author Sunil Gulabani
 * Jul 23, 2015
 */
public class BaseGenerator {
	public CrestelNetConfigurationData getConfiguration( String key, String xmlData){
		CrestelNetConfigurationData configuration = new CrestelNetConfigurationData();
//		configuration.setNetConfigurationId(id); // not needed.
		configuration.setNetConfigurationKey(key);
		configuration.setNetConfigurationData(xmlData.getBytes());
		return configuration;
	}
}
