package com.elitecore.sm.diameterpeer.validator;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.services.service.ServicesService;

@Component
public class DiameterPeerValidator extends BaseValidator {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ServicesService servicesService;
	
	
	/**
	 * Validate Diameter Peer parameter
	 * 
	 * @param peer
	 * @param serviceType
	 * @param result
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDiameterPeer(DiameterPeer peer, String serviceType, BindingResult result, List<ImportValidationErrors> importErrorList,String entityName, boolean validateForImport) { //NOSONAR
		
		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = result;
		}
		
		String peerName = peer.getName();
	    String classFullName = peer.getClass().getName();
	    boolean minFileSeqValue=false;
	    boolean maxFileSeqValue=false;
	    
		isValidate(SystemParametersConstant.DIAMETERPEER_NAME, peer.getName(), "name", entityName, peerName, validateForImport);
		isValidate(SystemParametersConstant.DIAMETERPEER_IDENTITY, peer.getIdentity(), "identity", entityName, peerName, validateForImport);
		isValidate(SystemParametersConstant.DIAMETERPEER_REALMNAME, peer.getRealmName(), "realmName", entityName, peerName, validateForImport);
		isValidate(SystemParametersConstant.DIAMETERPEER_WATCHDOGINTERVAL, peer.getWatchDogInterval(), "watchDogInterval", peerName,entityName, validateForImport, classFullName);
		isValidate(SystemParametersConstant.DIAMETERPEER_REQUESTTIMEOUT, peer.getRequestTimeOut(), "requestTimeOut", peerName,entityName, validateForImport, classFullName);
		isValidate(SystemParametersConstant.DIAMETERPEER_FILENAMEFORMAT, peer.getFileNameFormat(), BaseConstants.FILE_NAME_FORMAT, entityName,peerName, validateForImport);
		isValidate(SystemParametersConstant.DIAMETERPEER_OUTFILELOCATION, String.valueOf(peer.getOutFileLocation()), "outFileLocation",	entityName, peerName, validateForImport);
		
		if (peer.isFileSeqEnable()){
		 minFileSeqValue = isValidate(SystemParametersConstant.DIAMETERPEER_MINFILESEQVALUE, peer.getMinFileSeq(),"minFileSeq",peerName, entityName, validateForImport, classFullName);
		 maxFileSeqValue = isValidate(SystemParametersConstant.DIAMETERPEER_MAXFILESEQVALUE, peer.getMaxFileSeq(),"maxFileSeq",peerName, entityName, validateForImport, classFullName);
		}
		
		isValidate(SystemParametersConstant.DIAMETERPEER_VOLLOGROLLINGUNIT, peer.getLogRollingUnitVol(), "logRollingUnitVol", peerName, entityName, validateForImport, classFullName);
		isValidate(SystemParametersConstant.DIAMETERPEER_TIMELOGROLLINGUNIT, peer.getLogRollingUnitTime(), "logRollingUnitTime", peerName, entityName, validateForImport, classFullName);

		if (peer.isFileSeqEnable() && minFileSeqValue && maxFileSeqValue && (peer.getMinFileSeq() > peer.getMaxFileSeq())) {
			setErrorFieldErrorMessage(String.valueOf(peer.getMaxFileSeq()), "maxFileSeq", peerName, entityName, validateForImport,"error.DiameterPeer.maxFileSeqValue.invalid", getMessage("DiameterPeer.maxFileSeqValue.invalid.min.max"));
		}

		if (!StringUtils.isEmpty(peer.getFileNameFormat())) {
			if (validateDateFormat(parseFileName(peer.getFileNameFormat()))) {
				if (peer.getFileNameFormat().indexOf(BaseConstants.FULL_STOP) == -1
						|| peer.getFileNameFormat().substring(peer.getFileNameFormat().indexOf(BaseConstants.FULL_STOP) + 1).length() == 0) {
					setErrorFieldErrorMessage(peer.getFileNameFormat(), BaseConstants.FILE_NAME_FORMAT, peerName, entityName, validateForImport,
							"error.DiameterPeer.fileNameFormat.invalid", getMessage("DiameterPeer.fileNameFormat.invalid.extension"));
				}
			} else {
				setErrorFieldErrorMessage(peer.getFileNameFormat(), BaseConstants.FILE_NAME_FORMAT, peerName, entityName, validateForImport,
						"error.DiameterPeer.fileNameFormat.invalid", getMessage("DiameterPeer.fileNameFormat.invalid.dateFormat"));
			}
		}
	}
	
	/**
	 * To get the date Format String from file name
	 * 
	 * @param fileName
	 * @return dateFormat String
	 */
	public String parseFileName(String fileName) {
		String timestampFormat = "";

		char[] fileNameArray = fileName.toCharArray();
		String fileTimestampFormat = null;
		try {
			for (int i = 0; i < fileNameArray.length; i++) {
				if (fileNameArray[i] == '\\') {
					if (fileNameArray.length > i + 1) {
						i++; //NOSONAR

					}
				} else if (fileNameArray[i] == '{') {
					boolean hasClosingBrace = false;
					i++; //NOSONAR
					for (; i < fileNameArray.length; i++) { //NOSONAR
						if (fileNameArray[i] != '}') {
							if (fileNameArray[i] != '\\') { //NOSONAR
								timestampFormat += Character.toString(fileNameArray[i]);
							}
						} else {
							hasClosingBrace = true;
							break;
						}
					}
					if (hasClosingBrace) {
						fileTimestampFormat = timestampFormat;
					}

				}
			}

		} catch (Exception e) {
			logger.debug("Inside catch..So date format is not valid.." + e);
		}

		return fileTimestampFormat;
	}
}
