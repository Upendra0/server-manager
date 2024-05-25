package com.elitecore.sm.server.service;

import com.elitecore.sm.server.model.ServerType;

public interface ServerTypeService {

	void setActiveProductType(String productType);
	
	public ServerType getServerTypeById(int serverTypeId);
}
