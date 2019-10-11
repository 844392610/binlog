package org.xmbds.utils;

import com.mysql.cj.MysqlType;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * @author 詹志彬 zhanzhibin
 * @data 2019-10-11 10:43
 */
public class ParamUtils {

    public static String toString(Serializable param, MysqlType mysqlType){
        String result = null;
        if(param != null && mysqlType.getClassName() != null){
            switch (mysqlType.getClassName()){
                case "java.lang.Long":
                case "java.lang.Integer":{
                    result = param.toString();
                }
                break;
                case "java.lang.String":{
                    if(StringUtils.startsWith(param.toString(),"[B")){
                        result = StringUtils.toEncodedString((byte[]) param, Charset.defaultCharset());
                    }
                }
                break;
                default:
                    break;
            }
        }
        return result;
    }

    public static boolean needQuote(MysqlType mysqlType) {
        boolean needQuote = false;
        switch (mysqlType.getClassName()){
            case "java.lang.String":{
                needQuote = true;
            }
            break;
            default:{
                needQuote = false;
            }
            break;
        }
        return needQuote;
    }
}
