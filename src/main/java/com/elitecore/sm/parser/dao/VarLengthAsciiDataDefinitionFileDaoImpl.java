package com.elitecore.sm.parser.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.parser.model.VarLengthAsciiDataDefinitionFile;

@Repository(value="varLengthAsciiDataDefinitionFileDao")
public class VarLengthAsciiDataDefinitionFileDaoImpl extends GenericDAOImpl<VarLengthAsciiDataDefinitionFile> implements VarLengthAsciiDataDefinitionFileDao {

	@SuppressWarnings("rawtypes")
	@Override
	public VarLengthAsciiDataDefinitionFile getDataDefinitionByMappingId(int mappingId) {
		VarLengthAsciiDataDefinitionFile dataDefinitionFile = null;
		Criteria criteria =getCurrentSession().createCriteria(VarLengthAsciiDataDefinitionFile.class);
		criteria.add(Restrictions.eq("mapping.id", mappingId));
		List list = criteria.list();
		if(list!=null && !list.isEmpty()){
			dataDefinitionFile = (VarLengthAsciiDataDefinitionFile) list.get(0);
		}
		return dataDefinitionFile;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean isDataDefinitionFileExist(String fileName) {
		Criteria criteria =getCurrentSession().createCriteria(VarLengthAsciiDataDefinitionFile.class);
		criteria.add(Restrictions.eq(BaseConstants.DATADEFINITIONFILENAME, fileName));
		List list = criteria.list();
		if(list!=null && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
}
