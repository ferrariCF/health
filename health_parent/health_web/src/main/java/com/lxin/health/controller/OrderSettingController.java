package com.lxin.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxin.health.constant.MessageConstant;
import com.lxin.health.entity.Result;
import com.lxin.health.pojo.OrderSetting;
import com.lxin.health.service.OrderSettingService;
import com.lxin.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lee
 * @date: 2021-07-10
 **/
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    private static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量导入预约设置
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile) throws IOException {
        List<String[]> orderInfoStringArrList = POIUtils.readExcel(excelFile);

        final SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);

        List<OrderSetting> orderSettingList = orderInfoStringArrList.stream().map(orderInfoStringArr -> {
            OrderSetting os = new OrderSetting();

            String orderDateStr = orderInfoStringArr[0];
            try {
                os.setOrderDate(sdf.parse(orderDateStr));
            } catch (ParseException e) {}

            os.setNumber(Integer.valueOf(orderInfoStringArr[1]));
            return os;
        }).collect(Collectors.toList());

        orderSettingService.add(orderSettingList);

        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /**
     * 日历展示
     * @param month
     * @return
     */
    @GetMapping("/getDataByMonth")
    public Result getDataByMonth(String month){
        List<Map<String,Integer>> monthData = orderSettingService.getDataByMonth(month);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,monthData);
    }
}
