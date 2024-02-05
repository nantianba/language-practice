package com.nantianba.study.feature.jdk21.str;

import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Templates {

    /**
     * 稍微更优雅一点
     */
    public final static StringTemplate.Processor<String, RuntimeException> $ = STR;
    public final static StringTemplate.Processor<String, RuntimeException> JSON = t -> {
        List<String> values = t.values().stream()
                .map(o -> new GsonBuilder().create().toJson(o))
                .toList();

        return StringTemplate.interpolate(t.fragments(), values);
    };
}
