package com.internship.enterprise.service.impl;

import java.util.List;
import com.internship.common.utils.DateUtils;
import com.internship.commons.ImsConstants;
import com.internship.enterprise.domain.vo.RecruitmentInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.internship.enterprise.mapper.ImsEnterprisePostMapper;
import com.internship.enterprise.domain.ImsEnterprisePost;
import com.internship.enterprise.service.IImsEnterprisePostService;

import javax.annotation.Resource;

/**
 * 企业岗位信息Service业务层处理
 * 
 * @author internship
 * @date 2020-11-17
 */
@Service
public class ImsEnterprisePostServiceImpl implements IImsEnterprisePostService 
{
    @Resource
    private ImsEnterprisePostMapper imsEnterprisePostMapper;

    /**
     * 查询企业岗位信息
     * 
     * @param postId 企业岗位信息ID
     * @return 企业岗位信息
     */
    @Override
    public ImsEnterprisePost selectImsEnterprisePostById(Long postId)
    {
        return imsEnterprisePostMapper.selectImsEnterprisePostById(postId);
    }

    /**
     * 查询企业岗位信息列表
     * 
     * @param imsEnterprisePost 企业岗位信息
     * @return 企业岗位信息
     */
    @Override
    public List<ImsEnterprisePost> selectImsEnterprisePostList(ImsEnterprisePost imsEnterprisePost)
    {
        return imsEnterprisePostMapper.selectImsEnterprisePostList(imsEnterprisePost);
    }

    /**
     * 查询招聘信息列表
     * @param recruitmentInfoVo 招聘信息Vo
     * @return
     */
    @Override
    public List<RecruitmentInfoVo> selectEnterpriseWithPostList(RecruitmentInfoVo recruitmentInfoVo) {
        return imsEnterprisePostMapper.selectEnterpriseWithPostList(recruitmentInfoVo);
    }

    /**
     * 查询招聘信息详情
     * @param postId 岗位ID
     * @return
     */
    @Override
    public RecruitmentInfoVo selectEnterpriseWithPostByPostId(Long postId) {
        return imsEnterprisePostMapper.selectEnterpriseWithPostByPostId(postId);
    }

    /**
     * 新增企业岗位信息
     * 
     * @param imsEnterprisePost 企业岗位信息
     * @return 结果
     */
    @Override
    public int insertImsEnterprisePost(ImsEnterprisePost imsEnterprisePost)
    {
        imsEnterprisePost.setCreateTime(DateUtils.getNowDate());
        imsEnterprisePost.setDelFlag(ImsConstants.dEL_NO);
        return imsEnterprisePostMapper.insertImsEnterprisePost(imsEnterprisePost);
    }

    /**
     * 修改企业岗位信息
     * 
     * @param imsEnterprisePost 企业岗位信息
     * @return 结果
     */
    @Override
    public int updateImsEnterprisePost(ImsEnterprisePost imsEnterprisePost)
    {
        imsEnterprisePost.setUpdateTime(DateUtils.getNowDate());
        return imsEnterprisePostMapper.updateImsEnterprisePost(imsEnterprisePost);
    }

    /**
     * 批量删除企业岗位信息
     * 
     * @param postIds 需要删除的企业岗位信息ID
     * @return 结果
     */
    @Override
    public int deleteImsEnterprisePostByIds(Long[] postIds)
    {
        return imsEnterprisePostMapper.deleteImsEnterprisePostByIds(postIds);
    }

    /**
     * 删除企业岗位信息信息
     * 
     * @param postId 企业岗位信息ID
     * @return 结果
     */
    @Override
    public int deleteImsEnterprisePostById(Long postId)
    {
        return imsEnterprisePostMapper.deleteImsEnterprisePostById(postId);
    }
}
