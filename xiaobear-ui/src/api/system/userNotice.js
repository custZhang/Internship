import request from '@/utils/request'

// 查询用户与公告关联信息列表
export function listUserNotice(query) {
  return request({
    url: '/system/userNotice/list',
    method: 'get',
    params: query
  })
}

// 查询用户与公告关联信息详细
export function getUserNotice(userId) {
  return request({
    url: '/system/userNotice/' + userId,
    method: 'get'
  })
}

// 新增用户与公告关联信息
export function addUserNotice(data) {
  return request({
    url: '/system/userNotice',
    method: 'post',
    data: data
  })
}

// 修改用户与公告关联信息
export function updateUserNotice(data) {
  return request({
    url: '/system/userNotice',
    method: 'put',
    data: data
  })
}

// 删除用户与公告关联信息
export function delUserNotice(userId) {
  return request({
    url: '/system/userNotice/' + userId,
    method: 'delete'
  })
}

// 导出用户与公告关联信息
export function exportUserNotice(query) {
  return request({
    url: '/system/userNotice/export',
    method: 'get',
    params: query
  })
}