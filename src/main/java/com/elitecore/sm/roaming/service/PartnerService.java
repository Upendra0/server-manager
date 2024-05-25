package com.elitecore.sm.roaming.service;

import java.util.List;

import com.elitecore.sm.roaming.model.Partner;

public interface PartnerService {

	public List<Partner> loadPartnerByNameandLob(String name,String lob);

	public long getTotalPartnerCountByName(Partner partner,String partnerName);

	public List<Partner> getPaginatedListForPartner(Partner partner,String partnerName,int startIndex, int limit, String sidx,String sord);
	
}
