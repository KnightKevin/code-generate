package com.codegenerator.app.module;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class Button {
    private String key;
    /**
     * 选填，默认是所有角色类型都生效
     *
     * 生效范围，{@link com.zstack.cmp.base.model.value.RoleType}, size is empty的时候表示所有内置角色都有, 但每个菜单节点的该值会受到父级菜单的影响：
     * 如果父级的range.size不为0,而本级的size为0则会继承父级的range，否则会与父级取交集
     * */
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
