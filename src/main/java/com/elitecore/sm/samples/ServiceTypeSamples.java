/**
 * 
 */
package com.elitecore.sm.samples;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.model.ServiceTypeEnum;
import com.elitecore.sm.services.service.ServicesService;

/**
 * @author vandana.awatramani
 *
 */
public class ServiceTypeSamples {

	public void addServicetype(ServicesService servicesService) {
		ServiceType svcType = new ServiceType();
		
		//Collection Service	COLLECTION_SERVICE	Description : Collection Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Collection Service");
		svcType.setDescription("Description : Collection Service collects data from network elements");
		svcType.setAlias(EngineConstants.COLLECTION_SERVICE);	
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.CollectionService");
		servicesService.addServicetype(svcType);
		
		//Parsing Service	PARSING_SERVICE	Description : Parsing Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Parsing Service");
		svcType.setDescription("Description : Parsing Service maps raw format to generalized format.");
		svcType.setAlias(EngineConstants.PARSING_SERVICE);
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.ParsingService");
		servicesService.addServicetype(svcType);

		// Processing Service PROCESSING_SERVICE Description : Processing
		// Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Processing Service");
		svcType.setDescription("Description : Processing Service manipulates data as per rules configured.");
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.ProcessingService");
		svcType.setAlias(EngineConstants.PROCESSING_SERVICE);
		servicesService.addServicetype(svcType);

		// Distribution Service DISTRIBUTION_SERVICE Description : Distribution
		// Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Distribution Service");
		svcType.setDescription("Description : Distribution Service distributes data to downstream systems.");
		svcType.setAlias(EngineConstants.DISTRIBUTION_SERVICE);
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.DistributionService");
		servicesService.addServicetype(svcType);

		//Natflow Collection Service	NATFLOW_COLLECTION_SERVICE	Natflow Collection Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Netflow Collection Service");
		svcType.setDescription("Netflow Collection Service");
		svcType.setAlias(EngineConstants.NATFLOW_COLLECTION_SERVICE);	
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.NetflowCollectionService");
		servicesService.addServicetype(svcType);
		
		//Natflow Binary Collection Service	NATFLOWBINARY_COLLECTION_SERVICE	Natflow Binary Collection Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Netflow Binary Collection Service");
		svcType.setDescription("Netflow Binary Collection Service");
		svcType.setAlias(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE);
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.NetflowBinaryCollectionService");
		servicesService.addServicetype(svcType);
	
		//Data Consolidation Service	DATA_CONSOLIDATION_SERVICE	Data Consolidation Service
		svcType.setTypeOfService(ServiceTypeEnum.ADDITIONAL);
		svcType.setType("Data Consolidation Service");
		svcType.setDescription("Data Consolidation Service");
		svcType.setAlias(EngineConstants.DATA_CONSOLIDATION_SERVICE);
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.DataConsolidationService");
		servicesService.addServicetype(svcType);
		
		//IPLog Parsing Service	IPLOG_PARSING_SERVICE	IPLog Parsing Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("IPLog Parsing Service");
		svcType.setDescription("IPLog Parsing Service");
		svcType.setAlias(EngineConstants.IPLOG_PARSING_SERVICE);
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.IPLogParsingService");
		servicesService.addServicetype(svcType);
		
		//Aggregation Service	AGGREGATION_SERVICE	Aggregation Service
		svcType.setTypeOfService(ServiceTypeEnum.ADDITIONAL);
		svcType.setType("Aggregation Service");
		svcType.setDescription("Aggregation Service");
		svcType.setAlias(EngineConstants.AGGREGATION_SERVICE);	
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.AggregationService");
		servicesService.addServicetype(svcType);
		
		//GTP Prime Collection Service	GTPPRIME_COLLECTION_SERVICE	GTP PRIME COLLECTION SERVICE
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("GTP Prime Collection Service");
		svcType.setDescription("GTP PRIME COLLECTION SERVICE");
		svcType.setAlias(EngineConstants.GTPPRIME_COLLECTION_SERVICE);		
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.GTPPrimeCollectionService");
		servicesService.addServicetype(svcType);
		
		//Syslog Collection Service	SYSLOG_COLLECTION_SERVICE	Syslog Collection Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Syslog Collection Service");
		svcType.setDescription("Syslog Collection Service");
		svcType.setAlias(EngineConstants.SYSLOG_COLLECTION_SERVICE);	
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.SysLogCollectionService");
		servicesService.addServicetype(svcType);
		
		//Correlation Service	CORRELATION_SERVICE	Correlation Service
		svcType.setTypeOfService(ServiceTypeEnum.ADDITIONAL);
		svcType.setType("Correlation Service");
		svcType.setDescription("Correlation Service");
		svcType.setAlias(EngineConstants.CORRELATION_SERVICE);		
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.CorrelationService");
		servicesService.addServicetype(svcType);
		
		//Radius Collection Service	RADIUS_COLLECTION_SERVICE	Radius Collection Service
		svcType.setTypeOfService(ServiceTypeEnum.MAIN);
		svcType.setType("Radius Collection Service");
		svcType.setDescription("Radius Collection Service");
		svcType.setAlias(EngineConstants.RADIUS_COLLECTION_SERVICE);	
		svcType.setServiceFullClassName("com.elitecore.sm.services.model.RadiusCollectionService");
		servicesService.addServicetype(svcType);
		
		
		
	}
}
