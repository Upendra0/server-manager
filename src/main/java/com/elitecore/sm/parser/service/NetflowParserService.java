package com.elitecore.sm.parser.service;

import java.io.File;
import java.util.List;

import com.elitecore.sm.parser.model.ParserAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface NetflowParserService {

	
	public List<ParserAttribute> getAllAttributeFromFile(File file);
	
}
