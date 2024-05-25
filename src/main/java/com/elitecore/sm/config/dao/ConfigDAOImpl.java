/**
 * 
 */
package com.elitecore.sm.config.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.config.model.EntitiesRegex;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
@Repository(value = "configDAO")
public class ConfigDAOImpl  extends GenericDAOImpl<EntitiesRegex> implements ConfigDAO{

}
