package com.codegenerator.app.module;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleType {
    /**
     *
     * sdfgsdfg
     * */
    PLATFORM("platform"),
    VDC("vdc");

    private final String code;


    @JsonValue
    public String getCode() {
        return code;
    }

}