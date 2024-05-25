package com.elitecore.sm.migration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "startupMode", "sortingType",
		"sortingCriteria", "executionInterval", "immediateExecuteOnStartup",
		"queueSize", "minimumThread", "maximumThread", "fileBatchSize",
		"minimumDiskSpaceRequired", "fileCopyFolders", "driverList",
		"scheduling", "storeCdrFileSummaryInDb" })
@XmlRootElement(name = "collection-service")
public class CollectionServiceEntity {

	@XmlElement(name = "startup-mode", required = true)
	protected String startupMode;

	@XmlElement(name = "sorting-type", required = true)
	@Mapping("svcExecParams.sortingType")
	protected String sortingType;

	@XmlElement(name = "sorting-criteria", required = true)
	@Mapping("svcExecParams.sortingCriteria")
	protected String sortingCriteria;

	@XmlElement(name = "execution-interval")
	@Mapping("svcExecParams.executionInterval")
	protected int executionInterval;

	@XmlElement(name = "immediate-execute-on-startup", required = true)
	protected String immediateExecuteOnStartup;

	@XmlElement(name = "queue-size")
	@Mapping("svcExecParams.queueSize")
	protected int queueSize;

	@XmlElement(name = "minimum-thread")
	@Mapping("svcExecParams.minThread")
	protected int minimumThread;

	@XmlElement(name = "maximum-thread")
	@Mapping("svcExecParams.maxThread")
	protected int maximumThread;

	@XmlElement(name = "file-batch-size")
	@Mapping("svcExecParams.fileBatchSize")
	protected int fileBatchSize;

	@XmlElement(name = "minimum-disk-space-required")
	@Mapping("serverInstance.minDiskSpace")
	protected String minimumDiskSpaceRequired;

	@XmlElement(name = "file-copy-folders", required = true)
	protected String fileCopyFolders;

	@XmlElement(name = "driver-list", required = true)
	@Mapping("this")
	protected CollectionServiceEntity.DriverList driverList;

	@XmlElement(required = true)
	@Mapping("serviceSchedulingParams")
	protected CollectionServiceEntity.Scheduling scheduling;

	@XmlElement(name = "store-cdr-file-summary-in-db", required = true)
	@Mapping("enableDBStats")
	protected String storeCdrFileSummaryInDb;

	public String getStartupMode() {
		return startupMode;
	}

	public void setStartupMode(String value) {
		this.startupMode = value;
	}

	public String getSortingType() {
		return sortingType;
	}

	public void setSortingType(String value) {
		this.sortingType = value;
	}

	public String getSortingCriteria() {
		return sortingCriteria;
	}

	public void setSortingCriteria(String value) {
		this.sortingCriteria = value;
	}

	public int getExecutionInterval() {
		return executionInterval;
	}

	public void setExecutionInterval(int value) {
		this.executionInterval = value;
	}

	public String getImmediateExecuteOnStartup() {
		return immediateExecuteOnStartup;
	}

	public void setImmediateExecuteOnStartup(String value) {
		this.immediateExecuteOnStartup = value;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int value) {
		this.queueSize = value;
	}

	public int getMinimumThread() {
		return minimumThread;
	}

	public void setMinimumThread(int value) {
		this.minimumThread = value;
	}

	public int getMaximumThread() {
		return maximumThread;
	}

	public void setMaximumThread(int value) {
		this.maximumThread = value;
	}

	public int getFileBatchSize() {
		return fileBatchSize;
	}

	public void setFileBatchSize(int value) {
		this.fileBatchSize = value;
	}

	public String getMinimumDiskSpaceRequired() {
		return minimumDiskSpaceRequired;
	}

	public void setMinimumDiskSpaceRequired(String value) {
		this.minimumDiskSpaceRequired = value;
	}

	public String getFileCopyFolders() {
		return fileCopyFolders;
	}

	public void setFileCopyFolders(String value) {
		this.fileCopyFolders = value;
	}

	public CollectionServiceEntity.DriverList getDriverList() {
		return driverList;
	}

	public void setDriverList(CollectionServiceEntity.DriverList value) {
		this.driverList = value;
	}

	public CollectionServiceEntity.Scheduling getScheduling() {
		return scheduling;
	}

	public void setScheduling(CollectionServiceEntity.Scheduling value) {
		this.scheduling = value;
	}

	public String getStoreCdrFileSummaryInDb() {
		return storeCdrFileSummaryInDb;
	}

	public void setStoreCdrFileSummaryInDb(String value) {
		this.storeCdrFileSummaryInDb = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "driver" })
	public static class DriverList {

		@XmlElement(required = true)
		@Mapping("myDrivers")
		protected List<CollectionServiceEntity.DriverList.Driver> driver;

		public List<CollectionServiceEntity.DriverList.Driver> getDriver() {
			return driver;
		}

		public void setDriver(
				List<CollectionServiceEntity.DriverList.Driver> value) {
			this.driver = value;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "applicationOrder", "driverName",
				"enabled" })
		public static class Driver {

			@XmlElement(name = "application-order")
			@Mapping("applicationOrder")
			protected int applicationOrder;

			@XmlElement(name = "driver-name", required = true)
			@Mapping("name")
			protected String driverName;

			@XmlElement(required = true)
			protected String enabled;

			public int getApplicationOrder() {
				return applicationOrder;
			}

			public void setApplicationOrder(int value) {
				this.applicationOrder = value;
			}

			public String getDriverName() {
				return driverName;
			}

			public void setDriverName(String value) {
				this.driverName = value;
			}

			public String getEnabled() {
				return enabled;
			}

			public void setEnabled(String value) {
				this.enabled = value;
			}

		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "schedulingType", "schedulingDate",
			"schedulingDay", "schedulingTime" })
	public static class Scheduling {

		@XmlElement(name = "scheduling-type", required = true)
		@Mapping("schType")
		protected String schedulingType;

		@XmlElement(name = "scheduling-date")
		@Mapping("date")
		protected String schedulingDate;

		@XmlElement(name = "scheduling-day")
		@Mapping("day")
		protected String schedulingDay;

		@XmlElement(name = "scheduling-time", required = true)
		@XmlSchemaType(name = "time")
		@Mapping("time")
		protected XMLGregorianCalendar schedulingTime;

		@XmlAttribute(name = "enabled")
		@Mapping("schedulingEnabled")
		protected boolean enabled;

		public String getSchedulingType() {
			return schedulingType;
		}

		public void setSchedulingType(String value) {
			this.schedulingType = value;
		}

		public String getSchedulingDate() {
			return schedulingDate;
		}

		public void setSchedulingDate(String value) {
			this.schedulingDate = value;
		}

		public String getSchedulingDay() {
			return schedulingDay;
		}

		public void setSchedulingDay(String value) {
			this.schedulingDay = value;
		}

		public XMLGregorianCalendar getSchedulingTime() {
			return schedulingTime;
		}

		public void setSchedulingTime(XMLGregorianCalendar value) {
			this.schedulingTime = value;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean value) {
			this.enabled = value;
		}

	}

}
