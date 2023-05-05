package com.codegenerator.app.module;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class Menu {

    /**
     * 必填
     * */
    private String id = "";

    private List<String> range = new ArrayList<>(4);

    /**
     * 必填
     * 菜单名称
     * */
    private String name = "";

    /**
     * 必填
     * 菜单类型： menu-菜单， button-按钮
     * */
    private String type = "";

    /**
     * 选填
     * 拥有的api权限
     * */
    private Set<String> rights = new HashSet<>();
}
