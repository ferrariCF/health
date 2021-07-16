package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.constant.RedisMessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.CheckGroup;
import com.lxin.health.pojo.CheckItem;
import com.lxin.health.pojo.Order;
import com.lxin.health.pojo.Setmeal;
import com.lxin.health.service.OrderService;
import com.lxin.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: lee
 * @date: 2021-07-13
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> paraMap){
        // 校验验证码
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + paraMap.get("telephone");

        String codeInRedis = jedis.get(key);

        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"请重新发送验证码！");
        }

        if (!codeInRedis.equals(paraMap.get("validateCode"))) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 验证码正确，删除redis中的验证码
        jedis.del(key);
        jedis.close();

        //设置预约类型
        paraMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        Integer id = orderService.submit(paraMap);

        return new Result(true,MessageConstant.ORDER_SUCCESS,id);
    }

    /**
     * 预约订单回显
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,Object> orderInfo = orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }

    /**
     * pdf导出预约订单信息
     * @param id
     * @return
     */
    @GetMapping("/exportPdf")
    public void exportPdf(int id, HttpServletResponse res) throws Exception {
        //通过订单id查询预约订单信息
        Map<String, Object> orderInfo = orderService.findById(id);
        //获取套餐id
        Integer setmealId = (Integer) orderInfo.get("setmeal_id");
        //调用服务查询套餐信息
        Setmeal setmeal = setmealService.findDetailById(setmealId);
        //设置内容体信息
        res.setContentType("application/pdf");
        //设置响应头信息
        res.setHeader("Content-Disposition","attachment;filename=orderInfo.pdf");
        //创建pdf文档
        Document doc = new Document();
        //设置文档输出流
        PdfWriter.getInstance(doc,res.getOutputStream());
        //打开文档
        doc.open();
        //解决中文显示问题
        Font chinese = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        //添加内容
        doc.add(new Paragraph("预约信息",chinese));
        doc.add(new Paragraph("体检人："+orderInfo.get("member"),chinese));
        doc.add(new Paragraph("体检套餐："+orderInfo.get("setmeal"),chinese));
        doc.add(new Paragraph("体检日期："+orderInfo.get("orderDate"),chinese));
        doc.add(new Paragraph("预约类型："+orderInfo.get("orderType"),chinese));
        //添加套餐详情表格
        Table table = new Table(3);
        // ============= 表格样式 =================
        table.setWidth(80); // 宽度
        table.setBorder(1); // 边框
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
        /*设置表格属性*/
        table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
        table.setPadding(5);//设置表格与字体间的间距
        //table.setSpacing(2);//设置表格上下的间距
        table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式
        // ============= 表格样式 =================
        table.addCell(buildCell("项目名称",chinese));
        table.addCell(buildCell("项目内容",chinese));
        table.addCell(buildCell("项目解读",chinese));
        List<CheckGroup> checkGroups = setmeal.getCheckGroups();
        for (CheckGroup checkGroup : checkGroups) {
            table.addCell(buildCell(checkGroup.getName(),chinese));
            List<CheckItem> checkItems = checkGroup.getCheckItems();
            //空格分隔
            StringJoiner joiner = new StringJoiner(" ");
            checkItems.forEach(checkItem -> {
                joiner.add(checkItem.getName());
            });
            table.addCell(buildCell(joiner.toString(),chinese));
            table.addCell(buildCell(checkGroup.getRemark(),chinese));
        }
        doc.add(table);
        //关闭文档
        doc.close();
    }

    private Cell buildCell(String content,Font chinese) throws BadElementException {
        return new Cell(new Phrase(content,chinese));
    }
}
