package com.elitecore.sm.diameterpeer.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;

@Repository(value="diameterAVPDao")
public interface DiameterAVPDao  extends GenericDAO<DiameterAVP>{

	public int getAVPCount(int attributeId,int vendorId,int peerId);
	
	public List<DiameterAVP> getAVPListByAttrVendor(int attributeId, int vendorId, int peerId);
}
