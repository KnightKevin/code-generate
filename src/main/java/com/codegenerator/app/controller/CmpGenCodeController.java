package com.codegenerator.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.codegenerator.app.enums.RespCode;
import com.codegenerator.app.enums.StepKey;
import com.codegenerator.app.model.DbField;
import com.codegenerator.app.module.MenuTree;
import com.codegenerator.app.util.DbTypeConvert;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CmpGenCodeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    /**
     * 生成db相关的文件
     *
     * */
    @GetMapping("/db")
    public String db(
            String db,
            String tableName,
            String controllerPackage,
            String facadePackage,
            String entityPackage,
            String module
    ) throws IOException, TemplateException {

        String query = String.format("SELECT COLUMN_NAME, DATA_TYPE, COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '%s' and TABLE_NAME = '%s'", db, tableName);
        List<DbField> list = new ArrayList<>(jdbcTemplate.query(query, (rs, rowNum) -> {


            DbField field = new DbField();
            field.setName(rs.getString("COLUMN_NAME"));
            field.setDataType(rs.getString("DATA_TYPE"));
            field.setVarName(convertToCamelCase(field.getName()));


            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("DATA_TYPE");
            String columnType = rs.getString("COLUMN_TYPE");
            return field;
        }));






        // cmd bean中会排除数据库中那些不须要处理的字段
        String[] cmdExcludeFields = {"createDate", "updateDate"};

        // 获取FreeMarker配置
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        configuration.setSharedVariable("dbTypeConvert", new DbTypeConvert());

        // 加载模板
        Template entityTemplate = configuration.getTemplate("dao/Entity.ftl");
        Template qTemplate = configuration.getTemplate("dao/q.ftl");
        Template serviceTemplate = configuration.getTemplate("dao/service.ftl");
        Template serviceImplTemplate = configuration.getTemplate("dao/serviceImpl.ftl");
        Template replyTemplate = configuration.getTemplate("dao/reply.ftl");
        Template apiTemplate = configuration.getTemplate("dao/api.ftl");
        Template apiImplTemplate = configuration.getTemplate("dao/apiImpl.ftl");
        Template controllerTemplate = configuration.getTemplate("dao/controller.ftl");
        Template cmdTemplate = configuration.getTemplate("dao/cmd.ftl");
        Template qryTemplate = configuration.getTemplate("dao/qry.ftl");
        Template cmdPostBodyTemplate = configuration.getTemplate("dao/cmdPostBody.ftl");










        final String dir = "target/dao/";



        deleteDirectory(new File(dir));

        String className = uppercaseFirstChar(convertToCamelCase(tableName));
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("tableClassVarName", convertToCamelCase(tableName));
        entityMap.put("className", uppercaseFirstChar(className));
        entityMap.put("entityPackage", entityPackage);
        entityMap.put("module", module);

        entityMap.put("list", list);
        entityMap.put("cmdExcludeFields", cmdExcludeFields);
        // 渲染模板并获取文本内容
        String renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(entityTemplate, entityMap);

        String fileName = dir + String.format("entity/%s.java", className);
        writeToFile(renderedText, fileName);

        // reply对象
        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(replyTemplate, entityMap);
        fileName = dir + String.format("reply/%sReply.java", className);
        writeToFile(renderedText, fileName);

        // 渲染Q对象
        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(qTemplate, entityMap);
        fileName = dir + String.format("querydsl/Q%s.java", className);
        writeToFile(renderedText, fileName);

        // IxxService.java对象
        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(serviceTemplate, entityMap);
        fileName = dir + String.format("service/I%sService.java", className);
        writeToFile(renderedText, fileName);

        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(serviceImplTemplate, entityMap);
        fileName = dir + String.format("service/%sServiceImpl.java", className);
        writeToFile(renderedText, fileName);

        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(apiTemplate, entityMap);
        fileName = dir + String.format("facade/I%sApi.java", className);
        writeToFile(renderedText, fileName);

        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(apiImplTemplate, entityMap);
        fileName = dir + String.format("facade/%sApiImpl.java", className);
        writeToFile(renderedText, fileName);

        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(controllerTemplate, entityMap);
        fileName = dir + String.format("controller/%sController.java", className);
        writeToFile(renderedText, fileName);

        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(cmdTemplate, entityMap);
        fileName = dir + String.format("request/%sCmd.java", className);
        writeToFile(renderedText, fileName);


        fileName = dir + String.format("request/%sQry.java", className);
        render(fileName, qryTemplate, entityMap);


        renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(cmdPostBodyTemplate, entityMap);
        fileName = dir + String.format("%s.json", className);
        writeToFile(renderedText, fileName);
        return renderedText;
    }

    private void render(String fileName, Template template, Map<String, Object> entityMap) throws IOException, TemplateException {
        String renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(template, entityMap);
        writeToFile(renderedText, fileName);
    }

    /**
     * 生成返回码相关的国际化中文部分
     * */
    @GetMapping("/respCodeI18n")
    public String respCodeI18n() throws IOException, TemplateException {
        final String dir = "target/i18n/";
        deleteDirectory(new File(dir));

        String fileName = dir + "/Messages_zh_CN.properties";

        // 获取FreeMarker配置
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("i18n.ftl");

        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("list", StepKey.values());
        // 渲染模板并获取文本内容
        String renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(template, entityMap);

        writeToFile(renderedText, fileName);

        return renderedText;
    }

    @GetMapping("/c")
    public String c(@RequestParam List<String> arr) throws IOException, TemplateException {
        final String dir = "target/temp/";
        deleteDirectory(new File(dir));

        for (String i : arr) {
            String fileName = dir+i;

            // 获取FreeMarker配置
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("temp.ftl");

            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("fileName", i.replace(".java", ""));
            // 渲染模板并获取文本内容
            String renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(template, entityMap);

            writeToFile(renderedText, fileName);
        }



        return "ok";
    }

    @PostMapping("/d")
    public String d(@RequestBody MenuTree body) throws IOException, TemplateException {

        final String dir = "target/dao/";
        deleteDirectory(new File(dir));

        String fileName = dir + "/Menu.txt";

        // 获取FreeMarker配置
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("MenuBuilder.ftl");

        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put("list", body.getChildren());
        // 渲染模板并获取文本内容
        String renderedText = FreeMarkerTemplateUtils.processTemplateIntoString(template, entityMap);

        writeToFile(renderedText, fileName);

        return renderedText;
    }

    private void writeToFile(String text, String fileName) throws IOException {
        // 创建实体类


        // 创建文件对象
        File file = new File(fileName);

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

    /**
     * 转小驼峰
     * */
    public static String convertToCamelCase(String underscoreString) {
        StringBuilder camelCase = new StringBuilder();
        boolean nextUpperCase = false;

        for (int i = 0; i < underscoreString.length(); i++) {
            char c = underscoreString.charAt(i);

            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    camelCase.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    camelCase.append(Character.toLowerCase(c));
                }
            }
        }

        return camelCase.toString();
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
