package com.elitecore.sm.license.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.license.dao.CircleDao;
import com.elitecore.sm.license.dao.LicenseDao;
import com.elitecore.sm.license.exceptions.CircleNotFoundException;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.model.LicenseTypeEnum;
import com.elitecore.sm.license.model.LicenseUtilizationInfo;
import com.elitecore.sm.license.model.MappedDevicesInfoDTO;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.productconfig.service.ProductConfigurationService;
import com.elitecore.sm.server.dao.ServerDao;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.server.service.ServerTypeService;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author sterlite
 *
 */
@org.springframework.stereotype.Service(value = "circleService")
public class CircleServiceImpl implements CircleService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private CircleDao circleDao;
	
	@Autowired
	private LicenseDao licenseDao;

	@Autowired
	ProductConfigurationService productConfiguration;

	@Autowired
	ServerTypeService serverTypeService;

	@Autowired
	@Qualifier(value = "licenseUtilityQualifier")
	LicenseUtility licenseUtility;

	@Autowired
	ServletContext servletcontext;

	@Autowired
	ServerInstanceService serverInstanceService;
	
	@Autowired
	ServerService serverService;	
	
	@Autowired
	ServerDao serverDao;
	
	@Autowired
	private PathListDao pathListDao;
	
	@Autowired
	LicenseService licenseService;

	@Transactional
	@Override
	public ResponseObject getAllCirclesList() {
		ResponseObject responseObject = new ResponseObject();
		List<Circle> list = circleDao.getAllCirclesList();
		if (list != null) {
			logger.debug("License details fetch successfully.");
			for(Circle circle: list) {
				License license = licenseDao.getLicenseDetailsByCircleId(circle.getId());
				circle.setAssociated(pathListDao.isCircleAssociatedWithPathList(circle.getId()));
				if(license!=null) {
					circle.setLicenseApplied(true);
				//	circle.setLicenseApplied(licenseDao.isLicenseAppliedToCircle(circle.getId()));
					circle.setLicenseType(license.getLicenceType());					
					responseObject = licenseService.getLicenseUtilization(license);
					if(responseObject.isSuccess()) {
						circle.setLicenseExhausted(isLicenseExhausted(responseObject));
					}
				}
			}
			responseObject.setObject(list);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to get circle details");
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject createNewCircle(Circle circle, int staffId, String currentAction){
		ResponseObject responseObject =  new ResponseObject();
		CircleService circleService = (CircleService) SpringApplicationContext.getBean("circleService"); // getting spring bean for aop context issue.
		List<Circle> circleList = circleDao.getCircleListByName(circle.getName());
		if(circleList!=null && !circleList.isEmpty()){
			logger.info("Duplicate circle name is found.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_CIRCLE_NAME_FOUND);
		}else{
			responseObject = circleService.createCircle(circle);
		}
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_CIRCLE, actionType = BaseConstants.CREATE_ACTION, currentEntity = Circle.class , ignorePropList= "")
	public ResponseObject createCircle(Circle circle){
		ResponseObject responseObject = new ResponseObject();
		circleDao.save(circle);
		responseObject.setObject(circle);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.CIRCLE_CREATE_SUCCESS);
		return responseObject;
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_CIRCLE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Circle.class , ignorePropList= "")
	public ResponseObject updateCircle(Circle circle, int staffId) {
		ResponseObject responseObject =  new ResponseObject();		
		if(!isCircleNameUniqueForUpdate(circle.getId(),circle.getName())){
			logger.info("Duplicate circle name is found.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_CIRCLE_NAME_FOUND);
		}else{
			logger.info("Circle details has been validated successfully going to update circle details.");
			Circle circleNew = circleDao.findByPrimaryKey(Circle.class, circle.getId());
			circleNew.setName(circle.getName());
			circleNew.setDescription(circle.getDescription());
			circleNew.setLastUpdatedByStaffId(staffId);
			circleNew.setLastUpdatedDate(new Date());
			circleDao.merge(circleNew);
			if(circle.getId() > 0 ){
				responseObject.setObject(circleNew);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.UPDATE_CIRCLE_SUCCESS);
				logger.info("Circle has been updated successfully with name " + circleNew.getName() + ".");
			}else{
				responseObject.setObject(null);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.UPDATE_CIRCLE_FAILURE);
			}
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CIRCLE,actionType = BaseConstants.DELETE_ACTION, currentEntity= Circle.class, ignorePropList= "")
	public ResponseObject deleteCircle( int circleId, int staffId) throws CircleNotFoundException {
		ResponseObject responseObject = isCircleAssociated(circleId);
		if(responseObject.isSuccess()){
			Circle circle = circleDao.findByPrimaryKey(Circle.class, circleId);
			if(circle == null){
				responseObject.setResponseCode(ResponseCode.CIRCLE_DELETE_FAILURE);
				responseObject.setSuccess(false);
				throw new CircleNotFoundException(circleId,BaseConstants.DELETE_STAFF_STATE);
			}else{
				circle.setName(circle.getName() + BaseConstants.DELETED_MODEL_SUFFIX+ "_" + new Date().getTime());
				circle.setStatus(StateEnum.DELETED);
				circle.setLastUpdatedDate(new Date());
				circle.setLastUpdatedByStaffId(staffId);
				circleDao.merge(circle);				
				responseObject.setResponseCode(ResponseCode.CIRCLE_DELETE_SUCCESS);
				responseObject.setSuccess(true);
			}
		}else{
			responseObject.setResponseCode(ResponseCode.ASSOCIATE_CIRCLE_DELETE_FAILURE);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject isCircleAssociated(int circleId) {
		ResponseObject responseObject = new ResponseObject();
		List<PathList> pathList = pathListDao.getPathListByCircleId(circleId);
		if(pathList!=null && pathList.size()>0) {
			logger.info("Can not delete circle. It is associated with pathlist of parsing service." );
			responseObject.setSuccess(false);
		}else {
			logger.info("Circle isn't associated with any pathlist of parsing service." );
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	/**
	 * Method will Set response code messages for create or update device.
	 * @param currentAction
	 * @return
	 */
	private ResponseObject setResponseMessageCode(String currentAction){
		ResponseObject responseObject = new ResponseObject();
		if (BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
			logger.info("Failed to circle.");
			responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
		}else{
			logger.info("Failed to update circle.");
			responseObject.setResponseCode(ResponseCode.UPDATE_DEVICE_FAIL);
		}
		responseObject.setSuccess(false);
		responseObject.setObject(null);
		return responseObject;
	}

	@Transactional
	@Override
	public License prepareLicenseDetails(Circle circle, Map<String, String> licenseDetails) {
		License license = licenseDao.getLicenseDetailsByCircleId(circle.getId());
		if(license==null) {
			license =  new License();
			license.setCircle(circle);
			license.setCreatedDate(new Date());
			license.setServerInstance(null);
			license.setApplicationPath(System.getenv(BaseConstants.TOMCAT_HOME));
		}		
		license.setTps(getEncryptedTPS(licenseDetails.get(LicenseUtility.TPS).trim()));
		license.setComponentType(LicenseConstants.LICENSE_ENGINE);
		license.setCustomer(licenseDetails.get(LicenseUtility.CUSTOMER_NAME).trim());
		license.setHostName(licenseDetails.get(LicenseUtility.HOSTNAME).trim());
		Date startDate = DateFormatter.formatDate(licenseDetails.get(LicenseUtility.START_DATE), LicenseConstants.DATE_FORMAT);
		Date endDate = DateFormatter.formatDate(licenseDetails.get(LicenseUtility.END_DATE), LicenseConstants.DATE_FORMAT);
		license.setStartDate(startDate);
		license.setEndDate(endDate);
		license.setLocation(licenseDetails.get(LicenseUtility.LOCATION).trim());
		license.setLicenceType(LicenseTypeEnum.valueOf(licenseDetails.get(LicenseUtility.LICENSE_TYPE).toUpperCase()));
		license.setProductType(licenseDetails.get(LicenseUtility.PRODUCT).trim());		
		license.setLastUpdatedDate(new Date());		
		
		return license;		
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPLOAD_LICENSE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = License.class, ignorePropList= "")
	public ResponseObject saveOrUpdateLicenseInfo(License license) {
		ResponseObject responseObject =  new ResponseObject();
		licenseDao.merge(license);
		license = licenseDao.getLicenseDetailsByCircleId(license.getCircle().getId());
		if (license !=null && license.getId()>0) {
			logger.debug("License details uploaded successfully.");
			responseObject.setResponseCode(ResponseCode.LICENSE_DETAILS_SUCCESS);
			responseObject.setObject(license);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to upload license information.");
			responseObject.setResponseCode(ResponseCode.LICENSE_DETAILS_FAILURE);
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject getCircleById(int circleId) {
		ResponseObject responseObject = new ResponseObject();

		Circle circle = circleDao.getCircleById(circleId);
		if (circle != null) {
			logger.debug("License details fetch successfully.");
			responseObject.setObject(circle);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to get circle details");
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Check circle name is unique or not for update
	 * 
	 * @param circleId
	 * @param circleName
	 * @return boolean
	 */
	@Transactional
	public boolean isCircleNameUniqueForUpdate(int circleId, String circleName) {
		List<Circle> circleList = circleDao.getCircleListByName(circleName);
		boolean isUnique = false;
		if (circleList != null && !circleList.isEmpty()) {
			for (Circle circle : circleList) {
				// If ID is same , then it is same circle object
				if (circleId == (circle.getId())) {
					isUnique = true;
				} else { // It is another circle server object , but name is same
					isUnique = false;
				}
			}
		} else if (circleList != null && circleList.isEmpty()) { // No circle found with same name
			isUnique = true;
		}
		return isUnique;
	}

	@Override
	public ResponseObject validateLicenseDetails(int circleId, Map<String, String> licenseDetails) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private byte[] getEncryptedTPS(String tps) {
		String repositoryPath = servletcontext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator; //repository path up-to license folder.
		logger.info("repositoryPath for license folder = "+repositoryPath);
		byte [] encTps = licenseUtility.encryptSymMsg(tps, repositoryPath);
		if(encTps!=null && encTps.length>0) {
			return encTps;
		}
		return null;
	}
	
	@Transactional
	@Override
	public ResponseObject getAllMappedDevicesInfo(int circleId) {
		ResponseObject responseObject = new ResponseObject();
		List<MappedDevicesInfoDTO> data= circleDao.getMappedDevicesInfoByCircleId(circleId);
		if (data != null && data.size()>0) {
			logger.debug("Device and circle mapping details are fetched successfully.");
			responseObject.setObject(data);
			responseObject.setSuccess(true);
		} else {
			logger.debug("Failed to get Device and circle mapping details.");
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	private boolean isLicenseExhausted(ResponseObject responseObject) {
		
		if(responseObject!=null && responseObject.isSuccess()) {
			List<LicenseUtilizationInfo> data = (List<LicenseUtilizationInfo>) responseObject.getObject();
			if(data!=null && !data.isEmpty()) {
				for (LicenseUtilizationInfo obj : data) {
					if(obj.isLicenseExhausted()) 
						return true;
				}
			}
		}
		return false;
	}
		
}