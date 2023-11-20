package com.codegenerator.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.codegenerator.app.module.Button;
import com.codegenerator.app.module.MenuTree;
import com.codegenerator.app.module.MenuTreeVo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MenuController {

    private static String[] leftKeys;


    @PostMapping("/writeJsonToFile")
    public MenuTree writeJsonToFile(@RequestBody MenuTree menus) {
//        ClassPathResource resource = new ClassPathResource("/menus.json");
//        // 获取 menus.json 文件的路径
//        String menuFilePath = resource.getFile().getPath();
//
//        // 读取 menus.json 文件
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        MenuTree menus = objectMapper.readValue(new File(menuFilePath), MenuTree.class);



        // 将json写入到每一个文件中
        final String dir = "target/vm/";

        deleteDirectory(new File(dir));

        menus.getChildren().forEach(i->{

            String[] arr = i.getId().split("\\.");
            try {
                Button button = new Button();
                BeanUtils.copyProperties(i, button);

                String key = arr[arr.length-1];
                button.setId(key);

                writeJsonToFile(JSONObject.toJSONString(button), dir+key+".json");
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        return menus;
    }

    @GetMapping("/readJson")
    public MenuTreeVo readJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("menus.json");
        // 获取 menus.json 文件的路径
        String menuFilePath = resource.getFile().getPath();

        // 读取 menus.json 文件
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MenuTree menus = objectMapper.readValue(new File(menuFilePath), MenuTree.class);

        parseRefJson(menus);

        String json = JSON.toJSONString(menus);

        return JSON.parseObject(json, MenuTreeVo.class);
    }

    @PostMapping("/check")
    public List<String> checkVaild(@RequestBody MenuTreeVo body) {

        errorIds.clear();

        // 将id解析成key
        check(body, "");

        return errorIds;
    }


    private List<String> errorIds = new ArrayList<>();

    public void check(MenuTreeVo body, String parentId) {
        for (MenuTreeVo m : body.getChildren()) {
            if (!m.getId().startsWith(parentId)) {
                errorIds.add(m.getId());
            }

            check(m, m.getId());
        }
    }


    @PostMapping("/parseIdToKey")
    public MenuTreeVo parseIdToKey(@RequestBody MenuTreeVo body) {

        // 将id解析成key
        process(body, "");

        return body;
    }

    public void process(MenuTreeVo body, String parentId) {
        // 将id解析成key
        for (MenuTreeVo m : body.getChildren()) {

            String key;
            if (StringUtils.isEmpty(parentId)) {
                key = m.getId();
            } else {
                String[] keys = m.getId().replace(parentId+".", "").split("\\.");
                key = keys[0];
            }

//            leftKeys =  keys.length >= 1 ? Arrays.copyOfRange(keys, 1, keys.length-1) : ArrayUtils.EMPTY_STRING_ARRAY;
            m.setKey(key);
            process(m, m.getId());


        }
    }

    @PostMapping("/formatJson")
    public MenuTreeVo formatJson(@RequestBody MenuTree menus ) throws IOException {

        parseRefJson(menus);

        String json = JSON.toJSONString(menus, SerializerFeature.WriteEnumUsingToString);

        return JSON.parseObject(json, MenuTreeVo.class);
    }

    private void parseRefJson(MenuTree tree) throws IOException {
        List<MenuTree> children = new ArrayList<>();
        for (MenuTree i : tree.getChildren()) {
            MenuTree menu = i;
            if (StringUtils.isNotEmpty(i.getRefJson())) {
                menu = convert(i.getRefJson());
            }

            if (StringUtils.isNotEmpty(i.getRefButton())) {
                menu = convert(i.getRefButton());
                menu.setId(tree.getId()+"."+menu.getId());
            }

            // 如果该节点的id没有'.'符号，就将其的完整路径作为前缀构造id
            if (!i.getId().contains(".")) {
                menu.setId(tree.getId()+"."+i.getId());
            }

            children.add(menu);

            parseRefJson(menu);
        }

        tree.setChildren(children);
    }


    public void writeJsonToFile(String jsonString, String filePath) throws IOException {
        // Create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        // Create file object
        File file = new File(filePath);
        // Create file directory if it does not exist
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        // Create file if it does not exist
        if (!file.exists()) {
            file.createNewFile();
        }
        // Write formatted JSON string to file
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, mapper.readTree(jsonString));

    }

    public void deleteDirectory(File directory) {
        // Check if directory is not null and exists
        if (directory != null && directory.exists()) {
            // Get list of files and directories in the directory
            File[] files = directory.listFiles();
            // Delete all files and directories in the directory
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursively delete subdirectories and files
                    deleteDirectory(file);
                } else {
                    // Delete file
                    file.delete();
                }
            }
            // Delete directory
            directory.delete();
        }
    }

    private MenuTree convert(String refJson) throws IOException {
        ClassPathResource resource = new ClassPathResource(refJson);
        // 获取 menus.json 文件的路径
        String menuFilePath = resource.getFile().getPath();

        // 读取 menus.json 文件
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MenuTree menus = objectMapper.readValue(new File(menuFilePath), MenuTree.class);

        return menus;
    }
}
