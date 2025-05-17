import request from '@/utils/request'

// 查询开题报告信息列表
export function listReport(query) {
  return request({
    url: '/thesis/report/list',
    method: 'get',
    params: query
  })
}

// 查询开题报告信息详细
export function getReport(reportId) {
  return request({
    url: '/thesis/report/' + reportId,
    method: 'get'
  })
}

// 新增开题报告信息
export function addReport(data) {
  return request({
    url: '/thesis/report',
    method: 'post',
    data: data
  })
}

// 修改开题报告信息
export function updateReport(data) {
  return request({
    url: '/thesis/report',
    method: 'put',
    data: data
  })
}

// 删除开题报告信息
export function delReport(reportId) {
  return request({
    url: '/thesis/report/' + reportId,
    method: 'delete'
  })
}

// 导出开题报告信息
export function exportReport(query) {
  return request({
    url: '/thesis/report/export',
    method: 'get',
    params: query
  })
}