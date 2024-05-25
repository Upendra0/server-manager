package com.elitecore.sm.nfv.service;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.nfv.model.NFVAddServer;
import com.elitecore.sm.nfv.model.NFVClient;
import com.elitecore.sm.nfv.model.NFVCopyServer;
import com.elitecore.sm.nfv.model.NFVImportServer;
import com.elitecore.sm.nfv.model.NFVLicense;
import com.elitecore.sm.nfv.model.NFVSyncServer;


public interface NFVService {
	
	public ResponseObject checkStatus(NFVAddServer serverModel);
	
	public ResponseObject deleteServer(NFVAddServer serverModel);
	
	public ResponseObject deleteServerInstance(NFVAddServer serverModel);
	
	public ResponseObject addServerInstance(NFVAddServer serverModel);
	
	public ResponseObject activateEngineFullLicense(NFVLicense license);
	
	public ResponseObject addServer(NFVAddServer serverModel);
	
	public ResponseObject syncServer(NFVSyncServer serverModel);
	
	public ResponseObject copyServer(NFVCopyServer serverModel, int staffId) throws SMException;
	
	public ResponseObject restartServer(NFVSyncServer serverModel);
	
	public ResponseObject startServer(NFVSyncServer serverModel);
	
	public ResponseObject stopServer(NFVSyncServer serverModel);
	
	public ResponseObject addClient(NFVClient client);
	
	public ResponseObject deleteServerInstanceOnlyInSM(NFVAddServer serverModel);

	public ResponseObject importServer(NFVImportServer serverModel, int staffId) throws SMException;
	
}
