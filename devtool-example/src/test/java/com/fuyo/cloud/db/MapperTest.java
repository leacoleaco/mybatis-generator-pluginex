package com.fuyo.cloud.db;

import com.fuyo.cloud.common.utils.Mysql;
import com.fuyo.cloud.db.biz.exam.g.dao.GExamOrderDtoMapper;
import com.fuyo.cloud.db.biz.exam.g.dao.GExamOrderProductsDtoMapper;
import com.fuyo.cloud.db.biz.exam.g.domain.GExamOrderDto;
import com.fuyo.cloud.db.biz.exam.g.domain.GExamOrderDtoExample;
import com.fuyo.cloud.db.biz.exam.g.domain.GExamOrderProductsDto;
import com.fuyo.cloud.db.biz.exam.g.domain.GExamOrderProductsDtoExample;
import com.fuyo.cloud.db.biz.mall.g.dao.GGoodsDtoMapper;
import com.fuyo.cloud.db.biz.mall.g.dao.GGoodsProductDtoMapper;
import com.fuyo.cloud.db.biz.mall.g.domain.GGoodsProductDto;
import com.fuyo.cloud.db.biz.mall.g.domain.GGoodsProductDtoExample;
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
    private GExamOrderDtoMapper gExamOrderDtoMapper;

    @Resource
    private GExamOrderProductsDtoMapper gExamOrderProductsDtoMapper;

    @Test
    public void test1() {

        List<Map> maps = gExamOrderDtoMapper.selectToMap(
                GExamOrderDtoExample.build()
                        .andCreatedTimeBetween(
                                LocalDateTime.now().plusMonths(-12L),
                                LocalDateTime.now()
                        )
                        .example()
                        .groupBy(GExamOrderDto.Column.createdTime.yearWeekMondayFirst())
                ,
                GExamOrderDto.Column.createdTime.firstDayOfWeekAs(),
                "COUNT(1) as count"
        );


    }


    @Test
    public void test2() {


//
//        criteria.createJoinCriteria()
//                .innerJoinTable(urExample)
//                //使用User表的user_id关联user_role表的user_id
//                .on(a->a.getUserId(), a->a.equalTo(b->b.tableInfo.getUserId()));


        List<Map> maps = gExamOrderProductsDtoMapper.selectToMap(
                GExamOrderProductsDtoExample.build()
                        .andBetween(
                                GExamOrderDto.Column.createdTime.withAlias(),
                                LocalDateTime.now().plusMonths(-12L),
                                LocalDateTime.now()
                        )
                        .example()
                        .leftJoin(GExamOrderDto.TABLE,
                                Mysql.equal(GExamOrderProductsDto.Column.orderId.withAlias(), GExamOrderDto.Column.id.withAlias()))
                        .groupBy(GExamOrderProductsDto.Column.name.withAlias())
                        .orderBy("total desc")
                ,
                GExamOrderProductsDto.Column.name.withAlias("name"),
                Mysql.sum(GExamOrderProductsDto.Column.quantity.withAlias(), "total")
        );


        System.out.println(maps);

    }


    @Test
    public void test3() {
        GExamOrderDtoExample.Criteria joinCriteria = GExamOrderDtoExample.build()
//                .andUserIdEqualTo(1)
                ;


        List<Map> maps = gExamOrderProductsDtoMapper.selectToMap(
                GExamOrderProductsDtoExample.build()
                        .andBetween(
                                GExamOrderDto.Column.createdTime.withAlias(),
                                LocalDateTime.now().plusMonths(-1L),
                                LocalDateTime.now()
                        )
                        .example()
                        .leftJoin(GExamOrderDto.TABLE,
                                Mysql.equal(GExamOrderProductsDto.Column.orderId.withAlias(), GExamOrderDto.Column.id.withAlias()))
                        .andCriteria(joinCriteria)
                        .groupBy(GExamOrderProductsDto.Column.name.withAlias())
                        .orderBy("total desc")
                ,
                GExamOrderProductsDto.Column.name.withAlias("name"),
                Mysql.sum(GExamOrderProductsDto.Column.quantity.withAlias(), "total")
        );

        System.out.println(maps);

    }


    @Resource
    private GGoodsProductDtoMapper gGoodsProductDtoMapper;
    @Resource
    private GGoodsDtoMapper gGoodsDtoMapper;

    @Test
    public void test4() {




        GGoodsProductDto dto = gGoodsProductDtoMapper.selectOneByExampleSelective(
                GGoodsProductDtoExample.build()
                        .andSpecificationsEqualTo(new String[]{"标准"})
                        .example(),
                GGoodsProductDto.Column.all()
        );

        System.out.println(dto);


        List<Map> maps = gGoodsProductDtoMapper.selectToMap(
                GGoodsProductDtoExample.build()
                        .andSpecificationsEqualTo(new String[]{"标准"})
                        .example(),
                GGoodsProductDto.Column.id.as()
        );

        System.out.println(maps);

    }

}
