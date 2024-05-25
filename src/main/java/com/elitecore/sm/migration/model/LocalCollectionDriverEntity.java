package com.elitecore.sm.migration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fileRange", "fileSequenceOrder", "pathList",
		"maxRetryCount", "noFilesAlertInterval", "fileGrouping" })
@XmlRootElement(name = "local-collection-driver")
public class LocalCollectionDriverEntity {

	@XmlElement(name = "file-range", required = true)
	protected String fileRange;

	@XmlElement(name = "file-sequence-order", required = true)
	@Mapping("fileSeqOrder")
	protected boolean fileSequenceOrder;

	@XmlElement(name = "path-list", required = true)
	protected LocalCollectionDriverEntity.PathList pathList;

	@XmlElement(name = "max-retry-count")
	@Mapping("maxRetrycount")
	protected int maxRetryCount;

	@XmlElement(name = "no-files-alert-interval")
	@Mapping("noFileAlert")
	protected int noFilesAlertInterval;

	@XmlElement(name = "file-grouping", required = true)
	@Mapping("fileGroupingParameter")
	protected LocalCollectionDriverEntity.FileGrouping fileGrouping;

	public String getFileRange() {
		return fileRange;
	}

	public void setFileRange(String value) {
		this.fileRange = value;
	}

	public LocalCollectionDriverEntity.PathList getPathList() {
		return pathList;
	}

	public void setPathList(LocalCollectionDriverEntity.PathList value) {
		this.pathList = value;
	}

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(int value) {
		this.maxRetryCount = value;
	}

	public int getNoFilesAlertInterval() {
		return noFilesAlertInterval;
	}

	public void setNoFilesAlertInterval(int value) {
		this.noFilesAlertInterval = value;
	}

	public LocalCollectionDriverEntity.FileGrouping getFileGrouping() {
		return fileGrouping;
	}

	public void setFileGrouping(LocalCollectionDriverEntity.FileGrouping value) {
		this.fileGrouping = value;
	}

	public boolean isFileSequenceOrder() {
		return fileSequenceOrder;
	}

