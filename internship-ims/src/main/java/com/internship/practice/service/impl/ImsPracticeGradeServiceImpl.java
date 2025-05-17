package com.internship.practice.service.impl;

import com.internship.common.utils.DateUtils;
import com.internship.practice.domain.ImsPracticeGrade;
import com.internship.practice.mapper.ImsPracticeGradeMapper;
import com.internship.practice.service.IImsPracticeGradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实习成绩信息Service业务层处理
 * 
 * @author internship
 * @date 2020-12-20
 */
@Service
public class ImsPracticeGradeServiceImpl implements IImsPracticeGradeService 
{
    @Resource
    private ImsPracticeGradeMapper imsPracticeGradeMapper;

    /**
     * 查询实习成绩信息
     * 
     * @param gradeId 实习成绩信息ID
     * @return 实习成绩信息
     */
    @Override
    public ImsPracticeGrade selectImsPracticeGradeById(Long gradeId)
    {
        return imsPracticeGradeMapper.selectImsPracticeGradeById(gradeId);
    }

    /**
     * 查询实习成绩信息列表
     * 
     * @param imsPracticeGrade 实习成绩信息
     * @return 实习成绩信息
     */
    @Override
    public List<ImsPracticeGrade> selectImsPracticeGradeList(ImsPracticeGrade imsPracticeGrade)
    {
        return imsPracticeGradeMapper.selectImsPracticeGradeList(imsPracticeGrade);
    }

    /**
     * 新增实习成绩信息
     * 
     * @param imsPracticeGrade 实习成绩信息
     * @return 结果
     */
    @Override
    public int insertImsPracticeGrade(ImsPracticeGrade imsPracticeGrade)
    {
        imsPracticeGrade.setCreateTime(DateUtils.getNowDate());
        return imsPracticeGradeMapper.insertImsPracticeGrade(imsPracticeGrade);
    }

    /**
     * 修改实习成绩信息
     * 
     * @param imsPracticeGrade 实习成绩信息
     * @return 结果
     */
    @Override
    public int updateImsPracticeGrade(ImsPracticeGrade imsPracticeGrade)
    {
        imsPracticeGrade.setUpdateTime(DateUtils.getNowDate());
        return imsPracticeGradeMapper.updateImsPracticeGrade(imsPracticeGrade);
    }

    /**
     * 批量删除实习成绩信息
     * 
     * @param gradeIds 需要删除的实习成绩信息ID
     * @return 结果
     */
    @Override
    public int deleteImsPracticeGradeByIds(Long[] gradeIds)
    {
        return imsPracticeGradeMapper.deleteImsPracticeGradeByIds(gradeIds);
    }

    /**
     * 删除实习成绩信息信息
     * 
     * @param gradeId 实习成绩信息ID
     * @return 结果
     */
    @Override
    public int deleteImsPracticeGradeById(Long gradeId)
    {
        return imsPracticeGradeMapper.deleteImsPracticeGradeById(gradeId);
    }
}
