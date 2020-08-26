package com.study.netty.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private JsonUtil() {}
	private static final JsonUtil jsonUtil = new JsonUtil();
	public static JsonUtil getInstance() {
		return jsonUtil;
	}
	
	/**
	 * 获取对象转换后的json字符串
	 * @param obj
	 * @return
	 */
	public String getJsonByObject(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
        String mapJackson = "";
		try {
			mapJackson = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			System.out.println("JsonUtil getJsonByObject error :" + e);
		}
        return mapJackson;
	}
	
}
