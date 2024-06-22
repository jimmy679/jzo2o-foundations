package com.jzo2o.foundations.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author author
 * @since 2024-06-22
 */
public interface IServeService extends IService<Serve> {
    /*/**
     *@Param servePageQueryReqDTO
     *@Return PageResult<ServeResDTO>
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);
}
