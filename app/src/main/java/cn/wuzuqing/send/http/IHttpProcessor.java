package cn.wuzuqing.send.http;


import cn.wuzuqing.send.http.download.DownloadInfo;
import cn.wuzuqing.send.http.download.IDownloadCallBack;

import java.io.File;
import java.util.Map;

/**
 * @Author: 吴祖清
 * @CreateDate: 2018/10/25 11:51
 * @Description: 网络请求处理器
 * @UpdateUser: 吴祖清
 * @UpdateDate: 2018/10/25 11:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface IHttpProcessor {
    /**
     *  post请求
     * @param tag       标签
     * @param url       请求地址
     * @param params    请求参数
     * @param callBack  结果回调
     */
    void post(Object tag, String url, Map<String, Object> params, IHttpCallBack callBack);

    /**
     *  get请求
     * @param tag       标签
     * @param url       请求地址
     * @param params    请求参数
     * @param callBack  结果回调
     */
    void get(Object tag, String url, Map<String, Object> params, IHttpCallBack callBack);

    /**
     * 上传文件请求
     * @param url       上传地址
     * @param params    上传参数
     * @param callBack  结果回调
     * @param files     上传文件
     */
    void upload(String url, Map<String, Object> params, IHttpCallBack callBack, File... files);

    /**
     * 下载文件请求
     *
     * @param downloadInfo
     * @param listener
     */
    void download(DownloadInfo downloadInfo, final IDownloadCallBack listener);

    /**
     * 获取文件长度
     *
     * @param downloadUrl
     * @return
     */
    long getContentLength(String downloadUrl);

    /**
     * 根据tag取消网络请求
     *
     * @param tag
     */
    void cancelTag(Object tag);

    /**
     * 取消所有网络请求
     */
    void cancelAll();
}
