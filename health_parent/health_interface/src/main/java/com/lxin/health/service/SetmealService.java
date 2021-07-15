package com.lxin.health.service;

import com.lxin.health.entity.PageResult;
import com.lxin.health.entity.QueryPageBean;
import com.lxin.health.exception.MyException;
import com.lxin.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-06
 **/
public interface SetmealService {
    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    Integer add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 根据套餐id查询包含的检查组的id集合
     * @param setmealId
     * @return
     */
    List<Integer> findCheckGroupIdsBySetmealId(Integer setmealId);

    /**
     * 修改套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 根据id删除套餐
     * @param id
     * @throws MyException
     */
    void delete(Integer id) throws MyException;

    /**
     * 查询所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 移动端查询所有套餐
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 移动端查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);

    /**
     * 查询套餐占比
     * @return
     */
    List<Map<String, Object>> getSetmealReport();
}
