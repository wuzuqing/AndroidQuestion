package cn.wuzuqing.send;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/14 16:42
 * @description: java类作用描述
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/14 16:42
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public class CacheManager {
    private static CacheManager instance = new CacheManager();
    private Map<String, Object> weakHashMap;

    public CacheManager() {
        this.weakHashMap = new HashMap<>();
    }

    public static CacheManager getInstance() {
        return instance;
    }


    public void put(String key, Object value) {
        weakHashMap.put(key, value);
    }

    public synchronized  <T> T get(String key) {
        Object value = weakHashMap.get(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }
}
