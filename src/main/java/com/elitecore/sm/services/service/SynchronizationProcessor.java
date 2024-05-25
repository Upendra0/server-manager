package com.elitecore.sm.services.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;

/**
 * 
 * @author avani.panchal 
 * Process JAXB xml , fetch XSLT from file system and do  transformation
 *
 */

public class SynchronizationProcessor {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * @param klass
	 * @param obj
	 * @param xsltdata
	 * @param servletContext
	 * @return 
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws TransformerException 
	 * @throws SMException 
	 * 
	 */
	public String processXSLT(Class<?> klass, Object obj,String xsltFileName,String jaxbXmlPath,String xsltFilePath) throws SMException {
		String fileContent;

		File inputxml = generateXMlFromJAXB(klass, obj,klass.getName(),jaxbXmlPath);

		File xsltFile = getXSLTFromFileSystem(xsltFileName,xsltFilePath);

		fileContent = doTransformation(xsltFile, inputxml,0);

		return fileContent;
	}

	/**
	 * Generate JAXB XML from object
	 * @param klass
	 * @param obj
	 * @param fileName
	 * @param jaxbXmlPath
	 * @return
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws SMException
	 */
	public File generateXMlFromJAXB(Class<?> klass, Object obj,String fileName,String jaxbXmlPath) throws SMException {
	
		jaxbXmlPath=jaxbXmlPath+File.separator+fileName+ BaseConstants.XML_FILE_EXT;

		File inputxml = new File(jaxbXmlPath);

		try {
				JAXBContext jaxbContext;

	
			jaxbContext = JAXBContext.newInstance(klass);
		
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(obj, inputxml);
		
		BufferedReader br =null;
		FileReader fr=null;	
		try{
			fr=new FileReader(inputxml);
			br= new BufferedReader(fr);
			 String fileContent = null;
			 StringBuffer sb=new StringBuffer();
			 while ((fileContent = br.readLine()) != null) {
				 sb.append(fileContent);
			 }
			 logger.debug("JAXB XML of "+fileName+"is"+sb.toString());

			//jaxbMarshaller.marshal(obj, System.out);
			logger.debug(inputxml.toString());

		}finally{
			if (br!=null){
				br.close();
			}
			if (fr!=null){
				fr.close();
			}
		}
		
		} catch (Exception e) {
			logger.error("Exception occured ", e);
			throw new  SMException(e.getMessage());
		}
		
		return inputxml;
	}

	/**
	 * Get XSLT From File system
	 * 
	 * @param servletContext
	 * @return File
	 * @throws SMException 
	 */
	public File getXSLTFromFileSystem(String xsltFileName,String xsltFilePath) {
		
		xsltFilePath=xsltFilePath+File.separator+xsltFileName+".xsl";

	//	String xsltFilePath = servletContext.getRealPath("/WEB-INF/resources/xslt/" + xsltFileName + ".xsl");
		return new File(xsltFilePath);
	}

	/**
	 * Do XSLT Transformation on input XML
	 * 
	 * @param xsltFile
	 * @param inputxml
	 * @return
	 * @throws TransformerException 
	 * @throws SMException 
	 */
	public String doTransformation(File xsltFile, File inputxml,int entityId) throws SMException{

		StringWriter writer = new StringWriter();
		String fileContent = null;

		TransformerFactory transFactory = TransformerFactory.newInstance();//NOSONAR
		Transformer transformer;
		try {
			transformer = transFactory.newTransformer(new StreamSource(xsltFile));

		StreamSource input = new StreamSource(inputxml);

		StreamResult output = new StreamResult(writer);
		if(entityId!=0){
			transformer.setParameter("entityId", entityId);	
		}
		
		transformer.transform(input, output);
		fileContent = writer.toString();
		} catch (TransformerException e) {
			logger.error("Exception occured ", e);
			throw new  SMException(e.getMessage());
		}

		logger.debug("The generated XML file in string :" + fileContent);

		return fileContent;
	}

}
