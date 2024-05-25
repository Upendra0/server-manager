package com.elitecore.sm.pathlist.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.device.dao.DeviceDao;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.parser.dao.ParserDao;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.pathlist.dao.PathListHistoryDao;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.PathListHistory;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author nilesh.jadav
 *
 */
@org.springframework.stereotype.Service(value = "pathListHistoryService")
public class PathListHistoryServiceImpl implements PathListHistoryService {

	@Inject
	private PathListHistoryDao pathListHistoryDao;

	@Inject
	private ParserDao parserDao;

	@Inject
	private ServicesDao servicesDao;
	
	@Inject
	private DeviceDao deviceDao;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	@Transactional 
	public void save(PathList pathList, Object obj) {
		logger.debug("Inside PathListHistoryServiceImpl->save(PathList pathList, Composer composer)");
		Composer composer = null;
		Parser parser = null;
		if (obj instanceof Composer) {
			composer = (Composer) obj;
		} else if (obj instanceof Parser) {
			parser = (Parser) obj;
		}
		// PathListHistory oldhistory =
		// pathListHistoryDao.getLatestHistoryFromPath(pathList.getId());
		PathListHistory newHistory = preparePathListHistory(pathList, composer, parser);
		// if(oldhistory != null && oldhistory.equals(newHistory)) {
		// logger.debug("old path history found and it is same as new history so skip to insert new history");
		// } else {
		if (newHistory != null) {
			pathListHistoryDao.save(newHistory);
		}
		// }
	}
	
