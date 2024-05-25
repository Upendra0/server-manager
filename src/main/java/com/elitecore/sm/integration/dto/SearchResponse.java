package com.elitecore.sm.integration.dto;

import java.util.List;

/**
 * @author brijesh.soni
 *
 */
public class SearchResponse {

	private List<SearchServiceResponse> serviceList;

	public List<SearchServiceResponse> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<SearchServiceResponse> serviceList) {
		this.serviceList = serviceList;
	}
}
