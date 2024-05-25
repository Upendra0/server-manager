package com.elitecore.sm.roaming.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;
import com.elitecore.sm.roaming.model.FileManagementData;
import com.elitecore.sm.roaming.model.RoamingConfiguration;
import com.elitecore.sm.roaming.model.TestSimManagement;

public interface RoamingConfigurationService {

	public ResponseObject saveRoamingConfigurationDetails(RoamingConfiguration roamingConfiguration);
	public ResponseObject loadRoamingConfigurationDetails(String requestActionType);
	public List<RoamingFileSequenceMgmt> getFileSequenceDetails(int id);
	public List<TestSimManagement> getTestSimManagement(int id, String type);
	public List<TestSimManagement> getTestSimManagement(int id);
	public List<FileManagementData> getFileManagementData(int id, String servicetype);
	public List<FileManagementData> getFileManagementData(int id);
	public ResponseObject saveFileSequence(RoamingFileSequenceMgmt missingFileSequenceMgmt);
	public ResponseObject saveOrUpdateTestSimManagementDetails(TestSimManagement testSimManagementDb);
	public ResponseObject saveOrUpdateFileManagementDetails(FileManagementData fileManagementData);

}
