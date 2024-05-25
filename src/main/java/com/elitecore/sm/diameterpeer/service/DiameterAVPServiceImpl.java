package com.elitecore.sm.diameterpeer.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.diameterpeer.dao.DiameterAVPDao;
import com.elitecore.sm.diameterpeer.dao.DiameterPeerDao;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.service.ServicesService;


@org.springframework.stereotype.Service(value = "diameterAVPService")
public class DiameterAVPServiceImpl implements DiameterAVPService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	DiameterAVPDao avpDao;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ServicesDao serviceDao;
	
	@Autowired
	DiameterPeerDao peerDao;
	
	/**
	 * Add avp in database
	 * @param diameterAVP
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_CLIENT, actionType = BaseConstants.CREATE_ACTION, currentEntity = DiameterAVP.class, ignorePropList= "")
	public ResponseObject createAVP(DiameterAVP diameterAVP) {

		ResponseObject responseObject = new ResponseObject();
		
		int avpCount = avpDao.getAVPCount(diameterAVP.getAttributeId(), diameterAVP.getVendorId(), diameterAVP.getPeer().getId());
		if (avpCount > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_AVP);
		}
		else {
			DiameterPeer peer = (DiameterPeer) peerDao.findByPrimaryKey(DiameterPeer.class, diameterAVP.getPeer().getId());
			
			if (peer != null) {
				diameterAVP.setPeer(peer);
				avpDao.save(diameterAVP);
				
				if(diameterAVP.getId() !=0){
					responseObject.setSuccess(true);
					responseObject.setObject(diameterAVP);
					responseObject.setResponseCode(ResponseCode.AVP_ADD_SUCCESS);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.AVP_ADD_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.ADD_CLIENT_FAIL);
				}
			} else {
				// add AVP fail
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.AVP_ADD_FAIL_PEER_UNAVAIL);
			}
		}
		
		return responseObject;
	}
	
	/**
	 * Update AVP detail in database
	 * @param diameterAVP
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_CLIENT, actionType = BaseConstants.UPDATE_ACTION, currentEntity = DiameterAVP.class, ignorePropList= "")
	public ResponseObject updateAVP(DiameterAVP diameterAVP) {
		
		ResponseObject responseObject = new ResponseObject();
		
		if (isAVPUniqueForUpdate(diameterAVP.getAttributeId(),diameterAVP.getVendorId(),diameterAVP.getPeer().getId(),diameterAVP.getId())) {
			DiameterPeer peer = (DiameterPeer) peerDao.findByPrimaryKey(DiameterPeer.class, diameterAVP.getPeer().getId());
			
			if (peer != null) {
				diameterAVP.setPeer(peer);
				avpDao.merge(diameterAVP);

				responseObject.setSuccess(true);
				responseObject.setObject(diameterAVP);
				responseObject.setResponseCode(ResponseCode.DIAMETER_AVP_UPDATE_SUCCESS);
		
			} else {
				// add AVP fail
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.AVP_ADD_FAIL_PEER_UNAVAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_AVP);
		}
		
		
		return responseObject;
	}
	
	/**
	 * Delete AVP from database
	 * @param avpIdList
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject deleteAVPList(String avpIdList) {
		ResponseObject responseObject = new ResponseObject();
		String[] idArray = avpIdList.split(",");
		
		DiameterAVPService avpServiceImpl = (DiameterAVPService) SpringApplicationContext.getBean("diameterAVPService");
		for (int index = 0; index < idArray.length; index++) {
			avpServiceImpl.deleteAVP(Integer.parseInt(idArray[index]));
		}
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.AVP_DELETE_SUCCESS);

		return responseObject;
	}

	
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CLIENT,actionType = BaseConstants.DELETE_ACTION , currentEntity = DiameterAVP.class, ignorePropList= "")
	public ResponseObject deleteAVP(int avpId) {
		ResponseObject responseObject = new ResponseObject();
		DiameterAVP avp = avpDao.findByPrimaryKey(DiameterAVP.class, avpId);
		if (avp != null) {
			avp.setStatus(StateEnum.DELETED);
			avp.setLastUpdatedDate(new Date());
			avpDao.merge(avp);
		}
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.AVP_DELETE_SUCCESS);
		return  responseObject;
	}
	
	/**
	 * Check AVP is unique in case of update 
	 * @param attributeId
	 * @param vendorId
	 * @param peerId
	 * @return
	 */
	private boolean  isAVPUniqueForUpdate(int attributeId, int vendorId, int peerId, int avpId){
		
		List<DiameterAVP> avpList= avpDao.getAVPListByAttrVendor(attributeId, vendorId, peerId);
		boolean isUnique=false;
		
		if(avpList!=null && !avpList.isEmpty()){
			for(DiameterAVP avp:avpList){
				//If ID is same, then it is same peer object
				if(avpId == (avp.getId())){
					isUnique=true;
				}else{ // It is another peer object , but name is same
					isUnique=false;
				}
			}
		}else if(avpList!=null && avpList.isEmpty()){ // No peer found with same name 
			isUnique=true;
		}
		return isUnique;
	}
		
}
