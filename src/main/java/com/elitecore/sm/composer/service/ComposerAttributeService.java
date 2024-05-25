package com.elitecore.sm.composer.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.RoamingComposerAttribute;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;

public interface ComposerAttributeService {
	
	public ResponseObject createComposerAttributes(ComposerAttribute composerAttribute, int mappingId, String pluginType, int staffId,int importMode) ;
	
	public long getAttributeListCountByMappingId(int mappingId);
	
	public List<ComposerAttribute> getPaginatedList(int mappingId, int startIndex, int limit, String sidx, String sord);
	
	public List<Map<String, Object>> getAttributeMap(List<ComposerAttribute> resultList);
	
	public ResponseObject updateComposerAttributes(ComposerAttribute composerAttribute, int mappingId, String pluginType, int staffId);
	
	public ResponseObject deleteComposerAttributes(String attributeIds, int staffId) ;
	
	public ResponseObject deleteAttribute(int attributeId, int staffId) ;
	
	public void validateComoposerAttributes(ComposerAttribute composerAttribute,  List<ImportValidationErrors> importErrorList);

	public ResponseObject getAttributeListByMappingId(int mappingId, String deviceType);

	List<Integer> getAllSequenceNumbers(int mappingId, int composerId);

	public List<ComposerAttribute> getASN1PaginatedList(int mappingId, int startIndex, int limit, String sidx,
			String sord, ASN1ATTRTYPE asn1attrtype);

	public List<Integer> getAllSequenceNumbersForASN1(ComposerAttribute composerAttr);
	
	public List<ComposerAttribute> importComposerAttributesForUpdateMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping);

	public ResponseObject getASN1AttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);

	public ResponseObject getTapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	public ResponseObject getRapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	public ResponseObject getNrtrdeAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);

	public RoamingComposerAttribute getRoamingComposerAttributeFromList(List<ComposerAttribute> composerAttributeList,String childAttributes, String destinationField, String unifiedField,boolean associatedFlag);

	public void updateComposerAttribute(ComposerAttribute dbAttribute, ComposerAttribute exportedAttribute);

	public void saveComposerAttribute(ComposerAttribute composerAttribute, ComposerMapping composerMapping, int staffId);
	
	public List<ComposerAttribute> importComposerAttributesForAddMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping);
	
	public ResponseObject uploadComposerAttributesFromCSV (File csvFile, int mappingId,String pluginType, int staffId, String actionType);

}
