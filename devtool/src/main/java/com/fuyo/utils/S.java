package com.fuyo.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * 字符串工具
 */
public class S {

    public static class Param<V> extends HashMap<String, V> {
        public Param<V> set(String key, V value) {
            put(key, value);
            return this;
        }
    }

    public static <V> String format(String pattern, Consumer<Param<V>> params) {
        Param<V> map = new Param<>();
        params.accept(map);
        return StringSubstitutor.replace(pattern, map);
    }

    public static void main(String[] args) {
        String format = format("${name}(\"${description}\")${s}", p -> {
            p.set("name", "test");
            p.set("s", "xxx");
        });
        System.out.println(format);

    }

}
