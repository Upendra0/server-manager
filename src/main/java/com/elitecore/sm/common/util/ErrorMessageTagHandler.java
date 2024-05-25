package com.elitecore.sm.common.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * Class is used as handler for custom tag which is used to show errors in jsp
 * @author avani.panchal
 *
 */
public class ErrorMessageTagHandler extends TagSupport{
	private transient Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -7258875937649752549L;
	
	private String errorId;
	private String errorMessage;
	
	

	/**
	 * 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	/**
	 * @param errorId
	 */
	
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	@Override
	public int doStartTag() throws JspException{
		JspWriter out=pageContext.getOut();
		try {
			out.write("<span class=\"input-group-addon add-on last\" >\r\n");
			if(errorMessage != null && !"".equalsIgnoreCase(errorMessage.trim())){
				out.write("<i id=\""+errorId+"\"class=\"glyphicon glyphicon-alert\" data-toggle=\"tooltip\" data-placement=\"left\" title=\""+errorMessage+"\"></i>");
			} else {
				out.write("<i class=\"glyphicon glyphicon-alert\" data-toggle=\"tooltip\" data-placement=\"left\" title=\""+errorMessage+"\"></i>");
			}
			out.write("</span>\r\n");
		} catch(IOException e) {
			logger.error("Error in tag rendering", e);
		}
		return SKIP_BODY;
	}
}