package com.lsntsolutions.gtmApp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

public class IntegrationTestUtil {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	public static String convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		System.out.println(mapper.writeValueAsString(object));
		return mapper.writeValueAsString(object);
	}
}