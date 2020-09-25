package com.fuyo.cloud.db;

import com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.daos.TTest1Dao;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.records.TTest1Record;
import org.jooq.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class JooqTest {

    @Resource
    DSLContext dslContext;

    @Resource
    TTest1Dao tTest1Dao;

    @Test
    public void test2() {
        com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1 tTest1 = tTest1Dao.fetchOneById(1);
        System.out.println(tTest1);
    }

    @Test
    public void test() {

        String s = dslContext
                .select(T_TEST1.ID,
                        T_TEST1.NAME,
                        T_TEST1.DATE_TIME
                )
                .from(T_TEST1)
                .where(T_TEST1.DATE_TIME.ge(LocalDateTime.now().plusDays(-3)))
                .limit(2)
                .toString();

        System.out.println(s);
    }

    @Test
    public void testRecord() {
        SelectQuery<Record> query = dslContext.selectQuery();
//        query.addConditions(Operator.AND,T_TEST1.eq());

    }

}


