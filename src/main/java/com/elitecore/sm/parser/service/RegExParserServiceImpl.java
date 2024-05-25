package com.elitecore.sm.parser.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.dao.ParserAttributeDao;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.RegExPatternDao;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Service
public class RegExParserServiceImpl implements RegExParserService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	RegExPatternDao regExPatternDao;
	
	@Autowired
	ParserAttributeService parserAttributeService;
	
	@Autowired
	SystemParameterService systemParamService;
	
	@Autowired
	ParserAttributeDao parserAttributeDao;
	
	/**
	 * Generate regEx Parser token 
	 * @throws IOException 
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseObject generateRegExPatternToken(RegExPattern regExPattern,String patternListCounter) throws SMException{
		
		ResponseObject responseObject=new ResponseObject();
		Map<String,JSONArray> attributeMap=new HashMap<>();
		String sampleDate="";
		
		String[] patternRegExArr=null;
		String patternRegExIds=regExPattern.getPatternRegExId();
		String patternRegExId="";
			
		responseObject=getSampleDataFileForRegExParser(regExPattern.getParserMapping().getId());
			
			if(responseObject!=null && responseObject.isSuccess()){
				File sampleFile=(File)responseObject.getObject();
				try(BufferedReader reader=new BufferedReader(new FileReader(sampleFile))){
				
				while((sampleDate=reader.readLine())!=null){
					
					Pattern logPattern=Pattern.compile(regExPattern.getParserMapping().getLogPatternRegex());
					Matcher logPatternMatcher=logPattern.matcher(sampleDate);
				
					while (logPatternMatcher.find()) {
						logger.debug("Log Pattern RegEx Id Found :: "+logPatternMatcher.group());
						
						if(patternRegExIds.contains(",")){
							logger.debug("Multiple Pattern RegEx Found");
							patternRegExArr=patternRegExIds.split(",");
							
							for(int i=0;i<patternRegExArr.length;i++){
								patternRegExId=patternRegExArr[i];	
								if(logPatternMatcher.group().equals(patternRegExId)){
									logger.debug("Log Pattern RegExID Match with Pattern RegEx Id" );
									if(!attributeMap.containsKey(patternRegExId)){
										responseObject=processPatternRegEx(patternRegExId,sampleDate,regExPattern,attributeMap,patternListCounter);
										if(!responseObject.isSuccess()){
											break;
										}
									}
								}
							}
							
						}else{
							logger.debug("Single Pattern RegEx Found");
							if(logPatternMatcher.group().equals(patternRegExIds)){
								logger.debug("Log Pattern RegExID Match with Pattern RegEx Id" );
								if(!attributeMap.containsKey(patternRegExIds)){
									responseObject=processPatternRegEx(patternRegExIds,sampleDate,regExPattern,attributeMap,patternListCounter);
								}
							}
						}
					}
				}
				}catch(PatternSyntaxException  exp){
					 logger.debug(" Log Pattern RegEx may not be proper."+exp);
					 responseObject.setSuccess(false);
					 responseObject.setResponseCode(ResponseCode.REGEX_PARSER_LOGPATTERN_REGEX_INVALID);
				}catch(Exception e){
					logger.error("Exception Occured:"+e);
					throw new  SMException(e.getMessage());
				}
			}
		
		return responseObject;
	}
	
	/**
	 * Add Regex Pattern and Attribute to db
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject addRegExAttrBasicDetail(RegexParserMapping regExParser,File sampleFile) throws SMException {

		ResponseObject responseObject;
		
		RegexParserMapping regExParserMapping = (RegexParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, regExParser.getId());
		List<PluginTypeMaster> pluginList=(List<PluginTypeMaster>)MapCache.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);
		
		for(PluginTypeMaster pluginType:pluginList){
			if(pluginType.getAlias().equals(regExParser.getParserType().getAlias())){
				regExParserMapping.setParserType(pluginType);
			}
		}
		
		regExParserMapping.setLogPatternRegex(regExParser.getLogPatternRegex());
		regExParserMapping.setLogPatternRegexId(regExParser.getLogPatternRegexId());
		regExParserMapping.setSrcCharSetName(regExParser.getSrcCharSetName());
		regExParserMapping.setSrcDateFormat(regExParser.getSrcDateFormat());
		regExParserMapping.setLastUpdatedByStaffId(regExParser.getLastUpdatedByStaffId());
		regExParserMapping.setLastUpdatedDate(new Date());
		
		responseObject=generateLogPatternUsingRegEx(regExParserMapping.getLogPatternRegex(),sampleFile);
		if(responseObject!=null && responseObject.isSuccess()){
			regExParserMapping.setAvilablelogPatternRegexId((String)responseObject.getObject());
			parserMappingDao.merge(regExParserMapping);
			responseObject.setResponseCode(ResponseCode.REGEX_PARSER_BASIC_DETAIL_SAVE_SUCCESS);
		}
		
		return responseObject;
	}

	/**
	 * Genearate log pattern regid
	 * @param logPatternRegex
	 * @param sampleFile
	 * @return
	 * @throws IOException 
	 * @throws SMException
	 */
	public ResponseObject generateLogPatternUsingRegEx(String logPatternRegex,File sampleFile) throws SMException {
		ResponseObject responseObject=new ResponseObject();
	
		String sampleData="";
		Set<String> avilableLogPattern=new HashSet<>();
		StringBuffer sb=null;
		try(BufferedReader reader=new BufferedReader(new FileReader(sampleFile))){
			
			while((sampleData=reader.readLine())!=null){
				
				Pattern logPattern=Pattern.compile(logPatternRegex);
				Matcher logPatternMatcher=logPattern.matcher(sampleData);
			
				while (logPatternMatcher.find()) {
					logger.debug("Log Pattern RegEx Id Found :: "+logPatternMatcher.group());
					avilableLogPattern.add(logPatternMatcher.group());
					break;
				}
			}
			
			if(!avilableLogPattern.isEmpty()){
				sb=new StringBuffer();
				
				for(String strogPatternRegex:avilableLogPattern){
					sb.append(strogPatternRegex);
					sb.append(",");
				}
				sb.replace(sb.length()-1, sb.length(), "");
				
				logger.debug("Available Log Pattern RegEx Id are : "+sb.toString());
				responseObject.setSuccess(true);
				responseObject.setObject(sb.toString());
			}
		
		}catch(PatternSyntaxException  exp){
			 logger.debug(" Log Pattern RegEx may not be proper." +exp );
			 responseObject.setSuccess(false);
			 responseObject.setResponseCode(ResponseCode.REGEX_PARSER_LOGPATTERN_REGEX_INVALID);
		}catch(Exception e){
			logger.error("Exception Occured:"+e);
			throw new  SMException(e.getMessage());
		}
		return responseObject;
		
	}
	
	/**
	 * Find sample data file from file system 
	 */
	@Override
	public ResponseObject getSampleDataFileForRegExParser(final int parserMappingId){
		
		ResponseObject responseObject=systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.REGEXPARSER);
		File sampleFile=null;
		if(responseObject.isSuccess()){
			
			String backupLocation=responseObject.getObject().toString();
			logger.debug("System Backup Location :: "+backupLocation);
			
			File directory = new File(backupLocation);
			
			String fileList[]=directory.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File directory, String name) {
					return name.startsWith(parserMappingId+"_");
				}
			});
			
			if (fileList != null && fileList.length>0) {
				
				 String filename = fileList[0];
		         logger.debug("Sample data file found : "+filename);
		         sampleFile=new File(backupLocation+File.separator+filename);
		         responseObject.setSuccess(true);
		         responseObject.setObject(sampleFile);
		      }  else {
		    	  logger.debug("Either dir does not exist or is not a directory");
		    	  responseObject.setSuccess(false);
			      responseObject.setResponseCode(ResponseCode.REGEX_PARSER_NO_SAMPLE_FILE_FOUND);
		      } 
		}
		else{
			logger.debug("System Backup path is not valid");
		}
		
		return responseObject;
	}
	
	/**
	 * Process pattern regEx
	 * @param patternRegExId
	 * @param sampleDate
	 * @param regExPattern
	 * @param attributeMap
	 * @param patternListCounter
	 * @return
	 */
	private ResponseObject processPatternRegEx(String patternRegExId,String sampleDate,RegExPattern regExPattern,Map<String,JSONArray> attributeMap,String patternListCounter){
		
		logger.debug("Generate Token for Pattern Regex Id: "+patternRegExId);
		ResponseObject responseObject=new ResponseObject();
		JSONObject jObj = null;
		JSONArray jRowArr = new JSONArray();
		int Count = 0;
		try{
			Pattern pattern=Pattern.compile (regExPattern.getPatternRegEx());
			Matcher matcher=pattern.matcher(sampleDate);
			while(matcher.find()){
				
				logger.debug("Found Sample Token for given Pattern RegEx" );
				
				 jObj = new JSONObject();
				
				 jObj.put("seqNumber", ++Count);
				 jObj.put("sampleData", matcher.group());
				 jObj.put("unifiedField", "General"+Count);
				 jObj.put("description", "");
				 jObj.put("regex", "");
				 jObj.put("defaultValue", "");
				 jObj.put("trimChars", "");
				 jObj.put("edit", "NA");
				 jObj.put("patternListCounter", patternListCounter);
			
				 logger.debug("Sequence Id - " + Count + " : "+ matcher.group());
				jRowArr.put(jObj);
			}
			attributeMap.put(patternRegExId, jRowArr);
			responseObject.setSuccess(true);
			responseObject.setObject(attributeMap);
			
		}catch(PatternSyntaxException  exp){
			 logger.debug(" Pattern RegEx may not be proper." + exp);
			 responseObject.setSuccess(false);
			 responseObject.setResponseCode(ResponseCode.REGEX_PARSER_PATTERN_REGEX_INVALID);
		}
		return responseObject;
	}
	
	/**
	 * Fetch regex parser by mapping id
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getRegExParserMappingById(int regExParserMappingId){
		ResponseObject responseObject=new ResponseObject();
		
		RegexParserMapping regExParserMapping=parserMappingDao.getRegExParserMappingById(regExParserMappingId);
		
		if(regExParserMapping !=null){
			
			responseObject.setSuccess(true);
			responseObject.setObject(regExParserMapping);
			
		}else{
			logger.debug("Parser Mapping Not Found");
		}
		
		return responseObject;
	}
	
	/**
	 * Add regex pattern and attribute
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject addRegExPatternAndAttribute(RegExPattern regExPattern,String patternAttributeList){
		
		ResponseObject responseObject=new ResponseObject();
		JSONObject patternObj=new JSONObject();
		JSONArray jAttributeArr;
		
		int count=regExPatternDao.getRegexPatternCount(regExPattern.getPatternRegExName());
		if(count > 0){
			
			logger.debug("inside addRegExPatternAndAttribute : duplicate regex Patter name found:" + regExPattern.getPatternRegExName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_REGEX_PATTERN_NAME);
			
		}else{
			RegexParserMapping regExParserMapping=parserMappingDao.getRegExParserMappingById(regExPattern.getParserMapping().getId());
			
				regExPattern.setParserMapping(regExParserMapping);
				
				responseObject=prepareRegExAttributeListForSave(patternAttributeList,regExPattern);
				
				if(responseObject.isSuccess()){
					
					regExPattern.setAttributeList((List<RegexParserAttribute>)responseObject.getObject());
					
					regExPatternDao.save(regExPattern);	
					
					if(regExPattern.getId() !=0){
						responseObject=updateConfiguredLogPattenRegExId(regExPattern.getParserMapping().getId(),regExPattern.getId(),regExPattern.getPatternRegExId());
						
						List<RegexParserAttribute> parserAttrList=regExPattern.getAttributeList();
						if(parserAttrList !=null && !parserAttrList.isEmpty()){
							jAttributeArr=new JSONArray();
							for(RegexParserAttribute parserAttr:parserAttrList){
								JSONObject parserAttrObj=new JSONObject();
								
								if(!parserAttr.getStatus().equals(StateEnum.DELETED)){
									parserAttrObj.put("id", parserAttr.getId());
									parserAttrObj.put("seqNumber", parserAttr.getSeqNumber());
									parserAttrObj.put("sampleData", parserAttr.getSampleData());
									parserAttrObj.put("unifiedField", parserAttr.getUnifiedField());
									parserAttrObj.put("description", parserAttr.getDescription());
									parserAttrObj.put("regex", parserAttr.getRegex());
									parserAttrObj.put("defaultValue", parserAttr.getDefaultValue());
									parserAttrObj.put("trimChars", parserAttr.getTrimChars());
									parserAttrObj.put("patternId", parserAttr.getPattern().getId());
									
									jAttributeArr.put(parserAttrObj);
								}
							}
							patternObj.put("parserMappingId", regExPattern.getParserMapping().getId());
							patternObj.put("patternId", regExPattern.getId());
							patternObj.put("attributeList", jAttributeArr);
							patternObj.put("logRegExPattern", (String)responseObject.getObject());
						}
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_ADD_SUCCESS);
						responseObject.setObject(patternObj);
					}else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_ADD_FAIL);
					}
				}
		}
		return responseObject;
	}
	
	/**
	 * Get regex pattern and attribute by mapping id
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getRegExPatternAndAttrByMappingId(int parserMappingId){
		
		ResponseObject responseObject=new ResponseObject();
		JSONArray jAllPatternArr = new JSONArray();
		JSONObject jPatternObj;
		JSONArray jAttributeArr;
		
		List<RegExPattern> regExPatternList=regExPatternDao.getRegExPatternListByMappingId(parserMappingId);
		
		if(regExPatternList !=null){
			
			for(RegExPattern regexPattern:regExPatternList){
				logger.debug("Found Regex Pattern , Name: " +regexPattern.getPatternRegExName());
				
				jPatternObj=new JSONObject();
				jPatternObj.put("id", regexPattern.getId());
				jPatternObj.put("patternRegExName", regexPattern.getPatternRegExName());
				jPatternObj.put("patternRegExId", regexPattern.getPatternRegExId());
				jPatternObj.put("patternRegEx", regexPattern.getPatternRegEx());
				jPatternObj.put("parserMappingId", regexPattern.getParserMapping().getId());
				
				List<RegexParserAttribute> parserAttrList=regexPattern.getAttributeList();
				if(parserAttrList !=null && !parserAttrList.isEmpty()){
					jAttributeArr=new JSONArray();
					for(RegexParserAttribute parserAttr:parserAttrList){
						JSONObject parserAttrObj=new JSONObject();
						
						if(!parserAttr.getStatus().equals(StateEnum.DELETED)){
							parserAttrObj.put("id", parserAttr.getId());
							parserAttrObj.put("seqNumber", parserAttr.getSeqNumber());
							parserAttrObj.put("sampleData", parserAttr.getSampleData());
							parserAttrObj.put("unifiedField", parserAttr.getUnifiedField());
							parserAttrObj.put("description", parserAttr.getDescription());
							parserAttrObj.put("regex", parserAttr.getRegex());
							parserAttrObj.put("defaultValue", parserAttr.getDefaultValue());
							parserAttrObj.put("trimChars", parserAttr.getTrimChars());
							parserAttrObj.put("patternId", parserAttr.getPattern().getId());
							
							jAttributeArr.put(parserAttrObj);
						}
					}
					jPatternObj.put("attributeList", jAttributeArr);
				}
				jAllPatternArr.put(jPatternObj);
			}
			
			responseObject.setSuccess(true);
			responseObject.setObject(jAllPatternArr);
		}else{
			responseObject.setSuccess(false);
			logger.debug("No RegEx Pattern is configured for this mapping id");
		}
		
		return responseObject;
	}
	
	/**
	 * Update regex pattern detail 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject updateRegExPatternDetail(RegExPattern regexPattern,String patternAttributeList,boolean isDeleteAttribute){
		ResponseObject responseObject=new ResponseObject();
		
		if (isPatternNameUniqueForUpdate(regexPattern.getId(), regexPattern.getPatternRegExName())) {
			if(regexPattern.getId() !=0){
				RegExPattern regExPatternDB=regExPatternDao.findByPrimaryKey(RegExPattern.class, regexPattern.getId());
				
				if(regExPatternDB !=null){
					
					regExPatternDB.setPatternRegExName(regexPattern.getPatternRegExName());
					regExPatternDB.setPatternRegExId(regexPattern.getPatternRegExId());
					regExPatternDB.setPatternRegEx(regexPattern.getPatternRegEx());
					regExPatternDB.setLastUpdatedByStaffId(regexPattern.getLastUpdatedByStaffId());
					regExPatternDB.setLastUpdatedDate(new Date());
					
					if(isDeleteAttribute){

							logger.debug("Going to Delete all old attributes");
								
							responseObject=parserAttributeService.deleteRegExParserAttributeByPatternId(regexPattern.getId(),regexPattern.getLastUpdatedByStaffId());
								
								if(responseObject.isSuccess()){
									
									RegexParserMapping regExParserMapping=parserMappingDao.getRegExParserMappingById(regexPattern.getParserMapping().getId());
									
									regExParserMapping.setLogPatternRegexId(regexPattern.getParserMapping().getLogPatternRegexId());
									
									regExPatternDB.setParserMapping(regExParserMapping);
									
									logger.debug("All Old Attribute Deleted Successfully, create new attribute ");
									
									responseObject=prepareRegExAttributeListForSave(patternAttributeList,regExPatternDB);
									if(responseObject.isSuccess()){
										
										regExPatternDB.setAttributeList((List<RegexParserAttribute>)responseObject.getObject());
										
										regExPatternDao.merge(regExPatternDB);	
										responseObject=updateConfiguredLogPattenRegExId(regexPattern.getParserMapping().getId(),regexPattern.getId(),regexPattern.getPatternRegExId());
										responseObject.setSuccess(true);
										responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_UPDATE_SUCCESS);
									}
								}
							
					}else{
						logger.debug("Pattern RegEx name is updated , not process delete attribute flow");
						
						regExPatternDao.merge(regExPatternDB);
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_UPDATE_SUCCESS);
					}
					}else{
						logger.debug("RegEx Pattern not found from DB");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_UPDATE_FAIL);
					}
				}else{
					logger.debug("Pattern id not found");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_UPDATE_FAIL);
				}
		}else{
			logger.debug("inside updateRegExPatternDetail : duplicate regex Patter name found:" + regexPattern.getPatternRegExName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_REGEX_PATTERN_NAME);
		}
		
		
		return responseObject;
	}
	
	/**
	 * Prepare regex attribute list for save
	 * @param patternAttributeList
	 * @param regExPattern
	 * @return
	 */
	public ResponseObject prepareRegExAttributeListForSave(String patternAttributeList,RegExPattern regExPattern){
		
		ResponseObject responseObject=new ResponseObject();
		List<RegexParserAttribute> regExParserAttributeList=new ArrayList<>();
		JSONArray jPatternAttrArr = new JSONArray(patternAttributeList);
		
		for(int index=0;index<jPatternAttrArr.length();index++){
			JSONObject jAttr = jPatternAttrArr.getJSONObject(index);
			RegexParserAttribute regExAttribute=new RegexParserAttribute();
			
			regExAttribute.setSeqNumber(jAttr.getInt("seqNumber"));
			regExAttribute.setRegex(jAttr.getString("regex"));
			regExAttribute.setSampleData(jAttr.getString("sampleData"));
			regExAttribute.setDescription(jAttr.getString("description"));
			regExAttribute.setUnifiedField(UnifiedFieldEnum.valueOf(jAttr.getString("unifiedField")).getName());
			regExAttribute.setDefaultValue(jAttr.getString("defaultValue"));
			regExAttribute.setTrimChars(jAttr.getString("trimChars"));
			regExAttribute.setPattern(regExPattern);
			
			regExParserAttributeList.add(regExAttribute);
			}
		responseObject.setSuccess(true);
		responseObject.setObject(regExParserAttributeList);
		return responseObject;
	}
	
	/**
	 * Delete reg ex pattern 
	 */
	@Override
	@Transactional
	public ResponseObject deleteRegExPattern(int patternId,int staffId){
		ResponseObject responseObject=new ResponseObject();
		
		if(patternId !=0){
			RegExPattern regExPatternDB=regExPatternDao.findByPrimaryKey(RegExPattern.class, patternId);
			
			if(regExPatternDB !=null){
				regExPatternDB.setPatternRegExName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,regExPatternDB.getPatternRegExName()));
				regExPatternDB.setStatus(StateEnum.DELETED);
				regExPatternDB.setLastUpdatedByStaffId(staffId);
				regExPatternDB.setLastUpdatedDate(new Date());
				
				parserAttributeService.deleteRegExParserAttributeByPatternId(patternId,staffId);
				regExPatternDao.merge(regExPatternDB);
				
				responseObject=updateConfiguredLogPattenRegExId(regExPatternDB.getParserMapping().getId(),patternId,null);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_DELETE_SUCCESS);
				
			}else{
				logger.debug("RegEx Pattern not found from DB");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_DELETE_FAIL);
			}
		}else{
			logger.debug("Pattern id not found");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.REGEX_PATTERN_DELETE_FAIL);
		}
		return responseObject;
	}
	
	/**
	 * Update logpattern regex id after add,update,delete
	 * @param mappingId
	 * @param pattenId
	 * @param patternRegExId
	 * @return
	 */
	private ResponseObject updateConfiguredLogPattenRegExId(int mappingId,int pattenId,String patternRegExId){
		ResponseObject responseObject=new ResponseObject();
		Set<String> logRegExIdSet=new HashSet<>();
		if(mappingId !=0){
			List<RegExPattern> regExPatternList=regExPatternDao.getRegExPatternListByMappingId(mappingId);
			if(regExPatternList !=null){
				for(RegExPattern regExPattern:regExPatternList){
					if(regExPattern.getId() !=pattenId){
						logger.debug("Prepare set by using all DB pattern without using current which is delete");
						String logIdDB=regExPattern.getPatternRegExId();
						StringTokenizer tokens=new StringTokenizer(logIdDB,",");
						 while (tokens.hasMoreTokens()) {  
							 logRegExIdSet.add(tokens.nextToken());
						 }
					}
				}
				
				if(patternRegExId !=null){
					logger.debug("Add current pattern is set which is add or update");
					
					StringTokenizer tokens=new StringTokenizer(patternRegExId,",");
					 while (tokens.hasMoreTokens()) {  
						 logRegExIdSet.add(tokens.nextToken());
					 }
				}
				logger.debug("Final set found for update logregexid: "+logRegExIdSet);
				
				RegexParserMapping regExParser=(RegexParserMapping)parserMappingDao.findByPrimaryKey(ParserMapping.class, mappingId);
				StringBuilder sb=new StringBuilder();
				for(String logrexid: logRegExIdSet){
					sb.append(logrexid);
					sb.append(",");
				}
				sb.replace(sb.length()-1, sb.length(), "");
			
				regExParser.setLogPatternRegexId(sb.toString());
				
				parserMappingDao.merge(regExParser);
				responseObject.setSuccess(true);
				responseObject.setObject(regExParser.getLogPatternRegexId());
			}
		}
		return responseObject;
	}
	
	/**
	 * Check pattern name is unique or not for update 
	 * @param patternId
	 * @param patternName
	 * @return
	 */
	@Transactional
	public boolean  isPatternNameUniqueForUpdate(int patternId,String patternName){
		List<RegExPattern> patternList=regExPatternDao.getPatternListByName(patternName);
		boolean isUnique=false;
		if(patternList!=null && !patternList.isEmpty()){
			
			for(RegExPattern pattern:patternList){
				//If ID is same , then it is same driver object
				if(patternId == (pattern.getId())){
					isUnique=true;
				}else{ // It is another driver object , but name is same
					isUnique=false;
				}
			}
		}else if(patternList!=null && patternList.isEmpty()){ // No driver found with same name 
			isUnique=true;
		}
		
		return isUnique;
	}

	/**
	 * Method will iterate and add regex parser attributes and patter to mapping object.
	 * @param parserMapping
	 */
	@Override
	public void iterateAndAddRegexParserAttribute(ParserMapping parserMapping, int importMode) {
		RegexParserMapping mapping = (RegexParserMapping)parserMapping;
		List<RegExPattern> patternList = mapping.getPatternList();
			if(patternList != null && !patternList.isEmpty()){
				logger.debug("Iterating patter list.");
				for (RegExPattern regExPattern : patternList) {
					importRegexPatternForAddAndKeepBothMode(regExPattern, mapping, importMode);
				}		
			}else{
				logger.debug("No patten found for regex parser details");
			}
			mapping.setPatternList(patternList);
	}
	
	@Override
	public void importRegexPatternForUpdateMode(RegExPattern dbRegexPattern, RegExPattern exportedRegexPattern) {
		dbRegexPattern.setPatternRegExId(exportedRegexPattern.getPatternRegExId());
		dbRegexPattern.setPatternRegEx(exportedRegexPattern.getPatternRegEx());
		dbRegexPattern.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		
		List<RegexParserAttribute> dbRegexParserAttributeList = dbRegexPattern.getAttributeList();
		List<RegexParserAttribute> exportedRegexParserAttributeList = exportedRegexPattern.getAttributeList();
		
		if(exportedRegexParserAttributeList != null && !exportedRegexParserAttributeList.isEmpty()) {
			int attributeLength = exportedRegexParserAttributeList.size();
			for(int j = attributeLength-1; j >= 0; j--) {
				RegexParserAttribute exportedRegexParserAttribute = exportedRegexParserAttributeList.get(j);
				if(exportedRegexParserAttribute != null && !exportedRegexParserAttribute.getStatus().equals(StateEnum.DELETED)) {
					RegexParserAttribute dbRegexParserAttribute = getRegexParserAttributeFromList(dbRegexParserAttributeList, exportedRegexParserAttribute.getSampleData(), exportedRegexParserAttribute.getUnifiedField());
					if(dbRegexParserAttribute != null) {
						parserAttributeService.updateParserAttributeForImport(dbRegexParserAttribute, exportedRegexParserAttribute);
						dbRegexParserAttributeList.add(dbRegexParserAttribute);
					} else {
						parserAttributeService.saveParserAttributeForImport(exportedRegexParserAttribute, null, dbRegexPattern);
						dbRegexParserAttributeList.add(exportedRegexParserAttribute);
					}
				}
			}
		}
	}
	
	public RegexParserAttribute getRegexParserAttributeFromList(List<RegexParserAttribute> parserAttributeList, String sourceField, String unifiedField) {
		if(!CollectionUtils.isEmpty(parserAttributeList)) {
			int length = parserAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				RegexParserAttribute regexAttribute = parserAttributeList.get(i);
				if(regexAttribute != null && !regexAttribute.getStatus().equals(StateEnum.DELETED)
						&& regexAttribute.getSampleData().equalsIgnoreCase(sourceField)
						&& regexAttribute.getUnifiedField().equalsIgnoreCase(unifiedField)) {
					return parserAttributeList.remove(i);
				}
			}
		}
		return null;
	}
	
	@Override
	public RegExPattern getRegexPatternFromList(List<RegExPattern> regexPatternList, String regexName) {
		if(!CollectionUtils.isEmpty(regexPatternList)) {
			int length = regexPatternList.size();
			for(int i = length-1; i >= 0; i--) {
				RegExPattern regexPattern = regexPatternList.get(i);
				if(regexPattern != null && !regexPattern.getStatus().equals(StateEnum.DELETED)
						&& regexPattern.getPatternRegExName().equalsIgnoreCase(regexName)) {
					return regexPatternList.remove(i);
				}
			}
		}
		return null;
	}
	
	@Override
	public void importRegexPatternForAddAndKeepBothMode(RegExPattern regExPattern, RegexParserMapping mapping, int importMode) {
		Date date = new Date();
		regExPattern.setId(0);
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH || importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			regExPattern.setPatternRegExName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,regExPattern.getPatternRegExName()));
		}
		regExPattern.setId(0);
		regExPattern.setCreatedDate(date);
		regExPattern.setLastUpdatedDate(date);
		
		List<RegexParserAttribute> regexAttributeList = regExPattern.getAttributeList();
		if(regexAttributeList != null && !regexAttributeList.isEmpty()){
			logger.debug("Iterating attribute list for patter.");
			int length = regexAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				RegexParserAttribute regexParserAttribute = regexAttributeList.get(i);
				regexParserAttribute.setId(0);
				regexParserAttribute.setCreatedDate(date);
				regexParserAttribute.setLastUpdatedDate(date);
				regexParserAttribute.setPattern(regExPattern);
			}
		}
		
		regExPattern.setAttributeList(regexAttributeList);
		regExPattern.setParserMapping(mapping);
	}
}