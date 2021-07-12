package com.lxin.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.service.SetmealService;
import com.lxin.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: lee
 * @date: 2021-07-09
 * 清理七牛上的垃圾文件
 **/
//@Component
public class CleanImgJob {

    public static final Logger log = LoggerFactory.getLogger(CleanImgJob.class);

    @Reference
    private SetmealService setmealService;

    //@Scheduled(cron = "0 0 2 * * ? *")//发布后使用
    //@Scheduled(initialDelay = 3000,fixedDelay = 30*60*1000)
    public void cleanImg(){
        List<String> img7Niu = QiNiuUtils.listFile();
        log.debug("七牛上有{}张图片",null == img7Niu ? 0 : img7Niu.size());

        List<String> imgDB = setmealService.findImgs();
        log.debug("数据库上有{}张图片",null == imgDB ? 0 : imgDB.size());

        img7Niu.removeAll(imgDB);
        log.debug("要删除的图片有{}张",img7Niu.size());

        String[] need2Delete = img7Niu.toArray(new String[]{});
        QiNiuUtils.removeFiles(need2Delete);
        log.info("删除{}张图片成功",img7Niu.size());
    }
}
