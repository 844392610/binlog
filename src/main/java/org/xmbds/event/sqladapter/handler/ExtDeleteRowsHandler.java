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
import org.xmbds.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-11 10:32
 */
public class ExtDeleteRowsHandler implements SqlEventHandler {
    @Override
    public boolean accept(EventType eventType) {
        return eventType == EventType.EXT_DELETE_ROWS;
    }

    @Override
    public String generatorSql(EventType eventType, SqlParam sqlParam) {
        StringBuilder sql = new StringBuilder();
        DbMeta dbMeta = DbMetaManage.getDbMeta(sqlParam.getDbName());
        TableMeta tableMeta = dbMeta.getTableMeta(sqlParam.getTableName());
        List<ColumnMeta> privateKeyList = new ArrayList<>();
        for (int i = 0; i < sqlParam.getColumnsIndexs().length; i++) {
            long columnsIndex = sqlParam.getColumnsIndexs()[i];
            ColumnMeta columnMeta = tableMeta.getColumnMeta(columnsIndex);
            if(tableMeta.getPrimaryKeys().contains(columnMeta.getName())){
                privateKeyList.add(new ColumnMeta(i,columnMeta.getName(),columnMeta.getMysqlType()));
            }
        }
        for(Serializable[] row : sqlParam.getRows()) {
            sql.append("delete from ").append(sqlParam.getDbName()).append(".")
                    .append(sqlParam.getTableName()).append(" where ");

            for(ColumnMeta privateKey : privateKeyList){
                sql.append(privateKey.getName()).append(" = ");
                int columnIndex = (int) privateKey.getIndex();
                MysqlType mysqlType = privateKey.getMysqlType();
                Serializable param = row[columnIndex];
                boolean needQuote = ParamUtils.needQuote(mysqlType);
                if(needQuote){
                    sql.append("'");
                }
                String paramStr = ParamUtils.toString(param,mysqlType);
                sql.append(paramStr);
                if(needQuote){
                    sql.append("'");
                }
                sql.append(" and ");
            }
            sql.delete(sql.length() - " and ".length(),sql.length());
            sql.append(";");
        }

        return sql.toString();

    }
}
