package org.xmbds.event.sqladapter;

import com.github.shyiko.mysql.binlog.event.EventType;
import org.xmbds.event.sqladapter.handler.ExtDeleteRowsHandler;
import org.xmbds.event.sqladapter.handler.ExtUpdateRowsHandler;
import org.xmbds.event.sqladapter.handler.ExtWriteRowsHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 18:14
 */
public class GeneratorSqlAdapter {

    static List<SqlEventHandler> eventHandlerList = new ArrayList<>();

    static {
        eventHandlerList.add(new ExtWriteRowsHandler());
        eventHandlerList.add(new ExtDeleteRowsHandler());
        eventHandlerList.add(new ExtUpdateRowsHandler());
    }

    public String invoker(EventType eventType, SqlParam sqlParam) {
        SqlEventHandler acceptHandler = null;
        for(SqlEventHandler handler : eventHandlerList) {
            if (handler.accept(eventType)) {
                acceptHandler = handler;
                break;
            }
        }
        return acceptHandler.generatorSql(eventType,sqlParam);
    }
}
