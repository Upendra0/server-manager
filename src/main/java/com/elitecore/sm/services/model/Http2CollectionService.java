package com.elitecore.sm.services.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.SecureSchemeTypeEnum;

@Component(value="http2CollectionService")
@Entity
@Table(name = "TBLTHTTP2COLLSVC")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serviceCache")
@XmlType(propOrder = { "secureScheme", "encryption", "keystoreFileName", "keystoreFilePath", "keystorePassword", "keymanagerPassword" })
public class Http2CollectionService extends NetflowBinaryCollectionService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SecureSchemeTypeEnum secureScheme = SecureSchemeTypeEnum.HTTP;
	private boolean encryption = false;
	private String keystoreFileName = null;
	private String keystorePassword = null;
	private String keymanagerPassword = null;
	private String keystoreFilePath = null;
	private byte[]  keystoreFile; //NOSONAR
	
	
	@Column(name = "SECURESCHEME", nullable = true, length = 50)
	@Enumerated(EnumType.STRING)
	@XmlElement
	public SecureSchemeTypeEnum getSecureScheme() {
		return secureScheme;
	}
	
	public void setSecureScheme(SecureSchemeTypeEnum secureScheme) {
		this.secureScheme = secureScheme;
	}
	
	@XmlElement
	@Column(name = "ENCRYPTION", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isEncryption() {
		return encryption;
	}
	
	public void setEncryption(boolean encryption) {
		this.encryption = encryption;
	}
	
	@Column(name = "KEYSTOREFILENAME", nullable = true, length = 255)
	@XmlElement
	public String getKeystoreFileName() {
		return keystoreFileName;
	}
	
	public void setKeystoreFileName(String keystoreFileName) {
		this.keystoreFileName = keystoreFileName;
	}
	
	@Column(name = "KEYSTOREPASSWORD", nullable = true, length = 255)
	@XmlElement
	public String getKeystorePassword() {
		return keystorePassword;
	}
	
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}
	
	@Column(name = "KEYMANAGERPASSWORD", nullable = true, length = 255)
	@XmlElement
	public String getKeymanagerPassword() {
		return keymanagerPassword;
	}
	
	public void setKeymanagerPassword(String keymanagerPassword) {
		this.keymanagerPassword = keymanagerPassword;
	}
	
	@Column(name = "KEYSTOREFILEPATH", nullable = true, length = 255)
	@XmlElement
	public String getKeystoreFilePath() {
		return keystoreFilePath;
	}

	public void setKeystoreFilePath(String keystoreFilePath) {
		this.keystoreFilePath = keystoreFilePath;
	}

	@Lob
	@Column(name = "KEYSTOREFILE", nullable = true)
	@XmlTransient
	public byte[]  getKeystoreFile() {
		return keystoreFile;
	}

	public void setKeystoreFile(byte[]  keystoreFile) {
		this.keystoreFile = keystoreFile;
	}
	
}
