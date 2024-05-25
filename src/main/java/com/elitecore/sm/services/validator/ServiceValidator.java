package com.elitecore.sm.services.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.consolidationservice.model.ConsolidationTypeEnum;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.pathlist.model.FileGroupingParameterProcessing;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.CoAPCollectionService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.Http2CollectionService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.MqttCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceSchedulingParams;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.service.ServicesService;

/**
 * Validation class for service 
 * @author avani.panchal
 *
 */
@Component
public class ServiceValidator extends BaseValidator{
	private Service service;

	
	@Autowired
	ServicesService servicesService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Service.class.isAssignableFrom(clazz) || SearchServices.class.equals(clazz);
	}
	

	/**
	 * Validate Basic Parameter of Service
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateServiceBasicParam(Object target, Errors errors,String moduleName,boolean validateForImport) {
		
		this.errors = errors;
		service = (Service) target;
		
		isValidate(SystemParametersConstant.SERVICE_NAME,service.getName(),"name",moduleName,service.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_DESCRIPTION,service.getDescription(),"description",moduleName,service.getName(),validateForImport);

	}
	
	/**
	 * Validate All Parameter for service configuration , based on service type
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param validateForImport
	 */
	public void validateServiceConfigurationParameter(Object target, Errors errors,List<ImportValidationErrors> importErrorList,boolean validateForImport){
		
		logger.info("Target class is : " + target.getClass());
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		if(!validateForImport){
			validateNameForUniqueness(target,errors);
		}
		 if (target instanceof NetflowCollectionService){
				
			NetflowCollectionService netflowService=(NetflowCollectionService)target;
			validateServiceBasicParam(netflowService, errors, BaseConstants.NATFLOW_COLLECTION_SERVICE, validateForImport);	
			validateCommonNetflowParam(netflowService,BaseConstants.NATFLOW_COLLECTION_SERVICE,validateForImport);
			validateOptionTemplateParam(netflowService,BaseConstants.NATFLOW_COLLECTION_SERVICE,validateForImport);
				
		 } else if (target instanceof DiameterCollectionService){
				
			DiameterCollectionService diameterService=(DiameterCollectionService)target;
			validateServiceBasicParam(diameterService, errors, BaseConstants.DIAMETER_COLLECTION_SERVICE, validateForImport);	
			validateDiameterStackParam(diameterService,BaseConstants.DIAMETER_COLLECTION_SERVICE,validateForImport);
			validateDiameterOperationalParam(diameterService,BaseConstants.DIAMETER_COLLECTION_SERVICE,validateForImport);
				
		 } else if(target instanceof SysLogCollectionService){
			 
			 SysLogCollectionService syslogService=(SysLogCollectionService)target;
			 validateServiceBasicParam(syslogService, errors, BaseConstants.SYSLOG_COLLECTION_SERVICE, validateForImport);	
			 validateCommonNetflowParam(syslogService,BaseConstants.SYSLOG_COLLECTION_SERVICE,validateForImport);
		 } else if(target instanceof MqttCollectionService){
			 
			 MqttCollectionService mqttService=(MqttCollectionService)target;
			 validateServiceBasicParam(mqttService, errors, BaseConstants.MQTT_COLLECTION_SERVICE, validateForImport);	
			 validateCommonNetflowParam(mqttService,BaseConstants.MQTT_COLLECTION_SERVICE,validateForImport);
			 
		 } else if(target instanceof CoAPCollectionService){
			 
			 CoAPCollectionService coapService=(CoAPCollectionService)target;
			 validateServiceBasicParam(coapService, errors, BaseConstants.COAP_COLLECTION_SERVICE, validateForImport);	
			 validateCommonNetflowParam(coapService,BaseConstants.COAP_COLLECTION_SERVICE,validateForImport);
			 
		 } else if(target instanceof Http2CollectionService){
			 
			 Http2CollectionService http2Service=(Http2CollectionService)target;
			 validateServiceBasicParam(http2Service, errors, BaseConstants.HTTP2_COLLECTION_SERVICE, validateForImport);	
			 validateCommonNetflowParam(http2Service,BaseConstants.HTTP2_COLLECTION_SERVICE,validateForImport);
			 
		 } else if(target instanceof GTPPrimeCollectionService){
				logger.debug(" Validate service config for GTP Prime Collection Service");
				GTPPrimeCollectionService gtpPrimeService = (GTPPrimeCollectionService)target;
				validateServiceBasicParam(gtpPrimeService, errors, BaseConstants.GTPPRIME_COLLECTION_SERVICE, validateForImport);	
				validateCommonNetflowParam(gtpPrimeService,BaseConstants.GTPPRIME_COLLECTION_SERVICE,validateForImport);
		}  else if (target instanceof NetflowBinaryCollectionService){
			 
			 NetflowBinaryCollectionService netflowService=(NetflowBinaryCollectionService)target;
			 validateServiceBasicParam(netflowService, errors, BaseConstants.NATFLOW_BINARY_COLLECTION_SERVICE, validateForImport);	
			 validateCommonNetflowParam(netflowService,BaseConstants.NATFLOW_BINARY_COLLECTION_SERVICE,validateForImport);				
		 }	else if(target instanceof DiameterCollectionService){//NOSONAR
			 
			 DiameterCollectionService diameterService = (DiameterCollectionService) target; 
			 validateServiceBasicParam(diameterService, errors, BaseConstants.DIAMETER_COLLECTION_SERVICE, validateForImport);
			 validateServiceExceutionParameter(diameterService,BaseConstants.DIAMETER_COLLECTION_SERVICE,validateForImport);
			 validateDiameterStackParam(diameterService,BaseConstants.DIAMETER_COLLECTION_SERVICE,validateForImport);
			 validateDiameterOperationalParam(diameterService,BaseConstants.DIAMETER_COLLECTION_SERVICE,validateForImport);
			 
		 }	else if(target instanceof CollectionService){
			
			CollectionService collectionService=(CollectionService)target;
			logger.debug("Found collection service"+collectionService);			
			validateServiceBasicParam(collectionService, errors, BaseConstants.COLLECTION_SERVICE, validateForImport);
			validateServiceExceutionParameter(collectionService,BaseConstants.COLLECTION_SERVICE,validateForImport);
			ServiceSchedulingParams serviceSchedulingParams = collectionService.getServiceSchedulingParams();
			if( serviceSchedulingParams!=null && serviceSchedulingParams.isSchedulingEnabled()){
				isValidate(SystemParametersConstant.SERVICE_SERVICESCHEDULINGPARAMS_TIME,collectionService.getServiceSchedulingParams().getTime(),"serviceSchedulingParams.time",BaseConstants.COLLECTION_SERVICE,collectionService.getName(),validateForImport);	
			}				
		} else if(target instanceof DistributionService){
			
			logger.debug(" Validate service configuration parameters for Distribution Service.");			
			DistributionService distributionService = (DistributionService)target;
			validateServiceBasicParam(distributionService, errors, BaseConstants.DISTRIBUTION_SERVICE, validateForImport);			
			validateServiceExceutionParameter(distributionService,BaseConstants.DISTRIBUTION_SERVICE,validateForImport);			
			isValidate(SystemParametersConstant.SERVICE_SERVICESCHEDULINGPARAMS_TIME,distributionService.getServiceSchedulingParams().getTime(),"serviceSchedulingParams.time",BaseConstants.DISTRIBUTION_SERVICE,distributionService.getName(),validateForImport);
			validateDistributionServiceOperationalParameters(distributionService,BaseConstants.DISTRIBUTION_SERVICE,validateForImport);			
		} else if (target instanceof IPLogParsingService){
			
			logger.debug(" Validate service config for IPLogparsing service");
			IPLogParsingService iplogService=(IPLogParsingService)target;
			validateServiceBasicParam(iplogService, errors, BaseConstants.IPLOG_PARSING_SERVICE, validateForImport);	
			validateIplogParsingServiceParam(iplogService,BaseConstants.IPLOG_PARSING_SERVICE,validateForImport);
			validateServiceExceutionParameter(iplogService,BaseConstants.IPLOG_PARSING_SERVICE,validateForImport);
			
		} else if (target instanceof ParsingService){
			
			logger.debug(" Validate service config for Parsing service");
			ParsingService parsingService=(ParsingService)target;
			validateParsingServiceParameter(parsingService,BaseConstants.PARSING_SERVICE,validateForImport);
			validateServiceExceutionParameter(parsingService,BaseConstants.PARSING_SERVICE,validateForImport);
		} else if (target instanceof ProcessingService){
			
			logger.debug(" Validate service config for Processing service");
			ProcessingService processingService = (ProcessingService)target;
			validateProcessingServiceParameter(processingService, BaseConstants.PROCESSING_SERVICE, validateForImport);
			validateServiceExceutionParameter(processingService, BaseConstants.PROCESSING_SERVICE, validateForImport);
		} else if (target instanceof DataConsolidationService){
			
			logger.debug(" Validate service config for DataConsolidation service");
			DataConsolidationService dataConsolidationService = (DataConsolidationService)target;
			validateConsolidationServiceParameter(dataConsolidationService, BaseConstants.DATA_CONSOLIDATION_SERVICE, validateForImport);
			validateServiceExceutionParameter(dataConsolidationService, BaseConstants.DATA_CONSOLIDATION_SERVICE, validateForImport);
		} else if (target instanceof AggregationService){
			
			logger.debug(" Validate service config for Aggregation service");
			AggregationService aggregationService = (AggregationService)target;
			validateServiceBasicParam(aggregationService, errors, BaseConstants.AGGREGATION_SERVICE, validateForImport);	
			validateServiceExceutionParameter(aggregationService, BaseConstants.AGGREGATION_SERVICE, validateForImport);
			ServiceSchedulingParams serviceSchedulingParams = aggregationService.getServiceSchedulingParams();
			if( serviceSchedulingParams!=null && serviceSchedulingParams.isSchedulingEnabled()){
				isValidate(SystemParametersConstant.SERVICE_SERVICESCHEDULINGPARAMS_TIME,aggregationService.getServiceSchedulingParams().getTime(),"serviceSchedulingParams.time",BaseConstants.AGGREGATION_SERVICE,aggregationService.getName(),validateForImport);	
			}	
			validateAggreagationServiceParameter(aggregationService, BaseConstants.AGGREGATION_SERVICE, validateForImport);
		}
		 
	}
	
	
	public void validateNameForUniqueness(Object target, Errors errors) {
		Service newService = (Service)target;
		boolean isUnique = servicesService.isUniqueServiceName(newService.getId(),newService.getName().trim(),newService.getServerInstance().getId());
		if(!isUnique){
			errors.rejectValue("name", "service.name.already.exists", getMessage("service.name.already.exists"));;
		}
	}


	/**
	 * Method will validation distribution basic service parameters.
	 * @param distributionService
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateDistributionServiceOperationalParameters(DistributionService distributionService, String entityName,boolean validateForImport){
		
		String fullClassName = distributionService.getClass().getName(); 
		
		isValidate(SystemParametersConstant.DISTRIBUTION_PROCESS_RECORD_LIMIT,distributionService.getProcessRecordLimit(),"processRecordLimit",distributionService.getName(), entityName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.DISTRIBUTION_WRITE_RECORD_LIMIT,distributionService.getWriteRecordLimit(),"writeRecordLimit",distributionService.getName(), entityName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.DISTRIBUTION_TIMESTEN_DATASOURCE_NAME,distributionService.getTimestenDatasourceName(),"timestenDatasourceName",new Object[]{"1","500"},entityName,distributionService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_FILEGROUPPARAM_ARCHIVEPATH,distributionService.getFileGroupingParameter().getArchivePath(),"fileGroupingParameter.archivePath",entityName,distributionService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_ERRORPATH, distributionService.getErrorPath(), "errorPath", entityName, distributionService.getName(), validateForImport);
		
	}
	
	
	/**
	 * Validate Service Execution parameter
	 * @param service
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateServiceExceutionParameter(Service service,String entityName,boolean validateForImport){
		
		if(service.getSvcExecParams() !=null){
			
			// common to all services using service execution Parameters:
			
			String svcName=service.getName();
			String svcClassName=service.getClass().getName();
				
			if( ! (service instanceof NetflowBinaryCollectionService) && !(service instanceof DiameterCollectionService) ){
				// online service are always on, not interval for them			
				if(service instanceof CollectionService){
					ServiceSchedulingParams serviceSchedulingParams =  ((CollectionService) service).getServiceSchedulingParams();
					if(serviceSchedulingParams.isSchedulingEnabled()==true){
						isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_EXECUTIONINTERVAL,service.getSvcExecParams().getExecutionInterval(),"svcExecParams.executionInterval",svcName, entityName,validateForImport,svcClassName);
					}			
				} else if(service instanceof DistributionService){
					ServiceSchedulingParams serviceSchedulingParams =  ((DistributionService) service).getServiceSchedulingParams();
					if(serviceSchedulingParams.isSchedulingEnabled()==true){
						isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_EXECUTIONINTERVAL,service.getSvcExecParams().getExecutionInterval(),"svcExecParams.executionInterval",svcName, entityName,validateForImport,svcClassName);
					}
				}
			}
			
			boolean minThreadValid=	isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_MINTHREAD,service.getSvcExecParams().getMinThread(),"svcExecParams.minThread",svcName,entityName,validateForImport,svcClassName);
			boolean maxThreadValid=	isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_MAXTHREAD,service.getSvcExecParams().getMaxThread(),"svcExecParams.maxThread",svcName,entityName,validateForImport,svcClassName);
			
			isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_FILEBATCHSIZE,service.getSvcExecParams().getFileBatchSize(),"svcExecParams.fileBatchSize",svcName,entityName,validateForImport,svcClassName);
			
			if (service instanceof ParsingService){
				isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_QUEUESIZE,service.getSvcExecParams().getQueueSize(),"Service.svcExecParams.queueSize.parsingSvc.invalid",SystemParametersConstant.SERVICEEXEC_QUEUESIZE_PARAM_NAME,svcName,entityName,validateForImport,svcClassName);
			} else {
				isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_QUEUESIZE,service.getSvcExecParams().getQueueSize(),SystemParametersConstant.SERVICEEXEC_QUEUESIZE_PARAM_NAME,svcName,entityName,validateForImport,svcClassName);
			}
			// business validation	
			if (minThreadValid && maxThreadValid && (service.getSvcExecParams().getMinThread() > service.getSvcExecParams().getMaxThread() )){
				setErrorFieldErrorMessage(String.valueOf(service.getSvcExecParams().getMaxThread()), "svcExecParams.maxThread", svcName, entityName, validateForImport, "Service.svcExecParams.maxThread.islesser.invalid", getMessage("Service.svcExecParams.maxThread.islesser.invalid"));		
			}	
		}
	}
	
	/**
	 * Validate Option template parameter
	 * @param netflowService
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateOptionTemplateParam(NetflowCollectionService netflowService,String entityName,boolean validateForImport){
		
		isValidate(SystemParametersConstant.SERVICE_OPTIONTEMPLATEID,netflowService.getOptionTemplateId(),"optionTemplateId",entityName,netflowService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONTEMPLATEKEY,netflowService.getOptionTemplateKey(),"optionTemplateKey",entityName,netflowService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONTEMPLATEVALUE,netflowService.getOptionTemplateValue(),"optionTemplateValue",entityName,netflowService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONCOPYTOTEMPLATEID,netflowService.getOptionCopytoTemplateId(),"optionCopytoTemplateId",entityName,netflowService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONCOPYTOFIELD,netflowService.getOptionCopyTofield(),"optionCopyTofield",entityName,netflowService.getName(),validateForImport);
		
		if(netflowService.isOptionTemplateEnable()){
			if(StringUtils.isEmpty(netflowService.getOptionTemplateId())){
				setErrorFieldErrorMessage(netflowService.getOptionTemplateId(), "optionTemplateId",  entityName,netflowService.getName(), validateForImport, "Service.optionTemplateId.invalid", getMessage("Service.optionTemplateId.invalid"));
			}
			
			if(StringUtils.isEmpty(netflowService.getOptionTemplateKey())){
				setErrorFieldErrorMessage(netflowService.getOptionTemplateKey(), "optionTemplateKey",  entityName,netflowService.getName(), validateForImport, "Service.optionTemplateKey.invalid", getMessage("Service.optionTemplateKey.invalid"));
			}
				
			if(StringUtils.isEmpty(netflowService.getOptionTemplateValue())){
				setErrorFieldErrorMessage(netflowService.getOptionTemplateValue(), "optionTemplateValue",  entityName,netflowService.getName(), validateForImport, "Service.optionTemplateValue.invalid", getMessage("Service.optionTemplateValue.invalid"));
			}
				
			if(StringUtils.isEmpty(netflowService.getOptionCopytoTemplateId())){
				setErrorFieldErrorMessage(netflowService.getOptionCopytoTemplateId(), "optionCopytoTemplateId",  entityName,netflowService.getName(), validateForImport, "Service.optionCopytoTemplateId.invalid", getMessage("Service.optionCopytoTemplateId.invalid"));
			}
			if(StringUtils.isEmpty(netflowService.getOptionCopyTofield())){
				setErrorFieldErrorMessage(netflowService.getOptionCopyTofield(), "optionCopyTofield",  entityName,netflowService.getName(), validateForImport, "Service.optionCopyTofield.invalid", getMessage("Service.optionCopyTofield.invalid"));
			}
		}
	}

	/**
	 * validate common parameter for netflow service
	 * @param netflowService
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateCommonNetflowParam(NetflowBinaryCollectionService netflowService,String entityName,boolean validateForImport){
		String className=netflowService.getClass().getName();
		String svcName=netflowService.getName();
		
		validateIPAddress(netflowService.getServerIp(), "serverIp", entityName,svcName,validateForImport,"service.netFlowIP.invalid", getMessage("service.netFlowIP.invalid"));
		
		if(netflowService instanceof GTPPrimeCollectionService && netflowService.getRedirectionIP() != null && netflowService.getRedirectionIP().length() > 0) {
			validateIPAddress(netflowService.getRedirectionIP(), "redirectionIP", entityName,svcName,validateForImport,"service.gtpprime.redirectip.invalid", getMessage("service.gtpprime.redirectip.invalid"));
		}	
		
		//will validate for both Regex and range.
		isValidate(SystemParametersConstant.SERVICE_NETFLOWPORT,netflowService.getNetFlowPort(),"netFlowPort",entityName,svcName,validateForImport,className);
		if(netflowService instanceof MqttCollectionService) {
			isValidate(SystemParametersConstant.SERVICE_MAXREADRATE,netflowService.getMaxReadRate(),"maxReadRate",entityName,svcName,validateForImport,className);
			isValidate(SystemParametersConstant.SERVICE_RECEIVERBUFFERSIZE,netflowService.getReceiverBufferSize(),"receiverBufferSize",entityName,svcName,validateForImport,className);
			isValidate(SystemParametersConstant.SERVICE_CONNECTMAXATTEMPT,netflowService.getConnectAttemptsMax(),"connectAttemptsMax",entityName,svcName,validateForImport,className);
			isValidate(SystemParametersConstant.SERVICE_RECONNECTMAXATTEMPT,netflowService.getReconnectAttemptsMax(),"reconnectAttemptsMax",entityName,svcName,validateForImport,className);
			isValidate(SystemParametersConstant.SERVICE_RECONNECTDELAY,netflowService.getReconnectDelay(),"reconnectDelay",entityName,svcName,validateForImport,className);
		}
		if(!(netflowService instanceof GTPPrimeCollectionService) && !(netflowService instanceof MqttCollectionService)) {
			if(!(netflowService instanceof CoAPCollectionService)) {
				isValidate(SystemParametersConstant.SERVICE_MAXPKTSIZE,netflowService.getMaxPktSize(),"maxPktSize",entityName,svcName,validateForImport,className);
				isValidate(SystemParametersConstant.SERVICE_PARALLELFILEWRITECOUNT,netflowService.getParallelFileWriteCount(),"parallelFileWriteCount",entityName,svcName,validateForImport,className);
				isValidate(SystemParametersConstant.SERVICE_SNMPTIMEINTERVAL,String.valueOf(netflowService.getSnmpTimeInterval()),"snmpTimeInterval",entityName,svcName,validateForImport);
			}
			isValidate(SystemParametersConstant.SERVICE_BULKWRITELIMIT,netflowService.getBulkWriteLimit(),"bulkWriteLimit",entityName,svcName,validateForImport,className);
			isValidate(SystemParametersConstant.SERVICE_MAXWRITEBUFFERSIZE,netflowService.getMaxWriteBufferSize(),"maxWriteBufferSize",entityName,svcName,validateForImport, className);
		}	
		
		isValidate(SystemParametersConstant.SERVICE_MAXIDELCOMMUTIME,netflowService.getMaxIdelCommuTime(),"maxIdelCommuTime",entityName,svcName,validateForImport,className);
		validateServiceExceutionParameter(netflowService,entityName,validateForImport);
	}
	
	
	
	/**
	 * Validate Iplog parsing service parameter
	 * @param iplogService
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateIplogParsingServiceParam(IPLogParsingService iplogService,String entityName,boolean validateForImport){
		
		String valClassName=iplogService.getClass().getName();
		String svcName=iplogService.getName();
		if(iplogService.getEqualCheckField()!=null && !iplogService.getEqualCheckField().isEmpty())
			isValidate(SystemParametersConstant.SERVICE_EQUALCHECKFIELD,iplogService.getEqualCheckField(),"equalCheckField",entityName,iplogService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_EQUALCHECKVALUE,iplogService.getEqualCheckValue(),"equalCheckValue",entityName,svcName,validateForImport);		
		isValidate(SystemParametersConstant.SERVICE_RECORDBATCHSIZE,iplogService.getRecordBatchSize(),"recordBatchSize",entityName,svcName,validateForImport,valClassName);					
		isValidate(SystemParametersConstant.SERVICE_PURGEINTERVAL,iplogService.getPurgeInterval(),"purgeInterval",entityName,svcName,validateForImport,valClassName);			
		isValidate(SystemParametersConstant.SERVICE_PURGEDELAYINTERVAL,iplogService.getPurgeDelayInterval(),"purgeDelayInterval",entityName,svcName,validateForImport,valClassName);			
		
		//optional params but must comply to regex
		isValidate(SystemParametersConstant.SERVICE_CREATERECDESTPATH,iplogService.getCreateRecDestPath(),"createRecDestPath",entityName,svcName,validateForImport);
		isValidate(SystemParametersConstant.SERVICE_DELETERECDESTPATH,iplogService.getDeleteRecDestPath(),"deleteRecDestPath",entityName,svcName,validateForImport);
		isValidate(SystemParametersConstant.SERVICE_FILESTATSLOC,iplogService.getFileStatsLoc(),"fileStatsLoc",entityName,svcName,validateForImport);
		isValidate(SystemParametersConstant.SERVICE_FILEGROUPPARAM_ARCHIVEPATH,iplogService.getFileGroupingParameter().getArchivePath(),"fileGroupingParameter.archivePath",entityName,iplogService.getName(),validateForImport);
		
		if(iplogService.getMappedSourceField()!=null && !iplogService.getMappedSourceField().isEmpty())
			isValidate(SystemParametersConstant.SERVICE_EQUALCHECKFIELD,iplogService.getMappedSourceField(),"mappedSourceField",entityName,iplogService.getName(),validateForImport);
		if(iplogService.getDestPortField()!=null && !iplogService.getDestPortField().isEmpty())
			isValidate(SystemParametersConstant.SERVICE_EQUALCHECKFIELD,iplogService.getDestPortField(),"destPortField",entityName,iplogService.getName(),validateForImport);
		if(iplogService.getDestPortFilter()!=null && !iplogService.getDestPortFilter().isEmpty())
			isValidate(SystemParametersConstant.SERVICE_EQUALCHECKFIELD,iplogService.getDestPortFilter(),"destPortFilter",entityName,iplogService.getName(),validateForImport);
		
		if(iplogService.isCorrelEnabled()){
			if(StringUtils.isEmpty(iplogService.getCreateRecDestPath())){
				setErrorFieldErrorMessage(iplogService.getCreateRecDestPath(), "createRecDestPath",  entityName,svcName, validateForImport, "Service.createRecDestPath.invalid", getMessage("Service.createRecDestPath.invalid"));
			}
			
			if(StringUtils.isEmpty(iplogService.getDeleteRecDestPath())){
				setErrorFieldErrorMessage(iplogService.getDeleteRecDestPath(), "deleteRecDestPath",  entityName,svcName, validateForImport, "Service.deleteRecDestPath.invalid", getMessage("Service.deleteRecDestPath.invalid"));	
			}
		}
		if(iplogService.isFileStatsEnabled() && StringUtils.isEmpty(iplogService.getFileStatsLoc())){
			setErrorFieldErrorMessage(iplogService.getFileStatsLoc(), "fileStatsLoc",  entityName,svcName, validateForImport, "Service.fileStatsLoc.invalid", getMessage("Service.fileStatsLoc.invalid"));
		}

		if(iplogService.getFileGroupingParameter() != null && iplogService.getFileGroupingParameter().isEnableForArchive() && StringUtils.isEmpty(iplogService.getFileGroupingParameter().getArchivePath())){
			setErrorFieldErrorMessage(iplogService.getFileGroupingParameter().getArchivePath(), "fileGroupParam.archivePath",  entityName,svcName, validateForImport, "Service.fileGroupParam.archivePath.invalid", getMessage("Service.fileGroupParam.archivePath.invalid"));
		}
	}
	
	private void validateParsingServiceParameter(ParsingService parsingService,String entityName,boolean validateForImport){
		String valClassName=parsingService.getClass().getName();
		String svcName=parsingService.getName();
		
		validateServiceBasicParam(parsingService, errors, entityName, validateForImport);
		if(parsingService.getEqualCheckField()!=null && !parsingService.getEqualCheckField().isEmpty())
			isValidate(SystemParametersConstant.SERVICE_EQUALCHECKFIELD,parsingService.getEqualCheckField(),"equalCheckField",entityName,parsingService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_EQUALCHECKVALUE,parsingService.getEqualCheckValue(),"equalCheckValue",entityName,parsingService.getName(),validateForImport);		
		isValidate(SystemParametersConstant.SERVICE_RECORDBATCHSIZE,parsingService.getRecordBatchSize(),"recordBatchSize",entityName,svcName,validateForImport,valClassName);		
		boolean minfileRangeValid=isValidate(SystemParametersConstant.SERVICE_MINFILERANGE,parsingService.getMinFileRange(),"minFileRange",entityName,svcName,validateForImport,valClassName);		
		boolean fileBatchSizeValid=isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_FILEBATCHSIZE,service.getSvcExecParams().getFileBatchSize(),"svcExecParams.fileBatchSize",svcName,entityName,validateForImport,valClassName);
		boolean maxFileRangeValid= isValidate(SystemParametersConstant.SERVICE_MAXFILERANGE, ((ParsingService) service).getMaxFileRange(), SystemParametersConstant.MAXFILERANGE, entityName,svcName,validateForImport,valClassName);
		
		isValidate(SystemParametersConstant.SERVICE_NOFILEALERT,parsingService.getNoFileAlert(),"noFileAlert",entityName,svcName,validateForImport,valClassName);
				
		//business validations
		if(fileBatchSizeValid && maxFileRangeValid ){

			int value=((ParsingService) service).getMaxFileRange()/service.getSvcExecParams().getFileBatchSize();
			value *= 10;
			if(service.getSvcExecParams().getQueueSize() <=value){			
				setErrorFieldErrorMessage(String.valueOf(service.getSvcExecParams().getQueueSize()), SystemParametersConstant.SERVICEEXEC_QUEUESIZE_PARAM_NAME, svcName, entityName, validateForImport, "Service.svcExecParams.queueSize.invalid.min",new Object[]{value}, getMessage("Service.svcExecParams.queueSize.invalid.min"));
			}	
		}
		
		if(minfileRangeValid &&  maxFileRangeValid  && ( (parsingService.getMinFileRange()  == -1  &&  parsingService.getMaxFileRange() >= 1) ||  (parsingService.getMinFileRange()  >= 1  &&  parsingService.getMaxFileRange() == -1))){
			setErrorFieldErrorMessage(String.valueOf(parsingService.getMaxFileRange()), SystemParametersConstant.MAXFILERANGE, svcName, entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(minfileRangeValid &&  maxFileRangeValid && parsingService.getMaxFileRange() < parsingService.getMinFileRange()){
			setErrorFieldErrorMessage(String.valueOf(parsingService.getMaxFileRange()), SystemParametersConstant.MAXFILERANGE, svcName, entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}
		
		isValidate(SystemParametersConstant.SERVICE_FILEGROUPPARAM_ARCHIVEPATH,parsingService.getFileGroupingParameter().getArchivePath(),"fileGroupingParameter.archivePath",entityName,parsingService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_ERRORPATH, parsingService.getErrorPath(), "errorPath", entityName, parsingService.getName(), validateForImport);
		
		if(parsingService.getDateFieldForSummary()!=null && !parsingService.getDateFieldForSummary().isEmpty())
			isValidate(SystemParametersConstant.SERVICE_DATEFIELDFORSUMMARY,parsingService.getDateFieldForSummary(),"dateFieldForSummary",entityName,parsingService.getName(),validateForImport);
	}
	
	
	private void validateConsolidationServiceParameter(DataConsolidationService consolidationService, String entityName, boolean validateForImport){
		
		String valClassName = consolidationService.getClass().getName();
		String svcName = consolidationService.getName();
		validateServiceBasicParam(consolidationService, errors, entityName, validateForImport);
		
		boolean minfileRangeValid=isValidate(SystemParametersConstant.SERVICE_MINFILERANGE, consolidationService.getMinFileRange(),"minFileRange", entityName, svcName, validateForImport,valClassName);		
		boolean fileBatchSizeValid=isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_FILEBATCHSIZE,service.getSvcExecParams().getFileBatchSize(), "svcExecParams.fileBatchSize", svcName, entityName, validateForImport, valClassName);
		boolean maxFileRangeValid= isValidate(SystemParametersConstant.SERVICE_MAXFILERANGE, ((DataConsolidationService) service).getMaxFileRange(), "maxFileRange", entityName, svcName, validateForImport, valClassName);
		isValidate(SystemParametersConstant.SERVICE_NOFILEALERT, consolidationService.getNoFileAlertInterval(), "noFileAlertInterval", entityName, svcName, validateForImport, valClassName);
		isValidate(SystemParametersConstant.CONSOLIDATION_ACROSSFILE_PARTITION, consolidationService.getAcrossFilePartition(), "acrossFilePartition", entityName, svcName, validateForImport, valClassName);
		isValidate(SystemParametersConstant.SERVICE_FILEGROUPPARAM_ARCHIVEPATH,consolidationService.getFileGroupParam().getArchivePath(),"fileGroupParam.archivePath",entityName,svcName,validateForImport);
		
		if(consolidationService.getConsolidationType().equals(ConsolidationTypeEnum.ACROSS_FILE)){
			boolean acrossMinFileRange = isValidate(SystemParametersConstant.CONSOLIDATION_ACROSSFILE_MINFILERANGE, consolidationService.getAcrossFileMinBatchSize(), "acrossFileMinBatchSize", entityName, svcName, validateForImport, valClassName);
			boolean acrossMaxFileRange = isValidate(SystemParametersConstant.CONSOLIDATION_ACROSSFILE_MAXFILERANGE, consolidationService.getAcrossFileMaxBatchSize(), "acrossFileMaxBatchSize", entityName, svcName, validateForImport, valClassName);
			if(acrossMinFileRange && acrossMaxFileRange){
				if(consolidationService.getAcrossFileMinBatchSize()  == -1){
					setErrorFieldErrorMessage(String.valueOf(consolidationService.getAcrossFileMaxBatchSize()), "acrossFileMinBatchSize", svcName, entityName, validateForImport, "consolidation.minFileRange.both.invalid", getMessage("consolidation.minFileRange.both.invalid"));
				}
				if(consolidationService.getAcrossFileMaxBatchSize() == -1){
					setErrorFieldErrorMessage(String.valueOf(consolidationService.getAcrossFileMaxBatchSize()), "acrossFileMaxBatchSize", svcName, entityName, validateForImport, "consolidation.maxFileRange.both.invalid", getMessage("consolidation.maxFileRange.both.invalid"));
				}
				if(consolidationService.getAcrossFileMaxBatchSize() < consolidationService.getAcrossFileMinBatchSize()){
					setErrorFieldErrorMessage(String.valueOf(consolidationService.getAcrossFileMaxBatchSize()), "acrossFileMaxBatchSize", svcName, entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
				}
			}
		}
		//business validations
		if(fileBatchSizeValid && maxFileRangeValid ){

			int value=((DataConsolidationService) service).getMaxFileRange()/service.getSvcExecParams().getFileBatchSize();
			value *= 10;
			if(service.getSvcExecParams().getQueueSize() <=value){			
				setErrorFieldErrorMessage(String.valueOf(service.getSvcExecParams().getQueueSize()), SystemParametersConstant.SERVICEEXEC_QUEUESIZE_PARAM_NAME, svcName, entityName, validateForImport, "Service.svcExecParams.queueSize.invalid.min",new Object[]{value}, getMessage("Service.svcExecParams.queueSize.invalid.min"));
			}	
		}
		
		if((minfileRangeValid &&  maxFileRangeValid ) && ((consolidationService.getMinFileRange()  == -1  &&  consolidationService.getMaxFileRange() >= 1) ||  (consolidationService.getMinFileRange()  >= 1  &&  consolidationService.getMaxFileRange() == -1))){
			setErrorFieldErrorMessage(String.valueOf(consolidationService.getMaxFileRange()), "maxFileRange", svcName, entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(minfileRangeValid &&  maxFileRangeValid && consolidationService.getMaxFileRange() < consolidationService.getMinFileRange()){
			setErrorFieldErrorMessage(String.valueOf(consolidationService.getMaxFileRange()), "maxFileRange", svcName, entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}

	}
	
private void validateAggreagationServiceParameter(AggregationService aggregationService, String entityName, boolean validateForImport){
		
		String valClassName = aggregationService.getClass().getName();
		String svcName = aggregationService.getName();
		
		//Operational Parameter validation
		boolean minfileRangeValid=isValidate(SystemParametersConstant.SERVICE_MINFILERANGE, aggregationService.getMinFileRange(),"minFileRange", entityName, svcName, validateForImport,valClassName);		
		boolean maxFileRangeValid= isValidate(SystemParametersConstant.SERVICE_MAXFILERANGE, aggregationService.getMaxFileRange(), "maxFileRange", entityName, svcName, validateForImport, valClassName);
		isValidate(SystemParametersConstant.SERVICE_NOFILEALERT,aggregationService.getNoFileAlert(),"noFileAlert",entityName,svcName,validateForImport,valClassName);
		isValidate(SystemParametersConstant.SERVICE_FILEGROUPPARAM_ARCHIVEPATH,aggregationService.getFileGroupingParameter().getArchivePath(),"fileGroupingParameter.archivePath",entityName,aggregationService.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_ERRORPATH, aggregationService.getErrorPath(), "errorPath", entityName, aggregationService.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVICE_DELIMITER, aggregationService.getDelimiter(), "delimiter", entityName, aggregationService.getName(), validateForImport);
		
		//business validations
		boolean fileBatchSizeValid=isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_FILEBATCHSIZE,service.getSvcExecParams().getFileBatchSize(),"svcExecParams.fileBatchSize",svcName,entityName,validateForImport,valClassName);
		if(fileBatchSizeValid && maxFileRangeValid ){
			int value=((AggregationService) service).getMaxFileRange()/service.getSvcExecParams().getFileBatchSize();
			value *= 10;
			if(service.getSvcExecParams().getQueueSize() <=value){			
				setErrorFieldErrorMessage(String.valueOf(service.getSvcExecParams().getQueueSize()), SystemParametersConstant.SERVICEEXEC_QUEUESIZE_PARAM_NAME, svcName, entityName, validateForImport, "Service.svcExecParams.queueSize.invalid.min",new Object[]{value}, getMessage("Service.svcExecParams.queueSize.invalid.min"));
			}	
		}
		
		if((minfileRangeValid &&  maxFileRangeValid ) && ((aggregationService.getMinFileRange()  == -1  &&  aggregationService.getMaxFileRange() >= 1) ||  (aggregationService.getMinFileRange()  >= 1  &&  aggregationService.getMaxFileRange() == -1))){
			setErrorFieldErrorMessage(String.valueOf(aggregationService.getMaxFileRange()), "maxFileRange", svcName, entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(minfileRangeValid &&  maxFileRangeValid && aggregationService.getMaxFileRange() < aggregationService.getMinFileRange()){
			setErrorFieldErrorMessage(String.valueOf(aggregationService.getMaxFileRange()), "maxFileRange", svcName, entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}
		isValidate(SystemParametersConstant.AGGREGATIONSERVICE_SVCEXECPARAMS_EXECUTIONINTERVAL,service.getSvcExecParams().getExecutionInterval(),"svcExecParams.executionInterval",svcName, entityName,validateForImport,valClassName);
		
	}
	private void validateProcessingServiceParameter(ProcessingService processingService,String entityName,boolean validateForImport){
		
		String valClassName=processingService.getClass().getName();
		String svcName=processingService.getName();
		
		validateServiceBasicParam(processingService, errors, entityName, validateForImport);		
		isValidate(SystemParametersConstant.SERVICE_RECORDBATCHSIZE,processingService.getRecordBatchSize(),"recordBatchSize",entityName,svcName,validateForImport,valClassName);
		isValidate(SystemParametersConstant.PROCESSING_ERROR_PATH, processingService.getErrorPath(), "errorPath", entityName, svcName, validateForImport);
		boolean minfileRangeValid=isValidate(SystemParametersConstant.SERVICE_MINFILERANGE,processingService.getMinFileRange(),"minFileRange",entityName,svcName,validateForImport,valClassName);		
		boolean fileBatchSizeValid=isValidate(SystemParametersConstant.SERVICE_SVCEXECPARAMS_FILEBATCHSIZE,service.getSvcExecParams().getFileBatchSize(),"svcExecParams.fileBatchSize",svcName,entityName,validateForImport,valClassName);
		boolean maxFileRangeValid= isValidate(SystemParametersConstant.SERVICE_MAXFILERANGE, ((ProcessingService) service).getMaxFileRange(), "maxFileRange", entityName,svcName,validateForImport,valClassName);
		isValidate(SystemParametersConstant.SERVICE_NOFILEALERT,processingService.getNoFileAlert(),"noFileAlert",entityName,svcName,validateForImport,valClassName);
		
		
		
		//business validations
		if(fileBatchSizeValid && maxFileRangeValid ){

			int value=((ProcessingService) service).getMaxFileRange()/service.getSvcExecParams().getFileBatchSize();
			value *= 10;
			if(service.getSvcExecParams().getQueueSize() <=value){			
				setErrorFieldErrorMessage(String.valueOf(service.getSvcExecParams().getQueueSize()), SystemParametersConstant.SERVICEEXEC_QUEUESIZE_PARAM_NAME, svcName, entityName, validateForImport, "Service.svcExecParams.queueSize.invalid.min",new Object[]{value}, getMessage("Service.svcExecParams.queueSize.invalid.min"));
			}	
		}
		
		if((minfileRangeValid &&  maxFileRangeValid ) && ( (processingService.getMinFileRange()  == -1  &&  processingService.getMaxFileRange() >= 1) ||  (processingService.getMinFileRange()  >= 1  &&  processingService.getMaxFileRange() == -1))){
			setErrorFieldErrorMessage(String.valueOf(processingService.getMaxFileRange()), "maxFileRange", svcName, entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(minfileRangeValid &&  maxFileRangeValid && processingService.getMaxFileRange() < processingService.getMinFileRange()){
			setErrorFieldErrorMessage(String.valueOf(processingService.getMaxFileRange()), "maxFileRange", svcName, entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}
		
		//Validate Duplicate Record Params if Enabled
		isValidate(SystemParametersConstant.PROCESSING_PURGE_CACHE_INTERVAL, processingService.getAcrossFileDuplicatePurgeCacheInterval(), "acrossFileDuplicatePurgeCacheInterval", entityName, svcName, validateForImport, valClassName);
		
		//Validate Global Sequence Parameters
		if(((ProcessingService)service).isGlobalSeqEnabled()){
			isValidate(SystemParametersConstant.PROCESSING_GLOBAL_DEVICE, processingService.getGlobalSeqDeviceName(), "globalSeqDeviceName", entityName, svcName, validateForImport);
			isValidate(SystemParametersConstant.PROCESSING_GLOBAL_MAX_LIMIT, processingService.getGlobalSeqMaxLimit(), "globalSeqMaxLimit", entityName, svcName, validateForImport, valClassName);
		}
		
		
		FileGroupingParameterProcessing fileGroupingParameterProcessing = ((ProcessingService)service).getFileGroupingParameter();
		if(fileGroupingParameterProcessing != null && fileGroupingParameterProcessing.isFileGroupEnable()){
			isValidate(SystemParametersConstant.PROCESSING_FILEGROUPING_ARCHIVEPATH, fileGroupingParameterProcessing.getArchivePath(), "fileGroupingParameter.archivePath", entityName, svcName, validateForImport);
			isValidate(SystemParametersConstant.PROCESSING_FILEGROUPING_FILTERPATH, fileGroupingParameterProcessing.getFilterDirPath(), "fileGroupingParameter.filterDirPath", entityName, svcName, validateForImport);
			isValidate(SystemParametersConstant.PROCESSING_FILEGROUPING_INVALIDPATH, fileGroupingParameterProcessing.getInvalidDirPath(), "fileGroupingParameter.invalidDirPath", entityName, svcName, validateForImport);
			isValidate(SystemParametersConstant.PROCESSING_FILEGROUPING_DUPLICATEPATH, fileGroupingParameterProcessing.getDuplicateDirPath(), "fileGroupingParameter.duplicateDirPath", entityName, svcName, validateForImport);
		}
	}
	
	/**
	 * Validate Consolidation Definition Parameters
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param validateForImport
	 */
	public void validateConsolidationDefinitionParams(Object target, Errors errors,List<ImportValidationErrors> importErrorList,boolean validateForImport){
		logger.info("Target class is : " + target.getClass());
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		isValidate(SystemParametersConstant.CONSOLIDATION_DEFINITION_NAME, ((DataConsolidation)target).getConsName(), "consName", null, null, validateForImport);
		isValidate(SystemParametersConstant.CONSOLIDATION_DEFINITION_ACROSSFILE_PARTITION, ((DataConsolidation)target).getAcrossFilePartition(), "acrossFilePartition", null, null, validateForImport, DataConsolidation.class.getName());
		//isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,((DataConsolidation)target).getDateFieldName(),"dateFieldName",null,((DataConsolidation)target).getDateFieldName(),validateForImport);

	}
	
	
	private void validateDiameterStackParam(DiameterCollectionService diameterService,String entityName,boolean validateForImport){
		String className=diameterService.getClass().getName();
		String svcName=diameterService.getName();
		
		validateIPAddress(diameterService.getStackIp(), "stackIp", entityName,svcName,validateForImport,"service.stackIP.invalid", getMessage("service.stackIP.invalid"));
				
		//will validate for both Regex and range.
		isValidate(SystemParametersConstant.SERVICE_STACKPORT,diameterService.getStackPort(),"stackPort",entityName,svcName,validateForImport,className);
		isValidate(SystemParametersConstant.DIAMETER_STACK_IDENTITY,diameterService.getStackIdentity(),"stackIdentity",entityName,diameterService.getStackIdentity(),validateForImport);
		isValidate(SystemParametersConstant.DIAMETER_STACK_REALM,diameterService.getStackRealm(),"stackRealm",entityName,diameterService.getStackRealm(),validateForImport);
		
	}
	
	private void validateDiameterOperationalParam(DiameterCollectionService diameterService,String entityName,boolean validateForImport){
		String className=diameterService.getClass().getName();
		String svcName=diameterService.getName();
				
		isValidate(SystemParametersConstant.DIAMETER_SESSION_CLEAN_UP_INTERVAL,diameterService.getSessionCleanupInterval(),"sessionCleanupInterval",entityName,svcName,validateForImport,className);
		isValidate(SystemParametersConstant.DIAMETER_SESSION_TIMEOUT,diameterService.getSessionTimeout(),"sessionTimeout",entityName,svcName,validateForImport,className);
		
		if (diameterService.getActionOnOverload() != null && diameterService.getActionOnOverload().equals("REJECT"))
		{
			isValidate(SystemParametersConstant.DIAMETER_RESULT_CODE_ON_OVERLOAD,diameterService.getResultCodeOnOverload(),"resultCodeOnOverload",entityName,svcName,validateForImport,className);
		}
		
		if (diameterService.isDuplicateRequestCheck())
		{
			isValidate(SystemParametersConstant.DIAMETER_DUPLICATE_PURGE_INTERVAL,diameterService.getDuplicatePurgeInterval(),"duplicatePurgeInterval",entityName,svcName,validateForImport,className);
		}
		
		// Output Packet Format
		isValidate(SystemParametersConstant.DIAMETER_SEPERATOR,diameterService.getFieldSeparator(),"fieldSeparator",entityName,diameterService.getFieldSeparator(),validateForImport);
		isValidate(SystemParametersConstant.DIAMETER_SEPERATOR,diameterService.getKeyValueSeparator(),"keyValueSeparator",entityName,diameterService.getKeyValueSeparator(),validateForImport);
		isValidate(SystemParametersConstant.DIAMETER_SEPERATOR,diameterService.getGroupFieldSeparator(),"groupFieldSeparator",entityName,diameterService.getGroupFieldSeparator(),validateForImport);
	}
}


