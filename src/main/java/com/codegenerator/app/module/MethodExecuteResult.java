package com.codegenerator.app.module;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MethodExecuteResult {
    private boolean isSuccess;
    private Throwable throwable;
    private String errorMsg;
}
