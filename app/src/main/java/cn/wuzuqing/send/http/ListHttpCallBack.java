package cn.wuzuqing.send.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author:         吴祖清
 * @createDate:     2018/11/7 18:39
 * @description:
 * @updateUser:     吴祖清
 * @updateDate:     2018/11/7 18:39
 * @updateRemark:   更新说明
 * @version:        1.0
 */
public abstract class ListHttpCallBack<T> extends BaseHttpCallBack<T> {

    public ListHttpCallBack() {
    }

    public ListHttpCallBack(boolean needToastError) {
        super(needToastError);
    }

    @Override
    protected void parseData(String data, int code, String msg) {
        if (TextUtils.isEmpty(data)){
            emptyData();
            return;
        }
        Type type = this.getClass().getGenericSuperclass();
        Class<T> mClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        List<T> result = JSON.parseArray(data, mClass);
        if (result != null) {
            if (result.isEmpty()) {
                emptyData();
            } else {
                onSuccess(result);
            }
            onFinish();
        } else {
            onFailed(code, msg);
        }
    }

    @Override
    public final void onSuccess(@NonNull  T result) {

    }

    /**
     *
     * @param result 结果
     */
    public abstract void onSuccess(@NonNull List<T> result);


}
