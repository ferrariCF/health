package com.lxin.health.service;

import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.Setmeal;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-06
 **/
public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id) throws MyException;
}
