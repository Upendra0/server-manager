/**
 * 
 */
package com.elitecore.sm.common.util;

/**
 * @author Sunil Gulabani Apr 29, 2015
 */
public enum ResponseCode {
	STAFF_INSERT_SUCCESS(1, "Staff Added."), STAFF_INSERT_FAIL(2,
			"Failed to add staff."), DUPLICATE_STAFF(3,
			"This staff already exists."),

	STAFF_UPDATE_SUCCESS(4, "Staff Updated."),

	ACCESS_GROUP_INSERT_SUCCESS(5, "Access Group Added."), ACCESS_GROUP_INSERT_FAIL(
			6, "Failed to add Access Group."), DUPLICATE_ACCESS_GROUP(7,
			"This Access Group Name already exists."),

	DUPLICATE_STAFF_EMAIL_ID(8, "Email Id already exists."),

	SERVER_INSERT_SUCCESS(9, "server.add.success"), SERVER_INSERT_FAIL(10,
			"server.add.failed"), DUPLICATE_SERVER(11, "server.already.exists"), SERVER_UPDATE_SUCCESS(
			12, "server.update.success"), SERVER_DOES_NOT_EXIST(13,
			"server.does.not.exist"), SERVER_DELETE_SUCCESS(14,
			"server.delete.success"),SERVER_CREATE_SUCCESS_WITH_WARNING(15,
					"server.create.success.with.warning"),

	DUPLICATE_SERVER_INSTANCE(15, "server.instance.already.exists"), SERVER_INSTANCE_INSERT_SUCCESS(
			16, "server.instance.add.success"), SERVER_INSTANCE_ALREADY_AVALIABLE(
			17, "server.instance.already.running.on.port"), SERVER_INSTANCE_INSERT_FAIL(
			18, "server.instance.add.failed"), SYSTEM_PARAMETER_INSERT_FAIL(19,
			"Failed to add System Parameter."), SERVER_INSTANCE_INSERT_JMX_CONN_FAIL(
			20, "server.instance.add.failed.jmx.connection.failed"), SERVER_INSTANCE_INSERT_JMX_API_FAIL(
			21, "server.instance.add.failed.jmx.api.failed"), SERVER_INSTANCE_INSERT_NO_DEFAULT_DS(
			22, "server.instance.add.failed.no.default.ds.found"), SYSTEM_PARAM_GROUP_INSERT_SUCCESS(
			23, "System Parameter Group inserted Successfully."), SYSTEM_PARAM_GROUP_INSERT_FAIL(
			24, "Failed to add System Parameter Group."), SYSTEM_PARAM_VALUEPOOL_INSERT_SUCCESS(
			25, "System Parameter ValuePool inserted Successfully."), SYSTEM_PARAM_VALUEPOOL_INSERT_FAIL(
			26, "Failed to add System Parameter ValuePool."), SYSTEM_PARAMETER_UPDATE_SUCCESS(
			27, "System parameter Updated Successfully."), IMAGE_INSERT_SUCCESS(
			28, "Image inserted Successfully."), IMAGE_INSERT_FAIL(29,
			"Failed to image."), DATA_SOURCE_INSERT_FAIL(31,
			"dataSource.insert.fail"), SERVER_INSTANCE_UNIQUE_IN_DB(
			32, "Server Instance is Unique in database."), XSLT_INSERT_SUCCESS(
			33, "XSLT inserted Successfully."), FORGOT_PASSWORD_FIRST_LOGIN(34,
			"forgotPassword.firstTime.login.error"),
			SYSTEM_PARAMETER_INSERT_SUCCESS(36, "System Parameter inserted Successfully."),
	FORGOT_PASSWORD_USER_FOUND(37, "Staff found"), FORGOT_PASSWORD_WRONG_USER(
			38, "Username not avaliable"), FORGOT_PASSWORD_WRONG_SECURITY_QUESTION(
			39, "Security question/answer is wrong"),

	SERVER_INSTANCE_UPDATE_SUCCESS(40, "server.instance.updated.success"), SERVER_INSTANCE_UPDATE_FAIL(
			41, "Failed to update Server Instance."),

	DUPLICATE_SERVER_INSTANCE_NAME(251, "server.instance.name.duplicate.failed"),

	SERVICE_TYPE_ADD_SUCCESS(42, "Service type added successfully"), SERVICE_TYPE_ADD_FAIL(
			43, "Service type add failed"),

	SERVICE_ADD_SUCCESS(44, "Service added successfully"), SERVICE_ADD_FAIL(45,
			"Service add failed"),

	SERVICE_UPDATE_SUCCESS(46, "service.update.successfully"), 
	SERVICE_UPDATE_FAIL(47, "service.updated.fail"),
	SERVERINSTANCE_UPDATE_FAIL_NO_DEFAULT_DS(83, "server.instance.update.failed.no.default.ds.found"),

	DRIVER_ADD_SUCCESS(48, "driver.add.success"), DRIVER_ADD_FAIL(49,
			"driver.add.fail"),

	DRIVER_UPDATE_SUCCESS(50, "driver.update.success"), DRIVER_UPDATE_FAIL(51,
			"driver.update.fail"),

	INVALID_EXPORT_BACKUP_PATH(48, "invalid.export.path"),

	SERVER_INSTANCE_RUNNING(53, "Server Instance is running"), SERVICE_RUNNING(
			54, "server.instance.import.service.running"), SERVER_INSTANCE_COPY_CONFIG_SUCCESS(
			55, "Copy Configuration has been done successfully."), SERVER_INSTANCE_COPY_CONFIG_FAIL(
			56, "Copy Configuration has been failed"),

	SNMP_STATE_CHANGED_SUCCESS(57, "updtInstacne.snmp.alert.sts.success"), SERVER_INSTANCE_UNAVALIABLE(
			58, "server.instance.not.found"), SERVER_INSTANCE_ID_UNAVALIABLE(
			59, "server.instance.id.is.missing"), FILE_STATE_CHANGED_SUCCESS(
			60, "updtInstacne.file.state.sts.success"), WEBSERVICE_STATE_CHANGED_SUCCESS(
			61, "updtInstacne.webservice.sts.success"),

	PLUGIN_TYPE_ADD_SUCCESS(62, "Plugin type added successfully"), PLUGIN_TYPE_ADD_FAIL(
			63, "Plugin type add failed"),

	DRIVER_TYPE_ADD_SUCCESS(64, "Driver type added successfully"), DRIVER_TYPE_ADD_FAIL(
			65, "Driver type add failed"),

	SERVER_INSTANCE_RELOAD_CONFIG_SUCCESS(66,
			"server.instance.reload.config.success"), SERVER_INSTANCE_RELOAD_CONFIG_FAIL(
			67, "server.instance.reload.config.failed"), SERVER_INSTANCE_RELOAD_CONFIG_JMX_CONN_FAIL(
			68, "reload.config.jmx.connection.failed"), SERVER_INSTANCE_RELOAD_CONFIG_JMX_API_FAIL(
			69, "reload.config.jmx.api.failed"),

	JMX_CONNECTION_FAIL(70, "JMX connection failure"), JMX_API_FAIL(71,
			"JMX api failure"),

	SERVER_INSTANCE_SOFT_RESTART_SUCCESS(72, "soft.restart.success"), SERVER_INSTANCE_SOFT_RESTART_FAIL(
			73, "soft.restart.failed"), SERVER_INSTANCE_SOFT_RESTART_JMX_CONN_FAIL(
			74, "soft.restart.jmx.connection.failed"), SERVER_INSTANCE_SOFT_RESTART_JMX_API_FAIL(
			75, "soft.restart.jmx.api.failed"),

	
	SERVER_INSTANCE_STOP_SUCCESS(76, "stop.server.success"), SERVER_INSTANCE_STOP_FAIL(
			77, "stop.server.failed"), SERVER_INSTANCE_STOP_JMX_CONN_FAIL(76,
			"stop.server.jmx.connection.failed"),

	SERVER_INSTANCE_START_SUCCESS(78, "start.server.success"), SERVER_INSTANCE_START_FAIL(
			79, "start.server.failed"), SERVER_INSTANCE_START_SCRIPT_UNAVALIABLE(
			80, "unable.find.instance.script"), SERVER_INSTANCE_START_JMX_CONN_FAIL(
			81, "start.server.jmx.connection.failed"), SERVER_INSTANCE_START_JMX_API_FAIL(
			82, "start.server.jmx.api.failed"),

	SERVER_INSTANCE_RESTART_SUCCESS(83, "restart.server.success"), SERVER_INSTANCE_RESTART_FAIL(
			84, "restart.server.failed"), SERVER_INSTANCE_RESTART_JMX_CONN_FAIL(
			85, "restart.server.jmx.connection.failed"), SERVER_INSTANCE_RESTART_JMX_API_FAIL(
			86, "restart.server.jmx.api.failed"),

	SERVER_INSTANCE_INIT_FAIL(87, "server.instance.start.script.error"), SERVER_INSTANCE_INIT_SUCCESS(
			88, "server.instance.init.success"), P_ENGINE_NOT_RUNNING(89,
			"crestel.engine.not.running.on.port"), UNKNOWN_OS(90,
			"server.instance.unknown.os"), SERVER_INSTANCE_CREATE_SCRIPT_FAIL(
			91, "server.instance.create.script.error"), SERVER_INSTANCE_CREATE_SCRIPT_JMX_CONN_FAIL(
			92, "server.instance.create.script.jmx.conn.failed"), SERVER_INSTANCE_CREATE_SCRIPT_JMX_API_FAIL(
			93, "server.instance.create.script.jmx.api.failed"), SERVER_INSTANCE_RUN_SCRIPT_JMX_CONN_FAIL(
			94, "server.instance.run.script.jmx.conn.failed"), SERVER_INSTANCE_RUN_SCRIPT_JMX_API_FAIL(
			95, "server.instance.run.script.jmx.api.failed"), SERVER_INSTANCE_RUNNING_INSTANCE_VERSION_MISMATCH(
			96, "server.version.mismatch"), UNKNOWN_SERVICE_RUN_ON_PORT(97,
			"another.service.running.on.same.port"),

