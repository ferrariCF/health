package com.lxin.health.service;

import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.CheckItem;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-02
 **/
public interface CheckItemService {
    List<CheckItem> findAll();

    void addCheckItem(CheckItem checkItem);

    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    CheckItem findById(int id);

    void update(CheckItem checkItem);

    void deleteById(int id) throws MyException;
}
