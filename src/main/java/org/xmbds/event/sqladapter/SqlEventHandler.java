package org.xmbds.event.sqladapter;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 19:41
 */
public interface SqlEventHandler {
    boolean accept(EventType eventType);

    String generatorSql(EventType eventType, SqlParam sqlParam);
}
