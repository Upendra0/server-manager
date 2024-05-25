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
@XmlRootElement(name = "ascii-parser-plugin")
public class AsciiParserPluginEntity {

	protected List<AsciiParserPluginEntity.Instance> instance;

	public List<AsciiParserPluginEntity.Instance> getInstance() {
		if (instance == null) {
			instance = new ArrayList<AsciiParserPluginEntity.Instance>();
		}
		return this.instance;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "sourceCharsetName", "sourceDateFormat",
			"keyValueRecord", "keyValueSeparator", "fieldSeparator",
			"findAndReplace", "recordHeader", "fileHeader", "fileFooter",
			"attributeList" })
	public static class Instance {

		@XmlElement(name = "source-charset-name", required = true)
		protected String sourceCharsetName;

		@XmlElement(name = "source-date-format", required = true)
		@Mapping("srcDateFormat")
		protected String sourceDateFormat;

		@XmlElement(name = "key-value-record", required = true)
		@Mapping("keyValueRecordEnable")
		protected boolean keyValueRecord;

		@XmlElement(name = "key-value-separator", required = true)
		@Mapping("keyValueSeparator")
		protected String keyValueSeparator;

		@XmlElement(name = "field-separator", required = true)
		@Mapping("fieldSeparator")
		protected String fieldSeparator;

		@XmlElement(name = "find-and-replace", required = true)
		protected String findAndReplace;

		@XmlElement(name = "record-header", required = true)
		@Mapping("this")
		protected AsciiParserPluginEntity.Instance.RecordHeader recordHeader;

		@XmlElement(name = "file-header", required = true)
		@Mapping("this")
		protected AsciiParserPluginEntity.Instance.FileHeader fileHeader;

		@XmlElement(name = "file-footer", required = true)
		@Mapping("this")
		protected AsciiParserPluginEntity.Instance.FileFooter fileFooter;

		@XmlElement(name = "attribute-list", required = true)
		@Mapping("this")
		protected AsciiParserPluginEntity.Instance.AttributeList attributeList;

		@XmlAttribute(name = "id")
		@Mapping("id")
		protected Integer id;
		
		public boolean isKeyValueRecord() {
			return keyValueRecord;
		}

		public void setKeyValueRecord(boolean keyValueRecord) {
			this.keyValueRecord = keyValueRecord;
		}

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

		public String getKeyValueSeparator() {
			return keyValueSeparator;
		}

		public void setKeyValueSeparator(String value) {
			this.keyValueSeparator = value;
		}

		public String getFieldSeparator() {
			return fieldSeparator;
		}

		public void setFieldSeparator(String value) {
			this.fieldSeparator = value;
		}

		public String getFindAndReplace() {
			return findAndReplace;
		}

		public void setFindAndReplace(String value) {
			this.findAndReplace = value;
		}

		public AsciiParserPluginEntity.Instance.RecordHeader getRecordHeader() {
			return recordHeader;
		}

		public void setRecordHeader(
				AsciiParserPluginEntity.Instance.RecordHeader value) {
			this.recordHeader = value;
		}

		public AsciiParserPluginEntity.Instance.FileHeader getFileHeader() {
			return fileHeader;
		}

		public void setFileHeader(AsciiParserPluginEntity.Instance.FileHeader value) {
			this.fileHeader = value;
		}

		public AsciiParserPluginEntity.Instance.FileFooter getFileFooter() {
			return fileFooter;
		}

		public void setFileFooter(AsciiParserPluginEntity.Instance.FileFooter value) {
			this.fileFooter = value;
		}

		public AsciiParserPluginEntity.Instance.AttributeList getAttributeList() {
			return attributeList;
		}

		public void setAttributeList(
				AsciiParserPluginEntity.Instance.AttributeList value) {
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
			protected List<AsciiParserPluginEntity.Instance.AttributeList.Attribute> attribute;

			public List<AsciiParserPluginEntity.Instance.AttributeList.Attribute> getAttribute() {
				if (attribute == null) {
					attribute = new ArrayList<AsciiParserPluginEntity.Instance.AttributeList.Attribute>();
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

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "jaxbFileFooterParser",
				"fileFooterContains" })
		public static class FileFooter {

			@XmlElement(name = "file-footer-parser", required = true)
			protected String jaxbFileFooterParser;

			@XmlElement(name = "file-footer-contains", required = true)
			@Mapping("fileFooterContains")
			protected String fileFooterContains;

			@XmlAttribute(name = "present")
			@Mapping("fileFooterEnable")
			protected boolean present;

			public boolean isPresent() {
				return present;
			}

			public void setPresent(boolean present) {
				this.present = present;
			}

			public String getFileFooterContains() {
				return fileFooterContains;
			}

			public void setFileFooterContains(String value) {
				this.fileFooterContains = value;
			}
			
			public String getJaxbFileFooterParser() {
				return jaxbFileFooterParser;
			}

			public void setJaxbFileFooterParser(String jaxbFileFooterParser) {
				this.jaxbFileFooterParser = jaxbFileFooterParser;
			}

		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "jaxbFileHeaderParser", "containsFields" })
		public static class FileHeader {

			@XmlElement(name = "file-header-parser", required = true)
			protected String jaxbFileHeaderParser;

			@XmlElement(name = "contains-fields", required = true)
			@Mapping("fileHeaderContainsFields")
			protected String containsFields;

			@XmlAttribute(name = "present")
			@Mapping("fileHeaderEnable")
			protected boolean present;

			public boolean isPresent() {
				return present;
			}

			public void setPresent(boolean present) {
				this.present = present;
			}

			public String getContainsFields() {
				return containsFields;
			}

			public void setContainsFields(String value) {
				this.containsFields = value;
			}
			
			public String getJaxbFileHeaderParser() {
				return jaxbFileHeaderParser;
			}

			public void setJaxbFileHeaderParser(String jaxbFileHeaderParser) {
				this.jaxbFileHeaderParser = jaxbFileHeaderParser;
			}

		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "recordHeaderSeparator",
				"recordHeaderLength" })
		public static class RecordHeader {

			@XmlElement(name = "record-header-separator", required = true)
			@Mapping("recordHeaderSeparator")
			protected String recordHeaderSeparator;

			@XmlElement(name = "record-header-length" , required = true)
			@Mapping("recordHeaderLength")
			protected int recordHeaderLength;

			@XmlAttribute(name = "present")
			@Mapping("recordHeaderEnable")
			protected boolean present;

			public boolean isPresent() {
				return present;
			}

			public void setPresent(boolean present) {
				this.present = present;
			}

			public String getRecordHeaderSeparator() {
				return recordHeaderSeparator;
			}

			public void setRecordHeaderSeparator(String value) {
				this.recordHeaderSeparator = value;
			}

			public int getRecordHeaderLength() {
				return recordHeaderLength;
			}

			public void setRecordHeaderLength(int value) {
				this.recordHeaderLength = value;
			}

		}

	}

}