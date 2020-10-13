package com.fuyo.cloud.db.biz.jooq.util;

import org.jooq.AggregateFunction;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DSL辅助工具
 */
public class DSLEx {

    public static AggregateFunction<Integer> count1() {
        return DSL.count(DSL.field("1", Integer.class));
    }

    public static Field<String> dateFormat(Field<?> field, String format) {
        return DSL.field("date_format({0}, {1})", SQLDataType.VARCHAR, field, DSL.inline(format));
    }

    public static Field<LocalDate> lastDayOfLocalDate(Field<LocalDate> field) {
        return DSL.field("LAST_DAY({0})", SQLDataType.LOCALDATE, field);
    }

    public static Field<LocalDate> lastDayOfLocalDateTime(Field<LocalDateTime> field) {
        return DSL.field("LAST_DAY({0})", SQLDataType.LOCALDATE, field);
    }

    //=================================================================

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
     * cast(date_add(`date`, interval -((extract(day from `date`) - 1)) day) as date)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> castToFirstDayOfWeek(Field<LocalDateTime> date) {
        return firstDayOfWeekWithTime(date).cast(LocalDate.class);
    }

    //=================================================================

    /**
     * 计算出对应日期所在月份的第一天
     * 等价于 mysql语句：
     * date_add(`date`, interval -((extract(day from `date`) - 1)) day)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> firstDayOfMonth(Field<LocalDate> date) {
        return DSL.localDateSub(date, DSL.day(date).minus(1), DatePart.DAY);
    }

    /**
     * 计算出对应日期所在月份的第一天
     * 等价于 mysql语句：
     * date_add(`date`, interval -((extract(day from `date`) - 1)) day)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDateTime> firstDayOfMonthWithTime(Field<LocalDateTime> date) {
        return DSL.localDateTimeSub(date, DSL.day(date).minus(1), DatePart.DAY);
    }

    /**
     * 计算出对应日期所在月份的第一天
     * 等价于 mysql语句：
     * cast(date_add(`date`, interval -((extract(day from `date`) - 1)) day) as date)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> castToFirstDayOfMonth(Field<LocalDateTime> date) {
        return firstDayOfMonthWithTime(date).cast(LocalDate.class);
    }

    //=================================================================

    /**
     * 计算出对应日期所在季度的第一天
     * 等价于 mysql语句：
     * concat(date_format(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM `date`),1) + interval QUARTER(`date`)*3-3 month),’%Y-%m-’),‘01’)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> firstDayOfQuarter(Field<LocalDateTime> date) {
        return DSL.field(" concat(date_format(LAST_DAY(MAKEDATE(EXTRACT(YEAR FROM {0}),1) + interval QUARTER({0})*3-3 month),'%Y-%m-'),'01')",
                LocalDate.class, date);
    }

    //=================================================================

    /**
     * 计算出对应日期所在年的第一天
     * 等价于 mysql语句：
     * date_add(`date`, interval -((dayofyear(`date`) - 1)) day)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> firstDayOfYear(Field<LocalDate> date) {
        return DSL.localDateSub(date, DSL.dayOfYear(date).minus(1), DatePart.DAY);
    }

    /**
     * 计算出对应日期所在年的第一天
     * 等价于 mysql语句：
     * date_add(`date`, interval -((dayofyear(`date`) - 1)) day)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDateTime> firstDayOfYearWithTime(Field<LocalDateTime> date) {
        return DSL.localDateTimeSub(date, DSL.dayOfYear(date).minus(1), DatePart.DAY);
    }

    /**
     * 计算出对应日期所在年的第一天
     * 等价于 mysql语句：
     * cast(date_add(`date`, interval -((dayofyear(`date`) - 1)) day) as date)
     *
     * @return 周第一天的表达式
     */
    public static Field<LocalDate> castToFirstDayOfYear(Field<LocalDateTime> date) {
        return firstDayOfYearWithTime(date).cast(LocalDate.class);
    }
}
