/**
 * 
 */
package com.elitecore.sm.license.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.model.LicenseTypeEnum;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 * 
 * @author Keyur Raval - toStore the trial license in encrypt form and
 *         authenticate once trial license is created second time can not create
 *         the trial license on same server ID
 *
 */
public class LicenseUtility {

	private static Logger logger = Logger.getLogger(LicenseUtility.class);

	public static final String SERVER_HOME = "CRESTEL_P_ENGINE_HOME";
	private static final String LICENSE_SPLIT_TOKEN = "=";
	private static final String ALOGRITHM = "RSA";
	private static final String KEYFILE = "des.key";
	private static final String ALGORITHMDESEDE = "DESede";
	public static final String REPOSITORY_PATH = System.getenv(SERVER_HOME) + File.separator + "license"
			+ File.separator;
	public static final String SYSTEM_PATH = System.getenv(SERVER_HOME) + File.separator + "system" + File.separator;
	public static final String LICENSE_STR = "License.key";
	public static final String PRIVATE_STR = "private.key";
	public static final String TRIAL_STR = "TrialVer.key";
	public static final String SYSTEM_STR = "system.cnf";
	public static final String DAILY_TRIAL = "PARSING_SERVICE:100000000";
	public static final File TRIAL_FILE = new File(REPOSITORY_PATH + TRIAL_STR);

	public static final File LICENSE_FILE = new File(REPOSITORY_PATH + LICENSE_STR);
	public static final File SYSTEM_LIC_FILE = new File(SYSTEM_PATH);
	public static final String LICENSE = "License";
	public static final String KEY = "key";
	public static final String MACID = "MacID";
	public static final String START_DATE = "Start Date";
	public static final String END_DATE = "End Date";
	public static final String TPS = "Tps";
	public static final String CUSTOMER_NAME = "Customer Name";
	public static final String LOCATION = "Location";
	public static final String IS_ABOVE_730 = "Is above 730";
	public static final String NO_OF_CONTAINER = "No of Container";
	public static final String DEFAULT_NO_OF_CONTAINER = "5";

	public static final String HOSTNAME = "HostName";
	public static final String IP = "ip";
	public static final String VERSION = "Version";
	public static final String FULL = "Full";
	public static final String TRIAL = "Trial";
	public static final String TEMP_FILE_LICENSE = "Product_Info.txt";
	public static final String TRIAL_SPLIT = "#";

	public static final String DAILY_RECORDS = "Daily Records";
	public static final String MONTHLY_RECORDS = "Monthly Records";
	public static final String TRIAL_TPS = "PARSING_SERVICE:10000,PROCESSING_SERVICE:10000,DISTRIBUTION_SERVICE:10000,IPLOG_PARSING_SERVICE:10000";
	public static final long DEFAULT_DAILY = 100000000;
	private static final String UPGRADE = "upgrade";
	public static final String PRODUCT = "Product";
	public static final String SERVERID = "serverId";
	private static final String SHEET = "LicenseForm";
	public static final String LICENSEFILE = "License.key";

	public static final String TOMCAT_HOME = System.getenv(BaseConstants.TOMCAT_HOME);
	public static final String LICENSE_TYPE = "License Type";

	@Autowired
	ServletContext servletContext;

	public LicenseUtility() {
		// default constructor.
	}

	/**
	 * @param ip - Ip of the system
	 * @return - Host name for the specified IP.
	 */
	public String getHostName(String ip) {
		String hostName = "";
		/* hostName = InetAddress.getByName(ip).getHostName(); */
		hostName = LicenseConstants.LICENSE_HOSTNAME_DEFAULT;
		return hostName;
	}

	/**
	 * @param RepositoryPath - Path where license files are stored.
	 * @param serverId       - serverId for license
	 * @param host           - host for license.
	 * @param srcFile        - Source file location
	 * @param destFile       - Destination file location
	 */
	@SuppressWarnings("deprecation")
	public void generateLicenseTemplate(String repositoryPath, String serverId, String host, String srcFile,
			String destFile) {
		logger.debug("Generating License Template on path  " + repositoryPath);
		try (FileOutputStream fileOut = new FileOutputStream(repositoryPath + destFile);
				FileInputStream fis = new FileInputStream(new File(repositoryPath + File.separator + srcFile));) {
			HSSFWorkbook wb = new HSSFWorkbook(fis);
			HSSFSheet st = wb.getSheet(SHEET);
			HSSFRow row = st.getRow(14);
			short cellno = 1;
			HSSFCell cell = row.getCell(cellno);
			cell.setCellValue(serverId);
			row = st.getRow(17);
			cell = row.getCell(cellno);
			cell.setCellValue(host);
			wb.write(fileOut);
		} catch (IOException e) {
			logger.error("Error occurred while generating template for license file" + e);
		}
	}

