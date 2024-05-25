package com.elitecore.sm.migration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "content" })
@XmlRootElement(name = "aggregation-service")
public class AggregationService {

	@XmlElementRefs({
			@XmlElementRef(name = "is-sorting-required", type = JAXBElement.class),
			@XmlElementRef(name = "execution-interval", type = JAXBElement.class),
			@XmlElementRef(name = "processing-type", type = JAXBElement.class),
			@XmlElementRef(name = "source-path", type = JAXBElement.class),
			@XmlElementRef(name = "source-file-types", type = JAXBElement.class),
			@XmlElementRef(name = "file-grouping", type = JAXBElement.class),
			@XmlElementRef(name = "startup-mode", type = JAXBElement.class),
			@XmlElementRef(name = "immediate-execute-on-startup", type = JAXBElement.class),
			@XmlElementRef(name = "aggregation-list", type = JAXBElement.class),
			@XmlElementRef(name = "process-record-limit", type = JAXBElement.class),
			@XmlElementRef(name = "thread-queue-size", type = JAXBElement.class),
			@XmlElementRef(name = "input-database-info", type = JAXBElement.class),
			@XmlElementRef(name = "maximum-thread", type = JAXBElement.class),
			@XmlElementRef(name = "select-file-on-prefixes", type = JAXBElement.class),
			@XmlElementRef(name = "sorting-criteria", type = JAXBElement.class),
			@XmlElementRef(name = "store-cdr-file-summary-in-db", type = JAXBElement.class),
			@XmlElementRef(name = "device-name", type = JAXBElement.class),
			@XmlElementRef(name = "minimum-thread", type = JAXBElement.class),
			@XmlElementRef(name = "sorting-type", type = JAXBElement.class),
			@XmlElementRef(name = "entity-batch-size", type = JAXBElement.class) })
	@XmlMixed
	protected List<Serializable> content;

