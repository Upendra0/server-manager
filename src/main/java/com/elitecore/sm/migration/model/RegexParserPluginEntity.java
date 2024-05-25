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
@XmlType(name = "", propOrder = { "instance"})
@XmlRootElement(name = "regex-parser-plugin")
public class RegexParserPluginEntity {

	
	protected List<RegexParserPluginEntity.Instance> instance;

	public List<RegexParserPluginEntity.Instance> getInstance() {
		if (instance == null) {
			instance = new ArrayList<RegexParserPluginEntity.Instance>();
		}
		return this.instance;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "sourceCharsetName","sourceDateFormat","logPatternRegex","logPatternRegexid","patternList" })
	public static class Instance {
	
	    @XmlElement(name = "source-charset-name", required = true)
	    protected String sourceCharsetName;
	    
	    @XmlElement(name = "source-date-format", required = true)
	    @Mapping("srcDateFormat")
	    protected String sourceDateFormat;
	    
	    @XmlElement(name = "log-pattern-regex", required = true)
	    @Mapping("logPatternRegex")
	    protected String logPatternRegex;
	    
	    @XmlElement(name = "log-pattern-regexid", required = true)
	    @Mapping("logPatternRegexId")
	    protected String logPatternRegexid;
	   
	    @XmlAttribute(name = "id")
	  	@Mapping("id")
		protected Integer id;
	    
	    @XmlElement(name = "pattern-list", required = true)
	    protected RegexParserPluginEntity.Instance.PatternList patternList;
	    
	  
	    
	    public String getSourceCharsetName() {
	        return sourceCharsetName;
	    }

	    public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setSourceCharsetName(String value) {
	        this.sourceCharsetName = value;
	    }

	    public String getSourceDateFormat() {
	        return sourceDateFormat;
	    }

	    public void setSourceDateFormat(String value) {
	        this.sourceDateFormat = value;
	    }

	    public String getLogPatternRegex() {
	        return logPatternRegex;
	    }


	    public void setLogPatternRegex(String value) {
	        this.logPatternRegex = value;
	    }

	    public String getLogPatternRegexid() {
	        return logPatternRegexid;
	    }

	    public void setLogPatternRegexid(String value) {
	        this.logPatternRegexid = value;
	    }

	    public RegexParserPluginEntity.Instance.PatternList getPatternList() {
	        return patternList;
	    }

	    public void setPatternList(RegexParserPluginEntity.Instance.PatternList value) {
	        this.patternList = value;
	    }
	    
	 
	    @XmlAccessorType(XmlAccessType.FIELD)
	    @XmlType(name = "", propOrder = { "pattern" })
	    public static class PatternList {

	        @XmlElement(required = true)
	      //  @Mapping("patternList")
	        protected List<RegexParserPluginEntity.Instance.PatternList.Pattern> pattern;

	        public List<RegexParserPluginEntity.Instance.PatternList.Pattern> getPattern() {
	            return pattern;
	        }

	        public void setPattern(List<RegexParserPluginEntity.Instance.PatternList.Pattern> value) {
	            this.pattern = value;
	        }

	        @XmlAccessorType(XmlAccessType.FIELD)
	        @XmlType(name = "", propOrder = { "patternRegexid","patternRegex","attributeList"})
	        public static class Pattern {

	            @XmlElement(name = "pattern-regexid", required = true)
	            protected String patternRegexid;
	            
	            @XmlElement(name = "pattern-regex", required = true)
	            protected String patternRegex;
	            
	            @XmlElement(name = "attribute-list", required = true)
	            protected RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList attributeList;

	            public String getPatternRegexid() {
	                return patternRegexid;
	            }

	            public void setPatternRegexid(String value) {
	                this.patternRegexid = value;
	            }

	            public String getPatternRegex() {
	                return patternRegex;
	            }

	            public void setPatternRegex(String value) {
	                this.patternRegex = value;
	            }

	            public RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList getAttributeList() {
	                return attributeList;
	            }

	            public void setAttributeList(RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList value) {
	                this.attributeList = value;
	            }

	            @XmlAccessorType(XmlAccessType.FIELD)
	            @XmlType(name = "", propOrder = {"attribute" })
	            public static class AttributeList {

	        		@XmlElement(name = "attribute", required = true)
	                protected List<RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList.Attribute> attribute;

	                public List<RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList.Attribute> getAttribute() {
	                    return attribute;
	                }

	                public void setAttribute(List<RegexParserPluginEntity.Instance.PatternList.Pattern.AttributeList.Attribute> value) {
	                    this.attribute = value;
	                }

	                @XmlAccessorType(XmlAccessType.FIELD)
	                @XmlType(name = "", propOrder = { "unifiedField","sequenceNo","regex","defaultValue","trimChars","description"})
	                public static class Attribute {

	                    @XmlElement(name = "unified-field", required = true)
	                    protected String unifiedField;
	                    
	                    @XmlElement(name = "sequence-no")
	                    protected byte sequenceNo;
	                    
	                    @XmlElement(name="regex", required = true)
	                    protected String regex;
	                    
	                    @XmlElement(name = "default-value", required = true)
	                    protected String defaultValue;
	                    
	                    @XmlElement(name = "trim-chars", required = true)
	                    protected String trimChars;

	    				@XmlElement(name = "description", required = true)
	    				protected String description;
	                    
	    				public String getDescription() {
	    					return description;
	    				}

	    				public void setDescription(String description) {
	    					this.description = description;
	    				}
	    				
	                    public String getUnifiedField() {
	                        return unifiedField;
	                    }

	                    public void setUnifiedField(String value) {
	                        this.unifiedField = value;
	                    }

	                    public byte getSequenceNo() {
	                        return sequenceNo;
	                    }

	                    public void setSequenceNo(byte value) {
	                        this.sequenceNo = value;
	                    }

	                    public String getRegex() {
	                        return regex;
	                    }

	                    public void setRegex(String value) {
	                        this.regex = value;
	                    }

	                    public String getDefaultValue() {
	                        return defaultValue;
	                    }

	                    public void setDefaultValue(String value) {
	                        this.defaultValue = value;
	                    }

	                    public String getTrimChars() {
	                        return trimChars;
	                    }

	                    public void setTrimChars(String value) {
	                        this.trimChars = value;
	                    }

	                }

	            }

	        }

	    }
	}
}
