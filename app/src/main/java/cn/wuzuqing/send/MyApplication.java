package cn.wuzuqing.send;

import android.app.Application;
import android.content.Context;
import cn.wuzuqing.send.dao.DbCodeManager;
import cn.wuzuqing.send.http.HttpHelper;
import cn.wuzuqing.send.http.LogUtils;
import cn.wuzuqing.send.http.OkHttpProcessor;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/7 16:10
 * @description: java类作用描述
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/7 16:10
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public class MyApplication extends Application {
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        HttpHelper.init(new OkHttpProcessor());
        HttpHelper.obtain().setBaseUrl("http://120.79.16.93:17912/example/");
        DbCodeManager.init(getApplicationContext());
    }

    public static Context getApp(){
        return instance;
    }
}
