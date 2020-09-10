package com.fuyo.cloud.db;


import com.fuyo.cloud.db.biz.test.g.dao.GTestDtoMapper;
import com.fuyo.cloud.db.biz.test.g.domain.GTestDto;
import com.fuyo.cloud.db.biz.test.g.domain.GTestDtoExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MapperTest {

    @Resource
    private GTestDtoMapper gTestDtoMapper;


//    @Test
//    public void test3() {
//        GTestDtoExample.Criteria joinCriteria = GTestDtoExample.build()
////                .andUserIdEqualTo(1)
//                ;
//
//
//        List<Map> maps = gTestDtoMapper.selectToMap(
//                GTestDtoExample.build()
//                        .andBetween(
//                                GTestDto.Column.createdTime.withAlias(),
//                                LocalDateTime.now().plusMonths(-1L),
//                                LocalDateTime.now()
//                        )
//                        .example()
//                        .leftJoin(GTestDto.TABLE,
//                                Mysql.equal(GTestDto.Column.orderId.withAlias(), GTestDto.Column.id.withAlias()))
//                        .andCriteria(joinCriteria)
//                        .groupBy(GTestDto.Column.name.withAlias())
//                        .orderBy("total desc")
//                ,
//                GTestDto.Column.name.withAlias("name"),
//                Mysql.sum(GTestDto.Column.quantity.withAlias(), "total")
//        );
//
//        System.out.println(maps);
//
//    }


    @Test
    public void test4() {


        GTestDto dto = gTestDtoMapper.selectOneByExampleSelective(
                GTestDtoExample.build()
//                        .andSpecificationsEqualTo(new String[]{"标准"})
                        .example(),
                GTestDto.Column.all()
        );

        System.out.println(dto);


        List<Map> maps = gTestDtoMapper.selectToMap(
                GTestDtoExample.build()
//                        .andSpecificationsEqualTo(new String[]{"标准"})
                        .example(),
                GTestDto.Column.id.as()
        );

        System.out.println(maps);

    }

}
