package com.internship.thesis.service.impl;

import com.internship.common.utils.DateUtils;
import com.internship.common.utils.StringUtils;
import com.internship.commons.ImsConstants;
import com.internship.thesis.domain.ImsThesisPaper;
import com.internship.thesis.domain.ImsThesisTopic;
import com.internship.thesis.mapper.ImsThesisPaperMapper;
import com.internship.thesis.mapper.ImsThesisTopicMapper;
import com.internship.thesis.service.IImsThesisPaperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 论文信息Service业务层处理
 * 
 * @author internship
 * @date 2020-12-18
 */
@Service
public class ImsThesisPaperServiceImpl implements IImsThesisPaperService 
{
    @Resource
    private ImsThesisPaperMapper imsThesisPaperMapper;
    @Resource
    private ImsThesisTopicMapper thesisTopicMapper;

    /**
     * 查询论文信息
     * 
     * @param paperId 论文信息ID
     * @return 论文信息
     */
    @Override
    public ImsThesisPaper selectImsThesisPaperById(String paperId)
    {
        return imsThesisPaperMapper.selectImsThesisPaperById(paperId);
    }

    /**
     * 查询论文信息列表
     * 
     * @param imsThesisPaper 论文信息
     * @return 论文信息
     */
    @Override
    public List<ImsThesisPaper> selectImsThesisPaperList(ImsThesisPaper imsThesisPaper)
    {
        return imsThesisPaperMapper.selectImsThesisPaperList(imsThesisPaper);
    }

    /**
     * 新增论文信息
     * 
     * @param imsThesisPaper 论文信息
     * @return 结果
     */
    @Override
    public int insertImsThesisPaper(ImsThesisPaper imsThesisPaper)
    {
        imsThesisPaper.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNotEmpty(imsThesisPaper.getTopicId())){
            ImsThesisTopic thesisTopic = thesisTopicMapper.selectImsThesisTopicById(imsThesisPaper.getTopicId());
            thesisTopic.setTopicStatus(ImsConstants.TOPIC_STATUS_8);
        }
        return imsThesisPaperMapper.insertImsThesisPaper(imsThesisPaper);
    }

    /**
     * 修改论文信息
     * 
     * @param imsThesisPaper 论文信息
     * @return 结果
     */
    @Override
    public int updateImsThesisPaper(ImsThesisPaper imsThesisPaper)
    {
        imsThesisPaper.setUpdateTime(DateUtils.getNowDate());
        return imsThesisPaperMapper.updateImsThesisPaper(imsThesisPaper);
    }

    /**
     * 批量删除论文信息
     * 
     * @param paperIds 需要删除的论文信息ID
     * @return 结果
     */
    @Override
    public int deleteImsThesisPaperByIds(String[] paperIds)
    {
        return imsThesisPaperMapper.deleteImsThesisPaperByIds(paperIds);
    }

    /**
     * 删除论文信息信息
     * 
     * @param paperId 论文信息ID
     * @return 结果
     */
    @Override
    public int deleteImsThesisPaperById(String paperId)
    {
        return imsThesisPaperMapper.deleteImsThesisPaperById(paperId);
    }
}