	public List<Serializable> getContent() {
		if (content == null) {
			content = new ArrayList<>();
		}
		return this.content;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "aggregation" })
	public static class AggregationList {

		@XmlElement(required = true)
		protected AggregationService.AggregationList.Aggregation aggregation;

		public AggregationService.AggregationList.Aggregation getAggregation() {
			return aggregation;
		}

		public void setAggregation(
				AggregationService.AggregationList.Aggregation value) {
			this.aggregation = value;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "destinationPath",
				"outputDatabaseInfo", "matchAttributeList",
				"groupAttributeList", "aggregationAttributeList",
				"requiredDatecheckInputField", "requiredDatecheckOutputField",
				"inputCheckAttributeList", "outputCheckAttributeList",
				"procedureName", "procedureParameters" })
		public static class Aggregation {

			@XmlElement(name = "destination-path", required = true)
			protected String destinationPath;
			@XmlElement(name = "output-database-info", required = true)
			protected AggregationService.AggregationList.Aggregation.OutputDatabaseInfo outputDatabaseInfo;
			@XmlElement(name = "match-attribute-list", required = true)
			protected String matchAttributeList;
			@XmlElement(name = "group-attribute-list", required = true)
			protected AggregationService.AggregationList.Aggregation.GroupAttributeList groupAttributeList;
			@XmlElement(name = "aggregation-attribute-list", required = true)
			protected AggregationService.AggregationList.Aggregation.AggregationAttributeList aggregationAttributeList;
			@XmlElement(name = "required-datecheck-input-field", required = true)
			protected String requiredDatecheckInputField;
			@XmlElement(name = "required-datecheck-output-field", required = true)
			protected String requiredDatecheckOutputField;
			@XmlElement(name = "input-check-attribute-list", required = true)
			protected String inputCheckAttributeList;
			@XmlElement(name = "output-check-attribute-list", required = true)
			protected String outputCheckAttributeList;
			@XmlElement(name = "procedure-name", required = true)
			protected String procedureName;
			@XmlElement(name = "procedure-parameters", required = true)
			protected String procedureParameters;
			@XmlAttribute(name = "name")
			protected String name;

			public String getDestinationPath() {
				return destinationPath;
			}

			public void setDestinationPath(String value) {
				this.destinationPath = value;
			}

			public AggregationService.AggregationList.Aggregation.OutputDatabaseInfo getOutputDatabaseInfo() {
				return outputDatabaseInfo;
			}

			public void setOutputDatabaseInfo(
					AggregationService.AggregationList.Aggregation.OutputDatabaseInfo value) {
				this.outputDatabaseInfo = value;
			}

			public String getMatchAttributeList() {
				return matchAttributeList;
			}

			public void setMatchAttributeList(String value) {
				this.matchAttributeList = value;
			}

			public AggregationService.AggregationList.Aggregation.GroupAttributeList getGroupAttributeList() {
				return groupAttributeList;
			}

			public void setGroupAttributeList(
					AggregationService.AggregationList.Aggregation.GroupAttributeList value) {
				this.groupAttributeList = value;
			}

			public AggregationService.AggregationList.Aggregation.AggregationAttributeList getAggregationAttributeList() {
				return aggregationAttributeList;
			}

			public void setAggregationAttributeList(
					AggregationService.AggregationList.Aggregation.AggregationAttributeList value) {
				this.aggregationAttributeList = value;
			}

			public String getRequiredDatecheckInputField() {
				return requiredDatecheckInputField;
			}

			public void setRequiredDatecheckInputField(String value) {
				this.requiredDatecheckInputField = value;
			}

			public String getRequiredDatecheckOutputField() {
				return requiredDatecheckOutputField;
			}

			public void setRequiredDatecheckOutputField(String value) {
				this.requiredDatecheckOutputField = value;
			}

			public String getInputCheckAttributeList() {
				return inputCheckAttributeList;
			}

			public void setInputCheckAttributeList(String value) {
				this.inputCheckAttributeList = value;
			}

			public String getOutputCheckAttributeList() {
				return outputCheckAttributeList;
			}

			public void setOutputCheckAttributeList(String value) {
				this.outputCheckAttributeList = value;
			}

			public String getProcedureName() {
				return procedureName;
			}

			public void setProcedureName(String value) {
				this.procedureName = value;
			}

			public String getProcedureParameters() {
				return procedureParameters;
			}

			public void setProcedureParameters(String value) {
				this.procedureParameters = value;
			}

			public String getName() {
				return name;
			}

			public void setName(String value) {
				this.name = value;
			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "attribute" })
			public static class AggregationAttributeList {

				protected List<AggregationService.AggregationList.Aggregation.AggregationAttributeList.Attribute> attribute;

				public List<AggregationService.AggregationList.Aggregation.AggregationAttributeList.Attribute> getAttribute() {
					if (attribute == null) {
						attribute = new ArrayList<>();
					}
					return this.attribute;
				}

				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "", propOrder = { "fieldName", "dataType",
						"operation" })
				public static class Attribute {

					@XmlElement(name = "field-name", required = true)
					protected String fieldName;
					@XmlElement(name = "data-type", required = true)
					protected String dataType;
					protected byte operation;

					public String getFieldName() {
						return fieldName;
					}

					public void setFieldName(String value) {
						this.fieldName = value;
					}

					public String getDataType() {
						return dataType;
					}

					public void setDataType(String value) {
						this.dataType = value;
					}

					public byte getOperation() {
						return operation;
					}

					public void setOperation(byte value) {
						this.operation = value;
					}

				}

			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "fieldNames" })
			public static class GroupAttributeList {

				@XmlElement(name = "field-names", required = true)
				protected String fieldNames;

				public String getFieldNames() {
					return fieldNames;
				}

				public void setFieldNames(String value) {
					this.fieldNames = value;
				}

			}

			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "outputDatasourceName",
					"outputTableName" })
			public static class OutputDatabaseInfo {

				@XmlElement(name = "output-datasource-name", required = true)
				protected String outputDatasourceName;
				@XmlElement(name = "output-table-name", required = true)
				protected String outputTableName;

				public String getOutputDatasourceName() {
					return outputDatasourceName;
				}

				public void setOutputDatasourceName(String value) {
					this.outputDatasourceName = value;
				}

				public String getOutputTableName() {
					return outputTableName;
				}

				public void setOutputTableName(String value) {
					this.outputTableName = value;
				}

			}

		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "groupingType", "forArchive", "forError" })
	public static class FileGrouping {

		@XmlElement(name = "grouping-type", required = true)
		protected String groupingType;
		@XmlElement(name = "for-archive", required = true)
		protected String forArchive;
		@XmlElement(name = "for-error", required = true)
		protected String forError;
		@XmlAttribute(name = "enabled")
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

		public String getForError() {
			return forError;
		}

		public void setForError(String value) {
			this.forError = value;
		}

		public String getEnabled() {
			return enabled;
		}

		public void setEnabled(String value) {
			this.enabled = value;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "inputDatasourceName", "inputTableName" })
	public static class InputDatabaseInfo {

		@XmlElement(name = "input-datasource-name", required = true)
		protected String inputDatasourceName;
		@XmlElement(name = "input-table-name", required = true)
		protected String inputTableName;

		public String getInputDatasourceName() {
			return inputDatasourceName;
		}

		public void setInputDatasourceName(String value) {
			this.inputDatasourceName = value;
		}

		public String getInputTableName() {
			return inputTableName;
		}

		public void setInputTableName(String value) {
			this.inputTableName = value;
		}

	}

}
