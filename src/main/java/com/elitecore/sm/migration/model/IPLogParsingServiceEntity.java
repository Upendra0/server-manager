package com.elitecore.sm.migration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dozer.Mapping;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
"sourcePath","sortingType","sortingCriteria","isCompressed","minimumThread","maximumThread","fileBatchSize","queueSize","recordBatchSize","startupMode",
"executionInterval","immediateExecuteOnStartup","sourceFileTypes","selectFileOnPrefixes","selectFileOnSuffixes","fileCompression",
"pluginName","fileGrouping","excludeFileTypes","syslogOutputConfiguration","equalCheckField","equalCheckValue","outputFileHeader","fileStatistics","preCorrelation"
})
@XmlRootElement(name = "iplog-parsing-service")
public class IPLogParsingServiceEntity {

    @XmlElement(name = "source-path", required = true)
    protected String sourcePath;
    
    @XmlElement(name = "sorting-type", required = true)
    @Mapping("svcExecParams.sortingType")
    protected String sortingType;
    
    @XmlElement(name = "sorting-criteria", required = true)
    @Mapping("svcExecParams.sortingCriteria")
    protected String sortingCriteria;
    
    @XmlElement(name = "is-compressed", required = true)
    protected String isCompressed;
    
    @XmlElement(name = "minimum-thread")
    @Mapping("svcExecParams.minThread")
    protected int minimumThread;
    
    @XmlElement(name = "maximum-thread")
    @Mapping("svcExecParams.maxThread")
    protected int maximumThread;
    
    @XmlElement(name = "file-batch-size")
    @Mapping("svcExecParams.fileBatchSize")
    protected int fileBatchSize;
    
    @XmlElement(name = "queue-size")
    @Mapping("svcExecParams.queueSize")
    protected int queueSize;
    
    @XmlElement(name = "record-batch-size")
    @Mapping("recordBatchSize")
    protected int recordBatchSize;
    
    @XmlElement(name = "startup-mode", required = true)
    protected String startupMode;
    
    @XmlElement(name = "execution-interval")
    @Mapping("svcExecParams.executionInterval")
    protected int executionInterval;
    
    @XmlElement(name = "immediate-execute-on-startup", required = true)
    @Mapping("svcExecParams.executeOnStartup")
    protected String immediateExecuteOnStartup;
    
    @XmlElement(name = "source-file-types", required = true)
    protected String sourceFileTypes;
    
    @XmlElement(name = "select-file-on-prefixes", required = true)
    protected String selectFileOnPrefixes;
    
    @XmlElement(name = "select-file-on-suffixes", required = true)
    protected String selectFileOnSuffixes;
    
    @XmlElement(name = "file-compression", required = true)
    protected String fileCompression;
    
    @XmlElement(name = "plugin-name", required = true)
    protected String pluginName;
    
    @XmlElement(name = "file-grouping", required = true)
    @Mapping("this")
    protected IPLogParsingServiceEntity.FileGrouping fileGrouping;
    
    @XmlElement(name = "exclude-file-types", required = true)
    protected String excludeFileTypes;
    
    @XmlElement(name = "syslog-output-configuration", required = true)
    @Mapping("this") 
    protected IPLogParsingServiceEntity.SyslogOutputConfiguration syslogOutputConfiguration;
    
    @XmlElement(name = "equal-check-field", required = true)
    protected String equalCheckField;
    
    @XmlElement(name = "equal-check-value", required = true)
    @Mapping("equalCheckValue")
    protected String equalCheckValue;
    
    @XmlElement(name = "output-file-header", required = true)
    @Mapping("outFileHeaders")
    protected String outputFileHeader;
    
    @XmlElement(name = "file-statistics", required = true)
    @Mapping("this")
    protected IPLogParsingServiceEntity.FileStatistics fileStatistics;
    
