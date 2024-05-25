/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author avadhesh.sharma
 *
 */
@Component(value="PDFParserMapping")
@Entity
@DiscriminatorValue(EngineConstants.PDF_PARSING_PLUGIN)
@XmlRootElement
public class PDFParserMapping extends ParserMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4591616708918313077L;
	private boolean recordWisePdfFormat;
	private boolean fileParsed;
	private boolean multiInvoice;
	private boolean multiPages;
	

	/**
	 * @return the isRecordWisePdfFormat
	 */
	@XmlElement
	@Column(name = "RECORDWISEPDFFORMAT", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isRecordWisePdfFormat() {
		return recordWisePdfFormat;
	}
	
	/**
	 * @param recordWisePdfFormat
	 *            the recordWisePdfFormat to set
	 */
	public void setRecordWisePdfFormat(boolean recordWisePdfFormat) {
		this.recordWisePdfFormat = recordWisePdfFormat;
	}

	/**
	 * @return the fileParsed
	 */
	@XmlElement
	@Column(name = "FILEPARSED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isFileParsed() {
		return fileParsed;
	}
	
	/**
	 * @param fileParsed
	 *            the fileParsed to set
	 */
	public void setFileParsed(boolean fileParsed) {
		this.fileParsed =  fileParsed;
	}
	
	/**
	 * @return the multiInvoice
	 */
	@XmlElement
	@Column(name = "MULTIINVOICE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isMultiInvoice() {
		return multiInvoice;
	}
	
	/**
	 * @param multiInvoice
	 *            the multiInvoice to set
	 */
	public void setMultiInvoice(boolean multiInvoice) {
		this.multiInvoice =  multiInvoice;
	}

	@XmlElement
	@Column(name = "MULTIPAGES", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isMultiPages() {
		return multiPages;
	}

	public void setMultiPages(boolean multiPages) {
		this.multiPages = multiPages;
	}
	
}
