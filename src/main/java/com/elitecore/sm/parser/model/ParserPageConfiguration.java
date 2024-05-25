package com.elitecore.sm.parser.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;

import com.elitecore.sm.common.model.BaseModel;

@Scope(value = "prototype")
@Entity
@Table(name = "TBLTPARSERPAGECONFIG")
@DynamicUpdate
public class ParserPageConfiguration extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5782423943364749204L;
	
	private int id;
	private String pageSize;
	private String tableLocation;
	private String tableCols;
	private ParserGroupAttribute parserGroupAttribute;
	private String pageNumber;
	private String extractionMethod;
	
	public ParserPageConfiguration() {
		super();
	}

	public ParserPageConfiguration(String pageSize, String tableLocation,String tableCols) {
		super();
		this.pageSize = pageSize;
		this.tableLocation = tableLocation;
		this.tableCols = tableCols;
	}
	
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ParserPageConfiguration",allocationSize=1)
	/**
	 * represent the auto generated primary key
	 * @return Id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the pageSize
	 */
	@XmlElement
	@Column(name = "PAGESIZE", nullable = true, length = 500, unique = false)
	public String getPageSize() {
		return pageSize;
	}
	
	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * @return the tableLocation
	 */
	@XmlElement
	@Column(name = "TABLELOCATION", nullable = true, length = 500, unique = false)
	public String getTableLocation() {
		return tableLocation;
	}
	
	/**
	 * @param tableLocation
	 *            the tableLocation to set
	 */
	public void setTableLocation(String tableLocation) {
		this.tableLocation = tableLocation;
	}
	
	/**
	 * @return the tableCols
	 */
	@XmlElement
	@Column(name = "TABLECOLS", nullable = true, length = 500, unique = false)
	public String getTableCols() {
		return tableCols;
	}
	
	/**
	 * @param tableCols
	 *            the tableCols to set
	 */
	public void setTableCols(String tableCols) {
		this.tableCols = tableCols;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "PARSERGROUPATTRID", nullable = true, foreignKey = @ForeignKey(name = "PAGECONFIG_GROUPIDFK"))
	public ParserGroupAttribute getParserGroupAttribute() {
		return parserGroupAttribute;
	}

	public void setParserGroupAttribute(ParserGroupAttribute parserGroupAttribute) {
		this.parserGroupAttribute = parserGroupAttribute;
	}
	
	/**
	 * @return the pageNumber
	 */
	@XmlElement
	@Column(name = "PAGENUMBER", nullable = true, length = 500, unique = false)
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
	 * @return the extractionMethod
	 */
	@XmlElement
	@Column(name = "EXTRACTIONMETHOD", nullable = true, length = 100, unique = false)
	public String getExtractionMethod() {
		return extractionMethod;
	}
	
	/**
	 * @param extractionMethod
	 *            the extractionMethod to set
	 */
	public void setExtractionMethod(String extractionMethod) {
		this.extractionMethod = extractionMethod;
	}
	
}