	public void setFileSequenceOrder(boolean fileSequenceOrder) {
		this.fileSequenceOrder = fileSequenceOrder;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "myGroupingType", "forDuplicate" })
	public static class FileGrouping {

		@XmlElement(name = "grouping-type", required = true)
		protected String myGroupingType;

		@XmlElement(name = "for-duplicate", required = true)
		@Mapping("enableForDuplicate")
		protected boolean forDuplicate;

		@XmlAttribute(name = "enabled")
		@Mapping("fileGroupEnable")
		protected String enabled;

		public boolean isForDuplicate() {
			return forDuplicate;
		}

		public void setForDuplicate(boolean forDuplicate) {
			this.forDuplicate = forDuplicate;
		}

		public String getEnabled() {
			return enabled;
		}

		public void setEnabled(String value) {
			this.enabled = value;
		}

		public String getMyGroupingType() {
			return myGroupingType;
		}

		public void setMyGroupingType(String myGroupingType) {
			this.myGroupingType = myGroupingType;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "path" })
	public static class PathList {

		@Mapping("driverPathList")
		protected List<LocalCollectionDriverEntity.PathList.Path> path;

		public void setPath(List<LocalCollectionDriverEntity.PathList.Path> path) {
			this.path = path;
		}

		public List<LocalCollectionDriverEntity.PathList.Path> getPath() {
			if (path == null) {
				path = new ArrayList<>();
			}
			return this.path;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "readFilePath", "readFilenamePrefix",
				"readFilenameSuffix", "maxFilesCountAlert",
				"readFilenameContains", "writeFilePath", "writeFilenamePrefix",
				"filterPattern", "fileDate", "fileSequenceAlert" })
		public static class Path {

			@XmlElement(name = "read-file-path", required = true)
			@Mapping("readFilePath")
			protected String readFilePath;

			@XmlElement(name = "read-filename-prefix", required = true)
			@Mapping("readFilenamePrefix")
			protected String readFilenamePrefix;

			@XmlElement(name = "read-filename-suffix", required = true)
			@Mapping("readFilenameSuffix")
			protected String readFilenameSuffix;

			@XmlElement(name = "max-files-count-alert")
			@Mapping("maxFilesCountAlert")
			protected int maxFilesCountAlert;

			@XmlElement(name = "read-filename-contains", required = true)
			@Mapping("readFilenameContains")
			protected String readFilenameContains;

			@XmlElement(name = "write-file-path", required = true)
			@Mapping("writeFilePath")
			protected String writeFilePath;

			@XmlElement(name = "write-filename-prefix", required = true)
			@Mapping("writeFilenamePrefix")
			protected String writeFilenamePrefix;

			@XmlElement(name = "filter-pattern", required = true)
			@Mapping("this")
			protected LocalCollectionDriverEntity.PathList.Path.FilterPattern filterPattern;

			@XmlElement(name = "file-date", required = true)
			@Mapping("this")
			protected LocalCollectionDriverEntity.PathList.Path.FileDate fileDate;

			@XmlElement(name = "file-sequence-alert", required = true)
			@Mapping("this")
			protected LocalCollectionDriverEntity.PathList.Path.FileSequenceAlert fileSequenceAlert;

			public String getReadFilePath() {
				return readFilePath;
			}

			public void setReadFilePath(String value) {
				this.readFilePath = value;
			}

			public String getReadFilenamePrefix() {
				return readFilenamePrefix;
			}

			public void setReadFilenamePrefix(String value) {
				this.readFilenamePrefix = value;
			}

			public String getReadFilenameSuffix() {
				return readFilenameSuffix;
			}

			public void setReadFilenameSuffix(String value) {
				this.readFilenameSuffix = value;
			}

			public int getMaxFilesCountAlert() {
				return maxFilesCountAlert;
			}

			public void setMaxFilesCountAlert(int value) {
				this.maxFilesCountAlert = value;
			}

			public String getReadFilenameContains() {
				return readFilenameContains;
			}

			public void setReadFilenameContains(String value) {
				this.readFilenameContains = value;
			}

			public String getWriteFilePath() {
				return writeFilePath;
			}

			public void setWriteFilePath(String value) {
				this.writeFilePath = value;
			}

			public String getWriteFilenamePrefix() {
				return writeFilenamePrefix;
			}

			public void setWriteFilenamePrefix(String value) {
				this.writeFilenamePrefix = value;
			}

			public LocalCollectionDriverEntity.PathList.Path.FilterPattern getFilterPattern() {
				return filterPattern;
			}

			public void setFilterPattern(
					LocalCollectionDriverEntity.PathList.Path.FilterPattern value) {
				this.filterPattern = value;
			}

			public LocalCollectionDriverEntity.PathList.Path.FileDate getFileDate() {
				return fileDate;
			}

			public void setFileDate(
					LocalCollectionDriverEntity.PathList.Path.FileDate value) {
				this.fileDate = value;
			}

			public LocalCollectionDriverEntity.PathList.Path.FileSequenceAlert getFileSequenceAlert() {
				return fileSequenceAlert;
			}

			public void setFileSequenceAlert(
					LocalCollectionDriverEntity.PathList.Path.FileSequenceAlert value) {
				this.fileSequenceAlert = value;
			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "dateFormat", "position",
					"startIndex", "endIndex" })
			public static class FileDate {

				@XmlElement(name = "date-format", required = true)
				@Mapping("dateFormat")
				protected String dateFormat;

				@XmlElement(required = true)
				@Mapping("position")
				protected String position;

				@XmlElement(name = "start-index")
				@Mapping("startIndex")
				protected int startIndex;

				@XmlElement(name = "end-index")
				@Mapping("endIndex")
				protected int endIndex;

				@XmlAttribute(name = "enabled")
				@Mapping("fileGrepDateEnabled")
				protected String enabled;

				public String getDateFormat() {
					return dateFormat;
				}

				public void setDateFormat(String value) {
					this.dateFormat = value;
				}

				public String getPosition() {
					return position;
				}

				public void setPosition(String value) {
					this.position = value;
				}

				public int getStartIndex() {
					return startIndex;
				}

				public void setStartIndex(int value) {
					this.startIndex = value;
				}

				public int getEndIndex() {
					return endIndex;
				}

				public void setEndIndex(int value) {
					this.endIndex = value;
				}

				public String getEnabled() {
					return enabled;
				}

				public void setEnabled(String value) {
					this.enabled = value;
				}

			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "startIndex", "endIndex",
					"maxCounterLimit" })
			public static class FileSequenceAlert {

				@XmlElement(name = "start-index")
				@Mapping("seqStartIndex")
				protected int startIndex;

				@XmlElement(name = "end-index")
				@Mapping("seqEndIndex")
				protected int endIndex;

				@XmlElement(name = "max-counter-limit")
				@Mapping("maxCounterLimit")
				protected int maxCounterLimit;

				@XmlAttribute(name = "enabled")
				@Mapping("fileSeqAlertEnabled")
				protected String enabled;

				public int getStartIndex() {
					return startIndex;
				}

				public void setStartIndex(int value) {
					this.startIndex = value;
				}

				public int getEndIndex() {
					return endIndex;
				}

				public void setEndIndex(int value) {
					this.endIndex = value;
				}

				public int getMaxCounterLimit() {
					return maxCounterLimit;
				}

				public void setMaxCounterLimit(int value) {
					this.maxCounterLimit = value;
				}

				public String getEnabled() {
					return enabled;
				}

				public void setEnabled(String value) {
					this.enabled = value;
				}

			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "action", "param" })
			public static class FilterPattern {

				@XmlElement(required = true)
				@Mapping("remoteFileAction")
				protected String action;

				@XmlElement(required = true)
				@Mapping("this")
				protected LocalCollectionDriverEntity.PathList.Path.FilterPattern.Param param;

				public String getAction() {
					return action;
				}

				public void setAction(String value) {
					this.action = value;
				}

				public LocalCollectionDriverEntity.PathList.Path.FilterPattern.Param getParam() {
					return param;
				}

				public void setParam(
						LocalCollectionDriverEntity.PathList.Path.FilterPattern.Param value) {
					this.param = value;
				}

				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "", propOrder = { "value" })
				public static class Param {

					@XmlValue
					protected String value;

					@XmlAttribute(name = "key")
					@Mapping("remoteFileActionParamName")
					protected String key;

					@XmlAttribute(name = "value")
					@Mapping("remoteFileActionValue")
					protected String valueAttribute;

					public String getValue() {
						return value;
					}

					public void setValue(String value) {
						this.value = value;
					}

					public String getKey() {
						return key;
					}

					public void setKey(String value) {
						this.key = value;
					}

					public String getValueAttribute() {
						return valueAttribute;
					}

					public void setValueAttribute(String value) {
						this.valueAttribute = value;
					}

				}

			}

		}

	}

}
