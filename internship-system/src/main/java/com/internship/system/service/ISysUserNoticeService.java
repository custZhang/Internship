package com.internship.system.service;

import com.internship.system.domain.SysUserNotice;

import java.util.List;

/**
 * 用户与公告关联信息Service接口
 * 
 * @author internship
 * @date 2020-12-31
 */
public interface ISysUserNoticeService 
{
    /**
     * 查询用户与公告关联信息
     * 
     * @param userId 用户与公告关联信息ID
     * @return 用户与公告关联信息
     */
    public SysUserNotice selectSysUserNoticeById(Long userId);

    /**
     * 查询用户与公告关联信息列表
     * 
     * @param sysUserNotice 用户与公告关联信息
     * @return 用户与公告关联信息集合
     */
    public List<SysUserNotice> selectSysUserNoticeList(SysUserNotice sysUserNotice);

    /**
     * 新增用户与公告关联信息
     * 
     * @param sysUserNotice 用户与公告关联信息
     * @return 结果
     */
    public int insertSysUserNotice(SysUserNotice sysUserNotice);

    /**
     * 修改用户与公告关联信息
     * 
     * @param sysUserNotice 用户与公告关联信息
     * @return 结果
     */
    public int updateSysUserNotice(SysUserNotice sysUserNotice);

    /**
     * 批量删除用户与公告关联信息
     * 
     * @param userIds 需要删除的用户与公告关联信息ID
     * @return 结果
     */
    public int deleteSysUserNoticeByIds(Long[] userIds);

    /**
     * 删除用户与公告关联信息信息
     * 
     * @param userId 用户与公告关联信息ID
     * @return 结果
     */
    public int deleteSysUserNoticeById(Long userId);
}
