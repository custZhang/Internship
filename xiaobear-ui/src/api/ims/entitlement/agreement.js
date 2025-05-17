import request from '@/utils/request'

// 查询实习协议信息列表
export function listAgreement(query) {
  return request({
    url: '/ims/agreement/list',
    method: 'get',
    params: query
  })
}

// 查询实习协议信息详细
export function getAgreement(iaId) {
  return request({
    url: '/ims/agreement/' + iaId,
    method: 'get'
  })
}

// 新增实习协议信息
export function addAgreement(data) {
  return request({
    url: '/ims/agreement',
    method: 'post',
    data: data
  })
}

// 修改实习协议信息
export function updateAgreement(data) {
  return request({
    url: '/ims/agreement',
    method: 'put',
    data: data
  })
}

// 删除实习协议信息
export function delAgreement(iaId) {
  return request({
    url: '/ims/agreement/' + iaId,
    method: 'delete'
  })
}

// 导出实习协议信息
export function exportAgreement(query) {
  return request({
    url: '/ims/agreement/export',
    method: 'get',
    params: query
  })
}