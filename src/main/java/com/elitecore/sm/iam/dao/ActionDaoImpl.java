/**
 * 
 */
package com.elitecore.sm.iam.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.iam.model.Action;


/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value = "actionDao")
public class ActionDaoImpl  extends GenericDAOImpl<Action> implements ActionDao{

}
