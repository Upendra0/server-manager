/**
 * 
 */
package com.elitecore.sm.systemparam.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author vandana.awatramani
 * 
 */

@Entity
@Table(name = "TBLMSYSTEMPARAMETERGROUP")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="systemParameterGroupData")
public class SystemParameterGroupData extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1235204217190121792L;
	private int id;
	private String name;
	private boolean enabled;

	

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 50, unique = true)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isEnabled
	 */
	@Type(type = "yes_no")
	@Column(name = "SYSTEMGENERATED")
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param isEnabled
	 *            the isEnabled to set
	 */
	public void setEnabled(boolean isEnabled) {
		this.enabled = isEnabled;
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this).append("name", name)
				.append("enabled", enabled)
				.append("id", id).toString();
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "PARAMETERGROUPID", length = 8)
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="SystemParameterGroupData",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
