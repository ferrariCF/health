package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.Setmeal;
import com.lxin.health.service.SetmealService;
import com.lxin.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-11
 **/
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> setmealList = setmealService.findAll();

        setmealList.forEach(setmeal -> setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg()));
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
    }

    /**
     * 移动端根据id查询套餐详情
     * @param id
     * @return
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        Setmeal setmeal = setmealService.findDetailById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
