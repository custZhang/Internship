import request from '@/utils/request'

// 查询工资待遇信息列表
export function listBenefits(query) {
  return request({
    url: '/ims/benefits/list',
    method: 'get',
    params: query
  })
}

// 查询工资待遇信息详细
export function getBenefits(benefitsId) {
  return request({
    url: '/ims/benefits/' + benefitsId,
    method: 'get'
  })
}

// 新增工资待遇信息
export function addBenefits(data) {
  return request({
    url: '/ims/benefits',
    method: 'post',
    data: data
  })
}

// 修改工资待遇信息
export function updateBenefits(data) {
  return request({
    url: '/ims/benefits',
    method: 'put',
    data: data
  })
}

// 删除工资待遇信息
export function delBenefits(benefitsId) {
  return request({
    url: '/ims/benefits/' + benefitsId,
    method: 'delete'
  })
}

// 导出工资待遇信息
export function exportBenefits(query) {
  return request({
    url: '/ims/benefits/export',
    method: 'get',
    params: query
  })
}