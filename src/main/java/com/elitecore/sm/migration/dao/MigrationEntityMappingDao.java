package com.elitecore.sm.migration.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.migration.model.MigrationEntityMapping;

public interface MigrationEntityMappingDao extends GenericDAO<MigrationEntityMapping> {
	public List<MigrationEntityMapping> getMigrationEntityMappingList();
}
