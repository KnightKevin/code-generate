package com.codegenerator.app.controller;

import com.codegenerator.app.enums.MoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class BController {

    @Autowired
    TemplateEngine templateEngine;


    @GetMapping("/b")
    public String a() throws IOException {

        final String dir = "target/sync/";

        deleteDirectory(new File(dir));

        for (MoType moType : MoType.values()) {

            String firstUppercaseCharMoType = uppercaseFirstChar(moType.getCode());

            Context context = new Context();
            context.setVariable("moType", firstUppercaseCharMoType);
            String text = templateEngine.process("ICloudSyncTemplate.txt", context);

            String fileName = String.format("I%sCloudSync.java", firstUppercaseCharMoType);

            // 创建文件对象
            File file = new File(dir+fileName);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it does not exist
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();
        }


        return "a";
    }

    @GetMapping("/c")
    public String c() throws IOException {

        final String dir = "target/sync/";

        deleteDirectory(new File(dir));

        for (MoType moType : MoType.values()) {

            String firstUppercaseCharMoType = uppercaseFirstChar(moType.getCode());

            Context context = new Context();
            context.setVariable("moType", firstUppercaseCharMoType);
            String text = templateEngine.process("MoTypeConfig.txt", context);

            String fileName = String.format("%sSyncConfig.java", firstUppercaseCharMoType);

            // 创建文件对象
            File file = new File(dir+fileName);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Create file if it does not exist
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.write(text);
            writer.close();
        }


        return "a";
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

    private String uppercaseFirstChar(String s) {
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

}
