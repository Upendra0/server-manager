package com.elitecore.sm.composer.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;

public interface ComposerGroupAttributeDao extends GenericDAO<ComposerGroupAttribute> {

	List<ComposerGroupAttribute> getGroupAttributeListByMappingId(int mappingId);

	ComposerGroupAttribute getGroupAttributeByMappingId(int mappingId, int groupId);

	ComposerGroupAttribute checkUniqueGroupAttributeNameForUpdate(int mappingId, String groupAttributeName);

	List<ComposerGroupAttribute> getGroupAttributeListByGroupId(int groupId, int mappingId);
	
	List<ComposerAttribute> getAttachedAttributeListByGroupId(int groupId, int mappingId);
	
	List<ComposerGroupAttribute> getAttachedGroupAttributeListByGroupId(int groupId, int mappingId);

	List<ComposerAttribute> getAttributeListEligibleToAttachWithGroup(int mappingId);

	List<ComposerGroupAttribute> getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId);

	ComposerGroupAttribute getGroupAttributeByGroupId(int groupId, int mappingId);

}
