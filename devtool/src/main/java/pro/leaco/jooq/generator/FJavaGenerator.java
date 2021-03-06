package pro.leaco.jooq.generator;

import org.apache.commons.lang3.RegExUtils;
import org.jooq.*;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.impl.DSL;
import org.jooq.meta.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FJavaGenerator extends JavaGenerator {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FJavaGenerator.class);

    @Override
    protected void printRecordTypeMethod(JavaWriter out, Definition definition) {

        super.printRecordTypeMethod(out, definition);

        try {
            TableDefinition table = (TableDefinition) definition;

            //生成从url参数中构建的方法
            this.generateParseMethod(out, table);
            this.generaterConditionFormMethod(out, table);
            this.generateSortFormMethod(out, table);

            //生成内部辅助类
            ClassGenerateUtil.generateSqlFilterInnerClass(out);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    protected void generateDaoClassFooter(TableDefinition table, JavaWriter out) {
        super.generateDaoClassFooter(table, out);

        this.generateCreateQueryMethod(table, out);
        this.generateFetchPageMethod(table, out);
    }


    //=============================================================================================

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

    protected void generaterConditionFormMethod(JavaWriter out, TableDefinition table) {

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

        // method buildConditionByUrl
        out.tab(1).javadoc("从url参数中构建条件");
        out.tab(1).println("public %s buildCondition(%s<String, Object> params){", Condition.class, Map.class);
        out.tab(2).println("if (params == null || params.isEmpty()) {");
        out.tab(3).println("return %s.trueCondition();", DSL.class);
        out.tab(2).println("}");
        out.tab(2).println("SQLFilter.filter(params);\n" +
                "        java.util.Iterator entries = params.entrySet().iterator();\n" +
                "        Condition condition = null;\n" +
                "        while (entries.hasNext()) {\n" +
                "            %s.Entry entry = (%s.Entry) entries.next();\n" +
                "            String expression = entry.getKey().toString();\n" +
                "            String value = entry.getValue().toString();\n" +
                "            if (%s.matches(\"w[a-z]{3}_[a-zA-Z$_]+\", expression)) {\n" +
                "                condition = solveCondition(condition, expression, value);\n" +
                "            }\n" +
                "        }\n" +
                "        if(condition==null){\n" +
                "            return %s.trueCondition();\n" +
                "        } else {\n" +
                "            return condition;\n" +
                "        }\n" +
                "    }\n", Map.class, Map.class, Pattern.class, DSL.class
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
                "                if (value != null && !\"\".equals(value)) {\n" +
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

    protected void generateSortFormMethod(JavaWriter out, TableDefinition table) {

        out.println(
                "    private static int compareValue(Object a, Object b) {\n" +
                        "        if (a == null) {\n" +
                        "            return -1;\n" +
                        "        } else if (b == null) {\n" +
                        "            return 1;\n" +
                        "        } else {\n" +
                        "            int _a;\n" +
                        "            try {\n" +
                        "                _a = Integer.parseInt(a.toString());\n" +
                        "            } catch (Exception e) {\n" +
                        "                return -1;\n" +
                        "            }\n" +
                        "            int _b;\n" +
                        "            try {\n" +
                        "                _b = Integer.parseInt(b.toString());\n" +
                        "            } catch (Exception e) {\n" +
                        "                return 1;\n" +
                        "            }\n" +
                        "            return Integer.compare(_a, _b);\n" +
                        "        }\n" +
                        "    }");

        out.tab(1).javadoc("从url参数中构建排序");
        out.tab(1).println(" public SortField[] buildOrderBy(Map<String, Object> params, SortField... otherField) {\n" +
                "        return %s.concat(\n" +
                "                %s.ofNullable(otherField)\n" +
                "                        .map(%s::stream)\n" +
                "                        .orElse(Stream.empty())\n" +
                "                        .filter(Objects::nonNull),\n" +
                "                Arrays.stream(buildOrderBy(params))\n" +
                "        ).toArray(SortField[]::new);\n" +
                "    }", Stream.class, Optional.class, Arrays.class);

        out.tab(1).javadoc("从url参数中构建排序");
        out.tab(1).println("public %s[] buildOrderBy(Map<String, Object> params) {\n", SortField.class);
        out.tab(1).println(
                "        if (params == null || params.isEmpty()) {\n" +
                        "            return new SortField[0];\n" +
                        "        }\n" +
                        "        SQLFilter.filter(params);\n" +
                        "        return params.entrySet().stream()\n" +
                        "                .filter(x -> Pattern.matches(\"o(a|d)_[$_a-zA-Z]+\", x.getKey()))\n" +
                        "                .sorted((x, y) -> compareValue(x.getValue(), y.getValue()))\n" +
                        "                .map(entry -> {\n" +
                        "                    String expression = entry.getKey();\n" +
                        "                    String value = entry.getValue().toString();\n" +
                        "                    String orderPrefix = expression.substring(1, 2);\n" +
                        "                    String propName = SQLFilter.escape(expression.substring(3));\n" +
                        "                    Field field = parse(propName);\n" +
                        "                    switch (orderPrefix) {\n" +
                        "                        case \"a\":\n" +
                        "                            return field.sort(%s.ASC);\n" +
                        "                        case \"d\":\n" +
                        "                            return field.sort(%s.DESC);\n" +
                        "                        default:\n" +
                        "                            throw new %s();\n" +
                        "                    }\n" +
                        "                })\n" +
                        "                .filter(%s::nonNull)\n" +
                        "                .toArray(SortField[]::new);\n" +
                        "    }", SortOrder.class, SortOrder.class, AssertionError.class, Objects.class);

    }

    private void generateCreateQueryMethod(TableDefinition table, JavaWriter out) {
        final String recordClassName = out.ref(getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.RECORD));
        final String tableIdentifier = getStrategy().getFullJavaIdentifier(table);

        out.tab(1).println(" public %s<%s> createQuery() {\n" +
                "        return ctx().selectQuery(getTable());\n" +
                "    }", SelectQuery.class, recordClassName);

        out.tab(1).println("public SelectQuery<%s> createQuery(%s<String, Object> params) {\n" +
                "        SelectQuery<%s> query = createQuery();\n" +
                "        query.addConditions(%s.buildCondition(params));\n" +
                "        query.addOrderBy(%s.buildOrderBy(params));\n" +
                "        return query;\n" +
                "    }", recordClassName, Map.class, recordClassName, tableIdentifier, tableIdentifier);

        out.tab(1).println("public SelectQuery<%s> createQuery(%s<String, Object> params, %s... fields) {\n" +
                "        SelectQuery<%s> query = createQuery(params);\n" +
                "        query.addSelect(fields);\n" +
                "        return query;\n" +
                "    }", recordClassName, Map.class, SelectFieldOrAsterisk.class, recordClassName);


    }

    private void generateFetchPageMethod(TableDefinition table, JavaWriter out) {

        final String pType = out.ref(getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.POJO));
        final String recordClassName = out.ref(getStrategy().getFullJavaClassName(table, GeneratorStrategy.Mode.RECORD));

        out.tab(1).javadoc("查询分页");
        out.tab(1).println("public com.github.pagehelper.Page<%s> fetchPage(Map<String, Object> params) {\n" +
                "        SelectQuery<%s> query = createQuery(params);\n" +
                "        return fetchPage(query, params);\n" +
                "    }", pType, recordClassName);

        out.tab(1).javadoc("查询分页");
        out.tab(1).println("public com.github.pagehelper.Page<%s> fetchPage(SelectQuery<?> query, Map<String, Object> params) {\n" +
                "        int page = (int) %s.ofNullable(params.get(\"page\")).map(Object::toString).map(Integer::parseInt).orElse(1);\n" +
                "        int limit = (int) Optional.ofNullable(java.util.Optional.ofNullable(params.get(\"limit\")).orElse(params.get(\"pageSize\"))).map(Object::toString).map(Integer::parseInt).orElse(10);\n" +
                "        return fetchPage(query, page, limit);\n" +
                "    }", pType, Optional.class);

        out.tab(1).javadoc("查询分页");
        out.tab(1).println("public com.github.pagehelper.Page<%s> fetchPage(SelectQuery<?> query, int pageNum, int pageSize) {\n" +
                "        com.github.pagehelper.Page<%s> page = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, true, true, false);\n" +
                "        int total = ctx().fetchCount(query);\n" +
                "        page.setTotal(total);\n" +
                "        query.addLimit(page.getStartRow(), page.getPageSize());\n" +
                "        List<%s> list = ctx().fetch(query).into(this.getType());\n" +
                "        page.clear();\n" +
                "        page.addAll(list);\n" +
                "        return page;\n" +
                "    }", pType, pType, pType);


//        out.tab(1).javadoc("查询分页");
//        out.tab(1).println("public com.github.pagehelper.Page<%s> fetchPage(%s<?> selectLimitStep,int pageNum, int pageSize) {\n" +
//                "        com.github.pagehelper.Page<%s> page = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, true, true, false);\n" +
//                "        int total = ctx().fetchCount(selectLimitStep);\n" +
//                "        page.setTotal(total);\n" +
//                "        String pageSql = selectLimitStep.getSQL(%s.INLINED) + \" limit ?,?\";\n" +
//                "        List<%s> list =\n" +
//                "                ctx().fetch(pageSql, page.getStartRow(), page.getPageSize()).into(this.getType());\n" +
//                "        page.clear();\n" +
//                "        page.addAll(list);\n" +
//                "        return page;\n" +
//                "    }", pType, SelectLimitStep.class, pType, ParamType.class, pType);


        out.tab(1).javadoc("查询分页");
        out.tab(1).println("public <M> com.github.pagehelper.Page<M> fetchPage(SelectQuery<?> query, Map<String, Object> params, %s<M> clazz) {\n" +
                "        int page = (int) %s.ofNullable(params.get(\"page\")).map(Object::toString).map(Integer::parseInt).orElse(1);\n" +
                "        int limit = (int) Optional.ofNullable(java.util.Optional.ofNullable(params.get(\"limit\")).orElse(params.get(\"pageSize\"))).map(Object::toString).map(Integer::parseInt).orElse(10);\n" +
                "        return fetchPage(query, page, limit, clazz);\n" +
                "    }", Class.class, Optional.class);

        out.tab(1).javadoc("查询分页");
        out.tab(1).println("public <M> com.github.pagehelper.Page<M> fetchPage(SelectQuery<?> query, int pageNum, int pageSize, %s<M> clazz) {\n" +
                "        com.github.pagehelper.Page<M> page = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, true, true, false);\n" +
                "        int total = ctx().fetchCount(query);\n" +
                "        page.setTotal(total);\n" +
                "        query.addLimit(page.getStartRow(), page.getPageSize());\n" +
                "        List<M> list = fetchList(query, clazz);\n" +
                "        page.clear();\n" +
                "        page.addAll(list);\n" +
                "        return page;\n" +
                "    }", Class.class);

        //=============================================

        out.tab(1).javadoc("查询列表");
        out.tab(1).println("public <M> List<M> fetchList(ResultQuery<?> query, %s<M> clazz) {\n" +
                "        return fetchList(query).into(clazz);\n" +
                "    }", Class.class);

        out.tab(1).javadoc("查询列表");
        out.tab(1).println("public <R extends %s> %s<R> fetchList(%s<R> query) {\n" +
                "        return ctx().fetch(query);\n" +
                "    }", Record.class, Result.class, ResultQuery.class);
    }
}
