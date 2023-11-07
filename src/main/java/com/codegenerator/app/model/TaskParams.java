package com.codegenerator.app.model;

import lombok.Data;

@Data
public class TaskParams {
    private String jobId;
    private String stepExecutionId = "";
    private String keyName;
    private String val;
}
