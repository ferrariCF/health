package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.service.MemberService;
import com.lxin.health.service.OrderService;
import com.lxin.health.service.ReportService;
import com.lxin.health.service.SetmealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @date: 2021-07-14
 **/
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        Calendar car = Calendar.getInstance();
        // 过去一年
        car.add(Calendar.YEAR,-1);
        //构建12个月
        List<String> months = new ArrayList<>(12);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            //月份加1
            car.add(Calendar.MONTH,1);
            Date date = car.getTime();
            //格式化为yyyy-MM
            String month = dateFormat.format(date);
            months.add(month);
        }
        //调用服务查询过去12个月，每一个月的总会员数
        List<Integer> memberCount = memberService.getMemberReport(months);

        Map<String,Object> resultMap = new HashMap<>(2);
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }

    /**
     * 查询套餐占比
     * @return
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        List<Map<String,Object>> reportData = setmealService.getSetmealReport();

        List<String> setmealNames = reportData.stream().map(map -> {
            return (String) map.get("name");
        }).collect(Collectors.toList());

        Map<String,Object> resultMap = new HashMap<>(2);
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",reportData);

        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    /**
     * 查询运营统计数据
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> resultMap = reportService.getBusinessReportData();
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,resultMap);
    }

    /**
     * 导出运营统计数据excel报表
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        // 查询报表数据
        Map<String,Object> reportData = reportService.getBusinessReportData();
        // 获取模板文件 getRealPath webapp/
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        // 创建工作簿，读取模板
        try(Workbook wk = new XSSFWorkbook(template);) {
            // 获取工作表
            Sheet sht = wk.getSheetAt(0);
            // 通过模板填充数据
            sht.getRow(2).getCell(5).setCellValue(((String) reportData.get("reportDate")));
            // ============= 会员数量 ================
            sht.getRow(4).getCell(5).setCellValue((Integer)reportData.get("todayNewMember"));
            sht.getRow(4).getCell(7).setCellValue((Integer)reportData.get("totalMember"));
            sht.getRow(5).getCell(5).setCellValue((Integer)reportData.get("thisWeekNewMember"));
            sht.getRow(5).getCell(7).setCellValue((Integer)reportData.get("thisMonthNewMember"));

            // ============= 预约到诊 ================
            sht.getRow(7).getCell(5).setCellValue((Integer)reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer)reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer)reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer)reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer)reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer)reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map> hotSetmeal = (List<Map>) reportData.get("hotSetmeal");
            int rowIndex = 12;
            for (Map map : hotSetmeal) {
                Row row = sht.getRow(rowIndex);
                row.getCell(4).setCellValue(((String) map.get("name")));
                row.getCell(5).setCellValue((Long)map.get("setmeal_count"));
                BigDecimal bigDecimal = (BigDecimal)map.get("proportion");
                row.getCell(6).setCellValue(bigDecimal.doubleValue());
                row.getCell(7).setCellValue((String) map.get("remark"));
                rowIndex++;
            }
            // 内容体设置, 告诉浏览器的，下载的文件是就excel的文档
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营数据统计.xlsx";
            // 原始数据字节流,ISO-8859-1 支持 -127->+127
            byte[] bytes = filename.getBytes(); // 此处一定不能加入iso-8859-1
            filename = new String(bytes, "ISO-8859-1");
            System.out.println("转在iso-8859-1的字符串：" + filename);
            // 响应头信息设置
            res.setHeader("Content-Disposition","attachment;filename=" + filename);
            // 写到输出流
            wk.write(res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出运营统计数据excel报表
     */
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res){
        // 查询报表数据
        Map<String,Object> reportData = reportService.getBusinessReportData();
        // 获取模板文件 getRealPath webapp/
        String template = req.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        // 创建工作簿，读取模板
        // 数据模型
        Context context = new PoiContext();
        context.putVar("obj", reportData);
        try {
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template),res.getOutputStream(),context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exportPdf")
    public void exportPdf(HttpServletRequest req,HttpServletResponse res) throws Exception {
        //获取运营统计数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //定义模板位置
        String template = req.getSession().getServletContext().getRealPath("/template");
        String jrxml = template + File.separator + "health_business3.jrxml";
        //定义编译后模板位置
        String jasper = template + File.separator + "health_business3.jasper";
        //编译模板
        JasperCompileManager.compileReportToFile(jrxml,jasper);
        //填充数据
        List<Map> hotSetmeal = (List<Map>) reportData.get("hotSetmeal");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, reportData, new JRBeanCollectionDataSource(hotSetmeal));
        //设置响应头和内容体信息
        res.setContentType("application/pdf");
        res.setHeader("content-Disposition","attachment;filename=businessReport.pdf");
        //导出pdf
        JasperExportManager.exportReportToPdfStream(jasperPrint,res.getOutputStream());
    }
}
