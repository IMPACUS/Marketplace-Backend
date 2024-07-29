package com.impacus.maketplace.common.converter;

import com.impacus.maketplace.common.enumType.point.PointType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

import java.util.stream.Stream;

@Convert
public class PointTypeEnumConverter implements AttributeConverter<PointType, String> {
    @Override
    public String convertToDatabaseColumn(PointType pointType) {
        return pointType.getCode();
    }

    @Override
    public PointType convertToEntityAttribute(String dbData) {
        return Stream.of(PointType.values())
                .filter(t -> t.getValue().equals(dbData)).findFirst().orElse(null);

    }
}
