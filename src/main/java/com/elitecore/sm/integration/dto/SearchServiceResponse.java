package com.elitecore.sm.integration.dto;

import java.util.List;

/**
 * @author brijesh.soni
 *
 */
public class SearchServiceResponse {

	private String id;
	private String serviceType;
	private String serviceInstanceId;
	private List<SearchPluginResponse> pluginList;
	private List<SearchPathListResponse> pathList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SearchPluginResponse> getPluginList() {
		return pluginList;
	}

	public void setPluginList(List<SearchPluginResponse> pluginList) {
		this.pluginList = pluginList;
	}
	
	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	
	/**
	 * @return the serviceInstanceId
	 */
	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	
	/**
	 * @param serviceInstanceId the serviceInstanceId to set
	 */
	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	
	/**
	 * @return the pathList
	 */
	public List<SearchPathListResponse> getPathList() {
		return pathList;
	}

	
	/**
	 * @param pathList the pathList to set
	 */
	public void setPathList(List<SearchPathListResponse> pathList) {
		this.pathList = pathList;
	}

}
