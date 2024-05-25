package com.elitecore.sm.server.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.server.model.ServerType;

@Repository(value = "servertypeDao")
public class ServerTypeDaoImpl  extends GenericDAOImpl<ServerType> implements ServerTypeDao
{

}
