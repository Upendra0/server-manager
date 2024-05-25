package com.elitecore.sm.parser.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserPageConfiguration;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
public interface ParserGroupAttributeDao extends GenericDAO<ParserGroupAttribute> {
	
	ParserGroupAttribute checkUniqueGroupAttributeNameForUpdate(int mappingId, String name);
	
	ParserGroupAttribute getGroupAttributeByGroupId(int groupId, int mappingId);
	
	List<ParserGroupAttribute> getGroupAttributeListByGroupId(int groupId, int mappingId);
	
	List<ParserGroupAttribute> getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId);
	
	List<ParserAttribute> getAttributeListEligibleToAttachWithGroup(int mappingId);
	
	List<ParserGroupAttribute> getAttachedGroupAttributeListByGroupId(int groupId, int mappingId);
	
	List<ParserAttribute> getAttachedAttributeListByGroupId(int groupId, int mappingId);
	
	List<ParserGroupAttribute> getGroupAttributeListByMappingId(int mappingId);
	
	List<ParserPageConfiguration> getParserPageConfigurationByGroupId(int groupId);

	ParserGroupAttribute getGroupAttributeByMappingIdAndGroupName(int mappingId, String groupName);
	
	String getSubGroupNameListByGroupId(ParserGroupAttribute groupAttribute);

}
