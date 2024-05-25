/**
 * 
 */
package com.elitecore.sm.iam.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.iam.dao.MenuDAO;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
@Service(value = "menuService")
public class MenuServiceImpl implements MenuService{
	@Autowired
	private MenuDAO menuDAO;
	
	/**
	 * Add Business Model in database.
	 */
	@Override
	@Transactional
	public void addBusinessModel(BusinessModel businessModel){
		
		this.menuDAO.save(businessModel);
		
		for(BusinessModule businessModule : businessModel.getBusinessModuleList()){
			this.menuDAO.saveBusinessModule(businessModule);
			
			Set<BusinessSubModule> subModulesSet = businessModule.getSubModuleList();
			for(BusinessSubModule subModule : subModulesSet){
				
				this.menuDAO.saveBusinessSubModule(subModule);

				for(Action action : subModule.getActionList()){
					
					this.menuDAO.saveAction(action);
				}
			}
		}
	}

	/**
	 * Get full business model hierarchy.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<BusinessModel> getFullModelHierarchy() {
		return this.menuDAO.getFullModelHierarchy();
	}
	
	/**
	 * Get active business model hierarchy.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<BusinessModel> getActiveModelHierarchy() {
		return this.menuDAO.getActiveModelHierarchy();
	}
	
	
	public MenuServiceImpl() {}
	
	public MenuServiceImpl(MenuDAO menuDao) {
		this.menuDAO = menuDao;
	}
	
	public MenuDAO getMenuDAO() {
		return menuDAO;
	}
	
	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}
}