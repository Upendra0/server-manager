package com.elitecore.sm.license.service;

import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.license.exceptions.CircleNotFoundException;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.License;

public interface CircleService {

	public ResponseObject getAllCirclesList();
	
	public ResponseObject createCircle(Circle circle);
	
	public ResponseObject createNewCircle(Circle circle, int staffId, String currentAction);
	
	public License prepareLicenseDetails(Circle circle, Map<String, String> licenseDetails);
	
	public ResponseObject getCircleById(int circleId);
	
	public ResponseObject validateLicenseDetails(int circleId, Map<String, String> licenseDetails);
	
	public ResponseObject updateCircle(Circle circle, int staffId);
	
	public ResponseObject deleteCircle( int circleId, int staffId) throws CircleNotFoundException;
	
	public ResponseObject isCircleAssociated(int circleId);
	
	public ResponseObject saveOrUpdateLicenseInfo(License license);
	
	public ResponseObject getAllMappedDevicesInfo(int circleId);
	
}
