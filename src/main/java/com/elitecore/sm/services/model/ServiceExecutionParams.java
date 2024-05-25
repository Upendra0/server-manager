/**
 * 
 */
package com.elitecore.sm.services.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jay.shah
 * 
 */
@Embeddable
public class ServiceExecutionParams implements Serializable {

	private static final long serialVersionUID = -273331962829869715L;

	private StartUpModeEnum startupMode = StartUpModeEnum.Automatic;
	private int executionInterval = 60;
	private boolean executeOnStartup = true;
	private int queueSize = 1000;
	private int minThread = 5;
	private int maxThread = 8;
	private int fileBatchSize = 20;
	private String sortingType = SortingTypeEnum.Ascending.getValue();
	private String sortingCriteria = SortingCriteriaEnum.Last_Modified_Date.getValue();


	/**
	 * @return the startupMode
	 */
	@Column(name = "STARTUPMODE", nullable = true)
	@Enumerated(EnumType.STRING)
	@XmlElement
	public StartUpModeEnum getStartupMode() {
		return startupMode;
	}

	/**
	 * @return the executionInterval
	 */
	@Column(name = "EXEINTERVAL", nullable = true, length = 6)
	@XmlElement
	public int getExecutionInterval() {
		return executionInterval;
	}

	/**
	 * @return the executeOnStartup
	 */
	@Column(name = "EXEONSTARTUP", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isExecuteOnStartup() {
		return executeOnStartup;
	}

	/**
	 * @return the queueSize
	 */
	@Column(name = "QUEUESIZE", nullable = true, length = 10)
	@XmlElement
	public int getQueueSize() {
		return queueSize;
	}

	/**
	 * @return the minThread
	 */
	@Column(name = "MINTHREAD", nullable = true, length = 3)
	@XmlElement
	public int getMinThread() {
		return minThread;
	}

	/**
	 * @return the maxThread
	 */
	@Column(name = "MAXTHREAD", nullable = true, length = 3)
	@XmlElement
	public int getMaxThread() {
		return maxThread;
	}

	/**
	 * @return the fileBatchSize
	 */
	@Column(name = "FILEBATCHSIZE", nullable = true, length = 3)
	@XmlElement
	public int getFileBatchSize() {
		return fileBatchSize;
	}


	/**
	 * @return the sortingType
	 */
	@Column(name = "SORTINGTYPE", nullable = true, length = 20)
	@XmlElement
	public String getSortingType() {
		return sortingType;
	}

	/**
	 * @return the sortingCriteria
	 */
	@Column(name = "SORTINGCRITERIA", nullable = true, length = 20)
	@XmlElement
	public String getSortingCriteria() {
		return sortingCriteria;
	}

	/**
	 * @param startupMode
	 *            the startupMode to set
	 */
	public void setStartupMode(StartUpModeEnum startupMode) {
		this.startupMode = startupMode;
	}

	/**
	 * @param executionInterval
	 *            the executionInterval to set
	 */
	public void setExecutionInterval(int executionInterval) {
		this.executionInterval = executionInterval;
	}

	/**
	 * @param executeOnStartup
	 *            the executeOnStartup to set
	 */
	public void setExecuteOnStartup(boolean executeOnStartup) {
		this.executeOnStartup = executeOnStartup;
	}

	/**
	 * @param queueSize
	 *            the queueSize to set
	 */
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	/**
	 * @param minThread
	 *            the minThread to set
	 */
	public void setMinThread(int minThread) {
		this.minThread = minThread;
	}

	/**
	 * @param maxThread
	 *            the maxThread to set
	 */
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}

	/**
	 * @param fileBatchSize
	 *            the fileBatchSize to set
	 */
	public void setFileBatchSize(int fileBatchSize) {
		this.fileBatchSize = fileBatchSize;
	}

	
	/**
	 * @param sortingType
	 *            the sortingType to set
	 */
	public void setSortingType(String sortingType) {
		this.sortingType = sortingType;
	}

	/**
	 * @param sortingCriteria
	 *            the sortingCriteria to set
	 */
	public void setSortingCriteria(String sortingCriteria) {
		this.sortingCriteria = sortingCriteria;
	}

}
