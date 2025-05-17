import request from '@/utils/request'

// 查询流程配置信息列表
export function listConfig(query) {
  return request({
    url: '/ims/config/list',
    method: 'get',
    params: query
  })
}

// 查询流程配置信息详细
export function getConfig(processId) {
  return request({
    url: '/ims/config/' + processId,
    method: 'get'
  })
}

// 新增流程配置信息
export function addConfig(data) {
  return request({
    url: '/ims/config',
    method: 'post',
    data: data
  })
}

// 修改流程配置信息
export function updateConfig(data) {
  return request({
    url: '/ims/config',
    method: 'put',
    data: data
  })
}

// 删除流程配置信息
export function delConfig(processId) {
  return request({
    url: '/ims/config/' + processId,
    method: 'delete'
  })
}

// 导出流程配置信息
export function exportConfig(query) {
  return request({
    url: '/ims/config/export',
    method: 'get',
    params: query
  })
}