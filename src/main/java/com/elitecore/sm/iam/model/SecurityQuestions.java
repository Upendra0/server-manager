/**
 * 
 */
package com.elitecore.sm.iam.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunil Gulabani
 * May 7, 2015
 */
public class SecurityQuestions {
	private final static Map<String, String> questions ;
	static
	{
		questions = new HashMap<>();
		questions.put("BEST_FRIEND_FROM_CHILDHOOD","What is the name of your best friend from childhood?");
		questions.put("MANAGER_NAME_AT_FIRST_JOB","What is the name of your manager at your first job?");
		questions.put("FAV_HOLIDAY_PLACE","What is your favorite holiday place?");
		questions.put("FAV_PAST_TIME","What is your favorite past-time?");
		questions.put("NICKNAME","What is your nick name?");
		questions.put("VEHICLE_REGISTRATION_NUMBER","What is your vehicle registration number?");
		questions.put("YOUNGEST_CHILD_NICKNAME","What is your youngest child's nickname?");
		questions.put("FIRST_VEHICLE_COMPANY_NAME","What Make was your first car or bike?");
		questions.put("MOTHER_BIRTH_TOWN","What town was your mother born in?");
		questions.put("FIRST_SCHOOL_NAME","What was the name of your first school?");
		questions.put("SPOUSE_FIRST_MEET_PLACE","Where did you first meet your spouse?");
		questions.put("HONEYMOON_PLACE","Where did you spend your honeymoon?");
		questions.put("FAV_SPORTS","Which sports do you like?");
		questions.put("FAV_CHILDHOOD_HERO","Who was your childhood hero?");
	}
	public static Map<String, String> getQuestions() {
		return questions;
	}
}