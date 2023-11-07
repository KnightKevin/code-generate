package com.codegenerator.app.model.level2;

import lombok.Data;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JobScope
@Component
@Data
public class Task2Context implements Serializable {


    private Object params;


    private Map<String, Object> data;

    /**
     * 这个step须要共享的数据
     * */
    private Map<String, Object> forNextData;


    public void putData(String k, Object v) {
        if (forNextData == null) {
            forNextData = new HashMap<>();
        }
        forNextData.put(k, v);
    }
}
