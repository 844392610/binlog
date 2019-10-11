package org.xmbds.shyiko;

import com.github.shyiko.mysql.binlog.event.TableMapEventData;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 13:29
 */
public class TableMapEventDataWrap {

    private TableMapEventData tableMapEventData;

    private String tableName;
    private long tableId;

    public TableMapEventDataWrap(TableMapEventData tableMapEventData) {
        this.tableMapEventData = tableMapEventData;
    }
}
