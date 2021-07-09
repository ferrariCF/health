package com.lxin.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.dao.CheckGroupDao;
import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.CheckGroup;
import com.lxin.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-03
 **/
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //向t_checkgroup表中添加数据
        checkGroupDao.addCheckGroup(checkGroup);
        //获取自增长id
        Integer checkgroupId = checkGroup.getId();

        if (checkitemIds != null) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkgroupId, checkitemId);
            }
        }
    }

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page<CheckGroup> checkGroupPage = checkGroupDao.findByCondition(queryPageBean.getQueryString());

        return new PageResult<>(checkGroupPage.getTotal(), checkGroupPage.getResult());
    }

    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.updateCheckGroup(checkGroup);
        checkGroupDao.deleteByCheckGroupId(checkGroup.getId());
        if (checkitemIds != null) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(), checkitemId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) throws MyException {
        int count = checkGroupDao.findCountByCheckGroupId(id);

        if (count > 0) {
            throw new MyException("该检查组被套餐使用，不能删除");
        }

        checkGroupDao.deleteByCheckGroupId(id);
        checkGroupDao.deleteById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
