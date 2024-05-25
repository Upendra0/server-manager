/**
 * 
 */
package com.elitecore.sm.errorreprocess.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.services.model.ErrorReprocessingActionEnum;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * @author Ranjitsinh Reval
 *
 */
@Component(value="errorReprocessingBatch")
@Entity()
@Scope(value="prototype")
@Table(name = "TBLTERRORREPROCESSBATCH")
@DynamicUpdate
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ErrorReprocessingBatch.class)
public class ErrorReprocessingBatch  extends BaseModel {

	private static final long serialVersionUID = 2735550979361858115L;
	

	private int id;

	private ErrorReprocessingActionEnum errorProcessAction ;
	private String userComment;
	private List<ErrorReprocessDetails> reprocessDetailList = new ArrayList<>(0);
	private String errorCategory ;
	
	public ErrorReprocessingBatch(){
		//Default Constructor
	}
	
	public ErrorReprocessingBatch(int id) {
		this.id = id;
	}


	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="ErrorReprocessingBatch",allocationSize=1)
	@Column(name="ID")
	public int getId() {
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	

	
	/**
	 * @return the errorProcessAction
	 */

	@Enumerated(EnumType.STRING)
	@Column(name = "ERRORPROCESSACTION", nullable = false)
	public ErrorReprocessingActionEnum getErrorProcessAction() {
		return errorProcessAction;
	}

	
	/**
	 * @param errorProcessAction the errorProcessAction to set
	 */
	public void setErrorProcessAction(ErrorReprocessingActionEnum errorProcessAction) {
		this.errorProcessAction = errorProcessAction;
	}

	
	/**
	 * @return the reprocessDetailList
	 */
	@OneToMany(mappedBy = "reprocessingBatch",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	public List<ErrorReprocessDetails> getReprocessDetailList() {
		return reprocessDetailList;
	}

	
	/**
	 * @param reprocessDetailList the reprocessDetailList to set
	 */
	public void setReprocessDetailList(List<ErrorReprocessDetails> reprocessDetailList) {
		this.reprocessDetailList = reprocessDetailList;
	}

	
	/**
	 * @return the errorCategory
	 */
	@Column(name = "ERRORCATEGORY", nullable = false)
	public String getErrorCategory() {
		return errorCategory;
	}
	
	/**
	 * @param errorCategory the errorCategory to set
	 */
	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}

	
	/**
	 * @return the userComment
	 */
	@Column(name = "USERCOMMENT", nullable = true , length = 255)
	public String getUserComment() {
		return userComment;
	}

	
	/**
	 * @param userComment the userComment to set
	 */
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
}
