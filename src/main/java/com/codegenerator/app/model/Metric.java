package com.codegenerator.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Metric {
    private String instanceId;
    private double value;
}
