package com.elitecore.sm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONValue;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.productconfig.model.ProfileEntity;

/**
 * 
 * @author avani.panchal
 *  Convert Profile Entity list to JSTree data
 *
 */
public class JSTreeProductConfigurationData {
	private List<ProfileEntity> profileEntityList;

	public JSTreeProductConfigurationData() {
		// Default Constructor
	}

	public JSTreeProductConfigurationData(List<ProfileEntity> profileEntityList) {
		this.profileEntityList = profileEntityList;
	}

	@Override
	public String toString() {
		List<Map<String, Object>> rootList = new ArrayList<>();

		if (profileEntityList != null) {
			Set<String> entityTypeSet = new LinkedHashSet<>();
			for (ProfileEntity profileEntity : profileEntityList) {

				entityTypeSet.add(profileEntity.getEntityType());
			}

			Map<String, Object> entityTypeMap;

			for (String entityType : entityTypeSet) {

				entityTypeMap = new LinkedHashMap<>();
				entityTypeMap.put("id", "ENTITY_TYPE" + entityType);
				entityTypeMap.put("text", entityType);
				entityTypeMap.put("icon", "images/tabs.png");

				List<Map<String, Object>> entityAliasList = new ArrayList<>();
				Map<String, Object> entityAliasMap;

				for (ProfileEntity subprofileEntity : profileEntityList) {
					if (entityType.equals(subprofileEntity.getEntityType())) {

						entityAliasMap = new HashMap<>();
						entityAliasMap.put("id", "ENTITY_ALIAS_" + subprofileEntity.getId());
						entityAliasMap.put("text", subprofileEntity.getEntityAlias());
						entityAliasMap.put("icon", "images/action.png");

						if (StateEnum.ACTIVE.equals(subprofileEntity.getStatus())) {
							Map<String, Object> entityAliasStatus = new HashMap<>();
							entityAliasStatus.put("selected", true);

							entityAliasMap.put("state", entityAliasStatus);
						}

						entityAliasList.add(entityAliasMap);
					}
				}

				if (!entityAliasList.isEmpty()) {
					entityTypeMap.put("children", entityAliasList);
				}
				rootList.add(entityTypeMap);

			}
		}

		return JSONValue.toJSONString(rootList);
	}
}
