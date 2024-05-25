package com.elitecore.sm.diameterpeer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component("diameterAVP")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "TBLTDIAMETERAVP")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@XmlType(propOrder = { "id","vendorId","attributeId","value"})

public class DiameterAVP extends BaseModel {

	private static final long serialVersionUID = 2252086629078034849L;
	
	private int id;
	private int vendorId;
	private int attributeId;
	private String value;
	private DiameterPeer peer;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DiameterAVP",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlElement
	@Column(name = "VENDORID", nullable = true)
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	
	@XmlElement
	@Column(name = "ATTRIBUTEID", nullable = true)
	public int getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}
	
	@XmlElement
	@Column(name = "VALUE", nullable = true, length = 255)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PEERID", nullable = true, foreignKey = @ForeignKey(name = "FK_AVP_DIAMETER_PEER"))
	@XmlTransient
	@DiffIgnore
	public DiameterPeer getPeer() {
		return peer;
	}
	public void setPeer(DiameterPeer peer) {
		this.peer = peer;
	}
}
