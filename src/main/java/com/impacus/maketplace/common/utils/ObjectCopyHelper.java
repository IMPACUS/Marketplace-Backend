package com.impacus.maketplace.common.utils;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class ObjectCopyHelper {
    private Gson gson = new Gson();

    public <T> T copyObject(Object sourceObject, Class<T> targetType) {
        if (sourceObject == null) {
            return null;
        }

        String sourceJsonString = gson.toJson(sourceObject);
        Object tartgetObject = gson.fromJson(sourceJsonString, targetType);
        return targetType.cast(tartgetObject);
    }
}