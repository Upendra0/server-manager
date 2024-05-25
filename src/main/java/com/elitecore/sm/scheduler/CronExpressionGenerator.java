package com.elitecore.sm.scheduler;


import org.quartz.CronExpression;

import com.elitecore.sm.trigger.model.CrestelSMTrigger;

public class CronExpressionGenerator {
	
	// WARNING: PLEASE DO NOT MAKE ANY CHANGE WITHOUT CONFIRMATION, IT WILL AFFECT THE SCHEDULE OF QUARTZ JOBS 
	
	public static String generateCronExpression(CrestelSMTrigger crestelSMTrigger) {
		//String cronExpression = "";
		StringBuilder sb = new StringBuilder();
		//sb.append(cronExpression);
		String recurrenceType =  crestelSMTrigger.getRecurrenceType();
		Integer alterationCount = crestelSMTrigger.getAlterationCount();
		String daysOfWeek = crestelSMTrigger.getDayOfWeek();
		Integer dayOfMonth = crestelSMTrigger.getDayOfMonth();
		String firstOrLastDayOfMonth = crestelSMTrigger.getFirstOrLastDayOfMonth();
		Integer executionStartingHour = crestelSMTrigger.getExecutionStartingHour();
		Integer executionEndingHour = crestelSMTrigger.getExecutionEndingHour();
		
		
		if("Minute".equalsIgnoreCase(recurrenceType) && alterationCount != null) {
			sb.append("0 0/"+ alterationCount);
			if(executionStartingHour != null && !executionStartingHour.equals(executionEndingHour)) {
				sb.append(appendExecutionStartingAndEndingHours(executionStartingHour, executionEndingHour));
			}else {
				sb.append(" *");
			}
			sb.append(" * * ? *");
			
		} else if("Hourly".equalsIgnoreCase(recurrenceType) && alterationCount != null) {
			sb.append("0 0");
			if(executionStartingHour != null && !executionStartingHour.equals(executionEndingHour)) {
			sb.append(appendExecutionStartingAndEndingHours(executionStartingHour, executionEndingHour));
			}else {
				sb.append(" 0");
			}
			sb.append("/"+alterationCount+" * * ? *");
			
		} else if("Daily".equalsIgnoreCase(recurrenceType) && alterationCount != null) {
			sb.append("0 0");
			sb.append(appendExecutionStartingHours(executionStartingHour));
			sb.append(" 1/"+alterationCount+" * ? *");//day counts - day starts from 1
			
		} else if("Weekly".equalsIgnoreCase(recurrenceType) && alterationCount != null) {
			sb.append("0 0");
			sb.append(appendExecutionStartingHours(executionStartingHour));
			sb.append(" ? * "+(daysOfWeek!=null?daysOfWeek:"*")+" *");
			
		} else if("Monthly".equalsIgnoreCase(recurrenceType) && alterationCount != null) {
			sb.append("0 0");
			sb.append(appendExecutionStartingHours(executionStartingHour));
			if(dayOfMonth != null) {
				sb.append(" "+dayOfMonth+" 1/"+alterationCount+" ? *");
			}else if(firstOrLastDayOfMonth != null && firstOrLastDayOfMonth.equalsIgnoreCase("L")) {
				sb.append(" "+firstOrLastDayOfMonth+" 1/"+alterationCount+" ? *");
			}
			
		}
			
		return sb.toString();
	}
	
	public static boolean isCronExpressionValid(String cronExpression) {
	   return CronExpression.isValidExpression(cronExpression);
	}
	
	public static String appendExecutionStartingAndEndingHours(Integer executionStartingHour, Integer executionEndingHour) {
		StringBuilder sb = new StringBuilder();
		
		if(executionEndingHour != null) {
			sb.append(" " + executionStartingHour+"-"+executionEndingHour);
		}else {
			sb.append(" " + executionStartingHour);
			
		}
		return sb.toString();
	
	}
	
	public static String appendExecutionStartingHours(Integer executionStartingHour) {
		return (executionStartingHour != null?(" " + executionStartingHour) : " 0");
	}
}
