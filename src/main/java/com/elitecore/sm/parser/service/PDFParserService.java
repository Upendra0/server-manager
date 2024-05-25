package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.PDFParserMapping;

/**
 * @author ELITECOREADS\avadhesh.sharma
 *
 */
public interface PDFParserService{
	
	public ResponseObject getPDFParserMappingById(int pdfParserMappingId);
	
	public ResponseObject updatePDFParser(PDFParserMapping pdfParser);


}
