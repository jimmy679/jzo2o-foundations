package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jimmy679
 * @date 2024/6/22
 * @Description ServerMapper单测
 */
@SpringBootTest
@Slf4j
public class ServeMapperTest {
    @Resource
    private ServeMapper serveMapper;

    @Test
    void test_queryServeListByRegionId() {
        List<ServeResDTO> serveResDTOS = serveMapper.queryServeListByRegionId(1686303222843662337L);
        Assert.notEmpty(serveResDTOS,"列表为空");
    }
}
