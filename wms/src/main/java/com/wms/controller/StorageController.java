package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Storage;
import com.wms.service.IStorageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/storage")
public class StorageController {

    @Resource
    IStorageService storageService;

    //查询所有内容
    @GetMapping("/list")
    public Result list() {
        List list = storageService.lambdaQuery().list();
        return Result.success(list);
    }

    // 根据仓库名查询
    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name) {
        List list = storageService.lambdaQuery().eq(Storage::getName, name).list();
        return list.size() > 0 ? Result.success(list) : Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrMod(@RequestBody Storage storage) {
        return storageService.saveOrUpdate(storage) ? Result.success() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(Integer id) {
        return storageService.removeById(id) ? Result.success() : Result.fail();
    }

    // 前端用这个查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<Storage> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) lambdaQueryWrapper.like(Storage::getName, name);

        IPage result = storageService.pageCC(page, lambdaQueryWrapper);

        return Result.success(result.getTotal(), result.getRecords());
    }
}
