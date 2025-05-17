package com.internship.task.service;

import com.internship.task.domain.ImsTaskChild;
import com.internship.task.domain.vo.practiceVo.ImsChildPracticeVo;
import com.internship.task.domain.vo.practiceVo.ImsPracticeApplicationVo;

import java.util.List;

/**
 * 子任务信息Service接口
 * 
 * @author internship
 * @date 2020-12-25
 */
public interface IImsTaskChildService 
{
    /**
     * 查询子任务信息
     * 
     * @param childId 子任务信息ID
     * @return 子任务信息
     */
    public ImsTaskChild selectImsTaskChildById(Long childId);

    /**
     * 查询子任务信息

     * @return ImsTaskChild
     */
    public ImsTaskChild selectImsTaskChildByIds(ImsTaskChild imsTaskChild);

    /**
     * 查询子任务信息列表
     * 
     * @param imsTaskChild 子任务信息
     * @return 子任务信息集合
     */
    public List<ImsTaskChild> selectImsTaskChildList(ImsTaskChild imsTaskChild);

    /**
     * 查询实习周记、总结子任务信息列表
     *
     * @param imsChildPracticeVo 子任务信息
     * @return 子任务信息集合
     */
    public List<ImsChildPracticeVo> selectChildPracticeList(ImsChildPracticeVo imsChildPracticeVo);

    /**
     * 查询实习申请待办列表
     * @param vo
     * @return
     */
    public List<ImsPracticeApplicationVo> selectPracticeApplicationList(ImsPracticeApplicationVo vo);

    /**
     * 新增子任务信息
     * 
     * @param imsTaskChild 子任务信息
     * @return 结果
     */
    public void insertImsTaskChild(ImsTaskChild imsTaskChild);

    /**
     * 修改子任务信息
     * 
     * @param imsTaskChild 子任务信息
     * @return 结果
     */
    public int updateImsTaskChild(ImsTaskChild imsTaskChild);

    /**
     * 批量删除子任务信息
     * 
     * @param childIds 需要删除的子任务信息ID
     * @return 结果
     */
    public int deleteImsTaskChildByIds(Long[] childIds);

    /**
     * 删除子任务信息信息
     * 
     * @param childId 子任务信息ID
     * @return 结果
     */
    public int deleteImsTaskChildById(Long childId);
}
