package com.internship.thesis.mapper;

import java.util.List;
import com.internship.thesis.domain.ImsThesisTaskBook;

/**
 * 任务书信息Mapper接口
 * 
 * @author internship
 * @date 2020-12-18
 */
public interface ImsThesisTaskBookMapper 
{
    /**
     * 查询任务书信息
     * 
     * @param taskBookId 任务书信息ID
     * @return 任务书信息
     */
    public ImsThesisTaskBook selectImsThesisTaskBookById(Long taskBookId);

    /**
     * 查询任务书信息列表
     * 
     * @param imsThesisTaskBook 任务书信息
     * @return 任务书信息集合
     */
    public List<ImsThesisTaskBook> selectImsThesisTaskBookList(ImsThesisTaskBook imsThesisTaskBook);

    /**
     * 新增任务书信息
     * 
     * @param imsThesisTaskBook 任务书信息
     * @return 结果
     */
    public int insertImsThesisTaskBook(ImsThesisTaskBook imsThesisTaskBook);

    /**
     * 修改任务书信息
     * 
     * @param imsThesisTaskBook 任务书信息
     * @return 结果
     */
    public int updateImsThesisTaskBook(ImsThesisTaskBook imsThesisTaskBook);

    /**
     * 删除任务书信息
     * 
     * @param taskBookId 任务书信息ID
     * @return 结果
     */
    public int deleteImsThesisTaskBookById(Long taskBookId);

    /**
     * 批量删除任务书信息
     * 
     * @param taskBookIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImsThesisTaskBookByIds(Long[] taskBookIds);
}
