package com.elitecore.sm.roaming.dao;



import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;
import com.elitecore.sm.roaming.model.FileManagementData;
import com.elitecore.sm.roaming.model.RoamingConfiguration;
import com.elitecore.sm.roaming.model.TestSimManagement;

public interface RoamingConfigurationDao extends GenericDAO<RoamingConfiguration> {

	public RoamingConfiguration loadData(String requestActionType);
	
	public void merge(RoamingConfiguration roamingConfiguration);

	public void merge(RoamingFileSequenceMgmt missingFileSequenceMgmt);
		
	//public List<MissingFileSequenceMgmt> loadFileSequenceDetails(String partner, String lob);

	public List<RoamingFileSequenceMgmt> loadFileSequenceDetails(int id);

	public void merge(TestSimManagement testSimManagement);

	public List<TestSimManagement> getInboudOutboundTestSimData(int id, String type);

	public List<TestSimManagement> getInboudOutboundTestSimData(int id);

	public List<FileManagementData> getTestOrCommercialFileManagementData(int id, String servicetype);

	public void merge(FileManagementData fileManagementData);

	public List<FileManagementData> getTestOrCommercialFileManagementData(int id);
	
	

}
