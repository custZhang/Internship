package com.internship.thesis.mapper;

import java.util.List;
import com.internship.thesis.domain.ImsThesisPaper;

/**
 * 论文信息Mapper接口
 * 
 * @author internship
 * @date 2020-12-18
 */
public interface ImsThesisPaperMapper 
{
    /**
     * 查询论文信息
     * 
     * @param paperId 论文信息ID
     * @return 论文信息
     */
    public ImsThesisPaper selectImsThesisPaperById(String paperId);

    /**
     * 查询论文信息列表
     * 
     * @param imsThesisPaper 论文信息
     * @return 论文信息集合
     */
    public List<ImsThesisPaper> selectImsThesisPaperList(ImsThesisPaper imsThesisPaper);

    /**
     * 新增论文信息
     * 
     * @param imsThesisPaper 论文信息
     * @return 结果
     */
    public int insertImsThesisPaper(ImsThesisPaper imsThesisPaper);

    /**
     * 修改论文信息
     * 
     * @param imsThesisPaper 论文信息
     * @return 结果
     */
    public int updateImsThesisPaper(ImsThesisPaper imsThesisPaper);

    /**
     * 删除论文信息
     * 
     * @param paperId 论文信息ID
     * @return 结果
     */
    public int deleteImsThesisPaperById(String paperId);

    /**
     * 批量删除论文信息
     * 
     * @param paperIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImsThesisPaperByIds(String[] paperIds);
}
