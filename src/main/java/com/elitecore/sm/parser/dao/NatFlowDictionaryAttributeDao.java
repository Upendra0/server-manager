/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;

import com.elitecore.sm.parser.model.NatFlowDictionaryAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface NatFlowDictionaryAttributeDao {

	
	public List<NatFlowDictionaryAttribute> getAllDefaulAttributeList();
	
	public void addAttribute(NatFlowDictionaryAttribute natflowAttr);
}
