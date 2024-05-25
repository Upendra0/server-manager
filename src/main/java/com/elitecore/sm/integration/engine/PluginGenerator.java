package com.elitecore.sm.integration.engine;

import java.util.List;

import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.core.util.mbean.data.config.CrestelNetPluginData;

public class PluginGenerator extends BaseGenerator{
	
	public CrestelNetPluginData getPluginData(String id, String name, String description, int instanceId, String instanceName, String instanceMode,
			List<CrestelNetConfigurationData> configurationList){
		
		CrestelNetPluginData pluginData=new CrestelNetPluginData();
		
		pluginData.setNetPluginId(id);
		pluginData.setNetPluginName(name);
		pluginData.setDescription(description);
		pluginData.setNetPluginInstanceId(instanceId);
		pluginData.setNetPluginInstanceName(instanceName);
		pluginData.setNetPluginInstanceMode(instanceMode);
		pluginData.setNetConfigurationList(configurationList);
		
		return pluginData;
	}

}