	private PathListHistory preparePathListHistory(PathList pathList, Composer composer, Parser parser) {

		final String DASH = "-";
		final String MEDIATION_SERVER_N_DASH = EngineConstants.MEDIATION_SERVER + DASH;
		
		if (pathList instanceof CollectionDriverPathList) {

			CollectionDriverPathList collectionDriverPathList = (CollectionDriverPathList) pathList;

			if (collectionDriverPathList == null || collectionDriverPathList.getService() == null) {
				return null;
			}
			Service service = servicesDao.getServiceWithServerInstanceById(collectionDriverPathList.getService().getId());
			String serviceId = EngineConstants.COLLECTION_SERVICE + DASH + service.getServInstanceId();
			String serverId = MEDIATION_SERVER_N_DASH + getServerIdByServiceId(collectionDriverPathList.getService().getId());
			String groupServerId = service.getServerInstance().getServer().getGroupServerId();
			Drivers driver = collectionDriverPathList.getDriver();
			String driverId = driver == null ? null : serviceId + DASH + driver.getDriverType().getAlias() + DASH + driver.getApplicationOrder();
			String sourceFilePath = collectionDriverPathList.getReadFilePath();
			String destinationFilePath = collectionDriverPathList.getWriteFilePath();
			String referenceDevice = collectionDriverPathList.getReferenceDevice();
			String deviceName = null;
			Device device = deviceDao.getDevicebyId(collectionDriverPathList.getParentDevice().getId());
			if(device!=null)
				deviceName = device.getName();
			return getPathListHistory(serverId, serviceId, driverId, null, groupServerId, sourceFilePath, destinationFilePath, 
					collectionDriverPathList,deviceName,referenceDevice);
		}

		if (pathList instanceof DataConsolidationPathList) {

			DataConsolidationPathList dataConsolidationPathList = (DataConsolidationPathList) pathList;

			if (dataConsolidationPathList == null || dataConsolidationPathList.getService() == null) {
				return null;
			}
			Service service = servicesDao.getServiceWithServerInstanceById(dataConsolidationPathList.getService().getId());
			String serviceId = EngineConstants.DATA_CONSOLIDATION_SERVICE + DASH + service.getServInstanceId();
			String serverId = MEDIATION_SERVER_N_DASH + dataConsolidationPathList.getService().getServerInstance().getServer().getServerId();
			String groupServerId = service.getServerInstance().getServer().getGroupServerId();
			Drivers driver = dataConsolidationPathList.getDriver();
			String driverId = driver == null ? null : serviceId  + DASH + driver.getDriverType().getAlias() + DASH + driver.getApplicationOrder();
			String sourceFilePath = dataConsolidationPathList.getReadFilePath();
			String destinationFilePath = dataConsolidationPathList.getWriteFilePath();
			String deviceName = dataConsolidationPathList.getReferenceDevice();
			
			return getPathListHistory(serverId, serviceId, driverId, null, groupServerId, sourceFilePath, destinationFilePath, dataConsolidationPathList, deviceName, null);
		}

		if (pathList instanceof ProcessingPathList) {

			ProcessingPathList processingPathList = (ProcessingPathList) pathList;

			if (processingPathList == null || processingPathList.getService() == null) {
				return null;
			}
			String serviceId = EngineConstants.PROCESSING_SERVICE + DASH + processingPathList.getService().getServInstanceId();
			String serverId = MEDIATION_SERVER_N_DASH + processingPathList.getService().getServerInstance().getServer().getServerId();
			String groupServerId = processingPathList.getService().getServerInstance().getServer().getGroupServerId();
			Drivers driver = processingPathList.getDriver();
			String driverId = driver == null ? null : serviceId + DASH + driver.getDriverType().getAlias() + DASH + driver.getApplicationOrder();
			String sourceFilePath = processingPathList.getReadFilePath();
			String destinationFilePath = processingPathList.getWriteFilePath();
			String deviceName = processingPathList.getReferenceDevice();
			String invalidFilePath=null,filterFilePath=null,duplicateFilePath=null;
			if(((ProcessingService)processingPathList.getService()).getFileGroupingParameter().isEnableForInvalid())
				invalidFilePath=((ProcessingService)processingPathList.getService()).getFileGroupingParameter().getInvalidDirPath();
			if(((ProcessingService)processingPathList.getService()).getFileGroupingParameter().isEnableForFilter())
				filterFilePath=((ProcessingService)processingPathList.getService()).getFileGroupingParameter().getFilterDirPath();
			if(((ProcessingService)processingPathList.getService()).getFileGroupingParameter().isEnableForDuplicate())
				duplicateFilePath=((ProcessingService)processingPathList.getService()).getFileGroupingParameter().getDuplicateDirPath();
			
			return getPathListHistory(serverId, serviceId, driverId, null, groupServerId, sourceFilePath, 
					destinationFilePath, processingPathList, deviceName, null,invalidFilePath,filterFilePath,duplicateFilePath);
		}

		if (pathList instanceof ParsingPathList) {

			ParsingPathList parsingPathList = (ParsingPathList) pathList;

			if (parsingPathList == null || parsingPathList.getService() == null) {
				return null;
			}
			
			Service service = servicesDao.getServiceWithServerInstanceById(parsingPathList.getService().getId());
			String serviceId = EngineConstants.PARSING_SERVICE + DASH + service.getServInstanceId();
			String serverId = MEDIATION_SERVER_N_DASH + parsingPathList.getService().getServerInstance().getServer().getServerId();
			String groupServerId = service.getServerInstance().getServer().getGroupServerId();
			Drivers driver = parsingPathList.getDriver();
			String driverId = driver == null ? null : serviceId + DASH + driver.getDriverType().getAlias() + DASH + driver.getApplicationOrder();
			String sourceFilePath = parsingPathList.getReadFilePath();
			String deviceName = parsingPathList.getReferenceDevice();
			String destinationFilePath = null;
			String pluginId = null;
			if (parser != null) {
				destinationFilePath = parser.getWriteFilePath();
				pluginId = parser.getParserType().getAlias() + DASH + parser.getId();
			}
			return getPathListHistory(serverId, serviceId, driverId, pluginId, groupServerId, sourceFilePath,
					destinationFilePath, parsingPathList, deviceName, null);
		}
		if (pathList instanceof DistributionDriverPathList) {

			DistributionDriverPathList distributionDriverPathList = (DistributionDriverPathList) pathList;

			if (distributionDriverPathList == null || distributionDriverPathList.getService() == null) {
				return null;
			}

			Service service = servicesDao.getServiceWithServerInstanceById(distributionDriverPathList.getService().getId());
			String serviceId = EngineConstants.DISTRIBUTION_SERVICE + DASH + service.getServInstanceId();
			String serverId = MEDIATION_SERVER_N_DASH + getServerIdByServiceId(distributionDriverPathList.getService().getId());
			String groupServerId = service.getServerInstance().getServer().getGroupServerId();
			Drivers driver = distributionDriverPathList.getDriver();
			String driverId = driver == null ? null : serviceId + DASH + driver.getDriverType().getAlias() + DASH + driver.getApplicationOrder();
			String sourceFilePath = distributionDriverPathList.getReadFilePath();
			String deviceName = distributionDriverPathList.getReferenceDevice();
			String destinationFilePath = null;
			String pluginId = null;
			if (composer != null) {
				destinationFilePath = composer.getDestPath();
				if(service!=null && service instanceof DistributionService){
					DistributionService distSvc = (DistributionService) service;
					if(distSvc.isThirdPartyTransferEnabled())
						pluginId = "DEFAULT_COMPOSER_PLUGIN";
					else
						pluginId = composer.getComposerType().getAlias() + DASH + composer.getId();
				}
			}
			return getPathListHistory(serverId, serviceId, driverId, pluginId, groupServerId, sourceFilePath, 
					destinationFilePath, distributionDriverPathList, deviceName, null);
		}
		if (pathList instanceof AggregationServicePathList) {

			AggregationServicePathList aggServicePathList = (AggregationServicePathList) pathList;

			if (aggServicePathList == null || aggServicePathList.getService() == null) {
				return null;
			}
			Service service = servicesDao.getServiceWithServerInstanceById(aggServicePathList.getService().getId());
			String serviceId = EngineConstants.AGGREGATION_SERVICE + DASH +service.getServInstanceId();
			String serverId = MEDIATION_SERVER_N_DASH + aggServicePathList.getService().getServerInstance().getServer().getServerId();
			String groupServerId = service.getServerInstance().getServer().getGroupServerId();
			String sourceFilePath = aggServicePathList.getReadFilePath();
			String deviceName = aggServicePathList.getReferenceDevice();
			
			return getPathListHistory(serverId, serviceId, null, null, groupServerId, sourceFilePath, null, aggServicePathList, deviceName, null);
		}

		return null;
	}

