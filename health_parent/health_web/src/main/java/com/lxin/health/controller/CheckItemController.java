package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.entity.Result;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.CheckItem;
import com.lxin.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-02
 **/
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有检查项
     *
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        List<CheckItem> checkItemList = checkItemService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
    }

    @PostMapping("/add")
    public Result addCheckItem(@RequestBody CheckItem checkItem) {
        checkItemService.addCheckItem(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }

    @GetMapping("/findById")
    public Result findById(int id) {
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
        checkItemService.update(checkItem);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @PostMapping("/deleteById")
    public Result deleteById(int id) {
        checkItemService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
