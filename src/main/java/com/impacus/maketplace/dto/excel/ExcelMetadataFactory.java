package com.impacus.maketplace.dto.excel;

import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.annotation.excel.ExcelSheet;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.SuperClassReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelMetadataFactory {

    public static ExcelMetadataFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Class의 엑셀관련 어노테이션을 파악하여 메타데이터를 정리
     *
     * @param clazz 엑셀 데이터
     * @return
     */
    public ExcelMetadata createMetadata(Class<?> clazz) {
        Map<String, String> headerNamesMap = new LinkedHashMap<>();
        List<String> fieldNames = new ArrayList<>();

        for (Field field : SuperClassReflectionUtils.getAllFields(clazz)) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
                headerNamesMap.put(field.getName(), columnAnnotation.headerName());
                fieldNames.add(field.getName());
            }
        }

        if (headerNamesMap.isEmpty()) {
            throw new CustomException(CommonErrorType.FAIL_TO_CREATE_EXCEL, "Class %s has not @ExcelColumn at all");
        }

        return new ExcelMetadata(headerNamesMap, fieldNames, getSheetName(clazz));
    }

    private String getSheetName(Class<?> clazz) {
        ExcelSheet annotation = (ExcelSheet) SuperClassReflectionUtils.getAnnotation(clazz, ExcelSheet.class);
        if (annotation != null) {
            return annotation.name();
        }
        return "Data";
    }

    private static class SingletonHolder {
        private static final ExcelMetadataFactory INSTANCE = new ExcelMetadataFactory();
    }
}