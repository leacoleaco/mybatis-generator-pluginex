package com.fuyo.cloud.db;

import com.fuyo.cloud.db.biz.test.jooq.test.tables.daos.TTest1Dao;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1.T_TEST1;

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

        Map<String, Object> params = new HashMap<>();
        params.put("wale_dateTime", "2020-09-27 00:00:00");

        String s = dslContext
                .select(T_TEST1.ID,
                        T_TEST1.NAME,
                        T_TEST1.DATE_TIME
                )
                .from(T_TEST1)
                .where(T_TEST1.buildConditionByUrl(params))
                .and(T_TEST1.DATE_TIME.ge(LocalDateTime.now().plusDays(-3)))
                .limit(2)
                .fetch()
                .toString();

        System.out.println(s);
    }

    @Test
    public void testRecord() {
        SelectQuery<Record> query = dslContext.selectQuery();





//        Condition condition= T_TEST1.buildWithUrlParam();
//
//        TableField<TTest1Record, String> field;
//        Field<TTest1Record> f = new TableField<>() {
//        };
//        f.greaterOrEqual();
//
//        query.addConditions(Operator.AND, T_TEST1.NAME.greaterThan());

    }

}


