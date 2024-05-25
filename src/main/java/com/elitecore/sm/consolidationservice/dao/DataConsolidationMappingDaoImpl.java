package com.elitecore.sm.consolidationservice.dao;

import java.io.File;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;

@Repository
public class DataConsolidationMappingDaoImpl extends GenericDAOImpl<DataConsolidationMapping> implements DataConsolidationMappingDao{

	@Override
	public long getDataConsolidationMappingCountByMappingNameAndDestPath(int mappingId,String mappingName, String destPath) {
		Criteria criteria = getCurrentSession().createCriteria(DataConsolidationMapping.class);
		String destPath1 = "";
		String destPath2 = "";
		if(destPath.endsWith(File.separator)){
			destPath1 = destPath;
			destPath2 = destPath.substring(0, destPath.lastIndexOf(File.separator));
		} else {
			destPath1 = destPath;
			destPath2 = destPath + File.separator;
		}
		if(mappingId > 0)
			criteria.add(Restrictions.ne("id",mappingId));
		criteria.add(Restrictions.eq("mappingName", mappingName));
		criteria.add(Restrictions.or(Restrictions.eq("destPath", destPath1), Restrictions.eq("destPath", destPath2)));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

}
