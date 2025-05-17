import request from '@/utils/request'

// 查询实习成绩信息列表
export function listGrade(query) {
  return request({
    url: '/practice/grade/list',
    method: 'get',
    params: query
  })
}

// 查询实习成绩信息详细
export function getGrade(gradeId) {
  return request({
    url: '/practice/grade/' + gradeId,
    method: 'get'
  })
}

// 新增实习成绩信息
export function addGrade(data) {
  return request({
    url: '/practice/grade',
    method: 'post',
    data: data
  })
}

// 修改实习成绩信息
export function updateGrade(data) {
  return request({
    url: '/practice/grade',
    method: 'put',
    data: data
  })
}

// 删除实习成绩信息
export function delGrade(gradeId) {
  return request({
    url: '/practice/grade/' + gradeId,
    method: 'delete'
  })
}

// 导出实习成绩信息
export function exportGrade(query) {
  return request({
    url: '/practice/grade/export',
    method: 'get',
    params: query
  })
}