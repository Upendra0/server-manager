package com.elitecore.sm.parser.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.ParserPageConfiguration;

public interface ParserGroupAttributeService {
	
	public ResponseObject createGroupAttribute(ParserGroupAttribute parserGroupAttribute, String groupAttrLists, String attrLists, int mappingId, String plugInType, int staffId);

	public ResponseObject updateGroupAttribute(ParserGroupAttribute parserGroupAttribute, String groupAttrLists, String attrLists, int mappingId, String plugInType, int staffId);
	
	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId);
	
	public ResponseObject getAttributeListEligibleToAttachWithGroup(int mappingId);
	
	public ResponseObject getAttachedGroupAttributeListByGroupId(int groupId, int mappingId);
	
	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByGroupId(int groupId, int mappingId);
	
	public ResponseObject getAttachedAttributeListByGroupId(int groupId, int mappingId);
	
	public ResponseObject getGroupAttributeListByGroupId(int groupId, int mappingId);
	
	public ResponseObject getGroupAttributeListByMappingId(int mappingId);
	
	public ResponseObject deleteGroupAttributes(String groupAttributeIds, int staffId, int mappingId);
	
	public void setGroupAttributeHierarchyForImport(ParserGroupAttribute groupAttribute, ParserMapping parserMapping);

	public List<ParserGroupAttribute> importParserGroupAttributesForUpdateMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping);
	
	public List<ParserGroupAttribute> importParserGroupAttributesForAddMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping);
	
	public ResponseObject createParserGroupAttributes(ParserGroupAttribute parserGroupAttributeExp, int mappingId, String pluginType, int staffId);
	
	public ResponseObject updateParserGroupAttributes(ParserGroupAttribute parserGroupAttribute, int mappingId, String pluginType, int staffId,boolean isUpdateOrdering);
	
	public ResponseObject deleteGroupAttributeWithHierarchy(int groupAttributeId,  int staffId, int mappingId);
	
	ResponseObject getPageConfigListGroupId(int groupId);
	
	ResponseObject createParserPageConfiguration(ParserPageConfiguration parserPage, int staffId, int groupId);
	
	ResponseObject updateParserPageConfiguration(ParserPageConfiguration parserPage, int staffId, int groupId);
	
	public ResponseObject deletePageConfiguration(String pageIds, int staffId);
	
	public ResponseObject getGroupAttributeByMappingIdAndGroupName(int mappingId, String groupName);
	
	public String getSubGroupNameListByGroupId(ParserGroupAttribute groupAttribute);

}
