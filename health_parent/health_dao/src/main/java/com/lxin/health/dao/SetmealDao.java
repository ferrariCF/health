package com.lxin.health.dao;

import com.github.pagehelper.Page;
import com.lxin.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-07
 **/
public interface SetmealDao {
    /**
     * 添加套餐
     * @param setmeal
     */
    void addSetmeal(Setmeal setmeal);

    /**
     * 添加套餐和检查组关系
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 根据条件查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 查找该套餐的检查组
     * @param setmealId
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    /**
     * 修改套餐
     * @param setmeal
     */
    void updateSetmeal(Setmeal setmeal);

    /**
     * 删除该套餐的检查组
     * @param id
     */
    void deleteCheckGroupIdBySetmealId(Integer id);

    /**
     * 根据setmealId查询t_order中的订单个数
     * @param id
     * @return
     */
    int findCountBySetmealId(Integer id);

    /**
     * 根据id删除套餐
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 查询数据库中所有图片
     * @return
     */
    List<String> findImgs();
}
