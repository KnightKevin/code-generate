package com.codegenerator.app.util;

import freemarker.template.TemplateMethodModelEx;

import java.util.List;

public class DbTypeConvert implements TemplateMethodModelEx {

    private static final String numberPathTemplate = "NumberPath<%s>";
    private static final String createNumberPathTemplate = "createNumber(\"%s\", %s.class)";

    private static final String stringPathTemplate = "StringPath<%s>";
    private static final String createStringPathTemplate = "createString(\"%s\")";

    private static final String datePathTemplate = "DateTimePath<java.util.Date>";
    private static final String createDatePathTemplate = "createDateTime(\"%s\", java.util.Date.class)";

    @Override
    public Object exec(List args) {

        String dbType = args.get(0).toString();

        String type;
        switch (dbType) {
            case "text":
            case "varchar":
                type = "String";
                break;
            case "int":
                type = "Integer";
                break;
            case "double":
                type = "Double";
                break;
            case "decimal":
                type = "BigDecimal";
                break;
            case "datetime":
                type = "Date";
                break;
            default:
                type = String.format("/* the dbType{%s} has not been parsed yet! */", dbType);
        }


        return type;
    }
}
