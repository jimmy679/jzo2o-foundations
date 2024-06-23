package com.jzo2o.foundations.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-06-22
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {
    @Resource
    private ServeItemMapper serveItemMapper;
    @Resource
    private RegionMapper regionMapper;
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        //调用mapper查询数据，这里由于继承了ServiceImpl<ServeMapper, Serve>，使用baseMapper相当于使用ServeMapper
        PageResult<ServeResDTO> serveResDTOPageResult = PageHelperUtils.selectPage(servePageQueryReqDTO, () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
        return serveResDTOPageResult;
    }

    @Override
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        //合法性校验
        for(ServeUpsertReqDTO serveUpsertReqDTO: serveUpsertReqDTOList){
            //serve_item是否启用
            Long serveItemId = serveUpsertReqDTO.getServeItemId();
            ServeItem serveItem = serveItemMapper.selectById(serveItemId);
            if (ObjectUtils.isEmpty(serveItem) || serveItem.getActiveStatus()!= FoundationStatusEnum.ENABLE.getStatus()){
                //抛出异常
                throw new ForbiddenOperationException("服务项不存在或服务项未启动");
            }
            //同一区域下不能添加相同服务
            Integer count = lambdaQuery()
                    .eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                    .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId())
                    .count();
            if(count > 0){
                throw  new ForbiddenOperationException("同一区域下不能添加相同服务!"+serveItem.getName());
            }
            //校验完成，开始插入数据->serve表
            Serve serve = BeanUtil.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serveUpsertReqDTO.getRegionId());
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    @Override
    @Transactional
    public Serve update(Long id, BigDecimal price) {
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getPrice, price)
                .update();
        if(!update){
            throw new CommonException("修改价格失败");
        }
        Serve serve = baseMapper.selectById(id);
        return serve;
    }

    @Override
    public Serve onSale(Long id) {
        //校验状态
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        Integer saleStatus = serve.getSaleStatus();
        if(!(saleStatus==FoundationStatusEnum.INIT.getStatus() || saleStatus==FoundationStatusEnum.DISABLE.getStatus())){
            throw new ForbiddenOperationException("当前服务项状态不支持修改"+saleStatus);
        }
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if(ObjectUtils.isNull(serveItem)){
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        Integer activeStatus = serveItem.getActiveStatus();
        if(!(activeStatus== FoundationStatusEnum.ENABLE.getStatus())){
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
        }
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if(!update){
            throw  new CommonException("启动服务失败--区域服务上架接口");
        }
        return baseMapper.selectById(id);
    }

    @Override
    public Serve deleteById(Long id) {
        //校验数据状态是否合法
        Serve serve = baseMapper.selectById(id);
        Integer saleStatus = serve.getSaleStatus();
        if(saleStatus == FoundationStatusEnum.INIT.getStatus()){
            throw  new ForbiddenOperationException("只有区域状态为草稿时才能删除！");
        }
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .update();
        if(!update){
            throw  new CommonException("禁用失败！");
        }
        return baseMapper.selectById(id);
    }

    @Override
    public Serve offSale(Long id) {
        //数据合理性校验
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtils.isEmpty(serve)){
            throw  new ForbiddenOperationException("当前服务不存在！");
        }
        Integer saleStatus = serve.getSaleStatus();
        if(!(saleStatus == FoundationStatusEnum.ENABLE.getStatus())){
            throw  new ForbiddenOperationException("服务项只有在启用后才能下架！");
        }
        Long serveItemId = serve.getServeItemId();
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus,FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if(!update){
            throw  new CommonException("下架服务失败！");
        }
        return baseMapper.selectById(id);
    }
}