	private PathListHistory getPathListHistory(String serverId, String serviceId, String driverId,
			String pluginId, String groupServerId, String sourceFilePath, String destinationFilePath, PathList pathList, String deviceName, String referenceDevice) {

		PathListHistory pathListHistory = new PathListHistory();
		pathListHistory.setServiceId(serviceId);
		pathListHistory.setServerId(serverId);
		pathListHistory.setDriverId(driverId);
		pathListHistory.setPluginId(pluginId);
		pathListHistory.setGroupServerId(groupServerId);
		pathListHistory.setSourceFilePath(sourceFilePath);
		pathListHistory.setDestinationFilePath(destinationFilePath);
		pathListHistory.setPathList(pathList);
		pathListHistory.setPathId(pathList.getPathId());
		pathListHistory.setNetworkElement(deviceName);
		pathListHistory.setRefNetworkElement(referenceDevice);
		pathListHistory.setLastUpdatedByStaffId(pathList.getLastUpdatedByStaffId());
		pathListHistory.setCreatedByStaffId(pathList.getLastUpdatedByStaffId());
		
		if(pathList instanceof AggregationServicePathList){
			AggregationServicePathList aggPathList = (AggregationServicePathList) pathList;
			pathListHistory.setAggrDestPath(aggPathList.getWriteFilePath());
			pathListHistory.setNonAggrDestPath(aggPathList.getwPathNonAggregate());
			pathListHistory.setErrAggrDestPath(aggPathList.getwPathAggregateError());
		}
		return pathListHistory;
	}
	
	private PathListHistory getPathListHistory(String serverId, String serviceId, String driverId,
			String pluginId, String groupServerId, String sourceFilePath, String destinationFilePath, PathList pathList, String deviceName, String referenceDevice,String invalidFilePath,String filterFilePath,String duplicateFilePath) {

		PathListHistory pathListHistory = new PathListHistory();
		pathListHistory.setServiceId(serviceId);
		pathListHistory.setServerId(serverId);
		pathListHistory.setDriverId(driverId);
		pathListHistory.setPluginId(pluginId);
		pathListHistory.setGroupServerId(groupServerId);
		pathListHistory.setSourceFilePath(sourceFilePath);
		pathListHistory.setDestinationFilePath(destinationFilePath);
		pathListHistory.setPathList(pathList);
		pathListHistory.setPathId(pathList.getPathId());
		pathListHistory.setNetworkElement(deviceName);
		pathListHistory.setRefNetworkElement(referenceDevice);
		pathListHistory.setLastUpdatedByStaffId(pathList.getLastUpdatedByStaffId());
		pathListHistory.setCreatedByStaffId(pathList.getLastUpdatedByStaffId());
		pathListHistory.setInvalidFilePath(invalidFilePath);
		pathListHistory.setFilterFilePath(filterFilePath);
		pathListHistory.setDuplicateFilePath(duplicateFilePath);
		
		return pathListHistory;
	}

	private String getServerIdByServiceId(int serviceId) {
		Service service = servicesDao.getServiceWithServerInstanceById(serviceId);
		return service.getServerInstance().getServer().getServerId();
	}
}
