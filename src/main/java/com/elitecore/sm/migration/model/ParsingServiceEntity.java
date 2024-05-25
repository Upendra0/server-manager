package com.elitecore.sm.migration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "minimumThread", "sortingCriteria",
		"maximumThread", "fileBatchSize", "queueSize", "fileSequenceOrder",
		"jaxbStartupMode", "fileRange", "noFilesAlertInterval",
		"executionInterval", "immediateExecuteOnStartup",
		"storeCdrFileSummaryInDb", "equalCheckValue", "equalCheckField","fileStatInsertEnable",
		"cdrDateSummary", "sortingType", "fileGrouping",
		"recordBatchSize", "pathList" })
@XmlRootElement(name = "parsing-service")
public class ParsingServiceEntity {

	@XmlElement(name = "minimum-thread")
	@Mapping("svcExecParams.minThread")
	protected int minimumThread;

	@XmlElement(name = "sorting-criteria", required = true)
	@Mapping("svcExecParams.sortingCriteria")
	protected String sortingCriteria;

	@XmlElement(name = "maximum-thread")
	@Mapping("svcExecParams.maxThread")
	protected int maximumThread;

	@XmlElement(name = "file-batch-size")
	@Mapping("svcExecParams.fileBatchSize")
	protected int fileBatchSize;

	@XmlElement(name = "queue-size")
	@Mapping("svcExecParams.queueSize")
	protected int queueSize;

	@XmlElement(name = "file-sequence-order", required = true)
	@Mapping("fileSeqOrderEnable")
	protected String fileSequenceOrder;

	@XmlElement(name = "startup-mode", required = true)
	protected String jaxbStartupMode;

	@XmlElement(name = "file-range", required = true)
	protected String fileRange;

	@XmlElement(name = "no-files-alert-interval", required = true)
	@Mapping("noFileAlert")
	protected String noFilesAlertInterval;

	@XmlElement(name = "execution-interval")
	@Mapping("svcExecParams.executionInterval")
	protected int executionInterval;

	@XmlElement(name = "immediate-execute-on-startup", required = true)
	@Mapping("svcExecParams.executeOnStartup")
	protected String immediateExecuteOnStartup;

	@XmlElement(name = "store-cdr-file-summary-in-db", required = true)
	@Mapping("storeCDRFileSummaryDB")
	protected String storeCdrFileSummaryInDb;

	@XmlElement(name = "store-file-statatics-in-db", required = true)
	@Mapping("fileStatInsertEnable")
	protected String fileStatInsertEnable;
	
	@XmlElement(name = "equal-check-value", required = true)
	@Mapping("equalCheckValue")
	protected String equalCheckValue;

	@XmlElement(name = "equal-check-field", required = true)
	@Mapping("equalCheckField")
	protected String equalCheckField;

	@XmlElement(name = "cdr-date-summary", required = true)
	@Mapping("this")
	protected ParsingServiceEntity.CdrDateSummary cdrDateSummary;

	@XmlElement(name = "sorting-type", required = true)
	@Mapping("svcExecParams.sortingType")
	protected String sortingType;

	@XmlElement(name = "file-grouping", required = true)
	@Mapping("this")
	protected ParsingServiceEntity.FileGrouping fileGrouping;

	@XmlElement(name = "record-batch-size")
	@Mapping("recordBatchSize")
	protected int recordBatchSize;

	@XmlElement(name = "path-list", required = true)
	protected ParsingServiceEntity.PathList pathList;
	
	public String getJaxbStartupMode() {
		return jaxbStartupMode;
	}

	public void setJaxbStartupMode(String jaxbStartupMode) {
		this.jaxbStartupMode = jaxbStartupMode;
	}

	public int getMinimumThread() {
		return minimumThread;
	}

	public void setMinimumThread(int value) {
		this.minimumThread = value;
	}

	public String getSortingCriteria() {
		return sortingCriteria;
	}

	public void setSortingCriteria(String value) {
		this.sortingCriteria = value;
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

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int value) {
		this.queueSize = value;
	}

	public String getFileSequenceOrder() {
		return fileSequenceOrder;
	}

	public void setFileSequenceOrder(String value) {
		this.fileSequenceOrder = value;
	}

	public String getFileRange() {
		return fileRange;
	}

	public void setFileRange(String value) {
		this.fileRange = value;
	}

	public String getNoFilesAlertInterval() {
		return noFilesAlertInterval;
	}

