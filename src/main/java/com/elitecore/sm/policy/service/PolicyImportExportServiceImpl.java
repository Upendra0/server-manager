package com.elitecore.sm.policy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.policy.dao.IDatabaseQueryDao;
import com.elitecore.sm.policy.dao.IPolicyActionDao;
import com.elitecore.sm.policy.dao.IPolicyConditionDao;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.dao.IPolicyGroupDao;
import com.elitecore.sm.policy.dao.IPolicyGroupRelDao;
import com.elitecore.sm.policy.dao.IPolicyGroupRuleRelDao;
import com.elitecore.sm.policy.dao.IPolicyRuleDao;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.DatabaseQueryAction;
import com.elitecore.sm.policy.model.DatabaseQueryCondition;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.PolicyRuleActionRel;
import com.elitecore.sm.policy.model.PolicyRuleConditionRel;
import com.elitecore.sm.policy.model.wrapper.PolicyWrapper;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author brijesh.soni
 *
 */
@Service(value = "policyImportExportService")
public class PolicyImportExportServiceImpl implements IPolicyImportExportService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	IPolicyDao policyDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	@Autowired
	IPolicyConditionDao policyConditionDao;
	
	@Autowired
	IPolicyActionDao policyActionDao;
	
	@Autowired
	IPolicyRuleDao policyRuleDao;
	
	@Autowired
	IPolicyGroupRuleRelDao policyGroupRuleRelDao;
	
	@Autowired
	IPolicyGroupDao policyGroupDao;
	
	@Autowired
	IPolicyGroupRelDao policyGroupRelDao;
	
	@Autowired
	IDatabaseQueryDao databaseQueryDao;

	@Transactional
	@Override
	@Auditable(actionType = BaseConstants.EXPORT_ACTION, auditActivity = AuditConstants.EXPORT_POLICY, currentEntity = Policy.class, ignorePropList = "server,associationStatus,policyGroupRelSet")
	public ResponseObject getPolicyFullHierarchy(int policyId, boolean isExportForDelete, String tempPathForExport) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		String exportXmlPath = null;
		Policy policy;
		String policyName;
		
		logger.info("Call for Export configuration ");
		
		policy = policyDao.findByPrimaryKey(Policy.class, policyId);

		if (policy != null) {
			DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
			policyName = policy.getName().replaceAll(" ", "_");//NOSONAR
			exportXmlPath = tempPathForExport + File.separator + policyName + "_" + dateFormatter.format(new Date()) + ".xml";
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.EXPORT_FAIL);
			responseObject.setObject(null);
		}
		
		if (policy != null) {
			responseObject = this.getPolicyJAXB(exportXmlPath, policy.getId());
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.EXPORT_FAIL);
			responseObject.setObject(null);
		}
		return responseObject;
	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	@Auditable(actionType = BaseConstants.IMPORT_ACTION, auditActivity = AuditConstants.IMPORT_POLICY, currentEntity = Policy.class, ignorePropList = "server,associationStatus,policyGroupRelSet")
	public ResponseObject importPolicyConfig(int importServerInstanceId, int importPolicyId, File importFile, int staffId, int importMode, String jaxbXMLPath, boolean isCopy) throws SMException {
		ResponseObject responseObject;

		responseObject = unmarshalPolicyFromImportedFile(importFile, jaxbXMLPath);

		if (responseObject.isSuccess()) {

			PolicyWrapper exportedPolicyWrapper = (PolicyWrapper) responseObject.getObject();

			responseObject = importConfigUsingUnmarshllerPolicy(importServerInstanceId, importPolicyId, exportedPolicyWrapper, staffId, importMode,
					jaxbXMLPath);

			if (responseObject.isSuccess()) {
				logger.debug("Import operation performed successfully");
				responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_IMPORT_SUCCESS);
			} else {
				logger.debug("Import operation Fail");
				if (!(responseObject.getResponseCode() == ResponseCode.SERVICE_RUNNING)) {
					responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_IMPORT_FAIL);
				}
			}
			
		} else {
			logger.debug("UnMarshlling Fail For serverinstance");
		}
		return responseObject;
	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importPolicyCondition(ServerInstance serverInstance, PolicyCondition exportedPolicyCondition, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap, int importMode) {
		logger.debug("Call to import policy condition");
		switch(importMode) {
			case BaseConstants.IMPORT_MODE_ADD:
				importPolicyConditionAddMode(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_KEEP_BOTH:
				importPolicyConditionKeepBothMode(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_UPDATE:
				importPolicyConditionUpdateMode(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_OVERWRITE:
				importPolicyConditionOverwiteMode(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap);
				break;
			default:
				break;
		}
	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importPolicyAction(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap, int importMode) {
		logger.debug("Call to import policy action");
		switch(importMode) {
			case BaseConstants.IMPORT_MODE_ADD:
				importPolicyActionAddMode(serverInstance, exportedPolicyAction, policyActionMap, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_KEEP_BOTH:
				importPolicyActionKeepBothMode(serverInstance, exportedPolicyAction, policyActionMap, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_UPDATE:
				importPolicyActionUpdateMode(serverInstance, exportedPolicyAction, policyActionMap, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_OVERWRITE:
				importPolicyActionOverwriteMode(serverInstance, exportedPolicyAction, policyActionMap, databaseQueryMap);
				break;
			default:
				break;
		}
	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importDatabaseQuery(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap, int importMode) {
		logger.debug("Call to import database query");
		switch(importMode) {
			case BaseConstants.IMPORT_MODE_ADD:
				importDatabaseQueryAddMode(serverInstance, exportedDatabaseQuery, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_KEEP_BOTH:
				importDatabaseQueryKeepBothMode(serverInstance, exportedDatabaseQuery, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_UPDATE:
				importDatabaseQueryUpdateMode(serverInstance, exportedDatabaseQuery, databaseQueryMap);
				break;
			case BaseConstants.IMPORT_MODE_OVERWRITE:
				importDatabaseQueryOverwriteMode(serverInstance, exportedDatabaseQuery, databaseQueryMap);
				break;
			default:
				break;
		}
	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importPolicyRule(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap, int importMode) {
		logger.debug("Call to import policy rule");
		switch(importMode) {
			case BaseConstants.IMPORT_MODE_ADD:
				importPolicyRuleAddMode(serverInstance, exportedPolicyRule, policyRuleMap, policyConditionMap, policyActionMap);
				break;
			case BaseConstants.IMPORT_MODE_KEEP_BOTH:
				importPolicyRuleKeepBothMode(serverInstance, exportedPolicyRule, policyRuleMap, policyConditionMap, policyActionMap);
				break;
			case BaseConstants.IMPORT_MODE_UPDATE:
				importPolicyRuleUpdateMode(serverInstance, exportedPolicyRule, policyRuleMap, policyConditionMap, policyActionMap);
				break;
			case BaseConstants.IMPORT_MODE_OVERWRITE:
				importPolicyRuleOverwriteMode(serverInstance, exportedPolicyRule, policyRuleMap, policyConditionMap, policyActionMap);
				break;
			default:
				break;
		}
	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importPolicyGroup(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap, int importMode) {
		logger.debug("Call to import policy group");
		switch(importMode) {
			case BaseConstants.IMPORT_MODE_ADD:
				importPolicyGroupAddMode(serverInstance, exportedPolicyGroup, policyGroupMap, policyRuleMap);
				break;
			case BaseConstants.IMPORT_MODE_KEEP_BOTH:
				importPolicyGroupKeepBothMode(serverInstance, exportedPolicyGroup, policyGroupMap, policyRuleMap);
				break;
			case BaseConstants.IMPORT_MODE_UPDATE:
				importPolicyGroupUpdateMode(serverInstance, exportedPolicyGroup, policyGroupMap, policyRuleMap);
				break;
			case BaseConstants.IMPORT_MODE_OVERWRITE:
				importPolicyGroupOverwriteMode(serverInstance, exportedPolicyGroup, policyGroupMap, policyRuleMap);
				break;
			default:
				break;
		}
	}
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importPolicyForServerInstance(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap, int importMode) {
		logger.debug("Call to import policy for server instance");
		switch(importMode) {
			case BaseConstants.IMPORT_MODE_ADD:
				importPolicyForAddMode(serverInstance, exportedPolicy, policyMap, policyGroupMap);
				break;
			case BaseConstants.IMPORT_MODE_KEEP_BOTH:
				importPolicyForKeepBothMode(serverInstance, exportedPolicy, policyMap, policyGroupMap);
				break;
			case BaseConstants.IMPORT_MODE_UPDATE:
				importPolicyForUpdateMode(serverInstance, exportedPolicy, policyMap, policyGroupMap);
				break;
			case BaseConstants.IMPORT_MODE_OVERWRITE:
				importPolicyForOverwriteMode(serverInstance, exportedPolicy, policyMap, policyGroupMap);
				break;
			default:
				break;
		}
	}
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public void importPolicy(ServerInstance serverInstance, Policy exportedPolicy, int dbPolicyId, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap) {
		logger.debug("Call to import policy");
		Policy policyDb =null;
		if(dbPolicyId>0){
			policyDb = policyDao.findByPrimaryKey(Policy.class, dbPolicyId);
		}else{
			policyDb=policyDao.getPolicyByAlias(exportedPolicy.getAlias(), serverInstance.getId());
		}
		Date date = new Date();
		if(policyDb != null) {
			String oldPolicyName = exportedPolicy.getAlias();
			policyDb.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			policyDb.setCreatedDate(date);
			policyDb.setLastUpdatedDate(date);
			List<String> policyGroups = exportedPolicy.getPolicyGroups();
			
			List<PolicyGroupRel> policyGroupRelSet = policyDb.getPolicyGroupRelSet();
			int applicationOrader = 1;
			for (String policyGroup : policyGroups) {
				if (policyGroupMap.containsKey(policyGroup)) {
					PolicyGroup tempPolicyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroupMap.get(policyGroup));
					if(!isPolicyGroupAlreadyAssociatedWithPolicy(policyGroupRelSet, tempPolicyGroup)) {
						PolicyGroupRel tempPolicyRuleRel = new PolicyGroupRel();
						tempPolicyRuleRel.setId(0);
						tempPolicyRuleRel.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
						tempPolicyRuleRel.setCreatedDate(date);
						tempPolicyRuleRel.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
						tempPolicyRuleRel.setLastUpdatedDate(date);
						tempPolicyRuleRel.setApplicationOrder(applicationOrader++);
						tempPolicyRuleRel.setGroup(tempPolicyGroup);
						tempPolicyRuleRel.setPolicy(policyDb);
						policyGroupRelSet.add(tempPolicyRuleRel);
					}
				}
			}
			policyDb.setPolicyGroupRelSet(policyGroupRelSet);
			
			policyDao.merge(policyDb);
			
			updatePolicyInList(serverInstance.getPolicyList(), policyDb);
			
			updatePolicyMap(policyMap, oldPolicyName, policyDb.getId());
		} else {
			Policy policy = new Policy();
			policy.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicy.getName()));
			policy.setAlias(exportedPolicy.getAlias());
			policy.setStatus(StateEnum.ACTIVE);
			policy.setServer(serverInstance);
			policy.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			policy.setCreatedDate(date);
			policy.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			policy.setLastUpdatedDate(date);
			policyDao.save(policy);
			importPolicy(serverInstance,exportedPolicy,policy.getId(),policyMap,policyGroupMap);
		}
	}

	@Transactional
	@Override
	public ResponseObject removePolicyDependant(String policyAlias, int serverInstanceId, int dbPolicyId) {
		logger.debug("Call to remove policy dependant.");
		ResponseObject responseObject = new ResponseObject();
		Policy policy = null;
		if (dbPolicyId > 0) {
			policy = policyDao.findByPrimaryKey(Policy.class, dbPolicyId);
		} else {
			policy = policyDao.getPolicyByAlias(policyAlias, serverInstanceId);
		}
		if (policy != null) {
			for (PolicyGroupRel policyGroupRel : policy.getPolicyGroupRelSet()) {
				policyGroupRelDao.deletePolicyGroupRel(policyGroupRel);
			}
			Iterator<PolicyGroupRel> policyGroupRuleRelIter = policy.getPolicyGroupRelSet().iterator();

			while (policyGroupRuleRelIter.hasNext()) {
				policyGroupRuleRelIter.next();
				policyGroupRuleRelIter.remove();
			}
			policyDao.merge(policy);
		}
		// policyDao.evict(policy);

		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.POLICY_REMOVE_SUCCESS);

		return responseObject;
	}

	/**
	 * Iterate over policy and create jaxb xml
	 * 
	 * @param exportXmlPath
	 * @param policyId
	 * @return
	 * @throws SMException
	 */
	public ResponseObject getPolicyJAXB(String exportXmlPath, int policyId) throws SMException {
		logger.debug("Call to get policy jaxb");
		JAXBContext jaxbContext;
		ResponseObject responseObject = new ResponseObject();

		if (exportXmlPath != null) {
			try {
				PolicyWrapper policyWrapper = getPolicyWrapper(policyId);
				File exportxml = new File(exportXmlPath);
				jaxbContext = JAXBContext.newInstance(PolicyWrapper.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(policyWrapper, exportxml);

				responseObject = generateJAXBXml(exportxml, exportXmlPath, policyWrapper);

			} catch (JAXBException e) {
				logger.error("Export backup path is not valid ", e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.INVALID_EXPORT_BACKUP_PATH);
				responseObject.setArgs(new Object[] { exportXmlPath });
			}

		}
		return responseObject;

	}
	
	/**
	 * Write JAXB XML content into stringbuilder
	 * 
	 * @param exportxml
	 * @param exportXmlPath
	 * @return
	 */
	public ResponseObject generateJAXBXml(File exportxml, String exportXmlPath, PolicyWrapper policy) {

		ResponseObject responseObject = new ResponseObject();

		try (BufferedReader br = new BufferedReader(new FileReader(exportxml))) {

			Map<String, Object> policyJAXB;
			String fileContent;
			StringBuilder sb = new StringBuilder();
			while ((fileContent = br.readLine()) != null) {
				sb.append(fileContent);
			}
			logger.debug("JAXB XML is " + sb.toString());

			policyJAXB = new HashMap<>();
			policyJAXB.put(BaseConstants.POLICY_EXPORT_FILE, exportxml);
			policyJAXB.put(BaseConstants.SERVICE_JAXB_OBJECT, policy);
			responseObject.setSuccess(true);
			responseObject.setObject(policyJAXB);

		} catch (IOException e) {
			logger.error("Export backup path is not valid ", e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.INVALID_EXPORT_BACKUP_PATH);
			responseObject.setArgs(new Object[] { exportXmlPath });
		}
		return responseObject;
	}
	
	/**
	 * This method will return policy wrapper object from policy
	 * @param policyId
	 * @return PolicyWrapper
	 * @throws SMException
	 */
	public PolicyWrapper getPolicyWrapper(int policyId) throws SMException {
		logger.debug("Call to get policy wrapper from db policy");
		Policy policy = policyDao.getPolicyFullHierarchy(policyId);
		PolicyWrapper policyWrapper = new PolicyWrapper();
		List<PolicyGroupRel>  policyGroupRels = policy.getPolicyGroupRelSet();
		List<Policy> policyList = new ArrayList<>();
		policyList.add(policy);
		List<PolicyGroup> policyGroupList = new ArrayList<>();
		List<PolicyRule> policyRuleList = new ArrayList<>();
		List<PolicyCondition> policyConditionList = new ArrayList<>();
		List<PolicyAction> policyActionList = new ArrayList<>();
		List<DatabaseQuery> databaseQueryList = new ArrayList<>();
		
		if(policyGroupRels != null && !policyGroupRels.isEmpty()) {
			int policyGroupLength = policyGroupRels.size();
			for(int i = policyGroupLength-1; i >= 0 ; i--) {
				PolicyGroupRel policyGroupRel = policyGroupRels.get(i);
				if(policyGroupRel != null) {
					//get all unique policy group
					PolicyGroup policyGroup = policyGroupRel.getGroup();
					if(!isPolicyGroupAlreadyAssociatedWithList(policyGroupList, policyGroup)) {
						policyGroupList.add(policyGroup);
						List<PolicyGroupRuleRel> policyGroupRuleRels =  policyGroup.getPolicyGroupRuleRelSet();
						if(policyGroupRuleRels != null && !policyGroupRuleRels.isEmpty()) {
							int policyGroupRuleRelLength = policyGroupRuleRels.size();
							for(int j = policyGroupRuleRelLength-1; j >= 0; j--) {
								PolicyGroupRuleRel policyGroupRuleRel = policyGroupRuleRels.get(j);
								if(policyGroupRuleRel != null) {
									//get all unique policy rule
									PolicyRule policyRule = policyGroupRuleRel.getPolicyRule();
									if(!isPolicyRuleAlreadyAssociatedWithList(policyRuleList, policyRule)) {
										policyRuleList.add(policyRule);
										//get all unique policy conditions
										List<PolicyRuleConditionRel> policyRuleConditionRels = policyRule.getPolicyRuleConditionRel();
										if(policyRuleConditionRels != null && !policyRuleConditionRels.isEmpty()){
											int policyRuleConditionRelLength = policyRuleConditionRels.size();
											for(int k = policyRuleConditionRelLength-1; k >= 0; k--){
												PolicyRuleConditionRel policyRuleConditionRel = policyRuleConditionRels.get(k);
												if(policyRuleConditionRel != null){
													PolicyCondition policyCondition = policyRuleConditionRel.getCondition();
													if(!isPolicyConditionAlreadyAssociatedWithList(policyConditionList, policyCondition)) {
														policyConditionList.add(policyCondition);
														if(policyCondition.getDatabaseQuery() != null) {
															if(!isDatabaseQueryAlreadyAssociatedWithList(databaseQueryList, policyCondition.getDatabaseQuery())) {
																databaseQueryList.add(policyCondition.getDatabaseQuery());
															}
														}
													}	
												}
											}			
										}
										//get all unique policy actions
										List<PolicyRuleActionRel> policyRuleActionRels = policyRule.getPolicyRuleActionRel();
										if(policyRuleActionRels != null && !policyRuleActionRels.isEmpty()){
											int policyRuleActionRelLength = policyRuleActionRels.size();
											for(int k = policyRuleActionRelLength-1; k >= 0; k--){
												PolicyRuleActionRel policyRuleActionRel = policyRuleActionRels.get(k);
												if(policyRuleActionRel != null){
													PolicyAction policyAction = policyRuleActionRel.getAction();
													if(!isPolicyActionAlreadyAssociatedWithList(policyActionList, policyAction)) {
														policyActionList.add(policyAction);
														if(policyAction.getDatabaseQuery() != null) {
															if(!isDatabaseQueryAlreadyAssociatedWithList(databaseQueryList, policyAction.getDatabaseQuery())) {
																databaseQueryList.add(policyAction.getDatabaseQuery());
															}
														}
													}
												}
											}			
										}
									}
								}
							}
						}
					}
				}
			}
		}
		policyWrapper.setPolicyGroupList(policyGroupList);
		policyWrapper.setPolicyRuleList(policyRuleList);
		policyWrapper.setPolicyConditionList(policyConditionList);
		policyWrapper.setPolicyActionList(policyActionList);
		policyWrapper.setPolicyList(policyList);
		policyWrapper.setDatabaseQueryList(databaseQueryList);
		return policyWrapper;
	}
	
	/**
	 * @param policyGroupList
	 * @param policyGroup
	 * @see This method returns true if given PolicyGroup is already there in given list of PolicyGroup
	 * @return boolean
	 */
	public boolean isPolicyGroupAlreadyAssociatedWithList(List<PolicyGroup> policyGroupList, PolicyGroup policyGroup) {
		if(policyGroupList != null && policyGroup != null && !policyGroupList.isEmpty()) {
			int length = policyGroupList.size();
			for(int i = length-1; i >=0; i--) {
				PolicyGroup group = policyGroupList.get(i);
				if(group != null && group.getAlias().equalsIgnoreCase(policyGroup.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param policyRuleList
	 * @param policyRule
	 * @see This method returns true if given PolicyRule is already there in given list of PolicyRule
	 * @return boolean
	 */
	public boolean isPolicyRuleAlreadyAssociatedWithList(List<PolicyRule> policyRuleList, PolicyRule policyRule) {
		if(policyRuleList != null && policyRule != null && !policyRuleList.isEmpty()) {
			int length = policyRuleList.size();
			for(int i = length-1; i >= 0; i--) {
				PolicyRule rule = policyRuleList.get(i);
				if(rule != null && rule.getAlias().equalsIgnoreCase(policyRule.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param policyConditionList
	 * @param policyCondition
	 * @see This method returns true if given PolicyCondition is already there in given list of PolicyCondition
	 * @return boolean
	 */
	public boolean isPolicyConditionAlreadyAssociatedWithList(List<PolicyCondition> policyConditionList, PolicyCondition policyCondition) {
		if(policyConditionList != null && policyCondition != null && !policyConditionList.isEmpty()) {
			int length = policyConditionList.size();
			for(int i = length-1; i >= 0; i--) {
				PolicyCondition condition = policyConditionList.get(i);
				if(condition != null && condition.getAlias().equalsIgnoreCase(policyCondition.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isPolicyConditionAlreadyAssociatedWithRelList(List<PolicyRuleConditionRel> policyRuleConditionRelList, PolicyCondition policyCondition) {
		if(policyRuleConditionRelList != null && policyCondition != null && !policyRuleConditionRelList.isEmpty()) {
			int length = policyRuleConditionRelList.size();
			for(int i = length-1; i >= 0; i--) {
				PolicyRuleConditionRel policyRuleConditionRel = policyRuleConditionRelList.get(i);
				if(policyRuleConditionRel != null && policyRuleConditionRel.getCondition().getAlias().equalsIgnoreCase(policyCondition.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param policyActionList
	 * @param policyAction
	 * @see This method returns true if given PolicyAction is already there in given list of PolicyAction
	 * @return boolean
	 */
	public boolean isPolicyActionAlreadyAssociatedWithList(List<PolicyAction> policyActionList, PolicyAction policyAction) {
		if(policyActionList != null && policyAction != null && !policyActionList.isEmpty()) {
			int length = policyActionList.size();
			for(int i = length-1; i >= 0; i--) {
				PolicyAction action = policyActionList.get(i);
				if(action != null && action.getAlias().equalsIgnoreCase(policyAction.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isPolicyActionAlreadyAssociatedWithRelList(List<PolicyRuleActionRel> policyRuleActionRelList, PolicyAction policyAction) {
		if(policyRuleActionRelList != null && policyAction != null && !policyRuleActionRelList.isEmpty()) {
			int length = policyRuleActionRelList.size();
			for(int i = length-1; i >= 0; i--) {
				PolicyRuleActionRel policyRuleActionRel = policyRuleActionRelList.get(i);
				if(policyRuleActionRel != null && policyRuleActionRel.getAction().getAlias().equalsIgnoreCase(policyAction.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param databaseQueryList
	 * @param databaseQuery
	 * @see This method returns true if given DatabaseQuery is already there in given list of DatabaseQuery
	 * @return boolean
	 */
	public boolean isDatabaseQueryAlreadyAssociatedWithList(List<DatabaseQuery> databaseQueryList, DatabaseQuery databaseQuery) {
		if(databaseQueryList != null && databaseQuery != null && !databaseQueryList.isEmpty()) {
			int length = databaseQueryList.size();
			for(int i = length-1; i >= 0; i--) {
				DatabaseQuery query = databaseQueryList.get(i);
				if(query != null && query.getAlias().equalsIgnoreCase(databaseQuery.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param importFile
	 * @param jaxbXMLPath
	 * @see This method will unmarshal policy from imported file
	 * @return ResponseObject
	 */
	@Transactional(rollbackFor = SMException.class)
	public ResponseObject unmarshalPolicyFromImportedFile(File importFile, String jaxbXMLPath) {

		ResponseObject responseObject = new ResponseObject();
		final JSONArray finaljArray = new JSONArray();
		try {
			ValidationEventHandler validator = event -> {
				logger.debug("XSD Validation error occured");
				JSONArray jArray = new JSONArray();
				jArray.put(event.getLocator().getLineNumber());
				jArray.put(event.getMessage());
				finaljArray.put(jArray);
				return true;
			};
			// Unmarshal policy

			String xsdPAth = jaxbXMLPath + File.separator + "Import_validation_schema_for_policy.xsd";
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);//NOSONAR
			Schema schema = sf.newSchema(new File(xsdPAth));

			JAXBContext jaxbContext = JAXBContext.newInstance(PolicyWrapper.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			jaxbUnmarshaller.setSchema(schema);
			jaxbUnmarshaller.setEventHandler(validator);
			PolicyWrapper policyWrapper = (PolicyWrapper) jaxbUnmarshaller.unmarshal(importFile);

			if (finaljArray.length() > 0) {
				logger.debug("XSD Validation fail , so return validation error");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.IMPORT_XSD_VALIDATION_FAIL);
				responseObject.setObject(finaljArray);
			} else {
				logger.debug("XSD Validation Done Successfully");
				responseObject.setSuccess(true);
				responseObject.setObject(policyWrapper);
			}
		} catch (ClassCastException c) {
			logger.error("Class Cast Exception occure while unmarshlling" + c);
			responseObject.setResponseCode(ResponseCode.IMPORT_UNMARSHALL_FAIL_CLASSCAST);
			responseObject.setSuccess(false);
		} catch (JAXBException e) {
			logger.error("faced error in import", e);
			if (e.getCause() != null) {
				logger.error("Unable to unamarshall policy xml file due to  :" + e.getCause().getMessage());
				responseObject.setObject(String.valueOf(e.getCause().getMessage()));
			} else {
				logger.error("Unable to unamarshall policy xml file due to  :" + e.getMessage());
				responseObject.setObject(String.valueOf(e.getMessage()));
			}
			responseObject.setResponseCode(ResponseCode.POLICY_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
		} catch (SAXException e) {
			logger.error("issue in unmarsalling", e);
			responseObject.setResponseCode(ResponseCode.POLICY_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	/**
	 * @param importServerInstanceId
	 * @param dbPolicyId
	 * @param exportedPolicyWrapper
	 * @param staffId
	 * @param importMode
	 * @param jaxbXMLPath
	 * @return ResponseObject
	 * @see This method will import configuration using unmarshaller
	 * @throws SMException
	 */
	@Transactional(rollbackFor = SMException.class)
	public ResponseObject importConfigUsingUnmarshllerPolicy(int importServerInstanceId, int dbPolicyId, PolicyWrapper exportedPolicyWrapper, int staffId,
			int importMode, String jaxbXMLPath) throws SMException {

		ResponseObject responseObject = new ResponseObject() ;
		ServerInstance serverInstanceDB = serverInstanceDao.findByPrimaryKey(ServerInstance.class, importServerInstanceId);
		if (serverInstanceDB != null) {

			responseObject.setSuccess(true);

			// if import mode is overwrite than delete all policy relations
			if (importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
				logger.debug("Option : Import mode Override :  Delete Old Policy relations");
				for(Policy policy:exportedPolicyWrapper.getPolicyList()){
					responseObject = removePolicyDependant(policy.getAlias(),importServerInstanceId,dbPolicyId);
				}
			}
			
			if (responseObject.isSuccess()) {
				List<PolicyCondition> policyConditionList = exportedPolicyWrapper.getPolicyConditionList();
				List<PolicyAction> policyActionList = exportedPolicyWrapper.getPolicyActionList();
				List<PolicyRule> policyRuleList = exportedPolicyWrapper.getPolicyRuleList();
				List<PolicyGroup> policyGroupList = exportedPolicyWrapper.getPolicyGroupList();
				List<Policy> policyList = exportedPolicyWrapper.getPolicyList();
				List<DatabaseQuery> databaseQueries = exportedPolicyWrapper.getDatabaseQueryList();

				Map<String, Integer> policyConditionMap = new HashMap<>();
				Map<String, Integer> policyActionMap = new HashMap<>();
				Map<String, Integer> policyRuleMap = new HashMap<>();
				Map<String, Integer> policyGroupMap = new HashMap<>();
				Map<String, Integer> policyMap = new HashMap<>();
				Map<String, Integer> databaseQueryMap = new HashMap<>();

				logger.debug("Import Database Query");
				if(databaseQueries != null && !databaseQueries.isEmpty()) {
					int databaseQueriesLength = databaseQueries.size();
					for(int i = databaseQueriesLength-1; i >= 0; i--) {
						importDatabaseQuery(serverInstanceDB, databaseQueries.get(i), databaseQueryMap, importMode);
					}
				}
				
				logger.debug("Import Policy Condition");
				if(policyConditionList != null && !policyConditionList.isEmpty()) {
					int policyConditionLength = policyConditionList.size();
					for(int i = policyConditionLength-1; i >= 0; i--) {
						importPolicyCondition(serverInstanceDB, policyConditionList.get(i), policyConditionMap, databaseQueryMap, importMode);
					}
				}
				
				logger.debug("Import Policy Action");
				if(policyActionList != null && !policyActionList.isEmpty()) {
					int policyActionLength = policyActionList.size();
					for(int i = policyActionLength-1; i >= 0; i--) {
						importPolicyAction(serverInstanceDB, policyActionList.get(i), policyActionMap, databaseQueryMap, importMode);
					}
				}

				logger.debug("Import Policy Rule");
				if(policyRuleList != null && !policyRuleList.isEmpty()) {
					int policyRuleLength = policyRuleList.size();
					for(int i = policyRuleLength-1; i >= 0; i--) {
						importPolicyRule(serverInstanceDB, policyRuleList.get(i), policyRuleMap, policyConditionMap, policyActionMap, importMode);
					}
				}

				logger.debug("Import Policy Rule Group");
				if(policyGroupList != null && !policyGroupList.isEmpty()) {
					int policyGroupLength = policyGroupList.size();
					for(int i = policyGroupLength-1; i >= 0; i--) {
						importPolicyGroup(serverInstanceDB, policyGroupList.get(i), policyGroupMap, policyRuleMap, importMode);
					}
				}

				logger.debug("Import Policy");
				if(policyList != null && !policyList.isEmpty()) {
					// in this case there will be always one policy in policy list
					int policyLength = policyList.size();
					for(int i = policyLength-1; i >= 0; i--) {
						importPolicy(serverInstanceDB, policyList.get(i), dbPolicyId, policyMap, policyGroupMap);
						responseObject.setObject(policyDao.findByPrimaryKey(Policy.class, policyList.get(i).getId()));
					}
				}
				
			} else {
				logger.debug("Fail to remove policy relations");
				responseObject.setSuccess(false);
			}
		} else {
			logger.debug("Fail to fetch Server instance from database");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	/**
	 * @param exportedPolicyCondition
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param databaseQueryMap
	 * @see This method will import policy condition for add mode 
	 */
	public void importPolicyConditionAddMode(PolicyCondition exportedPolicyCondition, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap) {
		//get policy condition from db. if exists then skip, otherwise save
		PolicyCondition policyConditionDb = policyConditionDao.getPolicyConditionByAlias(exportedPolicyCondition.getAlias(), serverInstance.getId());
		if(policyConditionDb == null) {
			//add policy condition
			savePolicyConditionForImport(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap, null);
		} else {
			//update policy condition map
			updatePolicyConditionMap(policyConditionMap, policyConditionDb.getAlias(), policyConditionDb.getId());
		}
	}
	
	/**
	 * @param exportedPolicyCondition
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param databaseQueryMap
	 * @see This method will import policy condition for update mode
	 */
	public void importPolicyConditionUpdateMode(PolicyCondition exportedPolicyCondition, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap) {
		//get policy condition from db. if found then update that. otherwise add new
		PolicyCondition policyConditionDb = policyConditionDao.getPolicyConditionByAlias(exportedPolicyCondition.getAlias(), serverInstance.getId());
		if(policyConditionDb != null) {
			//update policy condition
			updatePolicyConditionForImport(policyConditionDb, exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap);
		} else {
			//add policy condition
			savePolicyConditionForImport(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap, null);
		}
	}
	
	/**
	 * @param policyConditionDb
	 * @param exportedPolicyCondition
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param databaseQueryMap
	 * @see This method will update policy condition for import and update policy condition map
	 */
	public void updatePolicyConditionForImport(PolicyCondition policyConditionDb, PolicyCondition exportedPolicyCondition, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap) {
		String oldPolicyConditionName = policyConditionDb.getAlias();
		policyConditionDb.setUnifiedField(exportedPolicyCondition.getUnifiedField());
		policyConditionDb.setOperator(exportedPolicyCondition.getOperator());
		policyConditionDb.setValue(exportedPolicyCondition.getValue());
		policyConditionDb.setConditionExpression(exportedPolicyCondition.getConditionExpression());		
		policyConditionDb.setType(exportedPolicyCondition.getType());
		policyConditionDb.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyConditionDb.setLastUpdatedDate(new Date());
		String databaseQueryAlias = exportedPolicyCondition.getDatabaseQueryAlias(); 
		if(databaseQueryAlias != null && databaseQueryMap.containsKey(databaseQueryAlias)) {
			DatabaseQuery databaseQuery = databaseQueryDao.findByPrimaryKey(DatabaseQuery.class, databaseQueryMap.get(databaseQueryAlias));
			if(databaseQuery != null) {
				policyConditionDb.setDatabaseQuery(databaseQuery);
				policyConditionDb.setDatabaseQueryAlias(databaseQuery.getAlias());
				policyConditionDb.setValue(databaseQuery.getAlias());
			} else {
				policyConditionDb.setDatabaseQuery(null);
				policyConditionDb.setDatabaseQueryAlias(null);
			}
		} else {
			policyConditionDb.setDatabaseQuery(null);
			policyConditionDb.setDatabaseQueryAlias(null);
		}
		policyConditionDb.setPolicyRuleConditionRel(exportedPolicyCondition.getPolicyRuleConditionRel());
		
		String conditionExpressionForSync = exportedPolicyCondition.getConditionExpression();
		if(conditionExpressionForSync!=null){				
			policyConditionDb.setConditionExpressionForSync(conditionExpressionForSync.replaceAll("'", ""));//NOSONAR
		}
		
		policyConditionDao.update(policyConditionDb);
		updatePolicyConditionInList(serverInstance.getPolicyConditionList(), policyConditionDb);
		logger.debug("After Policy Condition Update , new Id for policy condition name is : " + policyConditionDb.getName() + " id is ::  " + policyConditionDb.getId());
		updatePolicyConditionMap(policyConditionMap, oldPolicyConditionName, policyConditionDb.getId());
	}
	
	/**
	 * @param exportedPolicyCondition
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param databaseQueryMap
	 * @see This method will import policy condition for keepboth mode
	 */
	public void importPolicyConditionKeepBothMode(PolicyCondition exportedPolicyCondition, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap) {
		//add new policy condition with suffix _import
		String keepBothOldName = exportedPolicyCondition.getAlias();
		exportedPolicyCondition.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyCondition.getName()));
		exportedPolicyCondition.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyCondition.getAlias()));
		savePolicyConditionForImport(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap, keepBothOldName);
	}
	
	/**
	 * @param exportedPolicyCondition
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param databaseQueryMap
	 * @see This method will import policy condition for overwrite mode
	 */
	public void importPolicyConditionOverwiteMode(PolicyCondition exportedPolicyCondition, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap) {
		//add policy condition (in previous method, flush done)
		PolicyCondition policyConditionDb = policyConditionDao.getPolicyConditionByAlias(exportedPolicyCondition.getAlias(), serverInstance.getId());
		if(policyConditionDb == null) {
			//if policy condition is not there, add
			savePolicyConditionForImport(exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap, null);
		} else {
			//if policy condition is already there then update
			updatePolicyConditionForImport(policyConditionDb, exportedPolicyCondition, serverInstance, policyConditionMap, databaseQueryMap);
		}
		
	}
	
	/**
	 * @param policyConditionMap
	 * @param oldPolicyConditionName
	 * @param policyConditionId
	 * @see This method will update policy condition map
	 */
	public void updatePolicyConditionMap(Map<String, Integer> policyConditionMap, String oldPolicyConditionName, int policyConditionId) {
		if (policyConditionMap != null && oldPolicyConditionName != null && !oldPolicyConditionName.isEmpty()) {
			policyConditionMap.put(oldPolicyConditionName, policyConditionId);
		}
	}
	
	/**
	 * @param exportedPolicyCondition
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param databaseQueryMap
	 * @param keepBothOldName
	 * @see This method will save policy condition for import and update policy condition map
	 */
	public void savePolicyConditionForImport(PolicyCondition exportedPolicyCondition, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap, String keepBothOldName) {
		String oldPolicyConditionName = exportedPolicyCondition.getAlias();
		savePolicyCondition(exportedPolicyCondition, serverInstance, databaseQueryMap);
		logger.debug("After Policy Condition Save , new Id for policy condition name is : " + exportedPolicyCondition.getName() + " id is ::  " + exportedPolicyCondition.getId());
		if(keepBothOldName != null) {
			updatePolicyConditionMap(policyConditionMap, keepBothOldName, exportedPolicyCondition.getId());
		} else {
			updatePolicyConditionMap(policyConditionMap, oldPolicyConditionName, exportedPolicyCondition.getId());
		}
		
	}
	
	/**
	 * @param policyCondition
	 * @param serverInstance
	 * @param databaseQueryMap
	 * @see This method will save policy action
	 */
	public void savePolicyCondition(PolicyCondition policyCondition, ServerInstance serverInstance, Map<String, Integer> databaseQueryMap) {
		policyCondition.setId(0);
		policyCondition.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyCondition.setCreatedDate(new Date());
		policyCondition.setServer(serverInstance);
		policyCondition.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		String databaseQueryAlias = policyCondition.getDatabaseQueryAlias();
		if(databaseQueryAlias != null && databaseQueryMap.containsKey(databaseQueryAlias)) {
			DatabaseQuery databaseQuery = databaseQueryDao.findByPrimaryKey(DatabaseQuery.class, databaseQueryMap.get(databaseQueryAlias));
			if(databaseQuery != null) {
				policyCondition.setDatabaseQuery(databaseQuery);
				policyCondition.setDatabaseQueryAlias(databaseQuery.getAlias());
				policyCondition.setValue(databaseQuery.getAlias());
			}
		}
		String conditionExpressionForSync = policyCondition.getConditionExpression();
		if(conditionExpressionForSync!=null){				
			policyCondition.setConditionExpressionForSync(conditionExpressionForSync.replaceAll("'", ""));//NOSONAR
		}
		policyConditionDao.save(policyCondition);
		updatePolicyConditionInList(serverInstance.getPolicyConditionList(), policyCondition);
	}
	
	public void updatePolicyConditionInList(List<PolicyCondition> policyConditionList, PolicyCondition condition) {
		if(policyConditionList != null && condition != null) {
			int length= policyConditionList.size();
			boolean isConditionAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				PolicyCondition policyCondition = policyConditionList.get(i);
				if(policyCondition != null && !policyCondition.getStatus().equals(StateEnum.DELETED)
						&& policyCondition.getAlias().equalsIgnoreCase(condition.getAlias())) {
					isConditionAvailable = true;
					policyConditionList.set(i, condition);
					break;
				}
			}
			if(!isConditionAvailable) {
				policyConditionList.add(condition);
			}
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyAction
	 * @param policyActionMap
	 * @param databaseQueryMap
	 * @see This method will import policy action for add mode
	 */
	public void importPolicyActionAddMode(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap) {
		//get policy action from db. if exists then skip, otherwise save
		PolicyAction policyActionDb = policyActionDao.getPolicyActionByAlias(exportedPolicyAction.getAlias(), serverInstance.getId());
		if(policyActionDb == null) {
			//add policy action
			savePolicyActionForImport(exportedPolicyAction, serverInstance, policyActionMap, databaseQueryMap, null);
		} else {
			//update policy condition map
			updatePolicyActionMap(policyActionMap, policyActionDb.getAlias(), policyActionDb.getId());
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyAction
	 * @param policyActionMap
	 * @param databaseQueryMap
	 * @see This method will import policy action for update mode
	 */
	public void importPolicyActionUpdateMode(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap) {
		//get policy action from db. if found then update that. otherwise add new
		PolicyAction policyActionDb = policyActionDao.getPolicyActionByAlias(exportedPolicyAction.getAlias(), serverInstance.getId());
		if(policyActionDb != null) {
			//update policy action
			updatePolicyActionForImport(policyActionDb, exportedPolicyAction, serverInstance, policyActionMap, databaseQueryMap);
		} else {
			//add policy action
			savePolicyActionForImport(exportedPolicyAction, serverInstance, policyActionMap, databaseQueryMap, null);
		}
	}
	
	/**
	 * @param policyActionDb
	 * @param exportedPolicyAction
	 * @param serverInstance
	 * @param policyActionMap
	 * @param databaseQueryMap
	 * @see This method will update policy action for import and update policy action map
	 */
	public void updatePolicyActionForImport(PolicyAction policyActionDb, PolicyAction exportedPolicyAction, ServerInstance serverInstance, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap) {
		String oldPolicyActionName = exportedPolicyAction.getAlias();
		policyActionDb.setAction(exportedPolicyAction.getAction());
		policyActionDb.setOperator(exportedPolicyAction.getOperator());
		policyActionDb.setValue(exportedPolicyAction.getValue());
		policyActionDb.setActionExpression(exportedPolicyAction.getActionExpression());
		policyActionDb.setType(exportedPolicyAction.getType());
		policyActionDb.setUnifiedField(exportedPolicyAction.getUnifiedField());
		policyActionDb.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyActionDb.setLastUpdatedDate(new Date());
		String databaseQueryAlias = exportedPolicyAction.getDatabaseQueryAlias();
		if(databaseQueryAlias != null && databaseQueryMap.containsKey(databaseQueryAlias)) {
			DatabaseQuery databaseQuery = databaseQueryDao.findByPrimaryKey(DatabaseQuery.class, databaseQueryMap.get(databaseQueryAlias));
			if(databaseQuery != null) {
				policyActionDb.setDatabaseQuery(databaseQuery);
				policyActionDb.setDatabaseQueryAlias(databaseQuery.getAlias());
			} else {
				policyActionDb.setDatabaseQuery(null);
				policyActionDb.setDatabaseQueryAlias(null);
			}
		} else {
			policyActionDb.setDatabaseQuery(null);
			policyActionDb.setDatabaseQueryAlias(null);
		}
		policyActionDb.setPolicyRuleActionRel(exportedPolicyAction.getPolicyRuleActionRel());
		
		String actionExpressionForSync = exportedPolicyAction.getActionExpression();
		if(actionExpressionForSync!=null){				
			policyActionDb.setActionExpressionForSync(actionExpressionForSync.replaceAll("'", ""));//NOSONAR
		}
		policyActionDao.update(policyActionDb);
		
		updatePolicyActionInList(serverInstance.getPolicyActionList(), policyActionDb);
		updatePolicyActionMap(policyActionMap, oldPolicyActionName, policyActionDb.getId());
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyAction
	 * @param policyActionMap
	 * @param databaseQueryMap
	 * @see This method will import policy action for keepboth mode
	 */
	public void importPolicyActionKeepBothMode(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap) {
		//add new policy action with suffix _import
		String keepBothOldName = exportedPolicyAction.getAlias();
		exportedPolicyAction.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyAction.getName()));
		exportedPolicyAction.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyAction.getAlias()));
		savePolicyActionForImport(exportedPolicyAction, serverInstance, policyActionMap, databaseQueryMap, keepBothOldName);
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyAction
	 * @param policyActionMap
	 * @param databaseQueryMap
	 * @see This method will import policy action for overwrite mode
	 */
	public void importPolicyActionOverwriteMode(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap) {
		//add policy action (in previous method, flush done)
		PolicyAction policyActionDb = policyActionDao.getPolicyActionByAlias(exportedPolicyAction.getAlias(), serverInstance.getId());
		if(policyActionDb == null) {
			//if policy action is not there, add
			savePolicyActionForImport(exportedPolicyAction, serverInstance, policyActionMap, databaseQueryMap, null);
		} else {
			//if policy condition is already there, update
			updatePolicyActionForImport(policyActionDb, exportedPolicyAction, serverInstance, policyActionMap, databaseQueryMap);
		}
	}
	
	/**
	 * @param exportedPolicyAction
	 * @param serverInstance
	 * @param policyActionMap
	 * @param databaseQueryMap
	 * @param keepBothOldName
	 * @see This method will save policy action for import and update policy action map
	 */
	public void savePolicyActionForImport(PolicyAction exportedPolicyAction, ServerInstance serverInstance, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap, String keepBothOldName) {
		String oldPolicyActionName = exportedPolicyAction.getAlias();
		savePolicyAction(exportedPolicyAction, serverInstance, databaseQueryMap);
		logger.debug("After Policy Action Save , new Id for policy action name is : " + exportedPolicyAction.getName() + " id is ::  " + exportedPolicyAction.getId());
		if(keepBothOldName != null) {
			updatePolicyActionMap(policyActionMap, keepBothOldName, exportedPolicyAction.getId());
		} else {
			updatePolicyActionMap(policyActionMap, oldPolicyActionName, exportedPolicyAction.getId());
		}
		
	}
	
	/**
	 * @param policyAction
	 * @param serverInstance
	 * @param databaseQueryMap
	 * @see This method will save policy action
	 */
	public void savePolicyAction(PolicyAction policyAction, ServerInstance serverInstance, Map<String, Integer> databaseQueryMap) {
		policyAction.setId(0);
		policyAction.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyAction.setCreatedDate(new Date());
		policyAction.setServer(serverInstance);
		policyAction.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		String databaseQueryAlias = policyAction.getDatabaseQueryAlias();
		if(databaseQueryAlias != null && databaseQueryMap.containsKey(databaseQueryAlias)) {
			DatabaseQuery databaseQuery = databaseQueryDao.findByPrimaryKey(DatabaseQuery.class, databaseQueryMap.get(databaseQueryAlias));
			if(databaseQuery != null) {
				policyAction.setDatabaseQuery(databaseQuery);
				policyAction.setDatabaseQueryAlias(databaseQuery.getAlias());
				if("dynamic".equalsIgnoreCase(policyAction.getType())) {
					String action = policyAction.getAction();
					if(action != null) {
						String[] aliasArray = action.split("=");
						if(aliasArray != null && aliasArray.length > 0) {
							String newAction = aliasArray[0]+"="+databaseQuery.getAlias();
							policyAction.setAction(newAction);
						}
					}
				}
			}
		}
		String actionExpressionForSync = policyAction.getActionExpression();
		if(actionExpressionForSync!=null){				
			policyAction.setActionExpressionForSync(actionExpressionForSync.replaceAll("'", ""));//NOSONAR
		}
		policyActionDao.save(policyAction);
		updatePolicyActionInList(serverInstance.getPolicyActionList(), policyAction);
	}
	
	public void updatePolicyActionInList(List<PolicyAction> policyActionList, PolicyAction action) {
		if(policyActionList != null && action != null) {
			int length= policyActionList.size();
			boolean isActionAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				PolicyAction policyAction = policyActionList.get(i);
				if(policyAction != null && !policyAction.getStatus().equals(StateEnum.DELETED)
						&& policyAction.getAlias().equalsIgnoreCase(action.getAlias())) {
					isActionAvailable = true;
					policyActionList.set(i, action);
					break;
				}
			}
			if(!isActionAvailable) {
				policyActionList.add(action);
			}
		}
	}
	
	/**
	 * @param policyActionMap
	 * @param oldPolicyActionName
	 * @param policyActionId
	 * @see This method will update policy action map
	 */
	public void updatePolicyActionMap(Map<String, Integer> policyActionMap, String oldPolicyActionName, int policyActionId) {
		if (policyActionMap != null && oldPolicyActionName != null && !oldPolicyActionName.isEmpty()) {
			policyActionMap.put(oldPolicyActionName, policyActionId);
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedDatabaseQuery
	 * @param databaseQueryMap
	 * @see This method will import database query for add mode
	 */
	public void importDatabaseQueryAddMode(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap) {
		DatabaseQuery databaseQueryDb = databaseQueryDao.getDatabaseQueryByAlias(exportedDatabaseQuery.getAlias(), serverInstance.getId());
		if(databaseQueryDb == null) {
			//add database query
			saveDatabaseQueryForImport(serverInstance, exportedDatabaseQuery, databaseQueryMap, null);
		} else {
			//update map
			updateDatabaseQueryMap(databaseQueryMap, databaseQueryDb.getAlias(), databaseQueryDb.getId());
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedDatabaseQuery
	 * @param databaseQueryMap
	 * @see This method will import database query for update mode
	 */
	public void importDatabaseQueryUpdateMode(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap) {
		DatabaseQuery databaseQueryDb = databaseQueryDao.getDatabaseQueryByAlias(exportedDatabaseQuery.getAlias(), serverInstance.getId());
		if(databaseQueryDb != null) {
			//update database query
			updateDatabaseQueryForImport(databaseQueryDb, exportedDatabaseQuery, serverInstance, databaseQueryMap);
		} else {
			//add database query
			saveDatabaseQueryForImport(serverInstance, exportedDatabaseQuery, databaseQueryMap, null);
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedDatabaseQuery
	 * @param databaseQueryMap
	 * @see This method will import database query for keepboth mode
	 */
	public void importDatabaseQueryKeepBothMode(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap) {
		//add new database query with suffix _import 
		String keepBothOldName = exportedDatabaseQuery.getAlias();
		exportedDatabaseQuery.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedDatabaseQuery.getAlias()));
		exportedDatabaseQuery.setQueryName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedDatabaseQuery.getQueryName()));
		saveDatabaseQueryForImport(serverInstance, exportedDatabaseQuery, databaseQueryMap, keepBothOldName);
	}
	
	/**
	 * @param serverInstance
	 * @param exportedDatabaseQuery
	 * @param databaseQueryMap
	 * @see This method will import database query for overwrite mode
	 */
	public void importDatabaseQueryOverwriteMode(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap) {
		//add database query (in previous method, flush done)
		DatabaseQuery databaseQueryDb = databaseQueryDao.getDatabaseQueryByAlias(exportedDatabaseQuery.getAlias(), serverInstance.getId());
		if(databaseQueryDb == null) {
			//if database query is not there, add
			saveDatabaseQueryForImport(serverInstance, exportedDatabaseQuery, databaseQueryMap, null);
		} else {
			//if database query is already there, update
			updateDatabaseQueryForImport(databaseQueryDb, exportedDatabaseQuery, serverInstance, databaseQueryMap);
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedDatabaseQuery
	 * @param databaseQueryMap
	 * @param keepBothOldName
	 * @see This method will save database query for import and update database query map
	 */
	public void saveDatabaseQueryForImport(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap, String keepBothOldName) {
		String oldDatabaseQueryName = exportedDatabaseQuery.getAlias();
		saveDatabaseQuery(exportedDatabaseQuery, serverInstance, databaseQueryMap);
		logger.debug("After Database Query Save , new Id for database query is : " + exportedDatabaseQuery.getAlias() + " id is ::  " + exportedDatabaseQuery.getId());
		if(keepBothOldName != null) {
			updateDatabaseQueryMap(databaseQueryMap, keepBothOldName, exportedDatabaseQuery.getId());
		} else {
			updateDatabaseQueryMap(databaseQueryMap, oldDatabaseQueryName, exportedDatabaseQuery.getId());
		}
	}
	
	/**
	 * @param databaseQueryMap
	 * @param oldDatabaseQueryName
	 * @param databaseQueryId
	 * @see This method will update database query map
	 */
	public void updateDatabaseQueryMap(Map<String, Integer> databaseQueryMap, String oldDatabaseQueryName, int databaseQueryId) {
		if (databaseQueryMap != null && oldDatabaseQueryName != null && !oldDatabaseQueryName.isEmpty()) {
			databaseQueryMap.put(oldDatabaseQueryName, databaseQueryId);
		}
	}
	
	/**
	 * @param databaseQuery
	 * @param serverInstance
	 * @param databaseQueryMap
	 * @see This method will save database query
	 */
	public void saveDatabaseQuery(DatabaseQuery databaseQuery, ServerInstance serverInstance, Map<String, Integer> databaseQueryMap) {
		Date date = new Date();
		databaseQuery.setId(0);
		databaseQuery.setQueryName(databaseQuery.getQueryName());
		databaseQuery.setAlias(databaseQuery.getAlias());
		iterateDatabaseQueryActions(databaseQuery.getDatabaseQueryActions(), serverInstance);
		iterateDatabaseQueryConditions(databaseQuery.getDatabaseQueryConditions(), serverInstance);
		databaseQuery.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		databaseQuery.setCreatedDate(date);
		databaseQuery.setServerInstance(serverInstance);
		databaseQuery.setLastUpdatedDate(date);
		databaseQuery.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		databaseQueryDao.save(databaseQuery);
		updateDatabaseQueryInList(serverInstance.getDatabaseQueryList(), databaseQuery);
	}
	
	public void updateDatabaseQueryInList(List<DatabaseQuery> databaseQueryList, DatabaseQuery query) {
		if(databaseQueryList != null && query != null) {
			int length = databaseQueryList.size();
			boolean isDatabaseQueryAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				DatabaseQuery databaseQuery = databaseQueryList.get(i);
				if(databaseQuery != null && !databaseQuery.getStatus().equals(StateEnum.DELETED)
						&& databaseQuery.getAlias().equalsIgnoreCase(query.getAlias())) {
					databaseQueryList.set(i, query);
					isDatabaseQueryAvailable = true;
					break;
				}
			}
			if(!isDatabaseQueryAvailable) {
				databaseQueryList.add(query);
			}
		}
	}
	
	public void iterateDatabaseQueryActions(List<DatabaseQueryAction> databaseQueryActions, ServerInstance serverInstance) {
		for(DatabaseQueryAction action : databaseQueryActions){
			action.setId(0);
			action.setCreatedDate(new Date());
			action.setLastUpdatedDate(new Date());
			action.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			action.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		}
	}
	
	public void iterateDatabaseQueryConditions(List<DatabaseQueryCondition> databaseQueryConditions, ServerInstance serverInstance) {
		for(DatabaseQueryCondition databaseQueryCondition : databaseQueryConditions){
			databaseQueryCondition.setId(0);
			databaseQueryCondition.setCreatedDate(new Date());
			databaseQueryCondition.setLastUpdatedDate(new Date());
			databaseQueryCondition.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			databaseQueryCondition.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		}
	}
	
	/**
	 * @param databaseQuery
	 * @param exportedDatabaseQuery
	 * @param serverInstance
	 * @param databaseQueryMap
	 * @see This method will update database query for import and update database query map
	 */
	public void updateDatabaseQueryForImport(DatabaseQuery databaseQuery, DatabaseQuery exportedDatabaseQuery, ServerInstance serverInstance, Map<String, Integer> databaseQueryMap) {
		List<DatabaseQueryAction> databaseQueryActionList = databaseQuery.getDatabaseQueryActions();
		Iterator<DatabaseQueryAction> databaseQueryActionIter = databaseQueryActionList.iterator();
		while(databaseQueryActionIter.hasNext()) {
			databaseQueryActionIter.next();
			databaseQueryActionIter.remove();
		}
		
		List<DatabaseQueryCondition> databaseQueryConditionList = databaseQuery.getDatabaseQueryConditions();
		Iterator<DatabaseQueryCondition> databaseQueryConditionIter = databaseQueryConditionList.iterator();
		while(databaseQueryConditionIter.hasNext()) {
			databaseQueryConditionIter.next();
			databaseQueryConditionIter.remove();
		}
		
		List<DatabaseQueryAction> exportedActions = exportedDatabaseQuery.getDatabaseQueryActions();
		if(exportedActions != null && !exportedActions.isEmpty()) {
			int length = exportedActions.size();
			for(int i = length-1; i >= 0; i--) {
				databaseQueryActionList.add(exportedActions.get(i));
			}
		}
		
		List<DatabaseQueryCondition> exportedConditions = exportedDatabaseQuery.getDatabaseQueryConditions();
		if(exportedConditions != null && !exportedConditions.isEmpty()) {
			int length = exportedConditions.size();
			for(int i = length-1; i >= 0; i--) {
				databaseQueryConditionList.add(exportedConditions.get(i));
			}
		}
		
		iterateDatabaseQueryActions(databaseQueryActionList, serverInstance);
		iterateDatabaseQueryConditions(databaseQueryConditionList, serverInstance);
		databaseQuery.setLastUpdatedDate(new Date());
		databaseQuery.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		databaseQueryDao.merge(databaseQuery);
		updateDatabaseQueryInList(serverInstance.getDatabaseQueryList(), databaseQuery);
		updateDatabaseQueryMap(databaseQueryMap, exportedDatabaseQuery.getAlias(), databaseQuery.getId());
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyRule
	 * @param policyRuleMap
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @see This method will import policy rule for add mode
	 */
	public void importPolicyRuleAddMode(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap) {
		PolicyRule policyRuleDb = policyRuleDao.getPolicyRuleByAlias(exportedPolicyRule.getAlias(), serverInstance.getId());
		if(policyRuleDb == null) {
			//add policy rule
			savePolicyRuleForImport(exportedPolicyRule, serverInstance, policyRuleMap, policyConditionMap, policyActionMap, null);
		} else {
			//add created policy action and policy condition in existing policy rule
			List<String> policyConditions = exportedPolicyRule.getPolicyConditions();
			List<PolicyRuleConditionRel> policyRuleConditionRelList = policyRuleDb.getPolicyRuleConditionRel(); // policyRuleDb.getPolicyConditionSet();
			int applicationOrderCondition = 1;
			for(PolicyRuleConditionRel policyRuleConditionRel : policyRuleConditionRelList){
				int tempApplicationOrderCondition = policyRuleConditionRel.getApplicationOrder();
				if(tempApplicationOrderCondition >= applicationOrderCondition){
					applicationOrderCondition = ++tempApplicationOrderCondition;
				}
			}
			for (String policyCondition : policyConditions) {
				if (policyConditionMap.containsKey(policyCondition)) {
					PolicyCondition policyConditionObj = policyConditionDao.findByPrimaryKey(PolicyCondition.class, policyConditionMap.get(policyCondition));
					if(!isPolicyConditionAlreadyAssociatedWithRelList(policyRuleConditionRelList, policyConditionObj)) {
						PolicyRuleConditionRel policyRuleConditionRel = new PolicyRuleConditionRel();
						policyRuleConditionRel.setId(0);
						policyRuleConditionRel.setPolicyRuleCon(policyRuleDb);
						policyRuleConditionRel.setCondition(policyConditionObj);
						policyRuleConditionRel.setApplicationOrder(applicationOrderCondition);
						policyRuleConditionRelList.add(policyRuleConditionRel);
					}
				}
			}
			policyRuleDb.setPolicyRuleConditionRel(policyRuleConditionRelList);
			
			List<String> policyActions = exportedPolicyRule.getPolicyActions();
			List<PolicyRuleActionRel> policyRuleActionRelList = policyRuleDb.getPolicyRuleActionRel(); //policyRuleDb.getPolicyActionSet();
			int applicationOrderAction = 1;
			for(PolicyRuleActionRel policyRuleActionRel : policyRuleActionRelList){
				int tempApplicationOrderAction = policyRuleActionRel.getApplicationOrder();
				if(tempApplicationOrderAction >= applicationOrderAction){
					applicationOrderAction = ++tempApplicationOrderAction;
				}
			}
			for (String policyAction : policyActions) {
				if (policyActionMap.containsKey(policyAction)) {
					PolicyAction policyActionObj = policyActionDao.findByPrimaryKey(PolicyAction.class, policyActionMap.get(policyAction));
					if(!isPolicyActionAlreadyAssociatedWithRelList(policyRuleActionRelList, policyActionObj)) {
						PolicyRuleActionRel policyRuleActionRel = new PolicyRuleActionRel();
						policyRuleActionRel.setId(0);
						policyRuleActionRel.setPolicyRuleAction(policyRuleDb);
						policyRuleActionRel.setAction(policyActionObj);
						policyRuleActionRel.setApplicationOrder(applicationOrderAction);
						policyRuleActionRelList.add(policyRuleActionRel);
					}
				}
			}
			policyRuleDb.setPolicyRuleActionRel(policyRuleActionRelList);
			
			policyRuleDb.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			policyRuleDb.setLastUpdatedDate(new Date());
			policyRuleDao.update(policyRuleDb);
			updatePolicyRuleInList(serverInstance.getPolicyRuleList(), policyRuleDb);
			//update policy rule map
			updatePolicyRuleMap(policyRuleMap, policyRuleDb.getAlias(), policyRuleDb.getId());
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyRule
	 * @param policyRuleMap
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @see This method will import policy rule for update mode
	 */
	public void importPolicyRuleUpdateMode(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap) {
		PolicyRule policyRuleDb = policyRuleDao.getPolicyRuleByAlias(exportedPolicyRule.getAlias(), serverInstance.getId());
		if(policyRuleDb != null) {
			//update policy rule
			updatePolicyRuleForImport(policyRuleDb, exportedPolicyRule, serverInstance, policyRuleMap, policyConditionMap, policyActionMap, BaseConstants.IMPORT_MODE_UPDATE);
		} else {
			//add policy rule
			savePolicyRuleForImport(exportedPolicyRule, serverInstance, policyRuleMap, policyConditionMap, policyActionMap, null);
		}
	}
	
	/**
	 * @param policyRuleDb
	 * @param exportedPolicyRule
	 * @param serverInstance
	 * @param policyRuleMap
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @see This method will update policy rule for import and update policy rule map
	 */
	public void updatePolicyRuleForImport(PolicyRule policyRuleDb, PolicyRule exportedPolicyRule, ServerInstance serverInstance, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap, int importMode) {
		String oldPolicyRuleName = exportedPolicyRule.getAlias();
		policyRuleDb.setGlobalSequenceRuleId(exportedPolicyRule.getGlobalSequenceRuleId());
		policyRuleDb.setCategory(exportedPolicyRule.getCategory());
		policyRuleDb.setSeverity(exportedPolicyRule.getSeverity());
		policyRuleDb.setErrorCode(exportedPolicyRule.getErrorCode());
		policyRuleDb.setOperator(exportedPolicyRule.getOperator());
		policyRuleDb.setAssociationStatus(exportedPolicyRule.getAssociationStatus());
		policyRuleDb.setAlert(exportedPolicyRule.getAlert());
		
		List<PolicyRuleConditionRel> policyRuleConditionRelList = policyRuleDb.getPolicyRuleConditionRel(); // policyRuleDb.getPolicyConditionSet();
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			policyRuleConditionRelList.clear();
		}
		List<String> policyConditions = exportedPolicyRule.getPolicyConditions();
		int applicationOrderCondition = 1;
		for(PolicyRuleConditionRel policyRuleConditionRel : policyRuleConditionRelList){
			int tempApplicationOrderCondition = policyRuleConditionRel.getApplicationOrder();
			if(tempApplicationOrderCondition >= applicationOrderCondition){
				applicationOrderCondition = ++tempApplicationOrderCondition;
			}
		}
		for (String policyCondition : policyConditions) {
			if (policyConditionMap.containsKey(policyCondition)) {
				PolicyCondition policyConditionObj = policyConditionDao.findByPrimaryKey(PolicyCondition.class, policyConditionMap.get(policyCondition));
				if(!isPolicyConditionAlreadyAssociatedWithRelList(policyRuleConditionRelList, policyConditionObj)) {
					PolicyRuleConditionRel policyRuleConditionRel = new PolicyRuleConditionRel();
					policyRuleConditionRel.setId(0);
					policyRuleConditionRel.setPolicyRuleCon(policyRuleDb);
					policyRuleConditionRel.setCondition(policyConditionObj);
					policyRuleConditionRel.setApplicationOrder(applicationOrderCondition);
					policyRuleConditionRelList.add(policyRuleConditionRel);
				}
			}
		}
		policyRuleDb.setPolicyRuleConditionRel(policyRuleConditionRelList);
		
		List<PolicyRuleActionRel> policyRuleActionRelList = policyRuleDb.getPolicyRuleActionRel();// policyRuleDb.getPolicyActionSet();
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			policyRuleActionRelList.clear();
		}
		List<String> policyActions = exportedPolicyRule.getPolicyActions();
		int applicationOrderAction = 1;
		for(PolicyRuleActionRel policyRuleActionRel : policyRuleActionRelList){
			int tempApplicationOrderAction = policyRuleActionRel.getApplicationOrder();
			if(tempApplicationOrderAction >= applicationOrderAction){
				applicationOrderAction = ++tempApplicationOrderAction;
			}
		}
		for (String policyAction : policyActions) {
			if (policyActionMap.containsKey(policyAction)) {
				PolicyAction policyActionObj = policyActionDao.findByPrimaryKey(PolicyAction.class, policyActionMap.get(policyAction));
				if(!isPolicyActionAlreadyAssociatedWithRelList(policyRuleActionRelList, policyActionObj)) {
					PolicyRuleActionRel policyRuleActionRel = new PolicyRuleActionRel();
					policyRuleActionRel.setId(0);
					policyRuleActionRel.setPolicyRuleAction(policyRuleDb);
					policyRuleActionRel.setAction(policyActionObj);
					policyRuleActionRel.setApplicationOrder(applicationOrderAction);
					policyRuleActionRelList.add(policyRuleActionRel);
				}
			}
		}
		policyRuleDb.setPolicyRuleActionRel(policyRuleActionRelList);
		
		policyRuleDb.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyRuleDb.setLastUpdatedDate(new Date());
		policyRuleDao.update(policyRuleDb);
		updatePolicyRuleInList(serverInstance.getPolicyRuleList(), policyRuleDb);
		updatePolicyRuleMap(policyRuleMap, oldPolicyRuleName, policyRuleDb.getId());
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyRule
	 * @param policyRuleMap
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @see This method will import policy rule for keepboth mode
	 */
	public void importPolicyRuleKeepBothMode(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap) {
		//add new policy rule with suffix _import
		String keepBothOldName = exportedPolicyRule.getAlias();
		exportedPolicyRule.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyRule.getName()));
		exportedPolicyRule.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyRule.getAlias()));
		savePolicyRuleForImport(exportedPolicyRule, serverInstance, policyRuleMap, policyConditionMap, policyActionMap, keepBothOldName);
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyRule
	 * @param policyRuleMap
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @see This method will import policy rule for overwrite mode
	 */
	public void importPolicyRuleOverwriteMode(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap) {
		//add policy rule (in previous method, flush done)
		PolicyRule policyRuleDb = policyRuleDao.getPolicyRuleByAlias(exportedPolicyRule.getAlias(), serverInstance.getId());
		if(policyRuleDb == null) {
			//if policy rule is not there, add
			savePolicyRuleForImport(exportedPolicyRule, serverInstance, policyRuleMap, policyConditionMap, policyActionMap, null);
		} else {
			//if policy rule is already there, update
			updatePolicyRuleForImport(policyRuleDb, exportedPolicyRule, serverInstance, policyRuleMap, policyConditionMap, policyActionMap, BaseConstants.IMPORT_MODE_OVERWRITE);
		}
	}
	
	/**
	 * @param exportedPolicyRule
	 * @param serverInstance
	 * @param policyRuleMap
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @param keepBothOldName
	 * @see This method will save policy rule for import and update policy rule map
	 */
	public void savePolicyRuleForImport(PolicyRule exportedPolicyRule, ServerInstance serverInstance, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap, String keepBothOldName) {
		String oldPolicyRuleName = exportedPolicyRule.getAlias();
		savePolicyRule(exportedPolicyRule, serverInstance, policyConditionMap, policyActionMap);
		logger.debug("After Policy Save , new Id for policy name is : " + exportedPolicyRule.getName() + " id is ::  " + exportedPolicyRule.getId());
		if(keepBothOldName != null) {
			updatePolicyRuleMap(policyRuleMap, keepBothOldName, exportedPolicyRule.getId());
		} else {
			updatePolicyRuleMap(policyRuleMap, oldPolicyRuleName, exportedPolicyRule.getId());
		}
	}
	
	/**
	 * @param policyRule
	 * @param serverInstance
	 * @param policyConditionMap
	 * @param policyActionMap
	 * @see This method will save policy rule
	 */
	public void savePolicyRule(PolicyRule policyRule, ServerInstance serverInstance, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap) {
		policyRule.setId(0);
		policyRule.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyRule.setCreatedDate(new Date());
		policyRule.setServer(serverInstance);
		policyRule.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		
		List<String> policyConditions = policyRule.getPolicyConditions();
		List<PolicyRuleConditionRel> policyRuleConditionRelList = new ArrayList<>();
		int applicationOrderCondition = 1;
		for (String policyCondition : policyConditions) {
			if (policyConditionMap.containsKey(policyCondition)) {
				PolicyCondition tempPolicyCondition = policyConditionDao.findByPrimaryKey(PolicyCondition.class, policyConditionMap.get(policyCondition));
				PolicyRuleConditionRel tempPolicyRuleConditionRel = new PolicyRuleConditionRel();
				tempPolicyRuleConditionRel.setId(0);
				tempPolicyRuleConditionRel.setPolicyRuleCon(policyRule);
				tempPolicyRuleConditionRel.setCondition(tempPolicyCondition);
				tempPolicyRuleConditionRel.setApplicationOrder(applicationOrderCondition++);
				policyRuleConditionRelList.add(tempPolicyRuleConditionRel);
			}
		}
		policyRule.setPolicyRuleConditionRel(policyRuleConditionRelList);
		
		List<String> policyActions = policyRule.getPolicyActions();
		List<PolicyRuleActionRel> policyRuleActionRelList = new ArrayList<>();
		int applicationOrderAction = 1;
		for (String policyAction : policyActions) {
			if (policyActionMap.containsKey(policyAction)) {
				PolicyAction tempPolicyAction = policyActionDao.findByPrimaryKey(PolicyAction.class, policyActionMap.get(policyAction));
				PolicyRuleActionRel tempPolicyRuleActionRel = new PolicyRuleActionRel();
				tempPolicyRuleActionRel.setId(0);
				tempPolicyRuleActionRel.setPolicyRuleAction(policyRule);
				tempPolicyRuleActionRel.setAction(tempPolicyAction);
				tempPolicyRuleActionRel.setApplicationOrder(applicationOrderAction++);
				policyRuleActionRelList.add(tempPolicyRuleActionRel);
			}
		}
		policyRule.setPolicyRuleActionRel(policyRuleActionRelList);
		
		policyRuleDao.save(policyRule);
		updatePolicyRuleInList(serverInstance.getPolicyRuleList(), policyRule);
	}
	
	public void updatePolicyRuleInList(List<PolicyRule> policyRuleList, PolicyRule rule) {
		if(policyRuleList != null && rule != null) {
			int length= policyRuleList.size();
			boolean isRuleAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				PolicyRule policyRule = policyRuleList.get(i);
				if(policyRule != null && !policyRule.getStatus().equals(StateEnum.DELETED)
						&& policyRule.getAlias().equalsIgnoreCase(rule.getAlias())) {
					isRuleAvailable = true;
					policyRuleList.set(i, rule);
					break;
				}
			}
			if(!isRuleAvailable) {
				policyRuleList.add(rule);
			}
		}
	}
	
	/**
	 * @param policyRuleMap
	 * @param oldPolicyRuleName
	 * @param policyRuleId
	 * @see This method will update policy rule map
	 */
	public void updatePolicyRuleMap(Map<String, Integer> policyRuleMap, String oldPolicyRuleName, int policyRuleId) {
		if (policyRuleMap != null && oldPolicyRuleName != null && !oldPolicyRuleName.isEmpty()) {
			policyRuleMap.put(oldPolicyRuleName, policyRuleId);
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyGroup
	 * @param policyGroupMap
	 * @param policyRuleMap
	 * @see This method will import policy group for add mode
	 */
	public void importPolicyGroupAddMode(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap) {
		PolicyGroup policyGroupDb = policyGroupDao.getPolicyGroupByAlias(exportedPolicyGroup.getAlias(), serverInstance.getId());
		if(policyGroupDb == null) {
			//add policy group
			savePolicyGroupForImport(exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, null);
		} else {
			//add created policy rule in policy group. following method will add policy rule in existing group
			//also update policy group map
			updatePolicyGroupForImport(policyGroupDb, exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, BaseConstants.IMPORT_MODE_ADD);
		}
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyGroup
	 * @param policyGroupMap
	 * @param policyRuleMap
	 * @see This method will import policy group for update mode
	 */
	public void importPolicyGroupUpdateMode(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap) {
		PolicyGroup policyGroupDb = policyGroupDao.getPolicyGroupByAlias(exportedPolicyGroup.getAlias(), serverInstance.getId());
		if(policyGroupDb != null) {
			//update policy group
			updatePolicyGroupForImport(policyGroupDb, exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, BaseConstants.IMPORT_MODE_UPDATE);
		} else {
			//add policy group
			savePolicyGroupForImport(exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, null);
		}
	}
	
	/**
	 * @param policyGroupDb
	 * @param exportedPolicyGroup
	 * @param serverInstance
	 * @param policyGroupMap
	 * @param policyRuleMap
	 * @see This method will update policy group for import and update policy group map
	 */
	public void updatePolicyGroupForImport(PolicyGroup policyGroupDb, PolicyGroup exportedPolicyGroup, ServerInstance serverInstance, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap, int importMode) {
		String oldPolicyGroupName = exportedPolicyGroup.getAlias();
		List<String> policyRules = exportedPolicyGroup.getPolicyRules();
		List<PolicyGroupRuleRel> policyGroupRuleRelSet = policyGroupDb.getPolicyGroupRuleRelSet();
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			policyGroupRuleRelSet.clear();
		} 
		
		int maxApplicationOrder = policyGroupRuleRelDao.getMaxApplicationOrder();
		Date date = new Date();
		for (String policyRule : policyRules) {
			if (policyRuleMap.containsKey(policyRule)) {
				PolicyRule tempPolicyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, policyRuleMap.get(policyRule));
				if(!isPolicyRuleAlreadyAssociatedWithPolicyGroup(policyGroupRuleRelSet, tempPolicyRule)) {
					PolicyGroupRuleRel tempPolicyGroupRuleRel = new PolicyGroupRuleRel();
					tempPolicyGroupRuleRel.setId(0);
					tempPolicyGroupRuleRel.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					tempPolicyGroupRuleRel.setCreatedDate(date);
					tempPolicyGroupRuleRel.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					tempPolicyGroupRuleRel.setLastUpdatedDate(date);
					tempPolicyGroupRuleRel.setApplicationOrder(++maxApplicationOrder);
					tempPolicyGroupRuleRel.setPolicyRule(tempPolicyRule);
					tempPolicyGroupRuleRel.setGroup(policyGroupDb);
					policyGroupRuleRelSet.add(tempPolicyGroupRuleRel);
				}
			}
		}
		policyGroupDb.setPolicyGroupRuleRelSet(policyGroupRuleRelSet);
		policyGroupDb.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyGroupDb.setCreatedDate(date);
		policyGroupDao.update(policyGroupDb);
		updatePolicyGroupInList(serverInstance.getPolicyGroupList(), policyGroupDb);
		logger.debug("After Policy Save , new Id for policy name is : " + policyGroupDb.getName() + " id is ::  " + policyGroupDb.getId());
		updatePolicyGroupMap(policyGroupMap, oldPolicyGroupName, policyGroupDb.getId());
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyGroup
	 * @param policyGroupMap
	 * @param policyRuleMap
	 * @see This method will import policy group for keepboth mode
	 */
	public void importPolicyGroupKeepBothMode(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap) {
		//add new policy group with suffix _import 
		String keepBothOldName = exportedPolicyGroup.getAlias();
		exportedPolicyGroup.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyGroup.getName()));
		exportedPolicyGroup.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicyGroup.getAlias()));
		savePolicyGroupForImport(exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, keepBothOldName);
	}
	
	/**
	 * @param serverInstance
	 * @param exportedPolicyGroup
	 * @param policyGroupMap
	 * @param policyRuleMap
	 * @see This method will import policy group for overwrite mode
	 */
	public void importPolicyGroupOverwriteMode(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap) {
		//add policy rule (in previous method, flush done)
		PolicyGroup policyGroupDb = policyGroupDao.getPolicyGroupByAlias(exportedPolicyGroup.getAlias(), serverInstance.getId());
		if(policyGroupDb == null) {
			//if policy group is not there, add
			savePolicyGroupForImport(exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, null);
		} else {
			//if policy group is already there, update
			updatePolicyGroupForImport(policyGroupDb, exportedPolicyGroup, serverInstance, policyGroupMap, policyRuleMap, BaseConstants.IMPORT_MODE_OVERWRITE);
		}
	}
	
	/**
	 * @param policyGroupMap
	 * @param oldPolicyGroupName
	 * @param policyGroupId
	 * @see This method will update policy group map
	 */
	public void updatePolicyGroupMap(Map<String, Integer> policyGroupMap, String oldPolicyGroupName, int policyGroupId) {
		if (policyGroupMap != null && oldPolicyGroupName != null && !oldPolicyGroupName.isEmpty()) {
			policyGroupMap.put(oldPolicyGroupName, policyGroupId);
		}
	}
	
	/**
	 * @param policyGroup
	 * @param serverInstance
	 * @param policyRuleMap
	 * @see This method will save policy group
	 */
	public void savePolicyGroup(PolicyGroup policyGroup, ServerInstance serverInstance, Map<String, Integer> policyRuleMap) {
		Date date = new Date();
		policyGroup.setId(0);
		policyGroup.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policyGroup.setCreatedDate(date);
		policyGroup.setServer(serverInstance);
		policyGroup.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		
		List<String> policyRules = policyGroup.getPolicyRules();
		List<PolicyGroupRuleRel> policyGroupRuleRelSet = new ArrayList<>();
		int maxApplicationOrder = policyGroupRuleRelDao.getMaxApplicationOrder();
		for (String policyRule : policyRules) {
			if (policyRuleMap.containsKey(policyRule)) {
				PolicyRule tempPolicyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, policyRuleMap.get(policyRule));
				PolicyGroupRuleRel tempPolicyGroupRuleRel = new PolicyGroupRuleRel();
				tempPolicyGroupRuleRel.setId(0);
				tempPolicyGroupRuleRel.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
				tempPolicyGroupRuleRel.setCreatedDate(date);
				tempPolicyGroupRuleRel.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
				tempPolicyGroupRuleRel.setLastUpdatedDate(date);
				tempPolicyGroupRuleRel.setApplicationOrder(++maxApplicationOrder);
				tempPolicyGroupRuleRel.setPolicyRule(tempPolicyRule);
				tempPolicyGroupRuleRel.setGroup(policyGroup);
				policyGroupRuleRelSet.add(tempPolicyGroupRuleRel);
			}
		}
		policyGroup.setPolicyGroupRuleRelSet(policyGroupRuleRelSet);
		policyGroupDao.save(policyGroup);
		updatePolicyGroupInList(serverInstance.getPolicyGroupList(), policyGroup);
	}
	
	public void updatePolicyGroupInList(List<PolicyGroup> policyGroupList, PolicyGroup group) {
		if(policyGroupList != null && group != null) {
			int length= policyGroupList.size();
			boolean isGroupAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				PolicyGroup policyGroup = policyGroupList.get(i);
				if(policyGroup != null && !policyGroup.getStatus().equals(StateEnum.DELETED)
						&& policyGroup.getAlias().equalsIgnoreCase(group.getAlias())) {
					isGroupAvailable = true;
					policyGroupList.set(i, group);
					break;
				}
			}
			if(!isGroupAvailable) {
				policyGroupList.add(group);
			}
		}
	}
	
	/**
	 * @param exportedPolicyGroup
	 * @param serverInstance
	 * @param policyGroupMap
	 * @param policyRuleMap
	 * @param keepBothOldName
	 * @see This method will save policy group for import purpose and update policy group map
	 */
	public void savePolicyGroupForImport(PolicyGroup exportedPolicyGroup, ServerInstance serverInstance, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap, String keepBothOldName) {
		String oldPolicyGroupName = exportedPolicyGroup.getAlias();
		savePolicyGroup(exportedPolicyGroup, serverInstance, policyRuleMap);
		logger.debug("After Policy Save , new Id for policy name is : " + exportedPolicyGroup.getName() + " id is ::  " + exportedPolicyGroup.getId());
		if(keepBothOldName != null) {
			updatePolicyGroupMap(policyGroupMap, keepBothOldName, exportedPolicyGroup.getId());
		} else {
			updatePolicyGroupMap(policyGroupMap, oldPolicyGroupName, exportedPolicyGroup.getId());
		}
	}
	
	public void importPolicyForAddMode(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap) {
		Policy policyDb = policyDao.getPolicyByAlias(exportedPolicy.getAlias(), serverInstance.getId());
		if(policyDb == null) {
			savePolicyForImport(exportedPolicy, serverInstance, policyMap, policyGroupMap, null);
		} else {
			//add created policy group to this policy and update policy map
			importPolicy(serverInstance, exportedPolicy, policyDb.getId(), policyMap, policyGroupMap);//NOSONAR
		}
	}
	
	public void importPolicyForUpdateMode(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap) {
		Policy policyDb = policyDao.getPolicyByAlias(exportedPolicy.getAlias(), serverInstance.getId());
		if(policyDb != null) {
			updatePolicyForImport(policyDb, exportedPolicy, serverInstance, policyMap, policyGroupMap);
		} else {
			savePolicyForImport(exportedPolicy, serverInstance, policyMap, policyGroupMap, null);
		}
	}
	
	public void importPolicyForKeepBothMode(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap) {
		//add suffix import and save
		String keepBothOldName = exportedPolicy.getAlias();
		exportedPolicy.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicy.getName()));
		exportedPolicy.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedPolicy.getAlias()));
		savePolicyForImport(exportedPolicy, serverInstance, policyMap, policyGroupMap, keepBothOldName);
	}
	
	public void importPolicyForOverwriteMode(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap) {
		Policy policyDb = policyDao.getPolicyByAlias(exportedPolicy.getAlias(), serverInstance.getId());
		if(policyDb != null) {
			updatePolicyForImport(policyDb, exportedPolicy, serverInstance, policyMap, policyGroupMap);
		} else {
			savePolicyForImport(exportedPolicy, serverInstance, policyMap, policyGroupMap, null);
		}
	}
	
	public void savePolicy(Policy policy, ServerInstance serverInstance, Map<String, Integer> policyGroupMap) {
		policy.setId(0);
		policy.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		policy.setCreatedDate(EliteUtils.getDateForImport(false));
		policy.setServer(serverInstance);
		policy.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		
		List<String> policyGroups = policy.getPolicyGroups();
		List<PolicyGroupRel> policyGroupRelSet = new ArrayList<>();
		int applicationOrader = 1;
		for (String policyGroup : policyGroups) {
			if (policyGroupMap.containsKey(policyGroup)) {
				PolicyGroup tempPolicyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroupMap.get(policyGroup));
				PolicyGroupRel tempPolicyRuleRel = new PolicyGroupRel();
				tempPolicyRuleRel.setId(0);
				tempPolicyRuleRel.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
				tempPolicyRuleRel.setCreatedDate(EliteUtils.getDateForImport(false));
				tempPolicyRuleRel.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
				tempPolicyRuleRel.setLastUpdatedDate(EliteUtils.getDateForImport(false));
				tempPolicyRuleRel.setApplicationOrder(applicationOrader++);
				tempPolicyRuleRel.setGroup(tempPolicyGroup);
				tempPolicyRuleRel.setPolicy(policy);
				policyGroupRelSet.add(tempPolicyRuleRel);
			}
		}
		policy.setPolicyGroupRelSet(policyGroupRelSet);
		policyDao.save(policy);
		updatePolicyInList(serverInstance.getPolicyList(), policy);
	}
	
	public void updatePolicyInList(List<Policy> policyList, Policy policy) {
		if(policyList != null && policy != null) {
			int length= policyList.size();
			boolean isPolicyAvailable = false;
			for(int i = length-1; i >= 0; i--) {
				Policy myPolicy = policyList.get(i);
				if(myPolicy != null && !myPolicy.getStatus().equals(StateEnum.DELETED)
						&& myPolicy.getAlias().equalsIgnoreCase(policy.getAlias())) {
					isPolicyAvailable = true;
					policyList.set(i, policy);
					break;
				}
			}
			if(!isPolicyAvailable) {
				policyList.add(policy);
			}
		}
	}
	
	public void savePolicyForImport(Policy exportedPolicy, ServerInstance serverInstance, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap, String keepBothOldName) {
		String oldPolicyName = exportedPolicy.getAlias();
		savePolicy(exportedPolicy, serverInstance, policyGroupMap);
		logger.debug("After Policy  Save , new Id for policy name is : " + exportedPolicy.getName() + " id is ::  " + exportedPolicy.getId());
		if(keepBothOldName != null) {
			updatePolicyMap(policyMap, keepBothOldName, exportedPolicy.getId());
		} else {
			updatePolicyMap(policyMap, oldPolicyName, exportedPolicy.getId());
		}
	}
	
	public void updatePolicyForImport(Policy policyDb, Policy exportedPolicy, ServerInstance serverInstance, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap) {
		importPolicy(serverInstance, exportedPolicy, policyDb.getId(), policyMap, policyGroupMap);//NOSONAR
	}
	
	/**
	 * @param policyGroupRuleRelSet
	 * @param policyRule
	 * @see This method returs true if given policy rule is associated with policy group rule relation
	 * @return boolean
	 */
	public boolean isPolicyRuleAlreadyAssociatedWithPolicyGroup(List<PolicyGroupRuleRel> policyGroupRuleRelSet, PolicyRule policyRule) {
		if(policyGroupRuleRelSet != null && !policyGroupRuleRelSet.isEmpty()) {
			int policyGroupRuleRelLength = policyGroupRuleRelSet.size();
			for(int i = policyGroupRuleRelLength-1; i >= 0; i--) {
				PolicyGroupRuleRel policyGroupRuleRel = policyGroupRuleRelSet.get(i);
				if(policyGroupRuleRel != null && policyGroupRuleRel.getPolicyRule() != null 
						&& policyGroupRuleRel.getPolicyRule().getAlias().equalsIgnoreCase(policyRule.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param policyMap
	 * @param oldPolicyName
	 * @param policyId
	 * @see This method will update policy map
	 */
	public void updatePolicyMap(Map<String, Integer> policyMap, String oldPolicyName, int policyId) {
		if (policyMap != null && oldPolicyName != null && !oldPolicyName.isEmpty()) {
			policyMap.put(oldPolicyName, policyId);
		}
	}
	
	/**
	 * @param policyGroupRelSet
	 * @param policyGroup
	 * @see This method returns true if given policy is already associated with given policy group
	 * @return
	 */
	public boolean isPolicyGroupAlreadyAssociatedWithPolicy(List<PolicyGroupRel> policyGroupRelSet, PolicyGroup policyGroup) {
		if(policyGroupRelSet != null && !policyGroupRelSet.isEmpty()) {
			int policyGroupRelLength = policyGroupRelSet.size();
			for(int i = policyGroupRelLength-1; i >= 0; i--) {
				PolicyGroupRel policyGroupRel = policyGroupRelSet.get(i);
				if(policyGroupRel != null && policyGroupRel.getGroup() != null
						&& policyGroupRel.getGroup().getAlias().equalsIgnoreCase(policyGroup.getAlias())) {
					return true;
				}
			}
		}
		return false;
	}
}