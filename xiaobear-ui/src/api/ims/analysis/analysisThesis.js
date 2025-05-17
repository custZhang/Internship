import request from '@/utils/request'

// 查询实习人数统计分析列表
export function thesisTopicAnalysis(query) {
  return request({
    url: '/ims/analysis/thesis',
    method: 'get',
    params: query
  })
}

// 查询实习周记统计分析列表
export function practiceWeeklyAnalysis(query) {
  return request({
    url: '/ims/analysis/practice/weekly',
    method: 'get',
    params: query
  })
}

// 查询实习总结统计分析列表
export function practiceSummaryAnalysis(query) {
  return request({
    url: '/ims/analysis/practice/summary',
    method: 'get',
    params: query
  })
}

// 查询实习工资统计分析列表
export function practiceWareAnalysis(query) {
  return request({
    url: '/ims/analysis/practice/ware',
    method: 'get',
    params: query
  })
}
