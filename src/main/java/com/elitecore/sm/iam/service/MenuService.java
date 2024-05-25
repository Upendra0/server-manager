package com.elitecore.sm.iam.service;

import java.util.List;

import com.elitecore.sm.iam.dao.MenuDAO;
import com.elitecore.sm.iam.model.BusinessModel;

/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
public interface MenuService {
	public List<BusinessModel> getFullModelHierarchy();
	
	public void addBusinessModel(BusinessModel businessModel);
	
	public List<BusinessModel> getActiveModelHierarchy();
	
	public MenuDAO getMenuDAO();
	
}