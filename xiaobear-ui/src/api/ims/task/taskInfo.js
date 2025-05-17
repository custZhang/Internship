import request from '@/utils/request'

// 查询任务信息列表
export function listTaskInfo(query) {
  return request({
    url: '/ims/taskInfo/list',
    method: 'get',
    params: query
  })
}

// 查询任务信息详细
export function getTaskInfo(taskId) {
  return request({
    url: '/ims/taskInfo/' + taskId,
    method: 'get'
  })
}

// 新增任务信息
export function addTaskInfo(data) {
  return request({
    url: '/ims/taskInfo',
    method: 'post',
    data: data
  })
}

// 修改任务信息
export function updateTaskInfo(data) {
  return request({
    url: '/ims/taskInfo',
    method: 'put',
    data: data
  })
}

// 删除任务信息
export function delTaskInfo(taskId) {
  return request({
    url: '/ims/taskInfo/' + taskId,
    method: 'delete'
  })
}

// 导出任务信息
export function exportTaskInfo(query) {
  return request({
    url: '/ims/taskInfo/export',
    method: 'get',
    params: query
  })
}