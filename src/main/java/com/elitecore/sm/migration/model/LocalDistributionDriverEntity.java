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
@XmlType(name = "", propOrder = {
    "pathList",
    "maxRetryCount",
    "fileRange",
    "fileSequenceOrder",
    "noFilesAlertInterval"
})
@XmlRootElement(name = "local-distribution-driver")
public class LocalDistributionDriverEntity {

    @XmlElement(name = "path-list", required = true)
    protected LocalDistributionDriverEntity.PathList pathList;
    
    @XmlElement(name = "max-retry-count")
    protected byte maxRetryCount;
    
    @XmlElement(name = "file-range", required = true)
    protected String fileRange;
    
    @XmlElement(name = "file-sequence-order", required = true)
    protected String fileSequenceOrder;
    
    @XmlElement(name = "no-files-alert-interval")
    protected byte noFilesAlertInterval;

    
    public LocalDistributionDriverEntity.PathList getPathList() {
        return pathList;
    }

    
    public void setPathList(LocalDistributionDriverEntity.PathList value) {
        this.pathList = value;
    }

    
    public byte getMaxRetryCount() {
        return maxRetryCount;
    }

    
    public void setMaxRetryCount(byte value) {
        this.maxRetryCount = value;
    }

    
    public String getFileRange() {
        return fileRange;
    }

    
    public void setFileRange(String value) {
        this.fileRange = value;
    }

    
    public String getFileSequenceOrder() {
        return fileSequenceOrder;
    }

    
    public void setFileSequenceOrder(String value) {
        this.fileSequenceOrder = value;
    }

    
    public byte getNoFilesAlertInterval() {
        return noFilesAlertInterval;
    }

    
    public void setNoFilesAlertInterval(byte value) {
        this.noFilesAlertInterval = value;
    }


    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "path"
    })
    public static class PathList {

    	@Mapping("driverPathList")
        protected List<LocalDistributionDriverEntity.PathList.Path> path;

        
        public List<LocalDistributionDriverEntity.PathList.Path> getPath() {
            if (path == null) {
                path = new ArrayList<>();
            }
            return this.path;
        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "readFilePath",
            "archivePath",
            "readFilenamePrefix",
            "readFilenameSuffix",
            "maxFilesCountAlert",
            "readFilenameContains",
            "readFilenameExcludeTypes",
            "isCompressedInputFile",
            "isCompressedOutputFile",
            "writeFilePath",
            "pluginList"
        })
        public static class Path {

            @XmlElement(name = "read-file-path", required = true)
            @Mapping("readFilePath")
            protected String readFilePath;
            
            @XmlElement(name = "archive-path", required = true)
            @Mapping("archivePath")
            protected String archivePath;
            
            @XmlElement(name = "read-filename-prefix", required = true)
            @Mapping("readFilenamePrefix")
            protected String readFilenamePrefix;
            
            @XmlElement(name = "read-filename-suffix", required = true)
            @Mapping("readFilenameSuffix")
            protected String readFilenameSuffix;
            
            @XmlElement(name = "max-files-count-alert")
            @Mapping("maxFilesCountAlert")
            protected short maxFilesCountAlert;
            
            @XmlElement(name = "read-filename-contains", required = true)
            @Mapping("readFilenameContains")
            protected String readFilenameContains;
            
            @XmlElement(name = "read-filename-exclude-types", required = true)
            @Mapping("readFilenameExcludeTypes")
            protected String readFilenameExcludeTypes;
            
            @XmlElement(name = "is-compressed-input-file", required = true)
            @Mapping("compressInFileEnabled")
            protected String isCompressedInputFile;
            
            @XmlElement(name = "is-compressed-output-file", required = true)
            @Mapping("compressOutFileEnabled")
            protected String isCompressedOutputFile;
            
            @XmlElement(name = "write-file-path", required = true)
            @Mapping("writeFilePath")
            protected String writeFilePath;
            
            @XmlElement(name = "plugin-list", required = true)
            @Mapping("this")
            protected LocalDistributionDriverEntity.PathList.Path.PluginList pluginList;

            
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

            
            public short getMaxFilesCountAlert() {
                return maxFilesCountAlert;
            }

            
            public void setMaxFilesCountAlert(short value) {
                this.maxFilesCountAlert = value;
            }

            
            public String getReadFilenameContains() {
                return readFilenameContains;
            }

            
            public void setReadFilenameContains(String value) {
                this.readFilenameContains = value;
            }

            
            public String getReadFilenameExcludeTypes() {
                return readFilenameExcludeTypes;
            }

            
            public void setReadFilenameExcludeTypes(String value) {
                this.readFilenameExcludeTypes = value;
            }

            
            public String getIsCompressedInputFile() {
                return isCompressedInputFile;
            }

            
            public void setIsCompressedInputFile(String value) {
                this.isCompressedInputFile = value;
            }

            
            public String getIsCompressedOutputFile() {
                return isCompressedOutputFile;
            }

            
            public void setIsCompressedOutputFile(String value) {
                this.isCompressedOutputFile = value;
            }

            
            public String getWriteFilePath() {
                return writeFilePath;
            }

            
            public void setWriteFilePath(String value) {
                this.writeFilePath = value;
            }

            
            public LocalDistributionDriverEntity.PathList.Path.PluginList getPluginList() {
                return pluginList;
            }

            
            public void setPluginList(LocalDistributionDriverEntity.PathList.Path.PluginList value) {
                this.pluginList = value;
            }


            
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "plugin"
            })
            public static class PluginList {

            	@Mapping("composerWrappers")
                protected List<LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin> plugin;

                
                public List<LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin> getPlugin() {
                    if (plugin == null) {
                        plugin = new ArrayList<>();
                    }
                    return this.plugin;
                }


                
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "pluginName",
                    "pluginInstanceId",
                    "fileNamePrefix",
                    "fileNamePostfix",
                    "destinationPath",
                    "fileExtensionAfterRename",
                    "defaultFileExtRemoveEnabled",
                    "characterRenamingOperation"
                })
                public static class Plugin {

                    @XmlElement(name = "plugin-name", required = true)
                    @Mapping("composerType.alias")
                    protected String pluginName;
                    
                    @XmlElement(name = "plugin-instance-id")
                    @Mapping("id")
                    protected byte pluginInstanceId;
                    
                    @XmlElement(name = "file-name-prefix", required = true)
                    @Mapping("writeFilenamePrefix")
                    protected String fileNamePrefix;
                    
                    @XmlElement(name = "file-name-postfix", required = true)
                    @Mapping("writeFilenameSuffix")
                    protected String fileNamePostfix;
                    
                    @XmlElement(name = "destination-path", required = true)
                    @Mapping("destPath")
                    protected String destinationPath;
                    
                    @XmlElement(name = "file-extension-after-rename", required = true)
                    @Mapping("fileExtension")
                    protected String fileExtensionAfterRename;
                    
                    @XmlElement(name = "default-file-extension-remove-enabled", required = true)
                    @Mapping("defaultFileExtRemoveEnabled")
                    protected String defaultFileExtRemoveEnabled;
                    
                    @XmlElement(name = "character-renaming-operation", required = true)
                    @Mapping("this")
                    protected LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation characterRenamingOperation;

                    
                    public String getPluginName() {
                        return pluginName;
                    }

                    
                    public void setPluginName(String value) {
                        this.pluginName = value;
                    }

                    
                    public byte getPluginInstanceId() {
                        return pluginInstanceId;
                    }

                    
                    public void setPluginInstanceId(byte value) {
                        this.pluginInstanceId = value;
                    }

                    
                    public String getFileNamePrefix() {
                        return fileNamePrefix;
                    }

                    
                    public void setFileNamePrefix(String value) {
                        this.fileNamePrefix = value;
                    }

                    
                    public String getFileNamePostfix() {
                        return fileNamePostfix;
                    }

                    
                    public void setFileNamePostfix(String value) {
                        this.fileNamePostfix = value;
                    }

                    
                    public String getDestinationPath() {
                        return destinationPath;
                    }

                    
                    public void setDestinationPath(String value) {
                        this.destinationPath = value;
                    }

                    
                    public String getFileExtensionAfterRename() {
                        return fileExtensionAfterRename;
                    }

                    
                    public void setFileExtensionAfterRename(String value) {
                        this.fileExtensionAfterRename = value;
                    }
                    
                    public String getDefaultFileExtRemoveEnabled() {
						return defaultFileExtRemoveEnabled;
					}


					public void setDefaultFileExtRemoveEnabled(String defaultFileExtRemoveEnabled) {
						this.defaultFileExtRemoveEnabled = defaultFileExtRemoveEnabled;
					}

                    
                    public LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation getCharacterRenamingOperation() {
                        return characterRenamingOperation;
                    }

                    
                    public void setCharacterRenamingOperation(LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation value) {
                        this.characterRenamingOperation = value;
                    }


                    
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "character"
                    })
                    public static class CharacterRenamingOperation {

                        protected List<LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation.Character> character;

                        
                        public List<LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation.Character> getCharacter() {
                            if (character == null) {
                                character = new ArrayList<>();
                            }
                            return this.character;
                        }


                        
                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "", propOrder = {
                            "sequenceNo",
                            "query",
                            "position",
                            "startIndex",
                            "endIndex",
                            "padding",
                            "defaultValue",
                            "length"
                        })
                        public static class Character {

                            @XmlElement(name = "sequence-no")
                            @Mapping("sequenceNo")
                            protected byte sequenceNo;
                            
                            @XmlElement(required = true)
                            @Mapping("query")
                            protected String query;
                            
                            @XmlElement(required = true)
                            @Mapping("position")
                            protected String position;
                            
                            @XmlElement(name = "start-index")
                            @Mapping("startIndex")
                            protected byte startIndex;
                            
                            @XmlElement(name = "end-index")
                            @Mapping("endIndex")
                            protected byte endIndex;
                            
                            @XmlElement(required = true)
                            protected LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation.Character.Padding padding;
                            
                            @XmlElement(name = "default-value", required = true)
                            @Mapping("defaultValue")
                            protected String defaultValue;
                            
                            @XmlElement(name = "length", required = true)
                            @Mapping("length")
                            protected byte length;

                            
                            public byte getSequenceNo() {
                                return sequenceNo;
                            }

                            
                            public void setSequenceNo(byte value) {
                                this.sequenceNo = value;
                            }

                            
                            public String getQuery() {
                                return query;
                            }

                            
                            public void setQuery(String value) {
                                this.query = value;
                            }

                            
                            public String getPosition() {
                                return position;
                            }

                            
                            public void setPosition(String value) {
                                this.position = value;
                            }

                            
                            public byte getStartIndex() {
                                return startIndex;
                            }

                            
                            public void setStartIndex(byte value) {
                                this.startIndex = value;
                            }

                            
                            public byte getEndIndex() {
                                return endIndex;
                            }

                            
                            public void setEndIndex(byte value) {
                                this.endIndex = value;
                            }

                            
                            public LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation.Character.Padding getPadding() {
                                return padding;
                            }

                            
                            public void setPadding(LocalDistributionDriverEntity.PathList.Path.PluginList.Plugin.CharacterRenamingOperation.Character.Padding value) {
                                this.padding = value;
                            }

                            
                            public String getDefaultValue() {
                                return defaultValue;
                            }

                            
                            public void setDefaultValue(String value) {
                                this.defaultValue = value;
                            }

                            
                            public byte getLength() {
                                return length;
                            }

                            
                            public void setLength(byte value) {
                                this.length = value;
                            }


                            
                            @XmlAccessorType(XmlAccessType.FIELD)
                            @XmlType(name = "", propOrder = {
                                "value"
                            })
                            public static class Padding {

                                @XmlValue
                                protected String value;
                                @XmlAttribute(name = "type")
                                protected String type;

                                
                                public String getValue() {
                                    return value;
                                }

                                
                                public void setValue(String value) {
                                    this.value = value;
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

                }

            }

        }

    }

}
