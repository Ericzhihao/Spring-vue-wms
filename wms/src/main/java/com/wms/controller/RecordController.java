package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.service.IGoodsService;
import com.wms.service.IRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    IRecordService recordService;
    @Resource
    IGoodsService goodsService;


    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        Goods goods = goodsService.getById(record.getGoods());
        int num = record.getCount();

        // 出库
        if ("2".equals(record.getAction())) {
            num = -num;
            record.setCount(num);
        }

        // 入库
        int newnum = goods.getCount() + num;
        goods.setCount(newnum);
        goodsService.updateById(goods);




        return recordService.save(record) ? Result.success() : Result.fail();
    }

    // 前端用这个查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String goodstype = (String) param.get("goodstype");
        String storage = (String) param.get("storage");
        String roleId = (String) param.get("roleId");
        String userid = (String) param.get("userid");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" r.goods = g.id and g.storage = s.id and g.goodstype = t.id ");

        if ("2".equals(roleId)) {
            queryWrapper.apply(" r.userId = " + userid);
        }
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            queryWrapper.like("g.name", name);
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)) {
            queryWrapper.eq("s.id", storage);
        }
        if (StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) {
            queryWrapper.eq("t.id", goodstype);
        }
        IPage result = recordService.pageCC(page, queryWrapper);

        return Result.success(result.getTotal(), result.getRecords());
    }
}
