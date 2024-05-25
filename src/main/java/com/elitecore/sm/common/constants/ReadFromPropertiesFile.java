package com.elitecore.sm.common.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadFromPropertiesFile {
	public static String getProperties(String key) throws IOException
	{
	InputStream inputStream=ReadFromPropertiesFile.class.getClassLoader().getResourceAsStream("rsa.properties");
	Properties myproperties = new Properties();
	myproperties.load(inputStream);
	return myproperties.getProperty(key);
	}
}
