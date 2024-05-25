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
@XmlType(name = "", propOrder = {"instance"})
@XmlRootElement(name = "natflow-parser-plugin")
public class NatflowParserPluginEntity {

    @XmlElement(required = true)
    protected List<NatflowParserPluginEntity.Instance> instance;

    public List<NatflowParserPluginEntity.Instance> getInstance() {
		if (instance == null) {
			instance = new ArrayList<NatflowParserPluginEntity.Instance>();
		}
		return this.instance;
	}

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"readTemplateInitialy","optionTemplateLookup","attributeList","id"})
    public static class Instance {

        @XmlElement(name = "read-template-initialy", required = true)
        @Mapping("readTemplatesInitially")
        protected String readTemplateInitialy;
        
        @XmlElement(name = "option-template-lookup", required = true)
        @Mapping("this")
        protected NatflowParserPluginEntity.Instance.OptionTemplateLookup optionTemplateLookup;
        
        @XmlElement(name = "attribute-list", required = true)
        @Mapping("this")
        protected NatflowParserPluginEntity.Instance.AttributeList attributeList;
        
        @XmlAttribute(name = "id")
        @Mapping("id")
        protected Integer id;

        public String getReadTemplateInitialy() {
            return readTemplateInitialy;
        }

        public void setReadTemplateInitialy(String value) {
            this.readTemplateInitialy = value;
        }

        public NatflowParserPluginEntity.Instance.OptionTemplateLookup getOptionTemplateLookup() {
            return optionTemplateLookup;
        }

        public void setOptionTemplateLookup(NatflowParserPluginEntity.Instance.OptionTemplateLookup value) {
            this.optionTemplateLookup = value;
        }

        public NatflowParserPluginEntity.Instance.AttributeList getAttributeList() {
            return attributeList;
        }

        public void setAttributeList(NatflowParserPluginEntity.Instance.AttributeList value) {
            this.attributeList = value;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer value) {
            this.id = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {"attribute"})
        public static class AttributeList {

        	@Mapping("parserAttributes")
            protected List<NatflowParserPluginEntity.Instance.AttributeList.Attribute> attribute;

            public List<NatflowParserPluginEntity.Instance.AttributeList.Attribute> getAttribute() {
                if (attribute == null) {
                    attribute = new ArrayList<NatflowParserPluginEntity.Instance.AttributeList.Attribute>();
                }
                return this.attribute;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"unifiedField","sourceField","defaultValue","trimChars","trimPosition","description" })
            public static class Attribute {

                @XmlElement(name = "unified-field", required = true)
                protected String unifiedField;
                
                @XmlElement(name = "source-field", required = true)
                protected String sourceField;
                
                @XmlElement(name = "default-value", required = true)
                protected String defaultValue;
                
                @XmlElement(name = "trim-chars", required = true)
                protected String trimChars;
                
                @XmlElement(name = "trim-position", required = true)
                protected String trimPosition;
                
                @XmlElement(required = true)
                protected String description;

                public String getUnifiedField() {
                    return unifiedField;
                }

                public void setUnifiedField(String value) {
                    this.unifiedField = value;
                }

                public String getSourceField() {
                    return sourceField;
                }

                public void setSourceField(String value) {
                    this.sourceField = value;
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

                public String getTrimPosition() {
                    return trimPosition;
                }
                
                public void setTrimPosition(String value) {
                    this.trimPosition = value;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String value) {
                    this.description = value;
                }

            }

        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "optionTemplate","template" })
        public static class OptionTemplateLookup {

            @XmlElement(name = "option-template", required = true)
            @Mapping("this")
            protected NatflowParserPluginEntity.Instance.OptionTemplateLookup.OptionTemplate optionTemplate;
            
            @XmlElement(required = true)
            @Mapping("this")
            protected NatflowParserPluginEntity.Instance.OptionTemplateLookup.Template template;
            
            @XmlAttribute(name = "enable")
            @Mapping("optionTemplateEnable")
            protected String enable;

            public NatflowParserPluginEntity.Instance.OptionTemplateLookup.OptionTemplate getOptionTemplate() {
                return optionTemplate;
            }

            public void setOptionTemplate(NatflowParserPluginEntity.Instance.OptionTemplateLookup.OptionTemplate value) {
                this.optionTemplate = value;
            }

            public NatflowParserPluginEntity.Instance.OptionTemplateLookup.Template getTemplate() {
                return template;
            }

            public void setTemplate(NatflowParserPluginEntity.Instance.OptionTemplateLookup.Template value) {
                this.template = value;
            }

            public String getEnable() {
                return enable;
            }

            public void setEnable(String value) {
                this.enable = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"optionTemplateId","keyField","valueField"})
            public static class OptionTemplate {

                @XmlElement(name = "option-template-id")
                @Mapping("optionTemplateId")
                protected int optionTemplateId;
                
                @XmlElement(name = "key-field")
                @Mapping("optionTemplateKey")
                protected int keyField;
                
                @XmlElement(name = "value-field")
                @Mapping("optionTemplateValue")
                protected int valueField;

                public int getOptionTemplateId() {
                    return optionTemplateId;
                }

                public void setOptionTemplateId(int value) {
                    this.optionTemplateId = value;
                }

                public int getKeyField() {
                    return keyField;
                }

                public void setKeyField(int value) {
                    this.keyField = value;
                }

                public int getValueField() {
                    return valueField;
                }

                public void setValueField(int value) {
                    this.valueField = value;
                }

            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {"templateId","field"})
            public static class Template {

                @XmlElement(name = "template-id", required = true)
                @Mapping("optionCopytoTemplateId")
                protected String templateId;
                
                @XmlElement(required = true)
                @Mapping("optionCopyTofield")
                protected String field;

                public String getTemplateId() {
                    return templateId;
                }

                public void setTemplateId(String value) {
                    this.templateId = value;
                }

                public String getField() {
                    return field;
                }

                public void setField(String value) {
                    this.field = value;
                }
            
            }

        }

    }

}
