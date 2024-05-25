package com.elitecore.sm.composer.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;
import com.elitecore.sm.composer.model.RoamingComposerMapping;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;

/**
 * 
 * @author avani.panchal
 *
 */
public interface ComposerMappingService {
	
	public ResponseObject getMappingByDeviceAndComposerType(int deviceId,	String composerType);
	
	public ResponseObject createOrUpdateComposerMappingDetails(ComposerMapping newComposerMapping, int mappingId, int staffId, String actionType, String composerType) throws CloneNotSupportedException;
	
	public ResponseObject createNewMapping(ComposerMapping composerMapping);
	
	public ResponseObject updateAndAssociateComposerMapping(ComposerMapping composerMapping, String pluginType, int plugInId, String actionType, int staffId) ;
	
	public ResponseObject getComposerMappingDetailsById(int mappingId, String pluginType) ;
	
	public ResponseObject getMappingAssociationDetails(int mappingId);
	
	public ResponseObject getComposerMappingDetailsByNameAndType(String name, int pluginTypeId);
	
	public ResponseObject importComposerMappingAndDependents(Composer composer, boolean isImport, int importMode) ;
	
	public void validateImportedMappingDetails(ComposerMapping composerMapping, List<ImportValidationErrors> importErrorList);
	
	public ResponseObject setAndUpdateAsciiComposerDetail(ASCIIComposerMapping newasciiComposerMapping, ASCIIComposerMapping dbasciiComposerMapping);

	public ComposerMapping getComposerMappingById(int composerMapppingId);
	
	public ResponseObject setAndUpdateAsn1ComposerDetail(ASN1ComposerMapping newasn1ComposerMapping, ASN1ComposerMapping dbasn1ComposerMapping);
	
	public ResponseObject setAndUpdateRoamingComposerDetail(RoamingComposerMapping newRoamingComposerMapping,RoamingComposerMapping dbRoamingComposerMapping);
	
	public ASN1ComposerMapping getAsn1ComposerMappingById(int mappingId);
	
	public ResponseObject setAndUpdateFixedLengthAsciiComposerMapping(FixedLengthASCIIComposerMapping newFixedLengthAsciiMapping, FixedLengthASCIIComposerMapping dbFixedLengthAsciiMapping);
	
	public void createComposerAttributes(ComposerMapping mappingObj);
	
	public void importComposerMappingForUpdateMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping);
	
	public ResponseObject getAllMappingById(Integer[] ids);
	
	public ResponseObject getAllMappingByDeviceId(Integer[] deviceIds);
	
	public ResponseObject deleteMapping(ComposerMapping composerMapping, int staffId);
	
	public void importComposerMappingForAddAndKeepBothMode(Composer exportedComposer, DistributionDriverPathList pathList, int importMode);
	
	public void importComposerMappingForAddMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping);

}
