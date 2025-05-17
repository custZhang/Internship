package com.internship.dashboard.service;

import com.internship.analysis.domain.ImsPracticeWeeklyStatisticsVo;
import com.internship.analysis.domain.ImsThesisStatisticsVo;
import com.internship.dashboard.domain.ImsDashBoardNoticeVo;
import com.internship.dashboard.domain.ImsDashBoardTaskVo;

import java.util.List;

/**
 * @Author internship
 * @Date 2021/1/22 17:22
 * @Description 首页Service
 * @Version 1.0
 */
public interface ImsDashBoardService {

    /**
     * 首页实习考核plate数据
     * @param vo
     * @return
     */
    public List<List<?>> selectPracticeWeeklyStatisticsListInDashBoard(ImsPracticeWeeklyStatisticsVo vo);

    /**
     * 首页论文plate数据
     * @param vo
     * @return
     */
    public List<List<?>> selectThesisStatisticsListInDashBoard(ImsThesisStatisticsVo vo);

    /**
     * 首页 我的任务
     * @param vo
     * @return
     */
    public List<ImsDashBoardTaskVo> selectTaskInfoByUserId(ImsDashBoardTaskVo vo);

    /**
     * 首页最新动态
     * @param userId
     * @return
     */
    public List<ImsDashBoardNoticeVo> selectNoticeInfoByUserId(Long userId);

}
