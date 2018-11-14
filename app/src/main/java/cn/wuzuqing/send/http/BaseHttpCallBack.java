package cn.wuzuqing.send.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseHttpCallBack<T> implements IHttpCallBack {
    protected boolean needToastError;
    protected boolean needToastSuccess;
    protected String msg;

    public BaseHttpCallBack() {

    }

    public BaseHttpCallBack(boolean needToastError, boolean needToastSuccess) {
        this.needToastError = needToastError;
        this.needToastSuccess = needToastSuccess;
    }

    public BaseHttpCallBack(boolean needToastError) {
        this.needToastError = needToastError;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSuccessStr(String resultStr) {
        try {
            if (TextUtils.isEmpty(resultStr)) {
                onFailed(-1, "请求失败");
                return;
            }
            JSONObject bodyObject = JSON.parseObject(resultStr);
            msg = bodyObject.getString("msg");
            int code = bodyObject.getIntValue("code");
            if (code == 100) {
                parseData(bodyObject.getString("data"), code, msg);
            } else {
                onFailed(code, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFinish() {

    }

    protected void parseData(String data, int code, String msg) {
        if (!TextUtils.isEmpty(data)) {
            Type type = this.getClass().getGenericSuperclass();
            T result =   JSON.parseObject(data, ((ParameterizedType) type).getActualTypeArguments()[0]);
            if (result != null) {
                onSuccess(result);
                onFinish();
            } else {
                onFailed(code, msg);
            }
        } else {
            onFailed(code, msg);
        }
    }


    public abstract void onSuccess(@NonNull T result);

    public void emptyData() {

    }

    @Override
    public void onFailed(int what, String msg) {
        LogUtils.d("what:" + what + " msg:" + msg);
        onFinish();
        if (needToastError ) {
            showToast(msg);
        }
    }

    public void showToast(String msg){
//        ToastUtils.showShort(msg);
    }
}
