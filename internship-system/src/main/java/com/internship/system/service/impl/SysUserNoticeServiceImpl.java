package com.internship.system.service.impl;

import com.internship.common.constant.UserConstants;
import com.internship.system.domain.SysUserNotice;
import com.internship.system.mapper.SysUserNoticeMapper;
import com.internship.system.service.ISysUserNoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户与公告关联信息Service业务层处理
 * 
 * @author internship
 * @date 2020-12-31
 */
@Service
public class SysUserNoticeServiceImpl implements ISysUserNoticeService 
{
    @Resource
    private SysUserNoticeMapper sysUserNoticeMapper;

    /**
     * 查询用户与公告关联信息
     * 
     * @param userId 用户与公告关联信息ID
     * @return 用户与公告关联信息
     */
    @Override
    public SysUserNotice selectSysUserNoticeById(Long userId)
    {
        return sysUserNoticeMapper.selectSysUserNoticeById(userId);
    }

    /**
     * 查询用户与公告关联信息列表
     * 
     * @param sysUserNotice 用户与公告关联信息
     * @return 用户与公告关联信息
     */
    @Override
    public List<SysUserNotice> selectSysUserNoticeList(SysUserNotice sysUserNotice)
    {
        return sysUserNoticeMapper.selectSysUserNoticeList(sysUserNotice);
    }

    /**
     * 新增用户与公告关联信息
     * 
     * @param sysUserNotice 用户与公告关联信息
     * @return 结果
     */
    @Override
    public int insertSysUserNotice(SysUserNotice sysUserNotice)
    {
        return sysUserNoticeMapper.insertSysUserNotice(sysUserNotice);
    }

    /**
     * 修改用户与公告关联信息
     * 
     * @param sysUserNotice 用户与公告关联信息
     * @return 结果
     */
    @Override
    public int updateSysUserNotice(SysUserNotice sysUserNotice)
    {
        sysUserNotice.setIsRead(UserConstants.IS_READ_YES);
        return sysUserNoticeMapper.updateSysUserNotice(sysUserNotice);
    }

    /**
     * 批量删除用户与公告关联信息
     * 
     * @param userIds 需要删除的用户与公告关联信息ID
     * @return 结果
     */
    @Override
    public int deleteSysUserNoticeByIds(Long[] userIds)
    {
        return sysUserNoticeMapper.deleteSysUserNoticeByIds(userIds);
    }

    /**
     * 删除用户与公告关联信息信息
     * 
     * @param userId 用户与公告关联信息ID
     * @return 结果
     */
    @Override
    public int deleteSysUserNoticeById(Long userId)
    {
        return sysUserNoticeMapper.deleteSysUserNoticeById(userId);
    }
}