	/**
	 * Method will fetch Mac address using provided id.
	 * 
	 * @param Ip - Ip of the system.
	 * @return - Mac id for the specified IP.
	 */
	public String getMacAddressByIP(String iP) {
		InetAddress ip;
		StringBuilder sb = new StringBuilder();
		try {
			ip = InetAddress.getByName(iP);
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
			}
		} catch (UnknownHostException | SocketException e) {
			logger.error("Error occured while getting MAC Id, reason : " + e);
		}
		return sb.toString();
	}

	/**
	 * @param msg - Accepts the license data in form of String
	 * @return - License data in form of hash map.
	 */
	public Map<String, String> getData(String msg) {
		Map<String, String> hm = new HashMap<>();
		StringTokenizer token = new StringTokenizer(msg, "\n");
		while (token.hasMoreTokens()) {
			String[] temp = token.nextToken().split(LICENSE_SPLIT_TOKEN);
			if (temp != null && temp.length >= 2) {
				if ((temp[0].trim().equalsIgnoreCase(LicenseUtility.MONTHLY_RECORDS)
						|| temp[0].trim().equalsIgnoreCase(LicenseUtility.DAILY_RECORDS))
						&& ("0".equalsIgnoreCase(temp[1].trim())))
					temp[1] = "NA";
				hm.put(temp[0].trim(), temp[1].trim());
			}
		}
		return hm;
	}

	/**
	 * @param macid - macid for which license is generated.
	 * @return - boolean value for whether macid is valid or not.
	 */
	public boolean validateMacId(String macid) {
		logger.debug("Validating mac id.");
		boolean result = true;
		List<String> ipList = getIpList();
		for (String ip : ipList) {
			if (!macid.equalsIgnoreCase(getMacAddressByIP(ip))) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * @param serverId
	 * @return - Whether Server Id validation succeeded or not.
	 */
	public boolean validateServerId(String serverId) {
		boolean result = Boolean.TRUE;
		if (serverId.equalsIgnoreCase(UPGRADE)) {
			return result;
		}
		String macId = "";
		String host = "";
		List<String> ipList = getIpList();
		if (!ipList.isEmpty()) {
			String ip = ipList.get(0);
			macId = getMacAddressByIP(ip).toUpperCase();
			host = getHostName(ip);
		}
		String machineServerId = generateServerId(macId, host);
		if (!machineServerId.equalsIgnoreCase(serverId)) {
			result = Boolean.FALSE;
		}
		return result;
	}

	/**
	 * @param macId - Mac id of the server
	 * @param host  - Host name of the server
	 * @return - Server id as a combination of Mac id and Host name.
	 */
	public String generateServerId(String macId, String host) {
		String serverId = null;
		String[] macArray = macId.toUpperCase().split(":");
		int intlh1 = 10;
		int intlh2 = 10;
		int intlh3 = 10;
		if (macArray.length == 6) {
			String mid1 = new StringBuilder(macArray[0] + macArray[1]).reverse().toString();
			String mid2 = new StringBuilder(macArray[2] + macArray[3]).reverse().toString();
			String mid3 = new StringBuilder(macArray[4] + macArray[5]).reverse().toString();
			if (host != null) {
				String modifiedHost = host.replaceAll("\\.", "").trim();
				int hostLen = modifiedHost.length();
				int factor = hostLen / 3;
				String lh1 = modifiedHost.substring(0, factor);
				String lh2 = modifiedHost.substring(factor, 2 * factor);
				String lh3 = modifiedHost.substring(factor * 2);
				intlh1 = Math.abs(lh1.hashCode()) % 100; // NOSONAR
				intlh2 = Math.abs(lh2.hashCode()) % 100; // NOSONAR
				intlh3 = Math.abs(lh3.hashCode()) % 100; // NOSONAR
			}
			serverId = intlh1 + mid1 + "-" + intlh2 + mid2 + "-" + intlh3 + mid3;
		}
		return serverId;
	}

	/**
	 * MED-8160 : Licence activated for SM but after some time its again ask for
	 * active licence.
	 * 
	 * @return - Return the ip list of the system.
	 */
	public List<String> getIpList() {
		List<String> ipList = new ArrayList<>();
		DatagramSocket socket;
		try {
			socket = new DatagramSocket();// NOSONAR
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);// NOSONAR
			String ip = socket.getLocalAddress().getHostAddress();
			ipList.add(ip);
		} catch (SocketException e) {// NOSONAR
			logger.error("Error occured while getting IP List, reason : " + e);
		} catch (UnknownHostException e) {// NOSONAR
			logger.error("Error occured while getting IP List, reason : " + e);
		}
		return ipList;
	}

	/**
	 * @return - Return the ip list of the system.
	 */
	/*
	 * public List<String> getIpList() { List<String> ipList = new ArrayList<>();
	 * try { for (Enumeration<NetworkInterface> en =
	 * NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	 * NetworkInterface intf = en.nextElement(); for (Enumeration<InetAddress>
	 * enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	 * InetAddress inetAddress = enumIpAddr.nextElement(); if
	 * (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() &&
	 * inetAddress.isSiteLocalAddress()) { ipList.add(inetAddress.getHostAddress());
	 * } } } } catch (SocketException ex) {
	 * logger.error("Error occured while getting IP List, reason : " + ex); } return
	 * ipList; }
	 */
	/**
	 * Method will compare applied license product and file data.
	 * 
	 * @param moduleAlias
	 * @param productType
	 * @return
	 */
	public boolean validateProductType(String moduleAlias, String productType) {
		logger.debug("Validating selected products and decrpted product data.");
		boolean result = false;
		String[] moduleAliasArr = moduleAlias.split(",", -1);
		for (String module : moduleAliasArr) {
			if (productType.indexOf(module) != -1) {
				result = true;
			} else {
				result = false;
				break;
			}
		}
		if (result) {
			logger.debug("product types  validated successfully.");
		}
		return result;
	}

	/**
	 * Method will validate license date
	 * 
	 * @param startDate - Start Date.
	 * @param endDate   - End Date.
	 * @return - whether current date lies between start and end date.
	 */
	public boolean validateLicDate(String startDate, String endDate) {
		logger.debug("Validating license start and end date.");
		boolean result;
		Date stopDate = DateFormatter.formatDate(endDate, LicenseConstants.DATE_FORMAT);
		Date beginDate = DateFormatter.formatDate(startDate, LicenseConstants.DATE_FORMAT);
		Date date = new Date();
		if (date.after(stopDate) || date.before(beginDate)) {
			logger.debug("Failed to validate license date.");
			result = false;
		} else {
			logger.debug("License dates are validated successfully.");
			result = true;
		}
		return result;
	}

	/**
	 * This code check the trial license is encrypt or not and return to the the
	 * Service layer
	 * 
	 * @param str                     - String which has to be encrypted.
	 * @param file                    - Destination file.
	 * @param LICENSE_REPOSITORY_PATH - Path where destination file will be stored.
	 * @return -status for file creation.
	 */
	public boolean encryptSymMsg(String str, File file, String respositoryPath) {
		boolean result = false;
		Key symKey = null;
		Cipher c = null;
		byte[] encryptedMessage = null;
		File symfile = new File(respositoryPath + File.separator + KEYFILE);
		try (FileInputStream fis = new FileInputStream(symfile);
				ObjectInputStream inputStream = new ObjectInputStream(fis);
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);) {
			symKey = (Key) inputStream.readObject();
			c = Cipher.getInstance(ALGORITHMDESEDE);// NOSONAR
			c.init(Cipher.ENCRYPT_MODE, symKey);
			encryptedMessage = c.doFinal(str.getBytes());
			bos.write(encryptedMessage);
			result = true;
		} catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error("Error occurred while encrypting data " + e.getMessage());
			logger.trace(e);
			result = false;
		}
		return result;
	}

	/**
	 * @param file           - File to be decrypted
	 * @param repositoryPath - Path where license files are stored.
	 * @return - whether the decryption process was done or not.
	 * @throws IOException
	 */
	public String decrypt(String file, String repositoryPath) throws IOException {
		String actualData = repositoryPath + File.separator + TEMP_FILE_LICENSE;
		File tempFile = new File(actualData);
		String licenseKey = file;
		String publicKeyPath = repositoryPath + File.separator + "public.key";
		decryptFile(publicKeyPath, new FileInputStream(licenseKey), new FileOutputStream(actualData));
		byte[] b = readBytesFromFile(tempFile);
		if (tempFile.exists())
			tempFile.delete();
		return decryptSymMsg(b, repositoryPath);
	}

	/**
	 * @param strByte        - byte data which needs to be decrypted.
	 * @param repositoryPath - Path where license files are stored.
	 * @return - whether byte data was decrypted or not.
	 * @throws IOException
	 */
	public String decryptSymMsg(byte[] strByte, String repositoryPath) throws IOException {
		String str = null;
		Key symKey = null;
		Cipher c = null;
		File file = new File(repositoryPath + File.separator + KEYFILE);
		try (FileInputStream fis = new FileInputStream(file);
				ObjectInputStream inputStream = new ObjectInputStream(fis);) {
			symKey = (Key) inputStream.readObject();
			c = Cipher.getInstance(ALGORITHMDESEDE);// NOSONAR
			c.init(Cipher.DECRYPT_MODE, symKey);
			str = new String(c.doFinal(strByte));
		} catch (ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			logger.error("Error occured while ecrypt the data  " + e);
		}
		return str;
	}

	/**
	 * @param file - Represents the source file
	 * @return - Byte data from the file.
	 * @throws IOException
	 */
	public byte[] readBytesFromFile(File file) throws IOException {
		// Get the size of the file
		long length = file.length();
		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length
					+ " bytes, max supported " + Integer.MAX_VALUE + ")");
		}
		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead;
		try (InputStream is = new FileInputStream(file);) {
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
		} catch (IOException e) {
			logger.error("Exception occured during file reading  due to exception " + e);
		}
		// Ensure all the bytes have been read in
		return bytes;
	}

	/**
	 * @param publicKeyPath - Path where public key is stored.
	 * @param in            - Represent the source stream.
	 * @param out           - Represents the destination stream.
	 */
	public void decryptFile(String publicKeyPath, InputStream in, OutputStream out) {
		// Create a cipher object and use the generated key to initialize it
		byte[] b = new byte[128];
		PublicKey publicKey = null;
		Cipher cipher = null;
		CipherInputStream cis = null;
		try (FileInputStream fis = new FileInputStream(publicKeyPath);
				ObjectInputStream inputStream = new ObjectInputStream(fis);) {
			publicKey = (PublicKey) inputStream.readObject();
			cipher = Cipher.getInstance(ALOGRITHM); // NOSONAR
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			cis = new CipherInputStream(in, cipher);
			int i;
			while ((i = cis.read(b)) >= 0) {
				out.write(b, 0, i);
			}
		} catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException e) {
			logger.error("Error occured while decrypting file, reason : " + e);
		} finally {
			try {
				if (cis != null)
					cis.close();
			} catch (IOException e) {
				logger.error("Error occured while closing stream in decrypting file, reason : " + e);
			}
		}
	}

	/**
	 * @param fileName                - source file
	 * @param doc                     - Data obtained from uploaded file
	 * @param LICENSE_REPOSITORY_PATH - Path where all license files are stored.
	 * @return - status of file saved or not.
	 * @throws IOException
	 * @throws Exception
	 */
	public String saveFile(String fileName, byte[] doc, String repositoryPath) throws SMException {
		if (repositoryPath == null || doc == null || fileName == null) {
			logger.debug("Required Arguments found null");
			throw new SMException("Required Argument found null");
		}
		boolean isExist;
		File srcFile = new File(repositoryPath + File.separator + fileName);
		logger.info("File name : " + fileName);
		File srcDir = new File(repositoryPath);
		if (!srcDir.exists()) {
			srcDir.mkdir();
		}
		if (srcFile.exists()) {
			srcFile.delete();
		}
		try {
			isExist = srcFile.createNewFile();
		} catch (IOException exception) {
			logger.error("Error in File Operation" + exception);
			throw new SMException("Unable Perform File operation due to Access Rights or already exists read-only file."
					+ exception.getMessage());
		}
		if (isExist) {
			try (FileOutputStream fs = new FileOutputStream(srcFile);) {
				fs.write(doc);
			} catch (IOException exception) {
				logger.error("Error in File Operation" + exception);
				throw new SMException(
						"Unable Perform File operation due to Access Rights or already exists read-only file."
								+ exception.getMessage());
			}
		}
		return repositoryPath + File.separator + fileName;
	}

	/**
	 * Method will get server id using ip-address
	 * 
	 * @return
	 */
	public String getServerId() {
		logger.debug("Fetching server id using ip address.");
		String serverId = null;
		String ipAddress = getIpAddress();
		if (ipAddress != null) {
			serverId = generateServerId(getMacAddressByIP(ipAddress), getHostName(ipAddress));
			logger.info("Found " + serverId + " for IP Address " + ipAddress);
		} else {
			logger.info("Failed to get server id as IP address found null.");
		}
		return serverId;
	}

	/**
	 * Method will get IP address.
	 * 
	 * @return
	 */
	public String getIpAddress() {
		String ipAddress = null;
		List<String> ipList = getIpList();
		if (ipList != null && !ipList.isEmpty()) {
			ipAddress = ipList.get(0);
			logger.info("Found ip address " + ipAddress);
		} else {
			logger.debug("Failed to get ip address.");
		}
		return ipAddress;
	}

	public String convertLicenseDetailsToString(Map<String, String> licenseMap) {
		StringBuilder str = new StringBuilder();
		String serverId = getServerId();
		str.append(CUSTOMER_NAME).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(CUSTOMER_NAME)).append("\n");
		str.append(LOCATION).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(LOCATION)).append("\n");
		str.append(MACID).append(LICENSE_SPLIT_TOKEN).append(serverId).append("\n");
		str.append(PRODUCT).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(PRODUCT)).append("\n");
		str.append(VERSION).append(LICENSE_SPLIT_TOKEN).append(FULL).append("\n");
		str.append(START_DATE).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(START_DATE)).append("\n");
		str.append(END_DATE).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(END_DATE)).append("\n");
		str.append(HOSTNAME).append(LICENSE_SPLIT_TOKEN).append(LicenseConstants.LICENSE_HOSTNAME_DEFAULT).append("\n");
		str.append(DAILY_RECORDS).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(DAILY_RECORDS)).append("\n");
		str.append(MONTHLY_RECORDS).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(MONTHLY_RECORDS)).append("\n");
		str.append(TPS).append(LICENSE_SPLIT_TOKEN).append(licenseMap.get(TPS)).append("\n");
		str.append(IS_ABOVE_730).append(LICENSE_SPLIT_TOKEN).append("true").append("\n");
		str.append(NO_OF_CONTAINER).append(LICENSE_SPLIT_TOKEN).append(DEFAULT_NO_OF_CONTAINER);
		return str.toString();
	}

	public String encryptLicenseDetail(String licData, String repositoryPath) {
		String result = "false";
		String message = licData;
		String privateKeyPath = repositoryPath + PRIVATE_STR;
		String actualData = repositoryPath + TEMP_FILE_LICENSE;
		String licenseKey = repositoryPath + LICENSE_STR;
		File productInfo = new File(repositoryPath + TEMP_FILE_LICENSE);
		byte[] b1 = encryptSymMsg(message, repositoryPath);
		writeBytesToFile(productInfo, b1);
		try {
			result = encryptFile(privateKeyPath, new FileInputStream(actualData), new FileOutputStream(licenseKey));// NOSONAR
			productInfo.delete();// NOSONAR
		} catch (FileNotFoundException e) {
			logger.error(e);
			logger.error("Error in encryptLicenseDetail in LicenseUtility. Reason : " + e.getMessage());
		}
		if (!result.equalsIgnoreCase("false")) {
			result = licenseKey;
		}
		return result;
	}

	/**
	 * Encrypt sym msg.
	 *
	 * @param str            the str
	 * @param repositoryPath the repository path
	 * @return the byte[]
	 */
	public byte[] encryptSymMsg(String str, String repositoryPath) {
		ObjectInputStream inputStream = null;
		Key symKey = null;
		Cipher c = null;
		String symKeyFile = repositoryPath + KEYFILE;
		byte[] encryptedMessage = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(new File(symKeyFile)));// NOSONAR
			symKey = (Key) inputStream.readObject();
			c = Cipher.getInstance(ALGORITHMDESEDE);// NOSONAR
			c.init(Cipher.ENCRYPT_MODE, symKey);
			encryptedMessage = c.doFinal(str.getBytes());
		} catch (Exception e) {
			logger.error(e);
			logger.error("Error in encryptSymMsg in LicenseUtility. Reason : " + e.getMessage());
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				logger.error(e);
				logger.error("Error in encryptSymMsg in LicenseUtility");
			}
		}
		return encryptedMessage;
	}

	/**
	 * @param theFile - Represents destination file in which data will be written.
	 * @param bytes   - Takes the data in form of byte array.
	 */
	public void writeBytesToFile(File theFile, byte[] bytes) {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(theFile))) {
			bos.write(bytes);
		} catch (IOException e) {
			logger.error("Error in writeBytesToFile in LicenseUtility. Reason : " + e.getMessage());
			logger.error(e);
		}
	}

	/**
	 * Encrypt file.
	 *
	 * @param privateKeyPath the private key path
	 * @param in             the in
	 * @param out            the out
	 * @return the string
	 */
	private String encryptFile(String privateKeyPath, InputStream in, OutputStream out) {
		byte[] b = new byte[128];
		// Create a cipher object and use the generated key to initialize it
		PrivateKey privateKey = null;
		Cipher cipher = null;
		CipherOutputStream cos = null;
		String result = "false";
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(privateKeyPath));) {
			privateKey = (PrivateKey) inputStream.readObject();
			cipher = Cipher.getInstance(ALOGRITHM); // NOSONAR
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			cos = new CipherOutputStream(out, cipher);
			int i;
			while ((i = in.read(b)) >= 0) {
				cos.write(b, 0, i);
			}
			result = "true";
		} catch (Exception e) {
			logger.error(e);
			logger.error("Error in encryptFile in LicenseUtility. Reason : " + e.getMessage());
		} finally {
			try {
				if (cos != null) {
					cos.flush();
					cos.close();
				}
				if (in != null)
					in.close();
			} catch (IOException e) {
				logger.error(e);
				logger.error("Error in encryptFile in LicenseUtility. Reason : " + e.getMessage());
			}

		}
		return result;
	}

	public Map<Integer, Map<String, Object>> extractCircleAndDeviceFromLicense(ResponseObject responseObject,
			String repositoryPath) throws IOException {
		Map<Integer, Map<String, Object>> map = new HashMap<Integer, Map<String, Object>>();
		if (responseObject.isSuccess()) {
			List<License> licenseList = (List<License>) responseObject.getObject();
			for (License license : licenseList) {
				if (license.getStatus().equals(StateEnum.ACTIVE)) {
					if (license.getCircle() != null) {
						Map<String, Object> licenceType = new HashMap<String, Object>();
						if(license.getLicenceType().equals(LicenseTypeEnum.DEVICE)){
							licenceType.put(license.getLicenceType().name(),
									extractLicenseDeviceFromKey(getDecryptTPS(license.getTps(), repositoryPath)));
						}else {
							licenceType.put(license.getLicenceType().name(),
									getDecryptTPS(license.getTps(), repositoryPath));							
						}
						map.put(license.getCircle().getId(), licenceType);
					}
				}
			}
		}
		return map;
	}

	public List<String> extractLicenseDeviceFromKey(String str) {
		if (str != null && !str.equals("")) {
			return Stream.of(str.split(BaseConstants.LICENSE_KEY_SPILTER))
					.map(elem -> elem.substring(0, elem.lastIndexOf(BaseConstants.LICENSE_KEY_NAME_SEPERATER)))
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}

	private String getDecryptTPS(byte[] encTps, String repositoryPath) throws IOException {
		if (encTps != null) {
			logger.info("repositoryPath for license folder = " + repositoryPath);
			return decryptSymMsg(encTps, repositoryPath);
		} else {
			return null;
		}
	}

	public Map<String, String> extractLicenseDevicemap(String str) {
		if (str != null && !str.equals("")) {
			return Stream.of(str.split(BaseConstants.LICENSE_KEY_SPILTER))
					.collect(Collectors.toMap(
							elem -> elem.substring(0, elem.lastIndexOf(BaseConstants.LICENSE_KEY_NAME_SEPERATER)),
							elem -> elem.substring(elem.lastIndexOf(BaseConstants.LICENSE_KEY_NAME_SEPERATER) + 1)));
		} else {
			return null;
		}
	}
}