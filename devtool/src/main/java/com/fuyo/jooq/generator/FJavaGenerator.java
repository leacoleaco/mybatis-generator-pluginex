package com.fuyo.jooq.generator;

import org.apache.commons.lang3.RegExUtils;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.impl.CustomCondition;
import org.jooq.meta.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class FJavaGenerator extends JavaGenerator {
    private static final Logger log = LoggerFactory.getLogger(FJavaGenerator.class);

    @Override
    protected void generateTable(SchemaDefinition schema, TableDefinition table) {

        //生成 table.java
        super.generateTable(schema, table);

        //生成 TableQueryBuilder.java
//        this.generateTableQueryBuilder(schema, table);

    }

    protected void generateTableQueryBuilder(SchemaDefinition schema, TableDefinition table) {
        String dir = getTargetDirectory();
        String javaPackageName = getStrategy().getJavaPackageName(table) + ".builders";

        String pkg = javaPackageName.replaceAll("\\.", "/");
        File file = new File(dir + "/" + pkg, getStrategy().getJavaClassName(table, GeneratorStrategy.Mode.DEFAULT) + "QueryBuilder.java");
        JavaWriter out = newJavaWriter(file);
        generateTable(table, out);
        closeJavaWriter(out);
    }

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
        out.tab(2).println("throw new %s(\"can not found property\" + property);", IllegalArgumentException.class);
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

        // enum opera
        out.tab(1).println("protected enum Opera {\n" +
                           "        AND(\"a\", (a, b) -> a.and(b)),\n" +
                           "        OR(\"o\", (a, b) -> a.or(b)),\n" +
                           "        ;\n" +
                           "\n" +
                           "        private final String code;\n" +
                           "        private final %s<Condition, Condition, Condition> action;\n" +
                           "\n" +
                           "        Opera(String code, BiFunction<Condition, Condition, Condition> action) {\n" +
                           "            this.code = code;\n" +
                           "            this.action = action;\n" +
                           "        }\n" +
                           "\n" +
                           "        protected final Condition operate(Condition a, Condition b) {\n" +
                           "            if (a == null) {\n" +
                           "                return b;\n" +
                           "            } else if (b == null) {\n" +
                           "                return a;\n" +
                           "            }\n" +
                           "            return action.apply(a, b);\n" +
                           "        }\n" +
                           "\n" +
                           "        protected static Opera parse(String code) {\n" +
                           "            if (code == null) {\n" +
                           "                throw new IllegalArgumentException(\"opera code error\");\n" +
                           "            }\n" +
                           "            String _code = code.toLowerCase();\n" +
                           "            for (Opera value : values()) {\n" +
                           "                if (value.code.equals(_code)) {\n" +
                           "                    return value;\n" +
                           "                }\n" +
                           "            }\n" +
                           "            throw new IllegalArgumentException(\"opera code error,can not found operate:\" + code);\n" +
                           "        }\n" +
                           "    }", BiFunction.class);

        // method nothing
        out.tab(1).println("private %s emptyCondition() {\n" +
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
        out.tab(2).println("if (params == null || params.isEmpty()) {");
        out.tab(3).println("return emptyCondition();");
        out.tab(2).println("}");
        out.tab(2).println("SQLFilter.filter(params);\n" +
                           "        java.util.Iterator entries = params.entrySet().iterator();\n" +
                           "        Condition condition = null;\n" +
                           "        while (entries.hasNext()) {\n" +
                           "            java.util.Map.Entry entry = (java.util.Map.Entry) entries.next();\n" +
                           "            String expression = entry.getKey().toString();\n" +
                           "            String value = entry.getValue().toString();\n" +
                           "            if (java.util.regex.Pattern.matches(\"w[a-z]{3}_[a-zA-Z$_]+\", expression)) {\n" +
                           "                condition = solveCondition(condition, expression, value);\n" +
                           "            }\n" +
                           "        }\n" +
                           "        return condition;\n" +
                           "    }\n"
        );


        //method solveCondition
        out.tab(1).println("protected Condition solveCondition(Condition condition, String expression, String value) {\n" +
                           "        if (expression == null || \"\".equals(expression)) {\n" +
                           "            return condition;\n" +
                           "        }\n" +
                           "        String logicExp = expression.substring(1, 2);\n" +
                           "        Opera opera = Opera.parse(logicExp);\n" +
                           "        String compPrefix = expression.substring(2, 4);\n" +
                           "        String propName = SQLFilter.escape(expression.substring(5));\n" +
                           "        Field field = parse(propName);\n" +
                           "        switch (compPrefix) {\n" +
                           "            case \"eq\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.eq(value));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
                           "            case \"ne\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.ne(value));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
                           "            case \"gt\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.gt(value));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
                           "            case \"ge\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.ge(value));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
                           "            case \"lt\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.lt(value));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
                           "            case \"le\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.le(value));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
                           "            case \"lk\":\n" +
                           "                if (value != null) {\n" +
                           "                    return opera.operate(condition, field.like(\"%\" + value + \"%\"));\n" +
                           "                } else {\n" +
                           "                    return condition;\n" +
                           "                }\n" +
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
                           "                            return opera.operate(condition, field.in(strings));\n" +
                           "                        }\n" +
                           "                    }\n" +
                           "                }\n" +
                           "                return condition;\n" +
                           "            case \"ep\":\n" +
                           "                return opera.operate(condition, field.eq(\"\"));\n" +
                           "            case \"np\":\n" +
                           "                return opera.operate(condition, field.ne(\"\"));\n" +
                           "            case \"eu\":\n" +
                           "                return opera.operate(condition, field.isNull());\n" +
                           "            case \"nu\":\n" +
                           "                return opera.operate(condition, field.isNotNull());\n" +
                           "            default:\n" +
                           "                break;\n" +
                           "        }\n" +
                           "        throw new IllegalArgumentException(\"expression compare word error\");\n" +
                           "    }"
        );
    }
}