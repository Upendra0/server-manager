/**
 * 
 */
package com.elitecore.sm.systemaudit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.service.ActionService;
import com.elitecore.sm.systemaudit.dao.AuditDetailsDao;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "auditDetailsService")
public class AuditDetailsServiceImpl implements AuditDetailsService {

	@Autowired
	private AuditDetailsDao auditDetailsDao;
	
	@Autowired
	private ActionService actionService;
	
	
	
	/**
	 * Method will set all updated properties to system audit details.
	 * @param modifiedProps
	 * @return List<SystemAuditDetails>
	 */
	@Override
	public List<SystemAuditDetails> getAllModifiedProps(List<ValueChange> modifiedProps) {
		List<SystemAuditDetails> auditDetailsList = null;
		
		if (modifiedProps != null && !modifiedProps.isEmpty() ) {
			auditDetailsList = new ArrayList<>();
			for (int i = 0; i < modifiedProps.size(); i++) {
				String propertyName =  String.valueOf(modifiedProps.get(i).getPropertyName());
					SystemAuditDetails auditDetails = new SystemAuditDetails(propertyName, String.valueOf(modifiedProps.get(i).getLeft()), String.valueOf(modifiedProps.get(i).getRight()));
					auditDetailsList.add(auditDetails); 
			}
			return auditDetailsList;
		}else{
			return auditDetailsList;
		}
	}
	
	/**
	 * Method will set all updated properties to system audit details.
	 * @param modifiedProps
	 * @return List<SystemAuditDetails>
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<SystemAuditDetails> getAllPropties(Map<String, List<?>> allPropsList) {
		List<SystemAuditDetails> auditDetailsList = null;
		
		if(allPropsList != null && !allPropsList.isEmpty()){
			auditDetailsList = new ArrayList<>();
			
			List<ValueChange> modifiedPropList  = (List<ValueChange>) allPropsList.get(BaseConstants.UPDATED_PROP_LIST);
			if(modifiedPropList != null && !modifiedPropList.isEmpty()){
				for (int i = 0; i < modifiedPropList.size(); i++) {
					
					if(!BaseConstants.NULL.equalsIgnoreCase(String.valueOf(modifiedPropList.get(i).getLeft()))  && !BaseConstants.NULL.equalsIgnoreCase(String.valueOf(modifiedPropList.get(i).getRight()))){
						String propertyName =  String.valueOf(modifiedPropList.get(i).getPropertyName());
						SystemAuditDetails auditDetails = new SystemAuditDetails(propertyName, String.valueOf(modifiedPropList.get(i).getLeft()), String.valueOf(modifiedPropList.get(i).getRight()));
						auditDetailsList.add(auditDetails);
					}
				}
			}
			List<NewObject> newObjectList  = (List<NewObject>) allPropsList.get(BaseConstants.NEW_PROP_LIST);
			if(newObjectList != null && !newObjectList.isEmpty()){
				for (int i = 0; i < newObjectList.size(); i++) {
					
					ResponseObject responseObject = actionService.getActionById(Integer.parseInt(String.valueOf(newObjectList.get(i).getAffectedLocalId())));
					if(responseObject.isSuccess()){
						Action action = (Action) responseObject.getObject();
						SystemAuditDetails auditDetails = new SystemAuditDetails(action.getName(), BaseConstants.NA, "New action "+action.getName()+" added.");
						auditDetailsList.add(auditDetails);
					}
				}
			}
			
			List<ObjectRemoved> removedObjectList  = (List<ObjectRemoved>) allPropsList.get(BaseConstants.REMOVED_PROP_LIST);
			if(removedObjectList != null && !removedObjectList.isEmpty()){
				for (int i = 0; i < removedObjectList.size(); i++) {
					ResponseObject responseObject = actionService.getActionById(Integer.parseInt(String.valueOf(removedObjectList.get(i).getAffectedLocalId())));
					if(responseObject.isSuccess()){
						Action action = (Action) responseObject.getObject();
						SystemAuditDetails auditDetails = new SystemAuditDetails(action.getName(),"Current action "+action.getName()+" removed.",  BaseConstants.NA);
						auditDetailsList.add(auditDetails);
					}
				}
			}
		}
		return auditDetailsList;
	}
	

	/**
	 * Method will get all system audit details by system audit id.
	 * @param systermAuditId
	 * @return List<SystemAuditDetails>
	 */
	@Override
	@Transactional
	public List<SystemAuditDetails> getAuditDetailsBySystemAuditId(int systermAuditId) {
		return auditDetailsDao.getAuditDetailsBySystemAuditId(systermAuditId);
	}

}
