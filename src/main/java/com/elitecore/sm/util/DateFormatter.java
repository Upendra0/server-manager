/**
 * 
 */
package com.elitecore.sm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.SystemParametersConstant;

/**
 * @author Sunil Gulabani Apr 8, 2015
 */
public class DateFormatter {
	private  static Logger logger = Logger.getLogger(DateFormatter.class);

	private DateFormatter() {
		// no class needs to instantiate this, only static utility methods.
	}

	public static SimpleDateFormat getShortDataFormat() {
		return new SimpleDateFormat(MapCache.getConfigValueAsString(
				SystemParametersConstant.DATE_FORMAT, "dd/MM/yyyy"));
	}

	/**
	 * Formats the date.
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat ;
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(
						MapCache.getConfigValueAsString(
								SystemParametersConstant.DATE_TIME_FORMAT,
								"dd-MM-yyyy HH:mm:ss"));
				return dateFormat.format(date);
			}
		} catch (Exception e) {
			logger.error("Issue in date formatting", e);
		}

		return "";
	}

	
	/**
	 * Formats the date.
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date, String formate) {
		SimpleDateFormat dateFormat ;
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(formate);
				return dateFormat.format(date);
			}
		} catch (Exception e) {
			logger.error("Issue in date formatting", e);
		}

		return "";
	}
	
	
	/**
	 * Method will convert string to date.
	 * @param date
	 * @param formate
	 * @return
	 */
	public static Date formatDate(String date, String formate){
		logger.debug("Formatting date " + date  + " to formate " + formate);
	    DateFormat dateFormate = new SimpleDateFormat(formate); 
	    Date formatedDate = null;
	    try {
	    	formatedDate = dateFormate.parse(date);
	    } catch (ParseException e) {
	        logger.error("Failed to parser date in formate " + formate);
	    }
		return formatedDate;
	}
	
	/**
	 * Provides the End of the day.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * Provides the start of the day.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * Converts String to Date
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static Date stringToDate(String date, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		
		if(StringUtils.isEmpty(date)) {
			return null;
		} else {
			try {
				return format.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}
	
	/**
	 * Converts Date to String
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String dateToString(Date date, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		
		if(date == null) {
			return StringUtils.EMPTY;
		} else {
			
				return format.format(date);
			
		}
	}
}