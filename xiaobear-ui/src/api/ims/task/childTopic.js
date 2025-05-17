import request from '@/utils/request'

// 查询子任务信息列表
export function listChildTopic(query) {
  return request({
    url: '/ims/child/topic/list',
    method: 'get',
    params: query
  })
}
