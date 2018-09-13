package com.zd.business.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JacksonUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	};

	private static final ObjectWriter writer = objectMapper.writer();
	private static final ObjectWriter prettyWriter = objectMapper.writerWithDefaultPrettyPrinter();

	public static String objToJsonPretty(Object value) {
		try {
			return prettyWriter.writeValueAsString(value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String objToJson(Object obj) {
		try {
			return writer.writeValueAsString(obj);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> T jsonToObj(String json, Class<T> clazz) {
		if (json == null)
			return null;
		try {
			return objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to parse Json String.", e);
		}
	}

	/**
	 * for List, Map
	 *
	 * @param json
	 * @param typeReference
	 * @return
	 * @throws Exception
	 */
	public static <T> T jsonToObj(String json, TypeReference<T> typeReference) {
		if (json == null)
			return null;
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to parse Json String.", e);
		}
	}
}
