import request from '@/utils/request'
/**
 * 单文件上传
 * @param {*} fileName  文件名,非必传
 *            file      文件
 */
export function upload( param) {
  //multipart/form-data   application/x-www-form-urlencoded
  return request({
    url: '/ims/file/upload',
    method: 'post',
    data: param,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 0
  })
}

/**
 * 文件下载
 * @param fileId
 * @returns {AxiosPromise}
 */
export function download(fileId) {
  return request({
    url: '/ims/file/download/'+fileId,
    method: 'get',
    responseType: 'blob',
  })
}
