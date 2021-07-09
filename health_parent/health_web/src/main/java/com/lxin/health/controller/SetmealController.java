package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.Setmeal;
import com.lxin.health.service.SetmealService;
import com.lxin.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: lee
 * @date: 2021-07-06
 **/
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    //log日志
    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);
    @Reference
    private SetmealService setmealService;

    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //获取文件名
        String originalFilename = imgFile.getOriginalFilename();
        //获得文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成唯一文件名
        String imgName = UUID.randomUUID().toString() + suffix;

        try {
            //调用七牛云上传
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),imgName);
            //返回数据，域名+文件名
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("imgName",imgName);
            resultMap.put("domain",QiNiuUtils.DOMAIN);

            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,resultMap);
        } catch (IOException e) {
            log.error("上传文件失败");
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.add(setmeal,checkgroupIds);
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    @GetMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("domain",QiNiuUtils.DOMAIN);
        resultMap.put("setmeal",setmeal);

        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    @GetMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(Integer setmealId){
        List<Integer> checkgroupIds = setmealService.findCheckGroupIdsBySetmealId(setmealId);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,checkgroupIds);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        setmealService.update(setmeal,checkgroupIds);
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    @PostMapping("/delete")
    public Result delete(Integer id){
        setmealService.delete(id);
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
