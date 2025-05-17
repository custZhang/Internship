package com.internship.web.controller.ims.practice;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.domain.entity.SysUser;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.SecurityUtils;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.practice.domain.ImsPractice;
import com.internship.practice.domain.vo.ImsInternInstructorVo;
import com.internship.practice.service.IImsPracticeService;
import com.internship.system.service.ISysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实习信息Controller
 * 
 * @author internship
 * @date 2020-11-20
 */
@RestController
@RequestMapping("/ims/practice")
public class ImsPracticeController extends BaseController
{
    @Resource
    private IImsPracticeService imsPracticeService;

    @Resource
    private ISysUserService sysUserService;

    /**
     * 查询实习信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsPractice imsPractice)
    {
        startPage();
        List<ImsPractice> list = imsPracticeService.selectImsPracticeList(imsPractice);
        return getDataTable(list);
    }

    /**
     * 查询个人实习信息列表
     */
    @GetMapping("/personList")
    public TableDataInfo personalList(@RequestParam(value = "practiceStatus",required = false)String practiceStatus)
    {
        startPage();
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        Long userId = user.getUserId();
        List<ImsPractice> list = imsPracticeService.selectImsPracticePersonalList(userId,practiceStatus);
        return getDataTable(list);
    }

    /**
     * 查询实习指导老师信息列表
     */
    @GetMapping("/intern/list")
    public TableDataInfo internInstructorList(ImsInternInstructorVo imsInstructorVo)
    {
        startPage();
        List<ImsInternInstructorVo> list = imsPracticeService.selectInternInstructorList(imsInstructorVo);
        return getDataTable(list);
    }

    /**
     * 导出实习信息列表
     */
    @Log(title = "实习信息", businessType = BusinessType.EXPORT)
    @GetMapping("/intern/export")
    public AjaxResult exportInternInstructorList(ImsInternInstructorVo imsInstructorVo)
    {
        List<ImsInternInstructorVo> list = imsPracticeService.exportInternInstructorList(imsInstructorVo);
        ExcelUtil<ImsInternInstructorVo> util = new ExcelUtil<ImsInternInstructorVo>(ImsInternInstructorVo.class);
        return util.exportExcel(list, "实习指导老师");
    }

    /**
     * 根据实习单位查询实习信息列表
     */
    @GetMapping("/stu/list")
    public AjaxResult selectStu(ImsInternInstructorVo imsInstructorVo){
        List<ImsInternInstructorVo> list = imsPracticeService.exportInternInstructorList(imsInstructorVo);
        return AjaxResult.success(list);
    }

    /**
     * 导出实习信息列表
     */
    @Log(title = "实习信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsPractice imsPractice)
    {
        List<ImsPractice> list = imsPracticeService.selectImsPracticeList(imsPractice);
        ExcelUtil<ImsPractice> util = new ExcelUtil<ImsPractice>(ImsPractice.class);
        return util.exportExcel(list, "practice");
    }

    /**
     * 获取实习信息详细信息
     */
    @GetMapping(value = "/{practiceId}")
    public AjaxResult getInfo(@PathVariable("practiceId") Long practiceId)
    {
        return AjaxResult.success(imsPracticeService.selectImsPracticeById(practiceId));
    }

    /**
     * 新增实习信息
     */
    @Log(title = "实习信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsPractice imsPractice)
    {
        SysUser user = sysUserService.selectUserByUserName(SecurityUtils.getUsername());
        Long userId = user.getUserId();
        String userName = user.getUserName();
        String deptName = user.getDept().getDeptName();
        imsPractice.setUserId(userId);
        imsPractice.setCreateBy(userName);
        imsPractice.setCreateDept(deptName);
        return toAjax(imsPracticeService.insertImsPractice(imsPractice));
    }

    /**
     * 修改实习信息
     */
    @Log(title = "实习信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsPractice imsPractice)
    {
        return toAjax(imsPracticeService.updateImsPractice(imsPractice));
    }

    /**
     * 删除实习信息
     */
    @Log(title = "实习信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{practiceIds}")
    public AjaxResult remove(@PathVariable Long[] practiceIds)
    {
        return toAjax(imsPracticeService.deleteImsPracticeByIds(practiceIds));
    }
}
