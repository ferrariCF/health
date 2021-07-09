package com.lxin.health.dao;

import com.github.pagehelper.Page;
import com.lxin.health.pojo.CheckItem;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-02
 **/
public interface CheckItemDao {
    List<CheckItem> findAll();

    void addCheckItem(CheckItem checkItem);

    Page<CheckItem> findByCondition(String queryString);

    CheckItem findById(int id);

    void update(CheckItem checkItem);

    int findCountByCheckItemId(int id);

    void deleteById(int id);
}
