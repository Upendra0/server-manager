package com.elitecore.sm.migration.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;

public interface ParserPluginMigrationService {
	public ResponseObject getParserMapping(String pluginType, String xmlName, String migrationPrefix, int staffId) throws MigrationSMException;
}
