package com.elitecore.sm.diameterpeer.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;

public interface DiameterPeerDao extends GenericDAO<DiameterPeer> {

	public int getPeerCount(String peerName, int serviceId);
	
	public List<DiameterPeer> getPeerListForService(int serviceId);
	
	public List<DiameterPeer> getPeerListByName(String peerName, int serviceId);
	
	public DiameterPeer getDiameterPeerById(int peerId);
	
}
