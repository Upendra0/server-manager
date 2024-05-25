package com.elitecore.sm.parser.comparator;

import java.util.Comparator;

import com.elitecore.sm.parser.model.ParserAttribute;


public class ParserAttributeComparator implements Comparator<ParserAttribute>{

	@Override
	public int compare(ParserAttribute parserAttribute1, ParserAttribute parserAttribute2) {
		if(parserAttribute1 != null && parserAttribute2 != null) {
			if(parserAttribute1.getAttributeOrder() >= parserAttribute2.getAttributeOrder()){
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}
}
