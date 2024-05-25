package com.elitecore.sm.util;

import java.util.Comparator;

import com.elitecore.sm.systemparam.model.SystemParameterData;

/**
 * Class is used to sort System Parameter using displayOrder property
 * @author avani.panchal
 *
 */
public class DisplayOrderComparator implements Comparator<SystemParameterData>{
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare()
	 */
	@Override
	public int compare(SystemParameterData s1, SystemParameterData s2) {
		if(s1.getDisplayOrder()>s2.getDisplayOrder()){
			return 1;
		}else{
			return -1;
		}
	}
}
