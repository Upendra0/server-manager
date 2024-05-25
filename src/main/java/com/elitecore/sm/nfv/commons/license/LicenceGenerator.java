package com.elitecore.sm.nfv.commons.license;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.nfv.commons.license.GenerateKeysUtility;
import com.elitecore.sm.nfv.commons.license.LicenseBean;

/**
 * The Class LicenceGenerator.
 */
@Component
public class LicenceGenerator {
	
	/** The Constant serialVersionUID. */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
       
	/** The servlet context. */
	@Autowired
	ServletContext servletContext;  // to get class path location

	/**
	 * Creates the license.
	 *
	 * @param licenseDetailMap the license detail map
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public byte[] createLicense(Map<String,String> licenseDetailMap) throws IOException {
		byte[] dataBytes = null;
		GenerateKeysUtility keyUtility = new GenerateKeysUtility();
		LicenseBean lb = new LicenseBean();
		
		lb.setCustomer(licenseDetailMap.get(GenerateKeysUtility.CUSTOMER));
		lb.setLocation(licenseDetailMap.get(GenerateKeysUtility.LOCATION));
		lb.setMacid(licenseDetailMap.get(GenerateKeysUtility.MACID));
		lb.setProduct(licenseDetailMap.get(GenerateKeysUtility.PRODUCT));
		lb.setVersion(licenseDetailMap.get(GenerateKeysUtility.VERSION));
		lb.setStartDate(licenseDetailMap.get(GenerateKeysUtility.STARTDATE));
		lb.setEndDate(licenseDetailMap.get(GenerateKeysUtility.ENDDATE));
		lb.setHostname(licenseDetailMap.get(GenerateKeysUtility.HOSTNAME));
		lb.setTps(licenseDetailMap.get(GenerateKeysUtility.TPS));	   
		lb.setMonthlyRecords(licenseDetailMap.get(GenerateKeysUtility.MONTHLY_RECORDS));
		lb.setDailyRecords(licenseDetailMap.get(GenerateKeysUtility.DAILY_RECORDS));	 
		String encryptData = lb.toString();
		
		String REPOSITORY_PATH = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
		dataBytes = keyUtility.encryptLicenseDetail(encryptData,REPOSITORY_PATH);
		
		return dataBytes;
	}

}
