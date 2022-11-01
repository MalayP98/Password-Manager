package com.key.password_manager.utils;

import java.util.HashMap;
import java.util.Map;

public class GenericReponse {

    private Map<String, String> responseMap = new HashMap<>();

    public GenericReponse add(String key, String value) {
        responseMap.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return responseMap.toString();
    }

    public String toJsonString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{ \n");
        for (Map.Entry<String, String> entrySet : responseMap.entrySet()) {
            builder.append("\"" + entrySet.getKey() + "\" : \"" + entrySet.getValue() + "\", \n");
        }
        builder.append("}");
        return builder.toString();
    }
}
