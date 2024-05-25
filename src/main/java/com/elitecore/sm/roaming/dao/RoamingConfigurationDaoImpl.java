package com.elitecore.sm.roaming.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;
import com.elitecore.sm.roaming.model.FileManagementData;
import com.elitecore.sm.roaming.model.HostConfiguration;
import com.elitecore.sm.roaming.model.RoamingConfiguration;
import com.elitecore.sm.roaming.model.RoamingParameter;
import com.elitecore.sm.roaming.model.TestSimManagement;

@Repository(value = "roamingConfigurationDao")
public class RoamingConfigurationDaoImpl extends GenericDAOImpl<RoamingConfiguration> implements RoamingConfigurationDao{
	
	
	@Override
	public void merge(RoamingConfiguration roamingConfiguration){
		if(roamingConfiguration instanceof HostConfiguration){
			HostConfiguration hostConfiguration = (HostConfiguration)roamingConfiguration;
			getCurrentSession().merge(hostConfiguration);
			getCurrentSession().flush();
		}else if (roamingConfiguration instanceof RoamingParameter) {
			RoamingParameter roamingParameter = (RoamingParameter)roamingConfiguration;
			getCurrentSession().merge(roamingParameter);
			//getCurrentSession().flush();
		}
	
	}
	@Transactional
	public RoamingConfiguration loadData(String requestActionType) {
		RoamingConfiguration roamingConfiguration2 = null;
		switch (requestActionType) {
		case BaseConstants.HOST_CONFIGURATION:
			try{
			roamingConfiguration2 =  (RoamingConfiguration)getCurrentSession().createCriteria(HostConfiguration.class, "hostConfg").list().get(0);
			}
			catch (Exception e) {//NOSONAR
				roamingConfiguration2 = new HostConfiguration();
			}
			break;
		case BaseConstants.ROAMING_PARAMETER:
			try{
			roamingConfiguration2 =  (RoamingConfiguration)getCurrentSession().createCriteria(RoamingParameter.class, "roamingParameter").list().get(0);
			}
			catch (Exception e) {//NOSONAR
				roamingConfiguration2 = new RoamingParameter();
			}
			break;
		default:
			logger.info("Default switch case condition match"); // It will never match this condition.	
		}
	return roamingConfiguration2;
	}
	@Override
	public void merge(RoamingFileSequenceMgmt missingFileSequenceMgmt) {
		getCurrentSession().merge(missingFileSequenceMgmt);
	}
	
	@Override
	@Transactional
	public List<RoamingFileSequenceMgmt> loadFileSequenceDetails(int id) {
		Criteria createCriteria = getCurrentSession().createCriteria(RoamingFileSequenceMgmt.class);
		//createCriteria.add(Restrictions.eq("referenceDevice", partner));
		createCriteria.add(Restrictions.eq(BaseConstants.PARTNER_ID,id));
		
		
		return createCriteria.list();
	}
	@Override
	@Transactional
	public void merge(TestSimManagement testSimManagement) {
		getCurrentSession().merge(testSimManagement);
		
	}
	@Override
	@Transactional
	public List<TestSimManagement> getInboudOutboundTestSimData(int id,String type) {
		Criteria createCriteria = getCurrentSession().createCriteria(TestSimManagement.class);
		createCriteria.add(Restrictions.eq(BaseConstants.PARTNER_ID,id));
		createCriteria.add(Restrictions.eq(BaseConstants.SUBSCRIBER_TYPE, type).ignoreCase());
		return createCriteria.list();
	}
	@Override
	@Transactional
	public List<TestSimManagement> getInboudOutboundTestSimData(int id) {
		Criteria createCriteria = getCurrentSession().createCriteria(TestSimManagement.class);
		createCriteria.add(Restrictions.eq(BaseConstants.PARTNER_ID, id));
		return createCriteria.list();
	}
	@Override
	@Transactional
	public List<FileManagementData> getTestOrCommercialFileManagementData(int id, String serviceType) {
		Criteria createCriteria = getCurrentSession().createCriteria(FileManagementData.class);
		createCriteria.add(Restrictions.eq(BaseConstants.PARTNER_ID,id));
		createCriteria.add(Restrictions.eq(BaseConstants.SERVICE_TYPE, serviceType).ignoreCase());
		return createCriteria.list();
	}
	@Override
	@Transactional
	public void merge(FileManagementData fileManagementData) {
		getCurrentSession().merge(fileManagementData);
		
	}
	@Override
	@Transactional
	public List<FileManagementData> getTestOrCommercialFileManagementData(int id) {
		Criteria createCriteria = getCurrentSession().createCriteria(FileManagementData.class);
		createCriteria.add(Restrictions.eq(BaseConstants.PARTNER_ID,id));
		return createCriteria.list();
	}

}
