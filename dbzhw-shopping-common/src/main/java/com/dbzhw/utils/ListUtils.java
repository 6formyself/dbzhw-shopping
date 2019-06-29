package com.dbzhw.utils;

import java.util.List;
import java.util.Map;

public class ListUtils {

    /**
     * 判断list集合是否为空
     *
     * @param list
     * @return
     */
    public List<?> emptyList(List<?> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list;
    }

    /**
     * 判断map集合是否为空
     *
     * @param map
     * @return
     */
    public Map<?, ?> emptyMap(Map<?, ?> map) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        return map;
    }

}
