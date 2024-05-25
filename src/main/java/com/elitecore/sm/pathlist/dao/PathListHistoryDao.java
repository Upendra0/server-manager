package com.elitecore.sm.pathlist.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.pathlist.model.PathListHistory;

public interface PathListHistoryDao extends GenericDAO<PathListHistory> {
	
	public PathListHistory getLatestHistoryFromPath(int pathId);

}
