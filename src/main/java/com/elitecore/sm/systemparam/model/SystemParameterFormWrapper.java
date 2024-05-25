/**
 * 
 */
package com.elitecore.sm.systemparam.model;

import java.util.List;

/**
 * @author vandana.awatramani
 *
 */
public class SystemParameterFormWrapper {

	private List<SystemParameterData> genParamList;
	private List<SystemParameterData> pwdParamList;
	private List<SystemParameterData> custParamList;
	private List<SystemParameterData> custLogoParamList;
	private List<SystemParameterData> fileReprocessingParamList;
	private List<SystemParameterData> emailParamList;
	private List<SystemParameterData> ldapParamList;
	private List<SystemParameterData> ssoParamList;
	private List<SystemParameterData> emailFooterLogoList;
	private List<SystemParameterValuePoolData> systemParamValuePoolList;
	private String smallLogoPath;
	private String largeLogoPath;
	private String emailFooterImagePath;
	
	@Override
	public String toString() {
		return "SystemParameterFormWrapper [genParamList=" + genParamList + ", pwdParamList=" + pwdParamList
				+ ", custParamList=" + custParamList + ", custLogoParamList=" + custLogoParamList
				+ ", fileReprocessingParamList=" + fileReprocessingParamList + ", emailParamList=" + emailParamList
				+ ", ldapParamList=" + ldapParamList+ ", ssoParamList=" + ssoParamList + ", emailFooterLogoList=" + emailFooterLogoList
				+ ", systemParamValuePoolList=" + systemParamValuePoolList + ", smallLogoPath=" + smallLogoPath
				+ ", largeLogoPath=" + largeLogoPath + ", emailFooterImagePath=" + emailFooterImagePath + "]";
	}
	
	public List<SystemParameterData> getEmailFooterLogoList() {
		return emailFooterLogoList;
	}

	public void setEmailFooterLogoList(List<SystemParameterData> emailFooterLogoList) {
		this.emailFooterLogoList = emailFooterLogoList;
	}

	/**
	 * @return the genParamList
	 */
	public List<SystemParameterData> getGenParamList() {
		return genParamList;
	}
	/**
	 * @param genParamList the genParamList to set
	 */
	public void setGenParamList(List<SystemParameterData> genParamList) {
		this.genParamList = genParamList;
	}
	/**
	 * @return the pwdParamList
	 */
	public List<SystemParameterData> getPwdParamList() {
		return pwdParamList;
	}
	/**
	 * @param pwdParamList the pwdParamList to set
	 */
	public void setPwdParamList(List<SystemParameterData> pwdParamList) {
		this.pwdParamList = pwdParamList;
	}
	/**
	 * @return the custParamList
	 */
	public List<SystemParameterData> getCustParamList() {
		return custParamList;
	}
	/**
	 * @param custParamList the custParamList to set
	 */
	public void setCustParamList(List<SystemParameterData> custParamList) {
		this.custParamList = custParamList;
	}
	/**
	 * 
	 * @return SystemParamValuePoolList
	 */
	public List<SystemParameterValuePoolData> getSystemParamValuePoolList() {
		return systemParamValuePoolList;
	}

	/**
	 * 
	 * @param systemParamValuePoolList
	 */
	public void setSystemParamValuePoolList(List<SystemParameterValuePoolData> systemParamValuePoolList) {
		this.systemParamValuePoolList = systemParamValuePoolList;
	}

/**
 * 
 * @return
 */
	public String getSmallLogoPath() {
		return smallLogoPath;
	}
	/**
	 * 
	 * @param smallLogoPath
	 */
	public void setSmallLogoPath(String smallLogoPath) {
		this.smallLogoPath = smallLogoPath;
	}
	/**
	 * 
	 * @return
	 */
	public String getLargeLogoPath() {
		return largeLogoPath;
	}

	/**
	 * 
	 * @param largeLogoPath
	 */
	public void setLargeLogoPath(String largeLogoPath) {
		this.largeLogoPath = largeLogoPath;
	}

	/**
	 * 
	 * @return
	 */
	public List<SystemParameterData> getCustLogoParamList() {
		return custLogoParamList;
	}

	/**
	 * 
	 * @param custLogoParamList
	 */
	public void setCustLogoParamList(List<SystemParameterData> custLogoParamList) {
		this.custLogoParamList = custLogoParamList;
	}
	
	public List<SystemParameterData> getFileReprocessingParamList() {
		return fileReprocessingParamList;
	}

	public void setFileReprocessingParamList(List<SystemParameterData> fileReprocessingParamList) {
		this.fileReprocessingParamList = fileReprocessingParamList;
	}

	public List<SystemParameterData> getEmailParamList() {
		return emailParamList;
	}

	public void setEmailParamList(List<SystemParameterData> emailParamList) {
		this.emailParamList = emailParamList;
	}

	public List<SystemParameterData> getLdapParamList() {
		return ldapParamList;
	}
	
	public List<SystemParameterData> getSsoParamList() {
		return ssoParamList;
	}

	public void setLdapParamList(List<SystemParameterData> ldapParamList) {
		this.ldapParamList = ldapParamList;
	}
	
	public void setSsoParamList(List<SystemParameterData> ssoParamList) {
		this.ssoParamList = ssoParamList;
	}

	public String getEmailFooterImagePath() {
		return emailFooterImagePath;
	}

	public void setEmailFooterImagePath(String emailFooterImagePath) {
		this.emailFooterImagePath = emailFooterImagePath;
	}
	
}
