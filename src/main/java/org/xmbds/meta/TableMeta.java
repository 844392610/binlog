package org.xmbds.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 10:43
 */
public class TableMeta {

    private String tableName;

    private Map<Long,ColumnMeta> columnIndexMetaMap;
    private Map<String,ColumnMeta> columnNameMetaMap;
    private List<String> primaryKeys;

    public TableMeta() {
    }

    public TableMeta(String tableName) {
        this.tableName = tableName;
    }

    public void registerColumnMeta(long columnIndex, String columnName){

        registerColumnMeta(new ColumnMeta(columnIndex,columnName));
    }

    public void registerColumnMeta(ColumnMeta columnMeta){
        if(columnIndexMetaMap == null){
            columnIndexMetaMap = new HashMap<>();
        }
        if(columnNameMetaMap == null){
            columnNameMetaMap = new HashMap<>();
        }
        columnIndexMetaMap.put(columnMeta.getIndex(),columnMeta);
        columnNameMetaMap.put(columnMeta.getName(),columnMeta);
    }

    public String getTableName() {
        return tableName;
    }

    public void updateTableName(String tableName) {
        this.tableName = tableName;
    }

    public ColumnMeta getColumnMeta(long columnIndex){
        return columnIndexMetaMap.get(columnIndex);
    }

    public ColumnMeta getColumnMeta(String columnName){
        return columnNameMetaMap.get(columnName);
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }
}