	SERVERINSTANCE_SYNCHRONIZATION_SUCCESS(98,
			"Synchronization done successfully."), SERVERINSTANCE_SYNCHRONIZATION_FAIL(
			99, "Failed to sync server instance, check logs for more detail."), SERVERINSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL(
			100,
			"Failed to sync server. Cannot obtain JMX connection to engine."), SERVERINSTANCE_SYNC_FAIL_JMX_API_FAIL(
			101,
			"Failed to sync Server Instance. Error while invoking JMX api to engine."), SERVERINSTANCE_ALREADY_SYNC(
			102, "Server Instance is already Synchronize"), SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS(
			103, "Synchronization can perform for only running instance."),

	SERVERINSTANCE_COPY_FAIL_SERVICE_RUNNING(108,
			"server.instance.copy.service.running"), SERVERINSTANCE_COPY_FAIL(
			109, "server.instance.copy.failed"),

	SERVERiNSTANCE_IMPORT_UPLOAD_SUCCESS(110,
			"server.instance.import.upload.success"), SERVERiNSTANCE_IMPORT_UPLOAD_FAIL(
			111, "server.instance.import.upload.fail"),

	SERVERINSTANCE_ADD_FAIL_NO_DEFAULT_DS(83,
			"server.instance.add.failed.no.default.ds.found"), DUPLICATE_SERVICE_NAME(
			84, "service.name.already.exists"), ACTIVE_SERVICE(85,
			"service.status.active"), INACTIVE_SERVICE(86,
			"service.status.inactive"),

	SERVICE_INSTANCE_SYNCHRONIZATION_SUCCESS(87,
			"service.instance.sync.success"), SERVICE_INSTANCE_SYNCHRONIZATION_FAIL(
			88, "service.instance.sync.failed"), SERVICE_INSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL(
			89, "service.instance.sync.jmx.connection.failed"), SERVICE_INSTANCE_SYNC_FAIL_JMX_API_FAIL(
			90, "service.instance.sync.jmx.api.failed"), SERVICE_INSTANCE_ALREADY_SYNC(
			91, "service.instance.already.sync"), SERVICE_INSTANCE_SYNC_FAIL_INACTIVE_STATUS(
			92, "service.instance.status.running"), SERVICE_INSTANCE_ID_NOT_FOUND(
			93, "service.instance.id.notfound"), SERVICE_INSTANCE_START_SUCCESS(
			94, "service.start.success"), SERVICE_INSTANCE_STOP_SUCCESS(95,
			"service.stop.success"), SERVICE_INSTANCE_START_FAIL(96,
			"service.start.fail"), SERVICE_INSTANCE_STOP_FAIL(97,
			"service.stop.fail"), SERVICE_INSTANCE_NOT_FOUND(98,
			"service.instance.not.found"), EXPORT_FAIL(99,
			"serverMgmt.export.config.fail"), INVALID_EXPORT_FILE_PATH(100,
			"export.file.path.not.found"), IMPORT_FAIL(101,
			"server.instance.import.failed"), SERVICE_INSTANCE_IMPORT_SUCCESS(
			102, "service.instance.import.success"), IMPORT_SERVICE_RUNNING(
			103, "import.service.instance.running"), SERVICE_INSTANCE_DELETE_SUCCESS(
			104, "service.instance.delete.success"), SERVICE_INSTANCE_DELETE_FAIL(
			105, "service.instance.delete.fail"), SERVICE_INSTANCE_DELETE_FAIL_RUNNING(
			106, "service.instance.delete.fail.running"), SERVICE_INSTANCES_SYNC_ALL_SUCCESS(
			107, "service.instance.sync.success.all"), SERVICE_INSTANCES_SYNC_ALL__FAIL(
			108, "service.instance.sync.failed.all"), SERVICE_INSTANCES_SYNC_PARSIAL(
			109, "service.instance.sync.failed.partial"),

	DUPLICATE_DRIVER_NAME(112, "driver.name.already.exists"), FTP_DRIVER_CONFIG_UPDATE_SUCCESS(
			113, "ftp.driver.configuration.update.success"), FTP_DRIVER_CONFIG_UPDATE_FAIL(
			114, "ftp.driver.configuration.update.fail"),

	SERVERiNSTANCE_UNMARSHAL_FAIL(115, "server.instance.import.upload.fail"), SERVERINSTANCE_IMPORT_FILE_UNAVALIABLE(
			116, "server.instance.import.file.uavaliable"), SERVERINSTANCE_CREATE_COPY_SUCCESS(
			117, "server.instance.create.copy.success"), SERVERINSTANCE_CREATE_IMPORT_SUCCESS(
			118, "server.instance.create.import.success"), SERVERINSTANCE_CREATE_IMPORT_FILE_OVERSIZE(
			119, "server.instance.create.import.file.oversize"), SERVERINSTANCE_CREATE_SUCCESS_IMPORT_FILE_OVERSIZE(
			120, "server.instance.create.success.import.file.unavaliable"),

	SERVER_INSTANCE_RELOAD_CACHE_SUCCESS(121,
			"server.instance.reload.cache.success"), SERVER_INSTANCE_RELOAD_CACHE_FAIL(
			122, "server.instance.reload.cache.failed"), SERVER_INSTANCE_RELOAD_CACHE_JMX_CONN_FAIL(
			123, "server.instance.reload.cache.jmx.connection.failed"), SERVER_INSTANCE_RELOAD_CACHE_JMX_API_FAIL(
			124, "server.instance.reload.cache.jmx.api.failed"),

	FTP_COLLECTION_DRIVER_REORDER_SUCCESS(125,
			"collection.driver.reorder.success"), FTP_COLLECTION_DRIVER_NO_DRIVER_LIST(
			126, "collection.driver.reorder.empty.list"),

	DUPLICATE_PATHLIST_NAME(125, "pathList.name.already.exists"),DUPLICATE_REFERENCE_DEVICE_NAME(509, "reference.device.name.already.exists"), PATHLIST_ADD_SUCCESS(
			126, "pathList.add.success"), PATHLIST_ADD_FAIL(127,
			"pathList.add.fail"), SERVER_INSTANCE_CON_FAIL(128,
			"server.instance.con.failed"), DRIVER_DELETE_SUCCESS(129,
			"driver.delete.success"), DRIVER_DELETE_FAIL(130,
			"driver.delete.fail"), DRIVER_NOT_FOUND(132, "driver.not.found"), PATHLIST_UPDATE_SUCCESS(
			131, "pathList.update.success"),DRIVER_DELETE_ACTIVE_FAIL(133,"driver.delete.active.fail"),

	PATHLIST_DELETE_SUCCESS(132, "pathList.delete.success"), PATHLIST_DELETE_FAIL(
			133, "pathList.delete.fail"), SERVICE_INSTANCE_STOP(134,
			"service.instance.stop"), SERVICE_INSTANCE_START_JMX_API_FAIL(135,
			"start.service.jmx.api.failed"), SERVICE_INSTANCE_STOP_JMX_API_FAIL(
			136, "stop.service.jmx.api.failed"), SERVICE_INSTANCE_COUNTER_JMX_API_FAIL(
			1377, "service.instance.fetch.counter.jmx.connection.failed"), PATHLIST_UPDATE_FAIL(
			138, "pathList.update.fail"), SERVICE_INSTANCE_JMX_CON_FAIL(139,
			"service.instance.jmx.con.fail"),

	SERVICE_INSTANCE_JMX_API_FAIL(140, "service.instance.jmx.api.failed"),

	SERVER_DELETE_FAIL(141, "server.delete.failed"), SERVER_DELETE_FAIL_SERVERINSTANCE_ACTIVE(
			142, "server.delete.failed.serverinstance.active"), SERVER_INSTANCE_DELETE_SUCCESS(
			143, "server.instance.delete.success"),

	SFTP_DRIVER_TYPE_NOT_FOUND(144, "driver.type.warn.message"), DUPLICATE_NETFLOW_CLIENT_NAME(
			145, "netflow.client.name.already.exists"), NETFLOW_CLIENT_UPDATE_SUCCESS(
			146, "netflow.client.update.success"), DUPLICATE_CLIENT_NAME(147,
			"client.name.already.exists"), CLIENT_ADD_FAIL_SERV_UNAVAIL(148,
			"client.add.fail.serv.unavail"),

	CLIENT_ID_UNAVALIABLE(149, "client.id.unavaliable"),

