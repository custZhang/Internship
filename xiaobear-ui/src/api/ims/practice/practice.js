import request from '@/utils/request'

// 查询实习信息列表
export function listPractice(query) {
  return request({
    url: '/ims/practice/list',
    method: 'get',
    params: query
  })
}

// 查询实习信息列表
export function personalListPractice(query) {
  return request({
    url: '/ims/practice/personList',
    method: 'get',
    params: query
  })
}

// 查询实习指导老师信息列表
export function internInstructorList(query) {
  return request({
    url: '/ims/practice/intern/list',
    method: 'get',
    params: query
  })
}

// 根据实习单位查询实习信息
export function stuList(query) {
  return request({
    url: '/ims/practice/stu/list',
    method: 'get',
    params: query
  })
}

// 导出实习信息
export function exportInternInstructor(query) {
  return request({
    url: '/ims/practice/intern/export',
    method: 'get',
    params: query
  })
}

// 查询实习信息详细
export function getPractice(practiceId) {
  return request({
    url: '/ims/practice/' + practiceId,
    method: 'get'
  })
}

// 新增实习信息
export function addPractice(data) {
  return request({
    url: '/ims/practice',
    method: 'post',
    data: data
  })
}

// 修改实习信息
export function updatePractice(data) {
  return request({
    url: '/ims/practice',
    method: 'put',
    data: data
  })
}

// 删除实习信息
export function delPractice(practiceId) {
  return request({
    url: '/ims/practice/' + practiceId,
    method: 'delete'
  })
}

// 导出实习信息
export function exportPractice(query) {
  return request({
    url: '/ims/practice/export',
    method: 'get',
    params: query
  })
}