    @XmlElement(name = "pre-correlation", required = true)
    @Mapping("this")
    protected IPLogParsingServiceEntity.PreCorrelation preCorrelation;

    
    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String value) {
        this.sourcePath = value;
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

    public String getIsCompressed() {
        return isCompressed;
    }

    public void setIsCompressed(String value) {
        this.isCompressed = value;
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

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int value) {
        this.queueSize = value;
    }

    public int getRecordBatchSize() {
        return recordBatchSize;
    }

    public void setRecordBatchSize(int value) {
        this.recordBatchSize = value;
    }

    public String getStartupMode() {
        return startupMode;
    }

    public void setStartupMode(String value) {
        this.startupMode = value;
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

    public String getSourceFileTypes() {
        return sourceFileTypes;
    }

    public void setSourceFileTypes(String value) {
        this.sourceFileTypes = value;
    }

    public String getSelectFileOnPrefixes() {
        return selectFileOnPrefixes;
    }

    public void setSelectFileOnPrefixes(String value) {
        this.selectFileOnPrefixes = value;
    }

    public String getSelectFileOnSuffixes() {
        return selectFileOnSuffixes;
    }

    public void setSelectFileOnSuffixes(String value) {
        this.selectFileOnSuffixes = value;
    }

    public String getFileCompression() {
        return fileCompression;
    }

    public void setFileCompression(String value) {
        this.fileCompression = value;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String value) {
        this.pluginName = value;
    }

    public IPLogParsingServiceEntity.FileGrouping getFileGrouping() {
        return fileGrouping;
    }

    public void setFileGrouping(IPLogParsingServiceEntity.FileGrouping value) {
        this.fileGrouping = value;
    }

    public String getExcludeFileTypes() {
        return excludeFileTypes;
    }

    public void setExcludeFileTypes(String value) {
        this.excludeFileTypes = value;
    }

    public IPLogParsingServiceEntity.SyslogOutputConfiguration getSyslogOutputConfiguration() {
        return syslogOutputConfiguration;
    }

    public void setSyslogOutputConfiguration(IPLogParsingServiceEntity.SyslogOutputConfiguration value) {
        this.syslogOutputConfiguration = value;
    }


    public String getEqualCheckField() {
        return equalCheckField;
    }

    public void setEqualCheckField(String value) {
        this.equalCheckField = value;
    }

    public String getEqualCheckValue() {
        return equalCheckValue;
    }

    public void setEqualCheckValue(String value) {
        this.equalCheckValue = value;
    }

    public String getOutputFileHeader() {
        return outputFileHeader;
    }

    public void setOutputFileHeader(String value) {
        this.outputFileHeader = value;
    }

    public IPLogParsingServiceEntity.PreCorrelation getPreCorrelation() {
        return preCorrelation;
    }

    public void setPreCorrelation(IPLogParsingServiceEntity.PreCorrelation value) {
        this.preCorrelation = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"groupingType","forArchive","archivePath" })
    public static class FileGrouping {

        @XmlElement(name = "grouping-type", required = true)
        protected String groupingType;
        
        @XmlElement(name = "for-archive", required = true)
        //@Mapping("fileGroupingParameter.fileGroupEnable")
        protected String forArchive;
        
        @XmlElement(name = "archive-path", required = true)
        @Mapping("fileGroupingParameter.archivePath")
        protected String archivePath;
        
        @XmlAttribute(name = "enabled")
        @Mapping("fileGroupingParameter.fileGroupEnable")
        protected String enabled;

        public String getGroupingType() {
            return groupingType;
        }

        public void setGroupingType(String value) {
            this.groupingType = value;
        }

        public String getForArchive() {
            return forArchive;
        }

        public void setForArchive(String value) {
            this.forArchive = value;
        }

        public String getArchivePath() {
            return archivePath;
        }

        public void setArchivePath(String value) {
            this.archivePath = value;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String value) {
            this.enabled = value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "fileStatisticsPath"})
    public static class FileStatistics {

        @XmlElement(name = "file-statistics-path", required = true)
        @Mapping("fileStatsLoc")
        protected String fileStatisticsPath;
        
        @XmlAttribute(name = "enabled")
        @Mapping("fileStatsEnabled")
        protected String enabled;

        public String getFileStatisticsPath() {
            return fileStatisticsPath;
        }

        public void setFileStatisticsPath(String value) {
            this.fileStatisticsPath = value;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String value) {
            this.enabled = value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "cdrTypeField","destinationPortField","destinationPortsFilter","cDataDestinationPath",
    		"dDataDestinationPath" })
    public static class PreCorrelation {

        @XmlElement(name = "cdr-type-field", required = true)
        @Mapping("mappedSourceField")
        protected String cdrTypeField;
        
        @XmlElement(name = "destination-port-field", required = true)
        protected String destinationPortField;
        
        @XmlElement(name = "destination-ports-filter", required = true)
        protected String destinationPortsFilter;
        
        @XmlElement(name = "c-data-destination-path", required = true)
        @Mapping("createRecDestPath")
        protected String cDataDestinationPath;
        
        @XmlElement(name = "d-data-destination-path", required = true)
        @Mapping("deleteRecDestPath")
        protected String dDataDestinationPath;
        
        @XmlAttribute(name = "enabled")
        @Mapping("correlEnabled")
        protected String enabled;

        public String getCdrTypeField() {
            return cdrTypeField;
        }

        public void setCdrTypeField(String value) {
            this.cdrTypeField = value;
        }

        public String getDestinationPortField() {
            return destinationPortField;
        }

        public void setDestinationPortField(String value) {
            this.destinationPortField = value;
        }

        public String getDestinationPortsFilter() {
            return destinationPortsFilter;
        }

        public void setDestinationPortsFilter(String value) {
            this.destinationPortsFilter = value;
        }

        public String getCDataDestinationPath() {
            return cDataDestinationPath;
        }

        public void setCDataDestinationPath(String value) {
            this.cDataDestinationPath = value;
        }

        public String getDDataDestinationPath() {
            return dDataDestinationPath;
        }

        public void setDDataDestinationPath(String value) {
            this.dDataDestinationPath = value;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String value) {
            this.enabled = value;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "destinationDirectoryPath","filePartition","inprocessFilePurge" })
    public static class SyslogOutputConfiguration {

        @XmlElement(name = "destination-directory-path", required = true)
        protected String destinationDirectoryPath;
        
        @XmlElement(name = "file-partition", required = true)
        @Mapping("this")
        protected IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition filePartition;
        
        @XmlElement(name = "inprocess-file-purge", required = true)
        @Mapping("this")
        protected IPLogParsingServiceEntity.SyslogOutputConfiguration.InprocessFilePurge inprocessFilePurge;

        public String getDestinationDirectoryPath() {
            return destinationDirectoryPath;
        }

        public void setDestinationDirectoryPath(String value) {
            this.destinationDirectoryPath = value;
        }

        public IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition getFilePartition() {
            return filePartition;
        }

        public void setFilePartition(IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition value) {
            this.filePartition = value;
        }

        public IPLogParsingServiceEntity.SyslogOutputConfiguration.InprocessFilePurge getInprocessFilePurge() {
            return inprocessFilePurge;
        }

        public void setInprocessFilePurge(IPLogParsingServiceEntity.SyslogOutputConfiguration.InprocessFilePurge value) {
            this.inprocessFilePurge = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"fieldSeperator","partitionType","fieldBasedHashPartition"

        })
        public static class FilePartition {

            @XmlElement(name = "field-seperator", required = true)
            @Mapping("hashSeparator")
            protected String fieldSeperator;
            
            @XmlElement(name = "partition-type", required = true)
            @Mapping("indexType")
            protected String partitionType;
            
            @XmlElement(name = "field-based-hash-partition", required = true)
            @Mapping("this")
            protected IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition.FieldBasedHashPartition fieldBasedHashPartition;

            public String getFieldSeperator() {
                return fieldSeperator;
            }

            public void setFieldSeperator(String value) {
                this.fieldSeperator = value;
            }

            public String getPartitionType() {
                return partitionType;
            }

            public void setPartitionType(String value) {
                this.partitionType = value;
            }

            public IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition.FieldBasedHashPartition getFieldBasedHashPartition() {
                return fieldBasedHashPartition;
            }

            public void setFieldBasedHashPartition(IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition.FieldBasedHashPartition value) {
                this.fieldBasedHashPartition = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = { "keyField" })
            public static class FieldBasedHashPartition {

                @XmlElement(name = "key-field", required = true)
                @Mapping("partionParamList")
                protected List<IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition.FieldBasedHashPartition.KeyField> keyField;

                public List<IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition.FieldBasedHashPartition.KeyField> getKeyField() {
                    return keyField;
                }
               
                public void setKeyField(List<IPLogParsingServiceEntity.SyslogOutputConfiguration.FilePartition.FieldBasedHashPartition.KeyField> value) {
                    this.keyField = value;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {"unifiedFieldName", "range" })
                public static class KeyField {

                    @XmlElement(name = "unified-field-name", required = true)
                    protected String unifiedFieldName;
                    
                    @XmlElement(name="range", required = true)
                    @Mapping("partitionRange")
                    protected String range;
                    
                    @XmlAttribute(name = "type" , required = true)
                    protected String type;

                    public String getUnifiedFieldName() {
                        return unifiedFieldName;
                    }

                    public void setUnifiedFieldName(String value) {
                        this.unifiedFieldName = value;
                    }

                    public String getRange() {
                        return range;
                    }

                    public void setRange(String value) {
                        this.range = value;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String value) {
                        this.type = value;
                    }

                }

            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"purgeInterval","maxRenameInterval"   })
        public static class InprocessFilePurge {

            @XmlElement(name = "purge-interval")
            @Mapping("purgeInterval")
            protected int purgeInterval;
            
            @XmlElement(name = "max-rename-interval")
            @Mapping("purgeDelayInterval")
            protected int maxRenameInterval;

            public int getPurgeInterval() {
                return purgeInterval;
            }

            public void setPurgeInterval(int value) {
                this.purgeInterval = value;
            }

            public int getMaxRenameInterval() {
                return maxRenameInterval;
            }

            public void setMaxRenameInterval(int value) {
                this.maxRenameInterval = value;
            }

        }

    }

}