	CLIENT_DELETE_SUCCESS(150, "client.delete.success"), CLIENT_ACTIVE_DELETE_FAIL(
			151, "client.enable.delete.warning.message"), CLIENT_ADD_SUCCESS(
			152, "client.add.success"), SERVERINSTANCE_DELETE_FAIL_JMX_API_FAIL(
			153, "serverinstance.delete.fail.jmx.api.fail"), SERVERINSTANCE_RUNNING_DELETE_FAIL(
			154, "server.instance.delete.server.instance.running"), SERVERINSTANCE_DELETE_FAIL(
			155, "server.instance.delete.failed"), TOMCAT_HOME_NOT_FOUND(156,
			"export.backup.fail.tomcat.home.not.set"), IMPORT_VALIDATION_FAIL(
			157, "serverInstance.import.validation.fail"), SERVERINSTANCE_IMPORT_SUCCESS(
			158, "server.instance.import.success"), SERVERINSTANCE_IMPORT_FAIL(
			159, "server.instance.import.failed"), SERVICE_UNMARSHALL_FAIL(160,
			"server.instance.import.failed"), IMPORT_UNMARSHALL_FAIL_CLASSCAST(
			161, "server.instance.import.wrong.file.select"), SERVERINSTANCE_COPY_FROM_INSTANCE_UNAVAILABLE(
			162, "server.instance.copy.from.instance.unavailable"),
			IMPORT_XSD_VALIDATION_FAIL(163, "serverInstance.import.schema.validation.fail"), CLIENT_ADD_FAIL(
			164, "client.add.fail"), SERVER_INSTANCE_INACTIVE_COPY_CONFIG_FAIL(
			165, "server.instance.inactive.copy.fail"), NATFLOW_PARSER_CONFIG_UPDATE_SUCCESS(
			166, "natflow.parser.config.update.success"), NATFLOW_PARSER_CONFIG_UPDATE_FAIL(
			167, "natflow.parser.config.update.fail"), NATFLOW_PARSER_NOT_FOUND(
			168, "natflow.parser.not.found.id"), DEVICE_CONFIG_NOT_FOUND(169,
			"device.config.not.found.id"), DEVICE_CONFIG_UPDATE_SUCCESS(170,
			"device.config.update.success"), DEVICE_NOT_FOUND(171,
			"device.not.found.id"), DEVICE_CONFIG_CREATE_SUCCESS(172,
			"device.config.create.success"), DEVICE_CONFIG_CREATE_FAIL(173,
			"device.config.create.fail"), DEVICE_CREATE_FAIL(174,
			"device.create.fail"), DEVICE_CREATE_SUCCESS(175,
			"device.create.success"),

	DUPLICATE_PARSER_NAME(176, "parser.name.already.exists"), IMPORT_SERVICE_UNMARSHALL_FAIL_CLASSCAST(
			177, "service.import.wrong.file.select"), IMPORT_SERVICE_UNMARSHALL_FAIL_CLASSCAST_FILE_NAME(
			178, "service.import.wrong.file.select.file.name"),


	DUPLICATE_DEVICE_NAME(179, "device.name.already.exists"), 
	DEVICE_TYPE_NOT_FOUND(180, "device.type.not.found"), 
	VENDOR_TYPE_NOT_FOUND(181,"vendor.type.not.found"), 
	FAIL_GET_DEVICE_DETAILS(182,"device.details.get.fail"), 
	FAIL_GET_VENDOR_DETAILS(183,"vendor.details.get.fail"), 
	DUPLICATE_DEVICE_TYPE_NAME(184,"device.type.name.already.exists"), 
	DUPLICATE_VENDOR_TYPE_NAME(185,"vendor.type.name.already.exists"), 
	FAIL_CREATE_DEVICE_TYPE(186,"device.type.create.fail"), FAIL_CREATE_VENDOR_TYPE(187,
			"vendor.type.create.fail"), FAIL_GET_DEVICE_BY_ID(188,
			"device.get.by.id.fail"), FAIL_GET_MAPPING_BY_ID(189,
			"mapping.get.id.fail"), FAIL_DELETE_MAPPING(190,
			"mapping.delete.fail"), DELETE_MAPPING_SUCCESS(191,
			"mapping.delete.success"), DELETE_DEVICE_SUCCESS(192,
			"device.delete.success"), DELETE_DEVICE_FAIL(193,
			"device.delete.fail"), UPDATE_DEVICE_SUCCESS(194,
			"device.update.success"), UPDATE_DEVICE_FAIL(195,
			"device.update.fail"), DEVICE_MAPPING_DELETE_SUCCESS(196,
			"device.mapping.delete.success"), DEVICE_MAPPING_DELETE_FAIL(197,
			"device.mapping.delete.fail"), PARSER_ADD_SUCCESS(198,
			"parser.add.success"), PARSER_ADD_FAIL(199, "parser.add.fail"), PARSER_UPDATE_SUCCESS(
			200, "parser.update.success"), PARSER_UPDATE_FAIL(201,
			"parser.update.fail"), PARSER_DELETE_SUCCESS(202,
			"parser.delete.success"), PARSER_DELETE_FAIL(203,
			"parser.delete.fail"), REGEX_PARSER_BASIC_DETAIL_SAVE_SUCCESS(204,
			"regex.parser.basic.detail.save.success"), REGEX_PARSER_SAMPLE_FILE_SAVE_FAIL(
			205, "regex.parser.sample.file.not.save"), REGEX_PARSER_NO_SAMPLE_FILE_FOUND(
			206, "regex.parser.sample.file.not.found"), REGEX_PARSER_LOGPATTERN_REGEX_INVALID(
			207, "RegExParserMapping.logPatternRegex.invalid"), REGEX_PARSER_PATTERN_REGEX_INVALID(
			208, "RegExParserMapping.PatternRegex.invalid"), FAIL_GET_MAPPING_DEVICE_ID(
			209, "mapping.get.by.device.id.fail"), PARSER_ATTRIBUTE_ADD_SUCCESS(
			210, "parser.attribute.add.success"), PARSER_ATTRIBUTE_ADD_FAIL(
			211, "parser.attribute.add.fail"), PARSER_ATTRIBUTE_UPDATE_SUCCESS(
			212, "parser.attribute.update.success"), PARSER_ATTRIBUTE_UPDATE_FAIL(
			213, "parser.attribute.update.fail"), FAIL_GET_ATTRIBUTE_BY_ID(214,
			"parser.attribute.get.id.fail"), ATTRIBUTE_DELETE_SUCCESS(215,
			"attribute.delete.success"), ATTRIBUTE_DELETE_FAIL(216,
			"attribute.delete.fail"), FAIL_CREATE_MAPPING(217,
			"create.mapping.fail"), CREATE_MAPPING_SUCCESS(218,
			"create.mapping.success"), FAIL_UPDATE_MAPPING(219,
			"update.mapping.fail"), UPDATE_MAPPING_SUCCESS(220,
			"update.mapping.success"), DUPLICATE_MAPPING_FOUND(221,
			"mapping.name.already.exist"), DUPLICATE_ATTRIBUTE_FOUND(222,
			"attribute.name.already.exist"), STAFF_DELETE_SUCCESS(223,
			"staff.delete.success"), STAFF_DELETE_FAIL(224, "staff.delete.fail"), STAFF_LOCK_UPDATE_SUCCESS(
			225, "staff.locked.success"), STAFF_LOCK_UPDATE_FAIL(226,
			"staff.locked.fail"), STAFF_UNLOCK_UPDATE_SUCCESS(227,
			"staff.unlocked.success"), STAFF_UNLOCK_UPDATE_FAIL(228,
			"staff.unlocked.fail"), STAFF_STATUS_UPDATE_SUCCESS(229,
			"staff.state.changed.successfully"), STAFF_STATUS_UPDATE_FAIL(230,
			"staff.change.state.fail"),
		SNMP_SERVER_ADDED_SUCCESSFULLY(231, "snmp.server.added.successfully"), SNMP_SERVER_ADDED_FAIL(
					232, "snmp.server.added.fail"), SNMP_UPDATE_SUCCESS(233,
					"snmp.update.success"), SNMP_UPDATE_FAIL(234, "snmp.update.fail"), SNMP_SERVER_NOT_FOUND(
					235, "snmp.server.not.found.warn.message"), SNMP_UPDATE_FAIL_Only_One_Allowed(
					236, "snmp.server.already.one.active"), DUPLICATE_SNMP_SERVER_NAME(
					237, "snmp.server.name.already.exists"),


			AJAX_GENERIC_EXCEPTION(238, "ajax.generic.exception.fail"), FAIL_GET_COMPOSER_LIST(
					239, "fail.get.composer.list"), CREATE_COMPOSER_SUCCESS(240,
					"create.composer.success"), CREATE_COMPOSER_FAIL(241,
					"create.composer.fail"), UPDATE_COMPOSER_SUCCESS(242,
					"update.composer.success"), UPDATE_COMPOSER_FAIL(243,
					"update.composer.fail"),


	REGEX_PATTERN_ADD_SUCCESS(244,"regex.pattern.add.successfully"),
	REGEX_PATTERN_ADD_FAIL(245,"regex.pattern.add.fail"),
	REGEX_PATTERN_UPDATE_SUCCESS(246,"regex.pattern.update.successfully"),
	REGEX_PATTERN_UPDATE_FAIL(247,"regex.pattern.update.fail"),
	REGEX_PATTERN_DELETE_SUCCESS(248,"regex.pattern.delete.successfully"),
	REGEX_PATTERN_DELETE_FAIL(249,"regex.pattern.delete.fail"),
	DUPLICATE_REGEX_PATTERN_NAME(250,"regex.pattern.name.already.exists"),

	ASCII_PARSER_MAPPING_UPDATE_SUCCESS(251,"ascii.parser.mapping.update.success"),
	
	SNMP_SERVER_UPDATE_SUCCESS(252,"snmp.server.update.successfully"),
	SNMP_SERVER_UPDATE_FAIL(253,"snmp.server.update.fail"),
	SNMP_SERVER_DELETE_SUCCESS(254,"snmp.server.delete.success"),
	SNMP_SERVER_DELETE_FAIL(255,"snmp.server.delete.fail"),
	
