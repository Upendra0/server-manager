/**
 * 
 */
package com.elitecore.sm.samples;

import java.util.HashSet;
import java.util.Set;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.service.MenuService;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
public class MediationBusinessModel extends BaseBusinessModel{

	private StaffBusinessModel staffBusinessModel = new StaffBusinessModel();
	private ChangePasswordBusinessModel changePasswordBusinessModel = new ChangePasswordBusinessModel();
	private SystemParameterBusinessModel systemParamBusinessModel = new SystemParameterBusinessModel();
	private ServerManagerBusinessModel serverManagerBusinessModel = new ServerManagerBusinessModel();
	private ServiceManagerBusinessModel serviceManagerBusinessModel = new ServiceManagerBusinessModel();
	
	
	public void addMediationBusinessModel(MenuService menuService){
		menuService.addBusinessModel(getBusinessModel());
	}
	
	private BusinessModel getBusinessModel(){
		BusinessModel businessModel = new BusinessModel();
		businessModel.setName("Mediation");
		businessModel.setDescription("Mediation");
		businessModel.setAlias("MEDIATION");
		businessModel.setStatus(StateEnum.ACTIVE);

		Set<BusinessModule> businessModuleList = new HashSet<>();
		businessModuleList.add(staffBusinessModel.getStaffBusinessModule(businessModel));
		businessModuleList.add(changePasswordBusinessModel.getChangePasswordBusinessModule(businessModel));
		businessModuleList.add(systemParamBusinessModel.getSystemParameterBusinessModule(businessModel));
		businessModuleList.add(serverManagerBusinessModel.getServerManagerBusinessModule(businessModel));
		businessModuleList.add(serviceManagerBusinessModel.getServiceManagerBusinessModule(businessModel));
		businessModel.setBusinessModuleList(businessModuleList);
		return businessModel;
	}

	public SystemParameterBusinessModel getSystemParamBusinessModel() {
		return systemParamBusinessModel;
	}

	public void setSystemParamBusinessModel(SystemParameterBusinessModel systemParamBusinessModel) {
		this.systemParamBusinessModel = systemParamBusinessModel;
	}

	public StaffBusinessModel getStaffBusinessModel() {
		return staffBusinessModel;
	}

	public void setStaffBusinessModel(StaffBusinessModel staffBusinessModel) {
		this.staffBusinessModel = staffBusinessModel;
	}

	public ChangePasswordBusinessModel getChangePasswordBusinessModel() {
		return changePasswordBusinessModel;
	}

	public void setChangePasswordBusinessModel(
			ChangePasswordBusinessModel changePasswordBusinessModel) {
		this.changePasswordBusinessModel = changePasswordBusinessModel;
	}

	public ServerManagerBusinessModel getServerManagerBusinessModel() {
		return serverManagerBusinessModel;
	}

	public void setServerManagerBusinessModel(ServerManagerBusinessModel serverManagerBusinessModel) {
		this.serverManagerBusinessModel = serverManagerBusinessModel;
	}
	
	public ServiceManagerBusinessModel getServiceManagerBusinessModel() {
		return serviceManagerBusinessModel;
	}

	public void setServiceManagerBusinessModel(ServiceManagerBusinessModel serviceManagerBusinessModel) {
		this.serviceManagerBusinessModel = serviceManagerBusinessModel;
	}
}
