/**
 * 
 */
package com.elitecore.sm.common.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.json.simple.JSONValue;

import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.config.model.EntitiesRegex;
import com.elitecore.sm.config.model.EntityRegexCache;
import com.elitecore.sm.config.model.EntityValidationRange;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.VendorType;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.FileTypeEnum;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.LogLevelEnum;
import com.elitecore.sm.serverinstance.model.LogRollingTypeEnum;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.HashSeparatorEnum;
import com.elitecore.sm.services.model.MigrationStatusEnum;
import com.elitecore.sm.services.model.PartitionFieldEnum;
import com.elitecore.sm.services.model.ServiceSchedulingParams;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.systemparam.model.SystemParameterGroupData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Sunil Gulabani
 *         Jul 16, 2015
 */
public abstract class ToStringProcessor {

	@DiffIgnore
	protected Logger logger = Logger.getLogger(this.getClass().getName());

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<>(); 
		String propertyName;
		Object propertyValue = null;
		List<Field> fieldList = getAllFields(new ArrayList<Field>(), this.getClass());
		//for (Field field : this.getClass().getDeclaredFields()) {
		for (Field field : fieldList) {
			field.setAccessible(true); // if you want to modify private fields
			propertyName = field.getName();
			try {
				propertyValue = field.get(this);
			} catch (IllegalArgumentException e) {
				logger.error("Error :" + e, e);
			} catch (IllegalAccessException e) {
				logger.error("Error :" + e, e);
			}

			if (propertyName.equals("serialVersionUID")) {
				// do nothing
				// ignore the field
			} else if (propertyName.equals("logger")) {
				// do nothing
				// ignore the field
			} else if (propertyValue == null) {
				map.put(propertyName, null);
			} else if (field.getType().isAssignableFrom(String.class)) {
				map.put(propertyName, propertyValue);
			} else if (field.getType().isAssignableFrom(java.util.Date.class)) {
				map.put(propertyName, DateFormatter.formatDate((Date) propertyValue));
			} else if (field.getType().isAssignableFrom(Integer.class)
					|| field.getType().isAssignableFrom(Integer.TYPE)) {
				map.put(propertyName, Integer.valueOf(propertyValue.toString()));
			} else if (field.getType().isAssignableFrom(Long.class)
					|| field.getType().isAssignableFrom(Long.TYPE)) {
				map.put(propertyName, Long.valueOf(propertyValue.toString()));
			} else if (field.getType().isAssignableFrom(Double.class)
					|| field.getType().isAssignableFrom(Double.TYPE)) {
				map.put(propertyName, Double.valueOf(propertyValue.toString()));
			} else if (field.getType().isAssignableFrom(Float.class)
					|| field.getType().isAssignableFrom(Float.TYPE)) {
				map.put(propertyName, Float.valueOf(propertyValue.toString()));
			} else if (field.getType().isAssignableFrom(Boolean.class)
					|| field.getType().isAssignableFrom(Boolean.TYPE)) {
				map.put(propertyName, propertyValue);
			}

			// Collections
			else if (field.getType().isAssignableFrom(List.class)) {
				//map.put(propertyName, "NOT CONSIDERED TO BE PART OF toString() METHOD. Because List can be lazy initialized when loaded by Hibernate");
			} else if (field.getType().isAssignableFrom(Map.class)) {
				//map.put(propertyName, "NOT CONSIDERED TO BE PART OF toString() METHOD. Because Map can be lazy initialized when loaded by Hibernate");
			} else if (field.getType().isAssignableFrom(Set.class)) {
				//map.put(propertyName, "NOT CONSIDERED TO BE PART OF toString() METHOD. Because Set can be lazy initialized when loaded by Hibernate");
			}

			// Custom Object Type
			else if (field.getType().isAssignableFrom(StateEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(ServerType.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue);
			} else if (field.getType().isAssignableFrom(Server.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue);
			} else if (field.getType().isAssignableFrom(LogsDetail.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue);
			} else if (field.getType().isAssignableFrom(DataSourceConfig.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue);
			} else if (field.getType().isAssignableFrom(LogRollingTypeEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(LogLevelEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(SystemParameterValuePoolData.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(SystemParameterGroupData.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(ServiceType.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(ServerInstance.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(DriverType.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(PartitionFieldEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(UnifiedFieldEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(HashSeparatorEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(ParsingPathList.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(ParserMapping.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(DeviceType.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(VendorType.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(FileTypeEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(FileHeaderFooterTypeEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(EntitiesRegex.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(EntityValidationRange.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(EntityRegexCache.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(SNMPServerConfig.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(SNMPAlertWrapper.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(Action.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(AgentType.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			} else if (field.getType().isAssignableFrom(ServiceSchedulingParams.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			}else if (field.getType().isAssignableFrom(MigrationStatusEnum.class)) {
				if (propertyValue != null)
					map.put(propertyName, propertyValue.toString());
			}
			else {
				map.put(propertyName, "To add this custom object type, add else if in ToStringProcessor#toString() field.getType() is "
						+ field.getType());
			}
		}
		
		// remove following comment to get json object of server instance using jackson
		/*if(this instanceof ServerInstance){
			return super.toString();
		}*/
		if(this instanceof ErrorReprocessingBatch) {
			return super.toString();
		}

		return JSONValue.toJSONString(map);
	}

	public List<Field> getAllFields(List<Field> fields, Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}
		return fields;
	}
}