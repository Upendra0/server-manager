package com.elitecore.sm.integration.dto;


/**
 * @author brijesh.soni
 *
 */
public class SearchFileResponse {

	private String name;
	private double size;
	private boolean isCompress;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}
	
	/**
	 * @return the isCompress
	 */
	public boolean isCompress() {
		return isCompress;
	}

	
	/**
	 * @param isCompress the isCompress to set
	 */
	public void setCompress(boolean isCompress) {
		this.isCompress = isCompress;
	}

}
