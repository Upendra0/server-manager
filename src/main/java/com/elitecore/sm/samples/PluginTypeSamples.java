/**
 * 
 */
package com.elitecore.sm.samples;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.service.ParserService;

/**
 * @author vandana.awatramani
 *
 */
public class PluginTypeSamples {

	public void addPlugintype(ParserService parserService) {
		PluginTypeMaster pluginType = new PluginTypeMaster();
		
		
		//ASCII_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("ASCII_PARSING_PLUGIN");
		pluginType.setDescription("Description : caters to ASCII Format");
		pluginType.setAlias(EngineConstants.ASCII_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.AsciiParser");
		parserService.addPlugintype(pluginType);
		
		//VARIABLE_LENGTH_ASCII_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("VARIABLE_LENGTH_ASCII_PARSING_PLUGIN");
		pluginType.setDescription("Description : caters to Variable Length ASCII Format");
		pluginType.setAlias(EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.VarLengthAsciiParser");
		parserService.addPlugintype(pluginType);
		
		//VARIABLE_LENGTH_BINARY_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("VARIABLE_LENGTH_BINARY_PARSING_PLUGIN");
		pluginType.setDescription("Description : caters to Variable Length Binary Format");
		pluginType.setAlias(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.VarLengthBinaryParser");
		parserService.addPlugintype(pluginType);
		
		//FIXED_LENGTH_ASCII_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("FIXED_LENGTH_ASCII_PARSING_PLUGIN");
		pluginType.setDescription("Description : caters to fixed length ASCII format");
		pluginType.setAlias(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.FixedLengthASCIIParser");
		parserService.addPlugintype(pluginType);
		
		//FIXED_LENGTH_BINARY_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("FIXED_LENGTH_BINARY_PARSING_PLUGIN");
		pluginType.setDescription("Description : caters to fixed length BINARY format");
		pluginType.setAlias(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.FixedLengthBinaryParser");
		parserService.addPlugintype(pluginType);
		
		//PDF_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("PDF_PARSING_PLUGIN");
		pluginType.setDescription("Description : caters to PDF format");
		pluginType.setAlias(EngineConstants.PDF_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.PDFParser");
		parserService.addPlugintype(pluginType);
		
		//DETAIL_LOCAL_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("DETAIL_LOCAL_PARSING_PLUGIN");
		pluginType.setDescription("Description : Detail Local parser");
		pluginType.setAlias(EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.DetailLocalParser");
		parserService.addPlugintype(pluginType);
		
		//XML_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("XML_PARSING_PLUGIN");
		pluginType.setDescription("Description : XML parser");
		pluginType.setAlias(EngineConstants.XML_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.XMLParser");
		parserService.addPlugintype(pluginType);
		
		//SSTP_XML_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("SSTP_XML_PARSING_PLUGIN");
		pluginType.setDescription("Description : Collection Service collects data from network elements");
		pluginType.setAlias(EngineConstants.SSTP_XML_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.SstpXmlParser");
		parserService.addPlugintype(pluginType);
		
		//ASN1_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("ASN1_PARSING_PLUGIN");
		pluginType.setDescription("Description : ASN1_PARSING_PLUGIN");
		pluginType.setAlias(EngineConstants.ASN1_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.ASN1Parser");
		parserService.addPlugintype(pluginType);
		
		//NATFLOW_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("NATFLOW_PARSING_PLUGIN");
		pluginType.setDescription("Description : NATFLOW_PARSING_PLUGIN");
		pluginType.setAlias(EngineConstants.NATFLOW_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.NATFlowParser");
		parserService.addPlugintype(pluginType);
		
		//XLS_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("XLS_PARSING_PLUGIN");
		pluginType.setDescription("Description : XLS parser");
		pluginType.setAlias(EngineConstants.XLS_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.XLSParser");
		parserService.addPlugintype(pluginType);
		
		//JSON_PARSING_PLUGIN
		pluginType.setCategory(BaseConstants.PARSER_TYPE);
		pluginType.setType("JSON_PARSING_PLUGIN");
		pluginType.setDescription("Description : Json parser");
		pluginType.setAlias(EngineConstants.JSON_PARSING_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.parser.model.JsonParser");
		parserService.addPlugintype(pluginType);
				
		//ASCII_COMPOSER_PLUGIN
		pluginType.setCategory(BaseConstants.COMPOSERTYPE);
		pluginType.setType("ASCII_COMPOSER_PLUGIN");
		pluginType.setDescription("Description : ASCII_COMPOSER_PLUGIN");
		pluginType.setAlias(EngineConstants.ASCII_COMPOSER_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.composer.model.ASCIIComposer");
		parserService.addPlugintype(pluginType);
		
		//DETAIL_LOCAL_COMPOSER_PLUGIN
		pluginType.setCategory(BaseConstants.COMPOSERTYPE);
		pluginType.setType("DETAIL_LOCAL_COMPOSER_PLUGIN");
		pluginType.setDescription("Description : DETAIL_LOCAL_COMPOSER_PLUGIN");
		pluginType.setAlias(EngineConstants.DETAIL_LOCAL_COMPOSER_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.composer.model.DetailLocalComposer");
		parserService.addPlugintype(pluginType);
		
		//FIXED_LENGTH_ASCII_COMPOSER_PLUGIN
		pluginType.setCategory(BaseConstants.COMPOSERTYPE);
		pluginType.setType("FIXED_LENGTH_ASCII_COMPOSER_PLUGIN");
		pluginType.setDescription("Description : FIXED_LENGTH_ASCII_COMPOSER_PLUGIN");
		pluginType.setAlias(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.composer.model.FixedLengthASCIIComposer");
		parserService.addPlugintype(pluginType);
		
		//ASN1_COMPOSER_PLUGIN
		pluginType.setCategory(BaseConstants.COMPOSERTYPE);
		pluginType.setType("ASN1_COMPOSER_PLUGIN");
		pluginType.setDescription("Description : ASN1_COMPOSER_PLUGIN");
		pluginType.setAlias(EngineConstants.ASN1_COMPOSER_PLUGIN);	
		pluginType.setPluginFullClassName("com.elitecore.sm.composer.model.ASN1Composer");
		parserService.addPlugintype(pluginType);
		
	}
}
