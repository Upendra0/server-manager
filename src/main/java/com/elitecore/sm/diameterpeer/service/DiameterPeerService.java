package com.elitecore.sm.diameterpeer.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.services.model.DiameterCollectionService;


public interface DiameterPeerService {
	
	public long getTotalPeerCount(int serviceId);
	
	public List<DiameterPeer> getPaginatedList(int startIndex, int limit, String sidx, String sord,int serviceId);
	
	public ResponseObject addCollectionPeer(DiameterPeer peer);
	
	public ResponseObject getPeerListForService(int serviceId);

	ResponseObject updateDiameterPeer(DiameterPeer diameterPeer);
	
	public ResponseObject deleteDiameterPeer(int peerId);
	
	public ResponseObject updatePeerStatus(int peerId,String status);
	
	public DiameterPeer getPeerFromList(List<DiameterPeer> peerList, String peerName);
	
	public void importDiameterPeerForAddAndKeepBothMode(DiameterPeer diameterPeer, DiameterCollectionService service, int importMode);

	public void importDiameterPeerForUpdateMode(DiameterPeer dbDiameterPeer, DiameterPeer exportedDiamterPeer, int importMode);
	
	public void  iterateServicePeerDetails(DiameterCollectionService service,boolean isImport);
	
	public void importServicePeerUpdateMode(DiameterCollectionService dbService, DiameterCollectionService exportedService, int importMode);

	public List<ImportValidationErrors> validateDiameterPeerForImport(DiameterPeer diameterPeer,List<ImportValidationErrors> peerImportErrorList);

	public void importServicePeerAddAndKeepBothMode(DiameterCollectionService exportedService, int importMode);
	
	public void importDiameterPeerForAddMode(DiameterPeer dbDiameterPeer, DiameterPeer exportedDiamterPeer, int importMode);
	
}
