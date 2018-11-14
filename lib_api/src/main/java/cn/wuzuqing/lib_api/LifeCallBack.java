package cn.wuzuqing.lib_api;

import java.util.List;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/14 16:17
 * @description:
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/14 16:17
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public interface LifeCallBack {

    /**
     * 获取创建数据表的sql语句集合
     *
     * @return 集合
     */
    List<String> getAllCreateSql();

    /**
     * 获取删除数据表的sql语句集合
     *
     * @return 集合
     */
    List<String> getAllDropSql();
}