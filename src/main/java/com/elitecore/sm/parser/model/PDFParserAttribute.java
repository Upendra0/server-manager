/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author jay.shah
 *
 */
@Entity
@DiscriminatorValue("PDFParserAttribute")
public class PDFParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1284453441438035741L;
	
	private String location;
	private String columnStartLocation;
	private String columnIdentifier;
	private String referenceRow;
	private String referenceCol;
	private String columnStartsWith;
	private boolean tableFooter;
	private String pageNumber;
	private String valueSeparator;
	private String columnEndsWith;
	private String mandatory;
	private boolean multiLineAttribute;
	private String rowTextAlignment;
	private boolean multipleValues;
	
	/**
	 * @return the location
	 */
	@XmlElement
	@Column(name = "LOCATION", nullable = true, length = 200)	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the columnStartLocation
	 */
	@XmlElement
	@Column(name = "COLSTARTLOCATION", nullable = true, length = 200)	
	public String getColumnStartLocation() {
		return columnStartLocation;
	}
	
	public void setColumnStartLocation(String columnStartLocation) {
		this.columnStartLocation = columnStartLocation;
	}
	
	/**
	 * @return the columnIdentifier
	 */
	@XmlElement
	@Column(name = "COLIDENTIFIER", nullable = true, length = 200)	
	public String getColumnIdentifier() {
		return columnIdentifier;
	}
	
	public void setColumnIdentifier(String columnIdentifier) {
		this.columnIdentifier = columnIdentifier;
	}
	
	/**
	 * @return the referenceRow
	 */
	@XmlElement
	@Column(name = "REFERENCEROW", nullable = true, length = 200)	
	public String getReferenceRow() {
		return referenceRow;
	}
	
	public void setReferenceRow(String referenceRow) {
		this.referenceRow = referenceRow;
	}
	
	/**
	 * @return the referenceRow
	 */
	@XmlElement
	@Column(name = "REFERENCECOL", nullable = true, length = 200)	
	public String getReferenceCol() {
		return referenceCol;
	}
	
	public void setReferenceCol(String referenceCol) {
		this.referenceCol = referenceCol;
	}
	
	
	/**
	 * @return the tableFooter
	 */
	@XmlElement
	@Column(name = "TABLEFOOTER", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isTableFooter() {
		return tableFooter;
	}
	
	/**
	 * @param tableFooter
	 *            the tableFooter to set
	 */
	public void setTableFooter(boolean tableFooter) {
		this.tableFooter = tableFooter;
	}
	
	/**
	 * @return the columnStartsWith
	 */
	@XmlElement
	@Column(name = "COLSTARTSWITH", nullable = true, length = 200)	
	public String getColumnStartsWith() {
		return columnStartsWith;
	}
	
	public void setColumnStartsWith(String columnStartsWith) {
		this.columnStartsWith = columnStartsWith;
	}
	
	@XmlElement
	@Column(name = "VALUESEPARATOR", nullable = true)
	public String getValueSeparator() {
		return valueSeparator;
	}

	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
	}
	
	/**
	 * @return the pageNumber
	 */
	@XmlElement
	@Column(name = "PAGENUMBER", nullable = true, length = 10, unique = false)
	public String getPageNumber() {
		return pageNumber;
	}
	
	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	/**
	 * @return the columnEndsWith
	 */
	@XmlElement
	@Column(name = "COLENDSWITH", nullable = true, length = 400)	
	public String getColumnEndsWith() {
		return columnEndsWith;
	}
	
	public void setColumnEndsWith(String columnEndsWith) {
		this.columnEndsWith = columnEndsWith;
	}
	
	/**
	 * @return the mandatory
	 */
	@XmlElement
	@Column(name = "MANDATORY", nullable = true, length = 200)	
	public String getMandatory() {
		return mandatory;
	}
	
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	
	/**
	 * @return the multiLineAttribute
	 */
	@XmlElement
	@Column(name = "MULTILINEATTR", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isMultiLineAttribute() {
		return multiLineAttribute;
	}
	
	/**
	 * @param multiLineAttribute
	 *            the multiLineAttribute to set
	 */
	public void setMultiLineAttribute(boolean multiLineAttribute) {
		this.multiLineAttribute = multiLineAttribute;
	}
	
	/**
	 * @return the rowTextAlignment
	 */
	@XmlElement
	@Column(name = "ROWTEXTALIGNMENT", nullable = true, length = 100)	
	public String getRowTextAlignment() {
		return rowTextAlignment;
	}
	
	public void setRowTextAlignment(String rowTextAlignment) {
		this.rowTextAlignment = rowTextAlignment;
	}

	@XmlElement
	@Column(name = "MULTIPLEVALUES", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isMultipleValues() {
		return multipleValues;
	}

	public void setMultipleValues(boolean multipleValues) {
		this.multipleValues = multipleValues;
	}

}