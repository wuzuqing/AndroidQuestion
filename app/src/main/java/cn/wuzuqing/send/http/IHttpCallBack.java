package cn.wuzuqing.send.http;

public interface IHttpCallBack {

    void onStart();

    void onSuccessStr(String result);

    void onFailed(int what, String msg);
}
