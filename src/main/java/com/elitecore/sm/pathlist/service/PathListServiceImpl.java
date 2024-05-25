package com.elitecore.sm.pathlist.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.dao.ComposerDao;
import com.elitecore.sm.composer.dao.ComposerMappingDao;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.service.CharRenameOperationService;
import com.elitecore.sm.composer.service.ComposerAttributeService;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.consolidationservice.dao.DataConsolidationMappingDao;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.consolidationservice.service.IDataConsolidationService;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.fileseqmgmt.dao.FileSequenceMgmtDao;
import com.elitecore.sm.license.dao.CircleDao;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.parser.dao.ParserDao;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.ParserWrapperDao;
import com.elitecore.sm.parser.dao.PluginTypeDao;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.service.ParserAttributeService;
import com.elitecore.sm.parser.service.ParserGroupAttributeService;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.service.RegExParserService;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.CommonPathList;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.FileSequenceMgmt;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;

/**
 * 
 * @author avani.panchal
 *
 */
@org.springframework.stereotype.Service(value = "pathListService")
public class PathListServiceImpl implements PathListService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ComposerAttributeService composerAttributeService;

	@Autowired
	ServicesService servicesService;

	@Autowired
	IPolicyDao policyDao;

	@Autowired
	DriversService driversService;

	@Autowired
	PathListDao pathListDao;

	@Autowired
	PathListValidator pathListValidator;

	@Autowired
	ServicesDao serviceDao;

	@Autowired
	PluginTypeDao pluginTypeDao;

	@Autowired
	ParserDao parserDao;

	@Autowired
	ParserService parserService;

	@Autowired
	ParserWrapperDao wrapperDao;

	@Autowired
	ParserMappingDao parserMappingDao;

	@Autowired
	ParserMappingService parserMappingService;

	@Autowired
	ComposerMappingService composerMappingService;

	@Autowired
	ComposerService composerService;

	@Autowired
	CharRenameOperationService charRenameOperationService;

	@Autowired
	DataConsolidationMappingDao dataConsolidationMappingDao;

	@Autowired
	IDataConsolidationService dataConsolidationService;

	@Autowired
	ComposerDao composerDao;

	@Autowired
	ComposerMappingDao composerMappingDao;

	@Autowired
	FileSequenceMgmtDao fileSequenceMgmtDao;

	@Autowired
	DeviceService deviceService;

	@Autowired
	ParserAttributeService parserAttributeService;

	@Autowired
	ParserGroupAttributeService parserGroupAttributeService;

	@Autowired
	private RegExParserService regExParserService;

	@Autowired
	private CircleDao circleDao;

	private String duplicatePathNameConstant = "Found duplicate path list name ";

	/**
	 * Add pathList into database
	 */
	@Override
	@Transactional
	public ResponseObject addPathList(PathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		if (pathList.getDriver() != null) {
			Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
			if (driver != null) {
				pathList.setDriver(driver);
				String maxPathId = getMaxPathListId(driver);
				pathList.setPathId(maxPathId);
			}
		}
		pathListDao.save(pathList);
		if (pathList.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
			responseObject.setObject(pathList);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
		}

		return responseObject;
	}

	/**
	 * Update pathList
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PATHLIST, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject updatePathList(PathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		pathListDao.merge(pathList);

		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_SUCCESS);
		responseObject.setObject(pathList);

		return responseObject;
	}

	/**
	 * Add Collection PathList in DB
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject createPathList(PathList pathList) {

		ResponseObject responseObject = new ResponseObject();

		int pathListCount = pathListDao.getPathListCountByNameAndService(pathList.getName(),
				getServiceId(pathList.getService()), getDriverId(pathList.getDriver()));
		if (pathListCount > 0) {
			logger.debug("inside createPathList : duplicate pathList name found:" + pathList.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);

		} else {
			pathList.setLastUpdatedDate(new Date());
			if (pathList instanceof CollectionDriverPathList) {

				pathList = (CollectionDriverPathList) pathList;
				boolean isDuplicate = false;
				Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
				if (((CollectionDriverPathList) pathList).getFileSeqAlertEnabled()
						&& ((CollectionDriverPathList) pathList).getMissingFileSequenceId() != null) {
					isDuplicate = fileSequenceMgmtDao.validateDuplicateDeviceName(
							((CollectionDriverPathList) pathList).getMissingFileSequenceId());
					if (isDuplicate) {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DUPLICATE_REFERENCE_DEVICE_NAME);
					}
				}

				if (!isDuplicate) {
					if (driver != null) {
						pathList.setDriver(driver);
						responseObject = this.addPathList(pathList);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
					}
				}
			}
		}
		return responseObject;

	}

	/**
	 * Fetch List of pathList from DB
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getPathListByDriverId(int driverId) {

		ResponseObject responseObject = new ResponseObject();
		List<PathList> pathList = pathListDao.getPathListByDriverId(driverId);

		if (pathList != null && !pathList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(pathList);
		} else {
			responseObject.setSuccess(false);
		}

		return responseObject;

	}

	/**
	 * check pathlist name is unique , then update pathlist
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PATHLIST, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject updatePathListDetail(PathList pathList) {
		ResponseObject responseObject = new ResponseObject();

		if (isPathListUniqueForUpdate(getServiceId(pathList.getService()), getDriverId(pathList.getDriver()),
				pathList.getName())) {
			pathList.setLastUpdatedDate(new Date());

			Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
			FileSequenceMgmt seqMgmt = null;
			if (pathList instanceof CollectionDriverPathList) {
				seqMgmt = ((CollectionDriverPathList) pathList).getMissingFileSequenceId();
			}
			boolean isDuplicate = false;
			if (pathList instanceof CollectionDriverPathList) {
				// when FileSeq Tracking is turning OFF on this incoming Request.
				// we always check for FileSeqMgnt object from db based on path-id(to rule out
				// driver combination)

				if (!((CollectionDriverPathList) pathList).getFileSeqAlertEnabled()) {
					CollectionDriverPathList pathStored = null;
					List<PathList> pList = pathListDao.getParsingPathListbyId(pathList.getId());
					if (!pList.isEmpty() && pList.size() > 0) {

						pathStored = (CollectionDriverPathList) pList.get(0);
						logger.debug("path stored = " + pList + ", missing file seq = " + pathStored.getMissingFileSequenceId());

						if (pathStored != null && pathStored.getMissingFileSequenceId() != null) {
							
								logger.debug("file sequence tracking is enabled on the path. need to mark as deleted.");
								
								if (fileSequenceMgmtDao.updateStatusById(pathStored.getMissingFileSequenceId().getId(),
										StateEnum.DELETED, pathStored.getReferenceDevice())) {
									
									logger.debug("Status for id: " + pathStored.getMissingFileSequenceId().getId()
											+ " is updated to " + StateEnum.DELETED.getValue());
									((CollectionDriverPathList) pathList).setMissingFileSequenceId(null);
								}
						} else {
							((CollectionDriverPathList) pathList).setMissingFileSequenceId(null);
						}

					}
				}
			}
			

			if (seqMgmt != null && ((CollectionDriverPathList) pathList).getFileSeqAlertEnabled()) {
				isDuplicate = fileSequenceMgmtDao.validateDuplicateDeviceName(seqMgmt);
				if (isDuplicate) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.DUPLICATE_REFERENCE_DEVICE_NAME);
				}
			}
			if (!isDuplicate) {
				if (driver != null) {
					pathList.setDriver(driver);
					responseObject = this.updatePathList(pathList);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_FAIL);
				}
			}
		} else {
			logger.debug("inside updatePathListDetail : duplicate driver name found in update:" + pathList.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	/**
	 * Check path list is exist with same name or not , in case of update
	 * 
	 * @param serviceId
	 * @param driverId
	 * @param pathListName
	 * @return boolean
	 */
	@Transactional(readOnly = true)
	public boolean isPathListUniqueForUpdate(int serviceId, int driverId, String pathListName) {
		int count = pathListDao.getPathListCountByNameAndService(pathListName, serviceId, driverId);
		if (count > 1) {
			return false;
		}
		return true;
	}

	/**
	 * Check pathlist is exist with same name or not , in case of update
	 * 
	 * @param pathListId
	 * @param pathListName
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isPathListUnique(String pathListName) {

		List<PathList> pathlistList = pathListDao.getPathListByName(pathListName);
		boolean isUnique;
		if (pathlistList != null && !pathlistList.isEmpty()) {
			isUnique = false;
		} else { // No pathList found with same name
			isUnique = true;
		}
		return isUnique;
	}

	/**
	 * Method will delete driver path list details.
	 * 
	 * @param pathListId
	 * @param staffId
	 * @see com.elitecore.sm.pathlist.service.PathListService#deletePathListDetails(java.lang.String,
	 *      java.lang.String)
	 * @return ResponseObject
	 * @throws CloneNotSupportedException
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_PATHLIST, actionType = BaseConstants.DELETE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject deletePathListDetails(int id, int staffId, boolean isProcessing)
			throws CloneNotSupportedException {
		ResponseObject responseObject = new ResponseObject();
		if (isProcessing) {
			ProcessingPathList processingPathList = (ProcessingPathList) pathListDao.findByPrimaryKey(PathList.class,
					id);
			if (processingPathList != null) {
				processingPathList
						.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, processingPathList.getName()));
				processingPathList.setStatus(StateEnum.DELETED);
				processingPathList.setLastUpdatedByStaffId(staffId);
				processingPathList.setLastUpdatedDate(new Date());
				Policy policy = processingPathList.getPolicy();
				if (policy != null) {
					List<ProcessingPathList> processingPathsList = policy.getProcessingPathList();
					if (processingPathsList != null) {
						processingPathsList.remove(processingPathList);
						policy.setProcessingPathList(processingPathsList);
						processingPathList.setPolicy(null);
					}
					policyDao.merge(policy);
				}
				pathListDao.merge(processingPathList);

				responseObject.setSuccess(true);
				responseObject.setObject(processingPathList);
				responseObject.setResponseCode(ResponseCode.PATHLIST_DELETE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_DELETE_FAIL);
			}
		} else {
			PathList pathList = pathListDao.findByPrimaryKey(PathList.class, id);
			if (pathList != null) {

				if (pathList instanceof CollectionDriverPathList
						&& ((CollectionDriverPathList) pathList).getMissingFileSequenceId() != null) {

					String deletedReferenceDeviceName = EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,
							((CollectionDriverPathList) pathList).getReferenceDevice());

					((CollectionDriverPathList) pathList).getMissingFileSequenceId()
							.setReferenceDevice(deletedReferenceDeviceName);
					((CollectionDriverPathList) pathList).getMissingFileSequenceId().setStatus(StateEnum.DELETED);
					((CollectionDriverPathList) pathList).getMissingFileSequenceId().setLastUpdatedByStaffId(staffId);
					((CollectionDriverPathList) pathList).getMissingFileSequenceId().setLastUpdatedDate(new Date());
					((CollectionDriverPathList) pathList).setReferenceDevice(deletedReferenceDeviceName);
				}
				pathList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, pathList.getName()));
				pathList.setStatus(StateEnum.DELETED);
				pathList.setLastUpdatedByStaffId(staffId);
				pathList.setLastUpdatedDate(new Date());

				pathListDao.merge(pathList);

				responseObject.setSuccess(true);
				responseObject.setObject(pathList);
				responseObject.setResponseCode(ResponseCode.PATHLIST_DELETE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_DELETE_FAIL);
			}
		}
		return responseObject;
	}

	/**
	 * Validate pathlist parameter for import operation
	 * 
	 * @param pathList
	 * @param pathImportErrorList
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<ImportValidationErrors> validatePathListForImport(PathList pathList,
			List<ImportValidationErrors> pathImportErrorList) {

		pathListValidator.validatePathListForImport(pathList, pathImportErrorList);
		if (pathList instanceof DistributionDriverPathList) {

			DistributionDriverPathList distributionDriverPathList = (DistributionDriverPathList) pathList;
			List<Composer> composerList = distributionDriverPathList.getComposerWrappers();
			if (composerList != null && !composerList.isEmpty()) {
				logger.debug("Found " + composerList.size() + " plugin for path list "
						+ distributionDriverPathList.getName());

			}
		}

		return pathImportErrorList;
	}

	/**
	 * Add path-list and parser plug-in for iplog parsing service in database
	 * 
	 * @param parserWrapper
	 * @param serviceId
	 * @return Add path-list response as response object
	 */
	@Override
	@Transactional
	public ResponseObject addIpLogParsingPathList(Parser parser, int serviceId) {

		ResponseObject responseObject = new ResponseObject();

		int pathListCount = pathListDao.getPathListCountByNameAndService(parser.getParsingPathList().getName(),
				serviceId, 0);

		if (pathListCount <= 0) {

			if (parserService.isParserUnique(parser.getName())) {
				Service service = serviceDao.getAllServiceDepedantsByServiceId(serviceId);

				ParsingPathList pathlist = parser.getParsingPathList();
				pathlist.setPathId("000");
				pathlist.setService(service);

				PluginTypeMaster pluginType = pluginTypeDao.getPluginByType(parser.getParserType().getAlias());
				parser.setParserType(pluginType);

				// for wrapper set parser plug-in
				parser.setParsingPathList(pathlist);

				List<Parser> liWrapper = new ArrayList<>();
				liWrapper.add(parser);
				pathlist.setParserWrappers(liWrapper);
				PathListService pathServiceImple = (PathListService) SpringApplicationContext
						.getBean("pathListService"); // getting spring bean for aop context issue.
				responseObject = pathServiceImple.createIpLogPathListAndParser(pathlist);

			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_PARSER_NAME);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}

		return responseObject;
	}

	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_IPLOG_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = PathList.class, ignorePropList = "driver,service")
	public ResponseObject createIpLogPathListAndParser(ParsingPathList pathlist) {
		ResponseObject responseObject = new ResponseObject();
		// save whole path-list including parser-wrapper and parser plug-in
		pathListDao.evict(pathlist);
		pathListDao.save(pathlist);

		if (pathlist.getId() != 0) {
			responseObject.setObject(pathlist.getParserWrappers().get(0));
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
		}

		return responseObject;
	}

	/**
	 * Update path-list and parser plug-in in db.
	 * 
	 * @param parserWrapper
	 * @param serviceId
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_IPLOG_PATHLIST, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Parser.class, ignorePropList = "parserMapping,parsingPathList")
	public ResponseObject updateIpLogParsingPathList(Parser parser, int serviceId) {

		ResponseObject responseObject = new ResponseObject();
		boolean pathListUpdated = false;

		if (isPathListUniqueForUpdate(serviceId, 0, parser.getParsingPathList().getName())) {

			if (parserService.isParserUniqueForUpdate(parser.getId(), parser.getName())) {
				Service service = serviceDao.getAllServiceDepedantsByServiceId(serviceId);
				List<ParsingPathList> pathlist = pathListDao.getParsingPathListByServiceId(serviceId);

				for (int index = 0; index < pathlist.size(); index++) {

					ParsingPathList parsingPathList = pathlist.get(index);
					// if this path-list is same as passed from JSP
					if (parser.getParsingPathList().getId() == parsingPathList.getId()) {
						parsingPathList.setName(parser.getParsingPathList().getName());
						parsingPathList.setReadFilePath(parser.getParsingPathList().getReadFilePath());
						parsingPathList.setService(service);

						PluginTypeMaster pluginType = pluginTypeDao.getPluginByType(parser.getParserType().getAlias());

						parser.setParsingPathList(parsingPathList);// Setting path-list object to parser.
						parser.setParserType(pluginType);
						List<Parser> liWrapper = new ArrayList<>();
						liWrapper.add(parser);
						parsingPathList.setParserWrappers(liWrapper);

						if (parser.getParserMapping() != null) {
							if (parser.getParserMapping().getId() != 0) {
								ParserMapping parserMapping = parserMappingDao.findByPrimaryKey(ParserMapping.class,
										parser.getParserMapping().getId());
								parser.setParserMapping(parserMapping);
							} else {
								parser.setParserMapping(null);
							}
						}

						// save whole path-list including parser wrapper and parser plug-in
						pathListDao.evict(parsingPathList);
						pathListDao.merge(parsingPathList);
						pathListUpdated = true;

						responseObject.setObject(parser);
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_SUCCESS);
					}
				}

				if (!pathListUpdated) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_PARSER_NAME);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	@Override
	public void validateWrapperForImport(Parser wrapper, List<ImportValidationErrors> importErrorList) {

		Service service = wrapper.getParsingPathList().getService();
		if (service instanceof IPLogParsingService) {
			pathListValidator.validateParserWrapperForIPLogParsing(wrapper, null, BaseConstants.PARSERWRAPPER, true,
					importErrorList);
		} else if (service instanceof ParsingService) {
			pathListValidator.validatePathListParams(wrapper.getParsingPathList(), null, importErrorList,
					BaseConstants.PATHLIST, true);
		}
	}

	/**
	 * Iterate over path list dependents , change id and name for import operation
	 * 
	 * @param pathlist
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject iterateOverParsingPathListConfig(ParsingPathList pathlist, boolean isImport, int importMode) {
		ResponseObject responseObject = new ResponseObject();

		if (pathlist != null) {

			List<Parser> wrapperList = pathlist.getParserWrappers();

			if (wrapperList != null) {
				Parser parser;
				for (int i = 0; i < wrapperList.size(); i++) {
					parser = wrapperList.get(i);
					if (isImport) {
						logger.debug("Import parser details.");
						parser.setId(0);
						if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
							parser.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, parser.getName()));
						}
						parser.setParsingPathList(pathlist);
						parser.setLastUpdatedByStaffId(parser.getLastUpdatedByStaffId());
						parser.setLastUpdatedDate(new Date());

						parserMappingService.importParserMappingAndDependents(parser, isImport, importMode);
					} else {
						parser.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, parser.getName()));
						parser.setStatus(StateEnum.DELETED);
						parser.setParserMapping(null);
					}
				}
				pathlist.setParserWrappers(wrapperList);
			} else {
				logger.debug("Wrapper list not configured for pathlist");
			}
		}

		responseObject.setSuccess(true);
		responseObject.setObject(pathlist);

		return responseObject;
	}

	public void importParsingPathListUpdateMode(ParsingPathList dbPathList, ParsingPathList exportedPathList) {
		logger.debug("going to update parsers in path list : " + dbPathList.getName());
		List<Parser> dbParserList = dbPathList.getParserWrappers();
		List<Parser> exportedParserList = exportedPathList.getParserWrappers();
		if (!CollectionUtils.isEmpty(exportedParserList)) {
			int length = exportedParserList.size();
			for (int i = length - 1; i >= 0; i--) {
				Parser exportedParser = exportedParserList.get(i);
				updatePluginTypeByType(exportedParser);
				if (exportedParser != null && !exportedParser.getStatus().equals(StateEnum.DELETED)) {
					Parser finalParser = exportedParser;
					Parser dbParser = parserService.getParserFromList(dbParserList,
							exportedParser.getParserType().getAlias(), exportedParser.getName());
					if (dbParser != null) {
						logger.debug("going to update parser : " + dbParser.getName());
						finalParser = dbParser;
						ParserMapping dbParserMapping = dbParser.getParserMapping();
						if (dbParserMapping != null) {
							List<Parser> parserList = parserMappingDao
									.getMappingAssociationDetails(dbParserMapping.getId());
							if (parserList != null && !parserList.isEmpty()) {
								int accociatedParserLength = parserList.size();
								/*
								 * if(accociatedParserLength == 1) {
								 * logger.debug("going to update parser : "+dbParser.getName()
								 * +", mapping associated with 1 parser"); }
								 */if (exportedParser.getParserType().getAlias()
										.equalsIgnoreCase(dbParser.getParserType().getAlias())) {
									if (exportedParser.getName().equals(dbParser.getName())) {
										logger.debug("going to update parser");
									}

								} else if (accociatedParserLength > 1) {
									logger.debug("going to clone parser from parser : " + dbParser.getName()
											+ ", parser mapping associated with more than 1 parser");
									try {
										finalParser = (Parser) dbParser.clone();
										logger.debug("going to update cloned parser from exported parser : "
												+ exportedParser.getName());
									} catch (CloneNotSupportedException e) {
										logger.error("Clone not supported", e);
									}
								}
							} else {
								logger.debug(
										"going to update parser from db parser mapping, mapping is not associated with any parser");
							}
						} else {
							logger.debug("going to update parser : " + dbParser.getName());
						}
						parserService.importParserForUpdateMode(finalParser, exportedParser);
					} else {
						logger.debug("going to add parser :" + finalParser.getName());
						finalParser.setId(0);
						finalParser.setParsingPathList(dbPathList);
						finalParser.setLastUpdatedDate(EliteUtils.getDateForImport(false));
						parserMappingService.importParserMappingForAddAndKeepBothMode(finalParser, dbPathList,
								BaseConstants.IMPORT_MODE_UPDATE);
					}
					dbParserList.add(finalParser);
				} else {
					logger.debug("parser not found in exported parser list");
				}
			}
		}
	}

	public void importParsingPathListAddMode(ParsingPathList dbPathList, ParsingPathList exportedPathList) {
		logger.debug("going to update parsers in path list : " + dbPathList.getName());
		List<Parser> dbParserList = dbPathList.getParserWrappers();
		List<Parser> exportedParserList = exportedPathList.getParserWrappers();
		if (!CollectionUtils.isEmpty(exportedParserList)) {
			int length = exportedParserList.size();
			for (int i = length - 1; i >= 0; i--) {
				Parser exportedParser = exportedParserList.get(i);
				updatePluginTypeByType(exportedParser);
				if (exportedParser != null && !exportedParser.getStatus().equals(StateEnum.DELETED)) {
					Parser finalParser = exportedParser;
					Parser dbParser = parserService.getParserFromList(dbParserList,
							exportedParser.getParserType().getAlias(), exportedParser.getName());
					if (dbParser != null) {
						logger.debug("going to update parser : " + dbParser.getName());
						finalParser = dbParser;
						ParserMapping dbParserMapping = dbParser.getParserMapping();
						if (dbParserMapping != null) {
							List<Parser> parserList = parserMappingDao
									.getMappingAssociationDetails(dbParserMapping.getId());
							if (parserList != null && !parserList.isEmpty()) {
								int accociatedParserLength = parserList.size();
								if (exportedParser.getParserType().getAlias()
										.equalsIgnoreCase(dbParser.getParserType().getAlias())) {
									if (exportedParser.getName().equals(dbParser.getName())) {
										logger.debug("going to update parser");
									}
								} else if (accociatedParserLength > 1) {
									logger.debug("going to clone parser from parser : " + dbParser.getName()
											+ ", parser mapping associated with more than 1 parser");
									try {
										finalParser = (Parser) dbParser.clone();
										logger.debug("going to update cloned parser from exported parser : "
												+ exportedParser.getName());
									} catch (CloneNotSupportedException e) {
										logger.error("Clone not supported", e);
									}
								}
							} else {
								logger.debug(
										"going to update parser from db parser mapping, mapping is not associated with any parser");
							}
						} else {
							logger.debug("going to update parser : " + dbParser.getName());
						}
						parserService.importParserForAddMode(finalParser, exportedParser);
						dbParserList.add(finalParser);
					} else if (CollectionUtils.isEmpty(dbParserList)) {
						logger.debug("going to add parser when there is no parse already available in database:"
								+ finalParser.getName());
						finalParser.setId(0);
						finalParser.setParsingPathList(dbPathList);
						finalParser.setLastUpdatedDate(EliteUtils.getDateForImport(false));
						parserMappingService.importParserMappingForAddAndKeepBothMode(finalParser, dbPathList,
								BaseConstants.IMPORT_MODE_UPDATE);
						dbParserList.add(finalParser);
					}
				} else {
					logger.debug("parser not found in exported parser list");
				}
			}
		}
	}

	public void importParsingPathListAddAndKeepBothMode(ParsingPathList exportedParsingPathList, int importMode) {
		logger.debug("going to add parsers for path list : " + exportedParsingPathList.getName());
		List<Parser> exportedParserList = exportedParsingPathList.getParserWrappers();
		if (!CollectionUtils.isEmpty(exportedParserList)) {
			int length = exportedParserList.size();
			for (int i = length - 1; i >= 0; i--) {
				Parser exportedParser = exportedParserList.get(i);
				updatePluginTypeByType(exportedParser);
				if (exportedParser != null && !exportedParser.getStatus().equals(StateEnum.DELETED)) {

					exportedParser.setId(0);
					exportedParser.setParsingPathList(exportedParsingPathList);
					exportedParser.setLastUpdatedDate(EliteUtils.getDateForImport(false));

					if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
						logger.debug("going to add parser keepboth mode : " + exportedParser.getName());
						exportedParser.setName(
								EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedParser.getName()));
						parserMappingService.importParserMappingForAddAndKeepBothMode(exportedParser,
								exportedParsingPathList, importMode);
					} else {
						ParserMapping exportedParserMapping = exportedParser.getParserMapping();
						ParserMapping dbParserMapping = getDbMappingFromExportedMapping(exportedParserMapping);
						if (importMode == BaseConstants.IMPORT_MODE_UPDATE) {
							if (dbParserMapping != null) {
								List<Parser> associatedParserList = parserMappingDao
										.getMappingAssociationDetails(dbParserMapping.getId());
								if (!CollectionUtils.isEmpty(associatedParserList)) {
									int associatedParserLength = associatedParserList.size();
									Parser dbParser = associatedParserList.get(0);
									if (associatedParserLength == 1) {
										logger.debug("going to update parser : " + dbParser.getName()
												+ " , associated with 1 parser");
										parserService.importParserForUpdateMode(dbParser, exportedParser);
										exportedParserList.set(i, dbParser);
									} else if (associatedParserLength > 1) {
										logger.debug("going to clone parser from parser : " + dbParser.getName()
												+ " , associated with more than 1 parser");
										try {
											Parser cloneParser = (Parser) dbParser.clone();
											cloneParser.setParsingPathList(exportedParsingPathList);
											logger.debug("going to update clone parser from exported parser : "
													+ exportedParser.getName());
											parserService.importParserForUpdateMode(cloneParser, exportedParser);
											exportedParserList.set(i, cloneParser);
										} catch (CloneNotSupportedException e) {
											logger.error("Clone not supported", e);
										}
									}
								} else {
									logger.debug(
											"going to add parser from db mapping, mapping is not associated with any parser. parser name : "
													+ exportedParser.getName());
									parserMappingService.importParserMappingForAddAndKeepBothMode(exportedParser,
											exportedParsingPathList, importMode);
								}
							} else {
								logger.debug("going to add parser, parser mapping both. parser name : "
										+ exportedParser.getName());
								parserMappingService.importParserMappingForAddAndKeepBothMode(exportedParser,
										exportedParsingPathList, importMode);
							}
						} else if (importMode == BaseConstants.IMPORT_MODE_ADD) {
							if (dbParserMapping == null) {
								logger.debug("going to add parser for add mode : " + exportedParser.getName());
								parserMappingService.importParserMappingForAddAndKeepBothMode(exportedParser,
										exportedParsingPathList, importMode);
							} else if (EngineConstants.ASN1_PARSING_PLUGIN
									.equals(exportedParser.getParserType().getAlias())) {
								exportedParser.setParserMapping(dbParserMapping);
							} else {
								List<ParserAttribute> exportedParserAttr = exportedParserMapping.getParserAttributes();
								for (int epa = 0; epa < exportedParserAttr.size(); epa++) {
									parserAttributeService.createParserAttributes(exportedParserAttr.get(epa),
											dbParserMapping.getId(), dbParserMapping.getParserType().getAlias(), 1);
								}
								dbParserMapping.setParserAttributes(dbParserMapping.getParserAttributes());
								exportedParser.setParserMapping(dbParserMapping);
							}
						} else if (importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
							if (dbParserMapping == null) {
								logger.debug("going to add parser for add mode : " + exportedParser.getName());
								parserMappingService.importParserMappingForAddAndKeepBothMode(exportedParser,
										exportedParsingPathList, importMode);
							} else {
								if (EngineConstants.REGEX_PARSING_PLUGIN
										.equals(exportedParserMapping.getParserType().getAlias())) {
									ResponseObject responseObject = verifyAndCreateDeviceDetails(exportedParserMapping);
									if (responseObject.isSuccess()) {
										logger.debug(
												"parserMapping is type of RegexParserMapping , going to delete patterns and attributes");
										List<Integer> regexPatternIds = new ArrayList<>();
										RegexParserMapping regexParserMapping = (RegexParserMapping) dbParserMapping;
										if (regexParserMapping.getPatternList() != null
												&& !regexParserMapping.getPatternList().isEmpty()) {
											List<RegExPattern> regExPatternObj = regexParserMapping.getPatternList();
											for (RegExPattern regExPattern : regExPatternObj) {
												regexPatternIds.add(regExPattern.getId());
											}
											for (Integer regexPatternId : regexPatternIds) {
												regExParserService.deleteRegExPattern(regexPatternId, 1);
											}
										}
										RegexParserMapping mapping = (RegexParserMapping) exportedParserMapping;
										List<RegExPattern> patternList = mapping.getPatternList();
										if (patternList != null && !patternList.isEmpty()) {
											logger.debug("Iterating patter list.");
											for (RegExPattern regExPattern : patternList) {
												regExParserService.importRegexPatternForAddAndKeepBothMode(regExPattern,
														regexParserMapping, importMode);
											}
										} else {
											logger.debug("No patten found for regex parser details");
										}
										regexParserMapping.setPatternList(patternList);
										exportedParser.setParserMapping(regexParserMapping);
									}
								} else {
									logger.debug("going to add new parser with db parser mapping. parser name : "
											+ exportedParser.getName() + " : parser mapping name : "
											+ dbParserMapping.getName());
									parserMappingService.updateParserMappingBasicParameters(dbParserMapping,
											exportedParserMapping);
									// step 1 :: delete attributes
									String attrIds = new String();
									List<ParserAttribute> parserAttr = dbParserMapping.getParserAttributes();
									for (int pa = 0; pa < parserAttr.size(); pa++) {
										attrIds += parserAttr.get(pa).getId() + ",";
									}
									parserAttributeService.deleteParserAttributes(attrIds.toString(), 1);

									if (!(EngineConstants.TAP_PARSING_PLUGIN
											.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.RAP_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.PDF_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.HTML_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.XLS_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())
											|| EngineConstants.NRTRDE_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias()))) {
										// step 2 :: Add new attributes
										List<ParserAttribute> exportedParserAttr = exportedParserMapping
												.getParserAttributes();
										for (int epa = 0; epa < exportedParserAttr.size(); epa++) {
											exportedParserAttr.get(epa)
													.setParserMapping(exportedParser.getParserMapping());
											parserAttributeService.createParserAttributes(exportedParserAttr.get(epa),
													dbParserMapping.getId(), dbParserMapping.getParserType().getAlias(),
													1);
										}
										// dbParserMapping.setParserAttributes(exportedParserAttr);
										exportedParser.setParserMapping(dbParserMapping);
									} else if (EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN
											.equals(dbParserMapping.getParserType().getAlias())) {
										dbParserMapping
												.setParserAttributes(exportedParserMapping.getParserAttributes());
										parserAttributeService.importParserAttribute(dbParserMapping);
										exportedParser.setParserMapping(dbParserMapping);
									} else {
										String groupAttrIds = new String();
										List<ParserGroupAttribute> parserGroupAttrList = dbParserMapping
												.getGroupAttributeList();
										if (parserGroupAttrList != null && !parserGroupAttrList.isEmpty()) {
											for (int pa = 0; pa < parserGroupAttrList.size(); pa++) {
												groupAttrIds += parserGroupAttrList.get(pa).getId();
												if (pa != parserGroupAttrList.size() - 1) {
													groupAttrIds += ",";
												}
												if (EngineConstants.PDF_PARSING_PLUGIN
														.equals(dbParserMapping.getParserType().getAlias())) {
													parserGroupAttributeService.deleteGroupAttributeWithHierarchy(
															parserGroupAttrList.get(pa).getId(),
															dbParserMapping.getLastUpdatedByStaffId(),
															dbParserMapping.getId());
												}
											}
											if (!EngineConstants.PDF_PARSING_PLUGIN
													.equals(dbParserMapping.getParserType().getAlias())) {
												parserGroupAttributeService.deleteGroupAttributes(groupAttrIds,
														dbParserMapping.getLastUpdatedByStaffId(),
														dbParserMapping.getId());
											}

										}
										dbParserMapping
												.setParserAttributes(exportedParserMapping.getParserAttributes());
										dbParserMapping
												.setGroupAttributeList(exportedParserMapping.getGroupAttributeList());
										parserAttributeService.importParserAttribute(dbParserMapping);
										exportedParser.setParserMapping(dbParserMapping);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Method will verify and create device details.
	 * 
	 * @param parserMapping
	 * @return
	 */
	private ResponseObject verifyAndCreateDeviceDetails(ParserMapping parserMapping) {
		ResponseObject responseObject;
		if (parserMapping.getDevice().getIsPreConfigured() == BaseConstants.SYSTEM_DEFINED_DEVICE) {
			responseObject = deviceService.getDeviceByName(parserMapping.getDevice().getName());
			if (responseObject.isSuccess()) {
				logger.debug("Found already created device  with name  " + parserMapping.getDevice().getName());
				parserMapping.setDevice((Device) responseObject.getObject());
				responseObject.setSuccess(true);
			}
		} else {
			logger.debug("Failed to get device  with name " + parserMapping.getDevice().getName()
					+ " so creating new device.");
			responseObject = deviceService.verifyAndCreateDeviceDetails(parserMapping.getDevice(),
					parserMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_KEEP_BOTH);
			if (responseObject.isSuccess()) {
				parserMapping.setDevice((Device) responseObject.getObject());
			}
		}
		return responseObject;
	}

	public void updatePluginTypeByType(Parser parser) {
		if (parser != null) {
			parser.setParserType(pluginTypeDao.getPluginByType(parser.getParserType().getAlias()));
			if (parser.getParserMapping() != null) {
				parser.getParserMapping().setParserType(
						pluginTypeDao.getPluginByType(parser.getParserMapping().getParserType().getAlias()));
			}
		}
	}

	/**
	 * Get parsing path list with parser list for service
	 * 
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getParsingPathListByServiceId(int serviceId) {

		ResponseObject responseObject = new ResponseObject();
		JSONArray jPathList = new JSONArray();
		JSONObject jPathDetail;
		JSONArray jWrapperList;
		List<ParsingPathList> pathlistdetail = pathListDao.getParsingPathListByServiceId(serviceId);

		if (pathlistdetail != null && !pathlistdetail.isEmpty()) {

			for (PathList path : pathlistdetail) {

				if (!path.getStatus().equals(StateEnum.DELETED)) {

					ParsingPathList parsingPath = (ParsingPathList) path;
					jPathDetail = new JSONObject();
					logger.debug("Traverse Path List =" + parsingPath.getName());
					jPathDetail.put("name", parsingPath.getName());
					jPathDetail.put(BaseConstants.READ_PATH,
							Utilities.replaceBackwardSlash(parsingPath.getReadFilePath()));
					jPathDetail.put("id", parsingPath.getId());
					jPathDetail.put("fileGrepDateEnabled",
							parsingPath.getFileGrepDateEnabled() != null ? parsingPath.getFileGrepDateEnabled()
									: "false");
					jPathDetail.put("dateFormat",
							(parsingPath.getDateFormat() != null && !parsingPath.getDateFormat().isEmpty())
									? parsingPath.getDateFormat()
									: "yyyyMMddHHmm");
					jPathDetail.put("startIndex",
							parsingPath.getStartIndex() != null ? parsingPath.getStartIndex() : 0);
					jPathDetail.put("endIndex", parsingPath.getEndIndex() != null ? parsingPath.getEndIndex() : 0);
					jPathDetail.put("position",
							(parsingPath.getPosition() != null && !parsingPath.getPosition().isEmpty())
									? parsingPath.getPosition()
									: "left");
					jPathDetail.put("pathId", parsingPath.getPathId());
					jPathDetail.put("referenceDeviceName", parsingPath.getReferenceDevice());
					jPathDetail.put("circle", parsingPath.getCircle().getId());
					jPathDetail.put("mandatoryFields", parsingPath.getMandatoryFields());
					if (parsingPath.getParentDevice() != null) {
						jPathDetail.put("deviceId", parsingPath.getParentDevice().getId());
						jPathDetail.put("deviceName", parsingPath.getParentDevice().getName());
					} else {
						jPathDetail.put("deviceId", "");
						jPathDetail.put("deviceName", "");
					}
					jWrapperList = new JSONArray();

					List<Parser> liParserList = parsingPath.getParserWrappers();

					if (liParserList != null && !liParserList.isEmpty()) {

						for (Parser wrapper : liParserList) {

							if (!wrapper.getStatus().equals(StateEnum.DELETED)) {

								List<PluginTypeMaster> pluginList = (List<PluginTypeMaster>) MapCache
										.getConfigValueAsObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);

								for (PluginTypeMaster pluginType : pluginList) {
									if (pluginType.getId() == wrapper.getParserType().getId()) {
										wrapper.setParserType(pluginType);
									}
								}

								JSONObject parser = new JSONObject();
								parser.put("id", wrapper.getId());
								parser.put("name", wrapper.getName());
								parser.put("parserTypeId", wrapper.getParserType().getId());
								parser.put("parserType", wrapper.getParserType().getType());
								parser.put("parserTypeAlias", wrapper.getParserType().getAlias());
								parser.put("fileNamePattern", wrapper.getFileNamePattern());
								parser.put("readFilenamePrefix", wrapper.getReadFilenamePrefix());
								parser.put("readFilenameSuffix", wrapper.getReadFilenameSuffix());
								parser.put("readFilenameContains", wrapper.getReadFilenameContains());
								parser.put("readFilenameExcludeTypes", wrapper.getReadFilenameExcludeTypes());
								parser.put("compressInFileEnabled", wrapper.isCompressInFileEnabled());
								parser.put("compressOutFileEnabled", wrapper.isCompressOutFileEnabled());
								parser.put("writeFilePath", Utilities.replaceBackwardSlash(wrapper.getWriteFilePath()));
								parser.put("writeFilenamePrefix", wrapper.getWriteFilenamePrefix());
								parser.put("writeFileSplit", wrapper.isWriteFileSplit());
								parser.put("writeCdrHeaderFooterEnabled",wrapper.isWriteCdrHeaderFooterEnabled());
								parser.put("writeCdrDefaultAttributes", wrapper.isWriteCdrDefaultAttributes());
								if (wrapper.getParserMapping() != null) {
									parser.put("parserMappingId", wrapper.getParserMapping().getId());
								} else {
									parser.put("parserMappingId", "0");
								}
								jWrapperList.put(parser);
							}
						}
						jPathDetail.put("parserWrapper", jWrapperList);
					} else {
						jPathDetail.put("parserWrapper", new JSONArray());
					}
					jPathList.put(jPathDetail);
				}
			}
		}
		responseObject.setSuccess(true);
		responseObject.setObject(jPathList);
		logger.debug("Final JSON Response = " + jPathList);

		return responseObject;
	}

	/**
	 * Method will get all distribution path list and plug-in list.
	 * 
	 * @param driverId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getDistributionPathListAndPluginDetails(int driverId, String driverTypeAlias) {

		logger.debug("Fetching all distribution driver pathlist details with its associated plugin details for driver "
				+ driverTypeAlias);
		ResponseObject responseObject;

		responseObject = driversService.getDistributionDriverByIdAndType(driverId, driverTypeAlias);
		if (responseObject.isSuccess()) {
			logger.debug("Driver object found successfully now going fetch its dependents.");
			Drivers driver = (Drivers) responseObject.getObject();
			if (driver != null) {
				responseObject = iterateDistributionPathListDetails(driver.getDriverPathList());
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DRIVER_NOT_FOUND);
				responseObject.setObject(null);
			}

		} else {
			logger.info("Failed to get driver with id " + driverId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DRIVER_NOT_FOUND);
			responseObject.setObject(null);
		}
		return responseObject;
	}

	/**
	 * Method will iterate and make Json object for distribution pathlist and plugin
	 * details.
	 * 
	 * @param distributionDriverPathList
	 * @return
	 */
	public ResponseObject iterateDistributionPathListDetails(List<PathList> distributionDriverPathList) {
		ResponseObject responseObject = new ResponseObject();
		JSONArray pathListArrayObj = new JSONArray();
		JSONObject pathListJsonObj;
		JSONArray composerArrayObj;

		if (distributionDriverPathList != null && !distributionDriverPathList.isEmpty()) {

			for (PathList pathList : distributionDriverPathList) {

				if (StateEnum.ACTIVE.equals(pathList.getStatus())) {
					DistributionDriverPathList distributionPathList = (DistributionDriverPathList) pathList;
					pathListJsonObj = new JSONObject();
					composerArrayObj = new JSONArray();
					createDistributionDriverPathlistJsonObject(pathListJsonObj, distributionPathList); // Method will
																										// add all
																										// require
																										// details to
																										// JSON object.
					crateComposerPluginJsonObject(distributionPathList.getComposerWrappers(), pathListJsonObj,
							composerArrayObj);
					pathListArrayObj.put(pathListJsonObj);
				}
			}
			responseObject.setSuccess(true);
			responseObject.setObject(pathListArrayObj);
			logger.debug(" JSON Response = " + pathListArrayObj);
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Method will add distribution path list details to jsonObject
	 * 
	 * @param pathListJsonObj
	 * @param distributionPathList
	 */
	private void createDistributionDriverPathlistJsonObject(JSONObject pathListJsonObj,
			DistributionDriverPathList distributionPathList) {
		pathListJsonObj.put("id", distributionPathList.getId());
		pathListJsonObj.put("name", distributionPathList.getName());

		pathListJsonObj.put(BaseConstants.READ_PATH,
				Utilities.replaceBackwardSlash(distributionPathList.getReadFilePath()));
		pathListJsonObj.put("maxFileCountAlert", distributionPathList.getMaxFilesCountAlert());
		pathListJsonObj.put("isReadCompressEnable", distributionPathList.isCompressInFileEnabled());
		pathListJsonObj.put("fileNamePattern", distributionPathList.getFileNamePattern());
		pathListJsonObj.put(BaseConstants.READ_FILE_PREFIX, distributionPathList.getReadFilenamePrefix());
		pathListJsonObj.put(BaseConstants.READ_FILE_SUFFIX, distributionPathList.getReadFilenameSuffix());
		pathListJsonObj.put("fileNameContains", distributionPathList.getReadFilenameContains());
		pathListJsonObj.put("excludeFileType", distributionPathList.getReadFilenameExcludeTypes());

		pathListJsonObj.put("writePath", Utilities.replaceBackwardSlash(distributionPathList.getWriteFilePath()));
		pathListJsonObj.put("isWriteCompressEnable", distributionPathList.isCompressOutFileEnabled());
		pathListJsonObj.put("fileGrepDateEnabled",
				distributionPathList.getFileGrepDateEnabled() != null ? distributionPathList.getFileGrepDateEnabled()
						: "false");
		pathListJsonObj.put("dateFormat",
				(distributionPathList.getDateFormat() != null && !distributionPathList.getDateFormat().isEmpty())
						? distributionPathList.getDateFormat()
						: "yyyyMMddHHmm");
		pathListJsonObj.put("startIndex",
				distributionPathList.getStartIndex() != null ? distributionPathList.getStartIndex() : 0);
		pathListJsonObj.put("endIndex",
				distributionPathList.getEndIndex() != null ? distributionPathList.getEndIndex() : 0);
		pathListJsonObj.put("position",
				(distributionPathList.getPosition() != null && !distributionPathList.getPosition().isEmpty())
						? distributionPathList.getPosition()
						: "left");
		pathListJsonObj.put("pathId", distributionPathList.getPathId());
		pathListJsonObj.put("dbReadFileNameExtraSuffix", distributionPathList.getDbReadFileNameExtraSuffix());

		pathListJsonObj.put("referenceDeviceName", distributionPathList.getReferenceDevice());
		if (distributionPathList.getParentDevice() != null && distributionPathList.getParentDevice().getId() > 0) {
			pathListJsonObj.put("deviceId", distributionPathList.getParentDevice().getId());
			pathListJsonObj.put("deviceName", distributionPathList.getParentDevice().getName());
		} else {
			pathListJsonObj.put("deviceId", "");
			pathListJsonObj.put("deviceName", "");
		}
	}

	/**
	 * Method will create new composer plug-in JSON Object
	 * 
	 * @param composerPluginList
	 * @param pathListJsonObj
	 * @param composerArrayObj
	 */
	@SuppressWarnings("unchecked")
	private void crateComposerPluginJsonObject(List<Composer> composerPluginList, JSONObject pathListJsonObj,
			JSONArray composerArrayObj) {
		logger.debug("Adding plugin details to json object");
		if (composerPluginList != null && !composerPluginList.isEmpty()) {

			for (Composer composer : composerPluginList) {
				if (StateEnum.ACTIVE.equals(composer.getStatus())) {

					List<PluginTypeMaster> composerTypeList = (List<PluginTypeMaster>) MapCache
							.getConfigValueAsObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);

					for (PluginTypeMaster composerType : composerTypeList) {
						if (composerType.getId() == composer.getComposerType().getId()) {
							composer.setComposerType(composerType);
						}
					}

					JSONObject composerObj = new JSONObject();

					composerObj.put("id", composer.getId());
					composerObj.put("name", composer.getName());
					composerObj.put("pluginTypeId", composer.getComposerType().getId());
					composerObj.put("pluginTypeAlias", composer.getComposerType().getAlias());
					composerObj.put("pluginType", composer.getComposerType().getType());
					composerObj.put("writeFilenamePrefix", composer.getWriteFilenamePrefix());
					composerObj.put("writeFilenameSuffix", composer.getWriteFilenameSuffix());

					composerObj.put("destPath", Utilities.replaceBackwardSlash(composer.getDestPath()));

					composerObj.put("fileBackupPath", composer.getFileBackupPath());
					composerObj.put("fileExtension", composer.getFileExtension());
					composerObj.put("defaultFileExtensionRemoveEnabled",
							composer.isDefaultFileExtensionRemoveEnabled());
					composerObj.put("fileSplitEnabled", composer.isFileSplitEnabled());
					composerObj.put("characterRename", "[]");
					if (composer.getComposerMapping() != null) {
						composerObj.put("composerMappingId", composer.getComposerMapping().getId());
					} else {
						composerObj.put("composerMappingId", "0");
					}
					composerArrayObj.put(composerObj);
				}
			}
			pathListJsonObj.put("composerWrapper", composerArrayObj);
		} else {
			pathListJsonObj.put("composerWrapper", new JSONArray());
		}
	}

	/**
	 * Add parsing pathlist in database
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_PARSING_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject addParsingPathList(ParsingPathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		int pathListCount = pathListDao.getPathListCountByNameAndService(pathList.getName(),
				getServiceId(pathList.getService()), getDriverId(pathList.getDriver()));

		if (pathListCount <= 0) {
			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());

			if (service != null) {
				pathList.setService(service);
				String maxPathId = getMaxPathListId(service);
				pathList.setPathId(maxPathId);
				pathListDao.save(pathList);

				if (pathList.getId() != 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
					responseObject.setObject(pathList);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	/**
	 * Update parsing path list in database
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSING_PATHLIST, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PathList.class, ignorePropList = "pathListHistoryList")
	public ResponseObject updateParsingPathList(ParsingPathList pathList) {

		ResponseObject responseObject = new ResponseObject();
		if (isPathListUniqueForUpdate(getServiceId(pathList.getService()), getDriverId(pathList.getDriver()),
				pathList.getName())) {
			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());

			if (service != null) {
				pathList.setService(service);

				pathListDao.merge(pathList);

				responseObject.setObject(pathList);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	/**
	 * Method will delete driver path list details.
	 * 
	 * @param pathListId
	 * @param staffId
	 * @see com.elitecore.sm.pathlist.service.PathListService#deletePathListDetails(java.lang.String,
	 *      java.lang.String)
	 * @return ResponseObject
	 * @throws CloneNotSupportedException
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_PARSING_PATHLIST, actionType = BaseConstants.DELETE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject deleteParsingPathListDetails(int id, int staffId) throws SMException {
		return deleteCommonPathListDetails(id, staffId);
	}

	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_IPLOG_PATHLIST, actionType = BaseConstants.DELETE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject deleteIpLogParsingPathListDetails(int id, int staffId) throws SMException {
		return deleteCommonPathListDetails(id, staffId);
	}

	public ResponseObject deleteCommonPathListDetails(int id, int staffId) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		try {
			List<PathList> pathListDetail = pathListDao.getParsingPathListbyId(id);
			if (pathListDetail != null && !pathListDetail.isEmpty()) {
				PathList pathList = pathListDetail.get(0);
				responseObject.setAuditOldObject(pathList.clone());
				pathList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, pathList.getName()));
				pathList.setStatus(StateEnum.DELETED);
				pathList.setLastUpdatedByStaffId(staffId);
				pathList.setLastUpdatedDate(new Date());

				if (pathList instanceof ParsingPathList) {
					logger.debug("Parsing PathList found");
					List<Parser> parserList = ((ParsingPathList) pathList).getParserWrappers();
					if (parserList != null) {
						int size = parserList.size();
						if (size > 0) {
							for (int i = 0; i < size; i++) {
								logger.debug("PathList is type of Parsing PathList , so delete Parser also"
										+ parserList.get(i).getId());
								Parser parser = parserDao.findByPrimaryKey(Parser.class, parserList.get(i).getId());
								parser.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, parser.getName()));
								parser.setStatus(StateEnum.DELETED);
								parser.setParserMapping(null);
							}
						}
					}

				} else if (pathList instanceof DistributionDriverPathList) {
					logger.debug("Found distribution driver pathlist object.");
					List<Composer> composerList = ((DistributionDriverPathList) pathList).getComposerWrappers();

					if (composerList != null && !composerList.isEmpty()) {
						int size = composerList.size();
						for (int i = 0; i < size; i++) {
							logger.debug("PathList is type of Distribution PathList , so delete Composer also"
									+ composerList.get(i).getId());
							composerService.deleteComposerPluginAndItsDependents(composerList.get(i).getId(), staffId);
						}
					}
				}

				pathListDao.merge(pathList);

				responseObject.setAuditOldObject(pathList);
				responseObject.setSuccess(true);
				responseObject.setObject(pathList);
				responseObject.setResponseCode(ResponseCode.PATHLIST_DELETE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_DELETE_FAIL);
			}
		} catch (Exception e) {
			logger.error("Exception Occured:" + e);
			throw new SMException(e.getMessage());
		}
		return responseObject;
	}

	/**
	 * Method will create new distribution driver pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = PathList.class, ignorePropList = "")
	public ResponseObject addDistributionDriverPathList(DistributionDriverPathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Adding distrubiton driver pathlist details.");

		int pathListCount = pathListDao.getPathListCountByNameAndService(pathList.getName(),
				getServiceId(pathList.getService()), getDriverId(pathList.getDriver()));

		if (pathListCount <= 0) {
			Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
			if (driver != null) {
				pathList.setDriver(driver);
				String maxPathId = getMaxPathListId(driver);
				pathList.setPathId(maxPathId);
				pathListDao.save(pathList);
				if (pathList.getId() != 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
					responseObject.setObject(pathList);
					logger.info("Distribution path list details added successfully.");
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
					logger.info("Failed to add distribution path list details.");
				}
			} else {
				logger.info("Driver object not found so failed to add distribution pathlist.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DRIVER_NOT_FOUND);
			}
		} else {
			logger.info(duplicatePathNameConstant + pathList.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	@Override
	public String getMaxPathListId(Drivers driver) {
		return increasePathId(pathListDao.getDriverMaxPathId(driver.getId()));
	}

	public String increasePathId(String pathId) {
		String maxPathId;
		if (org.apache.commons.lang.StringUtils.isNotEmpty(pathId)) {
			try {
				int maxPathIdInt = Integer.parseInt(pathId);
				maxPathIdInt++;
				maxPathId = String.format("%03d", maxPathIdInt);
			} catch (NumberFormatException e) {
				maxPathId = "000";
			}
		} else {
			maxPathId = "000";
		}
		return maxPathId;
	}

	/**
	 * Fetch parsing pathlist using service id
	 * 
	 * @param serviceId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getParsingPathListUsingServiceId(int serviceId) {

		ResponseObject responseObject = new ResponseObject();

		List<ParsingPathList> parsingPathList = pathListDao.getParsingPathListByServiceId(serviceId);

		if (parsingPathList != null && !parsingPathList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(parsingPathList);
		} else {
			responseObject.setSuccess(false);
		}

		return responseObject;

	}

	/**
	 * Method will import or delete pathlist driver details and its dependents if
	 * found.
	 * 
	 * @param driver
	 * @param isImport
	 * @return
	 */
	@Override
	public ResponseObject iterateDriverPathListDetails(Drivers driver, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();

		List<PathList> pathlists = driver.getDriverPathList();

		if (pathlists != null && !pathlists.isEmpty()) {
			PathList pathList;

			for (int i = 0, size = pathlists.size(); i < size; i++) {
				pathList = pathlists.get(i);
				if (isImport) {
					logger.debug("import  Pathlist");
					if (pathList instanceof CollectionDriverPathList) {
						((CollectionDriverPathList) pathList)
								.setReferenceDevice(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,
										((CollectionDriverPathList) pathList).getReferenceDevice()));
						FileSequenceMgmt fileSeqMgmt = ((CollectionDriverPathList) pathList).getMissingFileSequenceId();
						if (fileSeqMgmt != null && fileSeqMgmt.getId() > 0) {
							fileSeqMgmt.setId(0);
							fileSeqMgmt.setReferenceDevice(((CollectionDriverPathList) pathList).getReferenceDevice());
							fileSeqMgmt.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
							fileSeqMgmt.setLastUpdatedDate(new Date());
						}
						((CollectionDriverPathList) pathList).setMissingFileSequenceId(fileSeqMgmt);

					}
					pathList.setId(0);
					pathList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, pathList.getName()));
					pathList.setDriver(driver);
					pathList.setCreatedByStaffId(driver.getCreatedByStaffId());
					pathList.setLastUpdatedByStaffId(driver.getCreatedByStaffId());
					pathList.setCreatedDate(new Date());
					pathList.setLastUpdatedDate(new Date());
					if (driver instanceof CollectionDriver || driver instanceof DistributionDriver) {
						pathList.setService(null);
					} else {
						pathList.setService(driver.getService());
					}

					if (pathList instanceof DistributionDriverPathList) {
						pathList.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
						responseObject = importOrDeleteDistributionPathlistDependents(
								(DistributionDriverPathList) pathList, isImport, BaseConstants.IMPORT_MODE_KEEP_BOTH);
					}

				} else {
					logger.debug("Delete pathlist");
					if (pathList instanceof CollectionDriverPathList) {
						((CollectionDriverPathList) pathList)
								.setReferenceDevice(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,
										((CollectionDriverPathList) pathList).getReferenceDevice()));
						FileSequenceMgmt fileSeqMgmt = ((CollectionDriverPathList) pathList).getMissingFileSequenceId();
						if (fileSeqMgmt != null && fileSeqMgmt.getId() > 0) {
							fileSeqMgmt.setStatus(StateEnum.DELETED);
							fileSeqMgmt.setReferenceDevice(((CollectionDriverPathList) pathList).getReferenceDevice());
							fileSeqMgmt.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
							fileSeqMgmt.setLastUpdatedDate(new Date());
						}
						((CollectionDriverPathList) pathList).setMissingFileSequenceId(fileSeqMgmt);
					}
					pathList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, pathList.getName()));
					pathList.setStatus(StateEnum.DELETED);
					pathList.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
					pathList.setLastUpdatedDate(new Date());
					if (pathList instanceof DistributionDriverPathList) {
						pathList.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
						responseObject = importOrDeleteDistributionPathlistDependents(
								(DistributionDriverPathList) pathList, isImport, BaseConstants.IMPORT_MODE_KEEP_BOTH); // Method
																														// will
																														// delete
																														// or
																														// import/create
																														// plug-in
																														// and
																														// its
																														// dependents.
					}
					responseObject.setSuccess(true);
				}
			}
			driver.setDriverPathList(pathlists);
		} else {
			logger.debug("PathList not configured for driver");
			responseObject.setSuccess(true);
		}
		return responseObject;
	}

	@Override
	public void importDriverPathListAddAndKeepBothMode(Drivers exportedDriver, int importMode) {
		int pathId = 0;
		List<PathList> exportedPathList = exportedDriver.getDriverPathList();
		if (!CollectionUtils.isEmpty(exportedPathList)) {
			int length = exportedPathList.size();
			for (int i = length - 1; i >= 0; i--) {
				PathList exportedPath = exportedPathList.get(i);
				if (exportedPath != null && !exportedPath.getStatus().equals(StateEnum.DELETED)) {
					importPathListAddAndKeepBothMode(exportedPath, exportedDriver, null, importMode);
				}
			}
		}
	}

	@Override
	public void importPathListAddAndKeepBothMode(PathList pathList, Drivers driver, Service service, int importMode) {
		ResponseObject responseObject;
		pathList.setId(0);
		if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			pathList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, pathList.getName()));
		}
		if (pathList instanceof CollectionDriverPathList) {
			boolean isDuplicateReferenceDevice = fileSequenceMgmtDao.validateDuplicateDeviceNameByReferenceDevice(
					((CollectionDriverPathList) pathList).getMissingFileSequenceId());
			if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH || isDuplicateReferenceDevice) {
				((CollectionDriverPathList) pathList).setReferenceDevice(EliteUtils.checkForNames(
						BaseConstants.ACTION_IMPORT, ((CollectionDriverPathList) pathList).getReferenceDevice()));
			}
			if (((CollectionDriverPathList) pathList).getParentDevice() != null
					&& StateEnum.ACTIVE.equals(((CollectionDriverPathList) pathList).getParentDevice().getStatus())) {
				responseObject = deviceService
						.getDeviceByName(((CollectionDriverPathList) pathList).getParentDevice().getName());
				if (responseObject.isSuccess()) {
					logger.debug("Found already created device  with name "
							+ ((CollectionDriverPathList) pathList).getParentDevice().getName());
					((CollectionDriverPathList) pathList).setParentDevice((Device) responseObject.getObject());
				} else {
					responseObject = deviceService.verifyAndCreateDeviceDetails(
							((CollectionDriverPathList) pathList).getParentDevice(), pathList.getCreatedByStaffId(),
							importMode);
					if (responseObject.isSuccess()) {
						((CollectionDriverPathList) pathList).setParentDevice((Device) responseObject.getObject());
					}
				}
			} else {
				((CollectionDriverPathList) pathList).setParentDevice(null);
			}
			charRenameOperationService.importOrDeleteCharRenameOperation(pathList, true);
			FileSequenceMgmt fileSeqMgmt = ((CollectionDriverPathList) pathList).getMissingFileSequenceId();
			if (fileSeqMgmt != null && fileSeqMgmt.getId() > 0) {
				fileSeqMgmt.setId(0);
				fileSeqMgmt.setReferenceDevice(((CollectionDriverPathList) pathList).getReferenceDevice());
				fileSeqMgmt.setCreatedDate(EliteUtils.getDateForImport(false));
				fileSeqMgmt.setLastUpdatedDate(EliteUtils.getDateForImport(false));
				fileSeqMgmt.setParentDevice(((CollectionDriverPathList) pathList).getParentDevice());
			}
		}

		pathList.setCreatedDate(EliteUtils.getDateForImport(false));
		pathList.setLastUpdatedDate(EliteUtils.getDateForImport(false));

		if (driver != null) {
			pathList.setDriver(driver);
			pathList.setCreatedByStaffId(driver.getCreatedByStaffId());
			pathList.setLastUpdatedByStaffId(driver.getLastUpdatedByStaffId());
			if (driver instanceof CollectionDriver || driver instanceof DistributionDriver) {
				pathList.setService(null);
			} else {
				pathList.setService(driver.getService());
			}
		}

		if (service != null) {
			pathList.setDriver(null);
			pathList.setCreatedByStaffId(service.getCreatedByStaffId());
			pathList.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
			if (importMode != BaseConstants.IMPORT_MODE_OVERWRITE) {
				pathList.setPathId(getMaxPathListId(service));
			}
			if (service instanceof ParsingService || service instanceof ProcessingService
					|| service instanceof DataConsolidationService || service instanceof IPLogParsingService
					|| service instanceof AggregationService) {
				pathList.setService(service);
			}
		}

		if (pathList instanceof DistributionDriverPathList) {
			if (((DistributionDriverPathList) pathList).getParentDevice() != null
					&& StateEnum.ACTIVE.equals(((DistributionDriverPathList) pathList).getParentDevice().getStatus())) {
				responseObject = deviceService
						.getDeviceByName(((DistributionDriverPathList) pathList).getParentDevice().getName());
				if (responseObject.isSuccess()) {
					logger.debug("Found already created device  with name "
							+ ((DistributionDriverPathList) pathList).getParentDevice().getName());
					((DistributionDriverPathList) pathList).setParentDevice((Device) responseObject.getObject());
				} else {
					if (((DistributionDriverPathList) pathList).getParentDevice() != null
							&& ((DistributionDriverPathList) pathList).getParentDevice()
									.getStatus() == StateEnum.ACTIVE) {
						responseObject = deviceService.verifyAndCreateDeviceDetails(
								((DistributionDriverPathList) pathList).getParentDevice(),
								pathList.getCreatedByStaffId(), importMode);
						if (responseObject.isSuccess()) {
							((DistributionDriverPathList) pathList)
									.setParentDevice((Device) responseObject.getObject());
						}
					}
				}
			} else {
				((DistributionDriverPathList) pathList).setParentDevice(null);
			}
			importDistributionPathListAddAndKeepBothMode((DistributionDriverPathList) pathList, importMode);
		} else if (pathList instanceof ParsingPathList) {
			if (((ParsingPathList) pathList).getParentDevice() != null
					&& StateEnum.ACTIVE.equals(((ParsingPathList) pathList).getParentDevice().getStatus())) {
				responseObject = deviceService
						.getDeviceByName(((ParsingPathList) pathList).getParentDevice().getName());
				if (responseObject.isSuccess()) {
					logger.debug("Found already created device  with name "
							+ ((ParsingPathList) pathList).getParentDevice().getName());
					((ParsingPathList) pathList).setParentDevice((Device) responseObject.getObject());
				} else {
					responseObject = deviceService.verifyAndCreateDeviceDetails(
							((ParsingPathList) pathList).getParentDevice(), pathList.getCreatedByStaffId(), importMode);
					if (responseObject.isSuccess()) {
						((ParsingPathList) pathList).setParentDevice((Device) responseObject.getObject());
					}
				}
			} else {
				((ParsingPathList) pathList).setParentDevice(null);
			}
			ParsingPathList parSingPathList = (ParsingPathList) pathList;
			if (parSingPathList.getCircle() == null) {
				Circle circle = new Circle();
				circle.setId(1);
				parSingPathList.setCircle(circle);
			} else {
				List<Circle> circleList = circleDao.getCircleListByName(parSingPathList.getCircle().getName());
				if (circleList != null && !circleList.isEmpty()) {
					parSingPathList.setCircle(circleList.get(0));
				} else {
					Circle defaltCircle = new Circle();
					defaltCircle.setId(1);
					parSingPathList.setCircle(defaltCircle);
				}
			}
			importParsingPathListAddAndKeepBothMode(parSingPathList, importMode);
		} else if (pathList instanceof ProcessingPathList) {
			if (((ProcessingPathList) pathList).getParentDevice() != null
					&& StateEnum.ACTIVE.equals(((ProcessingPathList) pathList).getParentDevice().getStatus())) {
				responseObject = deviceService
						.getDeviceByName(((ProcessingPathList) pathList).getParentDevice().getName());
				if (responseObject.isSuccess()) {
					logger.debug("Found already created device  with name "
							+ ((ProcessingPathList) pathList).getParentDevice().getName());
					((ProcessingPathList) pathList).setParentDevice((Device) responseObject.getObject());
				} else {
					responseObject = deviceService.verifyAndCreateDeviceDetails(
							((ProcessingPathList) pathList).getParentDevice(), pathList.getCreatedByStaffId(),
							importMode);
					if (responseObject.isSuccess()) {
						((ProcessingPathList) pathList).setParentDevice((Device) responseObject.getObject());
					}
				}
			} else {
				((ProcessingPathList) pathList).setParentDevice(null);
			}
			((ProcessingPathList) pathList).setAlertId(null);
			((ProcessingPathList) pathList).setAlertDescription(null);
			String exportedPathAlias = ((ProcessingPathList) pathList).getPolicyAlias();
			if (exportedPathAlias != null && !"".equalsIgnoreCase(exportedPathAlias) && service != null) {
				Policy dbPolicy = policyDao.getPolicyByAlias(exportedPathAlias, service.getServerInstance().getId());
				if (dbPolicy != null) {
					((ProcessingPathList) pathList).setPolicy(dbPolicy);
					((ProcessingPathList) pathList).setPolicyAlias(dbPolicy.getAlias());
				}
			}
		} else if (pathList instanceof DataConsolidationPathList) {
			if (((DataConsolidationPathList) pathList).getParentDevice() != null
					&& StateEnum.ACTIVE.equals(((DataConsolidationPathList) pathList).getParentDevice().getStatus())) {
				responseObject = deviceService
						.getDeviceByName(((DataConsolidationPathList) pathList).getParentDevice().getName());
				if (responseObject.isSuccess()) {
					logger.debug("Found already created device  with name "
							+ ((DataConsolidationPathList) pathList).getParentDevice().getName());
					((DataConsolidationPathList) pathList).setParentDevice((Device) responseObject.getObject());
				} else {
					responseObject = deviceService.verifyAndCreateDeviceDetails(
							((DataConsolidationPathList) pathList).getParentDevice(), pathList.getCreatedByStaffId(),
							importMode);
					if (responseObject.isSuccess()) {
						((DataConsolidationPathList) pathList).setParentDevice((Device) responseObject.getObject());
					}
				}
			} else {
				((DataConsolidationPathList) pathList).setParentDevice(null);
			}
			importDataConsolidationMappingAddAndKeepBothMode((DataConsolidationPathList) pathList, importMode);
		} else if (pathList instanceof AggregationServicePathList) {
			if (((AggregationServicePathList) pathList).getParentDevice() != null
					&& StateEnum.ACTIVE.equals(((AggregationServicePathList) pathList).getParentDevice().getStatus())) {
				responseObject = deviceService
						.getDeviceByName(((AggregationServicePathList) pathList).getParentDevice().getName());
				if (responseObject.isSuccess()) {
					logger.debug("Found already created device  with name "
							+ ((AggregationServicePathList) pathList).getParentDevice().getName());
					((AggregationServicePathList) pathList).setParentDevice((Device) responseObject.getObject());
				} else {
					responseObject = deviceService.verifyAndCreateDeviceDetails(
							((AggregationServicePathList) pathList).getParentDevice(), pathList.getCreatedByStaffId(),
							importMode);
					if (responseObject.isSuccess()) {
						((AggregationServicePathList) pathList).setParentDevice((Device) responseObject.getObject());
					}
				}
			} else {
				((AggregationServicePathList) pathList).setParentDevice(null);
			}
		}
	}

	public void importDataConsolidationMappingAddAndKeepBothMode(DataConsolidationPathList exportedPath,
			int importMode) {
		List<DataConsolidationMapping> exportedMappingList = exportedPath.getConMappingList();
		if (!CollectionUtils.isEmpty(exportedMappingList)) {
			int length = exportedMappingList.size();
			for (int i = length - 1; i >= 0; i--) {
				DataConsolidationMapping exportedMapping = exportedMappingList.get(i);
				if (exportedMapping != null) {
					exportedMapping.setId(0);
					if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
						exportedMapping.setMappingName(
								EliteUtils.checkForNames(BaseConstants.IMPORT, exportedMapping.getMappingName()));
					}
					exportedMapping.setCreatedDate(EliteUtils.getDateForImport(false));
					exportedMapping.setLastUpdatedDate(EliteUtils.getDateForImport(false));
					exportedMapping.setCreatedByStaffId(exportedPath.getCreatedByStaffId());
					exportedMapping.setLastUpdatedByStaffId(exportedPath.getLastUpdatedByStaffId());
					exportedMapping.setDataConsPathList(exportedPath);
				}
			}
		}
	}

	@Override
	public void importDriverPathListUpdateMode(Drivers dbDriver, Drivers exportedDriver, int importMode) {
		List<PathList> dbPathList = dbDriver.getDriverPathList();
		List<PathList> exportedPathList = exportedDriver.getDriverPathList();
		if (exportedPathList != null && !exportedPathList.isEmpty()) {
			int pathListLength = exportedPathList.size();
			for (int i = pathListLength - 1; i >= 0; i--) {
				PathList exportedPath = exportedPathList.get(i);
				if (exportedPath != null && !exportedPath.getStatus().equals(StateEnum.DELETED)) {
					PathList dbPath = getPathFromList(dbPathList, exportedPath.getName());
					if (dbPath != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						importPathListUpdateMode(dbPath, exportedPath, importMode);
						dbPathList.add(dbPath);
					} else if (dbPath != null && importMode == BaseConstants.IMPORT_MODE_ADD) {
						importPathListAddMode(dbPath, exportedPath);
						dbPathList.add(dbPath);
					} else {
						importPathListAddAndKeepBothMode(exportedPath, dbDriver, null, importMode);
						dbPathList.add(exportedPath);
					}
				}
			}
		}
	}

	@Override
	public void importPathListUpdateMode(PathList dbPath, PathList exportedPath, int importMode) {
		// final int importMode = -1;
		if (dbPath instanceof CollectionDriverPathList && exportedPath instanceof CollectionDriverPathList) {
			iterateCollectionDriverPathList((CollectionDriverPathList) dbPath, (CollectionDriverPathList) exportedPath,
					importMode);
		} else if (dbPath instanceof ParsingPathList && exportedPath instanceof ParsingPathList) {
			iterateParsingPathList((ParsingPathList) dbPath, (ParsingPathList) exportedPath, importMode);
		} else if (dbPath instanceof DataConsolidationPathList && exportedPath instanceof DataConsolidationPathList) {
			iterateDataConsolidationPathList((DataConsolidationPathList) dbPath,
					(DataConsolidationPathList) exportedPath, importMode);
		} else if (dbPath instanceof DistributionDriverPathList && exportedPath instanceof DistributionDriverPathList) {
			iterateDistributionDriverPathList((DistributionDriverPathList) dbPath,
					(DistributionDriverPathList) exportedPath, importMode);
		} else if (dbPath instanceof ProcessingPathList && exportedPath instanceof ProcessingPathList) {
			// MED-9164
			if (importMode != BaseConstants.IMPORT_MODE_ADD) {
				iterateProcessingPathList((ProcessingPathList) dbPath, (ProcessingPathList) exportedPath);
			}
		} else if (dbPath instanceof AggregationServicePathList && exportedPath instanceof AggregationServicePathList) {
			if (importMode != BaseConstants.IMPORT_MODE_ADD) {
				iterateAggregationServicePathList((AggregationServicePathList) dbPath,
						(AggregationServicePathList) exportedPath);
			}
		} else {
			iteratePathList(dbPath, exportedPath);
		}
	}

	@Override
	public void importPathListAddMode(PathList dbPath, PathList exportedPath) {
		if (dbPath instanceof CollectionDriverPathList && exportedPath instanceof CollectionDriverPathList) {
			iterateCollectionDriverPathList((CollectionDriverPathList) dbPath, (CollectionDriverPathList) exportedPath,
					BaseConstants.IMPORT_MODE_ADD);
		} else if (dbPath instanceof ParsingPathList && exportedPath instanceof ParsingPathList) {
			iterateParsingPathList((ParsingPathList) dbPath, (ParsingPathList) exportedPath,
					BaseConstants.IMPORT_MODE_ADD);
		} else if (dbPath instanceof DataConsolidationPathList && exportedPath instanceof DataConsolidationPathList) {
			iterateDataConsolidationPathList((DataConsolidationPathList) dbPath,
					(DataConsolidationPathList) exportedPath, BaseConstants.IMPORT_MODE_ADD);
		} else if (dbPath instanceof DistributionDriverPathList && exportedPath instanceof DistributionDriverPathList) {
			iterateDistributionDriverPathList((DistributionDriverPathList) dbPath,
					(DistributionDriverPathList) exportedPath, BaseConstants.IMPORT_MODE_ADD);
		}
	}

	public void iterateAggregationServicePathList(AggregationServicePathList dbPath,
			AggregationServicePathList exportedPath) {
		dbPath.setMaxFilesCountAlert(exportedPath.getMaxFilesCountAlert());
		dbPath.setDateFormat(exportedPath.getDateFormat());
		dbPath.setFileGrepDateEnabled(exportedPath.getFileGrepDateEnabled());
		dbPath.setStartIndex(exportedPath.getStartIndex());
		dbPath.setEndIndex(exportedPath.getEndIndex());
		dbPath.setPosition(exportedPath.getPosition());
		dbPath.setwPathNonAggregate(exportedPath.getwPathNonAggregate());
		dbPath.setwPathAggregateError(exportedPath.getwPathAggregateError());
		dbPath.setoFilePathName(exportedPath.getoFilePathName());
		dbPath.setoFileMinRange(exportedPath.getoFileMinRange());
		dbPath.setoFileMaxRange(exportedPath.getoFileMaxRange());
		dbPath.setoFileSeqEnables(exportedPath.getoFileSeqEnables());
		dbPath.setoFilePathNameForNonAgg(exportedPath.getoFilePathNameForNonAgg());
		dbPath.setoFileMinRangeForNonAgg(exportedPath.getoFileMinRangeForNonAgg());
		dbPath.setoFileMaxRangeForNonAgg(exportedPath.getoFileMaxRangeForNonAgg());
		dbPath.setoFileSeqEnablesForNonAgg(exportedPath.isoFileSeqEnablesForNonAgg());
		dbPath.setoFilePathNameForError(exportedPath.getoFilePathNameForError());
		dbPath.setoFileMinRangeForError(exportedPath.getoFileMinRangeForError());
		dbPath.setoFileMaxRangeForError(exportedPath.getoFileMaxRangeForError());
		dbPath.setoFileSeqEnablesForError(exportedPath.isoFileSeqEnablesForError());
		iterateCommonPathList(dbPath, exportedPath);
	}

	public void iterateCollectionDriverPathList(CollectionDriverPathList dbPath, CollectionDriverPathList exportedPath,
			int importMode) {

		if (importMode != BaseConstants.IMPORT_MODE_ADD) {
			dbPath.setMaxFilesCountAlert(exportedPath.getMaxFilesCountAlert());
			dbPath.setRemoteFileAction(exportedPath.getRemoteFileAction());
			dbPath.setRemoteFileActionParamName(exportedPath.getRemoteFileActionParamName());
			dbPath.setRemoteFileActionValue(exportedPath.getRemoteFileActionValue());
			dbPath.setRemoteFileActionParamNameTwo(exportedPath.getRemoteFileActionParamNameTwo());
			dbPath.setRemoteFileActionValueTwo(exportedPath.getRemoteFileActionValueTwo());
			dbPath.setFileGrepDateEnabled(exportedPath.getFileGrepDateEnabled());
			dbPath.setDateFormat(exportedPath.getDateFormat());
			dbPath.setStartIndex(exportedPath.getStartIndex());
			dbPath.setEndIndex(exportedPath.getEndIndex());
			dbPath.setPosition(exportedPath.getPosition());
			dbPath.setFileSeqAlertEnabled(exportedPath.getFileSeqAlertEnabled());
			dbPath.setSeqStartIndex(exportedPath.getSeqStartIndex());
			dbPath.setSeqEndIndex(exportedPath.getSeqEndIndex());
			dbPath.setDuplicateCheckParamName(exportedPath.getDuplicateCheckParamName());
			dbPath.setDuplicateFileSuffix(exportedPath.getDuplicateFileSuffix());
			dbPath.setTimeInterval(exportedPath.getTimeInterval());
			dbPath.setFileSizeCheckEnabled(exportedPath.getFileSizeCheckEnabled());
			dbPath.setFileSizeCheckMinValue(exportedPath.getFileSizeCheckMinValue());
			dbPath.setFileSizeCheckMaxValue(exportedPath.getFileSizeCheckMaxValue());

			boolean isDuplicateReferenceDevice = fileSequenceMgmtDao
					.validateDuplicateDeviceNameByReferenceDevice(exportedPath.getMissingFileSequenceId());
			if (isDuplicateReferenceDevice) {
				exportedPath.setReferenceDevice(
						EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPath.getReferenceDevice()));
				exportedPath.getMissingFileSequenceId().setReferenceDevice(exportedPath.getReferenceDevice());
			}
			charRenameOperationService.importCharacterRenameOperationUpdateMode(dbPath, exportedPath);
			dbPath.setReferenceDevice(exportedPath.getReferenceDevice());
			// dbPath.setMaxCounterLimit(exportedPath.getMaxCounterLimit());
			if (exportedPath.getMissingFileSequenceId() != null) {
				iterateFileSeqMgmt(dbPath.getMissingFileSequenceId(), exportedPath.getMissingFileSequenceId());
			}
			iterateCommonPathList(dbPath, exportedPath);
		} else {
			charRenameOperationService.importCharacterRenameOperationAddMode(dbPath, exportedPath);
		}
	}

	public void iterateFileSeqMgmt(FileSequenceMgmt dbFileSeqMgmt, FileSequenceMgmt exportedFileSeq) {
		dbFileSeqMgmt.setElementType(exportedFileSeq.getElementType());
		dbFileSeqMgmt.setReferenceDevice(exportedFileSeq.getReferenceDevice());
		dbFileSeqMgmt.setMinValue(exportedFileSeq.getMinValue());
		dbFileSeqMgmt.setMaxValue(exportedFileSeq.getMaxValue());
		dbFileSeqMgmt.setResetFrequency(exportedFileSeq.getResetFrequency());
		dbFileSeqMgmt.setResetTime(exportedFileSeq.getResetTime());
		dbFileSeqMgmt.setMissingFileStartIndex(exportedFileSeq.getMissingFileStartIndex());
		dbFileSeqMgmt.setMissingFileEndIndex(exportedFileSeq.getMissingFileEndIndex());
		dbFileSeqMgmt.setStatus(exportedFileSeq.getStatus());
		dbFileSeqMgmt.setLastUpdatedDate(EliteUtils.getDateForImport(false));

	}

	public void iterateParsingPathList(ParsingPathList dbPath, ParsingPathList exportedPath, int importMode) {
		if (importMode != BaseConstants.IMPORT_MODE_ADD) {
			dbPath.setFileGrepDateEnabled(exportedPath.getFileGrepDateEnabled());
			dbPath.setDateFormat(exportedPath.getDateFormat());
			dbPath.setStartIndex(exportedPath.getStartIndex());
			dbPath.setEndIndex(exportedPath.getEndIndex());
			dbPath.setPosition(exportedPath.getPosition());
			dbPath.setMandatoryFields(exportedPath.getMandatoryFields());
			importParsingPathListUpdateMode(dbPath, exportedPath);
			iteratePathList(dbPath, exportedPath);
		} else {
			importParsingPathListAddMode(dbPath, exportedPath);
		}
	}

	public void iterateDataConsolidationPathList(DataConsolidationPathList dbPath,
			DataConsolidationPathList exportedPath, int importMode) {
		dbPath.setMaxCounterLimit(exportedPath.getMaxCounterLimit());

		List<DataConsolidationMapping> dbMappingList = dbPath.getConMappingList();
		List<DataConsolidationMapping> exportedMappingList = exportedPath.getConMappingList();

		if (!CollectionUtils.isEmpty(exportedMappingList)) {
			int length = exportedMappingList.size();
			for (int i = length - 1; i >= 0; i--) {
				DataConsolidationMapping exportedMapping = exportedMappingList.get(i);
				if (exportedMapping != null) {
					DataConsolidationMapping dbMapping = dataConsolidationService.getDataConsolidationMappingFromList(
							dbMappingList, exportedMapping.getMappingName(), exportedMapping.getDestPath());
					if (dbMapping != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						dataConsolidationService.importDataConsolidationMappingUpdateMode(dbMapping, exportedMapping);
						dbMappingList.add(dbMapping);
					} else if (dbMapping == null || importMode != BaseConstants.IMPORT_MODE_ADD) {
						dataConsolidationService.importDataConsolidationMappingAddAndKeepBothMode(exportedMapping,
								dbPath, BaseConstants.IMPORT_MODE_UPDATE);
						dbMappingList.add(exportedMapping);
					}
				}
			}
		}

		if (importMode != BaseConstants.IMPORT_MODE_ADD) {
			iterateCommonPathList(dbPath, exportedPath);
		}
	}

	public void iterateDistributionDriverPathList(DistributionDriverPathList dbPath,
			DistributionDriverPathList exportedPath, int importMode) {
		if (importMode != BaseConstants.IMPORT_MODE_ADD) {
			dbPath.setMaxFilesCountAlert(exportedPath.getMaxFilesCountAlert());
			dbPath.setFileGrepDateEnabled(exportedPath.getFileGrepDateEnabled());
			dbPath.setDateFormat(exportedPath.getDateFormat());
			dbPath.setStartIndex(exportedPath.getStartIndex());
			dbPath.setEndIndex(exportedPath.getEndIndex());
			dbPath.setPosition(exportedPath.getPosition());
			dbPath.setDbReadFileNameExtraSuffix(exportedPath.getDbReadFileNameExtraSuffix());
			importDistributionPathListUpdateMode(dbPath, exportedPath);
			iterateCommonPathList(dbPath, exportedPath);
		} else if (importMode == BaseConstants.IMPORT_MODE_ADD) {
			importDistributionPathListAddMode(dbPath, exportedPath, importMode);
		}
	}

	public void iterateProcessingPathList(ProcessingPathList dbPath, ProcessingPathList exportedPath) {
		dbPath.setFileGrepDateEnabled(exportedPath.getFileGrepDateEnabled());
		dbPath.setDateFormat(exportedPath.getDateFormat());
		dbPath.setStartIndex(exportedPath.getStartIndex());
		dbPath.setEndIndex(exportedPath.getEndIndex());
		dbPath.setwriteCdrHeaderFooterEnabled(exportedPath.getwriteCdrHeaderFooterEnabled());
		dbPath.setPosition(exportedPath.getPosition());
		dbPath.setMaxFilesCountAlert(exportedPath.getMaxFilesCountAlert());
		dbPath.setAcrossFileDuplicateCDRCacheLimit(exportedPath.getAcrossFileDuplicateCDRCacheLimit());
		dbPath.setAcrossFileDuplicateDateField(exportedPath.getAcrossFileDuplicateDateField());
		dbPath.setAcrossFileDuplicateDateFieldFormat(exportedPath.getAcrossFileDuplicateDateFieldFormat());
		dbPath.setAcrossFileDuplicateDateInterval(exportedPath.getAcrossFileDuplicateDateInterval());
		dbPath.setAcrossFileDuplicateDateIntervalType(exportedPath.getAcrossFileDuplicateDateIntervalType());
		dbPath.setAlertDescription(null);
		dbPath.setAlertId(null);
		dbPath.setDuplicateRecordPolicyEnabled(exportedPath.isDuplicateRecordPolicyEnabled());
		dbPath.setDuplicateRecordPolicyType(exportedPath.getDuplicateRecordPolicyType());
		dbPath.setUnifiedFields(exportedPath.getUnifiedFields());

		String exportedPathAlias = exportedPath.getPolicyAlias();
		if (exportedPathAlias != null && !"".equalsIgnoreCase(exportedPathAlias)) {
			Policy dbPolicy = policyDao.getPolicyByAlias(exportedPathAlias,
					dbPath.getService().getServerInstance().getId());
			if (dbPolicy != null) {
				dbPath.setPolicy(dbPolicy);
				dbPath.setPolicyAlias(dbPolicy.getAlias());
			}
		}

		iterateCommonPathList(dbPath, exportedPath);
	}

	public void iterateCommonPathList(CommonPathList dbPath, CommonPathList exportedPath) {
		dbPath.setFileNamePattern(exportedPath.getFileNamePattern());
		dbPath.setReadFilenamePrefix(exportedPath.getReadFilenamePrefix());
		dbPath.setReadFilenameSuffix(exportedPath.getReadFilenameSuffix());
		dbPath.setReadFilenameContains(exportedPath.getReadFilenameContains());
		dbPath.setReadFilenameExcludeTypes(exportedPath.getReadFilenameExcludeTypes());
		dbPath.setWriteFilePath(exportedPath.getWriteFilePath());
		dbPath.setWriteFilenamePrefix(exportedPath.getWriteFilenamePrefix());
		dbPath.setCompressInFileEnabled(exportedPath.isCompressInFileEnabled());
		dbPath.setCompressOutFileEnabled(exportedPath.isCompressOutFileEnabled());
		iteratePathList(dbPath, exportedPath);
	}

	public void iteratePathList(PathList dbPath, PathList exportedPath) {
		dbPath.setReadFilePath(exportedPath.getReadFilePath());
		dbPath.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}

	public void importDistributionPathListUpdateMode(DistributionDriverPathList dbPathList,
			DistributionDriverPathList exportedPathList) {
		logger.debug("import : going to add/update composers for path : " + dbPathList.getName());
		List<Composer> dbComposerList = dbPathList.getComposerWrappers();
		List<Composer> exportedComposerList = exportedPathList.getComposerWrappers();
		if (!CollectionUtils.isEmpty(exportedComposerList)) {
			int length = exportedComposerList.size();
			for (int i = length - 1; i >= 0; i--) {
				Composer exportedComposer = exportedComposerList.get(i);
				updatePluginTypeByType(exportedComposer);
				Composer finalComposer = exportedComposer;
				if (exportedComposer != null && !StateEnum.DELETED.equals(exportedComposer.getStatus())) {
					Composer dbComposer = composerService.getComposerFromList(dbComposerList,
							exportedComposer.getComposerType().getAlias(), exportedComposer.getName());
					if (dbComposer != null) {
						finalComposer = dbComposer;
						// update : check composer mapping already associated with other or not
						ComposerMapping dbComposerMapping = dbComposer.getComposerMapping();
						if (dbComposerMapping != null && !dbComposerMapping.getStatus().equals(StateEnum.DELETED)) {
							// get associated composers from mapping
							List<Composer> composerList = composerMappingDao
									.getMappingAssociationDetails(dbComposerMapping.getId());
							if (!CollectionUtils.isEmpty(composerList)) {
								int composerLength = composerList.size();
								/*
								 * if(composerLength == 1) { logger.
								 * debug("going to update composer for import, mapping is associated with 1 composer : "
								 * +dbComposer.getName()); }
								 */
								if (exportedComposer.getComposerType().getAlias()
										.equalsIgnoreCase(dbComposer.getComposerType().getAlias())) {
									if (exportedComposer.getName().equals(dbComposer.getName())) {

									}
								} else if (composerLength > 1) {
									try {
										logger.debug(
												"creating clone from composer, mapping is associated with more than 1 composer  : "
														+ dbComposer.getName());
										finalComposer = (Composer) dbComposer.clone();
										finalComposer.setMyDistDrvPathlist(dbPathList);
										logger.debug("going to update cloned composer for import : "
												+ finalComposer.getName());
									} catch (CloneNotSupportedException e) {
										logger.error("Clone not supported", e);
									}
								}
							} else {
								logger.debug(
										"going to update composer for import, mapping is not associated with any composer : "
												+ dbComposer.getName());
							}
						} else {
							logger.debug("going to update composer for import, no composer mapping found "
									+ dbComposer.getName());
						}
						composerService.importComposerForUpdateMode(finalComposer, exportedComposer);
					} else {
						logger.debug("going to add composer for import : " + finalComposer.getName());
						finalComposer.setId(0);
						finalComposer.setMyDistDrvPathlist(dbPathList);
						charRenameOperationService.importOrDeleteComposerCharRenameOperation(finalComposer, true); // Method
																													// will
																													// delete
																													// or
																													// import/create
																													// char
																													// rename
																													// operation
																													// parameters.
						composerMappingService.importComposerMappingForAddAndKeepBothMode(finalComposer, dbPathList,
								BaseConstants.IMPORT_MODE_UPDATE);
					}
					dbComposerList.add(finalComposer);
				} else {
					logger.debug("Composer not available in exported composer list");
				}
			}
		}
	}

	public void importDistributionPathListAddMode(DistributionDriverPathList dbPathList,
			DistributionDriverPathList exportedPathList, int importMode) {
		logger.debug("import : going to add/update composers for path : " + dbPathList.getName());
		List<Composer> dbComposerList = dbPathList.getComposerWrappers();
		List<Composer> exportedComposerList = exportedPathList.getComposerWrappers();
		if (!CollectionUtils.isEmpty(exportedComposerList)) {
			int length = exportedComposerList.size();
			for (int i = length - 1; i >= 0; i--) {
				Composer exportedComposer = exportedComposerList.get(i);
				updatePluginTypeByType(exportedComposer);
				Composer finalComposer = exportedComposer;
				if (exportedComposer != null && !StateEnum.DELETED.equals(exportedComposer.getStatus())) {
					Composer dbComposer = composerService.getComposerFromList(dbComposerList,
							exportedComposer.getComposerType().getAlias(), exportedComposer.getName());
					if (dbComposer != null) {
						finalComposer = dbComposer;
						// update : check composer mapping already associated with other or not
						ComposerMapping dbComposerMapping = dbComposer.getComposerMapping();
						if (dbComposerMapping != null && !dbComposerMapping.getStatus().equals(StateEnum.DELETED)) {
							// get associated composers from mapping
							List<Composer> composerList = composerMappingDao
									.getMappingAssociationDetails(dbComposerMapping.getId());
							if (!CollectionUtils.isEmpty(composerList)) {
								int composerLength = composerList.size();
								if (exportedComposer.getComposerType().getAlias()
										.equalsIgnoreCase(dbComposer.getComposerType().getAlias())) {
									if (exportedComposer.getName().equals(dbComposer.getName())) {
									}
								} else if (composerLength > 1) {
									try {
										logger.debug(
												"creating clone from composer, mapping is associated with more than 1 composer  : "
														+ dbComposer.getName());
										finalComposer = (Composer) dbComposer.clone();
										finalComposer.setMyDistDrvPathlist(dbPathList);
										logger.debug("going to update cloned composer for import : "
												+ finalComposer.getName());
									} catch (CloneNotSupportedException e) {
										logger.error("Clone not supported", e);
									}
								}
							} else {
								logger.debug(
										"going to update composer for import, mapping is not associated with any composer : "
												+ dbComposer.getName());
							}
						} else {
							logger.debug("going to update composer for import, no composer mapping found "
									+ dbComposer.getName());
						}
						// Added
						if (importMode == BaseConstants.IMPORT_MODE_ADD) {
							if (EngineConstants.ASN1_COMPOSER_PLUGIN
									.equals(exportedComposer.getComposerType().getAlias())) {
								exportedComposer.setComposerMapping(dbComposerMapping);
							} else {
								List<ComposerAttribute> exportedComposerAttr = exportedComposer.getComposerMapping()
										.getAttributeList();
								for (int eca = 0; eca < exportedComposerAttr.size(); eca++) {
									composerAttributeService.createComposerAttributes(exportedComposerAttr.get(eca),
											dbComposer.getComposerMapping().getId(),
											exportedComposer.getComposerType().getAlias(), 1,
											BaseConstants.IMPORT_MODE_ADD);
								}
								if (dbComposerMapping != null)
									dbComposerMapping.setAttributeList(dbComposerMapping.getAttributeList());

								exportedComposer.setComposerMapping(dbComposerMapping);
							}
						} else {
							composerService.importComposerForUpdateMode(finalComposer, exportedComposer);
						}
					} else {
						logger.debug("going to add composer for import : " + finalComposer.getName());
						finalComposer.setId(0);
						finalComposer.setMyDistDrvPathlist(dbPathList);
						charRenameOperationService.importOrDeleteComposerCharRenameOperation(finalComposer, true); // Method
																													// will
																													// delete
																													// or
																													// import/create
																													// char
																													// rename
																													// operation
																													// parameters.
						composerMappingService.importComposerMappingForAddAndKeepBothMode(finalComposer, dbPathList,
								BaseConstants.IMPORT_MODE_UPDATE);
					}
					dbComposerList.add(finalComposer);
				} else {
					logger.debug("Composer not available in exported composer list");
				}
			}
		}
	}

	public void importDistributionPathListAddAndKeepBothMode(DistributionDriverPathList exportedPathList,
			int importMode) {
		logger.debug("going to add composers for exported path : " + exportedPathList.getName());
		List<Composer> exportedComposerList = exportedPathList.getComposerWrappers();
		if (!CollectionUtils.isEmpty(exportedComposerList)) {
			int length = exportedComposerList.size();
			for (int i = length - 1; i >= 0; i--) {
				Composer exportedComposer = exportedComposerList.get(i);
				updatePluginTypeByType(exportedComposer);
				if (exportedComposer != null && !exportedComposer.getStatus().equals(StateEnum.DELETED)) {
					// set basic parameter to composer
					exportedComposer.setId(0);
					exportedComposer.setMyDistDrvPathlist(exportedPathList);
					charRenameOperationService.importOrDeleteComposerCharRenameOperation(exportedComposer, true); // Method
																													// will
																													// delete
																													// or
																													// import/create
																													// char
																													// rename
																													// operation
																													// parameters.
					exportedComposer.setLastUpdatedDate(EliteUtils.getDateForImport(false));

					if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
						logger.debug("going to add composer for keepboth: " + exportedComposer.getName());
						exportedComposer.setName(
								EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedComposer.getName()));
						composerMappingService.importComposerMappingForAddAndKeepBothMode(exportedComposer,
								exportedPathList, importMode);
					} else {
						ComposerMapping exportedComposerMapping = exportedComposer.getComposerMapping();
						ComposerMapping dbComposerMapping = getDbMappingFromExportedMapping(exportedComposerMapping);
						if (importMode == BaseConstants.IMPORT_MODE_UPDATE) {
							if (dbComposerMapping != null) {
								logger.debug("going to update composer mapping..");
								List<Composer> composerList = composerMappingDao
										.getMappingAssociationDetails(dbComposerMapping.getId());
								if (!CollectionUtils.isEmpty(composerList)) {
									int composerLength = composerList.size();
									if (composerLength == 1) {
										Composer dbComposer = composerList.get(0);
										logger.debug(
												"going to update composer, mapping is associated with 1 composer : "
														+ dbComposer.getName());
										composerService.importComposerForUpdateMode(dbComposer, exportedComposer);
										exportedComposerList.set(i, dbComposer);
									} else if (composerLength > 1) {
										try {
											Composer dbComposer = composerList.get(0);
											logger.debug(
													"going to clone composer form composer : " + dbComposer.getName());
											Composer cloneComposer = (Composer) dbComposer.clone();
											cloneComposer.setMyDistDrvPathlist(exportedPathList);
											logger.debug(
													"going to update cloned composer : " + cloneComposer.getName());
											composerService.importComposerForUpdateMode(cloneComposer,
													exportedComposer);
											exportedComposerList.set(i, cloneComposer);
										} catch (CloneNotSupportedException e) {
											logger.error("Clone not supported", e);
										}
									}
								} else {
									logger.debug("going to add composer : " + exportedComposer.getName()
											+ ", using composer mapping : " + dbComposerMapping.getName());
									composerMappingService.importComposerMappingForAddAndKeepBothMode(exportedComposer,
											exportedPathList, importMode);
								}
							} else {
								logger.debug("going to add composer : " + exportedComposer.getName());
								composerMappingService.importComposerMappingForAddAndKeepBothMode(exportedComposer,
										exportedPathList, importMode);
							}
						} else if (importMode == BaseConstants.IMPORT_MODE_ADD
								|| importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
							if (dbComposerMapping == null) {
								logger.debug("going to add composer : " + exportedComposer.getName());
								composerMappingService.importComposerMappingForAddAndKeepBothMode(exportedComposer,
										exportedPathList, importMode);
							} else {
								/*************** MED-9568**commented below code *******************/
								// exportedComposer.setComposerMapping(dbComposerMapping);
								/************************ MED-9568 ********************************/
								logger.debug("adding db composer mapping : " + dbComposerMapping.getName()
										+ " to exported composer : " + exportedComposer.getName());
								// step 1 :: delete attributes
								String attrIds = new String();
								List<ComposerAttribute> composerAttr = dbComposerMapping.getAttributeList();
								for (int pa = 0; pa < composerAttr.size(); pa++) {
									attrIds += composerAttr.get(pa).getId() + ",";
								}
								composerAttributeService.deleteComposerAttributes(attrIds, 1);
								// step 2 :: Add new attributes
								List<ComposerAttribute> exportedComposerAttr = exportedComposer.getComposerMapping()
										.getAttributeList();
								for (int excompcnt = 0; excompcnt < exportedComposerAttr.size(); excompcnt++) {
									exportedComposerAttr.get(excompcnt)
											.setMyComposer(exportedComposer.getComposerMapping());
									composerAttributeService.createComposerAttributes(
											exportedComposerAttr.get(excompcnt), dbComposerMapping.getId(),
											dbComposerMapping.getComposerType().getAlias(), 1, importMode);
								}
								// dbComposerMapping.setAttributeList(exportedComposerAttr);
								exportedComposer.setComposerMapping(dbComposerMapping);
								/************************ MED-9568 ********************************/
							}
						}
					}
				} else {
					logger.debug("Composer status found inactive or deleted.");
				}
			}
			exportedPathList.setComposerWrappers(exportedComposerList); // Setting plug-in list in distribution driver
																		// pathlist.
		}
	}

	public void updatePluginTypeByType(Composer composer) {
		if (composer != null) {
			composer.setComposerType(pluginTypeDao.getPluginByType(composer.getComposerType().getAlias()));
			if (composer.getComposerMapping() != null) {
				composer.getComposerMapping().setComposerType(
						pluginTypeDao.getPluginByType(composer.getComposerMapping().getComposerType().getAlias()));
			}
		}
	}

	public ComposerMapping getDbMappingFromExportedMapping(ComposerMapping exportedComposerMapping) {
		ComposerMapping dbComposerMapping = null;
		if (exportedComposerMapping != null && !exportedComposerMapping.getStatus().equals(StateEnum.DELETED)) {
			ResponseObject responseObject = composerMappingService
					.getComposerMappingDetailsByNameAndType(exportedComposerMapping.getName(), -1);
			if (responseObject.isSuccess() && responseObject.getObject() instanceof ComposerMapping) {
				dbComposerMapping = (ComposerMapping) responseObject.getObject();
			}
		}
		return dbComposerMapping;
	}

	public ParserMapping getDbMappingFromExportedMapping(ParserMapping exportedParserMapping) {
		ParserMapping dbParserMapping = null;
		if (exportedParserMapping != null && !exportedParserMapping.getStatus().equals(StateEnum.DELETED)) {
			dbParserMapping = parserMappingDao.getMappingDetailsByNameAndType(exportedParserMapping.getName(),
					exportedParserMapping.getParserType().getId());
		}
		return dbParserMapping;
	}

	@Override
	public void importServicePathListUpdateMode(Service dbService, Service exportedService, int importMode) {
		List<PathList> dbPathList = dbService.getSvcPathList();
		List<PathList> exportedPathList = exportedService.getSvcPathList();

		if (!CollectionUtils.isEmpty(exportedPathList)) {
			int length = exportedPathList.size();
			for (int i = length - 1; i >= 0; i--) {
				PathList exportedPath = exportedPathList.get(i);
				if (exportedPath != null && !exportedPath.getStatus().equals(StateEnum.DELETED)) {
					PathList dbPath = getPathFromList(dbPathList, exportedPath.getName());
					// MED-5899 : if pathlist's path names are same and and plugin names are
					// different then it should create another pathlist with import suffix
					if (dbPath != null && dbService instanceof ParsingService
							&& importMode != BaseConstants.IMPORT_MODE_ADD) {
						if (!((ParsingPathList) dbPath).getParserWrappers().get(0).getName().equalsIgnoreCase(
								((ParsingPathList) exportedPath).getParserWrappers().get(0).getName())) {
							dbPath = null;
							exportedPath.setName(
									EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPath.getName()));
						}
					}
					if (dbPath != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						if (dbService instanceof IPLogParsingService
								&& importMode == BaseConstants.IMPORT_MODE_UPDATE) {
							logger.debug("IPLog Parsing Service can't allow multiple plugin in same pathlist.");
						} else {
							importPathListUpdateMode(dbPath, exportedPath, importMode);
						}
						dbPathList.add(dbPath);
					} else if (dbPath != null && importMode == BaseConstants.IMPORT_MODE_ADD) {
						importPathListAddMode(dbPath, exportedPath);
						dbPathList.add(dbPath);
					} else {
						if (dbService instanceof IPLogParsingService) { // in iplog parsing service, only one path is
																		// supported
							if (EliteUtils.getActiveListFromGivenList(dbService.getSvcPathList()).isEmpty()) {
								importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
								dbPathList.add(exportedPath);
							}
						} else if (dbService instanceof AggregationService) { // in aggregation service, only one path
																				// is supported
							if (EliteUtils.getActiveListFromGivenList(dbService.getSvcPathList()).isEmpty()
									&& dbPath == null) {
								importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
								dbPathList.add(exportedPath);
							}
						} else {
							importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
							dbPathList.add(exportedPath);
						}
					}
				}
			}
		}
	}

	@Override
	public void importServicePathListAddAndKeepBothMode(Service exportedService, int importMode) {
		List<PathList> exportedPathList = exportedService.getSvcPathList();
		if (!CollectionUtils.isEmpty(exportedPathList)) {
			int length = exportedPathList.size();
			for (int i = length - 1; i >= 0; i--) {
				PathList exportedPath = exportedPathList.get(i);
				if (exportedPath != null && !exportedPath.getStatus().equals(StateEnum.DELETED)) {
					// add path
					importPathListAddAndKeepBothMode(exportedPath, null, exportedService, importMode);
				}
			}
		}
	}

	/**
	 * Method will import or delete distribution driver PATHLIST dependents.
	 * 
	 * @param distributionDriver
	 * @param isImport
	 * @return
	 */
	public ResponseObject importOrDeleteDistributionPathlistDependents(
			DistributionDriverPathList distributionDriverPathList, boolean isImport, int importMode) {
		ResponseObject responseObject = new ResponseObject();

		List<Composer> pluginList = distributionDriverPathList.getComposerWrappers();

		if (pluginList != null && !pluginList.isEmpty()) {
			Composer composer;
			for (int i = 0, size = pluginList.size(); i < size; i++) {
				composer = pluginList.get(i);
				if (StateEnum.ACTIVE.equals(composer.getStatus())) {
					if (isImport) {
						logger.debug("Going to import composer details.");

						composer.setId(0);
						composer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, composer.getName()));
						composer.setMyDistDrvPathlist(distributionDriverPathList);
						responseObject = charRenameOperationService.importOrDeleteComposerCharRenameOperation(composer,
								isImport); // Method will delete or import/create char rename operation parameters.
						if (responseObject.isSuccess()) {
							responseObject = composerMappingService.importComposerMappingAndDependents(composer,
									isImport, importMode);
						}
					} else {
						logger.debug("Going to delete composer. ");
						composer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, composer.getName()));
						composer.setComposerMapping(null);
						composer.setStatus(StateEnum.DELETED);
						composer.setLastUpdatedByStaffId(distributionDriverPathList.getLastUpdatedByStaffId());
						composer.setLastUpdatedDate(new Date());
						// composerService.updateComposer(composer);
						responseObject = charRenameOperationService.importOrDeleteComposerCharRenameOperation(composer,
								isImport); // Method will delete or import/create char rename operation parameters.
					}

				} else {
					logger.debug("Composer status found inactive or deleted.");
				}
			}
			distributionDriverPathList.setComposerWrappers(pluginList); // Setting plug-in list in distribution driver
																		// pathlist.
		} else {
			logger.debug("Plugin list not configured for distribution driver pathlist.");
			responseObject.setSuccess(true);
		}
		return responseObject;
	}

	/**
	 * Method will create new processing service pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = ProcessingPathList.class, ignorePropList = "")
	public ResponseObject addProcessingServicePathList(ProcessingPathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Adding Processing Service pathlist details.");

		int pathListCount = pathListDao.getPathListCountByNameAndService(pathList.getName(),
				getServiceId(pathList.getService()), getDriverId(pathList.getDriver()));

		if (pathListCount <= 0) {
			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());
			if (service != null) {
				pathList.setService(service);
				String maxPathId = getMaxPathListId(service);
				pathList.setPathId(maxPathId);
				pathListDao.save(pathList);
			}
			if (pathList.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
				responseObject.setObject(pathList);
				logger.info("ProcessingService path list details added successfully.");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
				logger.info("Failed to add processing path list details.");
			}
		} else {
			logger.info(duplicatePathNameConstant + pathList.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	@Override
	public String getMaxPathListId(Service service) {
		return increasePathId(pathListDao.getServiceMaxPathId(service.getId()));
	}

	/**
	 * Method will create new processing service pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PATHLIST, actionType = BaseConstants.UPDATE_LIST_ACTION, currentEntity = ProcessingPathList.class, ignorePropList = "driver,service")
	public ResponseObject updateProcessingServicePathList(ProcessingPathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		if (isPathListUniqueForUpdate(getServiceId(pathList.getService()), getDriverId(pathList.getDriver()),
				pathList.getName())) {
			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());
			if (service != null) {
				pathList.setService(service);
				pathListDao.merge(pathList);
				responseObject.setObject(pathList);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	/**
	 * Get Processing path list
	 * 
	 * @param serviceId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getProcessingPathListByServiceId(int serviceId) {

		ResponseObject responseObject = new ResponseObject();
		JSONArray jPathList = new JSONArray();
		JSONObject jPathDetail;

		List<PathList> pathlistdetails = pathListDao.getPathListByServiceId(serviceId);
		if (pathlistdetails != null && !pathlistdetails.isEmpty()) {
			for (PathList pathList : pathlistdetails) {

				if (StateEnum.ACTIVE.equals(pathList.getStatus())) {
					ProcessingPathList processingPathList = (ProcessingPathList) pathList;
					jPathDetail = new JSONObject();
					createProcessingPathlistJsonObject(jPathDetail, processingPathList); // Method will add all require
																							// details to JSON object.
					jPathList.put(jPathDetail);
				}
			}
		}
		responseObject.setSuccess(true);
		responseObject.setObject(jPathList);
		logger.debug(" JSON Response =" + jPathList);

		return responseObject;
	}

	/**
	 * Method will add Processing path list details to jsonObject
	 * 
	 * @param pathListJsonObj
	 * @param processingPathList
	 */
	private void createProcessingPathlistJsonObject(JSONObject pathListJsonObj, ProcessingPathList processingPathList) {
		pathListJsonObj.put("id", processingPathList.getId());
		pathListJsonObj.put("name", processingPathList.getName());
		pathListJsonObj.put(BaseConstants.READ_PATH,
				Utilities.replaceBackwardSlash(processingPathList.getReadFilePath()));
		pathListJsonObj.put("maxFileCountAlert", processingPathList.getMaxFilesCountAlert());
		pathListJsonObj.put("isReadCompressEnable", processingPathList.isCompressInFileEnabled());
		pathListJsonObj.put(BaseConstants.READ_FILE_PREFIX, processingPathList.getReadFilenamePrefix());
		pathListJsonObj.put(BaseConstants.READ_FILE_SUFFIX, processingPathList.getReadFilenameSuffix());
		pathListJsonObj.put("fileNameContains", processingPathList.getReadFilenameContains());
		pathListJsonObj.put("excludeFileType", processingPathList.getReadFilenameExcludeTypes());
		pathListJsonObj.put("writePath", Utilities.replaceBackwardSlash(processingPathList.getWriteFilePath()));
		pathListJsonObj.put("isWriteCompressEnable", processingPathList.isCompressOutFileEnabled());
		if (processingPathList.getPolicy() != null) {
			pathListJsonObj.put("policyId", processingPathList.getPolicy().getId());
			pathListJsonObj.put("policyName", processingPathList.getPolicy().getName());
		} else {
			pathListJsonObj.put("policyId", 0);
			pathListJsonObj.put("policyName", BaseConstants.UNDEFINED_POLICY);
		}
		pathListJsonObj.put("fileGrepDateEnabled",
				processingPathList.getFileGrepDateEnabled() != null ? processingPathList.getFileGrepDateEnabled()
						: "false");
		pathListJsonObj.put("dateFormat",
				(processingPathList.getDateFormat() != null && !processingPathList.getDateFormat().isEmpty())
						? processingPathList.getDateFormat()
						: "yyyyMMddHHmm");
		pathListJsonObj.put("startIndex",
				processingPathList.getStartIndex() != null ? processingPathList.getStartIndex() : 0);
		pathListJsonObj.put("endIndex",
				processingPathList.getEndIndex() != null ? processingPathList.getEndIndex() : 0);
		pathListJsonObj.put("position",
				(processingPathList.getPosition() != null && !processingPathList.getPosition().isEmpty())
						? processingPathList.getPosition()
						: "left");
		pathListJsonObj.put("pathId", processingPathList.getPathId());
		pathListJsonObj.put("writeCdrHeaderFooterEnabled",
				processingPathList.getwriteCdrHeaderFooterEnabled());

		pathListJsonObj.put("duplicateRecordPolicyEnabled", processingPathList.isDuplicateRecordPolicyEnabled());
		/*
		 * pathListJsonObj.put("alertId",processingPathList.getAlertId() != null ?
		 * processingPathList.getAlertId() : "");
		 * pathListJsonObj.put("alertDescription",processingPathList.getAlertDescription
		 * () != null ? processingPathList.getAlertDescription() : "");
		 */
		pathListJsonObj.put("unifiedFields",
				processingPathList.getUnifiedFields() != null ? processingPathList.getUnifiedFields() : "");
		pathListJsonObj.put("duplicateRecordPolicyType",
				processingPathList.getDuplicateRecordPolicyType() != null
						? processingPathList.getDuplicateRecordPolicyType()
						: "");
		pathListJsonObj.put("acrossFileDuplicateCDRCacheLimit",
				processingPathList.getAcrossFileDuplicateCDRCacheLimit());
		pathListJsonObj.put("acrossFileDuplicateDateField",
				processingPathList.getAcrossFileDuplicateDateField() != null
						? processingPathList.getAcrossFileDuplicateDateField()
						: "");
		pathListJsonObj.put("acrossFileDuplicateDateFieldFormat",
				(processingPathList.getAcrossFileDuplicateDateFieldFormat() != null && !processingPathList.getAcrossFileDuplicateDateFieldFormat().isEmpty())
						? processingPathList.getAcrossFileDuplicateDateFieldFormat()
						: "yyyyMMddHHmm");
		pathListJsonObj.put("acrossFileDuplicateDateIntervalType",
				processingPathList.getAcrossFileDuplicateDateIntervalType() != null
						? processingPathList.getAcrossFileDuplicateDateIntervalType()
						: "");
		pathListJsonObj.put("acrossFileDuplicateDateInterval", processingPathList.getAcrossFileDuplicateDateInterval());

		pathListJsonObj.put("referenceDeviceName", processingPathList.getReferenceDevice());
		if (processingPathList.getParentDevice() != null && processingPathList.getParentDevice().getId() > 0) {
			pathListJsonObj.put("deviceId", processingPathList.getParentDevice().getId());
			pathListJsonObj.put("deviceName", processingPathList.getParentDevice().getName());
		} else {
			pathListJsonObj.put("deviceId", "");
			pathListJsonObj.put("deviceName", "");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public PathList getPathListById(int pathListId) {
		return pathListDao.findByPrimaryKey(PathList.class, pathListId);
	}

	/**
	 * Method will create new consolidation service pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = DataConsolidationPathList.class, ignorePropList = "")
	public ResponseObject addConsolidationServicePathList(DataConsolidationPathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Adding Processing Service pathlist details.");

		int pathListCount = pathListDao.getPathListCountByNameAndService(pathList.getName(),
				getServiceId(pathList.getService()), getDriverId(pathList.getDriver()));

		if (pathListCount <= 0) {

			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());
			pathList.setService(service);
			String maxPathId = getMaxPathListId(service);
			pathList.setPathId(maxPathId);
			pathListDao.save(pathList);
			if (pathList.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
				responseObject.setObject(pathList);
				logger.info("ProcessingService path list details added successfully.");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
				logger.info("Failed to add processing path list details.");
			}
		} else {
			logger.info(duplicatePathNameConstant + pathList.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	/**
	 * Method will Update Consolidation service pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PATHLIST, actionType = BaseConstants.UPDATE_ACTION, currentEntity = DataConsolidationPathList.class, ignorePropList = "driver,service,conMappingList")
	public ResponseObject updateConsolidationServicePathList(DataConsolidationPathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		if (isPathListUniqueForUpdate(getServiceId(pathList.getService()), getDriverId(pathList.getDriver()),
				pathList.getName())) {
			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());
			if (service != null) {
				pathList.setService(service);
//				serviceDao.update(service);
				pathListDao.merge(pathList);
				responseObject.setObject(pathList);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	/**
	 * Get Consolidation path list
	 * 
	 * @param serviceId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getConsolidationPathListByServiceId(int serviceId) {
		ResponseObject responseObject = new ResponseObject();
		JSONArray jPathList = new JSONArray();
		JSONObject jPathDetail;

		List<PathList> pathlistdetails = pathListDao.getPathListByServiceId(serviceId);
		if (pathlistdetails != null && !pathlistdetails.isEmpty()) {
			for (PathList pathList : pathlistdetails) {

				if (StateEnum.ACTIVE.equals(pathList.getStatus())) {
					DataConsolidationPathList consolidationPathList = (DataConsolidationPathList) pathList;
					jPathDetail = new JSONObject();
					createConsolidationPathlistJsonObject(jPathDetail, consolidationPathList); // Method will add all
																								// require details to
																								// JSON object.
					addConsolidationMappingToJson(consolidationPathList.getConMappingList(), jPathDetail); // Method
																											// will add
																											// all
																											// require
																											// details
																											// to JSON
																											// object.
					jPathList.put(jPathDetail);
				}
			}
		}
		responseObject.setSuccess(true);
		responseObject.setObject(jPathList);
		logger.debug(" JSON Response =" + jPathList);

		return responseObject;
	}

	private void addConsolidationMappingToJson(List<DataConsolidationMapping> consolidationMappingList,
			JSONObject pathListJsonObj) {
		JSONArray jsonList = new JSONArray();
		if (consolidationMappingList != null) {
			for (DataConsolidationMapping mapping : consolidationMappingList) {
				if (!mapping.getStatus().equals(StateEnum.DELETED)) {
					JSONObject consolidationMappingObj = new JSONObject();
					consolidationMappingObj.put("id", mapping.getId());
					consolidationMappingObj.put("mappingName", mapping.getMappingName());
					consolidationMappingObj.put("destPath", mapping.getDestPath());
					consolidationMappingObj.put("logicalOperator", mapping.getLogicalOperator());
					consolidationMappingObj.put("processRecordLimit", mapping.getProcessRecordLimit());
					consolidationMappingObj.put("conditionList", mapping.getConditionList());
					consolidationMappingObj.put("compressedOutput", mapping.isCompressedOutput());
					consolidationMappingObj.put("writeOnlyConfiguredAttribute",
							mapping.isWriteOnlyConfiguredAttribute());
					consolidationMappingObj.put("fieldNameForCount", mapping.getFieldNameForCount());
					consolidationMappingObj.put("recordSortingType", mapping.getRecordSortingType());
					consolidationMappingObj.put("recordSortingField", mapping.getRecordSortingField());
					consolidationMappingObj.put("recordSortingFieldType", mapping.getRecordSortingFieldType());
					consolidationMappingObj.put("fileName", mapping.getFileName());
					consolidationMappingObj.put("fileSequence", mapping.isFileSequence());
					consolidationMappingObj.put("minSeqRange", mapping.getMinSeqRange());
					consolidationMappingObj.put("maxSeqRange", mapping.getMaxSeqRange());
					jsonList.put(consolidationMappingObj);
				}
			}
		}
		pathListJsonObj.put("consolidationMappingList", jsonList);
	}

	/**
	 * Method will add Consolidation path list details to jsonObject
	 * 
	 * @param pathListJsonObj
	 * @param pathList
	 */
	private void createConsolidationPathlistJsonObject(JSONObject pathListJsonObj, DataConsolidationPathList pathList) {
		pathListJsonObj.put("id", pathList.getId());
		pathListJsonObj.put("name", pathList.getName());
		pathListJsonObj.put(BaseConstants.READ_PATH, pathList.getReadFilePath());
		pathListJsonObj.put("maxCountAlert", pathList.getMaxCounterLimit());
		pathListJsonObj.put("compressedInput", pathList.isCompressInFileEnabled());
		pathListJsonObj.put(BaseConstants.READ_FILE_PREFIX, pathList.getReadFilenamePrefix());
		pathListJsonObj.put(BaseConstants.READ_FILE_SUFFIX, pathList.getReadFilenameSuffix());
		pathListJsonObj.put("referenceDeviceName", pathList.getReferenceDevice());
		pathListJsonObj.put("pathId", pathList.getPathId());
		if (pathList.getParentDevice() != null && pathList.getParentDevice().getId() > 0) {
			pathListJsonObj.put("deviceId", pathList.getParentDevice().getId());
			pathListJsonObj.put("deviceName", pathList.getParentDevice().getName());
		} else {
			pathListJsonObj.put("deviceId", "");
			pathListJsonObj.put("deviceName", "");
		}
	}

	/**
	 * Method will create new DataConsolidation Mapping with Pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_CONSOLIDATION_MAPPING, actionType = BaseConstants.CREATE_ACTION, currentEntity = DataConsolidationMapping.class, ignorePropList = "")
	public ResponseObject addConsolidationPathListMapping(DataConsolidationMapping mapping) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Adding Consolidation Mapping.");
		if (mapping != null) {
			List<PathList> pathList = pathListDao.getParsingPathListbyId(mapping.getDataConsPathList().getId());
			if (pathList != null && !pathList.isEmpty()) {
				DataConsolidationPathList consolidationPathList = (DataConsolidationPathList) pathList.get(0);
				Service service = serviceDao
						.getServiceWithServerInstanceById(consolidationPathList.getService().getId());
				consolidationPathList.setService(service);
				if (service != null) {
					serviceDao.merge(service);
				}
				dataConsolidationMappingDao.save(mapping);
				if (mapping.getId() > 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_CREATE_SUCCESS);
					mapping.setDataConsPathList(consolidationPathList);
					responseObject.setObject(mapping);
					logger.info("Consolidation Mapping details added successfully.");
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_CREATE_FAIL);
					logger.info("Failed to add consolidation mapping details.");
				}
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_CREATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Method will create new DataConsolidation Mapping with Pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_CONSOLIDATION_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = DataConsolidationMapping.class, ignorePropList = "dataConsPathList")
	public ResponseObject updateConsolidationPathListMapping(DataConsolidationMapping mapping) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Update Consolidation Mapping.");
		if (mapping != null) {
			List<PathList> pathList = pathListDao.getParsingPathListbyId(mapping.getDataConsPathList().getId());
			if (pathList != null && !pathList.isEmpty()) {
				DataConsolidationPathList consolidationPathList = (DataConsolidationPathList) pathList.get(0);
				Service service = serviceDao
						.getServiceWithServerInstanceById(consolidationPathList.getService().getId());
				if (service != null) {
					serviceDao.merge(service);
				}
				dataConsolidationMappingDao.merge(mapping);
				if (mapping.getId() > 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_UPDATE_SUCCESS);
					responseObject.setObject(mapping);
					logger.info("Consolidation Mapping details updated successfully.");
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_UPDATE_FAIL);
					logger.info("Failed to update consolidation mapping details.");
				}
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_UPDATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Method will create new DataConsolidation Mapping with Pathlist.
	 * 
	 * @param pathList
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CONSOLIDATION_MAPPING, actionType = BaseConstants.DELETE_MULTIPLE_ACTION, currentEntity = DataConsolidationMapping.class, ignorePropList = "")
	public ResponseObject deleteConsolidationPathListMapping(String mappingIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if (!StringUtils.isEmpty(mappingIds)) {
			String[] mappingIdList = mappingIds.split(",");
			for (int i = 0; i < mappingIdList.length; i++) {
				deleteConsolidationPathListMapping(Integer.parseInt(mappingIdList[i]), staffId);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_DELETE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_DELETE_FAIL);
		}
		return responseObject;
	}

	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CONSOLIDATION_MAPPING, actionType = BaseConstants.DELETE_ACTION, currentEntity = DataConsolidationMapping.class, ignorePropList = "")
	public ResponseObject deleteConsolidationPathListMapping(int mappingId, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		DataConsolidationMapping mapping = dataConsolidationMappingDao.findByPrimaryKey(DataConsolidationMapping.class,
				mappingId);
		mapping.setMappingName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, mapping.getMappingName()));
		mapping.setStatus(StateEnum.DELETED);
		mapping.setLastUpdatedByStaffId(staffId);
		mapping.setLastUpdatedDate(new Date());
		dataConsolidationMappingDao.merge(mapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.CONSOLIDATION_MAPPING_DELETE_SUCCESS);
		return responseObject;
	}

	public int getServiceId(Service service) {
		if (service != null) {
			return service.getId();
		}
		return 0;
	}

	public int getDriverId(Drivers driver) {
		if (driver != null) {
			return driver.getId();
		}
		return 0;
	}

	@Override
	public PathList getPathFromList(List<PathList> pathList, String pathName) {
		if (!CollectionUtils.isEmpty(pathList)) {
			int length = pathList.size();
			for (int i = length - 1; i >= 0; i--) {
				PathList path = pathList.get(i);
				if (path != null && !path.getStatus().equals(StateEnum.DELETED)
						&& path.getName().equalsIgnoreCase(pathName)) {
					return pathList.remove(i);
				}
			}
		}
		return null;
	}

	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_PATHLIST, actionType = BaseConstants.CREATE_ACTION, currentEntity = AggregationServicePathList.class, ignorePropList = "driver,service")
	public ResponseObject addAggregationServicePathList(AggregationServicePathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Adding Aggregation Service pathlist details.");

		int pathListCount = pathListDao.getPathListCountByNameAndService(pathList.getName(),
				getServiceId(pathList.getService()), getDriverId(pathList.getDriver()));

		if (pathListCount <= 0) {

			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());
			pathList.setService(service);
			pathListDao.save(pathList);
			if (service != null) {
				serviceDao.update(service);
			}
			if (pathList.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_SUCCESS);
				responseObject.setObject(pathList);
				logger.info("Aggregation Service path list details added successfully.");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_ADD_FAIL);
				logger.info("Failed to add Aggregation path list details.");
			}
		} else {
			logger.info(duplicatePathNameConstant + pathList.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PATHLIST, actionType = BaseConstants.UPDATE_LIST_ACTION, currentEntity = AggregationServicePathList.class, ignorePropList = "driver,service")
	public ResponseObject updateAggregationServicePathList(AggregationServicePathList pathList) {
		ResponseObject responseObject = new ResponseObject();
		if (isPathListUniqueForUpdate(getServiceId(pathList.getService()), getDriverId(pathList.getDriver()),
				pathList.getName())) {
			Service service = serviceDao.getServiceWithServerInstanceById(pathList.getService().getId());
			if (service != null) {
				AggregationServicePathList aggregationServicePathList = (AggregationServicePathList) pathListDao
						.getPathListByServiceIdAndPathId(service.getId(), pathList.getPathId());
				if (aggregationServicePathList != null) {
					pathList.setCreatedByStaffId(aggregationServicePathList.getCreatedByStaffId());
					pathList.setCreatedDate(aggregationServicePathList.getCreatedDate());
				}
				pathList.setService(service);
				serviceDao.update(service);
				pathListDao.merge(pathList);
				responseObject.setObject(pathList);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PATHLIST_UPDATE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_ID_NOT_FOUND);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PATHLIST_NAME);
		}
		return responseObject;
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseObject getAggregationPathListByServiceId(int serviceId) {
		ResponseObject responseObject = new ResponseObject();
		JSONArray jPathList = new JSONArray();
		JSONObject jPathDetail;

		List<PathList> pathlistdetails = pathListDao.getPathListByServiceId(serviceId);
		if (pathlistdetails != null && !pathlistdetails.isEmpty()) {
			for (PathList pathList : pathlistdetails) {

				if (StateEnum.ACTIVE.equals(pathList.getStatus())) {
					AggregationServicePathList aggPathList = (AggregationServicePathList) pathList;
					jPathDetail = new JSONObject();
					createAggregationPathlistJsonObject(jPathDetail, aggPathList); // Method will add all require
																					// details to JSON object.
					jPathList.put(jPathDetail);
				}
			}
		}
		responseObject.setSuccess(true);
		responseObject.setObject(jPathList);
		logger.debug(" JSON Response =" + jPathList);

		return responseObject;
	}

	private void createAggregationPathlistJsonObject(JSONObject pathListJsonObj, AggregationServicePathList pathList) {
		pathListJsonObj.put("id", pathList.getId());
		pathListJsonObj.put("name", pathList.getName());
		pathListJsonObj.put(BaseConstants.READ_PATH, pathList.getReadFilePath());
		pathListJsonObj.put("maxFileCountAlert", pathList.getMaxFilesCountAlert());
		pathListJsonObj.put("isReadCompressEnable", pathList.isCompressInFileEnabled());
		pathListJsonObj.put("isWriteCompressEnable", pathList.isCompressOutFileEnabled());
		pathListJsonObj.put(BaseConstants.READ_FILE_PREFIX, pathList.getReadFilenamePrefix());
		pathListJsonObj.put(BaseConstants.READ_FILE_SUFFIX, pathList.getReadFilenameSuffix());
		pathListJsonObj.put("writePathAggregated", pathList.getWriteFilePath());
		pathListJsonObj.put("fileGrepDateEnabled", pathList.getFileGrepDateEnabled());
		pathListJsonObj.put("dateFormat",
				(pathList.getDateFormat() != null && !pathList.getDateFormat().isEmpty()) ? pathList.getDateFormat()
						: "yyyyMMddHHmm");
		pathListJsonObj.put("startIndex", pathList.getStartIndex() != null ? pathList.getStartIndex() : 0);
		pathListJsonObj.put("endIndex", pathList.getEndIndex() != null ? pathList.getEndIndex() : 0);
		pathListJsonObj.put("position",
				(pathList.getPosition() != null && !pathList.getPosition().isEmpty()) ? pathList.getPosition()
						: "left");
		pathListJsonObj.put("pathId", pathList.getPathId());
		pathListJsonObj.put("writePathNonAggregated", pathList.getwPathNonAggregate());
		pathListJsonObj.put("writePathAggregatedError", pathList.getwPathAggregateError());
		pathListJsonObj.put("outputfilename", pathList.getoFilePathName());
		pathListJsonObj.put("outputfileseq", pathList.getoFileSeqEnables());
		pathListJsonObj.put("outputfileminrange", pathList.getoFileMinRange());
		pathListJsonObj.put("outputfilemaxrange", pathList.getoFileMaxRange());
		pathListJsonObj.put("outputfilenamefornonagg", pathList.getoFilePathNameForNonAgg());
		pathListJsonObj.put("outputfileseqfornonagg", pathList.isoFileSeqEnablesForNonAgg());
		pathListJsonObj.put("outputfileminrangefornonagg", pathList.getoFileMinRangeForNonAgg());
		pathListJsonObj.put("outputfilemaxrangefornonagg", pathList.getoFileMaxRangeForNonAgg());
		pathListJsonObj.put("outputfilenameforerr", pathList.getoFilePathNameForError());
		pathListJsonObj.put("outputfileseqforerr", pathList.isoFileSeqEnablesForError());
		pathListJsonObj.put("outputfileminrangeforerr", pathList.getoFileMinRangeForError());
		pathListJsonObj.put("outputfilemaxrangeforerr", pathList.getoFileMaxRangeForError());

		pathListJsonObj.put("referenceDeviceName", pathList.getReferenceDevice());
		if (pathList.getParentDevice() != null && pathList.getParentDevice().getId() > 0) {
			pathListJsonObj.put("deviceId", pathList.getParentDevice().getId());
			pathListJsonObj.put("deviceName", pathList.getParentDevice().getName());
		} else {
			pathListJsonObj.put("deviceId", "");
			pathListJsonObj.put("deviceName", "");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public PathList getPathListByServiceAndPathId(int serviceId, String pathId) {
		return pathListDao.getPathListByServiceIdAndPathId(serviceId, pathId);
	}
}
