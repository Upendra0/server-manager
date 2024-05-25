package com.elitecore.sm.policy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import org.springframework.stereotype.Component;

@Entity(name = "TBLTRULEHISTORY")
@Component
public class PolicyRuleHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String ruleName;
	private String ruleCatagory;
	private String ruleSubCatagory;
	private String errorCode;
	
	
	
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ruleHistory",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "RULENAME", nullable = false, length = 100)
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	@Column(name = "RULECATAGORY", nullable = false, length = 100)
	public String getRuleCatagory() {
		return ruleCatagory;
	}
	public void setRuleCatagory(String ruleCatagory) {
		this.ruleCatagory = ruleCatagory;
	}
	@Column(name = "RULESUBCATAGORY", nullable = false, length = 100)
	public String getRuleSubCatagory() {
		return ruleSubCatagory;
	}
	public void setRuleSubCatagory(String ruleSubCatagory) {
		this.ruleSubCatagory = ruleSubCatagory;
	}
	@Column(name = "ERRORCODE", nullable = false, length = 100)
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	

}
