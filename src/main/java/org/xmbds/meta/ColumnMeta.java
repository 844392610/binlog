package org.xmbds.meta;

import com.mysql.cj.MysqlType;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 10:44
 */
public class ColumnMeta {
    private long index;
    private String name;
    private MysqlType mysqlType;

    public ColumnMeta() {
    }

    public ColumnMeta(long index, String name) {
        this.index = index;
        this.name = name;
    }

    public ColumnMeta(long index, String name, MysqlType mysqlType) {
        this.index = index;
        this.name = name;
        this.mysqlType = mysqlType;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MysqlType getMysqlType() {
        return mysqlType;
    }

    public void setMysqlType(MysqlType mysqlType) {
        this.mysqlType = mysqlType;
    }
}
