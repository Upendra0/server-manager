/**
 * 
 */
package com.elitecore.sm.parser.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.parser.model.ParserPageConfiguration;

/**
 * @author Avadhesh Sharma
 *
 */
@Repository(value="parserPageConfigurationDao")
public class ParserPageConfigurationDaoImpl extends GenericDAOImpl<ParserPageConfiguration> implements ParserPageConfigurationDao{

}
