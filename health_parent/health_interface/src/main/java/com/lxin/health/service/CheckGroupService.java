package com.lxin.health.service;

import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.CheckGroup;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-03
 **/
public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    CheckGroup findById(int id);

    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(int id) throws MyException;

    List<CheckGroup> findAll();
}
