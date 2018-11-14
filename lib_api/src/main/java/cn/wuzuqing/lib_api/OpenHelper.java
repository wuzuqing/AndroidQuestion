package cn.wuzuqing.lib_api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/12 13:01
 * @description: java类作用描述
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/12 13:01
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public class OpenHelper extends SQLiteOpenHelper implements Database {

    protected LifeCallBack lifeCallBack;

    public OpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void setLifeCallBack(LifeCallBack lifeCallBack) {
        this.lifeCallBack = lifeCallBack;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (lifeCallBack != null) {
            execSQL(db, lifeCallBack.getAllCreateSql());
        }
    }

    private void execSQL(SQLiteDatabase db, List<String> list) {
        if (list != null && list.size() > 0) {
            for (String sql : list) {
                db.execSQL(sql);
            }
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (lifeCallBack != null) {
            execSQL(db, lifeCallBack.getAllDropSql());
        }
        onCreate(db);
    }

}
