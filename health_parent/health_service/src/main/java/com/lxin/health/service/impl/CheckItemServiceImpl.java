package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.dao.CheckItemDao;
import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.CheckItem;
import com.lxin.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-02
 **/
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    @Override
    public void addCheckItem(CheckItem checkItem) {
        checkItemDao.addCheckItem(checkItem);
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        if (! StringUtil.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }

        Page<CheckItem> checkItemPage = checkItemDao.findByCondition(queryPageBean.getQueryString());

        long total = checkItemPage.getTotal();
        List<CheckItem> rows = checkItemPage.getResult();

        return new PageResult<>(total,rows);
    }

    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public void deleteById(int id) {
        //判断是否被检查组使用了
        int count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            throw new MyException("该检查项被检查组使用，不能删除");
        }

        checkItemDao.deleteById(id);
    }

}
