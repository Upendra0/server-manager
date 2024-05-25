/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RAPATTRTYPE;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.TAPATTRTYPE;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ParserAttributeService {

	
	
	public ResponseObject getASN1AttributeListByMappingId(int mappingId,ASN1ATTRTYPE asn1attrtype);
	
	public ResponseObject convertErrorToJSONObj(List<ImportValidationErrors> attributeErrorList);
	
	public long getAttributeListCountByMappingId(int mappingId);
	
	public List<ParserAttribute> getPaginatedList(int mappingId, int startIndex, int limit, String sidx, String sord);
	
	public ResponseObject createParserAttributes(ParserAttribute parserAttribute, int mappingId,String pluginType, int staffId);
	
	public ResponseObject getAttributeById(int attributeId);
	
	public ResponseObject deleteParserAttributes(String attributeId , int staffId);
	
	public ResponseObject deleteAttribute(int attributeId, int staffId);
	
	public ResponseObject updateRegExParserAttribute(RegexParserAttribute regExParserAttr);
	
	public ResponseObject deleteRegExParserAttributeByPatternId(int regExPatternId,int staffId);
	
	public ResponseObject updateParserAttributes(ParserAttribute parserAttribute, int mappingId, String pluginType, int staffId,boolean isUpdateOrdering);
	
	public void validateImportedAttributesDetails(ParserAttribute parserAttribute, List<ImportValidationErrors> importErrorList);
	
	public void importParserAttribute(ParserMapping parserMapping);

	public ResponseObject getAttributeListByMappingId(int mappingId, String deviceType);

	public List<ParserAttribute> getASN1PaginatedList(int mappingId, int startIndex, int limit, String sidx, String sord,
			ASN1ATTRTYPE asn1attrtype);
	
	public void updateParserAttributeForImport(ParserAttribute dbParserAttribute, ParserAttribute exportedParserAttribute);
	
	public void saveParserAttributeForImport(ParserAttribute exportedParserAttribute, ParserMapping dbParserMapping, RegExPattern regexPattern);
	
	public ResponseObject getRAPAttributeListByMappingId(int mappingId,RAPATTRTYPE rapattrtype);
	
	public ResponseObject getTAPAttributeListByMappingId(int mappingId,TAPATTRTYPE tapattrtype);
	
	public ResponseObject getNRTRDEAttributeListByMappingId(int mappingId,ASN1ATTRTYPE asn1attrtype);
	
	public ResponseObject uploadParserAttributesFromCSV (File csvFile, int mappingId,String pluginType, int staffId, String actionType, int groupId);
	
	public void updateAsciiParserTypeByMappingId (int mappingId);
	
	public ResponseObject updateParserAttributesOrder(ParserMapping parserMapping,int staffId);
	
	public ResponseObject getVarLengthBinaryAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
		
	public List<String> getListOfUnifiedFields();

	public boolean associateParserAttributeGroupWithSubGroups(Map<String,List<String>> groupOfGroupMap, int mappingId);
	
	public ResponseObject generateAttributeFromJsonString(String jsonString, int mappingId, String pluginType, int staffId);

}
