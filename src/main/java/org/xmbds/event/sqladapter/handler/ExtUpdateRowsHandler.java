package org.xmbds.event.sqladapter.handler;

import com.github.shyiko.mysql.binlog.event.EventType;
import org.xmbds.event.sqladapter.SqlEventHandler;
import org.xmbds.event.sqladapter.SqlParam;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-11 13:44
 */
public class ExtUpdateRowsHandler implements SqlEventHandler {
    @Override
    public boolean accept(EventType eventType) {
        return EventType.EXT_UPDATE_ROWS == eventType;
    }

    @Override
    public String generatorSql(EventType eventType, SqlParam sqlParam) {
        return null;
    }
}
