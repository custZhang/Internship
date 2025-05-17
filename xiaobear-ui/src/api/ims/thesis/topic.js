import request from '@/utils/request'

// 查询课题信息列表
export function listTopic(query) {
  return request({
    url: '/thesis/topic/list',
    method: 'get',
    params: query
  })
}

// 根据用户ID查询课题信息列表
export function topicByUserId(query) {
  return request({
    url: '/thesis/topic/userId',
    method: 'get',
    params: query
  })
}

// 查询课题信息详细
export function getTopic(topicId) {
  return request({
    url: '/thesis/topic/' + topicId,
    method: 'get'
  })
}

// 新增课题信息
export function addTopic(data) {
  return request({
    url: '/thesis/topic',
    method: 'post',
    data: data
  })
}

// 修改课题信息
export function updateTopic(data) {
  return request({
    url: '/thesis/topic',
    method: 'put',
    data: data
  })
}

// 删除课题信息
export function delTopic(topicId) {
  return request({
    url: '/thesis/topic/' + topicId,
    method: 'delete'
  })
}

// 导出课题信息
export function exportTopic(query) {
  return request({
    url: '/thesis/topic/export',
    method: 'get',
    params: query
  })
}
