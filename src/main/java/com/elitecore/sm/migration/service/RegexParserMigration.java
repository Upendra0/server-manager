package com.elitecore.sm.migration.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;

public interface RegexParserMigration {

	public ResponseObject getRegexParserPluginList(Class<?> destinationClass, String migrationPrefix, int staffId) throws MigrationSMException;
}
