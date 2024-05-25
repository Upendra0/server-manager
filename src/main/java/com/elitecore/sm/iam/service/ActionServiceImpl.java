package com.elitecore.sm.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.dao.ActionDao;
import com.elitecore.sm.iam.model.Action;


/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "actionService")
public class ActionServiceImpl implements ActionService {
	

	@Autowired
	private ActionDao actionDao;
	
	/**
	 * Method will get action object by action id.
	 *@param actionId
	 *@return 
	 */
	@Override
	@Transactional
	public ResponseObject getActionById(int actionId) {
		ResponseObject responseObject = new ResponseObject();
		Action action = actionDao.findByPrimaryKey(Action.class, actionId);
		if (action != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(action);
		}else{
			responseObject.setSuccess(false);
		}
		return responseObject;
	} 

}