	PATHLIST_NOT_FOUND(256,"pathlist.not.found"),
	DUPLICATE_COMPOSER_NAME(257,"composer.name.already.exist"),
	PLUGIN_TYPE_NOT_FOUND(258,"composer.plugin.type.not.found"),
	PLUGIN_NOT_FOUND(259,"composer.plugin.not.found"),
	CHAR_RENAME_PARAM_ADD_SUCCESS(260,"char.rename.param.add.success"),
	CHAR_RENAME_PARAM_ADD_FAIL(261,"char.rename.param.add.fail"),
	CHAR_RENAME_PARAM_UPDATE_SUCCESS(262,"char.rename.param.update.success"),
	CHAR_RENAME_PARAM_UPDATE_FAIL(263,"char.rename.param.update.fail"),
	CHAR_RENAME_PARAM_DELETE_SUCCESS(264,"char.rename.param.delete.success"),
	CHAR_RENAME_PARAM_DELETE_FAIL(265,"char.rename.param.delete.fail"),
	CHAR_RENAME_PARAM_NOT_FOUNT(266,"char.rename.param.get.plugin.id.fail"),
	DELETE_COMPOSER_SUCCESS(267,"delete.composer.success"),
	DELETE_COMPOSER_FAIL(268,"delete.composer.fail"),
	ASCII_COMPOSER_MAPPING_UPDATE_SUCCESS(269,"ascii.composer.mapping.update.success"),
	COMPOSER_ATTRIBUTE_ADD_SUCCESS(270,"composer.attribute.add.success"),
	COMPOSER_ATTRIBUTE_ADD_FAIL(271,"composer.attribute.add.fail"),
	COMPOSER_ATTRIBUTE_UPDATE_SUCCESS(272,"composer.attribute.update.success"),
	COMPOSER_ATTRIBUTE_UPDATE_FAIL(273,"composer.attribute.update.fail"),

	SERVER_INSTANCE_PORT_ACROSS_NOT_UNIQUE_SERVERNAME(274, "server.instance.Port.Not.Unique.Page"),
	SERVER_INSTANCE_INACTIVE_INSERT_SUCCESS(275,"server.instance.inactive.add.success"),
	SNMP_CLIENT_ADDED_SUCCESSFULLY(276,"snmp.client.added.successfully"),
	SNMP_CLIENT_ADDED_FAIL(277,"snmp.client.added.fail"),
	SNMP_CLIENT_UPDATE_SUCCESS(278,"snmp.client.update.success"),
	SNMP_CLIENT_UPDATE_FAIL(279,"snmp.client.update.fail"),
	SNMP_CLIENT_DELETE_SUCCESS(280,"snmp.client.delete.success"),
	SNMP_CLIENT_DELETE_FAIL(281,"snmp.client.delete.fail"),
	SNMP_ALERT_UPDATE_SUCCESS(282,"snmp.alert.update.success"),

	DUPLICATE_SNMP_CLIENT_NAME(283, "snmp.client.name.already.exists"),
	SNMP_SERVICE_THRESHOLD_FAIL(284,"snmp.servicethreshold.update.fail"),

	SNMP_SERVICE_THRESHOLD_SUCCESS(285,"snmp.servicethreshold.update.success"),
	
	TRIAL_LICENSE_ACTIVATE_SUCCESS(286,"trial.license.activate.success"),
	
	TRIAL_LICENSE_ACTIVATE_FAIL(287,"trial.license.activate.fail"),
	
	TRIAL_LICENSE_EXPIRE_SOON(288,"trial.license.expire.alert"),
	
	SM_LICENSE_EXPIRE(289,"sm.license.expired"),

	PRODUCT_CONFIGURATION_UPDATE_SUCCESS(290,"product.configuration.update.success"),
	RESET_PRODUCT_CONFIGURATION_SUCCESS(291,"reset.product.configuration.success"),
	PROFILE_CONFIGURATION_UPDATE_SUCCESS(292,"profile.configuration.update.success"),
	TRIAL_LICENSE_ACTIVATE_ONLY_ONCE(290,"trial.license.activate.only.once"),
	FULL_CREATE_FAIL_SERVERID(291,"full.license.create.fail.serverid"),
	FULL_CREATE_FAIL_INVALID_DATE(292,"full.create.fail.invalid.date"),
	FULL_LICENSE_CREATE_FAIL(293,"full.license.create.fail"),
	FULL_LICENSE_CREATE_SUCCESS(294,"full.license.create.success"),
	FULL_LICENSE_EXPIRE(295,"license.date.expire"),
	FULL_LICENSE_REMINDER(296,"full.license.expire.reminder"),
	TRAIL_LICENSE_REMINDER(297,"trial.license.expire.reminder"),
	LICENSE_FOUND(298,"license.not.expired"),
	TRAIL_LICENSE_ALREADY_EXIST(299,"trail.license.exist"),
	PACKET_SATASTIC_AGENT_UPDATE_SUCCESS(300,"packetStatastic.agent.update.successfully"),

	PACKET_SATASTIC_AGENT_UPDATE_FAIL(301,"packetStatastic.agent.update.fail"),
	SVC_PACKET_SATASTIC_AGENT_NOT_FOUND(302,"service.packetStatastic.agent.not.found"),

	AGENT_UPDATE_SUCCESS(303, "agent.update.successfully"), 
	AGENT_UPDATE_FAIL(304, "agent.updated.fail"),
	DUPLICATE_SERVER_SAME_TYPE(305, "server.already.exists.type"),
	
	STAFF_NOT_FOUND(307,"no.staff.found"),
	STAFF_PASSWORD_RESET_SUCCESS(308,"reset.password.success"),
	STAFF_OLD_PASSWORD_REUSED(309,"old.password.reused"),
	STAFF_PASSWORD_CHANGE_SUCCESS(310,"change.password.success"),
	STAFF_OLD_PASSWORD_INVALID(311,"old.password.invalid"),
	ACCESS_GROUP_UPDATE_SUCCESS(312,"access.group.state.changed.successfully"),
	ACCESS_GROUP_DELETE_SUCCESS(313,"access.group.delete.success"),

	SM_EXCEPTION_GENERIC_MESSAGE(314,"sm.exception.generic.msg"),
	STAFF_PROFILE_PIC_CHANGE_SUCCESS(315,"staff.profile.pic.change.success"),
	STAFF_UPDATE_FAIL(316,"staff.update.fail"),
	STAFF_ACCESSGROPUP_ASSIGN_SUCCESS(317,"staff.access.group.assign.success"),
	FAIL_DOWNLOAD_ENGINE_FILE(318,"fail.engine.file.download"),
	INVALID_LICENSE_FILE(319,"invalid.license.file"),
	
	SERVER_INSTANCE_INACTIVE_COPY(320,"server.instance.inactive.copy.success"),
	SERVER_INSTANCE_INACTIVE_IMPORT(321,"server.instance.inactive.import.success"),
	FAIL_SERVER_INSTANCE_DETAILS(322,"fail.get.instance.details"),
	FAIL_GET_LICENSE_INSTANCE_DETAILS(323,"fail.get.license.instance.details"),
	USER_ACCOUNT_LOCKED(324,"user.account.locked"),
	POLICY_CREATE_SUCCESS(325,"policy.create.success"),
	POLICY_UPDATE_SUCCESS(326,"policy.update.success"),
	POLICY_REMOVE_SUCCESS(327,"policy.remove.success"),
	POLICY_ACTION_INSERT_SUCCESS(325,"policy.action.insert.success"),
	POLICY_ACTION_INSERT_FAIL(326,"policy.action.insert.failed"),
	DUPLICATE_POLICY_ACTION(327,"policy.action.duplicate"),
	POLICY_ACTION_DELETE_SUCCESSFULL(328,"policy.action.delete.success"),
	POLICY_ACTION_DELETE_FAIL(329,"policy.action.delete.failed"),
	NO_POLICY_ACTION_FOUND(330,"policy.action.not.found"),
	POLICY_ACTION_UPDATED_SUCCESSFULL(331,"policy.action.update.success"),
	POLICY_ACTION_UPDATE_FAILED(332,"policy.action.update.failed"),
	FILE_RENAME_AGENT_UPDATE_SUCCESS(333,"file.rename.agent.update.success"),
	FILE_RENAME_AGENT_UPDATE_FAIL(334,"file.rename.agent.update.fail"),
	FILE_RENAME_AGENT_SERVICE_NOT_FOUND(335,"file.rename.agent.service.not.found"),
	FILE_RENAME_AGENT_CONFIGURED_SERVICE_NOT_FOUND(336,"file.rename.agent.configured.service.not.found"),
	SERVICE_FILE_RENAME_AGENT_DELETE_SUCCESS(337,"service.file.rename.agent.deleted.success"),
	SERVICE_FILE_RENAME_AGENT_DELETE_FAIL(338,"service.file.rename.agent.deleted.FAIL"),
	FILE_RENAME_AGENT_ADD_SUCCESS(339,"service.file.rename.agent.add.success"),
	FILE_RENAME_AGENT_ADD_FAIL(340,"service.file.rename.agent.add.fail"),
	ASN1_PARSER_MAPPING_UPDATE_SUCCESS(341,"asn1.parser.mapping.update.success"),
	DUPLICATE_POLICY(342,"policy.name.duplicate"),DATASOURCE_UPDATE_SUCCESS(335, "data.source.update.success"),
	DUPLICATE_DATASOURCE_NAME(343,"data.source.update.fail.duplicate.datasource.name"),
	DATASOURCE_DELETE_SUCCESS(344,"data.source.delete.success"),
	DATASOURCE_DELETE_FAIL(345,"data.source.delete.fail"),
	DATASOURCE_DELETE_FAIL_SERVER_MAPPING_EXISTS(346,"DataSourceConfig.delete.fail.server.mapping.exists"),
	DATASOURCE_ADD_FAIL_DUPLICATE_URL_USERNAME(347,"DataSourceConfig.add.fail.duplicate.url.username"),
	DATASOURCE_ADD_FAIL_DUPLICATE_DATASOURCE_NAME(348,"DataSourceConfig.add.fail.duplicate.datasource.name"),
	NO_SERVICE_AVAILABLE(349,"no.service.available.for.server"),
	NO_SERVER_AVAILABLE(350,"no.server.available.for.service"),
	DATA_SOURCE_INSERT_SUCCESS(351,"data.source.insert.success"),
	DATASOURCE_INSERT_FAIL(352, "Failed to add Server Instance."),
	POLICY_CONDITION_INSERT_SUCCESS(353,"policy.condition.insert.success"),
	POLICY_CONDITION_INSERT_FAIL(354,"policy.condition.insert.failed"),
	DUPLICATE_POLICY_CONDITION(355,"policy.condition.duplicate"),
	POLICY_CONDITION_DELETE_SUCCESSFULL(356,"policy.condition.delete.success"),
	POLICY_CONDITION_DELETE_FAIL(357,"policy.condition.delete.failed"),
	NO_POLICY_CONDITION_FOUND(358,"policy.condition.not.found"),
	POLICY_CONDITION_UPDATED_SUCCESSFULL(359,"policy.condition.update.success"),
	POLICY_CONDITION_UPDATE_FAILED(360,"policy.condition.update.failed"),
	NO_LICENSE_FILE(361,"no.license.file.select"),
	ABOUTUS_NULL_LICENSE_DATA(362,"aboutus.null.license.data"),
	ABOUTUS_NULL_VERSION_WRAPPER_DATA(363,"aboutus.null.version.wrapper.data"),
	ASN1_COMPOSER_MAPPING_UPDATE_SUCCESS(364,"asn1.composer.mapping.update.success"),
	DUPLICATE_POLICY_GROUP(365,"policygroup.name.duplicate"),
	POLICY_GROUP_CREATE_SUCCESS(366,"policygroup.create.success"),
	POLICY_GROUP_UPDATE_SUCCESS(367,"policygroup.update.success"),
	POLICY_GROUP_REMOVE_SUCCESS(368,"policygroup.remove.success"),
	DOZER_MAPPING_FAIL(369,"fail.dozer.convert"),

