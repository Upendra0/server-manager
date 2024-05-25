package com.elitecore.sm.integration.dto;

import java.util.List;


/**
 * @author Ranjitsinh Reval
 *
 */
public class SearchPathListResponse {

	private String pathId;
	private int ruleId;
	private String ruleAlias;
	private String errorPath;
	private List<SearchFileResponse> fileList;
	
	/**
	 * @return the ruleId
	 */
	public int getRuleId() {
		return ruleId;
	}
	
	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	
	/**
	 * @return the ruleAlias
	 */
	public String getRuleAlias() {
		return ruleAlias;
	}
	
	/**
	 * @param ruleAlias the ruleAlias to set
	 */
	public void setRuleAlias(String ruleAlias) {
		this.ruleAlias = ruleAlias;
	}
	
	/**
	 * @return the errorPath
	 */
	public String getErrorPath() {
		return errorPath;
	}
	
	/**
	 * @param errorPath the errorPath to set
	 */
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}
	
	/**
	 * @return the fileList
	 */
	public List<SearchFileResponse> getFileList() {
		return fileList;
	}
	
	/**
	 * @param fileList the fileList to set
	 */
	public void setFileList(List<SearchFileResponse> fileList) {
		this.fileList = fileList;
	}

	
	/**
	 * @return the pathId
	 */
	public String getPathId() {
		return pathId;
	}

	
	/**
	 * @param pathId the pathId to set
	 */
	public void setPathId(String pathId) {
		this.pathId = pathId;
	}
	
	
}
