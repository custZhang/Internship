package com.internship.web.controller.ims.task;

import com.internship.common.annotation.Log;
import com.internship.common.core.controller.BaseController;
import com.internship.common.core.domain.AjaxResult;
import com.internship.common.core.page.TableDataInfo;
import com.internship.common.enums.BusinessType;
import com.internship.task.domain.ImsTaskChildTopic;
import com.internship.task.domain.vo.thesisVo.ImsThesisTopicChildVo;
import com.internship.task.service.IImsTaskChildTopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 论文子任务信息Controller
 * 
 * @author internship
 * @date 2021-03-15
 */
@RestController
@RequestMapping("/ims/child/topic")
public class ImsTaskChildTopicController extends BaseController
{
    @Resource
    private IImsTaskChildTopicService imsTaskChildService;


    @GetMapping("/list")
    public TableDataInfo topicList(ImsThesisTopicChildVo imsTaskChildTopicVo){
        startPage();
        List<ImsThesisTopicChildVo> voList = imsTaskChildService.selectThesisTopicList(imsTaskChildTopicVo);
        return getDataTable(voList);
    }

    /**
     * 获取子任务信息详细信息
     */
    @GetMapping(value = "/{childId}")
    public AjaxResult getInfo(@PathVariable("childId") Long childId)
    {
        return AjaxResult.success(imsTaskChildService.selectImsTaskChildTopicById(childId));
    }

    /**
     * 新增子任务信息
     */
    @Log(title = "子任务信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ImsTaskChildTopic imsTaskChild)
    {
        imsTaskChildService.insertImsTaskChildTopic(imsTaskChild);
        return AjaxResult.success();
    }

    /**
     * 修改子任务信息
     */
    @Log(title = "子任务信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ImsTaskChildTopic imsTaskChild)
    {
        return toAjax(imsTaskChildService.updateImsTaskChildTopic(imsTaskChild));
    }

    /**
     * 删除子任务信息
     */
    @Log(title = "子任务信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{childIds}")
    public AjaxResult remove(@PathVariable Long[] childIds)
    {
        return toAjax(imsTaskChildService.deleteImsTaskChildTopicByIds(childIds));
    }
}
