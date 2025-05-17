import request from '@/utils/request'

// 查询审批明细信息列表
export function listApproval(query) {
  return request({
    url: '/ims/approval/list',
    method: 'get',
    params: query
  })
}

// 查询审批明细信息详细
export function getApproval(approvalId) {
  return request({
    url: '/ims/approval/' + approvalId,
    method: 'get'
  })
}

// 新增审批明细信息
export function addApproval(data) {
  return request({
    url: '/ims/approval',
    method: 'post',
    data: data
  })
}

// 修改审批明细信息
export function updateApproval(data) {
  return request({
    url: '/ims/approval',
    method: 'put',
    data: data
  })
}

// 删除审批明细信息
export function delApproval(approvalId) {
  return request({
    url: '/ims/approval/' + approvalId,
    method: 'delete'
  })
}

// 导出审批明细信息
export function exportApproval(query) {
  return request({
    url: '/ims/approval/export',
    method: 'get',
    params: query
  })
}
