package com.elitecore.sm.parser.controller;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.DetailLocalParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.HtmlParserAttribute;
import com.elitecore.sm.parser.model.JsonParserAttribute;
import com.elitecore.sm.parser.model.MTSiemensParserAttribute;
import com.elitecore.sm.parser.model.NRTRDEParserAttribute;
import com.elitecore.sm.parser.model.PDFParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.RAPParserAttribute;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.VarLengthAsciiParserAttribute;
import com.elitecore.sm.parser.model.VarLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.XMLParserAttribute;
import com.elitecore.sm.parser.model.NatFlowParserAttribute;
import com.elitecore.sm.parser.model.XlsParserAttribute;

@Component
public class ParserAttributeFactory {
	
	public static ParserAttribute getParserAttributeByType(String pluginType) {
		ParserAttribute parserAttribute = null;
		if (pluginType == null) {
			return parserAttribute;
		}
		if (EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new AsciiParserAttribute();
		} else if (EngineConstants.XML_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new XMLParserAttribute();
		} else if (EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new DetailLocalParserAttribute();
		} else if (EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new ASN1ParserAttribute();
		} else if (EngineConstants.RAP_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new RAPParserAttribute();
		} else if (EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new TAPParserAttribute();
		} else if (EngineConstants.NRTRDE_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new NRTRDEParserAttribute();
		} else if (EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new FixedLengthASCIIParserAttribute();
		} else if (EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new FixedLengthBinaryParserAttribute();
		} else if (EngineConstants.HTML_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new HtmlParserAttribute();	
		}else if (EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new PDFParserAttribute();
		}else if (EngineConstants.XLS_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new XlsParserAttribute();
		}else if (EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new VarLengthAsciiParserAttribute();
		}else if (EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new VarLengthBinaryParserAttribute();
		}else if (EngineConstants.JSON_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new JsonParserAttribute();
		}else if (EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new MTSiemensParserAttribute();
		}else if (EngineConstants.NATFLOW_PARSING_PLUGIN.equals(pluginType)) {
			parserAttribute = new NatFlowParserAttribute();
		}else {
			parserAttribute = new ParserAttribute();
		}
		return parserAttribute;
	}
}
