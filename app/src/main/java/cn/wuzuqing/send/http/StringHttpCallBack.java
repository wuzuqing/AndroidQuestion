package cn.wuzuqing.send.http;


import android.support.annotation.NonNull;
import android.text.TextUtils;

public abstract class StringHttpCallBack extends BaseHttpCallBack<String> {
    public StringHttpCallBack() {
    }

    public StringHttpCallBack(boolean needToastError) {
        super(needToastError);
    }

    @Override
    protected void parseData(String data, int code, String msg) {
        onSuccess(TextUtils.isEmpty(data)?msg:data, msg);
        onFinish();
        if (needToastError) {
           showToast(msg);
        }
    }

    @Override
    public void onSuccess(@NonNull String result) {

    }

    public abstract void onSuccess(String result, String msg);
}
