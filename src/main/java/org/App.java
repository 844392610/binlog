package org;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.mysql.cj.MysqlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmbds.event.TransactionWatch;
import org.xmbds.meta.ColumnMeta;
import org.xmbds.meta.DbMeta;
import org.xmbds.meta.DbMetaManage;
import org.xmbds.meta.TableMeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        initData();
        BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "");
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        client.setEventDeserializer(eventDeserializer);
        client.registerEventListener(new BinaryLogClient.EventListener() {
            TransactionWatch transactionWatch = new TransactionWatch();
            @Override
            public void onEvent(Event event) {
                switch (event.getHeader().getEventType()){
                    case QUERY:{
                        QueryEventData eventData = event.getData();
                        String sql = eventData.getSql();
                        if("BEGIN".equalsIgnoreCase(sql)){
                            transactionWatch.start();
                        }
                    }
                    break;
                    case TABLE_MAP:{
                        TableMapEventData tableMapEventData = event.getData();
                        String tableName = tableMapEventData.getTable();
                        long tableId = tableMapEventData.getTableId();
                        transactionWatch.setTableRelation(tableId,tableName);
                        transactionWatch.setDbName(tableMapEventData.getDatabase());
                    }
                    break;
                    case EXT_WRITE_ROWS:{
                        WriteRowsEventData eventData = event.getData();
                        long tableId = eventData.getTableId();
                        BitSet bitSet = eventData.getIncludedColumns();
                        int[] columnIndexs = bitSet.stream().toArray();
                        String tableName = transactionWatch.getTableName(tableId);
                        String sql = transactionWatch.generatorSql(event.getHeader().getEventType(),tableName,columnIndexs,eventData.getRows(),null);
                        logger.info(sql);

                    }
                    break;
                    case EXT_DELETE_ROWS:{
                        DeleteRowsEventData eventData = event.getData();
                        long tableId = eventData.getTableId();
                        BitSet bitSet = eventData.getIncludedColumns();
                        int[] columnIndexs = bitSet.stream().toArray();
                        String tableName = transactionWatch.getTableName(tableId);
                        String sql = transactionWatch.generatorSql(event.getHeader().getEventType(),tableName,columnIndexs,eventData.getRows(),null);
                        logger.info(sql);
                    }
                    break;
                    case EXT_UPDATE_ROWS:{
                        UpdateRowsEventData eventData = event.getData();
                        long tableId = eventData.getTableId();
                        BitSet bitSet = eventData.getIncludedColumns();
                        int[] columnIndexs = bitSet.stream().toArray();
                        String tableName = transactionWatch.getTableName(tableId);
                        String sql = transactionWatch.generatorSql(event.getHeader().getEventType(),tableName,columnIndexs,null,eventData.getRows());
                        logger.info(sql);

                    }
                    break;
                    case XID:{
                        XidEventData eventData = event.getData();
                    }
                    break;
                }
                logger.info(event.toString());
            }
        });
        client.connect();
    }

    private static void initData() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String dbName = "binlogexplore";
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tableResultSet = metaData.getTables(null, "public", null, new String[]{"TABLE"});
        try {
            DbMeta dbMeta = DbMetaManage.registerDbMeta(dbName);
            while (tableResultSet.next()) {
                String tableName = tableResultSet.getString("TABLE_NAME");
                ResultSet columnResultSet = metaData.getColumns(null, "public", tableName, null);
                try {
                    int columnIndex = 0;
                    while (columnResultSet.next()) {
                        String columnName = columnResultSet.getString("COLUMN_NAME");
                        String typeName = columnResultSet.getString("TYPE_NAME");
                        MysqlType mysqlType = MysqlType.getByName(typeName);

//                        columnResultSet.getMetaData().getColumnTypeName(columnIndex);
                        dbMeta.registerTable(tableName,new ColumnMeta(columnIndex,columnName,mysqlType));
//                        System.out.println("columnName:"+columnName + "-->" + typeName);
//                        System.out.println(mysqlType.getClassName());
                        columnIndex++;
                    }
                } finally {
                    columnResultSet.close();
                }
                ResultSet pkRSet = metaData.getPrimaryKeys(null,null,tableName);
                TableMeta tableMeta = dbMeta.getTableMeta(tableName);
                List<String> primaryKeys = new ArrayList<>();
                tableMeta.setPrimaryKeys(primaryKeys);
                try {
                    while(pkRSet.next()) {
                        String columnName = (String) pkRSet.getObject(4);
                        primaryKeys.add(columnName);
//                        System.err.println("****** Comment ******");
//                        System.err.println("TABLE_CAT : "+pkRSet.getObject(1));
//                        System.err.println("TABLE_SCHEM: "+pkRSet.getObject(2));
//                        System.err.println("TABLE_NAME : "+pkRSet.getObject(3));
//                        System.err.println("COLUMN_NAME: "+pkRSet.getObject(4));
//                        System.err.println("KEY_SEQ : "+pkRSet.getObject(5));
//                        System.err.println("PK_NAME : "+pkRSet.getObject(6));
//                        System.err.println("****** ******* ******");
                    }
                }finally {
                    pkRSet.close();
                }

            }
        } finally {
            tableResultSet.close();
        }
    }
}
