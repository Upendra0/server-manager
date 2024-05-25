package com.elitecore.sm.roaming.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;




@Entity
@Component
@Table(name = "TBLMPARTNER")
public class Partner extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3992288598653503949L;

		private int  id ;
		private String name;
		private String lob;
		private String primaryTadig;
		private String secondaryTadig;
		
		
		
		@Id
		@Column(name="ID")
		@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
		@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
									 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
									 pkColumnValue="partner",allocationSize=1)
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		@Column(name = "NAME",nullable = false,length = 100)
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Column(name = "LOB",nullable = false,length = 50)
		public String getLob() {
			return lob;
		}
		public void setLob(String lob) {
			this.lob = lob;
		}
		@Column(name = "PRIMARYTADIGCODE",nullable = true,length = 10)
		public String getPrimaryTadig() {
			return primaryTadig;
		}
		public void setPrimaryTadig(String primaryTadig) {
			this.primaryTadig = primaryTadig;
		}
		@Column(name = "SECONDARYTADIGCODE",nullable = true,length = 10)
		public String getSecondaryTadig() {
			return secondaryTadig;
		}
		public void setSecondaryTadig(String secondaryTadig) {
			this.secondaryTadig = secondaryTadig;
		}
		
}
