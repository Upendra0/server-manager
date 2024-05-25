/**
 * 
 */
package com.elitecore.sm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.MappingStyle;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.clazz.EntityDefinitionBuilder;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;

/**
 * @author Ranjitsinh Reval
 * 2,Feb,2016
 *
 */
public class JaversUtil {

	private  static Logger logger = Logger.getLogger(JaversUtil.class);
	
	JaversUtil(){
		//Default Constructor
	}
	
	/**
	 * Method will find deep level changes for modified properties between old object and new object.
	 * @param oldObj
	 * @param newObj
	 * @return List<ValueChange>
	 */
	public static List<ValueChange> compareObj(Object oldObj, Object newObj, String propertyList){
		Diff modifiedProps =  getJaverDiff(oldObj, newObj, propertyList); 
		//logger.info("Difference between old and new object for modified props: " + modifiedProps.prettyPrint());
		List<ValueChange> finalModifiedList = new ArrayList<>();
			
			List<ValueChange> modifiedPropsList = modifiedProps.getChangesByType(ValueChange.class); // Getting list of modified value.
			for (int i = 0; i < modifiedPropsList.size() ; i++) {
					
				if (logger.isDebugEnabled()) {
					logger.info("Property " + modifiedPropsList.get(i).getPropertyName() + " modified from "  + modifiedPropsList.get(i).getLeft() + " to " + modifiedPropsList.get(i).getRight());
				}
				// below condition will check for both old and new props not be null.
				if ( (!StringUtils.isEmpty(modifiedPropsList.get(i).getLeft()) && modifiedPropsList.get(i).getLeft() != null) 
					||  (!StringUtils.isEmpty(modifiedPropsList.get(i).getRight()) && modifiedPropsList.get(i).getRight() != null)) {
					finalModifiedList.add(modifiedPropsList.get(i));  //adding the modified props in list.
				}
			}
		return finalModifiedList;	
	}
	
	
		/**
		 * Method will get all added, removed and modified properties.
		 * @param oldObj
		 * @param newObj
		 * @param propertyList
		 * @return
		 */
		public static Map<String,List<?>>  getAllModifiedProp(Object oldObj, Object newObj,String propertyList){
			Map<String, List<?>> proptiesList = new HashMap<>();
			
			Diff modifiedProps =  getJaverDiff(oldObj, newObj, propertyList);
			
			List<ValueChange> modifiedPropsList = modifiedProps.getChangesByType(ValueChange.class); // Getting list of modified value.
			if(modifiedPropsList != null && !modifiedPropsList.isEmpty()){
				proptiesList.put(BaseConstants.UPDATED_PROP_LIST, modifiedPropsList);
			}
			List<NewObject> newObjectList = modifiedProps.getChangesByType(NewObject.class); // Getting list of new added object.
			if(newObjectList != null && !newObjectList.isEmpty()){
				proptiesList.put(BaseConstants.NEW_PROP_LIST, newObjectList);
			}
			List<ObjectRemoved> removedObjectList = modifiedProps.getChangesByType(ObjectRemoved.class); // Getting list of removed object.
			if(removedObjectList != null && !removedObjectList.isEmpty()){
				proptiesList.put(BaseConstants.REMOVED_PROP_LIST, removedObjectList);
			}
			return proptiesList;
			
		}
			
		
		/**
		 * Method will compare old object and new object and return difference between both object.
		 * @param oldObj
		 * @param newObj
		 * @param propertyList
		 * @return
		 */
	private static Diff getJaverDiff(Object oldObj, Object newObj, String propertyList) {
		JaversBuilder javersBuilder = JaversBuilder.javers().withMappingStyle(MappingStyle.BEAN);

		if (propertyList != null && !StringUtils.isEmpty(propertyList)) {
			List<String> ignorePropList = new ArrayList<>();
			String[] propList = propertyList.split(",");
			for (int i = 0; i < propList.length; i++) {
				ignorePropList.add(propList[i]);
			}
			javersBuilder.registerEntity(EntityDefinitionBuilder.entityDefinition(newObj.getClass()).withIgnoredProperties(ignorePropList).build());
		}

		Javers javers = javersBuilder.build();

		logger.info("Javers old object type "
				+ javers.getTypeMapping(oldObj.getClass()).prettyPrint());
		//logger.info("Old object is  " + oldObj.toString());
		logger.info("Javers new object type "
				+ javers.getTypeMapping(newObj.getClass()).prettyPrint());
		//logger.info("New  object is  " + newObj.toString());

		Diff objectDiff = javers.compare(oldObj, newObj); // Compare old object and new object.
		/*logger.info("Difference between old and new object for modified props: "
				+ objectDiff.prettyPrint());*/
		return objectDiff;
	}
	
}
