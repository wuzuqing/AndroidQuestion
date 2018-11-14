package cn.wuzuqing.lib_api;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/12 13:08
 * @description:
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/12 13:08
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public interface Database {

    /**
     * 获取操作数据库
     *
     * @return 数据库操作类
     */
    SQLiteDatabase getWritableDatabase();

    /**
     * 获取读取数据库
     *
     * @return 数据库操作类
     */
    SQLiteDatabase getReadableDatabase();

}
