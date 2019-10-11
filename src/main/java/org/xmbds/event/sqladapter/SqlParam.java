package org.xmbds.event.sqladapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 19:38
 */
public class SqlParam {

    private String tableName;

    private int[] columnsIndexs;

    private List<Serializable[]> rows;

    private List<Map.Entry<Serializable[],Serializable[]>> updateRows;

    private String dbName;

    public SqlParam() {
    }

    public SqlParam(String tableName, int[] columnsIndexs, List<Serializable[]> rows,String dbName,List<Map.Entry<Serializable[],Serializable[]>> updateRows) {
        this.tableName = tableName;
        this.columnsIndexs = columnsIndexs;
        this.rows = rows;
        this.dbName = dbName;
        this.updateRows = updateRows;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int[] getColumnsIndexs() {
        return columnsIndexs;
    }

    public void setColumnsIndexs(int[] columnsIndexs) {
        this.columnsIndexs = columnsIndexs;
    }

    public List<Serializable[]> getRows() {
        return rows;
    }

    public void setRows(List<Serializable[]> rows) {
        this.rows = rows;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
