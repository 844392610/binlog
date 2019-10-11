package org.xmbds.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 11:27
 */
public class DbMetaManage {

    private static Map<String,DbMeta> dbMetaMap;

    public static DbMeta registerDbMeta(String dbName){
        if(dbMetaMap == null){
            dbMetaMap = new HashMap<>();
        }
       return dbMetaMap.computeIfAbsent(dbName,key-> new DbMeta(key));
    }

    public static DbMeta getDbMeta(String dbName) {
        return dbMetaMap.get(dbName);
    }

}
