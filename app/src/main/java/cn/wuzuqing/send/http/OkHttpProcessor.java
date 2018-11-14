package cn.wuzuqing.send.http;

import android.os.Handler;
import cn.wuzuqing.send.http.download.DownloadInfo;
import cn.wuzuqing.send.http.download.DownloadState;
import cn.wuzuqing.send.http.download.DownloadUtil;
import cn.wuzuqing.send.http.download.IDownloadCallBack;
import com.alibaba.fastjson.JSON;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 吴祖清
 * @CreateDate: 2018/10/25 11:40
 * @Description:
 * @UpdateUser: 吴祖清
 * @UpdateDate: 2018/10/25 11:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OkHttpProcessor implements IHttpProcessor {
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    enum Method {
        GET, POST, UPLOAD
    }

    public static SSLSocketFactory initSSLSocketFactory() {
        //2.生成SSLContext(加密上下文)
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL"); //与服务器保持一致，SSL算法或者TSL算法。
            X509TrustManager[] xTrustArray = new X509TrustManager[]
                    {initTrustManager()};
            sslContext.init(null,
                    xTrustArray, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }


    public static X509TrustManager initTrustManager() {
        X509TrustManager mTrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

        };
        return mTrustManager;
    }

    public OkHttpProcessor() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                //支持所有类型https请求
                return true;
            }
        });
        builder.sslSocketFactory(initSSLSocketFactory(), initTrustManager());

        mOkHttpClient = builder.build();
        mHandler = new Handler();


    }


    private void doAction(Object tag, Method method, final String url, Map<String, Object> params, List<File> files, final IHttpCallBack callBack) {
        Request.Builder builder = new Request.Builder().url(url);
        switch (method) {
            case GET:
//                MediaType parse = MediaType.parse("application/x-www-form-urlencoded");
                builder.get();
                break;
            case POST:
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                String bodyStr = JSON.toJSONString(params);
                RequestBody requestBody = RequestBody.create(mediaType, bodyStr);
                builder.post(requestBody);
//                FormBody.Builder formBodyBuild = new FormBody.Builder();
//                Set<String> postKeySet = params.keySet();
//                Object value;
//                for (String key : postKeySet) {
//                    value = params.get(key);
//                    if (value != null) {
//                        formBodyBuild.add(key, params.get(key).toString());
//                    }
//                }
//                builder.post(formBodyBuild.build());
                break;
            case UPLOAD:
                MultipartBody.Builder fileRequestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                RequestBody fileBody;
                for (File file : files) {
                    fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                    fileRequestBody.addFormDataPart("file", file.getAbsolutePath(), fileBody);
                }
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    fileRequestBody.addFormDataPart(key, params.get(key).toString());
                }
                builder.post(fileRequestBody.build());
                break;
            default:
        }
        callBack.onStart();
        if (tag != null) {
            builder.tag(tag);
        }
        mOkHttpClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(-1, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                LogUtils.d(url, result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccessStr(result);
                    }
                });
            }
        });
    }

    @Override
    public void post(Object tag, String url, Map<String, Object> params, final IHttpCallBack callBack) {
        doAction(tag, Method.POST, url, params, null, callBack);

    }

    @Override
    public void get(Object tag, String url, Map<String, Object> params, final IHttpCallBack callBack) {
        doAction(tag, Method.GET, url, params, null, callBack);
    }

    @Override
    public void upload(String url, Map<String, Object> params, IHttpCallBack callBack, File... files) {
        doAction(null, Method.UPLOAD, url, params, Arrays.asList(files), callBack);
    }

    @Override
    public void download(DownloadInfo downloadInfo, IDownloadCallBack listener) {
        String url = downloadInfo.getUrl();
        if (downloadInfo.getProgress() == downloadInfo.getTotal()) {
            if (listener != null) {
                listener.onSuccessStr("您已下载该文件");
            }
            downloadInfo.setState(DownloadState.SUCCESS);
            return;
        } else if (downloadInfo.getProgress() == 0) {
            if (listener != null) {
                listener.onStart();
            }
        }
        Request request = new Request.Builder()
                //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                .addHeader("RANGE", "bytes=" + downloadInfo.getProgress() + "-" + downloadInfo.getTotal())
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        try {
            Response response = call.execute();
            DownloadUtil.parseInputStream(downloadInfo, response.body().byteStream(), listener);
        } catch (IOException e) {
            e.printStackTrace();
            downloadInfo.setState(DownloadState.FAILED);
            if (listener != null) {
                listener.onFailed(-1, e.getMessage());
            }
        }
    }

    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    @Override
    public long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength == 0 ? DownloadInfo.TOTAL_ERROR : contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DownloadInfo.TOTAL_ERROR;
    }


    /**
     * 根据Tag取消请求
     */
    @Override
    public void cancelTag(Object tag) {
        cancelTag(mOkHttpClient, tag);
    }

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) {
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    @Override
    public void cancelAll() {
        cancelAll(mOkHttpClient);
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll(OkHttpClient client) {
        if (client == null) {
            return;
        }
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

}
