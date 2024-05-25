package com.elitecore.sm.services.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.services.dao.PartitionParamDao;
import com.elitecore.sm.services.model.PartitionFieldEnum;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.validator.PartitionParamValidator;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author vishal.lakhyani
 *
 */
@org.springframework.stereotype.Service(value="paramService")
public class PartitionParamServiceImpl implements PartitionParamService {
	
	@Autowired
	PartitionParamDao paramDao;
	
	@Autowired
	PartitionParamValidator partitionParamvalidator;
	
	@Override
	@Transactional
	public ResponseObject updatePartitionParamStatus(int paramId,String status){
		ResponseObject responseObject = new ResponseObject();
		PartitionParam param = getPartitionParamById(paramId);
		if(param != null){
			param.setStatus(StateEnum.valueOf(status));
			responseObject.setSuccess(true);
			paramDao.merge(param);
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Override
	@Transactional(readOnly=true)
	public PartitionParam getPartitionParamById(int paramId){
		return paramDao.findByPrimaryKey(PartitionParam.class,paramId);
	}
	
	@Override
	@Transactional
	public void validatePartitionParamForImport(List<PartitionParam> partitionParamList,List<ImportValidationErrors> importErrorList){
		if(partitionParamList !=null && !partitionParamList.isEmpty()){
			for(PartitionParam partitionParam:partitionParamList){
				if(!StateEnum.DELETED.equals(partitionParam.getStatus())){
					partitionParamvalidator.validatePartitionParam(partitionParam,null,BaseConstants.SERVICE,true,importErrorList);
				}
			}
		}
	}
	
	@Override
	public void importPartitionParamUpdateMode(PartitionParam dbParam, PartitionParam exportedParam) {
		dbParam.setUnifiedField(exportedParam.getUnifiedField());
		dbParam.setBaseUnifiedField(exportedParam.getBaseUnifiedField());
		dbParam.setPartitionRange(exportedParam.getPartitionRange());
		dbParam.setNetMask(exportedParam.getNetMask());
	}
	
	@Override
	public void importPartitionParamAddAndKeepBothMode(PartitionParam exportedParam, Service parsingService) {
		exportedParam.setId(0);
		exportedParam.setParsingService(parsingService);
		exportedParam.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedParam.setCreatedByStaffId(parsingService.getCreatedByStaffId());
		exportedParam.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedParam.setLastUpdatedByStaffId(parsingService.getLastUpdatedByStaffId());
	}
	
	@Override
	public PartitionParam getPartitionParamFromList(List<PartitionParam> partitionParamList, PartitionFieldEnum partitionField) {
		if(!CollectionUtils.isEmpty(partitionParamList)) {
			int length = partitionParamList.size();
			for(int i = length-1; i >= 0; i--) {
				PartitionParam partitionParam = partitionParamList.get(i);
				if(partitionParam != null && !partitionParam.getStatus().equals(StateEnum.DELETED)
						&& partitionParam.getPartitionField().equals(partitionField)) {
					return partitionParamList.remove(i);
				}
			}
		}
		return null;
	}
}