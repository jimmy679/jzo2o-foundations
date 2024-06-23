package com.jzo2o.foundations.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author author
 * @since 2024-06-22
 */
public interface IServeService extends IService<Serve> {
    /**
     *@Param servePageQueryReqDTO
     *@Return PageResult<ServeResDTO>
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /*/**
     *@Param 批量添加区域服务
     *@Return
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /*/**
     *@Param 修改运营价格
     *@Return
     */
    Serve update(Long id, BigDecimal price);

    /*/**
     *@Param 上架服务
     *@Return
     */
    Serve onSale(Long id);
    /*/**
     *@Param 删除区域服务
     *@Return 
     */
    Serve deleteById(Long id);

    /*/**
     *@Param 下架服务
     *@Return
     */
    Serve offSale(Long id);

    /*/**
     *@Param 设置区域服务热门
     *@Return
     */
    Serve onHot(Long id);

    /*/**
     *@Param 设置区域服务非热门
     *@Return
     */
    Serve offHot(Long id);
}
