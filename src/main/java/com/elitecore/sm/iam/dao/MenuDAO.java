/**
 * 
 */
package com.elitecore.sm.iam.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
public interface MenuDAO extends GenericDAO<BusinessModel> {
	public List<BusinessModel> getFullModelHierarchy();
	
	public void saveBusinessModule(BusinessModule businessModule);
	
	public void saveBusinessSubModule(BusinessSubModule tab);
	
	public void saveAction(Action action);
	
	public List<BusinessModel> getActiveModelHierarchy();
	
	public List<Action> getAllActions();
	
	public void updateAction(Action action) ;
	
	public void updateBusinessSubModule(BusinessSubModule subModule);
	
	public void updateBusinessModule(BusinessModule module);
	
	public List<BusinessSubModule> getAllBusinessSubModule();
	
	public List<BusinessModule> getAllBusinessModule();
	
}
