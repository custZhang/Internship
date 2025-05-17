import request from '@/utils/request'

// 查询任务与班级用户关联信息列表
export function listAssignment(query) {
  return request({
    url: '/ims/assignment/list',
    method: 'get',
    params: query
  })
}

// 查询任务与班级用户关联信息详细
export function getAssignment(taskId) {
  return request({
    url: '/ims/assignment/' + taskId,
    method: 'get'
  })
}

// 新增任务与班级用户关联信息
export function addTaskDept(data) {
  return request({
    url: '/ims/assignment',
    method: 'post',
    data: data
  })
}

// 修改任务与班级用户关联信息
export function updateAssignment(data) {
  return request({
    url: '/ims/assignment',
    method: 'put',
    data: data
  })
}

// 删除任务与班级用户关联信息
export function delAssignment(taskId) {
  return request({
    url: '/ims/assignment/' + taskId,
    method: 'delete'
  })
}

// 导出任务与班级用户关联信息
export function exportAssignment(query) {
  return request({
    url: '/ims/assignment/export',
    method: 'get',
    params: query
  })
}
