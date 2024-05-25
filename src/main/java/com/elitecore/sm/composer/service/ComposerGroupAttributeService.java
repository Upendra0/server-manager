package com.elitecore.sm.composer.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;

public interface ComposerGroupAttributeService {

	public ResponseObject getGroupAttributeByMappingId(int mappingId, int groupId);

	public ResponseObject getGroupAttributeListByMappingId(int mappingId);

	public ResponseObject deleteGroupAttributes(String groupAttributeIds, int staffId, int mappingId);

	public ResponseObject createGroupAttribute(ComposerGroupAttribute composerGroupAttribute, String groupAttrLists, String attrLists, int mappingId, String plugInType, int staffId);

	public ResponseObject updateGroupAttribute(ComposerGroupAttribute composerGroupAttribute, String groupAttrLists, String attrLists, int mappingId, String plugInType, int staffId);

	public ResponseObject getGroupAttributeListByGroupId(int groupId, int mappingId);
	
	public ResponseObject getAttachedAttributeListByGroupId(int groupId, int mappingId);
	
	public ResponseObject getAttachedGroupAttributeListByGroupId(int groupId, int mappingId);

	public ResponseObject getAttributeListEligibleToAttachWithGroup(int mappingId);

	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId);

	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByGroupId(int groupId, int mappingId);

	public void setGroupAttributeHierarchyForImport(ComposerGroupAttribute groupAttribute, ComposerMapping composerMapping);

	public List<ComposerGroupAttribute> importComposerGroupAttributesForUpdateMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping);

}
