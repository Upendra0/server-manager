/**
 * 
 */
package com.elitecore.sm.systemparam.model;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author vandana.awatramani
 * 
 */
@Component(value="imageModelObject")
@Scope(value="prototype")
@Entity
@Table(name = "TBLTIMAGE")
@DynamicUpdate
@NamedQueries({@NamedQuery(name="findByImageId",query="from ImageModel where id=:arg1")})
public class ImageModel extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3465461994709141486L;
	private int id;
	private String fileName;
	private int extRefId;
	private String imageType;
	private Blob image; //NOSONAR
	
	private byte imageBytes[];

	@Id
	@Column(name = "ID", length = 8)
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="ImageModel",allocationSize=1)
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the fileName
	 */
	@Column(name = "FILENAME", nullable = false, length = 250)
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the extRefId
	 */
	@Column(name = "EXTREFID", nullable = false, length = 50)
	public int getExtRefId() {
		return extRefId;
	}

	/**
	 * @return the imageType
	 */
	@Column(name = "IMAGETYPE", nullable = false, length = 250)
	public String getImageType() {
		return imageType;
	}

	/**
	 * @return the image
	 */
	@Lob
	@Column(name = "IMAGEBLOB", nullable = false)
	public Blob getImage() {
		return image;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @param extRefId
	 *            the extRefId to set
	 */
	public void setExtRefId(int extRefId) {
		this.extRefId = extRefId;
	}

	/**
	 * @param imageType
	 *            the imageType to set
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Blob image) {
		this.image = image;
	}
	
	/**
	 * 
	 * @return the imageBytes
	 */
	@Lob
	@Column(name = "IMAGEBYTE")
	public byte[] getImageBytes() {
		return imageBytes;
	}

	/**
	 * 
	 * @param imageBytes
	 */
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ImageModel [id=" + id
				+ ", fileName=" + fileName + ", extRefId=" + extRefId + ",imageType=" +imageType+"]";
	}




}
