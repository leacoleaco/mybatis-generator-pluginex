/*
 * This file is generated by jOOQ.
 */
package com.fuyo.cloud.db.biz.test.jooq.test;


import com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1Part;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.records.TTest1PartRecord;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.records.TTest1Record;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>test</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<TTest1Record> KEY_T_TEST1_PRIMARY = UniqueKeys0.KEY_T_TEST1_PRIMARY;
    public static final UniqueKey<TTest1PartRecord> KEY_T_TEST1_PART_PRIMARY = UniqueKeys0.KEY_T_TEST1_PART_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<TTest1Record> KEY_T_TEST1_PRIMARY = Internal.createUniqueKey(TTest1.T_TEST1, "KEY_t_test1_PRIMARY", new TableField[] { TTest1.T_TEST1.ID }, true);
        public static final UniqueKey<TTest1PartRecord> KEY_T_TEST1_PART_PRIMARY = Internal.createUniqueKey(TTest1Part.T_TEST1_PART, "KEY_t_test1_part_PRIMARY", new TableField[] { TTest1Part.T_TEST1_PART.ID }, true);
    }
}