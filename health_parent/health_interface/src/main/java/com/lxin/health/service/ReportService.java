package com.lxin.health.service;

import java.util.Map;

/**
 * @author: lee
 * @date: 2021-07-15
 **/
public interface ReportService {
    /**
     * 查询运营统计数据
     * @return
     */
    Map<String, Object> getBusinessReportData();
}
