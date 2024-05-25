/**
 * 
 */
package com.elitecore.sm.config.service;

import java.util.List;

import com.elitecore.sm.config.model.EntitiesRegex;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
public interface ConfigService {
	public List<EntitiesRegex> loadSystemParameters();
	
	public void addEntitiesRegex(List<EntitiesRegex> systemParametersList);
}
