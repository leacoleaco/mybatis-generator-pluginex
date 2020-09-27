package com.fuyo.jooq.generator;

import com.fuyo.utils.S;
import org.apache.commons.lang3.RegExUtils;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.TableField;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.impl.CustomCondition;
import org.jooq.impl.DSL;
import org.jooq.meta.*;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FJavaGenerator extends JavaGenerator {
    private static final String ENUMS_SCHEMA = "enums";

    private static final Logger log = LoggerFactory.getLogger(FJavaGenerator.class);


//    @Override
//    protected void generateSchema(SchemaDefinition schema) {
//        // Apply custom logic only for `enums` schema. Others schema has regular generation
////        if (!schema.getName().equals(ENUMS_SCHEMA)) {
////            super.generateSchema(schema);
////            return;
////        }
//
//        log.info("Generating column enums");
//        log.info("----------------------------------------------------------");
//
//        Database db = schema.getDatabase();
//
//        db.getTables(schema).forEach(
//                (table) -> {
//                    // Prepare enum name from snake_case to CamelCase
//                    String enumName = table.getName();
//
//                    JavaWriter writer = newJavaWriter(new File(getFile(schema).getParentFile(), enumName + ".java"));
//                    log.info("Generating enum: {}.java [input={}, output={}]", enumName, table.getName(), enumName);
//
//                    printPackage(writer, schema);
//
//                    writer.println("public enum $enumName {");
//
//                    try {
//
//                        ResultSet rs = db.getConnection().prepareStatement(
//                                S.format("SELECT * FROM ${schema}.\"${tableName}\"",
//                                        p -> p.set("schema", schema)
//                                                .set("tableName", table.getName())
//                                )).executeQuery();
//
//                        while (true) {
//                            if (!rs.next()) break;
//
//                            // Generate enum entry
//                            String name = rs.getString("name");
//                            String description = rs.getString("description");
//                            String s = rs.isLast() ? ";" : ",";
//                            writer.tab(1).println(S.format("${name}(\"${description}\")${s}",
//                                    p -> p.set("name", name)
//                                            .set("description", description)
//                                            .set("s", s)));
//                        }
//
//                        writer.println("|    private final String description;\n" +
//                                       "|\n" +
//                                       "|    private " + enumName + "(String description) {\n" +
//                                       "|        this.description = description;\n" +
//                                       "|    }\n" +
//                                       "|}\n");
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    } finally {
//                        closeJavaWriter(writer);
//                    }
//                }
//        );
//
//        log.info("----------------------------------------------------------");
//        super.generateSchema(schema);
//    }


    @Override
    protected void printRecordTypeMethod(JavaWriter out, Definition definition) {

        TableDefinition table = (TableDefinition) definition;


        //生成从url参数中构建的方法
        this.generateBuildWithParamMethod(out, table);

        this.generateParseMethod(out, table);
        this.generaterBuildConditionByUrl(out, table);


        super.printRecordTypeMethod(out, definition);

        //生成内部辅助类
        ClassGenerateUtil.generateSqlFilterInnerClass(out);

    }

    protected void generateBuildWithParamMethod(JavaWriter out, TableDefinition table) {
        out.tab(1).javadoc("从url参数中构建条件");
        out.tab(1).println("public %s buildWithUrlParam(java.util.Map<String, Object> params){", Condition.class);
        out.tab(2).println("return null;");
        out.tab(1).println("}");
    }

    protected void generateParseMethod(JavaWriter out, TableDefinition table) {
        out.tab(1).javadoc("通过字符串解析列属性");
        out.tab(1).println("public %s parse(%s property) {", Field.class, String.class);
        out.tab(2).println("if (property == null) {\n" +
                           "            throw new %s(\"property can not be  null\");\n" +
                           "        }", IllegalArgumentException.class);
        out.tab(2).println("String _p = property.replaceAll(\"_\", \"\").toLowerCase();");
        out.tab(2).println("switch (_p) {");

        for (ColumnDefinition column : table.getColumns()) {
//            final String columnId = out.ref(getStrategy().getJavaIdentifier(column), colRefSegments(column));
            final String columnId = getStrategy().getJavaIdentifier(column);
            final String columnName = column.getName();
            out.tab(3).println("case \"%s\":", escape(columnName));
            out.tab(4).println("return this.%s;", columnId);
        }
        out.tab(3).println("default: break;");
        out.tab(2).println("}");
        out.tab(2).println("throw new %s(\"can not found property\");", IllegalArgumentException.class);
        out.tab(1).println("}");
    }

    private String escape(String column) {
        String res = RegExUtils.replaceAll(column, "_", "");
        return org.apache.commons.lang3.StringUtils.lowerCase(res);
    }

    private String ref(String clazzOrId, int keepSegments) {
        return clazzOrId == null ? null : ref(Arrays.asList(clazzOrId), keepSegments).get(0);
    }

    private List<String> ref(List<String> clazzOrId, int keepSegments) {
        return clazzOrId == null ? Collections.<String>emptyList() : clazzOrId;
    }

    private int colRefSegments(TypedElementDefinition<?> column) {
        if (column != null && column.getContainer() instanceof UDTDefinition)
            return 2;
        if (!getStrategy().getInstanceFields())
            return 2;
        return 3;
    }

    protected void generaterBuildConditionByUrl(JavaWriter out, TableDefinition table) {

        // method nothing
        out.tab(1).println("private %s condition() {\n" +
                           "        return new %s() {\n" +
                           "            @Override\n" +
                           "            public void accept(%s<?> ctx) {\n" +
                           "                ctx.sql(\"1 = 1\");\n" +
                           "            }\n" +
                           "        };\n" +
                           "    }", Condition.class, CustomCondition.class, Context.class);


        // method buildConditionByUrl
        out.tab(1).javadoc("从url参数中构建条件");
        out.tab(1).println("public %s buildConditionByUrl(java.util.Map<String, Object> params){", Condition.class);
        out.tab(2).println("Condition condition = condition();");
        out.tab(2).println("if (params == null) {");
        out.tab(3).println("return condition;");
        out.tab(2).println("}");
        out.tab(2).println("SQLFilter.filter(params);\n" +
                           "        java.util.Iterator entries = params.entrySet().iterator();\n" +
                           "        while (entries.hasNext()) {\n" +
                           "            java.util.Map.Entry entry = (java.util.Map.Entry) entries.next();\n" +
                           "            String expression = entry.getKey().toString();\n" +
                           "            String value = entry.getValue().toString();\n" +
                           "            if (java.util.regex.Pattern.matches(\"w[a-z]{3}_[a-zA-Z]+\", expression)) {\n" +
                           "                solveCondition(condition, expression, value);\n" +
                           "            }\n" +
                           "        }\n" +
                           "        return condition;\n" +
                           "    }\n"
        );


        //method solveCondition
        out.tab(1).println("private void solveCondition(Condition condition, String expression, String value) {\n" +
                           "        if (expression == null || \"\" == expression) {\n" +
                           "            return;\n" +
                           "        }\n" +
                           "        String logicExp = expression.substring(1, 2);\n" +
                           "        boolean isAnd = \"a\".equalsIgnoreCase(logicExp);\n" +
                           "        String compPrefix = expression.substring(2, 4);\n" +
                           "        String propName = SQLFilter.escape(expression.substring(5));\n" +
                           "        Field field = parse(propName);\n" +
                           "        switch (compPrefix) {\n" +
                           "            case \"eq\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.eq(value));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.eq(value));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"ne\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.ne(value));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.ne(value));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"gt\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.gt(value));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.gt(value));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"ge\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.ge(value));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.ge(value));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"lt\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.lt(value));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.lt(value));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"le\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.le(value));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.le(value));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"lk\":\n" +
                           "                if (value != null) {\n" +
                           "                    if (isAnd) {\n" +
                           "                        condition.and(field.like(\"%\" + value + \"%\"));\n" +
                           "                    } else {\n" +
                           "                        condition.or(field.like(\"%\" + value + \"%\"));\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"in\":\n" +
                           "                if (value instanceof String) {\n" +
                           "                    String v = (String) value;\n" +
                           "                    if (v != null && !\"\".equals(v)) {\n" +
                           "                        String[] split = v.split(\",\");\n" +
                           "                        if (split != null && split.length > 0) {\n" +
                           "                            java.util.ArrayList<String> strings = new java.util.ArrayList<>();\n" +
                           "                            for (String s : split) {\n" +
                           "                                strings.add(s);\n" +
                           "                            }\n" +
                           "                            if (isAnd) {\n" +
                           "                                condition.and(field.in(strings));\n" +
                           "                            } else {\n" +
                           "                                condition.or(field.in(strings));\n" +
                           "                            }\n" +
                           "                        }\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"ep\":\n" +
                           "                if (isAnd) {\n" +
                           "                    condition.and(field.eq(\"\"));\n" +
                           "                } else {\n" +
                           "                    condition.or(field.eq(\"\"));\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"np\":\n" +
                           "                if (isAnd) {\n" +
                           "                    condition.and(field.ne(\"\"));\n" +
                           "                } else {\n" +
                           "                    condition.or(field.ne(\"\"));\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"eu\":\n" +
                           "                if (isAnd) {\n" +
                           "                    condition.and(field.isNull());\n" +
                           "                } else {\n" +
                           "                    condition.or(field.isNull());\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            case \"nu\":\n" +
                           "                if (isAnd) {\n" +
                           "                    condition.and(field.isNotNull());\n" +
                           "                } else {\n" +
                           "                    condition.or(field.isNotNull());\n" +
                           "                }\n" +
                           "                break;\n" +
                           "            default:\n" +
                           "                break;\n" +
                           "        }\n" +
                           "    }");
    }
}