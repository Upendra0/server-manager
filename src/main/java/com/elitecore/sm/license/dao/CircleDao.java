package com.elitecore.sm.license.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.MappedDevicesInfoDTO;

public interface CircleDao extends GenericDAO<Circle> {
	
	public List<Circle> getAllCirclesList();
	
	public Circle getCircleById(int circleId);
	
	public int getCountByCircleName(String circleName);
	
	public List<Circle> getCircleListByName(String circleName);
	
	public List<MappedDevicesInfoDTO> getMappedDevicesInfoByCircleId(int circleId);

}
