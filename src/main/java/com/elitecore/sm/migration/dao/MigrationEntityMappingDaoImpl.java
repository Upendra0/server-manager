package com.elitecore.sm.migration.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.migration.model.MigrationEntityMapping;

@Repository(value = "migrationEntityMappingDao")
public class MigrationEntityMappingDaoImpl extends	GenericDAOImpl<MigrationEntityMapping> implements
		MigrationEntityMappingDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MigrationEntityMapping> getMigrationEntityMappingList() {

		Criteria criteria = getCurrentSession().createCriteria(MigrationEntityMapping.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		return (List<MigrationEntityMapping>) criteria.list();

	}

}
