import request from '@/utils/request'

// 查询实习周记信息列表 -->学生
export function listWeekly(query) {
  return request({
    url: '/practice/weekly/list',
    method: 'get',
    params: query
  })
}
// 查询实习考核信息详细
export function getWeekly(weeklyId) {
  return request({
    url: '/practice/weekly/' + weeklyId,
    method: 'get'
  })
}

// 新增实习考核信息
export function addWeekly(data) {
  return request({
    url: '/practice/weekly',
    method: 'post',
    data: data
  })
}

// 修改实习考核信息
export function updateWeekly(data) {
  return request({
    url: '/practice/weekly',
    method: 'put',
    data: data
  })
}

// 删除实习考核信息
export function delWeekly(weeklyId) {
  return request({
    url: '/practice/weekly/' + weeklyId,
    method: 'delete'
  })
}

// 导出实习考核信息
export function exportWeekly(query) {
  return request({
    url: '/practice/weekly/export',
    method: 'get',
    params: query
  })
}
