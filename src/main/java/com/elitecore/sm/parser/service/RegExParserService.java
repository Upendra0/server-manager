package com.elitecore.sm.parser.service;

import java.io.File;
import java.util.List;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserMapping;

/**
 * 
 * @author avani.panchal
 *
 */
public interface RegExParserService {
	
	public ResponseObject generateRegExPatternToken(RegExPattern regExPattern,String patternListCounter) throws SMException ;
	
	public ResponseObject addRegExAttrBasicDetail(RegexParserMapping regExParser,File sampleFile) throws SMException;
	
	public ResponseObject getRegExParserMappingById(int regExParserMappingId);
	
	public ResponseObject addRegExPatternAndAttribute(RegExPattern regExPattern,String patternAttributeList);
	
	public ResponseObject getRegExPatternAndAttrByMappingId(int parserMappingId);
	
	public ResponseObject getSampleDataFileForRegExParser(final int parserMappingId);
	
	public ResponseObject updateRegExPatternDetail(RegExPattern regexPattern,String patternAttributeList,boolean isDeleteAttribute);
	
	public ResponseObject deleteRegExPattern(int patternId,int staffId);
	
	public void iterateAndAddRegexParserAttribute(ParserMapping parserMapping, int importMode);
	
	public void importRegexPatternForUpdateMode(RegExPattern dbRegexPattern, RegExPattern exportedRegexPattern);
	
	public void importRegexPatternForAddAndKeepBothMode(RegExPattern regExPattern, RegexParserMapping mapping, int importMode);
	
	public RegExPattern getRegexPatternFromList(List<RegExPattern> regexPatternList, String regexName);

}
