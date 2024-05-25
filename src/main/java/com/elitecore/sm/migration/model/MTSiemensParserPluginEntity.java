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
@XmlType(name = "", propOrder = { "instance" })
@XmlRootElement(name = "mt-siemens-binary-parser-plugin")
public class MTSiemensParserPluginEntity {

	protected List<MTSiemensParserPluginEntity.Instance> instance;

	public List<MTSiemensParserPluginEntity.Instance> getInstance() {
		if (instance == null) {
			instance = new ArrayList<MTSiemensParserPluginEntity.Instance>();
		}
		return this.instance;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "sourceCharsetName", "sourceDateFormat",
			"attributeList" })
	public static class Instance {

		@XmlElement(name = "source-charset-name", required = true)
		protected String sourceCharsetName;

		@XmlElement(name = "source-date-format", required = true)
		@Mapping("srcDateFormat")
		protected String sourceDateFormat;

		@XmlElement(name = "attribute-list", required = true)
		@Mapping("this")
		protected MTSiemensParserPluginEntity.Instance.AttributeList attributeList;

		@XmlAttribute(name = "id")
		@Mapping("id")
		protected Integer id;
		
		
		public String getSourceCharsetName() {
			return sourceCharsetName;
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
		
		public MTSiemensParserPluginEntity.Instance.AttributeList getAttributeList() {
			return attributeList;
		}

		public void setAttributeList(
				MTSiemensParserPluginEntity.Instance.AttributeList value) {
			this.attributeList = value;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer value) {
			this.id = value;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "attribute" })
		public static class AttributeList {

			@Mapping("parserAttributes")
			protected List<MTSiemensParserPluginEntity.Instance.AttributeList.Attribute> attribute;

			public List<MTSiemensParserPluginEntity.Instance.AttributeList.Attribute> getAttribute() {
				if (attribute == null) {
					attribute = new ArrayList<MTSiemensParserPluginEntity.Instance.AttributeList.Attribute>();
				}
				return this.attribute;
			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "description", "defaultValue",
					"trimChars", "unifiedField", "sourceField", "trimPosition",
					"sourceFieldFormat" })
			public static class Attribute {

				@XmlElement(name = "description", required = true)
				@Mapping("description")
				protected String description;

				@XmlElement(name = "default-value", required = true)
				@Mapping("defaultValue")
				protected String defaultValue;

				@XmlElement(name = "trim-chars", required = true)
				@Mapping("trimChars")
				protected String trimChars;

				@XmlElement(name = "unified-field", required = true)
				@Mapping("unifiedField")
				protected String unifiedField;

				@XmlElement(name = "source-field", required = true)
				@Mapping("sourceField")
				protected String sourceField;
				
				@XmlElement(name = "source-field-format", required = true)
				@Mapping("sourceFieldFormat")
				protected String sourceFieldFormat;

				@XmlElement(name = "trim-position", required = true)
				protected String trimPosition;

				public String getDescription() {
					return description;
				}

				public void setDescription(String description) {
					this.description = description;
				}

				public String getDefaultValue() {
					return defaultValue;
				}

				public void setDefaultValue(String defaultValue) {
					this.defaultValue = defaultValue;
				}

				public String getTrimChars() {
					return trimChars;
				}

				public void setTrimChars(String trimChars) {
					this.trimChars = trimChars;
				}

				public String getUnifiedField() {
					return unifiedField;
				}

				public void setUnifiedField(String unifiedField) {
					this.unifiedField = unifiedField;
				}

				public String getSourceField() {
					return sourceField;
				}

				public void setSourceField(String sourceField) {
					this.sourceField = sourceField;
				}

				public String getTrimPosition() {
					return trimPosition;
				}

				public void setTrimPosition(String trimPosition) {
					this.trimPosition = trimPosition;
				}

				public String getSourceFieldFormat() {
					return sourceFieldFormat;
				}

				public void setSourceFieldFormat(String sourceFieldFormat) {
					this.sourceFieldFormat = sourceFieldFormat;
				}

			}

		}

	}

}
