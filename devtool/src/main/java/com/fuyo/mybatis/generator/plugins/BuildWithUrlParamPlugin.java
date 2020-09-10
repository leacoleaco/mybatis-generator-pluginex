package com.fuyo.mybatis.generator.plugins;

import com.fuyo.mybatis.generator.plugins.utils.BasePlugin;
import com.fuyo.mybatis.generator.plugins.utils.FormatTools;
import com.fuyo.mybatis.generator.plugins.utils.JavaElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

/**
 * 使用url参数进行构建
 *
 * @author leaco
 */
public class BuildWithUrlParamPlugin extends BasePlugin {

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        //增加自定义sql过滤内部类
        topLevelClass.addInnerClass(generateSqlFilterClass());

        //增加分页参数
        this.addPageField(topLevelClass);

        //增加build方法
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generateBuildMethod(topLevelClass, introspectedTable));

        //增加 buildWithUrlParam 方法
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generateBuildWithUrlMethod(topLevelClass, introspectedTable));
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generateBuildByUrlParamsMethod(topLevelClass, introspectedTable));
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generateBuildWhereExpression(topLevelClass, introspectedTable));
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generateBuildOrderExpressionMethod(topLevelClass, introspectedTable));
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generateParseMethod(topLevelClass, introspectedTable));
        FormatTools.addMethodWithBestPosition(topLevelClass, this.generatesafeParseMethod(topLevelClass, introspectedTable));
        return true;
    }


    private Method generateBuildMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "build",
                false, JavaVisibility.PUBLIC,
                FullyQualifiedJavaType.getCriteriaInstance()
        );
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        method.setStatic(true);
        method.addBodyLine(topLevelClass.getType().getShortName() + " example = new " + topLevelClass.getType().getShortName() + "();");
        method.addBodyLine("return example.createCriteria();");
        return method;
    }

    private Method generateBuildWithUrlMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "buildWithUrlParam",
                false, JavaVisibility.PUBLIC,
                FullyQualifiedJavaType.getCriteriaInstance(),
                new Parameter(new FullyQualifiedJavaType("java.util.Map<String,Object>"),
                        "params")
        );
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        method.setStatic(true);
        method.addBodyLine(topLevelClass.getType().getShortName() + " example = new " + topLevelClass.getType().getShortName() + "();");
        method.addBodyLine("return example.buildByUrlParams(params);");
        return method;
    }

    private Method generateBuildByUrlParamsMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "buildByUrlParams",
                false, JavaVisibility.PUBLIC,
                FullyQualifiedJavaType.getCriteriaInstance(),
                new Parameter(new FullyQualifiedJavaType("java.util.Map<String, Object>"), "params")
        );
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        method.setFinal(true);
        method.addBodyLine(
                " Criteria urlCriteria = this.createCriteria();\n" +
                        "        if (params == null || params.isEmpty()) {\n" +
                        "            return urlCriteria;\n" +
                        "        }\n" +
                        "        //校验url参数\n" +
                        "        for (java.util.Map.Entry<String, Object> entry : params.entrySet()) {\n" +
                        "            String expression = entry.getKey();\n" +
                        "            String value = entry.getValue().toString();\n" +
                        "\n" +
                        "            //防止sql注入\n" +
                        "            // 防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）\n" +
                        "            SQLFilter.sqlInject(expression);\n" +
                        "            SQLFilter.sqlInject(value);\n" +
                        "        }\n" +
                        "\n" +
                        "        //构建page参数\n" +
                        "        for (java.util.Map.Entry<String, Object> entry : params.entrySet()) {\n" +
                        "            String expression = entry.getKey();\n" +
                        "            String value = entry.getValue().toString();\n" +
                        "            String lowExp = expression.toLowerCase();\n" +
                        "            //构建自身的参数\n" +
                        "            if (\"page\".equals(lowExp)) {\n" +
                        "                this.page = safeParse(value, 1);\n" +
                        "                continue;\n" +
                        "            } else if (\"limit\".equals(lowExp)) {\n" +
                        "                this.pageSize = safeParse(value, 10);\n" +
                        "                continue;\n" +
                        "            }\n" +
                        "        }\n" +
                        "        //构建example的参数\n" +
                        "        //检测所有参数,组建query\n" +
                        "        java.util.Iterator entries = params.entrySet().iterator();\n" +
                        "        java.util.List<String> orderExps = null;\n" +
                        "        while (entries.hasNext()) {\n" +
                        "            java.util.Map.Entry entry = (java.util.Map.Entry) entries.next();\n" +
                        "            String expression = entry.getKey().toString();\n" +
                        "            String value = entry.getValue().toString();\n" +
                        "            //从默认的数据中构造 where 和 order\n" +
                        "            if (java.util.regex.Pattern.matches(\"w[a-z]{3}_[a-zA-Z]+\", expression)) {\n" +
                        "                buildWhereExpression(urlCriteria, expression, value);\n" +
                        "            } else if (java.util.regex.Pattern.matches(\"o[a-z]_[$_a-zA-Z]+\", expression)) {\n" +
                        "                if (orderExps == null) {\n" +
                        "                    orderExps = new java.util.ArrayList<>();\n" +
                        "                }\n" +
                        "                orderExps.add(expression);\n" +
                        "            }\n" +
                        "        }\n" +
                        "        if (orderExps != null) {\n" +
                        "            buildOrderExpression(orderExps);\n" +
                        "        }\n" +
                        "\n" +
                        "        return urlCriteria;"
        );

        return method;
    }

    private Method generateBuildWhereExpression(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "buildWhereExpression",
                false, JavaVisibility.PRIVATE,
                null,
                new Parameter(FullyQualifiedJavaType.getCriteriaInstance(), "urlCriteria"),
                new Parameter(new FullyQualifiedJavaType("java.lang.String"), "expression"),
                new Parameter(new FullyQualifiedJavaType("java.lang.Object"), "value")
        );
        return JavaElementGeneratorTools.generateMethodBody(
                method,
                "if (expression == null || \"\" == expression) {\n" +
                        "            return;\n" +
                        "        }\n" +
                        "\n" +
                        "        String compPrefix = expression.substring(2, 4);\n" +
                        "        String propName = SQLFilter.escape(expression.substring(5));\n" +
                        "        String columnName = " +
//                        introspectedTable..getAliasedFullyQualifiedTableNameAtRuntime()
                        "parse(propName);\n" +
                        "        if(columnName == null){\n" +
                        "            throw new java.lang.IllegalArgumentException(\"\\\"\" + propName + \"\\\" not exist\");\n" +
                        "        }\n" +
                        "        switch (compPrefix) {\n" +
                        "            case \"eq\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" =\", value, propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"ne\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" <>\", value, propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"gt\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" >\", value, propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"ge\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" >=\", value, propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"lt\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" <\", value, propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"le\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" <=\", value, propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"lk\":\n" +
                        "                if (value != null) {\n" +
                        "                    urlCriteria.addCriterion(columnName + \" like\", \"%\" + value + \"%\", propName);\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"in\":\n" +
                        "                if (value instanceof String) {\n" +
                        "                    String v = (String) value;\n" +
                        "                    if (v != null && !\"\".equals(v)) {\n" +
                        "                        String[] split = v.split(\",\");\n" +
                        "                        if (split != null && split.length > 0) {\n" +
                        "                            ArrayList<String> strings = new ArrayList<>();\n" +
                        "                            for (String s : split) {\n" +
                        "                                strings.add(s);\n" +
                        "                            }\n" +
                        "                            urlCriteria.addCriterion(columnName + \" in\", strings, propName);\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "                }\n" +
                        "                break;\n" +
                        "            case \"ep\":\n" +
                        "                urlCriteria.addCriterion(columnName + \" =\", \"\", propName);\n" +
                        "                break;\n" +
                        "            case \"np\":\n" +
                        "                urlCriteria.addCriterion(columnName + \" <>\", \"\", propName);\n" +
                        "                break;\n" +
                        "            case \"eu\":\n" +
                        "                urlCriteria.addCriterion(columnName + \" is null\");\n" +
                        "                break;\n" +
                        "            case \"nu\":\n" +
                        "                urlCriteria.addCriterion(columnName + \" is not null\");\n" +
                        "                break;\n" +
                        "            default:\n" +
                        "                break;\n" +
                        "        }"
        );
    }

    private Method generateBuildOrderExpressionMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "buildOrderExpression",
                false, JavaVisibility.PRIVATE,
                null,
                new Parameter(new FullyQualifiedJavaType("java.util.List<java.lang.String>"), "orderMaps")
        );
        return JavaElementGeneratorTools.generateMethodBody(
                method,
                "if (orderMaps == null || orderMaps.isEmpty()) {\n" +
                        "            return;\n" +
                        "        }\n" +
                        "        int size = orderMaps.size();\n" +
                        "        java.lang.String[] orderByCause = new String[size];\n" +
                        "        for (int i = 0; i < size; i++) {\n" +
                        "            java.lang.String expression  = orderMaps.get(i);\n" +
                        "            if (expression.startsWith(\"od_\") || expression.startsWith(\"oa_\")) {\n" +
                        "                String orderPrefix = expression.substring(1, 2);\n" +
                        "                String propName = SQLFilter.escape(expression.substring(3));\n" +
                        "                String columnName = parse(propName);\n" +
                        "                if(columnName == null){\n" +
                        "                   throw new java.lang.IllegalArgumentException(\"\\\"\" + propName + \"\\\" not exist\");\n" +
                        "                }\n" +
                        "                switch (orderPrefix) {\n" +
                        "                    case \"a\":\n" +
                        "                        orderByCause[i] = columnName + \" asc\";\n" +
                        "                        break;\n" +
                        "                    case \"d\":\n" +
                        "                        orderByCause[i] = columnName + \" desc\";\n" +
                        "                        break;\n" +
                        "                    default:\n" +
                        "                        break;\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        this.orderBy(orderByCause);"
        );
    }

    private Method generateParseMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "parse",
                false, JavaVisibility.PRIVATE,
                new FullyQualifiedJavaType("java.lang.String"),
                new Parameter(new FullyQualifiedJavaType("java.lang.String"), "column")
        );
        method.setStatic(true);
        FullyQualifiedJavaType columnParamType = new FullyQualifiedJavaType(introspectedTable.getRules().calculateAllFieldsClass().getShortName() + "." + ModelColumnPlugin.ENUM_NAME);
        String dtoColumnEnumFullName = columnParamType.getFullyQualifiedName();
        return JavaElementGeneratorTools.generateMethodBody(
                method,
                "if (column != null) {\n" +
                        "            for (" + dtoColumnEnumFullName + " value : " + dtoColumnEnumFullName + ".values()) {\n" +
                        "                if (column.equals(value.getJavaProperty())){\n" +
                        "                    return value.getValue();\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "        return null;"
        );
    }

    private void addPageField(TopLevelClass topLevelClass) {
        topLevelClass.addField(JavaElementGeneratorTools.generateField("page", JavaVisibility.PUBLIC, new FullyQualifiedJavaType("int"), "1"));
        topLevelClass.addField(JavaElementGeneratorTools.generateField("pageSize", JavaVisibility.PUBLIC, new FullyQualifiedJavaType("int"), "10"));
    }

    private InnerClass generateSqlFilterClass() {
        InnerClass sqlFilterClazz = new InnerClass("SQLFilter");
        sqlFilterClazz.setStatic(true);
        sqlFilterClazz.addField(JavaElementGeneratorTools.generateStaticFinalField("ESCAPE_START_STR", new FullyQualifiedJavaType("java.lang.String"), "\"$\""));
        sqlFilterClazz.addField(JavaElementGeneratorTools.generateStaticFinalField("ESCAPE_END_STR", new FullyQualifiedJavaType("java.lang.String"), "\"_\""));
        sqlFilterClazz.addField(JavaElementGeneratorTools.generateStaticFinalField("KEYWORDS", new FullyQualifiedJavaType("java.util.Map<String, String>"), null));

        InitializationBlock keyWordsInit = new InitializationBlock(true);
        keyWordsInit.addBodyLine("KEYWORDS = new java.util.HashMap<String, String>() {{\n" +
                "            put(\"mastor\", \"master\");\n" +
                "            put(\"clr\", \"truncate\");\n" +
                "            put(\"ins\", \"insert\");\n" +
                "            put(\"sel\", \"select\");\n" +
                "            put(\"del\", \"delete\");\n" +
                "            put(\"upt\", \"update\");\n" +
                "            put(\"def\", \"declare\");\n" +
                "            put(\"alt\", \"alert\");\n" +
                "            put(\"dpt\", \"drop\");\n" +
                "        }};");
        sqlFilterClazz.addInitializationBlock(keyWordsInit);


        //增加 sqlInject 方法
        Method sqlInjectMethod = JavaElementGeneratorTools.generateMethod(
                "sqlInject",
                false, JavaVisibility.PROTECTED,
                new FullyQualifiedJavaType("java.lang.String"),
                new Parameter(new FullyQualifiedJavaType("java.lang.String"), "str")
        );
        sqlInjectMethod.setStatic(true);
        FormatTools.addMethodWithBestPosition(sqlFilterClazz,
                JavaElementGeneratorTools.generateMethodBody(
                        sqlInjectMethod,
                        "if (isBlank(str)) {\n" +
                                "            return null;\n" +
                                "        }\n" +
                                "        //去掉'|\"|;|\\字符\n" +
                                "        str = str.replace(\"'\", \"\");\n" +
                                "        str = str.replace(\"\\\"\", \"\");\n" +
                                "        str = str.replace(\";\", \"\");\n" +
                                "        str = str.replace(\"\\\\\", \"\");\n" +
                                "        //转换成小写\n" +
                                "        str = str.toLowerCase();\n" +
                                "        //判断是否包含非法字符\n" +
                                "        java.util.Set<java.util.Map.Entry<String, String>> entries = KEYWORDS.entrySet();\n" +
                                "        for (java.util.Map.Entry<String, String> entry : entries) {\n" +
                                "            String keyword = entry.getValue();\n" +
                                "            if (str.contains(keyword)) {\n" +
                                "                throw new IllegalArgumentException(\"parameter \\\"\" + str + \"\\\"contains illegal characters:\\\"\" + keyword + \"\\\", please use escape characters:\\\"\" + ESCAPE_START_STR + entry.getKey() + ESCAPE_END_STR + \"\\\"\");\n" +
                                "            }\n" +
                                "        }\n" +
                                "\n" +
                                "        return str;"
                )

        );


        //增加 isBlank 判断方法
        Method isBlankMethod = JavaElementGeneratorTools.generateMethod(
                "isBlank",
                false, JavaVisibility.PRIVATE,
                new FullyQualifiedJavaType("boolean"),
                new Parameter(new FullyQualifiedJavaType("java.lang.String"), "str")
        );
        isBlankMethod.setStatic(true);
        FormatTools.addMethodWithBestPosition(sqlFilterClazz,
                JavaElementGeneratorTools.generateMethodBody(
                        isBlankMethod,
                        "int strLen;\n" +
                                "        if (str == null || (strLen = str.length()) == 0) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "        for (int i = 0; i < strLen; i++) {\n" +
                                "            if ((!Character.isWhitespace(str.charAt(i)))) {\n" +
                                "                return false;\n" +
                                "            }\n" +
                                "        }\n" +
                                "        return true;"
                ));


        //增加转义字符输出方法
        Method escapeMethod = JavaElementGeneratorTools.generateMethod(
                "escape",
                false, JavaVisibility.PRIVATE,
                new FullyQualifiedJavaType("java.lang.String"),
                new Parameter(new FullyQualifiedJavaType("java.lang.String"), "str")
        );
        escapeMethod.setStatic(true);
        FormatTools.addMethodWithBestPosition(sqlFilterClazz,
                JavaElementGeneratorTools.generateMethodBody(
                        escapeMethod,
                        "if (str == null || \"\".equals(str) || !str.contains(ESCAPE_START_STR) || !str.contains(ESCAPE_END_STR)) {\n" +
                                "            return str;\n" +
                                "        }\n" +
                                "        java.util.Set<java.util.Map.Entry<String, String>> entries = KEYWORDS.entrySet();\n" +
                                "        for (java.util.Map.Entry<String, String> entry : entries) {\n" +
                                "            String esWord = entry.getKey();\n" +
                                "            String realWord = entry.getValue();\n" +
                                "            str = str.replace(ESCAPE_START_STR + esWord + ESCAPE_END_STR, realWord);\n" +
                                "        }\n" +
                                "        return str;"
                ));


        return sqlFilterClazz;
    }

    private Method generatesafeParseMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method method = JavaElementGeneratorTools.generateMethod(
                "safeParse",
                false, JavaVisibility.PRIVATE,
                new FullyQualifiedJavaType("int"),
                new Parameter(new FullyQualifiedJavaType("java.lang.Object"), "obj"),
                new Parameter(new FullyQualifiedJavaType("int"), "defaultInt")
        );
        method.setStatic(true);
        return JavaElementGeneratorTools.generateMethodBody(
                method,
                "if (obj == null) {\n" +
                        "            return defaultInt;\n" +
                        "        }\n" +
                        "        return Integer.parseInt(obj.toString());"

        );
    }

}
