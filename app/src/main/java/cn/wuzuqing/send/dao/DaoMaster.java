package cn.wuzuqing.send.dao;

import android.content.Context;
import cn.wuzuqing.lib_api.AbsDaoMaster;
import cn.wuzuqing.lib_api.DevOpenHelper;


public class DaoMaster extends AbsDaoMaster {

	public static final int SCHEMA_VERSION = 1;

	private DaoSession daoSession;

	public DaoMaster(Context context, String name) {
		DevOpenHelper helper = new DevOpenHelper(context, name, SCHEMA_VERSION);
		daoSession = new DaoSession(helper);
		helper.setLifeCallBack(daoSession);
	}

	public DaoSession getSession() {
		return daoSession;
}

}