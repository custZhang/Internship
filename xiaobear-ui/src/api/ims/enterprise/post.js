import request from '@/utils/request'

// 查询企业岗位信息列表
export function listPost(query) {
  return request({
    url: '/ims/post/list',
    method: 'get',
    params: query
  })
}

// 查询招聘信息列表
export function listRec(query) {
  return request({
    url: '/ims/post/rec/list',
    method: 'get',
    params: query
  })
}

// 查询企业岗位信息详细
export function getRecPost(postId) {
  return request({
    url: '/ims/post/rec/' + postId,
    method: 'get'
  })
}

// 查询企业岗位信息详细
export function getPost(postId) {
  return request({
    url: '/ims/post/' + postId,
    method: 'get'
  })
}

// 新增企业岗位信息
export function addPost(data) {
  return request({
    url: '/ims/post',
    method: 'post',
    data: data
  })
}

// 修改企业岗位信息
export function updatePost(data) {
  return request({
    url: '/ims/post',
    method: 'put',
    data: data
  })
}

// 删除企业岗位信息
export function delPost(postId) {
  return request({
    url: '/ims/post/' + postId,
    method: 'delete'
  })
}

// 导出企业岗位信息
export function exportPost(query) {
  return request({
    url: '/ims/post/export',
    method: 'get',
    params: query
  })
}
