package com.elitecore.sm.roaming.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.roaming.model.Partner;

public interface PartnerDao extends GenericDAO<Partner>{

	public List<Partner> getPartnerByNameandLob(String partner,String lob);


	public long getTotalPartnerCountByName(Partner partner,String partnerName);

	public List<Partner> getDevicesPaginatedList(Class<Partner> class1,String partnerName, int startIndex, int limit, String sidx,String sord);
}
