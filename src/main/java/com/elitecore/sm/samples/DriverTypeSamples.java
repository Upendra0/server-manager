package com.elitecore.sm.samples;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.service.DriversService;

/**
 * @author vishal.lakhyani
 *
 */
public class DriverTypeSamples {

	public void addDriverType(DriversService driverService) {

		DriverType driverType = new DriverType();

		// FTP_COLLECTION_DRIVER
		driverType.setType("FTP Collection Driver");
		driverType.setCategory(DriverCategory.COLLECTION);
		driverType.setDescription("FTP Collection Driver");
		driverType.setAlias(EngineConstants.FTP_COLLECTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.collection.FTPCollectionDriver");
		driverService.addDriverType(driverType);

		// SFTP_COLLECTION_DRIVER
		driverType.setType("SFTP Collection Driver");
		driverType.setCategory(DriverCategory.COLLECTION);
		driverType.setDescription("SFTP Collection Driver");
		driverType.setAlias(EngineConstants.SFTP_COLLECTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.collection.SFTPCollectionDriver");
		driverService.addDriverType(driverType);

		// LOCAL_COLLECTION_DRIVER
		driverType.setType("Local Collection Driver");
		driverType.setCategory(DriverCategory.COLLECTION);
		driverType.setDescription("Local Collection Driver");
		driverType.setAlias(EngineConstants.LOCAL_COLLECTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.collection.LocalCollectionDriver");
		driverService.addDriverType(driverType);

		
		driverType.setType("Database Distribution Driver");
		driverType.setCategory(DriverCategory.DISTRIBUTION);
		driverType.setDescription("Database Distribution Driver");
		driverType.setAlias(EngineConstants.DATABASE_DISTRIBUTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.distribution.DatabaseDistributionDriver");
		driverService.addDriverType(driverType);

		driverType.setType("Hadoop Distribution Driver");
		driverType.setCategory(DriverCategory.DISTRIBUTION);
		driverType.setDescription("Hadoop Distribution Driver");
		driverType.setAlias(EngineConstants.HADOOP_DISTRIBUTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.distribution.HadoopDistributionDriver");
		driverService.addDriverType(driverType);

		driverType.setType("Local Distribution Driver");
		driverType.setCategory(DriverCategory.DISTRIBUTION);
		driverType.setDescription("Local Distribution Driver");
		driverType.setAlias(EngineConstants.LOCAL_DISTRIBUTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.distribution.LocalDistributionDriver");
		driverService.addDriverType(driverType);
		
		driverType.setType("FTP Distribution Driver");
		driverType.setCategory(DriverCategory.DISTRIBUTION);
		driverType.setDescription("FTP Distribution Driver");
		driverType.setAlias(EngineConstants.FTP_DISTRIBUTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.distribution.FTPDistributionDriver");
		driverService.addDriverType(driverType);

		driverType.setType("SFTP Distribution Driver");
		driverType.setCategory(DriverCategory.DISTRIBUTION);
		driverType.setDescription("SFTP Distribution Driver");
		driverType.setAlias(EngineConstants.SFTP_DISTRIBUTION_DRIVER);
		driverType.setDriverFullClassName("com.elitecore.sm.drivers.model.distribution.SFTPDistributionDriver");
		driverService.addDriverType(driverType);

	}
}
