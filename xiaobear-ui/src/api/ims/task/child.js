import request from '@/utils/request'

// 查询子任务信息列表
export function listChild(query) {
  return request({
    url: '/ims/child/list',
    method: 'get',
    params: query
  })
}

// 查询实习周记、总结子任务信息列表
export function listChildPractice(query) {
  return request({
    url: '/ims/child/practice/list',
    method: 'get',
    params: query
  })
}

// 查询实习申请子任务信息列表
export function listPracticeApplication(query) {
  return request({
    url: '/ims/child/application/list',
    method: 'get',
    params: query
  })
}

// 查询子任务信息详细
export function getChild(childId) {
  return request({
    url: '/ims/child/' + childId,
    method: 'get'
  })
}

// 新增子任务信息
export function addChild(data) {
  return request({
    url: '/ims/child',
    method: 'post',
    data: data
  })
}

// 修改子任务信息
export function updateChild(data) {
  return request({
    url: '/ims/child',
    method: 'put',
    data: data
  })
}

// 删除子任务信息
export function delChild(childId) {
  return request({
    url: '/ims/child/' + childId,
    method: 'delete'
  })
}

// 导出子任务信息
export function exportChild(query) {
  return request({
    url: '/ims/child/export',
    method: 'get',
    params: query
  })
}
