package com.codegenerator.app.module;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MenuTree extends Menu {
    private List<MenuTree> children = new ArrayList<>();
}
