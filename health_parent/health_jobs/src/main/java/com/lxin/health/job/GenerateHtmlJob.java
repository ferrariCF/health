package com.lxin.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.pojo.Setmeal;
import com.lxin.health.service.SetmealService;
import com.lxin.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: lee
 * @date: 2021-07-12
 **/
@Component
public class GenerateHtmlJob {

    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Reference
    private SetmealService setmealService;

    /**
     * 初始化，配置模板路径和编码
     */
    @PostConstruct
    public void init(){
        freemarkerConfiguration.setClassForTemplateLoading(GenerateHtmlJob.class,"/ftl");
        freemarkerConfiguration.setDefaultEncoding("utf-8");
    }

    @Value("${out_put_path}")
    private String out_put_path; // 存放静态文件的目录


    @Scheduled(initialDelay = 3000,fixedDelay = 30*60*1000)
    public void generateHtml(){
        log.info("generateHtml 任务启动了...");
        Jedis jedis = jedisPool.getResource();
        Set<String> ids = jedis.smembers("setmeal:static:html");
        log.debug("要处理的套餐id个数：{}",ids.size());
        ids.forEach(id ->{
            //id id|操作类型|时间戳
            String[] idInfo = id.split("\\|");
            Integer setmealId = Integer.valueOf(idInfo[0]);
            //操作类型
            String operationType = idInfo[1];

            // 输出文件名
            String filename = out_put_path + File.separator + "setmeal_" + setmealId + ".html";

            if ("1".equals(operationType)) {
                generateDetailHtml(setmealId,filename);
            }else if ("0".equals(operationType)){
                log.debug("删除静态页面 {}",setmealId);
                new File(filename).delete();
            }
            // 删除redis中对应的id
            jedis.srem("setmeal:static:html",id);
        });

        if (ids.size() > 0){
            generateSetmealListHtml();
        }
        jedis.close();
    }

    /**
     * 生成套餐列表静态页面
     */
    private void generateSetmealListHtml() {
        log.debug("生成套餐列表静态页面");
        // 模板文件名字
        String templateName = "mobile_setmeal.ftl";

        Map<String,Object> dataMap = new HashMap<>();
        List<Setmeal> setmealList = setmealService.findAll();
        setmealList.forEach(setmeal -> {setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());});
        dataMap.put("setmealList",setmealList);

        String filename = out_put_path + File.separator + "mobile_setmeal.html";

        generateStaticHtml(templateName,filename,dataMap);
        log.info("生成套餐列表静态页面成功");
    }

    /**
     * 生成套餐详情静态页面
     * @param setmealId
     * @param filename
     */
    private void generateDetailHtml(Integer setmealId, String filename) {
        log.debug("生成详情静态页面 {}",setmealId);
        // 模板文件名字
        String templateName = "mobile_setmeal_detail.ftl";

        Map<String,Object> dataMap = new HashMap<>();
        Setmeal setmeal = setmealService.findDetailById(setmealId);
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        dataMap.put("setmeal",setmeal);

        generateStaticHtml(templateName,filename,dataMap);
        log.info("生成套餐详情静态页面成功");
    }

    private void generateStaticHtml(String templateName, String filename, Map<String, Object> dataMap) {
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
            template.process(dataMap,writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("生成静态页面失败 {}",filename,e);
        }
    }
}
