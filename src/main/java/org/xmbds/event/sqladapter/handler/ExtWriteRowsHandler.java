package org.xmbds.event.sqladapter.handler;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.mysql.cj.MysqlType;
import org.xmbds.event.sqladapter.SqlEventHandler;
import org.xmbds.event.sqladapter.SqlParam;
import org.xmbds.meta.ColumnMeta;
import org.xmbds.meta.DbMeta;
import org.xmbds.meta.DbMetaManage;
import org.xmbds.meta.TableMeta;
import org.xmbds.utils.ParamUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-8 19:52
 */
public class ExtWriteRowsHandler implements SqlEventHandler {
    @Override
    public boolean accept(EventType eventType) {
        return eventType == EventType.EXT_WRITE_ROWS;
    }

    @Override
    public String generatorSql(EventType eventType, SqlParam sqlParam) {
        DbMeta dbMeta = DbMetaManage.getDbMeta(sqlParam.getDbName());
        TableMeta tableMeta = dbMeta.getTableMeta(sqlParam.getTableName());
        List<ColumnMeta> columnMetas = new ArrayList<>();
//        MysqlType[] paramTypes = new MysqlType[sqlParam.getColumnsIndexs().length];
        for (int i = 0; i < sqlParam.getColumnsIndexs().length; i++) {
            long columnsIndex = sqlParam.getColumnsIndexs()[i];
            ColumnMeta columnMeta = tableMeta.getColumnMeta(columnsIndex);
//            sql.append(columnMeta.getName());
//            sql.append(",");
//            paramTypes[i] = columnMeta.getMysqlType();
            columnMetas.add(columnMeta);
        }
        StringBuilder sql = new StringBuilder();
        for(Serializable[] row : sqlParam.getRows()) {
            sql.append("insert into ").append(sqlParam.getDbName()).append(".")
                    .append(sqlParam.getTableName()).append(" (");
            for(ColumnMeta columnMeta : columnMetas){
                sql.append(columnMeta.getName());
                sql.append(",");
            }
            sql.delete(sql.length() - 1, sql.length());
            sql.append(")").append(" values ").append("(");

            for(int i = 0;i < columnMetas.size();i++){
                ColumnMeta columnMeta = columnMetas.get(i);
                MysqlType mysqlType = columnMeta.getMysqlType();
//                MysqlType mysqlType = paramTypes[i];
                Serializable param = row[i];
                if(param != null){
                    boolean needQuote = ParamUtils.needQuote(mysqlType);
                    if(needQuote){
                        sql.append("'");
                    }
                    String paramStr = ParamUtils.toString(param,mysqlType);
                    sql.append(paramStr);
                    if(needQuote){
                        sql.append("'");
                    }
                }else{
                    sql.append(param);
                }

                sql.append(",");
            }

            sql.delete(sql.length() - 1, sql.length());
            sql.append(");");
        }

        return sql.toString();
    }
}
