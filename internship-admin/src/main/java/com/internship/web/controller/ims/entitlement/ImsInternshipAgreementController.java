package com.internship.web.controller.ims.entitlement;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.entitlement.domain.ImsInternshipAgreement;
import com.internship.entitlement.service.IImsInternshipAgreementService;
import com.internship.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实习协议信息Controller
 * 
 * @author internship
 * @date 2020-12-08
 */
@RestController
@RequestMapping("/ims/agreement")
public class ImsInternshipAgreementController extends BaseController
{
    @Resource
    private IImsInternshipAgreementService imsInternshipAgreementService;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 查询实习协议信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsInternshipAgreement imsInternshipAgreement)
    {
        startPage();
        List<ImsInternshipAgreement> list = imsInternshipAgreementService.selectImsInternshipAgreementList(imsInternshipAgreement);
        return getDataTable(list);
    }

    /**
     * 导出实习协议信息列表
     */
    @Log(title = "实习协议信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsInternshipAgreement imsInternshipAgreement)
    {
        List<ImsInternshipAgreement> list = imsInternshipAgreementService.selectImsInternshipAgreementList(imsInternshipAgreement);
        ExcelUtil<ImsInternshipAgreement> util = new ExcelUtil<ImsInternshipAgreement>(ImsInternshipAgreement.class);
        return util.exportExcel(list, "agreement");
    }

    /**
     * 获取实习协议信息详细信息
     */
    @GetMapping(value = "/{iaId}")
    public AjaxResult getInfo(@PathVariable("iaId") Long iaId)
    {
        return AjaxResult.success(imsInternshipAgreementService.selectImsInternshipAgreementById(iaId));
    }

    /**
     * 新增实习协议信息
     */
    @Log(title = "实习协议信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsInternshipAgreement imsInternshipAgreement)
    {
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        imsInternshipAgreement.setCreateDept(sysUser.getDept().getDeptName());
        imsInternshipAgreement.setCreateBy(sysUser.getUserName());
        return toAjax(imsInternshipAgreementService.insertImsInternshipAgreement(imsInternshipAgreement));
    }

    /**
     * 修改实习协议信息
     */
    @Log(title = "实习协议信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsInternshipAgreement imsInternshipAgreement)
    {
        return toAjax(imsInternshipAgreementService.updateImsInternshipAgreement(imsInternshipAgreement));
    }

    /**
     * 删除实习协议信息
     */
    @Log(title = "实习协议信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{iaIds}")
    public AjaxResult remove(@PathVariable Long[] iaIds)
    {
        return toAjax(imsInternshipAgreementService.deleteImsInternshipAgreementByIds(iaIds));
    }
}
