package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务相关接口")
public class ServeController {
    @Resource
    private IServeService iServeService;
    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        PageResult<ServeResDTO> page = iServeService.page(servePageQueryReqDTO);
        return page;
    }

    @PostMapping("batch")
    @ApiOperation("添加区域服务")
    public void add(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList){
        iServeService.batchAdd(serveUpsertReqDTOList);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改区域服务运营价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void update(@PathVariable("id") Long id, BigDecimal price){
        iServeService.update(id,price);
    }

    @PutMapping("/onSale/{id}")
    @ApiOperation("区域服务上架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void onSale(@PathVariable("id") Long id) {
        iServeService.onSale(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "区域服务id", required = true, dataTypeClass = Long.class),
    })
    public void delete(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        iServeService.deleteById(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation("区域服务下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void offSale(@PathVariable("id") Long id) {
        iServeService.offSale(id);
    }

}