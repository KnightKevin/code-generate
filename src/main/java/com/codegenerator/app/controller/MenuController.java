package com.codegenerator.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.codegenerator.app.module.Button;
import com.codegenerator.app.module.MenuTree;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
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

    @PostMapping("/writeJsonToFile")
    public MenuTree writeJsonToFile(@RequestBody MenuTree menus) throws IOException {
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
                button.setKey(key);

                writeJsonToFile(JSONObject.toJSONString(button), dir+key+".json");
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        return menus;
    }

    @GetMapping("/readJson")
    public MenuTree readJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("menus.json");
        // 获取 menus.json 文件的路径
        String menuFilePath = resource.getFile().getPath();

        // 读取 menus.json 文件
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MenuTree menus = objectMapper.readValue(new File(menuFilePath), MenuTree.class);

        parseRefJson(menus);


        return menus;
    }

    private void parseRefJson(MenuTree tree) throws IOException {
        List<MenuTree> children = new ArrayList<>();
        for (MenuTree i : tree.getChildren()) {
            MenuTree menu = i;
            if (StringUtils.hasText(i.getRefJson())) {
                menu = convert(i.getRefJson());
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
