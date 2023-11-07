package com.codegenerator.app.model;

import lombok.Data;

@Data
public class InputParams {
    private String name;
    private Disk disk;

    @Data
    public static class Disk {
        private String id;
        private String name;
    }
}
