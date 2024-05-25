/**
 * 
 */
package com.elitecore.sm.errorreprocess.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * @author Ranjitsinh Reval
 *
 */
@Component(value="ruleConditionDetails")
@Entity()
@Table(name = "TBLTRULECONDITIONDETAILS")
@DynamicUpdate
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=RuleConditionDetails.class)
public class RuleConditionDetails extends BaseModel {

	private static final long serialVersionUID = -5754665268596316984L;


	private int id;
	///private ErrorReprocessingBatch reprocessingBatch;
	private int applicationOrder;
	private String conditionExpression;
	private String actionExpression;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="RuleConditionDetails",allocationSize=1)
	@Column(name="ID")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/*@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BATCHID", nullable = false, foreignKey = @ForeignKey(name = "FK_BATCH_RULE_DETAIL"))
	public ErrorReprocessingBatch getReprocessingBatch() {
		return reprocessingBatch;
	}
	
	public void setReprocessingBatch(ErrorReprocessingBatch reprocessingBatch) {
		this.reprocessingBatch = reprocessingBatch;
	}*/
	
	@Column(name="APPLICATIONORDER", nullable=false)
	public int getApplicationOrder() {
		return applicationOrder;
	}
	
	public void setApplicationOrder(int applicationOrder) {
		this.applicationOrder = applicationOrder;
	}
	
	@Column(name="CONDITIONEXPRESSION", nullable=false, length = 1000)
	public String getConditionExpression() {
		return conditionExpression;
	}
	
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	
	@Column(name="ACTIONEXPRESSION", nullable=false, length = 1000)
	public String getActionExpression() {
		return actionExpression;
	}

	
	public void setActionExpression(String actionExpression) {
		this.actionExpression = actionExpression;
	}
	
	
}
