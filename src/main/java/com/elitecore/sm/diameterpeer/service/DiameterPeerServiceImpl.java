package com.elitecore.sm.diameterpeer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.diameterpeer.dao.DiameterAVPDao;
import com.elitecore.sm.diameterpeer.dao.DiameterPeerDao;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.diameterpeer.validator.DiameterPeerValidator;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;


@org.springframework.stereotype.Service(value = "diameterPeerService")
public class DiameterPeerServiceImpl implements DiameterPeerService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	DiameterPeerDao peerDao;
	
	@Autowired
	DiameterAVPDao avpDao;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ServicesDao serviceDao;
	
	@Autowired
	DiameterPeerValidator diameterPeerValidator;
	
	@Override
	@Transactional()
	@Auditable(auditActivity = AuditConstants.CREATE_CLIENT,actionType = BaseConstants.CREATE_ACTION, currentEntity = DiameterPeer.class, ignorePropList= "")
	public ResponseObject addCollectionPeer(DiameterPeer peer){
	
		ResponseObject responseObject = new ResponseObject();

		int peerCount = peerDao.getPeerCount(peer.getName(),getServiceId(peer.getService()));
		if (peerCount > 0) {
			//logger.debug("inside addCollectionPeer : duplicate peer name found:" + peer.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PEER_NAME);
		} else {
			Service service = serviceDao.findByPrimaryKey(Service.class,peer.getService().getId());
			if (service != null) {
				peer.setService((DiameterCollectionService)service);
				
				peerDao.save(peer);
				
				if(peer.getId() !=0){
					responseObject.setSuccess(true);
					responseObject.setObject(peer);
					responseObject.setResponseCode(ResponseCode.PEER_ADD_SUCCESS);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PEER_ADD_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.ADD_CLIENT_FAIL);
				}
			} else {
				// add peer fail
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PEER_ADD_FAIL_SERV_UNAVAIL);
			}
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getPeerListForService(int serviceId) {
			ResponseObject responseobject = new ResponseObject();
			JSONArray jPeerList = new JSONArray();
			JSONObject jPeerDetail ;
			JSONArray jWrapperList ;
			List<DiameterPeer> peerList = peerDao.getPeerListForService(serviceId);
			
			if (peerList != null && !peerList.isEmpty()){
				for(DiameterPeer peer : peerList) {
					jPeerDetail = new JSONObject();
					jPeerDetail.put("id", peer.getId());
					jPeerDetail.put("status", peer.getStatus());
					jPeerDetail.put("name", peer.getName());
					jPeerDetail.put("identity", peer.getIdentity());
					jPeerDetail.put("realmName", (peer.getRealmName() != null && peer.getRealmName() != "" ) ? peer.getRealmName() : "" );
					jPeerDetail.put("watchDogInterval", peer.getWatchDogInterval() );
					jPeerDetail.put("requestTimeOut", peer.getRequestTimeOut());
					jPeerDetail.put("fileNameFormat", peer.getFileNameFormat());
					jPeerDetail.put("outFileLocation", peer.getOutFileLocation() );
					jPeerDetail.put("fileSeqEnable", peer.isFileSeqEnable() );
					jPeerDetail.put("minFileSeq", peer.getMinFileSeq() );
					jPeerDetail.put("maxFileSeq", peer.getMaxFileSeq() );
					jPeerDetail.put("logRollingUnitTime", peer.getLogRollingUnitTime());
					jPeerDetail.put("logRollingUnitVol", peer.getLogRollingUnitVol());
					jPeerDetail.put("inputCompressed", peer.isInputCompressed());
					jPeerDetail.put("appendpaddinginfileseq", peer.isAppendFilePaddingInFileName());
					
					jWrapperList = new JSONArray();
					List<DiameterAVP> avpList = peer.getDiameterAVPs();
					if (avpList != null && !avpList.isEmpty())
					{
						for (DiameterAVP avp : avpList)
						{
							if (!avp.getStatus().equals(StateEnum.DELETED))
							{
								JSONObject finalavp = new JSONObject();
								finalavp.put("id", avp.getId());
								finalavp.put("vendorId", avp.getVendorId());
								finalavp.put("attributeId", avp.getAttributeId());
								finalavp.put("attributeValue", avp.getValue());
								jWrapperList.put(finalavp);
							}
						}
						jPeerDetail.put("avpWrapper", jWrapperList);
					} else {
						jPeerDetail.put("avpWrapper", new JSONArray());
					}
					jPeerList.put(jPeerDetail);
				}
			}
			responseobject.setSuccess(true);
			responseobject.setObject(jPeerList);
			/*logger.debug("Final JSON Response = "+jPeerList);*/
			return responseobject;
	}	
	
	public int getServiceId(Service service) {
		if (service != null) {
			return service.getId();
		}
		return 0;
	}
		
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_CLIENT,actionType = BaseConstants.UPDATE_ACTION, currentEntity = DiameterPeer.class, ignorePropList= "")
	public ResponseObject updateDiameterPeer(DiameterPeer peer) {
		ResponseObject responseObject = new ResponseObject();

		if (isPeerUniqueForUpdate(peer.getId(), peer.getName(), getServiceId(peer.getService()))) {
			
			DiameterPeer peerNew = peerDao.getDiameterPeerById(peer.getId());
			logger.debug("Service Id  : "+ peer.getService().getId());
			
			peerNew.setStatus(peer.getStatus());
			peerNew.setName(peer.getName());
			peerNew.setIdentity(peer.getIdentity());
			peerNew.setRealmName(peer.getRealmName());
			peerNew.setWatchDogInterval(peer.getWatchDogInterval());
			peerNew.setRequestTimeOut(peer.getRequestTimeOut());
			peerNew.setFileNameFormat(peer.getFileNameFormat());
			peerNew.setOutFileLocation(peer.getOutFileLocation());
			peerNew.setFileSeqEnable(peer.isFileSeqEnable());
			peerNew.setMinFileSeq(peer.getMinFileSeq());
			peerNew.setMaxFileSeq(peer.getMaxFileSeq());
			peerNew.setLogRollingUnitTime(peer.getLogRollingUnitTime());
			peerNew.setLogRollingUnitVol(peer.getLogRollingUnitVol());
			peerNew.setInputCompressed(peer.isInputCompressed());
			peerNew.setLastUpdatedByStaffId(peer.getLastUpdatedByStaffId());
			peerNew.setLastUpdatedDate(peer.getLastUpdatedDate());
			peerNew.setAppendFilePaddingInFileName(peer.isAppendFilePaddingInFileName());
						
			peerDao.merge(peerNew);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DIAMETER_PEER_UPDATE_SUCCESS);
			responseObject.setObject(peerNew);
			
		} else {
			//logger.debug("inside updateDiameterPeer : duplicate peer name found in update:" + peer.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DIAMETER_PEER_NAME);
		}
		return responseObject;
	}
	
	
	/**
	 * Method will delete Diameter Peer detail from database
	 * @param peerId
	 * @return Delete diameter peer response as response object
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CLIENT,actionType = BaseConstants.DELETE_ACTION, currentEntity = DiameterPeer.class, ignorePropList= "")
	public ResponseObject deleteDiameterPeer(int peerId) {
		ResponseObject responseObject = new ResponseObject();
		
		DiameterPeer peer = peerDao.getDiameterPeerById(peerId);
		if (peer != null) {
			if(!peer.getStatus().equals(StateEnum.ACTIVE)){
				peer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,peer.getName()));
				peer.setStatus(StateEnum.DELETED);
				
				List<DiameterAVP> avpList = peer.getDiameterAVPs();
				if (avpList != null)
				{
					int size = avpList.size();
					for (int i = 0; i < size ; i++)
					{
						logger.debug("Deleting Avp also  " + avpList.get(i).getId());
						DiameterAVP avp = avpDao.findByPrimaryKey(DiameterAVP.class, avpList.get(i).getId());
						avp.setValue(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, avp.getValue()));
						avp.setStatus(StateEnum.DELETED);
					}
				}

				peerDao.merge(peer);
				
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PEER_DELETE_SUCCESS);
				responseObject.setObject(peer);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PEER_ACTIVE_DELETE_FAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PEER_ID_UNAVALIABLE);
		}
		
		return responseObject;
	}
	
	/**
	 * Check Peer name is unique in case of update 
	 * @param peerId
	 * @param peerName
	 * @return
	 */
	private boolean  isPeerUniqueForUpdate(int peerId,String peerName,int serviceId){
		
		List<DiameterPeer> peerList= peerDao.getPeerListByName(peerName,serviceId);
		boolean isUnique=false;
		
		if(peerList!=null && !peerList.isEmpty()){
			for(DiameterPeer peer:peerList){
				//If ID is same, then it is same peer object
				if(peerId == (peer.getId())){
					isUnique=true;
				}else{ // It is another peer object , but name is same
					isUnique=false;
				}
			}
		}else if(peerList!=null && peerList.isEmpty()){ // No peer found with same name 
			isUnique=true;
		}
		return isUnique;
	}
	
	/**
	 * @param serviceId
	 * @return Total Diameter peer count for service
	 */
	@Override
	@Transactional
	public long getTotalPeerCount(int serviceId) {
		
		HashMap<String, String> aliases = new HashMap<>();
		
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		aliases.put("service", "s");
		conditions.add(Restrictions.eq("s.id",serviceId));
		
		return peerDao.getQueryCount(DiameterPeer.class,conditions,aliases);
	}

	/**
	 * Get Paginated Diameter peer list
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @param serviceId
	 * @return List of paginated diameter peer
	 */
	@Override
	@Transactional
	public List<DiameterPeer> getPaginatedList(int startIndex, int limit, String sidx,String sord,int serviceId) {
		
		HashMap<String, String> aliases = new HashMap<>();
		
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		
		aliases.put("service", "s");
		conditions.add(Restrictions.eq("s.id",serviceId));

		List<DiameterPeer> clientList = peerDao.getPaginatedList(DiameterPeer.class, conditions, aliases, startIndex,
													limit, sidx, sord);

		return clientList;
	}
	
	/**
	 * update diameter peer status in database
	 * @param peerId
	 * @param status
	 * @return Update diameter peer status response as response object
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_CLIENT_STATUS,actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = DiameterPeer.class, ignorePropList= "")
	public ResponseObject updatePeerStatus(int peerId,String status){
		ResponseObject responseObject = new ResponseObject();
		
		DiameterPeer peer = peerDao.getDiameterPeerById(peerId);
		
		if(peer != null){
			if((StateEnum.ACTIVE.name().equals(status)))
			{
				peer.setStatus(StateEnum.ACTIVE);
			}
			else
			{
				peer.setStatus(StateEnum.INACTIVE);
			}
			
			peerDao.merge(peer);
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DIAMETER_PEER_UPDATE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PEER_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	@Override
	public DiameterPeer getPeerFromList(List<DiameterPeer> peerList, String peerName) {
		if(!CollectionUtils.isEmpty(peerList)) {
			int length = peerList.size();
			for(int i = length-1; i >= 0; i--) {
				DiameterPeer diameterPeer = peerList.get(i);
				if(diameterPeer != null && !diameterPeer.getStatus().equals(StateEnum.DELETED) && diameterPeer.getName().equalsIgnoreCase(peerName)) {
					return peerList.remove(i);
				}
			}
		}
		return null;
	}

	@Override
	public void importDiameterPeerForAddAndKeepBothMode(DiameterPeer diameterPeer, DiameterCollectionService service,
			int importMode) {
		
		diameterPeer.setId(0);
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			diameterPeer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,diameterPeer.getName()));
		}
		diameterPeer.setCreatedByStaffId(service.getLastUpdatedByStaffId());
		diameterPeer.setCreatedDate(EliteUtils.getDateForImport(false));
		diameterPeer.setService(service);
		List<DiameterAVP> avps = importDiameterAVPForAddAndKeepBothMode(diameterPeer, importMode);
		diameterPeer.setDiameterAVPs(avps);
	}

	public List<DiameterAVP> importDiameterAVPForAddAndKeepBothMode(DiameterPeer diameterPeer, int importMode){
		List<DiameterAVP> avps = diameterPeer.getDiameterAVPs();
		
		for(int i=0;i<avps.size();i++){
			DiameterAVP avp = avps.get(i);
			avp.setId(0);
			
			avp.setCreatedByStaffId(diameterPeer.getLastUpdatedByStaffId());
			avp.setCreatedDate(EliteUtils.getDateForImport(false));
			avp.setPeer(diameterPeer);
		}
		return avps;
		
	}
	
	@Override
	public void importDiameterPeerForUpdateMode(DiameterPeer dbDiameterPeer, DiameterPeer exportedDiamterPeer, int importMode) {
		
		logger.debug("going to add/update diameter peer in service : "+dbDiameterPeer.getName());
		
		List<DiameterAVP> exportedDiamterAvp = exportedDiamterPeer.getDiameterAVPs();
		List<DiameterAVP> dbDiameterAvp = dbDiameterPeer.getDiameterAVPs();
		if(importMode != BaseConstants.IMPORT_MODE_ADD){
			dbDiameterPeer.setName(exportedDiamterPeer.getName());
			dbDiameterPeer.setIdentity(exportedDiamterPeer.getIdentity());
			dbDiameterPeer.setRealmName(exportedDiamterPeer.getRealmName());
			dbDiameterPeer.setWatchDogInterval(exportedDiamterPeer.getWatchDogInterval());
			dbDiameterPeer.setRequestTimeOut(exportedDiamterPeer.getRequestTimeOut());
			dbDiameterPeer.setFileNameFormat(exportedDiamterPeer.getFileNameFormat());
			dbDiameterPeer.setOutFileLocation(exportedDiamterPeer.getOutFileLocation());
			dbDiameterPeer.setFileSeqEnable(exportedDiamterPeer.isFileSeqEnable());
			dbDiameterPeer.setMinFileSeq(exportedDiamterPeer.getMinFileSeq());
			dbDiameterPeer.setMaxFileSeq(exportedDiamterPeer.getMaxFileSeq());
			dbDiameterPeer.setLogRollingUnitVol(exportedDiamterPeer.getLogRollingUnitVol());
			dbDiameterPeer.setLogRollingUnitTime(exportedDiamterPeer.getLogRollingUnitVol());
			dbDiameterPeer.setInputCompressed(exportedDiamterPeer.isInputCompressed());
			dbDiameterPeer.setAppendFilePaddingInFileName(exportedDiamterPeer.isAppendFilePaddingInFileName());
		}
		importDiameterAvpForUpdateMode(dbDiameterAvp, exportedDiamterAvp,dbDiameterPeer,importMode);
		dbDiameterPeer.setDiameterAVPs(dbDiameterAvp);
	}
	
	@Override
	public void importDiameterPeerForAddMode(DiameterPeer dbDiameterPeer, DiameterPeer exportedDiamterPeer, int importMode) {
		
		logger.debug("going to add/update diameter peer in service : "+dbDiameterPeer.getName());
		
		List<DiameterAVP> exportedDiamterAvp = exportedDiamterPeer.getDiameterAVPs();
		List<DiameterAVP> dbDiameterAvp = dbDiameterPeer.getDiameterAVPs();
						
		importDiameterAvpForAddMode(dbDiameterAvp, exportedDiamterAvp,dbDiameterPeer);
		dbDiameterPeer.setDiameterAVPs(dbDiameterAvp);
	}

	public void importDiameterAvpForUpdateMode(List<DiameterAVP> dbDiameterAvps, List<DiameterAVP> exportedDiamterAvps, DiameterPeer peer, int importMode) {
		if (!CollectionUtils.isEmpty(exportedDiamterAvps)) {
			int length = exportedDiamterAvps.size();
			for (int i = length - 1; i >= 0; i--) {
				DiameterAVP exportedDiameterAvp = exportedDiamterAvps.get(i);
				if (exportedDiameterAvp != null) {
					DiameterAVP dbDiameterAvp = getDiameterAVPFromAVPList(dbDiameterAvps, exportedDiameterAvp);
					if (dbDiameterAvp == null) {
						dbDiameterAvp = new DiameterAVP();
						dbDiameterAvp.setId(0);
						dbDiameterAvp.setVendorId(exportedDiameterAvp.getVendorId());
						dbDiameterAvp.setAttributeId(exportedDiameterAvp.getAttributeId());
						dbDiameterAvp.setValue(exportedDiameterAvp.getValue());
						dbDiameterAvp.setStatus(exportedDiameterAvp.getStatus());
						dbDiameterAvp.setPeer(peer);
						dbDiameterAvps.add(dbDiameterAvp);
					}
					else if(importMode != BaseConstants.IMPORT_MODE_ADD){
						dbDiameterAvp.setValue(exportedDiameterAvp.getValue());
					}
				}
			}
		}
	}
	
	public void importDiameterAvpForAddMode(List<DiameterAVP> dbDiameterAvps, List<DiameterAVP> exportedDiamterAvps, DiameterPeer peer) {
		if (!CollectionUtils.isEmpty(exportedDiamterAvps)) {
			int length = exportedDiamterAvps.size();
			for (int i = length - 1; i >= 0; i--) {
				DiameterAVP exportedDiameterAvp = exportedDiamterAvps.get(i);
				if (exportedDiameterAvp != null) {
					DiameterAVP dbDiameterAvp = getDiameterAVPFromAVPList(dbDiameterAvps, exportedDiameterAvp);
					if (dbDiameterAvp == null) {
						dbDiameterAvp = new DiameterAVP();
						dbDiameterAvp.setId(0);
						dbDiameterAvp.setVendorId(exportedDiameterAvp.getVendorId());
						dbDiameterAvp.setAttributeId(exportedDiameterAvp.getAttributeId());
						dbDiameterAvp.setValue(exportedDiameterAvp.getValue());
						dbDiameterAvp.setStatus(exportedDiameterAvp.getStatus());
						dbDiameterAvp.setPeer(peer);
						dbDiameterAvps.add(dbDiameterAvp);
					}					
				}
			}
		}
	}
	
	public DiameterAVP getDiameterAVPFromAVPList(List<DiameterAVP> dbDiameterAvps, DiameterAVP exportedDiameterAvp){
		if(!CollectionUtils.isEmpty(dbDiameterAvps)) {
			int length = dbDiameterAvps.size();
			for(int i = length-1; i >= 0; i--) {
				DiameterAVP diAvp = dbDiameterAvps.get(i);
				if(diAvp != null && !diAvp.getStatus().equals(StateEnum.DELETED) && (diAvp.getVendorId()==exportedDiameterAvp.getVendorId()) && (diAvp.getAttributeId()==exportedDiameterAvp.getAttributeId())) {
					return dbDiameterAvps.remove(i);
				}
			}
		}
		return null;
	}
	
	@Override
	public void importServicePeerAddAndKeepBothMode(DiameterCollectionService exportedService, int importMode) {
		if(exportedService != null ){
			List<DiameterPeer> exportedPeerList = exportedService.getDiameterPeerList();
			if (!CollectionUtils.isEmpty(exportedPeerList)) {
				int clientLength = exportedPeerList.size();
				for(int i = clientLength-1; i >= 0; i--) {
					DiameterPeer exportedPeer = exportedPeerList.get(i);
					if(exportedPeer != null && !StateEnum.DELETED.equals(exportedPeer.getStatus())) {
						importDiameterPeerForAddAndKeepBothMode(exportedPeer, exportedService, importMode);
					}
				}
			}
		}
		
	}

	@Override
	public void importServicePeerUpdateMode(DiameterCollectionService dbService, DiameterCollectionService exportedService, int importMode) {
		if(exportedService != null ){
			List<DiameterPeer> dbDiameterPeerList = dbService.getDiameterPeerList();
			List<DiameterPeer> exportedDiameterPeerList = exportedService.getDiameterPeerList();
			if (exportedDiameterPeerList != null && !exportedDiameterPeerList.isEmpty()) {
				int clientLength = exportedDiameterPeerList.size();
				for(int i = clientLength-1; i >= 0; i--) {
					DiameterPeer exportedDiameterPeer = exportedDiameterPeerList.get(i);
					if(exportedDiameterPeer != null) {
						DiameterPeer dbDiameterPeer = getPeerFromList(dbDiameterPeerList, exportedDiameterPeer.getName());
						if(dbDiameterPeer != null) {
							//update diameter peer							
							if(importMode==BaseConstants.IMPORT_MODE_ADD) {
								importDiameterPeerForAddMode(dbDiameterPeer, exportedDiameterPeer, importMode);
							}else {
								importDiameterPeerForUpdateMode(dbDiameterPeer, exportedDiameterPeer, importMode);
							}								
							dbDiameterPeerList.add(dbDiameterPeer);
						} else {
							//add diameter peer
							importDiameterPeerForAddAndKeepBothMode(exportedDiameterPeer, dbService, importMode);
							dbDiameterPeerList.add(exportedDiameterPeer);
						}
						
					}
				}
			}
		}
	}
	
	@Override
	public void iterateServicePeerDetails(DiameterCollectionService service, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();
		
		if(service != null ){
			List<DiameterPeer> diameterPeers = service.getDiameterPeerList();
			
			if (diameterPeers != null && !diameterPeers.isEmpty()) {
				DiameterPeer diameterPeer ;
				for (int i = 0, size = diameterPeers.size(); i < size; i++) {
					diameterPeer = diameterPeers.get(i);
					if(isImport){
						diameterPeer.setId(0);
						diameterPeer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,diameterPeer.getName()));
						diameterPeer.setCreatedByStaffId(service.getLastUpdatedByStaffId());
						diameterPeer.setCreatedDate(new Date());
						diameterPeer.setService(service);
					}else{
						diameterPeer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,diameterPeer.getName()));
						diameterPeer.setStatus(StateEnum.DELETED);
						
						List<DiameterAVP> avpList = diameterPeer.getDiameterAVPs();
						if (avpList != null && !avpList.isEmpty())
						{
							for (int j = 0 ; j < avpList.size() ; j ++)
							{
								DiameterAVP avp = avpList.get(j);
								avp.setValue(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, avp.getValue()));
								avp.setStatus(StateEnum.DELETED);
							}
						}
					}
						
					diameterPeer.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
					diameterPeer.setLastUpdatedDate(new Date());
						
					responseObject.setSuccess(true);
					responseObject.setObject(diameterPeer);
				}
			}else{
				logger.debug("Client not configured for service " + service.getId());
				responseObject.setSuccess(true);
				responseObject.setObject(service);
			}
		}
	}
	
	/**
	 * Validate diameter peer parameter for import operation
	 * @param diameterPeer
	 * @param peerImportErrorList
	 * @return
	 */
	@Override
	public List<ImportValidationErrors> validateDiameterPeerForImport(DiameterPeer diameterPeer,List<ImportValidationErrors> peerImportErrorList){
		
		logger.debug("Validate Diameter Peer");
		diameterPeerValidator.validateDiameterPeer(diameterPeer, null, null, peerImportErrorList, BaseConstants.DIAMETER_PEER, true);
		return peerImportErrorList;
	}

	
}
