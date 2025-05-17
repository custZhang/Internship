import request from '@/utils/request'

// 查询任务书信息列表
export function listBook(query) {
  return request({
    url: '/thesis/book/list',
    method: 'get',
    params: query
  })
}

// 查询任务书信息详细
export function getBook(taskBookId) {
  return request({
    url: '/thesis/book/' + taskBookId,
    method: 'get'
  })
}

// 新增任务书信息
export function addBook(data) {
  return request({
    url: '/thesis/book',
    method: 'post',
    data: data
  })
}

// 修改任务书信息
export function updateBook(data) {
  return request({
    url: '/thesis/book',
    method: 'put',
    data: data
  })
}

// 删除任务书信息
export function delBook(taskBookId) {
  return request({
    url: '/thesis/book/' + taskBookId,
    method: 'delete'
  })
}

// 导出任务书信息
export function exportBook(query) {
  return request({
    url: '/thesis/book/export',
    method: 'get',
    params: query
  })
}