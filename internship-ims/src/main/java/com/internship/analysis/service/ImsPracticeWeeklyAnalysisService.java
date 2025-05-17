package com.internship.analysis.service;

import java.util.List;
import java.util.Map;

/**
 * @Author internship
 * @Date 2021/1/22 17:22
 * @Description 实习统计相关分析
 * @Version 1.0
 */
public interface ImsPracticeWeeklyAnalysisService {

    /**
     * 实习周志相关分析
     * @param deptId 院系ID
     * @param academicYear 年份
     * @param deptType 院系类型
     * @return
     */
    public Map<Object, List<?>> calculationPracticeWeekly(Long deptId, String academicYear, String deptType, String week);
}
