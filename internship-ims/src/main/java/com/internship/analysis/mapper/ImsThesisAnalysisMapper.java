package com.internship.analysis.mapper;

import com.internship.analysis.domain.ImsThesisStatisticsVo;

import java.util.List;

/**
 * @author internship
 */
public interface ImsThesisAnalysisMapper {

    /**
     * 论文统计分析
     * @param vo
     * @return
     */
    public List<ImsThesisStatisticsVo> selectThesisStatisticsList(ImsThesisStatisticsVo vo);
}
