package org.xmbds.meta;

import com.mysql.cj.MysqlType;

import java.util.HashMap;
import java.util.Map;

/**
 * 元数据管理
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 10:53
 */
public class DbMeta {

    private String dbName;

    private Map<String,TableMeta> tableMetaMap;

    public DbMeta(String dbName) {
        this.dbName = dbName;
    }

    public TableMeta registerTable(String tableName){
        if(tableMetaMap == null){
            tableMetaMap = new HashMap<>();
        }
        TableMeta tableMeta = tableMetaMap.computeIfAbsent(tableName,tbName->new TableMeta(tbName));
        return tableMeta;
    }

    public void registerTable(String tableName, int columnIndex, String columnName){
      registerTable(tableName,new ColumnMeta(columnIndex,columnName));
    }

    public void registerTable(String tableName, ColumnMeta columnMeta){
        TableMeta tableMeta = registerTable(tableName);
        tableMeta.registerColumnMeta(columnMeta);
    }


    public void updateDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public TableMeta getTableMeta(String tableName) {
        return tableMetaMap.get(tableName);
    }
}
