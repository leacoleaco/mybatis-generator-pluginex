package com.fuyo.cloud.db;

import com.fuyo.cloud.db.biz.jooq.util.DSLEx;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.daos.TTest1Dao;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.records.TTest1Record;
import com.github.pagehelper.Page;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    public void testN() {
        Map<String, Object> params = new HashMap<>();
//        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("oa_name", "3");
//        params.put("woeq_id", "1");
//        params.put("wolk_name", "21");
        params.put("od_id", "2");


        String s = dslContext.select(
                DSLEx.count1(),
                DSLEx.castToFirstDayOfWeek(T_TEST1.DATE_TIME)
        )
                .from(T_TEST1)
                .where(T_TEST1.buildCondition(params))
                .groupBy(
                        DSL.week(T_TEST1.DATE_TIME)
                )
                .toString();

        System.out.println(s);

    }

    @Test
    public void testN1() {
        Map<String, Object> params = new HashMap<>();
        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("oa_name", "3");
        params.put("woeq_id", "1");
        params.put("wolk_name", "21");
        params.put("od_id", "2");
        Page<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> tTest1s = tTest1Dao.fetchPage(params);
        System.out.println(tTest1s);

    }

    @Test
    public void test2() {
        Map<String, Object> params = new HashMap<>();
        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("oa_name", "3");
        params.put("woeq_id", "1");
        params.put("wolk_name", "21");
        params.put("od_id", "2");
//        SortField[] sortFields = T_TEST1.buildOrderBy(params, T_TEST1.NAME.asc());
        SortField[] sortFields = T_TEST1.buildOrderBy(params, T_TEST1.NAME.asc(), null);
        System.out.println(sortFields.length);
    }

    @Test
    public void test() {

        Map<String, Object> params = new HashMap<>();
        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("oa_name", "3");
        params.put("waeq_id", "1");
        params.put("walk_name", "21");
        params.put("od_id", "2");


//        PageHelper.startPage()
//                .doSelectPage(new ISelect() {
//                    @Override
//                    public void doSelect() {
//
//                    }
//                })

        List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetch = tTest1Dao.fetch(
                T_TEST1.ID,
                1
        );

        String s = dslContext
                .select(T_TEST1.ID,
                        T_TEST1.NAME,
                        T_TEST1.DATE_TIME
                )
                .from(T_TEST1)
                .where(T_TEST1.buildCondition(params))
                .and(T_TEST1.DATE_TIME.lt(LocalDateTime.now().plusDays(-3)))
                .orderBy(T_TEST1.buildOrderBy(params, T_TEST1.NAME.asc()))
                .limit(2, 3)
//                .fetch()
                .toString();


        System.out.println(s);
    }


    @Test
    public void testRecord() {
        Map<String, Object> params = new HashMap<>();
        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("oa_name", "3");
        params.put("od_id", "2");

        SelectQuery<TTest1Record> query = tTest1Dao.createQuery(params);

        System.out.println(query);

        tTest1Dao.fetchPage(query, 1, 2);

//        System.out.println(fetch);


    }

}


