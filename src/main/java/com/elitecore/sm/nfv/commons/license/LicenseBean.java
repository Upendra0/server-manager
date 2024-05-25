package com.elitecore.sm.nfv.commons.license;

import org.springframework.stereotype.Component;

/**
 * The Class LicenseBean.
 */
@Component
public class LicenseBean {
	
	/** The customer. */
	private String customer;
	
	/** The location. */
	private String location;
	
	/** The macid. */
	private String macid;
	
	/** The product. */
	private String product;
	
	/** The version. */
	private String version;
	
	/** The start date. */
	private String startDate;
	
	/** The end date. */
	private String endDate;
	
	/** The hostname. */
	private String hostname;
	
	/** The tps. */
	private String tps;
	
	/** The daily records. */
	private String dailyRecords;
	
	/** The monthly records. */
	private String monthlyRecords;
	
	/**
	 * Gets the daily records.
	 *
	 * @return the daily records
	 */
	public String getDailyRecords() {
		return dailyRecords;
	}
	
	/**
	 * Sets the daily records.
	 *
	 * @param dailyRecords the new daily records
	 */
	public void setDailyRecords(String dailyRecords) {
		this.dailyRecords = dailyRecords;
	}
	
	/**
	 * Gets the monthly records.
	 *
	 * @return the monthly records
	 */
	public String getMonthlyRecords() {
		return monthlyRecords;
	}
	
	/**
	 * Sets the monthly records.
	 *
	 * @param monthlyRecords the new monthly records
	 */
	public void setMonthlyRecords(String monthlyRecords) {
		this.monthlyRecords = monthlyRecords;
	}
	
	/**
	 * Gets the customer.
	 *
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}
	
	/**
	 * Sets the customer.
	 *
	 * @param customer the new customer
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Gets the macid.
	 *
	 * @return the macid
	 */
	public String getMacid() {
		return macid;
	}
	
	/**
	 * Sets the macid.
	 *
	 * @param macid the new macid
	 */
	public void setMacid(String macid) {
		this.macid = macid;
	}
	
	/**
	 * Gets the product.
	 *
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}
	
	/**
	 * Sets the product.
	 *
	 * @param product the new product
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	
	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public String getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public String getEndDate() {
		return endDate;
	}
	
	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Gets the hostname.
	 *
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	
	/**
	 * Sets the hostname.
	 *
	 * @param hostname the new hostname
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	/**
	 * Gets the tps.
	 *
	 * @return the tps
	 */
	public String getTps() {
		return tps;
	}
	
	/**
	 * Sets the tps.
	 *
	 * @param tps the new tps
	 */
	public void setTps(String tps) {
		this.tps = tps;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		str.append("Customer Name =").append(customer).append("\n");
		str.append("Location =").append(location).append("\n");
		str.append("MacID =").append(macid).append("\n");
		str.append("Product =").append(product).append("\n");
		str.append("Version =").append(version).append("\n");
		str.append("Start Date =").append(startDate).append("\n");
		str.append("End Date =").append(endDate).append("\n");
		str.append("HostName =").append(hostname).append("\n");
		str.append("Daily Records =").append(dailyRecords).append("\n");
		str.append("Monthly Records =").append(monthlyRecords).append("\n");
		str.append("Tps =").append(tps);
		return str.toString();
	}
}
