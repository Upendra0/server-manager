/**
 * 
 */
package com.elitecore.sm.samples;

import java.util.List;

import com.elitecore.sm.parser.model.NatFlowDictionaryAttribute;

/**
 * @author elitecore
 *
 */
public class AttributeSample {

	List<NatFlowDictionaryAttribute> tempList ;
	

	/**
	 * @return the tempList
	 */
	public List<NatFlowDictionaryAttribute> getTempList() {
		return tempList;
	}

	/**
	 * @param tempList the tempList to set
	 */
	public void setTempList(List<NatFlowDictionaryAttribute> tempList) {
		this.tempList = tempList;
	}
	
	public void addAllAttribute(){
		
		for (@SuppressWarnings("unused") NatFlowDictionaryAttribute natFlowDictionaryAttribute : tempList) {
			
		}
	}
}
