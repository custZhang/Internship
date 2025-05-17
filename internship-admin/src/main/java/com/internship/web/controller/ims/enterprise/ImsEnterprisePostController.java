package com.internship.web.controller.ims.enterprise;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.enterprise.domain.ImsEnterprisePost;
import com.internship.enterprise.domain.vo.RecruitmentInfoVo;
import com.internship.enterprise.service.IImsEnterprisePostService;
import com.internship.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 企业岗位信息Controller
 * 
 * @author internship
 * @date 2020-11-17
 */
@RestController
@RequestMapping("/ims/post")
public class ImsEnterprisePostController extends BaseController
{
    @Resource
    private IImsEnterprisePostService imsEnterprisePostService;

    @Resource
    private ISysUserService iSysUserService;
    /**
     * 查询企业岗位信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsEnterprisePost imsEnterprisePost)
    {
        startPage();
        List<ImsEnterprisePost> list = imsEnterprisePostService.selectImsEnterprisePostList(imsEnterprisePost);
        return getDataTable(list);
    }

    /**
     * 查询招聘信息列表
     */
    @GetMapping("/rec/list")
    public TableDataInfo list(RecruitmentInfoVo recruitmentInfoVo)
    {
        List<RecruitmentInfoVo> list = imsEnterprisePostService.selectEnterpriseWithPostList(recruitmentInfoVo);
        return getDataTable(list);
    }

    /**
     * 获取企业岗位信息详细信息
     */
    @GetMapping(value = "/rec/{postId}")
    public AjaxResult getRecInfo(@PathVariable("postId") Long postId)
    {
        return AjaxResult.success(imsEnterprisePostService.selectEnterpriseWithPostByPostId(postId));
    }

    /**
     * 导出企业岗位信息列表
     */
    @Log(title = "企业岗位信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsEnterprisePost imsEnterprisePost)
    {
        List<ImsEnterprisePost> list = imsEnterprisePostService.selectImsEnterprisePostList(imsEnterprisePost);
        ExcelUtil<ImsEnterprisePost> util = new ExcelUtil<ImsEnterprisePost>(ImsEnterprisePost.class);
        return util.exportExcel(list, "post");
    }

    /**
     * 获取企业岗位信息详细信息
     */
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable("postId") Long postId)
    {
        return AjaxResult.success(imsEnterprisePostService.selectImsEnterprisePostById(postId));
    }

    /**
     * 新增企业岗位信息
     */
    @Log(title = "企业岗位信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsEnterprisePost imsEnterprisePost)
    {
        SysUser user = iSysUserService.selectUserByUserName(SecurityUtils.getUsername());   //用户信息
        imsEnterprisePost.setCreateDept(user.getDept().getDeptName());   //创建院系
        imsEnterprisePost.setCreateBy(user.getUserName());               //创建者
        return toAjax(imsEnterprisePostService.insertImsEnterprisePost(imsEnterprisePost));
    }

    /**
     * 修改企业岗位信息
     */
    @Log(title = "企业岗位信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsEnterprisePost imsEnterprisePost)
    {
        imsEnterprisePost.setUpdateBy(SecurityUtils.getUsername());      //更新者
        return toAjax(imsEnterprisePostService.updateImsEnterprisePost(imsEnterprisePost));
    }

    /**
     * 删除企业岗位信息
     */
    @Log(title = "企业岗位信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{postIds}")
    public AjaxResult remove(@PathVariable Long[] postIds)
    {
        return toAjax(imsEnterprisePostService.deleteImsEnterprisePostByIds(postIds));
    }
}
