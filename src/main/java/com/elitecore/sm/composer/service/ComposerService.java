/**
 * 
 */
package com.elitecore.sm.composer.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.Composer;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ComposerService {

	
	public ResponseObject addComposer(Composer composer);
	
	public ResponseObject updateComposer(Composer composer);
	
	public boolean isUniqueComposer(String name);
	
	public boolean isUniqueComposerForUpdate(String name,int composerId);
	
	public ResponseObject deleteComposer(String pluginIds , int staffId);
	
	public ResponseObject deleteComposerPluginAndItsDependents(int composerId, int staffId);
	
	public Composer getComposerMappingDetailsByComposerId(int composerId);
	
	public ResponseObject updateComposerMapping(Composer composer) ;
	
	public void  validateComposerDetails(Composer composer, List<ImportValidationErrors> importErrorList);
	
	public Composer getComposerById(int composerId);
	
	public void importComposerForUpdateMode(Composer dbComposer, Composer exportedComposer);
	
	public Composer getComposerFromList(List<Composer> composerList, String composerAlias, String composerName);
	
	public void importComposerForAddMode(Composer dbComposer, Composer exportedComposer);
	
}
