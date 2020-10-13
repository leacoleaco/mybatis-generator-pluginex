package com.fuyo.cloud.db.biz.jooq.util;

import org.jooq.AggregateFunction;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DSL辅助工具
 */
public class DSLEx {

    public static AggregateFunction<Integer> count1() {
        return DSL.count(DSL.field("1", Integer.class));
    }

    /**
     * 计算出对应日期所在周的第一天
     * 等价于 mysql语句：
     * date_add(`date`, interval -((dayofweek(`date`) - 1)) day)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDateTime> firstDayOfWeekWithTime(Field<LocalDateTime> date) {
        return DSL.localDateTimeSub(date, DSL.dayOfWeek(date).minus(1), DatePart.DAY);
    }

    /**
     * 计算出对应日期所在周的第一天
     * 等价于 mysql语句：
     * date_add(`date`, interval -((dayofweek(`date`) - 1)) day)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> firstDayOfWeek(Field<LocalDate> date) {
        return DSL.localDateSub(date, DSL.dayOfWeek(date).minus(1), DatePart.DAY);
    }

    /**
     * 计算出对应日期所在周的第一天
     * 等价于 mysql语句：
     * cast(date_add(`date`, interval -((dayofweek(`date`) - 1)) day) as date)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> castToFirstDayOfWeek(Field<LocalDateTime> date) {
        return firstDayOfWeekWithTime(date).cast(LocalDate.class);
    }

}
