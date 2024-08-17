package com.backend.testdata.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


@TestComponent
public class FormDataEncoder {

    @Autowired
    private ObjectMapper mapper;


    public String encode(Object obj) {
        return encode(obj, true);
    }

    public String encode(Object obj, boolean applyUrlEncoding) {
        Map<String, Object> fieldMap = mapper.convertValue(obj, new TypeReference<>() {
        });

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        fieldMap.forEach((key, value) -> addToBuilder(builder, key, value));

        return applyUrlEncoding ? builder.build().encode().getQuery() : builder.build().getQuery();
    }

    private void addToBuilder(UriComponentsBuilder builder, String key, Object value) {
        switch (value) {
            case Map<?, ?> map ->
                    map.forEach((subKey, subValue) -> addToBuilder(builder, key + "." + subKey, subValue));
            case List<?> list ->
                    IntStream.range(0, list.size()).forEach(i -> addToBuilder(builder, key + "[" + i + "]", list.get(i)));
            case null -> {}
            default -> builder.queryParam(key, value.toString());
        }
    }
}
