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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MapperTest {

    @Resource
    private GTestDtoMapper gTestDtoMapper;


    @Test
    public void test3() {

        Map<String, Object> params = new HashMap<>();
        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("oa_name", "3");
        params.put("od_id", "2");

        List<GTestDto> gTestDtos = gTestDtoMapper.selectByExample(GTestDtoExample.buildWithUrlParam(params).example());
        System.out.println(gTestDtos);

    }

    @Test
    public void test5() {

        Map<String, Object> params = new HashMap<>();
        params.put("wage_dateTime", "2020-09-01 00:00:00");
        params.put("page", 3);
        params.put("limit", 5);
        params.put("oa_name", "3");
        params.put("od_id", "2");

        GTestDtoExample example = GTestDtoExample.buildWithUrlParam(params).example();
        System.out.println(example.page);
        System.out.println(example.pageSize);

        List<GTestDto> gTestDtos = gTestDtoMapper.selectByExample(example);
        System.out.println(gTestDtos);

    }

}
