package com.wisdomme.plugin.mysqlforspigot.apis;

import com.wisdomme.plugin.mysqlforspigot.beans.ColumnEntity;
import com.wisdomme.plugin.mysqlforspigot.beans.TableEntity;

import java.util.List;
import java.util.Map;

public interface MysqlAPI {

    /**
     * @param tableName 表名称
     * @return 表是否存在
     */
    boolean isTableExists(String tableName);

    /**
     * @param tableName 表名称
     * @return 是否成功删除表
     */
    boolean dropTable(String tableName);

    /**
     * @param tableEntity 表实体
     * @return 是否成功创建表
     */
    boolean createTable(TableEntity tableEntity);

    /**
     * @param tableName         表名称
     * @param primaryColumnName 数据唯一的列名称
     * @param id                需要获取的记录id
     * @return 一个Map代表查询的数据，包含键为数据名称，值为数据。Null为没有查到。
     */
    Map<String, String> getRecordFromTable(String tableName, String primaryColumnName, String id);

    /**
     * 还在开发中，请勿使用
     */
    Object getRecordFromTable(String tableName, String id, Class<?> clazz);

    /**
     * 还在开发中，请勿使用
     */
    List<?> getAllRecordFromTable(String tableName, Class<?> clazz);

    /**
     * @param tableName 表名称
     * @return 一个列表包含所有查询到的数据的Map，键为数据名称，值为数据。若表为空，则返回空列表。
     */
    List<Map<String, String>> getAllRecordsFromTable(String tableName);

    /**
     * @param tableName  表名称
     * @param columnName 需要查询的列的名称
     * @return 一个此列所有的数据
     */
    List<String> getKeys(String tableName, String columnName);

    /**
     * @param tableName    表名称
     * @param columnEntity 需要添加的列实体
     */
    void addColumn(String tableName, ColumnEntity columnEntity);

    /**
     * @param tableName  表名称
     * @param columnName 需要查询的列名称
     * @return 此列是否存在
     */
    boolean isColumnExists(String tableName, String columnName);

    /**
     * @param tableName       表名称
     * @param pivotColumnName 作为查询依据的列名称
     * @param id              需要查询的记录id
     * @return 查询的记录是否存在
     */
    boolean isRecordExists(String tableName, String pivotColumnName, String id);

    /**
     * @param tableName 表名称
     * @param dataMap   键为数据名称，值为数据的map
     * @return 是否添加成功
     */
    boolean insertData(String tableName, Map<String, String> dataMap);

    /**
     * 还在开发中，请勿使用
     */
    boolean insertData(String tableName, Class<?> dataObject);

    /**
     * @param tableName         表名称
     * @param pivotColumnName   作为查询依据的列名称
     * @param primaryColumnName 数据唯一的列名称
     * @param id                需要修改的记录id
     * @param value             更新的值
     * @return 是否更新成功
     */
    boolean updateData(String tableName, String pivotColumnName, String primaryColumnName, String id, String value);

    /**
     * 还在开发中，请勿使用
     */
    boolean updateData(String tableName, String id, Class<?> dataObject);

}
