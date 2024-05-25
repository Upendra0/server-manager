package com.elitecore.sm.roaming.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;

@Repository(value="roamingFileSequenceMgmtDao")
public class RoamingFileSequenceMgmtDaoImpl extends GenericDAOImpl<RoamingFileSequenceMgmt> implements RoamingFileSequenceMgmtDao {

}
