package com.elitecore.sm.integration.dto;

import java.util.List;

/**
 * @author brijesh.soni
 *
 */
public class SearchPluginResponse {

	private int pluginId;
	private String pluginType;
	private String errorPath;
	private List<SearchFileResponse> fileList;

	public int getPluginId() {
		return pluginId;
	}

	public void setPluginId(int pluginId) {
		this.pluginId = pluginId;
	}

	public String getPluginType() {
		return pluginType;
	}

	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}

	public String getErrorPath() {
		return errorPath;
	}

	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

	public List<SearchFileResponse> getFileList() {
		return fileList;
	}

	public void setFileList(List<SearchFileResponse> fileList) {
		this.fileList = fileList;
	}

}
