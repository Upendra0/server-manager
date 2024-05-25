package com.elitecore.sm.diameterpeer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;


public interface DiameterAVPService {
	
	public ResponseObject createAVP(DiameterAVP diameterAVP);
	
	public ResponseObject updateAVP(DiameterAVP diameterAVP);
	
	public ResponseObject deleteAVPList(String avpIdList);
	
	public ResponseObject deleteAVP(int avpId);	
	
}
