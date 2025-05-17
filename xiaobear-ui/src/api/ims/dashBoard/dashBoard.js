import request from '@/utils/request'

// 查询首页plate
export function dashBoard(query) {
  return request({
    url: '/dashBoard/plate',
    method: 'get',
    params: query
  })
}

// 查询首页论文plate
export function dashThesisBoard(query) {
  return request({
    url: '/dashBoard/thesis/plate',
    method: 'get',
    params: query
  })
}


// 查询首页-- 我的任务
export function dashBoardTask(query) {
  return request({
    url: '/dashBoard/task/mine',
    method: 'get',
    params: query
  })
}

// 查询首页-- 最新动态
export function dashBoardNotice(query) {
  return request({
    url: '/dashBoard/notice/mine',
    method: 'get',
    params: query
  })
}
