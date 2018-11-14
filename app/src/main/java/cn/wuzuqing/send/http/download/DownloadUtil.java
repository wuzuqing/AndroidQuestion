package cn.wuzuqing.send.http.download;

import cn.wuzuqing.send.http.FileUtils;
import cn.wuzuqing.send.http.HttpHelper;
import com.alibaba.fastjson.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DownloadUtil {
    private static DownloadUtil downloadUtil;

    private Map<String, DownloadInfo> mDownloadInfos;

    public DownloadUtil() {
        mDownloadInfos = new HashMap<>();
    }

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }


    public DownloadInfo getDownloadInfo(String url) {
        if (mDownloadInfos.containsKey(url)) {
            return mDownloadInfos.get(url);
        } else {
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.setUrl(url);
            String fileName = url.substring(url.lastIndexOf("/"));
            File file = new File(FileUtils.getDir("download"), fileName);
            downloadInfo.setFileName(file.getAbsolutePath());
            downloadInfo.setTotal(HttpHelper.obtain().getContentLength(url));
            if (file.exists()) {
                downloadInfo.setProgress(file.length());
            }
            mDownloadInfos.put(url, downloadInfo);
            return downloadInfo;
        }
    }

    public void cancel(String url) {
        if (mDownloadInfos.containsKey(url)) {
            DownloadInfo downloadInfo = mDownloadInfos.remove(url);
            downloadInfo.setState(DownloadState.DELETE);
            if (FileUtils.isFileExists(downloadInfo.getFileName())) {
                FileUtils.deleteFile(downloadInfo.getFileName());
            }
        }
    }

    public static void parseInputStream(DownloadInfo downloadInfo, InputStream is, IDownloadCallBack listener) throws IOException {
        long downloadLength = downloadInfo.getProgress();//已经下载好的长度
        long contentLength = downloadInfo.getTotal();//文件的总长度
        FileOutputStream fileOutputStream = null;
        File file = new File(downloadInfo.getFileName());
        fileOutputStream = new FileOutputStream(file, true);
        byte[] buffer = new byte[2048];//缓冲数组2kB
        int len;
        while ((len = is.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
            downloadLength += len;
            downloadInfo.setProgress(downloadLength);
            if (listener != null) {
                listener.onDownloading(downloadInfo);
            }
            if (downloadInfo.getState() != DownloadState.START) {
                break;
            }
        }
        if (downloadInfo.getProgress() == contentLength) {
            downloadInfo.setState(DownloadState.SUCCESS);
        }
        fileOutputStream.flush();
        IOUtils.close(is);
        IOUtils.close(fileOutputStream);
    }
}
