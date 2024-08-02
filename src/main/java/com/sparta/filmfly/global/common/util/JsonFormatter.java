package com.sparta.filmfly.global.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonFormatter {
    public static String formatJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonString, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(json);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonString; // 오류 발생 시 원래 JSON 문자열 반환
        }
    }
}