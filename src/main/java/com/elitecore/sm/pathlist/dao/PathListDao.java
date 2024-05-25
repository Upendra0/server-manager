package com.elitecore.sm.pathlist.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;

public interface PathListDao extends GenericDAO<PathList>{
	
	public int getPathListCount(String pathListName);
	
	public List<PathList> getPathListByDriverId(int driverId);
	
	public List<PathList> getPathListByName(String pathListName);
	
	public List<PathList> getParsingPathListbyId(int pathListId);
	
	public PathList getDistributionPathListById(int id);
	
	public void irerateOverParsingPathList(ParsingPathList pathlist);
	
	public void iterateOverDistributionPathList(DistributionDriverPathList distributionPathList);
	
	public List<ParsingPathList> getParsingPathListByServiceId(int serviceId);
	
	public List<PathList> getPathListByServiceId(int serviceId);
	
	public String getServiceMaxPathId(int serviceId);
	
	public String getDriverMaxPathId(int driverId);
	
	public int getPathListCountByNameAndService(String pathListName, int serviceId, int driverId);
	
	public boolean validateDuplicateDeviceName(PathList pathList);

	public void iterateOverCollectionPathList(CollectionDriverPathList pathlist);
	
	public PathList getPathListByServiceIdAndPathId(int serviceId, String pathId);
	
	public List<PathList> getPathListByCircleId(int circleId);
	
	public boolean isCircleAssociatedWithPathList(int circleId);

}
