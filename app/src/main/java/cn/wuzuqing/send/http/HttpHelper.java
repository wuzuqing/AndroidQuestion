package cn.wuzuqing.send.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import cn.wuzuqing.send.MyApplication;
import cn.wuzuqing.send.http.download.DownloadState;
import cn.wuzuqing.send.http.download.DownloadUtil;
import cn.wuzuqing.send.http.download.IDownloadCallBack;

import java.io.File;
import java.net.NetworkInterface;
import java.util.*;


/**
 * @author: 吴祖清
 * @createDate: 2018/11/7 16:27
 * @description:
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/7 16:27
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public class HttpHelper {

    private static HttpHelper _INSTANCE;
    private static IHttpProcessor sIHttpProcessor;
    private String baseUrl;

    private HttpHelper() {
    }


    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static HttpHelper obtain() {
        if (_INSTANCE == null) {
            synchronized (HttpHelper.class) {
                if (_INSTANCE == null) {
                    _INSTANCE = new HttpHelper();
                }
            }
        }
        return _INSTANCE;
    }

    public static void init(IHttpProcessor processor) {
        sIHttpProcessor = processor;
    }


    public void get(String url, Consumer<Map<String, Object>> createParams, IHttpCallBack callBack) {
        get(null, url, createParams, callBack);
    }

    public void get(Object tag, String url, Consumer<Map<String, Object>> createParams, IHttpCallBack callBack) {
        if (!NetworkUtils.isConnected()) {
            callBack.onFailed(-1000, "请求失败,请检查您的网络是否存在异常");
            return;
        } else if (isWifiProxy()) {
            callBack.onFailed(-1000, "请求失败,请检查您的网络是否存在异常");
            return;
        }
//        }else if ()
        url = getRealUrl(url);
        Map<String, Object> params = defaultParams(createParams);
        url = tranUrl(url, params);
        LogUtils.d(url, params.toString());
        sIHttpProcessor.get(tag, url, null, callBack);
    }

    public void post(String url, Consumer<Map<String, Object>> createParams, IHttpCallBack callBack) {
        post(null, url, createParams, callBack);
    }

    public void post(Object tag, String url, Consumer<Map<String, Object>> createParams, IHttpCallBack callBack) {
        if (!NetworkUtils.isConnected()) {
            callBack.onFailed(-1000, "请求失败,请检查您的网络是否存在异常");
            return;
        } else if (isWifiProxy()) {
            callBack.onFailed(-1000, "请求失败,请检查您的网络是否存在异常");
            return;
        }
        url = getRealUrl(url);
        Map<String, Object> params = defaultParams(createParams);
        LogUtils.d(url, params.toString());
        sIHttpProcessor.post(tag, url, params, callBack);
    }


    public void cancelTag(Object tag) {
        sIHttpProcessor.cancelTag(tag);
    }

    public void cancelAll() {
        sIHttpProcessor.cancelAll();
    }


    /**
     * 判断设备 是否使用代理(WiFi状态下的,避免被抓包)
     */
    private boolean isWifiProxy() {
        final boolean IS_ICS_OR_LATER = true;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(MyApplication.getApp());
            proxyPort = android.net.Proxy.getPort(MyApplication.getApp());
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 是否正在使用VPN
     */
    private static boolean isVpnUsed() {
        try {
            Enumeration niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                ArrayList<NetworkInterface> list = Collections.list(niList);
                for (NetworkInterface intf : list) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    Log.d("-----", "isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        // The VPN is up
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 上传图片
     *
     * @param url
     * @param createParams
     * @param callBack
     * @param files
     */
    public void upload(String url, Consumer<Map<String, Object>> createParams, IHttpCallBack callBack, File... files) {
        if (isWifiProxy()) {
            callBack.onFailed(-1000, "请求失败,请检查您的网络是否存在异常");
            return;
        }
        url = getRealUrl(url);
        Map<String, Object> params = defaultParams(createParams);
        LogUtils.d(url, params.toString());
        sIHttpProcessor.upload(url, params, callBack, files);
    }

    /**
     * 获取下载长度
     *
     * @param downloadUrl 下载路径
     * @return
     */
    public long getContentLength(String downloadUrl) {
        return sIHttpProcessor.getContentLength(downloadUrl);
    }

    public void download(final String url, final DownloadState state, final IDownloadCallBack downloadCallBack) {
        if (state == DownloadState.DELETE) {
            DownloadUtil.get().cancel(url);
            return;
        }

//        ThreadManager.INSTANCE.getFixThread().execute(new Runnable() {
//            @Override
//            public void run() {
//                DownloadInfo info = DownloadUtil.get().getDownloadInfo(url);
//                switch (state) {
//                    case START:
//                        if (info.getState() != DownloadState.START) {
//                            info.setState(state);
//                            sIHttpProcessor.download(info, downloadCallBack);
//                        }
//                        break;
//                    case PAUSE:
//                        info.setState(state);
//                        break;
//                    default:
//                }
//            }
//        });

    }

    @NonNull
    private Map<String, Object> defaultParams(Consumer<Map<String, Object>> createParams) {
        Map<String, Object> params = new HashMap<>();
        params.put("device", "android");

        try {
            if (createParams != null) {
                createParams.accept(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return params;
    }

    private String getRealUrl(String url) {
        if (!TextUtils.isEmpty(baseUrl)) {
            url = String.format("%s%s", baseUrl, url);
        }
        return url;
    }

    private String tranUrl(String url, Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            Set<String> keySet = params.keySet();
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            for (String key : keySet) {
                if (params.get(key) != null) {
                    sb.append(key).append("=").append(params.get(key)).append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return url;
    }
}
