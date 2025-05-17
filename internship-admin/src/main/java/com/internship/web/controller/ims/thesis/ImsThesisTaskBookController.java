package com.internship.web.controller.ims.thesis;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.common.utils.poi.ExcelUtil;
import com.internship.thesis.domain.ImsThesisTaskBook;
import com.internship.thesis.service.IImsThesisTaskBookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务书信息Controller
 * 
 * @author internship
 * @date 2020-12-18
 */
@RestController
@RequestMapping("/thesis/book")
public class ImsThesisTaskBookController extends BaseController
{
    @Resource
    private IImsThesisTaskBookService imsThesisTaskBookService;

    /**
     * 查询任务书信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ImsThesisTaskBook imsThesisTaskBook)
    {
        startPage();
        List<ImsThesisTaskBook> list = imsThesisTaskBookService.selectImsThesisTaskBookList(imsThesisTaskBook);
        return getDataTable(list);
    }

    /**
     * 导出任务书信息列表
     */
    @Log(title = "任务书信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ImsThesisTaskBook imsThesisTaskBook)
    {
        List<ImsThesisTaskBook> list = imsThesisTaskBookService.selectImsThesisTaskBookList(imsThesisTaskBook);
        ExcelUtil<ImsThesisTaskBook> util = new ExcelUtil<ImsThesisTaskBook>(ImsThesisTaskBook.class);
        return util.exportExcel(list, "book");
    }

    /**
     * 获取任务书信息详细信息
     */
    @GetMapping(value = "/{taskBookId}")
    public AjaxResult getInfo(@PathVariable("taskBookId") Long taskBookId)
    {
        return AjaxResult.success(imsThesisTaskBookService.selectImsThesisTaskBookById(taskBookId));
    }

    /**
     * 新增任务书信息
     */
    @Log(title = "任务书信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsThesisTaskBook imsThesisTaskBook)
    {
        return toAjax(imsThesisTaskBookService.insertImsThesisTaskBook(imsThesisTaskBook));
    }

    /**
     * 修改任务书信息
     */
    @Log(title = "任务书信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsThesisTaskBook imsThesisTaskBook)
    {
        return toAjax(imsThesisTaskBookService.updateImsThesisTaskBook(imsThesisTaskBook));
    }

    /**
     * 删除任务书信息
     */
    @Log(title = "任务书信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{taskBookIds}")
    public AjaxResult remove(@PathVariable Long[] taskBookIds)
    {
        return toAjax(imsThesisTaskBookService.deleteImsThesisTaskBookByIds(taskBookIds));
    }
}
