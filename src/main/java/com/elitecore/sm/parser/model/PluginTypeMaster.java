/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@Table( name="TBLMPLUGINTYPE")
public class PluginTypeMaster  extends BaseModel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9157498475282049569L;
	private int id;
	private String type;
	private String category;
	private String description;
	private String alias;
	private String pluginFullClassName;

	
	
	
	/**
	 * 
	 */
	public PluginTypeMaster() {
		// default constructor for hibernate
	}




	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="PluginTypeMaster",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE", nullable = false, length = 100)
	@XmlElement
	public String getType() {
		return type;
	}

	/**
	 * @return the category
	 */
	@Column(name = "CATEGORY", nullable = false, length = 100)
	@XmlElement
	public String getCategory() {
		return category;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = false, length = 100)
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @return the alias
	 */
	@Column(name="ALIAS",nullable=false,length=255)
	@XmlElement
	public String getAlias() {
		return alias;
	}




	/**
	 * @return the pluginFullClassName
	 */

	@Column(name = "PLUGINFULLCLASSNAME", nullable = false, length = 1000)
	@XmlElement
	public String getPluginFullClassName() {
		return pluginFullClassName;
	}


	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @param pluginFullClassName the pluginFullClassName to set
	 */
	public void setPluginFullClassName(String pluginFullClassName) {
		this.pluginFullClassName = pluginFullClassName;
	}
	
	
	
}
