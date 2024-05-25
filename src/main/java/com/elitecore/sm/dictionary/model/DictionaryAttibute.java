/**
 * 
 */
package com.elitecore.sm.dictionary.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ranjitsinh Reval
 *
 */
@XmlRootElement(name="attribute-list")
public class DictionaryAttibute {

	 	private List<DictionaryField> dictionaryFieldList;
	 	
	    private String vendorId;
	    
	    private String vendorName;
	    
		/**
		 * @return the dictionaryFieldList
		 */
	    @XmlElement(name = "field")
		public List<DictionaryField> getDictionaryFieldList() {
			return dictionaryFieldList;
		}
		/**
		 * @param dictionaryFieldList the dictionaryFieldList to set
		 */
		public void setDictionaryFieldList(List<DictionaryField> dictionaryFieldList) {
			this.dictionaryFieldList = dictionaryFieldList;
		}
		/**
		 * @return the vendorId
		 */
		@XmlAttribute(name="vendorid")
		public String getVendorId() {
			return vendorId;
		}
		/**
		 * @param vendorId the vendorId to set
		 */
		public void setVendorId(String vendorId) {
			this.vendorId = vendorId;
		}
		/**
		 * @return the vendorName
		 */
		@XmlAttribute(name="vendor-name")
		public String getVendorName() {
			return vendorName;
		}
		/**
		 * @param vendorName the vendorName to set
		 */
		public void setVendorName(String vendorName) {
			this.vendorName = vendorName;
		}
	
	
}
