package com.elitecore.sm.parser.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.elitecore.sm.common.model.BaseModel;

@Entity
@Table(name="TBLTVLAPMDATADEFINITION")
public class VarLengthAsciiDataDefinitionFile extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	private String dataDefinitionFileName;
	private Blob dataDefinitionFile; //NOSONAR
	private VarLengthAsciiParserMapping mapping;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="VarLengthAsciiDataDefinitionFile",allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "DATADEFINITIONFILENAME", nullable = true)
	public String getDataDefinitionFileName() {
		return dataDefinitionFileName;
	}

	public void setDataDefinitionFileName(String dataDefinitionFileName) {
		this.dataDefinitionFileName = dataDefinitionFileName;
	}

	@Lob
	@Column(name = "DATADEFINITIONFILE", nullable = true)
	public Blob getDataDefinitionFile() {
		return dataDefinitionFile;
	}

	public void setDataDefinitionFile(Blob dataDefinitionFile) {
		this.dataDefinitionFile = dataDefinitionFile;
	}

	@OneToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="MAPPINGID",nullable=true)
	public VarLengthAsciiParserMapping getMapping() {
		return mapping;
	}

	public void setMapping(VarLengthAsciiParserMapping mapping) {
		this.mapping = mapping;
	}
	
}
