import request from '@/utils/request'

// 查询企业信息列表
export function listInfo(query) {
  return request({
    url: '/ims/info/list',
    method: 'get',
    params: query
  })
}
// 查询企业ID和企业名信息列表
export function companyListInfo(query) {
  return request({
    url: '/ims/info/company/list',
    method: 'get',
    params: query
  })
}

// 查询企业信息详细
export function getInfo(companyId) {
  return request({
    url: '/ims/info/' + companyId,
    method: 'get'
  })
}

// 新增企业信息
export function addInfo(data) {
  return request({
    url: '/ims/info',
    method: 'post',
    data: data
  })
}

// 修改企业信息
export function updateInfo(data) {
  return request({
    url: '/ims/info',
    method: 'put',
    data: data
  })
}

// 删除企业信息
export function delInfo(companyId) {
  return request({
    url: '/ims/info/' + companyId,
    method: 'delete'
  })
}

// 导出企业信息
export function exportInfo(query) {
  return request({
    url: '/ims/info/export',
    method: 'get',
    params: query
  })
}
