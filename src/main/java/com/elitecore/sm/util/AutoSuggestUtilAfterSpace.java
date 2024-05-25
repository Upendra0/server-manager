package com.elitecore.sm.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class AutoSuggestUtilAfterSpace {

	private AutoSuggestUtilAfterSpace() {
	}

	private static List<String> unifiedDataList = new ArrayList<>();

	protected static Logger logger = Logger.getLogger("com.elitecore.sm.util.AutoSuggestUtilAfterSpace");

	public static List<String> simulateSearchResultAfterSpace(String tagName, List<String> list) {

		unifiedDataList = list;
		logger.info("Enter into action method to get autocomplete data for String : " + tagName);
		List<String> result = new ArrayList<>();

		try {
			// iterate a list and filter by tagName
			if (!tagName.contains(",")) {
				tagName = tagName.trim();
				for (String tag : unifiedDataList) {
					//if (tag.startsWith(tagName)) {
						result.add(tag);
					//}
				}
			} else {
				for (String tag : unifiedDataList) {
					String tagNameWithoutSpace = tagName.replace(" ", "");
					//if ((tagNameWithoutSpace).contains(tag + ",")) {
						result = getDataReccursively(
								tagNameWithoutSpace.substring(0, tagNameWithoutSpace.indexOf(",")));
						return result;
					//}
				}
			}
		} catch (Exception e) {
			logger.error("Error while getting auto complete data for policy action, Reason : " + e.getMessage());
			logger.error(e);
		}
		logger.info("Autocomplete data list :: " + result);
		return result;
	}

	private static List<String> getDataReccursively(String dataStr) {
		List<String> result = new ArrayList<>();
		if (dataStr != null) {
			for (String tag : unifiedDataList) {
				result.add(tag);
			}
		}
		return result;
	}

}
