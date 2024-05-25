/**
 * 
 */
package com.elitecore.sm.iam.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
@Component(value="passwordHistoryObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLTPASSWORDHISTORY")
@DynamicUpdate
public class PasswordHistory extends BaseModel implements Serializable{

	private static final long serialVersionUID = 3654842080184844182L;

	private int id;

	private Staff staff;

	private String password;

	private Date modifiedDate;
	
	@Override
	public String toString(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("password", password);
		map.put("modifiedDate", modifiedDate);
		return JSONValue.toJSONString(map);
	}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="PasswordHistory",allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staffid")
	public Staff getStaff() {
		return staff;
	}
	
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
	@Column(name="STF_PASSWORD", nullable = false, length = 255)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="lastmodifieddate")
	@Type(type="timestamp")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}