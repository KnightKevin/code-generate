package com.codegenerator.app.module;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class MenuTree extends Menu {
    /**
     * 选填
     * 引用目标文件。表示该menu的结构与refJson目标json文件一致，会去找starter/src/main/resources/gconfig/menus/{refJson}
     * 一旦该字段被配置，除了range，其他字都都无效了
     * */
    private String refJson = "";

    private String refButton = "";
    private List<MenuTree> children = new ArrayList<>();
}
