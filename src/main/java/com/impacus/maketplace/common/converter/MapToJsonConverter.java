package com.impacus.maketplace.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class MapToJsonConverter implements AttributeConverter<Map<Long, String>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<Long, String> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new CustomException(CommonErrorType.FAIL_TO_CONVERT_MAP, "맵을 JSON으로 변환하는 데 오류가 발생했습니다");
        }
    }

    @Override
    public Map<Long, String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(dbData, HashMap.class);
        } catch (IOException e) {
            throw new CustomException(CommonErrorType.FAIL_TO_CONVERT_MAP, "JSON을 맵으로 변환하는 데 오류가 발생했습니다");
        }
    }
}
