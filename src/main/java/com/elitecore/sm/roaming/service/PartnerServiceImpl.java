package com.elitecore.sm.roaming.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elitecore.sm.roaming.dao.PartnerDao;
import com.elitecore.sm.roaming.model.Partner;

@Service
public class PartnerServiceImpl implements PartnerService {

	@Autowired
	private PartnerDao partnerDao;
	@Override
	public List<Partner> loadPartnerByNameandLob(String partner, String lob) {
		
		List<Partner> partnerByNameandLob = partnerDao.getPartnerByNameandLob(partner, lob);
		
		return partnerByNameandLob;
	}
	@Override
	public long getTotalPartnerCountByName(Partner partner,String partnerName) {
		return partnerDao.getTotalPartnerCountByName(partner,partnerName);
	}
	@Override
	public List<Partner> getPaginatedListForPartner(Partner partner,String partnerName, int startIndex, int limit, String sidx,
			String sord) {
		
		return partnerDao.getDevicesPaginatedList(Partner.class,partnerName,startIndex, limit, sidx, sord);
	}

}
