import request from '@/utils/request'

// 查询论文中期检查信息列表
export function listInspection(query) {
  return request({
    url: '/thesis/inspection/list',
    method: 'get',
    params: query
  })
}

// 查询论文中期检查信息详细
export function getInspection(inspectId) {
  return request({
    url: '/thesis/inspection/' + inspectId,
    method: 'get'
  })
}

// 新增论文中期检查信息
export function addInspection(data) {
  return request({
    url: '/thesis/inspection',
    method: 'post',
    data: data
  })
}

// 修改论文中期检查信息
export function updateInspection(data) {
  return request({
    url: '/thesis/inspection',
    method: 'put',
    data: data
  })
}

// 删除论文中期检查信息
export function delInspection(inspectId) {
  return request({
    url: '/thesis/inspection/' + inspectId,
    method: 'delete'
  })
}

// 导出论文中期检查信息
export function exportInspection(query) {
  return request({
    url: '/thesis/inspection/export',
    method: 'get',
    params: query
  })
}