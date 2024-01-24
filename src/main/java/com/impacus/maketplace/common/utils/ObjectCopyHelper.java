package com.impacus.maketplace.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.impacus.maketplace.common.adapter.LocalDateTimeTypeAdapter;
import com.impacus.maketplace.common.adapter.LocalDateTypeAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ObjectCopyHelper {
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    public <T> T copyObject(Object sourceObject, Class<T> targetType) {
        if (sourceObject == null) {
            return null;
        }

        String sourceJsonString = gson.toJson(sourceObject);
        Object tartgetObject = gson.fromJson(sourceJsonString, targetType);
        return targetType.cast(tartgetObject);
    }
}