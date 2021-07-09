package com.lxin.health.dao;

import com.github.pagehelper.Page;
import com.lxin.health.pojo.CheckGroup;
import com.lxin.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-03
 **/
public interface CheckGroupDao {
    /**
     * 添加检查组
     * @param checkGroup
     */
    void addCheckGroup(CheckGroup checkGroup);

    /**
     * 向检查组中添加检查项
     * @param checkgroupId
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkgroupId") Integer checkgroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 根据查询条件查询检查组列表
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 根据检查组的id查询检查项的id集合
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 修改检查组
     * @param checkGroup
     */
    void updateCheckGroup(CheckGroup checkGroup);

    /**
     * 删除该检查组的检查项
     * @param id
     */
    void deleteByCheckGroupId(Integer id);

    /**
     * 查询使用该检查组的套餐数量
     * @param id
     * @return
     */
    int findCountByCheckGroupId(int id);

    /**
     * 根据id删除检查组
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}
