/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.elitecore.sm.parser.model.ParserAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value="netflowParserService")
public class NetflowParserServiceImpl implements NetflowParserService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	@Qualifier(value = "natFlowDictionaryAttributeService")
	private NatFlowDictionaryAttributeService natFlowDictionaryAttributeService;
	
	/**
	 * Method will read uploaded file and check attribute is defined in default attribute.xml and this will set the field in list. 
	 * 
	 */
	@Override
	public List<ParserAttribute> getAllAttributeFromFile(File file) {
		
		if(logger.isDebugEnabled()){
			logger.debug("Uploaded file found with name " + file.getName());
		}
		List<ParserAttribute> attributeList = new ArrayList<>();
		
		/*
		
		
		//HashMap<String,String> defaultAttribute =  natFlowDictionaryAttributeService.getAllDefaultDictionaryAttributes();
		String line = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
				while ((line = br.readLine()) != null) {
				
				logger.info("Line is :: "  +line);
				
				st = new StringTokenizer(line, ",");
				ParserAttribute attr;
				while (st.hasMoreTokens()) {
			           System.out.print(st.nextToken() + "  ");
			           
			          String elementId =  	st.nextToken().toString();
			           
			           if(defaultAttribute.get(elementId) != null ){
			        	   attr = new ParserAttribute("", defaultAttribute.get(st.nextToken().toString()), "", "","");
			           }else{
			        	   attr = new ParserAttribute("", st.nextToken().toString(), "", "", "");
			           }
			         
			      attributeList.add(attr);  
			     }	
			}
		}catch (Exception e) {
			logger.error("IO Exception found while reading  file: " + e.getMessage());
		}*/
		
		return attributeList;
	}

}
