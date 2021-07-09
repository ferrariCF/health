package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.CheckGroup;
import com.lxin.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-03
 **/
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        checkGroupService.add(checkGroup,checkitemIds);
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<CheckGroup> checkGroupPageResult = checkGroupService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupPageResult);
    }

    @GetMapping("/findById")
    public Result findById(int id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int id){
        List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        checkGroupService.update(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/deleteById")
    public Result deleteById(int id){
        checkGroupService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @GetMapping("findAll")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
    }
}
