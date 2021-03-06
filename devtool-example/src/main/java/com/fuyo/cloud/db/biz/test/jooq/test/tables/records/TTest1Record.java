/*
 * This file is generated by jOOQ.
 */
package com.fuyo.cloud.db.biz.test.jooq.test.tables.records;


import com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.interfaces.ITTest1;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TTest1Record extends UpdatableRecordImpl<TTest1Record> implements Record4<Integer, String, LocalDateTime, Byte>, ITTest1 {

    private static final long serialVersionUID = -932145670;

    /**
     * Setter for <code>test.t_test1.id</code>.
     */
    @Override
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>test.t_test1.id</code>.
     */
    @Override
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>test.t_test1.name</code>.
     */
    @Override
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>test.t_test1.name</code>.
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>test.t_test1.date_time</code>.
     */
    @Override
    public void setDateTime(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>test.t_test1.date_time</code>.
     */
    @Override
    public LocalDateTime getDateTime() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>test.t_test1.type</code>. test[start(1):开始,end(2):結束]
     */
    @Override
    public void setType(Byte value) {
        set(3, value);
    }

    /**
     * Getter for <code>test.t_test1.type</code>. test[start(1):开始,end(2):結束]
     */
    @Override
    public Byte getType() {
        return (Byte) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, LocalDateTime, Byte> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Integer, String, LocalDateTime, Byte> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return TTest1.T_TEST1.ID;
    }

    @Override
    public Field<String> field2() {
        return TTest1.T_TEST1.NAME;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return TTest1.T_TEST1.DATE_TIME;
    }

    @Override
    public Field<Byte> field4() {
        return TTest1.T_TEST1.TYPE;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public LocalDateTime component3() {
        return getDateTime();
    }

    @Override
    public Byte component4() {
        return getType();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public LocalDateTime value3() {
        return getDateTime();
    }

    @Override
    public Byte value4() {
        return getType();
    }

    @Override
    public TTest1Record value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public TTest1Record value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TTest1Record value3(LocalDateTime value) {
        setDateTime(value);
        return this;
    }

    @Override
    public TTest1Record value4(Byte value) {
        setType(value);
        return this;
    }

    @Override
    public TTest1Record values(Integer value1, String value2, LocalDateTime value3, Byte value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(ITTest1 from) {
        setId(from.getId());
        setName(from.getName());
        setDateTime(from.getDateTime());
        setType(from.getType());
    }

    @Override
    public <E extends ITTest1> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TTest1Record
     */
    public TTest1Record() {
        super(TTest1.T_TEST1);
    }

    /**
     * Create a detached, initialised TTest1Record
     */
    public TTest1Record(Integer id, String name, LocalDateTime dateTime, Byte type) {
        super(TTest1.T_TEST1);

        set(0, id);
        set(1, name);
        set(2, dateTime);
        set(3, type);
    }
}
