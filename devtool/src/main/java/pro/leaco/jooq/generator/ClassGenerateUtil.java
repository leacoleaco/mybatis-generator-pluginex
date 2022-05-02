package pro.leaco.jooq.generator;

import org.jooq.codegen.JavaWriter;

public class ClassGenerateUtil {

    public static void generateSqlFilterInnerClass(JavaWriter out) {

        out.tab(1).print("static class SQLFilter {\n" +
                         "        public static final String ESCAPE_START_STR = \"$\";\n" +
                         "        public static final String ESCAPE_END_STR = \"_\";\n" +
                         "        public static final java.util.Map<String, String> KEYWORDS;\n" +
                         "\n" +
                         "        static {\n" +
                         "            KEYWORDS = new java.util.HashMap<String, String>() {{\n" +
                         "                put(\"mastor\", \"master\");\n" +
                         "                put(\"clr\", \"truncate\");\n" +
                         "                put(\"ins\", \"insert\");\n" +
                         "                put(\"sel\", \"select\");\n" +
                         "                put(\"del\", \"delete\");\n" +
                         "                put(\"upt\", \"update\");\n" +
                         "                put(\"def\", \"declare\");\n" +
                         "                put(\"alt\", \"alert\");\n" +
                         "                put(\"dpt\", \"drop\");\n" +
                         "            }};\n" +
                         "        }\n" +
                         "\n" +
                         "        protected static String filterSqlInject(String str) {\n" +
                         "            if (isBlank(str)) {\n" +
                         "                return null;\n" +
                         "            }\n" +
                         "            //去掉'|\"|;|\\字符\n" +
                         "            str = str.replace(\"'\", \"\");\n" +
                         "            str = str.replace(\"\\\"\", \"\");\n" +
                         "            str = str.replace(\";\", \"\");\n" +
                         "            str = str.replace(\"\\\\\", \"\");\n" +
                         "            //转换成小写\n" +
                         "            str = str.toLowerCase();\n" +
                         "            //判断是否包含非法字符\n" +
                         "            java.util.Set<java.util.Map.Entry<String, String>> entries = KEYWORDS.entrySet();\n" +
                         "            for (java.util.Map.Entry<String, String> entry : entries) {\n" +
                         "                String keyword = entry.getValue();\n" +
                         "                if (str.contains(keyword)) {\n" +
                         "                    throw new IllegalArgumentException(\"parameter \\\"\" + str + \"\\\"contains illegal characters:\\\"\" + keyword + \"\\\", please use escape characters:\\\"\" + ESCAPE_START_STR + entry.getKey() + ESCAPE_END_STR + \"\\\"\");\n" +
                         "                }\n" +
                         "            }\n" +
                         "            return str;\n" +
                         "        }\n" +
                         "\n" +
                         "        private static boolean isBlank(String str) {\n" +
                         "            int strLen;\n" +
                         "            if (str == null || (strLen = str.length()) == 0) {\n" +
                         "                return true;\n" +
                         "            }\n" +
                         "            for (int i = 0; i < strLen; i++) {\n" +
                         "                if ((!Character.isWhitespace(str.charAt(i)))) {\n" +
                         "                    return false;\n" +
                         "                }\n" +
                         "            }\n" +
                         "            return true;\n" +
                         "        }\n" +
                         "\n" +
                         "        private static String escape(String str) {\n" +
                         "            if (str == null || \"\".equals(str) || !str.contains(ESCAPE_START_STR) || !str.contains(ESCAPE_END_STR)) {\n" +
                         "                return str;\n" +
                         "            }\n" +
                         "            java.util.Set<java.util.Map.Entry<String, String>> entries = KEYWORDS.entrySet();\n" +
                         "            for (java.util.Map.Entry<String, String> entry : entries) {\n" +
                         "                String esWord = entry.getKey();\n" +
                         "                String realWord = entry.getValue();\n" +
                         "                str = str.replace(ESCAPE_START_STR + esWord + ESCAPE_END_STR, realWord);\n" +
                         "            }\n" +
                         "            return str;\n" +
                         "        }\n" +
                         "\n" +
                         "        protected static void filter(java.util.Map<String, Object> params) {\n" +
                         "            //校验url参数\n" +
                         "            for (java.util.Map.Entry<String, Object> entry : params.entrySet()) {\n" +
                         "                String expression = entry.getKey();\n" +
                         "                String value = entry.getValue().toString();\n" +
                         "                //防止sql注入\n" +
                         "                // 防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）\n" +
                         "                filterSqlInject(expression);\n" +
                         "                filterSqlInject(value);\n" +
                         "            }\n" +
                         "        }\n" +
                         "    }");
    }

}
