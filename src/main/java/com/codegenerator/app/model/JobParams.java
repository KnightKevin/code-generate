package com.codegenerator.app.model;

import lombok.Data;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@JobScope
@Component
@Data
public class JobParams implements Serializable {
    private String a;
    private String b;
}