	public void setNoFilesAlertInterval(String value) {
		this.noFilesAlertInterval = value;
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

	public String getStoreCdrFileSummaryInDb() {
		return storeCdrFileSummaryInDb;
	}

	public void setStoreCdrFileSummaryInDb(String value) {
		this.storeCdrFileSummaryInDb = value;
	}

	public String getEqualCheckValue() {
		return equalCheckValue;
	}

	public void setEqualCheckValue(String value) {
		this.equalCheckValue = value;
	}

	public String getEqualCheckField() {
		return equalCheckField;
	}

	public void setEqualCheckField(String value) {
		this.equalCheckField = value;
	}

	public ParsingServiceEntity.CdrDateSummary getCdrDateSummary() {
		return cdrDateSummary;
	}

	public void setCdrDateSummary(ParsingServiceEntity.CdrDateSummary value) {
		this.cdrDateSummary = value;
	}

	public String getSortingType() {
		return sortingType;
	}

	public void setSortingType(String value) {
		this.sortingType = value;
	}

	public ParsingServiceEntity.FileGrouping getFileGrouping() {
		return fileGrouping;
	}

	public void setFileGrouping(ParsingServiceEntity.FileGrouping value) {
		this.fileGrouping = value;
	}

	public int getRecordBatchSize() {
		return recordBatchSize;
	}

	public void setRecordBatchSize(int value) {
		this.recordBatchSize = value;
	}

	public ParsingServiceEntity.PathList getPathList() {
		return pathList;
	}

	public void setPathList(ParsingServiceEntity.PathList value) {
		this.pathList = value;
	}
	
	public String getFileStatInsertEnable() {
		return fileStatInsertEnable;
	}

	public void setFileStatInsertEnable(String fileStatInsertEnable) {
		this.fileStatInsertEnable = fileStatInsertEnable;
	}

	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "date", "type","overrideFileDate","jaxbOverrideFileDateType" })
	public static class CdrDateSummary {

		@XmlElement(required = true)
		protected String date;
		
		@XmlElement(required = true)
		protected String type;
		
		@XmlElement(name = "override-file-date", required = true)
		@Mapping("overrideFileDateEnabled")
		protected String overrideFileDate;
		
		@XmlElement(name = "override-file-date-type")
		@Mapping("overrideFileDateType")
		protected String jaxbOverrideFileDateType;
		
		public String getOverrideFileDate() {
			return overrideFileDate;
		}

		public void setOverrideFileDate(String overrideFileDate) {
			this.overrideFileDate = overrideFileDate;
		}

		
		public String getJaxbOverrideFileDateType() {
			return jaxbOverrideFileDateType;
		}

		public void setJaxbOverrideFileDateType(String jaxbOverrideFileDateType) {
			this.jaxbOverrideFileDateType = jaxbOverrideFileDateType;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String value) {
			this.date = value;
		}

		public String getType() {
			return type;
		}

		public void setType(String value) {
			this.type = value;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "jaxbGroupingType", "forArchive" })
	public static class FileGrouping {

		@XmlElement(name = "grouping-type", required = true)
		protected String jaxbGroupingType;

		@XmlElement(name = "for-archive", required = true)
		@Mapping("fileGroupingParameter.fileGroupEnable")
		protected boolean forArchive;

		public String getJaxbGroupingType() {
			return jaxbGroupingType;
		}

		public void setJaxbGroupingType(String jaxbGroupingType) {
			this.jaxbGroupingType = jaxbGroupingType;
		}

		public boolean isForArchive() {
			return forArchive;
		}

		public void setForArchive(boolean forArchive) {
			this.forArchive = forArchive;
		}
		
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "path" })
	public static class PathList {

		@Mapping("svcPathList")
		protected List<ParsingServiceEntity.PathList.Path> path;

		public List<ParsingServiceEntity.PathList.Path> getPath() {
			if (path == null) {
				path = new ArrayList<>();
			}
			return this.path;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "readFilePath", "archivePath","pluginList" })
		public static class Path {

			@XmlElement(name = "read-file-path", required = true)
			@Mapping("readFilePath")
			protected String readFilePath;

			@XmlElement(name = "archive-path", required = true)
			@Mapping("archivePath")
			protected String archivePath;

			@XmlElement(name = "plugin-list", required = true)
			@Mapping("this")
			protected ParsingServiceEntity.PathList.Path.PluginList pluginList;

			public String getReadFilePath() {
				return readFilePath;
			}

			public void setReadFilePath(String value) {
				this.readFilePath = value;
			}

			public String getArchivePath() {
				return archivePath;
			}

			public void setArchivePath(String value) {
				this.archivePath = value;
			}

			public ParsingServiceEntity.PathList.Path.PluginList getPluginList() {
				return pluginList;
			}

			public void setPluginList(
					ParsingServiceEntity.PathList.Path.PluginList value) {
				this.pluginList = value;
			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "plugin" })
			public static class PluginList {

				@Mapping("parserWrappers")
				protected List<ParsingServiceEntity.PathList.Path.PluginList.Plugin> plugin;

				public List<ParsingServiceEntity.PathList.Path.PluginList.Plugin> getPlugin() {
					if (plugin == null) {
						plugin = new ArrayList<>();
					}
					return this.plugin;
				}

				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "", propOrder = { "pluginName",
						"readFilenameContains", "readFilenamePrefix",
						"maxFilesCountAlert", "isCompressedOutputFile",
						"writeFileSplitting", "readFilenameExcludeTypes",
						"isCompressedInputFile", "readFilenameSuffix","writeFilenamePrefix",
						"writeFilePath","writeCdrHeaderFooterEnabled","writeCdrDefaultAttributes" ,"pluginInstanceId" })
				public static class Plugin {

					@XmlElement(name = "plugin-name", required = true)
					@Mapping("parserType.alias")
					protected String pluginName;

					@XmlElement(name = "read-filename-contains", required = true)
					@Mapping("readFilenameContains")
					protected String readFilenameContains;

					@XmlElement(name = "read-filename-prefix", required = true)
					@Mapping("readFilenamePrefix")
					protected String readFilenamePrefix;

					@XmlElement(name = "max-files-count-alert", required = true)
					@Mapping("maxFileCountAlert")
					protected int maxFilesCountAlert;

					@XmlElement(name = "is-compressed-output-file", required = true)
					@Mapping("compressOutFileEnabled")
					protected boolean isCompressedOutputFile;

					@XmlElement(name = "write-file-splitting", required = true)
					@Mapping("writeFileSplit")
					protected String writeFileSplitting;

					@XmlElement(name = "read-filename-exclude-types", required = true)
					@Mapping("readFilenameExcludeTypes")
					protected String readFilenameExcludeTypes;

					@XmlElement(name = "is-compressed-input-file", required = true)
					@Mapping("compressInFileEnabled")
					protected boolean isCompressedInputFile;

					@XmlElement(name = "read-filename-suffix", required = true)
					@Mapping("readFilenameSuffix")
					protected String readFilenameSuffix;

					@XmlElement(name = "write-filename-prefix", required = true)
					@Mapping("writeFilenamePrefix")
					protected String writeFilenamePrefix;
					
					@XmlElement(name = "write-file-path", required = true)
					@Mapping("writeFilePath")
					protected String writeFilePath;

					@XmlElement(name = "plugin-instance-id", required = true)
					@Mapping("id")
					protected int pluginInstanceId;
					
					@XmlElement(name = "write-cdr-header-footer", required = true)
					@Mapping("writeCdrHeaderFooterEnabled")
					protected String writeCdrHeaderFooter;
					
					@XmlElement(name = "write-cdr-default-attributes", required = true)
					@Mapping("writeCdrDefaultAttributes")
					protected String writeCdrDefaultAttributes;

					public String getPluginName() {
						return pluginName;
					}

					public void setPluginName(String pluginName) {
						this.pluginName = pluginName;
					}

					public String getReadFilenameContains() {
						return readFilenameContains;
					}

					public void setReadFilenameContains(
							String readFilenameContains) {
						this.readFilenameContains = readFilenameContains;
					}

					public int getMaxFilesCountAlert() {
						return maxFilesCountAlert;
					}

					public void setMaxFilesCountAlert(int maxFilesCountAlert) {
						this.maxFilesCountAlert = maxFilesCountAlert;
					}

					public boolean isCompressedOutputFile() {
						return isCompressedOutputFile;
					}

					public void setCompressedOutputFile(
							boolean isCompressedOutputFile) {
						this.isCompressedOutputFile = isCompressedOutputFile;
					}

					public String getWriteFileSplitting() {
						return writeFileSplitting;
					}

					public void setWriteFileSplitting(String writeFileSplitting) {
						this.writeFileSplitting = writeFileSplitting;
					}

					public String getReadFilenameExcludeTypes() {
						return readFilenameExcludeTypes;
					}

					public void setReadFilenameExcludeTypes(
							String readFilenameExcludeTypes) {
						this.readFilenameExcludeTypes = readFilenameExcludeTypes;
					}

					public boolean isCompressedInputFile() {
						return isCompressedInputFile;
					}

					public void setCompressedInputFile(
							boolean isCompressedInputFile) {
						this.isCompressedInputFile = isCompressedInputFile;
					}

					public String getReadFilenameSuffix() {
						return readFilenameSuffix;
					}

					public void setReadFilenameSuffix(String readFilenameSuffix) {
						this.readFilenameSuffix = readFilenameSuffix;
					}

					public String getWriteFilePath() {
						return writeFilePath;
					}

					public void setWriteFilePath(String writeFilePath) {
						this.writeFilePath = writeFilePath;
					}

					public int getPluginInstanceId() {
						return pluginInstanceId;
					}

					public void setPluginInstanceId(int pluginInstanceId) {
						this.pluginInstanceId = pluginInstanceId;
					}

					public String getReadFilenamePrefix() {
						return readFilenamePrefix;
					}

					public void setReadFilenamePrefix(String readFilenamePrefix) {
						this.readFilenamePrefix = readFilenamePrefix;
					}
					
					public String getWriteCdrHeaderFooter() {
						return writeCdrHeaderFooter;
					}

					public void setWriteCdrHeaderFooter(
							String writeCdrHeaderFooter) {
						this.writeCdrHeaderFooter = writeCdrHeaderFooter;
					}
					
					public String getWriteCdrDefaultAttributes() {
						return writeCdrDefaultAttributes;
					}

					public void setWriteCdrDefaultAttributes(
							String writeCdrDefaultAttributes) {
						this.writeCdrDefaultAttributes = writeCdrDefaultAttributes;
					}

				}

			}

		}

	}

}
