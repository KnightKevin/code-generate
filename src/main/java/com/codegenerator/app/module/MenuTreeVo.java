package com.codegenerator.app.module;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuTreeVo extends Menu {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuTreeVo> children = new ArrayList<>();
}