	MIGRATION_UNMARSHAL_FAIL(370,"fail.migration.instance"),

	DATA_SOURCE_VALIDATION_FAIL(371,"data.source.validation.fail"),
	FIXED_LENGTH_ASCII_PARSER_MAPPING_SUCESS(372,"fixed.length.ascii.parser.mapping.sucess"),
	CONSOLIDATION_MAPPING_DELETE_SUCCESS(373, "consolidation.mapping.delete.success"), 
	CONSOLIDATION_MAPPING_DELETE_FAIL(374, "consolidation.mapping.delete.fail"), 
	CONSOLIDATION_MAPPING_CREATE_SUCCESS(375,"consolidation.mapping.create.success"), 
	CONSOLIDATION_MAPPING_CREATE_FAIL(376,"consolidation.mapping.create.fail"), 
	CONSOLIDATION_MAPPING_UPDATE_SUCCESS(377, "consolidation.mapping.update.success"), 
	CONSOLIDATION_MAPPING_UPDATE_FAIL(378, "consolidation.mapping.update.fail"),	
	DUPLICATE_POLICY_RULE(379,"policyrule.name.duplicate"),
	POLICY_RULE_CREATE_SUCCESS(380,"policyrule.create.success"),
	POLICY_RULE_UPDATE_SUCCESS(381,"policyrule.update.success"),
	POLICY_RULE_REMOVE_SUCCESS(382,"policyrule.remove.success"),
	MIGRATION_INSTANCE_NULL(383,"migration.instance.null"),
	DRIVER_INSTANCE_NULL(384,"driver.instance.null"),
	DISTRIBUTION_PATH_LIST_FAIL(385,"distribution.driver.pathlist.fail"),
	MIGRATION_SERVERINSTANCE_CHECK_FAIL(371,"fail.migration.instance.details.check"),
	SERVERINSTANCE_NOT_RUNNING(372,"serverInstance.not.running"),
	SERVERINSTANCE_SCRIPT_FILE_MISSING(373,"serverInstance.script.file.missing"),
	SERVER_VERSION_NOT_SUPPORTED(374,"server.version.not.supported"),
	SERVERINSTANCE_ALREADY_EXISTS_SAME_PORT(375,"serverInstance.already.exists.same.port"),
	SERVERINSTANCE_ZIP_UPLOAD_FAIL(376,"serverInstance.zip.upload.fail"),

	MIGRATION_XSD_VALIDATION_FAIL(389, "serverInstance.migration.schema.validation.fail"),
	SERVER_VALIDATION_SUCCESS(390, "server.validation.done"),
	SERVERINSTANCE_VALIDATION_SUCCESS(391, "serverInstance.validation.done"),

	XML_PARSER_MAPPING_UPDATE_SUCCESS(392,"xml.parser.mapping.update.success"),
	XML_COMPOSER_MAPPING_UPDATE_SUCCESS(393,"xml.composer.mapping.update.success"),
	COMPOSER_DUPLICATE_ATTRIBUTE_FOUND(394,"composer.attribute.name.already.exist"),
	DRIVER_ATTRIBUTE_ADD_SUCCESS(395,"driver.attribute.add.success"),
	DRIVER_ATTRIBUTE_ADD_FAIL(396,"driver.attribute.add.fail"),
	DRIVER_ATTRIBUTE_UPDATE_SUCCESS(397,"driver.attribute.update.success"),
	DRIVER_ATTRIBUTE_UPDATE_FAIL(398,"driver.attribute.update.fail"),

	DRIVER_DUPLICATE_ATTRIBUTE_FOUND(399,"driver.attribute.name.already.exist"),
	MIGRATION_FAIL(400,"migration.fail"),
	SERVER_GET_IPADDRESS_FAIL(401,"fail.get.server.ipaddress"),
	FAIL_GET_BYTE_DATA(402,"fail.get.byte.data"),
	FAIL_GET_ENTITY_MAPPING_DETAILS(403,"fail.get.entity.mapping.details"),

	FAIL_GET_OPEN_STATUS_INSTANCES(404,"fail.get.open.status.instances"),
	SNMP_VALIDATION_FAIL(405,"snmp.validation.fail"),
	PACKET_STATISTICS_NULL(406,"packet.statistics.found.null"),
	FILE_RENAME_NULL(407,"file.rename.found.null"),
	FAIL_GET_SERVICE(408,"fail.get.service.for.serviceconfigstats"),
	FAIL_CREAT_AGENT(409,"fail.create.agent"),
	SNMP_NOT_FOUND(410,"snmp.not.present"),
	
	CONSOLIDATION_DEFINITION_VIEW_FAIL(411,"consolidation.definition.get.fail"),
	CONSOLIDATION_DEFINITION_CREATE_SUCCESS(412,"consolidation.definition.create.success"),
	CONSOLIDATION_DEFINITION_CREATE_FAIL(413,"consolidation.definition.create.fail"),
	CONSOLIDATION_DEFINITION_UPDATE_SUCCESS(414,"consolidation.definition.update.success"),
	CONSOLIDATION_DEFINITION_UPDATE_FAIL(415,"consolidation.definition.update.fail"),
	CONSOLIDATION_DEFINITION_DELETE_SUCCESS(416,"consolidation.definition.delete.success"),
	CONSOLIDATION_DEFINITION_DELETE_FAIL(417,"consolidation.definition.delete.fail"),

	CONSOLIDATION_DEFINITION_GROUP_VIEW_SUCCESS(418,"consolidation.definition.group.get.success"),
	CONSOLIDATION_DEFINITION_GROUP_VIEW_FAIL(419,"consolidation.definition.group.get.fail"),
	CONSOLIDATION_DEFINITION_GROUP_ADD_SUCCESS(420,"consolidation.definition.group.add.success"),
	CONSOLIDATION_DEFINITION_GROUP_ADD_FAIL(421,"consolidation.definition.group.add.fail"),
	CONSOLIDATION_DEFINITION_GROUP_UPDATE_SUCCESS(422,"consolidation.definition.group.update.success"),
	CONSOLIDATION_DEFINITION_GROUP_UPDATE_FAIL(423,"consolidation.definition.group.update.fail"),

