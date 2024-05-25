package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

@Entity
@DiscriminatorValue("XlsParserAttribute")
public class XlsParserAttribute extends ParserAttribute{

	private static final long serialVersionUID = 837908498069492876L;
	private String columnStartsWith;
	private String startsWith;
	private boolean tableFooter;
	private String excelRow;
	private String excelCol;
	private String relativeExcelRow;
	private String columnContains;
	private boolean tableRowAttribute;
	
	@XmlElement
	@Column(name = "COLSTARTSWITH", nullable = true)
	public String getColumnStartsWith() {
		return columnStartsWith;
	}

	public void setColumnStartsWith(String columnStartsWith) {
		this.columnStartsWith = columnStartsWith;
	}

	@XmlElement
	@Column(name = "TABLEFOOTER", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isTableFooter() {
		return tableFooter;
	}

	public void setTableFooter(boolean tableFooter) {
		this.tableFooter = tableFooter;
	}

	@XmlElement
	@Column(name = "EXCELROW", nullable = true)
	public String getExcelRow() {
		return excelRow;
	}

	public void setExcelRow(String excelRow) {
		this.excelRow = excelRow;
	}

	@XmlElement
	@Column(name = "EXCELCOL", nullable = true)
	public String getExcelCol() {
		return excelCol;
	}

	public void setExcelCol(String excelCol) {
		this.excelCol = excelCol;
	}

	@XmlElement
	@Column(name = "RELATIVEEXCELROW", nullable = true)
	public String getRelativeExcelRow() {
		return relativeExcelRow;
	}

	public void setRelativeExcelRow(String relativeExcelRow) {
		this.relativeExcelRow = relativeExcelRow;
	}

	@XmlElement
	@Column(name = "STARTSWITH", nullable = true)
	public String getStartsWith() {
		return startsWith;
	}

	public void setStartsWith(String startsWith) {
		this.startsWith = startsWith;
	}

	@XmlElement
	@Column(name = "COLCONTAINS", nullable = true)
	public String getColumnContains() {
		return columnContains;
	}

	public void setColumnContains(String columnContains) {
		this.columnContains = columnContains;
	}

	@XmlElement
	@Column(name = "ISTABLEROWATTRIBUTE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isTableRowAttribute() {
		return tableRowAttribute;
	}

	public void setTableRowAttribute(boolean tableRowAttribute) {
		this.tableRowAttribute = tableRowAttribute;
	}

	
	
}
