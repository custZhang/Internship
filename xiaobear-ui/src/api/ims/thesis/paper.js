import request from '@/utils/request'

// 查询论文信息列表
export function listPaper(query) {
  return request({
    url: '/thesis/paper/list',
    method: 'get',
    params: query
  })
}

// 查询论文信息详细
export function getPaper(paperId) {
  return request({
    url: '/thesis/paper/' + paperId,
    method: 'get'
  })
}

// 新增论文信息
export function addPaper(data) {
  return request({
    url: '/thesis/paper',
    method: 'post',
    data: data
  })
}

// 修改论文信息
export function updatePaper(data) {
  return request({
    url: '/thesis/paper',
    method: 'put',
    data: data
  })
}

// 删除论文信息
export function delPaper(paperId) {
  return request({
    url: '/thesis/paper/' + paperId,
    method: 'delete'
  })
}

// 导出论文信息
export function exportPaper(query) {
  return request({
    url: '/thesis/paper/export',
    method: 'get',
    params: query
  })
}