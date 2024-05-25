package com.elitecore.sm.util;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.elitecore.sm.common.constants.BaseConstants;


@Component(value="languageUtilProperties")
public class LanguageUtilProperties {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	 @Resource(name="langProperties")
	 Properties properties;
	
	 @Resource(name="localeResolver")
	 CookieLocaleResolver localeResolver;
	 
	 @Autowired
	 ServletContext context;  // to get class path location
	
	private int fileCounter = 0 ;  // It will check total number of files found during iteration.
	 
	/**
	 * Method will read all properties from file and add it to the Map and validate 
	 * the properties based on the locale files are present or not.
	 * @return HashMap
	 */
	public Map<String, String> getAllProperties(){
		
		if(properties.get("default.lang") != null ){
			localeResolver.setDefaultLocale(new Locale(String.valueOf(properties.get("default.lang"))));
			logger.info("Found default language from property file is :: " + properties.get("default.lang"));
		}else{
			localeResolver.setDefaultLocale(new Locale(BaseConstants.APP_DEFAULT_LANG));
			logger.info("Default language property missing so setting app default value :: " + BaseConstants.APP_DEFAULT_LANG);
		}
		
		Enumeration<Object> em = properties.keys();
		HashMap<String, String> langPropList = new HashMap<>();
		
		  while(em.hasMoreElements()){
			  String langKey = (String)em.nextElement();
			  fileCounter = 0;
			  if(langKey.indexOf("file") < 0  ){ // condition for remove unnecessary iteration from loop.
				 if(langKey.indexOf("default.lang") < 0 && validateLanguageProps(langKey)){ //condition for remove unnecessary iteration from loop and validate file count 
					 langPropList.put(langKey, properties.get(langKey).toString());
				 } else if(langKey.indexOf("default.lang") >= 0){
					 langPropList.put(langKey, properties.get(langKey).toString()); // Default set value will be use to set value in dropdown.
				 }
			  }
		  }
		return langPropList;
	}
	
	/**
	 * Method will validate the language based on its properties files available or not.
	 * @param langLocale
	 * @return int
	 */
	private boolean validateLanguageProps(String langLocale){
		String fullPath = context.getRealPath(BaseConstants.CLASS_PATH);
	
		File currentDir = new File(fullPath);
		logger.debug("Going to read files from  " + fullPath + " for locale " + langLocale);
		int finalFileCounter = checkLangFileCount(currentDir , langLocale);
		int appFileCount = 0;
		
		if(properties.get("file.count") != null ){
			appFileCount = Integer.parseInt(String.valueOf(properties.get("file.count")));
			logger.info("Found Total default file counter from property is :: " + appFileCount);
		}else{
			appFileCount = BaseConstants.APP_LANGUAGE_FILE_COUNT;
			logger.info("Total file counter not found from propertie file so setting default system file count : " + appFileCount);
		}
		
		logger.info("Total number of files found during iteration is :: " + finalFileCounter);
		if(finalFileCounter >=  appFileCount ){
			return true ;
		}else {
			return false;
		}
	}
	
	/**
	 * Method will check it file counter based on we set in language.properties files.
	 * @param currentDir
	 * @param langLocale
	 * @return int
	 */
	private int checkLangFileCount(File currentDir ,String langLocale){
		File[] filesList = currentDir.listFiles();
		 
		if(filesList != null && filesList.length > 0){
			 for (File file : filesList) {
			     if(file.isDirectory() && (BaseConstants.MODULE_COMMON_FOLDER.equals(file.getName()) || BaseConstants.MODULE_SERVER_FOLDER.equals(file.getName()) || BaseConstants.MODULE_SERVICE_FOLDER.equals(file.getName()) )  ){
			    	 logger.debug("Going to iterate files from directory " + file.getName() + " for locale " + langLocale);
			    	 checkLangFileCount(file , langLocale);
			     }else {
			    	 if(file.isFile() && file.getName().indexOf(langLocale) >= 0){
			    		 logger.debug(file.getName() + "file found for locale "  + langLocale);
			    		 fileCounter++ ;
			    	 }
			     }   
		     }
		 }
		return fileCounter; 
	}
	
	/**
	 * @param properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
