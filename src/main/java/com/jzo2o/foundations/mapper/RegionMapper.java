package com.jzo2o.foundations.mapper;

import com.jzo2o.foundations.model.domain.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 区域表 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface RegionMapper extends BaseMapper<Region> {
    Integer queryByRegionId(@Param("regionId") Long regionId);
}
