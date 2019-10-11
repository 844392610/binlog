package org.xmbds.event;

import com.github.shyiko.mysql.binlog.event.EventType;
import org.xmbds.event.sqladapter.GeneratorSqlAdapter;
import org.xmbds.event.sqladapter.SqlParam;
import org.xmbds.meta.ColumnMeta;
import org.xmbds.meta.DbMeta;
import org.xmbds.meta.DbMetaManage;
import org.xmbds.meta.TableMeta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 17:48
 */
public class TransactionWatch {
    Map<Long,String> tableMap = new HashMap<>();
    String dbName;
    GeneratorSqlAdapter adapter = new GeneratorSqlAdapter();


    public void start() {
        tableMap.clear();
    }

    public void setTableRelation(long tableId,String tableName){
        tableMap.put(tableId,tableName);
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName(long tableId) {
        return tableMap.get(tableId);
    }

    public String generatorSql(EventType eventType, String tableName, int[] columnsIndexs, List<Serializable[]> rows,List<Map.Entry<Serializable[],Serializable[]>> updateRows) {
        return adapter.invoker(eventType,new SqlParam(tableName,columnsIndexs,rows,dbName,updateRows));
    }
}
