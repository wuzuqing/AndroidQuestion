package cn.wuzuqing.send.http.download;


import cn.wuzuqing.send.http.IHttpCallBack;

public interface IDownloadCallBack extends IHttpCallBack {
    /**
     * @param info 下载进度
     */
    void onDownloading(DownloadInfo info);
}