	CONSOLIDATION_DEFINITION_ATTRIBUTE_VIEW_SUCCESS(424,"consolidation.definition.attribute.get.success"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_VIEW_FAIL(425,"consolidation.definition.attribute.get.fail"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_ADD_SUCCESS(426,"consolidation.definition.attribute.add.success"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_ADD_FAIL(427,"consolidation.definition.attribute.add.fail"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_UPDATE_SUCCESS(428,"consolidation.definition.attribute.update.success"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_UPDATE_FAIL(429,"consolidation.definition.attribute.update.fail"),
	CONSOLIDATION_DEFINITION_VIEW_SUCCESS(430,"consolidation.definition.get.success"),
	
	DUPLICATE_QUERY_FOUND(431,"database.query.duplicate.get.failure"),
	DATABASE_QUERY_ADD_SUCESS(432,"database.query.add.sucess"),
	DATABASE_QUERY_ADD_FAILURE(433, "database.query.add.failure"),
	DATABASE_QUERY_UPDATE_SUCCESS(434, "database.query.update.success"),
	DATABASE_QUERY_UPDATE_FAILURE(435, "database.query.update.failure"),
	QUERY_DELETE_SUCCESS(436, "database.query.delete.success"),
	QUERY_DELETE_FAILURE(437,"database.query.delete.failure"),
	
	MIGRATION_TRACK_DETAIL_ADD_SUCCESS(438,"migration.track.detail.add.success"),
	MIGRATION_TRACK_DETAIL_ADD_FAIL(439,"migration.track.detail.add.fail"),
	MIGRATION_TRACK_DETAIL_UPDATE_SUCCESS(440,"migration.track.detail.update.success"),
	MIGRATION_TRACK_DETAIL_UPDATE_FAIL(441,"migration.track.detail.update.fail"),
	MIGRATION_TRACK_DETAIL_DUPLICATE_FOUND(442,"migration.track.detail.duplicate.found"),
	MIGRATION_TRACK_DETAIL_DELETE_SUCCESS(443,"migration.track.detail.delete.success"),
	MIGRATION_NO_SERVER_FOUND_FOR_PRODUCT_TYPE(444,"migration.no.server.found"),
	
	CONSOLIDATION_DEFINITION_DELETE_USED_FAIL(445,"consolidation.definition.delete.used.fail"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_DELETE_SUCCESS(446,"consolidation.definition.attribute.delete.success"),
	CONSOLIDATION_DEFINITION_ATTRIBUTE_DELETE_FAIL(447,"consolidation.definition.attribute.delete.fail"),
	CONSOLIDATION_DEFINITION_GROUP_DELETE_SUCCESS(422,"consolidation.definition.group.delete.success"),
	CONSOLIDATION_DEFINITION_GROUP_DELETE_FAIL(423,"consolidation.definition.group.delete.fail"),
	FAIL_GET_SERVICE_BY_TYPE(424,"fail.get.service.by.type"),
	
	FIXED_LENGTH_ASCII_COMPOSER_MAPPING_UPDATE_SUCCESS(445,"fixed.length.ascii.composer.mapping.success"),
	FAIL_GET_SERVICE_BY_IDS(446, "fail.get.service.by.ids"),
	FAIL_GET_POLICY_RULE(447,"fail.get.policy.rule"),
	XML_CONTENT_NOT_FOUND(448,"xml.content.not.found"),
	FAIL_UPLOAD_REPROCESS(449,"fail.upload.reprocess"),
	SERVICE_REPROCESS_FAIL(450,"fail.service.reprocess"),
	FAIL_DOWNLOAD_FILE(451,"fail.download.file"),

	FAIL_CREATE_CSV_FILE(452,"fail.create.csv.file"),
	FAIL_READ_FILE_DATA(453, "fail.file.read.data"),
	INVALID_FILE_SIZE(454,"invalid.file.size"),
	DUPLICATE_LOOKUP_TABLE_FOUND(455,"lookup.table.duplicate.get.failure"),
	RULE_LOOKUP_FIELD_MORE_THAN_THREE_UNIQUE_FAIL(547,"lookup.field.unique.more.than.three.failure"),
	RULE_LOOKUP_TABLE_ADD_SUCESS(456,"lookup.table.add.success"),
	RULE_LOOKUP_TABLE_DELETE_SUCESS(457,"lookup.table.delete.success"),
	RULE_LOOKUP_TABLE_DELETE_FAILURE(458,"lookup.table.delete.failure"),
	RULE_LOOKUP_TABLE_UPDATE_SUCCESS(459,"lookup.table.update.success"),
	RULE_LOOKUP_TABLE_ADD_FAILURE(460,"lookup.table.add.failure"),
	RULE_LOOKUP_FILE_EMPTY(461,"lookup.file.empty.failure"),
	RULE_LOOKUP_FILE_HEADER_MISMATCH(462,"lookup.file.header.mismatch"),
	RULE_LOOKUP_FILE_DATA_INVALID(463,"lookup.file.data.invalid"),
	RULE_LOOKUP_FILE_READING_ERROR (464, "lookup.file.read.failure"),
	RULE_LOOKUP_FILE_HEADER_LIMIT(465,"lookup.file.header.limit"),
	RULE_LOOKUP_DATA_INSERT_SUCCESS(466,"lookup.file.data.insert.success"),
	RULE_LOOKUP_DATA_UNIQUE_VIOLATION(467,"lookup.file.data.not.unique"),
	RULE_LOOKUP_DATA_UPDATE_FAILURE(468,"lookup.table.update.failure"),
	NO_DS_BY_THIS_TYPE(469,"no.ds.by.this.type"),
	RULE_ACTION_CONDITION_CREATE_SUCCESS(470,"action.condition.create.success"),
	FAIL_RULE_ACTION_CONDITION_CREATE(471,"fail.action.condition.create"),
	RULE_ACTION_CONDITION_UPDATE_SUCCESS(472,"action.condition.update.success"),
	FAIL_RULE_ACTION_CONDITION_UPDATE(473,"fail.action.condition.update"),
	RULE_ACTION_CONDITION_DELETE_SUCCESS(474,"action.condition.delete.success"),
	FAIL_RULE_ACTION_CONDITION_DELETE(475,"fail.action.condition.delete"),
	FAIL_RULE_APPLICATION_ORDER(476,"fail.rule.application.order"),
	SUCCESS_RULE_APPLICATION_ORDER(477,"success.rule.application.order"),
	FAIL_GET_RULE_DETAILS(488,"fail.get.rule.list"),
	FAIL_REVERT_FILES(489,"fail.revert.modified.files"),
	REVERT_MODIFIED_FILE_SUCESS(490,"revert.modified.files.sucess"),
	FAIL_APPLY_RULE_BULK_EDIT(491,"fail.apply.rule.bulk.edit"),
	DATA_CONSOLIDATION_NAME_EXIST(492,"fail.import.dataconsolidation.consName"),
	PARSER_EXISTS_FOR_PATHLIST(493, "Parser.exists.for.pathlist"),
	FOUND_SERVICE_ID_LIST_NULL(494,"service.id.list.null"),
	FAIL_VIEW_FILE(495,"fail.view.file"),
	INVALID_UPLOAD_FILE(496,"invalid.upload.file"),
	FAIL_ERROR_FILE_DOWNLOAD(497,"fail.error.file.download"),
	FAIL_REPROCESS_EDITED_FILE(498,"fail.updated.file.reprocess"),
	FAIL_GET_BATCH_DETAIL_BY_IDS(499, "fail.get.batchDetail.by.ids"),
	FAIL_JMX_DONWLOAD_API(500,"fail.jmx.download.api"),
	NO_SERVICE_FOUND_BY_TYPE(501,"no.service.found.by.type"),
	NO_SERVICE_FOUND_BY_SERVER(501,"no.service.found.by.server"),
	FAIL_UPLOAD_REPROCESS_FILE(502,"fail.upload.reprocess.file"),
	DUPLICATE_UNIFIED_ATTRIBUTE_FOUND(503,"attribute.unified.name.already.exist"),
	SERVER_INSTANCE_IMPORT_FAIL_SERVER_TYPE_DIFFERENT(504,"server.instance.import.fail.different.server.type"),
	FAIL_REPROCESS_MODIFIED_FILES(505,"fail.reprocess.modified.files"),
	POLICY_UNMARSHAL_FAIL(506, "policy.import.upload.fail"),
	DOWNLOAD_FAIL_FILE_NOT_EXIST(507,"download.fail.file.not.exist"),

	DOWNLOAD_FAIL_FILE_CONFIG_WRONG(508,"download.fail.file.config.wrong"),
	RAP_PARSER_MAPPING_UPDATE_SUCCESS(513,"rap.parser.mapping.update.success"),
	TAP_PARSER_MAPPING_UPDATE_SUCCESS(514,"tap.parser.mapping.update.success"),
	NRTRDE_PARSER_MAPPING_UPDATE_SUCCESS(515,"nrtrde.parser.mapping.update.success"),

	AGGREGATION_DEFINITION_ATTRIBUTE_ADD_SUCCESS(509,"aggregation.definition.attribute.add.success"),
	AGGREGATION_DEFINITION_ATTRIBUTE_ADD_FAIL(510,"aggregation.definition.attribute.add.fail"),
	AGGREGATION_DEFINITION_ATTRIBUTE_DELETE_SUCCESS(511,"aggregation.definition.attribute.delete.success"),
	AGGREGATION_DEFINITION_ATTRIBUTE_DELETE_FAIL(512,"aggregation.definition.attribute.delete.fail"),
	AGGREGATION_DEFINITION_ATTRIBUTE_VIEW_SUCCESS(513,"aggregation.definition.attribute.get.success"),
	AGGREGATION_DEFINITION_ATTRIBUTE_VIEW_FAIL(514,"aggregation.definition.attribute.get.fail"),
	AGGREGATION_DEFINITION_ADD_SUCCESS(515,"aggregation.definition.add.success"),
	AGGREGATION_DEFINITION_ADD_FAIL(516,"aggregation.definition.add.fail"),
	AGGREGATION_DEFINITION_NAME_EXIST(517,"aggregation.definition.name.exist"),
	AGGREGATION_DEFINITION_UPDATE_SUCCESS(518,"aggregation.definition.update.success"),
	AGGREGATION_DEFINITION_UPDATE_FAIL(519,"aggregation.definition.update.fail"),
	TAP_COMPOSER_MAPPING_UPDATE_SUCCESS(510,"tap.composer.mapping.update.success"),
	RAP_COMPOSER_MAPPING_UPDATE_SUCCESS(511,"rap.composer.mapping.update.success"),
	NRTRDE_COMPOSER_MAPPING_UPDATE_SUCCESS(512,"nrtrde.composer.mapping.update.success"),
	HOST_CONFIGURATION_INSERT_SUCCESS(516,"host.configuration.insert.success"),
	HOST_CONFIGURATION_UPDATE_SUCCESS(517,"host.configuration.update.success"),
	ROAMING_PARAMETER_UPDATE_SUCCESS(518,"roaming.configuration.update.success"),
	FILE_SEQUENCE_MANAGEMENT_UPDATE_SUCCESS(519,"file.sequence.management.update.success"),
	FILE_SEQUENCE_MANAGEMENT_INSERT_SUCCESS(520,"file.sequence.management.insert.success"),
	TEST_SIM_MANAGEMENT(521,"test.sim.management.update.success"),

	FILE_MANAGEMENT(522,"file.management.update.success"),
	FIXED_LENGTH_BINARY_PARSER_MAPPING_SUCESS(523,"fixed.length.binary.parser.mapping.sucess"),
	PDF_PARSER_MAPPING_SUCESS(613,"pdf.parser.mapping.sucess"),
	COMPOSER_GROUP_ATTRIBUTE_ADD_FAIL(523,"composer.group.attribute.add.fail"),
	COMPOSER_DUPLICATE_GROUP_ATTRIBUTE_FOUND(524,"composer.group.attribute.name.already.exist"),
	PARSER_DUPLICATE_GROUP_ATTRIBUTE_FOUND(525,"Parser.group.attribute.name.already.exist"),
	PARSER_GROUP_ATTRIBUTE_ADD_FAIL(526,"parser.group.attribute.add.fail"),

	FAIL_ZERO_SIZE_FILE_DOWNLOAD(527,"fail.zero.size.file.download"),
	DATA_SOURCE_CONNECTED_SUCCESS(528,"data.source.connected.success"),
	DATA_SOURCE_NOT_CONNECTED(529,"data.source.not.connected"),
	DATASOURCE_CONNECT_FAIL_DUPLICATE_DATASOURCE_NAME(530,"DataSourceConfig.connect.fail.duplicate.datasource.name"),
	RULE_LOOKUP_DATA_EMPTY_OR_NULL_VALUE_IN_ID(531,"lookup.file.data.empty.id.field"),
	RULE_LOOKUP_VIEW_DATA_DELETE_SUCCESS(532,"lookup.table.data.delete.success"), 
	RULE_LOOKUP_VIEW_DATA_DELETE_FAIL(533,"lookup.table.data.delete.failure"),
	RULE_LOOKUP_VIEW_DATA_CREATE_SUCCESS(534,"lookup.table.data.create.success"), 
	RULE_LOOKUP_VIEW_DATA_CREATE_FAIL(535,"lookup.table.data.create.failure"),
	RULE_LOOKUP_VIEW_DATA_UPDATE_SUCCESS(536,"lookup.table.data.update.success"), 
	RULE_LOOKUP_VIEW_DATA_UPDATE_FAIL(537,"lookup.table.data.update.failure"),
	
	DUPLICATE_PEER_NAME(538,"peer.name.already.exists"),
	PEER_ADD_SUCCESS(539, "peer.add.success"),
	PEER_ADD_FAIL(540, "peer.add.fail"),
	PEER_ADD_FAIL_SERV_UNAVAIL(541,"peer.add.fail.serv.unavail"),
	DIAMETER_PEER_UPDATE_SUCCESS(542, "diameter.peer.update.success"),
	DUPLICATE_DIAMETER_PEER_NAME(543, "diameter.peer.name.already.exists"),
	PEER_DELETE_SUCCESS(544, "peer.delete.success"),
	PEER_ACTIVE_DELETE_FAIL(545, "peer.enable.delete.warning.message"),
	PEER_ID_UNAVALIABLE(546, "peer.id.unavaliable"),
	
	AVP_ADD_SUCCESS(547, "avp.add.success"),
	AVP_ADD_FAIL(548, "avp.add.fail"),
	AVP_ADD_FAIL_PEER_UNAVAIL(549,"avp.add.fail.peer.unavail"),
	DIAMETER_AVP_UPDATE_SUCCESS(550, "diameter.avp.update.success"),
	AVP_DELETE_SUCCESS(551,"avp.delete.success"),
	
	RULE_LOOKUP_VIEW_WITHOUT_ANY_FIELD(552,"lookup.table.without.any.field.failure"),
	RULE_LOOKUP_FIELD_ATLEAST_ONE_UNIQUE_FAILURE(553,"lookup.field.unique.atleast.one.failure"),
	
	TRIGGER_ADD_SUCCESS(554,"trigger.add.success"),
	TRIGGER_ADD_FAIL(555,"trigger.add.fail"),
	DUPLICATE_TRIGGER_FOUND(556,"trigger.duplicate.found"),
	TRIGGER_DELETE_SUCESS(557,"trigger.delete.success"),
	TRIGGER_DELETE_FAILURE(558,"trigger.delete.fail"),
	FAIL_GET_TRIGGER_BY_ID(559,"trigger.get.by.id.fail"),
	TRIGGER_UPDATE_SUCCESS(560,"trigger.update.success"),
	TRIGGER_UPDATE_FAILURE(561,"trigger.update.fail"),

	EXPRESSION_VALIDATION_FAILED(562,"processingService.validation.expression.conn.fail"),
	
	AUTO_RELOAD_TABLE_ADD_FAILURE(563,"autoReloadCache.add.failure"),
	AUTO_RELOAD_TABLE_ADD_SUCCESS(564,"autoReloadCache.add.success"),
	AUTO_RELOAD_DELETE_SUCESS(565,"autoReloadCache.delete.success"),
	AUTO_RELOAD_DELETE_FAILURE(566,"autoReloadCache.delete.fail"),
	RULE_LOOKUP_TABLE_GET_SUCCESS(567,"lookup.table.detail.success"),
	RULE_LOOKUP_TABLE_GET_FAIL(568,"lookup.table.detail.fail"),
	AUTO_ERROR_REPROCESSING_SUCCESS(569,"autoErrorReprocessing.add.success"),
	AUTO_ERROR_REPROCESSING_FAILURE(570,"autoErrorReprocessing.add.failure"),
	
	DUPLICATE_AVP(571,"avp.pair.already.exists"),
	
	AUTO_ERROR_REPROCESSING_DELETE_SUCCESS(572,"autoErrorReprocessing.delete.success"),
	AUTO_ERROR_REPROCESSING_DELETE_FAILURE(573,"autoErrorReprocessing.delete.failure"),
	
	AUTO_ERROR_REPROCESSING_UPDATE_SUCCESS(574,"autoErrorReprocessing.update.success"),
	AUTO_ERROR_REPROCESSING_UPDATE_FAILURE(575,"autoErrorReprocessing.update.failure"),
	
	FAIL_GET_AUTO_ERROR_REPROCESS_DETAIL_BY_ID(574,"autoErrorReprocessing.get.by.id.fail"),
	AUTO_UPLOAD_ADD_FAILURE(575,"autoUploadJob.add.failure"),
	AUTO_UPLOAD_ADD_SUCCESS(576,"autoUploadJob.add.success"),
	AUTO_UPLOAD_DELETE_SUCESS(577,"autoUploadJob.delete.success"),
	AUTO_UPLOAD_DELETE_FAILURE(578,"autoUploadJob.delete.fail"),
	AUTO_UPLOAD_UPDATE_SUCESS(579,"autoUploadJob.update.success"),
	AUTO_UPLOAD_UPDATE_FAILURE(580,"autoUploadJob.update.failure"),
	AUTO_RELOAD_UPDATE_SUCESS(581,"autoReloadCache.update.success"),
	AUTO_RELOAD_UPDATE_FAILURE(582,"autoReloadCache.update.failure"),
	AUTO_RELOAD_IMMEDIATE_FAILURE(583,"autoReloadCache.immediate.failure"),
	REST_WEBSERVICE_STATE_CHANGED_SUCCESS(584, "updtInstacne.restwebservice.sts.success"),
	SERVER_INSTANCE_UPDATE_SCRIPT_FAIL(585, "server.instance.update.script.error"),
	SERVER_INSTANCE_SYNC_FAIL_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT(586, "server.instance.sync.fail.empty.storage.path"),
	FORGOT_PASSWORD_LDAP_USER(587, "forgotPassword.contact.system.administrator.error"),
	DUPLICATE_SERVER_SAME_TYPE_CONTAINER(588, "server.already.exists.type.container"),
	DUPLICATE_PROXY_CLIENT(589, "proxyclient.already.exists"),
	CONTAINER_LICENSE_UPGRADE_FAIL(590,"container.license.create.fail"),
	CONTAINER_LICENSE_UPGRADE_SUCCESS(591,"container.license.create.success"),
	
	INVALID_RSA_URL(595,
			"rsa.invalid.url"),RSA_INVALID_OTP(592,
			"rsa.invalid.otp"),RSA_SERVER_USER_NOTCREATED(593,"rsa.user.notcreated"),RSA_SUCCESS(594,"rsa.server.success"),
	
	PARSER_ATTR_UPLOAD_SUCCESS(596,"parser.attribute.file.upload.success"),
	UPLOADED_ATRRIBUTE_FILE_EMPTY(597,"parser.attribute.file.empty.failure"),
	ATRRIBUTE_FILE_UPLOAD_FAILURE(598,"parser.attribute.file.upload.failure"),
	ATRRIBUTE_FILE_HEADER_MISTMATCH(599,"parser.attribute.file.header.mismatch"),
	PARSER_ATTR_VALIDATION_FAIL(600,"parser.attribute.validation.fail"),
	DUPLICATE_UNIFIED_OR_PORT_UNIFIED_ATTRIBUTE_FOUND(601,"attribute.unified.port.unified.name.already.exist"),
	COMPOSER_ATTR_UPLOAD_SUCCESS(602,"composer.attribute.file.upload.success"),
	SYNC_PUBLISH_SERVER_INSTANCE_SUCCESS(603,"server.instance.sync.publish.success"),
	SYNC_PUBLISH_SERVER_INSTANCE_FAIL(604,"server.instance.sync.publish.fail"),
	SYNC_PUBLISH_SERVER_INSTANCE_JMX_CONNECTION_FAIL(605,"server.instance.sync.publish.jmx.connection.fail"),
	SYNC_PUBLISH_SERVER_INSTANCE_JMX_API_FAIL(606,"server.instance.sync.publish.jmx.api.fail"),
	SYNC_PUBLISH_SERVER_INSTANCE_INACTIVE_STATUS_FAIL(607,"server.instance.sync.publish.inactive.status.fail"),
	SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL(608,"server.instance.sync.publish.update.script.fail"),
	SYNC_PUBLISH_SERVER_INSTANCE_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT(609,"server.instance.sync.publish.empty.storage.path.fail"),
	RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_SUCCESS(610,"server.instance.restore.sync.publish.success"),
	RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_FAIL(611,"server.instance.restore.sync.publish.fail"),
	PARSER_GROUP_ATTRIBUTE_UPDATE_SUCCESS(617,"parser.group.attribute.update.success"),
	PARSER_GROUP_ATTRIBUTE_UPDATE_FAIL(618,"parser.group.attribute.update.fail"),
	HTML_PARSER_MAPPING_UPDATE_SUCCESS(612,"html.parser.mapping.update.success"),
	PARSER_GROUP_ATTRIBUTE_DETAIL_UPDATE_SUCCESS(613, "parser.group.attribute.detail.update.success"),
	PARSER_GROUP_ATTRIBUTE_DETAIL_UPDATE_FAIL(614, "parser.group.attribute.detail.update.fail"),
	PARSER_GROUP_ATTRIBUTE_DETAIL_ADD_SUCCESS(	615, "parser.group.attribute.detail.add.success"),
	PARSER_GROUP_ATTRIBUTE_DETAIL_ADD_FAIL(	616, "parser.group.attribute.add.detail.fail"),
	PARSER_PAGE_CONFIG_ADD_SUCCESS(619, "parser.page.add.success"),
	PARSER_PAGE_CONFIG_ADD_FAIL(620, "parser.page.add.fail"),
	PARSER_PAGE_CONFIG_UPDATE_SUCCESS(621, "parser.page.update.success"),
	PARSER_PAGE_CONFIG_UPDATE_FAIL(622, "parser.page.update.fail"),
	XLS_PARSER_MAPPING_UPDATE_SUCCESS(623,"xls.parser.mapping.update.success"),
	PARSER_GROUP_ATTRIBUTE_DELETE_SUCCESS(624,"parser.group.attribute.delete.success"),
	PARSER_GROUP_ATTRIBUTE_DELETE_FAIL(625,"parser.group.attribute.delete.fail"),
	VAR_LENGTH_ASCII_PARSER_MAPPING_UPDATE_SUCCESS(626,"var.length.ascii.parser.mapping.update.success"),
	VAR_LENGTH_ASCII_PARSER_MAPPING_UPLOAD_DATA_FILE_SUCCESS(627,"var.length.ascii.parser.mapping.upload.data.file.success"),
	VAR_LENGTH_ASCII_PARSER_MAPPING_UPLOAD_DATA_FILE_FAIL(628,"var.length.ascii.parser.mapping.upload.data.file.fail"),
	VAR_LENGTH_ASCII_PARSER_MAPPING_DATA_FILE_EXIST(629,"var.length.ascii.parser.mapping.data.file.exist"),
	STAFF_MIGRATION_TO_KEYCLOAK_SUCCESS(630,"staff.migration.keycloak.success"),
	STAFF_MIGRATION_TO_KEYCLOAK_FAIL(631,"staff.migration.keycloak.fail"),
	DICTIONARY_ADD_SUCCESS(632,"dictionary.add.success"),
	DICTIONARY_ADD_FAILURE(633,"dictionary.add.failure"),
	DICTIONARY_FILE_UPLOAD_SYNC_SUCCESS(634,"dictionary.file.upload.sync.success"),
	DICTIONARY_FILE_UPLOAD_SYNC_FAILURE(635,"dictionary.file.upload.sync.failure"),
	DICTIONARY_FILE_UPLOAD_SUCCESS(634,"dictionary.file.upload.success"),
	DICTIONARY_FILE_UPLOAD_FAILURE(635,"dictionary.file.upload.failure"),
	DICTIONARY_FILENAME_MISMATCH_FAILURE(636,"dictionary.filename.mismatch.failure"),
    DRIVER_ATTR_UPLOAD_SUCCESS(637,"driver.attribute.file.upload.success"),
	DICTIONARY_FILE_ADD_SYNC_SUCCESS(638,"dictionary.file.add.sync.success"),
	DICTIONARY_FILE_ADD_SYNC_FAILURE(639,"dictionary.file.add.sync.failure"),
	DICTIONARY_FILE_ADD_SUCCESS(638,"dictionary.file.add.success"),
	DICTIONARY_FILE_ADD_FAILURE(639,"dictionary.file.add.failure"),
	DICTIONARY_SAME_FILENAME_AT_SAMEPATH(640,"dictionary.file.samefileName.samePath"),
	RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL(641,"server.instance.restore.sync.publish.update.script.fail"),
	SERVER_INSTANCE_MAX_LIMIT_ALERT(642,"ServerInstance.max.limit.alert.msg"),
	ASN1_DICTIONARY_FILE_UPLOAD_FAILURE(645,"asn1.dictionary.file.upload.failure"),
	ROLE_CREATION_TO_KEYCLOAK_SUCCESS(646,"staff.migration.keycloak.success"),
	ROLE_CREATION_TO_KEYCLOAK_FAIL(647,"staff.migration.keycloak.fail"),
	FTP_SFTP_TEST_CONNECTION_SUCCESS(648,"ftpSftp.test.connection.success"),
	FTP_SFTP_TEST_CONNECTION_FAILURE(643,"ftpSftp.test.connection.failure"),
	JMX_FAILURE(644,"jmx.connection.failure"),
	KEY_STORE_FILE_UPLOAD_SUCCESS(645,"key.store.file.upload.success"),
	KEY_STORE_FILE_UPLOAD_FAILURE(646,"key.store.file.upload.failure"),
	SERVER_INSTANCE_SYNC_FAIL_DUE_TO_DATASOURCE_DISABLED_FOR_LICENSE(647, "server.instance.sync.fail.datasource.disabled.license"),
	KAFKA_DATASOURCE_ADD_FAIL_DUPLICATE_KAKFA_DATASOURCE_NAME(648,"kafka.datasource.add.fail.duplicate.datasource.name"),
	KAFKA_DATASOURCE_INSERT_SUCCESS(649,"kafka.data.source.insert.success"),
	KAFKA_DATASOURCE_INSERT_FAIL(650, "kafka.data.source.insert.fail"),
	KAFKA_DATASOURCE_UPDATE_SUCCESS(651, "kafka.data.source.update.success"),
	DUPLICATE_KAFKA_DATASOURCE_NAME(652,"kafka.data.source.update.fail.duplicate.datasource.name"),
	KAFKA_DATASOURCE_DELETE_SUCCESS(653,"kafka.data.source.delete.success"),
	KAFKA_DATASOURCE_DELETE_FAIL(654,"kafka.data.source.delete.fail"),
	KAFKA_DATASOURCE_DELETE_FAIL_CLIENT_MAPPING_EXISTS(655,"kafka.data.source.delete.fail.client.mapping.exists"),
	UPDATE_CIRCLE_SUCCESS(656, "circle.update.success"),
	UPDATE_CIRCLE_FAILURE(657, "circle.update.failure"),
	CIRCLE_DELETE_SUCCESS(658, "circle.delete.success"), 
	CIRCLE_DELETE_FAILURE(659, "circle.delete.failure"),
	DUPLICATE_CIRCLE_NAME_FOUND(660,"circle.edit.duplicate.get.failure"),
	ASSOCIATE_CIRCLE_DELETE_FAILURE(659, "circle.delete.failure"),
	KAFKADATASOURCE_ADD_FAIL_DUPLICATE_SERVER_IP_PORT(662,"KafkaDataSourceConfig.add.fail.duplicate.server.ip.port"),
	KAFKADATASOURCE_UPDATE_FAIL_DUPLICATE_SERVER_IP_PORT(663,"KafkaDataSourceConfig.update.fail.duplicate.server.ip.port"),
	SERVER_INSTANCE_SYNC_FAIL_DUE_TO_INVALID_LICENSE(664, "server.instance.sync.fail.device.invalid.license"),	
	LICENSE_DETAILS_SUCCESS(665, "circle.license.key.data.update.success"),
	LICENSE_DETAILS_FAILURE(666, "circle.license.key.data.update.failure"),
	JSON_PARSER_MAPPING_UPDATE_SUCCESS(667,"json.parser.mapping.update.success"),
	GENERATE_JSON_PARSER_ATTRIBUTES_SUCCESS(668, "generate.json.parser.attributes.success"),
	GENERATE_JSON_PARSER_ATTRIBUTES_FAIL(669,"generate.json.parser.attributes.fail"),
	GENERATE_JSON_PARSER_ATTRIBUTES_INVALID_JSON_STRING(670, "generate.json.parser.attributes..invalid.json.string"),
	CIRCLE_CREATE_SUCCESS(671, "circle.create.success"), 
	CIRCLE_CREATE_FAILURE(672, "circle.create.failure"),
	MTSIEMENS_BINARY_PARSER_MAPPING_UPDATE_SUCCESS(673,"mtsiemens.binary.parser.mapping.update.success"),

	// saumil.vachheta
	DETAIL_LOCAL_COMPOSER_MAPPING_UPDATE_SUCCESS(707,"detail.local.parser.mapping.update.success"),	
	DETAIL_LOCAL_PARSER_MAPPING_UPDATE_SUCCESS(708,"detail.local.parser.mapping.update.success");

	
	private final int code;
	private final String description;

	private ResponseCode(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return description;
	}
}