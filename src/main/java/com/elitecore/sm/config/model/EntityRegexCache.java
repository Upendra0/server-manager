/**
 * 
 */
package com.elitecore.sm.config.model;

import java.util.Map;

import com.elitecore.sm.common.model.ToStringProcessor;

/**
 * @author Ranjitsinh Reval
 *
 */
public class EntityRegexCache  extends ToStringProcessor{

	
	private EntitiesRegex entitiesRegex ;
	private Map<String,EntityValidationRange> entityValidationRange;
	
	
	
	public EntityRegexCache(EntitiesRegex entitiesRegex,Map<String, EntityValidationRange> entityValidationRange) {
		super();
		this.entitiesRegex = entitiesRegex;
		this.entityValidationRange = entityValidationRange;
	}
	/**
	 * @return the entitiesRegex
	 */
	public EntitiesRegex getEntitiesRegex() {
		return entitiesRegex;
	}
	/**
	 * @return the entityValidationRange
	 */
	public Map<String, EntityValidationRange> getEntityValidationRange() {
		return entityValidationRange;
	}
	/**
	 * @param entitiesRegex the entitiesRegex to set
	 */
	public void setEntitiesRegex(EntitiesRegex entitiesRegex) {
		this.entitiesRegex = entitiesRegex;
	}
	/**
	 * @param entityValidationRange the entityValidationRange to set
	 */
	public void setEntityValidationRange(
			Map<String, EntityValidationRange> entityValidationRange) {
		this.entityValidationRange = entityValidationRange;
	}
	
	
	
}
