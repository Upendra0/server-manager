/**
 * 
 */
package com.elitecore.sm.iam.dao;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
@Repository(value = "menuDAO")
public class MenuDAOImpl  extends GenericDAOImpl<BusinessModel> implements MenuDAO{
	
	/**
	 * Provides the complete business model hierarchy.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<BusinessModel> getFullModelHierarchy() {
		
		Criteria criteria = getCurrentSession().createCriteria(BusinessModel.class,"model");
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		
		criteria.createAlias("model.businessModuleList", "moduleList"); 
		
		criteria.createAlias("moduleList.subModuleList", "subModuleList"); 
		
		criteria.createAlias("subModuleList.actionList", "actionList");
		
		criteria.addOrder(Order.asc("model.id"));
		criteria.addOrder(Order.asc("moduleList.id"));
		criteria.addOrder(Order.asc("subModuleList.id"));
		criteria.addOrder(Order.asc("actionList.id"));
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		List<BusinessModel> modelList = criteria.list();
		logger.info("Model List Size" + modelList.size());
		
		for(BusinessModel model : modelList){
			for(BusinessModule module : model.getBusinessModuleList()){
				logger.info("\tTab List Size" + module.getSubModuleList().size());
				for(BusinessSubModule tab : module.getSubModuleList()){
					logger.info("\t\tAction List Size" + tab.getActionList().size());
					for(Action action : tab.getActionList()){
						action.getId();
					}
				}
			}
		}
		return modelList;
	}
	
	/**
	 * Provides the complete business model hierarchy.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<BusinessModel> getActiveModelHierarchy() {
		
		Criteria criteria = getCurrentSession().createCriteria(BusinessModel.class,"model");
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("model.id"));
		
		List<BusinessModel> modelList = criteria.list();
		
		if(modelList !=null && !modelList.isEmpty()){
			logger.info("Model List Size" + modelList.size());
			
			for(BusinessModel model : modelList){
				Criteria criteriaModule=getCurrentSession().createCriteria(BusinessModule.class);
				criteriaModule.add(Restrictions.eq("parentBusinessModel.id", model.getId()));
				criteriaModule.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
				List<BusinessModule> businessModuleList=criteriaModule.list();
				
				for(BusinessModule businessModule:businessModuleList){
					Criteria criteriaSubModule=getCurrentSession().createCriteria(BusinessSubModule.class);
					criteriaSubModule.add(Restrictions.eq("parentBusinessModule.id", businessModule.getId()));
					criteriaSubModule.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
					List<BusinessSubModule> businessSubModuleList=criteriaSubModule.list();
					
					for(BusinessSubModule businessSubModule:businessSubModuleList){
						Criteria criteriaAction=getCurrentSession().createCriteria(Action.class);
						criteriaAction.add(Restrictions.eq("parentBusinessSubModule.id", businessSubModule.getId()));
						criteriaAction.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
						List<Action> businessActionList=criteriaAction.list();
						
						businessSubModule.setActionList(businessActionList);
					}
					businessModule.setSubModuleList(new HashSet<BusinessSubModule>(businessSubModuleList));
				}
				model.setBusinessModuleList(new HashSet<BusinessModule>(businessModuleList));
			}
		}
		return modelList;
	}
	
	/**
	 * Adds action in database
	 */
	@Override
	public void saveAction(Action action) {
		getCurrentSession().save(action);
	}
	
	/**
	 * Update action in database
	 */
	@Override
	public void updateAction(Action action) {
		getCurrentSession().merge(action);
	}
	
	/**
	 * Adds business sub module in database.
	 */
	@Override
	public void saveBusinessSubModule(BusinessSubModule tab) {
		getCurrentSession().save(tab);
	}
	
	/**
	 * Update Business Sub module
	 */
	@Override
	public void updateBusinessSubModule(BusinessSubModule subModule) {
		getCurrentSession().merge(subModule);
	}
	
	/**
	 * Adds business module in database.
	 */
	@Override
	public void saveBusinessModule(BusinessModule businessModule){
		getCurrentSession().save(businessModule);
	}
	
	/**
	 * Update Business  module
	 */
	@Override
	public void updateBusinessModule(BusinessModule module) {
		getCurrentSession().merge(module);
	}
	
	/**
	 * Fetch All Action from database
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Action> getAllActions(){
		
		Criteria criteria=getCurrentSession().createCriteria(Action.class);
		
		return criteria.list();
		
	}
	
	/**
	 * Fetch all business sub module
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessSubModule> getAllBusinessSubModule(){
		
		Criteria criteria=getCurrentSession().createCriteria(BusinessSubModule.class);
		
		return criteria.list();
		
	}
	
	/**
	 * fetch all business model 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessModule> getAllBusinessModule(){
		
		Criteria criteria=getCurrentSession().createCriteria(BusinessModule.class);
		
		return criteria.list();
		
	}
}