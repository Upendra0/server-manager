package com.elitecore.sm.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JsonDateDeserializer extends JsonDeserializer<Date> {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private SimpleDateFormat dateFormat = DateFormatter.getShortDataFormat();

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException {
		String date = jsonparser.getText();
		Date deserializeDate = null;
		try {
			deserializeDate = dateFormat.parse(date);
		} catch (ParseException e) {
			logger.error("Exception Occured in JsonDateDeserializer :"+e);
		}
		return deserializeDate;
	}
}
