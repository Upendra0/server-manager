

package com.elitecore.sm.migration.model;

import java.util.ArrayList;
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
    "instance"
})
@XmlRootElement(name = "ascii-composer-plugin")
public class AsciiComposerPluginEntity {

	
	@Mapping("this")
    protected List<AsciiComposerPluginEntity.Instance> instance;

    
    public List<AsciiComposerPluginEntity.Instance> getInstance() {
        if (instance == null) {
            instance = new ArrayList<>();
        }
        return this.instance;
    }


    
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "fileHeader",
        "destinationCharsetName",
        "destinationDateFormat",
        "fieldSeparator",
        "destinationFileExtension",
        "attributeList"
    })
    public static class Instance {

        @XmlElement(name = "file-header", required = true)
        protected AsciiComposerPluginEntity.Instance.FileHeader fileHeader;
        
        @XmlElement(name = "destination-charset-name", required = true)
        protected String destinationCharsetName;
        
        @XmlElement(name = "destination-date-format", required = true)
        @Mapping("destDateFormat")
        protected String destinationDateFormat;
        
        @XmlElement(name = "field-separator", required = true)
        @Mapping("fieldSeparator")
        protected String fieldSeparator;
        
        @XmlElement(name = "destination-file-extension", required = true)
        @Mapping("destFileExt")
        protected String destinationFileExtension;
        
        @XmlElement(name = "attribute-list", required = true)
        protected AsciiComposerPluginEntity.Instance.AttributeList attributeList;
        
        @XmlAttribute(name = "id")
        @Mapping("id")
        protected Integer id;

        
        public AsciiComposerPluginEntity.Instance.FileHeader getFileHeader() {
            return fileHeader;
        }

        
        public void setFileHeader(AsciiComposerPluginEntity.Instance.FileHeader value) {
            this.fileHeader = value;
        }

        
        public String getDestinationCharsetName() {
            return destinationCharsetName;
        }

        
        public void setDestinationCharsetName(String value) {
            this.destinationCharsetName = value;
        }

        
        public String getDestinationDateFormat() {
            return destinationDateFormat;
        }

        
        public void setDestinationDateFormat(String value) {
            this.destinationDateFormat = value;
        }

        
        public String getFieldSeparator() {
            return fieldSeparator;
        }

        
        public void setFieldSeparator(String value) {
            this.fieldSeparator = value;
        }

        
        public String getDestinationFileExtension() {
            return destinationFileExtension;
        }

        
        public void setDestinationFileExtension(String value) {
            this.destinationFileExtension = value;
        }

        
        public AsciiComposerPluginEntity.Instance.AttributeList getAttributeList() {
            return attributeList;
        }

        
        public void setAttributeList(AsciiComposerPluginEntity.Instance.AttributeList value) {
            this.attributeList = value;
        }

        
        public Integer getId() {
            return id;
        }

        
        public void setId(Integer value) {
            this.id = value;
        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "attribute"
        })
        public static class AttributeList {

        	//@Mapping("attributeList")
            protected List<AsciiComposerPluginEntity.Instance.AttributeList.Attribute> attribute;

            
            public List<AsciiComposerPluginEntity.Instance.AttributeList.Attribute> getAttribute() {
                if (attribute == null) {
                    attribute = new ArrayList<>();
                }
                return this.attribute;
            }


            
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "description",
                "sequenceNumber",
                "destinationField",
                "unifiedField",
                "replaceConditionList",
                "defaultValue",
                "dateFormat",
                "mydataType",
                "trimChars",
                "trimPosition",
                "padding"
            })
            public static class Attribute {

                @XmlElement(required = true)
                @Mapping("description")
                protected String description;
                
                @XmlElement(name = "sequence-number")
                @Mapping("sequenceNumber")
                protected int sequenceNumber;
                
                @XmlElement(name = "destination-field", required = true)
                @Mapping("destinationField")
                protected String destinationField;
                
                @XmlElement(name = "unified-field", required = true)
                @Mapping("unifiedField")
                protected String unifiedField;
                
                @XmlElement(name = "replace-condition-list", required = true)
                @Mapping("replaceConditionList")
                protected String replaceConditionList;
                
                @XmlElement(name = "default-value", required = true)
                @Mapping("defualtValue")
                protected String defaultValue;
                
                @XmlElement(name = "date-format", required = true)
                @Mapping("dateFormat")
                protected String dateFormat;
                
                @XmlElement(name = "data-type", required = true)
                //@Mapping("dataType")
                protected String mydataType;
                
                @XmlElement(name = "trim-chars", required = true)
                @Mapping("trimchars")
                protected String trimChars;
				
				@XmlElement(name = "trim-position", required = true)
               // @Mapping("trimPosition")
                protected String trimPosition;
                
                
                @XmlElement(required = true)
                protected AsciiComposerPluginEntity.Instance.AttributeList.Attribute.Padding padding;

                
                public String getDescription() {
                    return description;
                }

                
                public void setDescription(String value) {
                    this.description = value;
                }

                
                public int getSequenceNumber() {
                    return sequenceNumber;
                }

                
                public void setSequenceNumber(int value) {
                    this.sequenceNumber = value;
                }

                
                public String getDestinationField() {
                    return destinationField;
                }

                
                public void setDestinationField(String value) {
                    this.destinationField = value;
                }

                
                public String getUnifiedField() {
                    return unifiedField;
                }

                
                public void setUnifiedField(String value) {
                    this.unifiedField = value;
                }

                
                public String getReplaceConditionList() {
                    return replaceConditionList;
                }

                
                public void setReplaceConditionList(String value) {
                    this.replaceConditionList = value;
                }

                
                public String getDefaultValue() {
                    return defaultValue;
                }

                
                public void setDefaultValue(String value) {
                    this.defaultValue = value;
                }

                
                public String getDateFormat() {
                    return dateFormat;
                }

                
                public void setDateFormat(String value) {
                    this.dateFormat = value;
                }

                
                public String getTrimChars() {
                    return trimChars;
                }

                
                public void setTrimChars(String value) {
                    this.trimChars = value;
                }

                
                public String getTrimPosition() {
					return trimPosition;
				}


				
				public void setTrimPosition(String trimPosition) {
					this.trimPosition = trimPosition;
				}



				
                public AsciiComposerPluginEntity.Instance.AttributeList.Attribute.Padding getPadding() {
                    return padding;
                }

                
                public void setPadding(AsciiComposerPluginEntity.Instance.AttributeList.Attribute.Padding value) {
                    this.padding = value;
                }

                
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "length",
                    "paddingType",
                    "paddingChar",
                    "prefix",
                    "suffix"
                })
                public static class Padding {

                	@XmlElement(name = "length", required = true)
                	@Mapping("length")
                    protected int length;
                    
                    @XmlElement(name = "padding-type", required = false)
                    protected String paddingType;
                    
                    @XmlElement(name = "padding-char", required = true)
                    @Mapping("paddingChar")
                    protected String paddingChar;
                    
                    @XmlElement(required = true)
                    @Mapping("prefix")
                    protected String prefix;
                    
                    @XmlElement(required = true)
                    @Mapping("suffix")
                    protected String suffix;
                    
                    @XmlAttribute(name = "enable")
                    @Mapping("paddingEnable")
                    protected String enable;

                    
                    public int getLength() {
                        return length;
                    }

                    
                    public void setLength(int value) {
                        this.length = value;
                    }

                    
                    public String getPaddingType() {
                        return paddingType;
                    }

                    
                    public void setPaddingType(String value) {
                        this.paddingType = value;
                    }

                    
                    public String getPaddingChar() {
                        return paddingChar;
                    }

                    
                    public void setPaddingChar(String value) {
                        this.paddingChar = value;
                    }

                    
                    public String getPrefix() {
                        return prefix;
                    }

                    
                    public void setPrefix(String value) {
                        this.prefix = value;
                    }

                    
                    public String getSuffix() {
                        return suffix;
                    }

                    
                    public void setSuffix(String value) {
                        this.suffix = value;
                    }

                    
                    public String getEnable() {
                        return enable;
                    }

                    
                    public void setEnable(String value) {
                        this.enable = value;
                    }

                }



				
				/**
				 * @return the mydataType
				 */
				public String getMydataType() {
					return mydataType;
				}


				
				/**
				 * @param mydataType the mydataType to set
				 */
				public void setMydataType(String mydataType) {
					this.mydataType = mydataType;
				}

            }

        }


        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "myfileHeaderParser",
            "containsFields"
        })
        public static class FileHeader {

            @XmlElement(name = "file-header-parser", required = true)
            protected String myfileHeaderParser;
            
            @XmlElement(name = "contains-fields", required = true)
            @Mapping("fileHeaderContainsFields")
            protected String containsFields;
            
            @XmlAttribute(name = "present")
            @Mapping("fileFooterEnable")
            protected String present;

            
            public String getContainsFields() {
                return containsFields;
            }

            
            public void setContainsFields(String value) {
                this.containsFields = value;
            }

            
            public String getPresent() {
                return present;
            }

            
            public void setPresent(String value) {
                this.present = value;
            }

			public String getMyfileHeaderParser() {
				return myfileHeaderParser;
			}
			
			public void setMyfileHeaderParser(String myfileHeaderParser) {
				this.myfileHeaderParser = myfileHeaderParser;
			}

        }

    }

}
