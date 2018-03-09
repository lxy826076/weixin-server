package com.weixin.datasource;
/**
 * Created by IDEA
 * 本地线程设置和获取数据源信息
 * User: mashaohua
 * Date: 2016-07-07 13:35
 * Desc:
 */
public class DynamicDataSourceHolder {

    private static final ThreadLocal<DynamicDataSourceGlobal> holder = new ThreadLocal<DynamicDataSourceGlobal>();

    public static void putDataSource(DynamicDataSourceGlobal dataSource){
        holder.set(dataSource);
    }

    public static DynamicDataSourceGlobal getDataSource(){
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }
}
