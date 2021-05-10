package com.wisdomme.plugin.mysqlforspigot.impls;

import com.wisdomme.plugin.mysqlforspigot.MySqlForSpigot;
import com.wisdomme.plugin.mysqlforspigot.apis.MysqlAPI;
import com.wisdomme.plugin.mysqlforspigot.beans.ColumnEntity;
import com.wisdomme.plugin.mysqlforspigot.beans.TableEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlAPIImpl implements MysqlAPI {
    private static final DataSource dataSource;

    static {
        FileConfiguration mainConfig = MySqlForSpigot.getInstance().getConfig();
        HikariConfig config = new HikariConfig();
        String ip = mainConfig.getString("database.host");
        String port = mainConfig.getString("database.port");
        String database = mainConfig.getString("database.name");
        String useUnicode = mainConfig.getString("database.useUnicode");
        String characterEncoding = mainConfig.getString("database.characterEncoding");
        String useSSL = mainConfig.getString("database.useSSL");
        config.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + database + "?useUnicode=" + useUnicode + "&characterEncoding=" + characterEncoding + "&useSSL=" + useSSL);
        String username = mainConfig.getString("database.username");
        config.setUsername(username);
        String password = mainConfig.getString("database.password");
        config.setPassword(password);
        String connectionTimeout = mainConfig.getString("datasourse.connectionTimeout");
        String idleTimeout = mainConfig.getString("datasourse.idleTimeout");
        String maximumPoolSize = mainConfig.getString("datasourse.maximumPoolSize");
        config.addDataSourceProperty("connectionTimeout", connectionTimeout);
        config.addDataSourceProperty("idleTimeout", idleTimeout);
        config.addDataSourceProperty("maximumPoolSize", maximumPoolSize);
        dataSource = new HikariDataSource(config);
    }

    @Override
    public boolean isTableExists(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA ='dbname' and TABLE_NAME =?;")) {
                ps.setString(1, tableName);
                try (ResultSet re = ps.executeQuery()) {
                    return re.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean dropTable(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement("drop table " + tableName + ";")) {
                ps.executeUpdate();
                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createTable(TableEntity tableEntity) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(tableEntity.getSql())) {
                ps.executeUpdate();
                connection.commit();
                return true;
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, String> getRecordFromTable(String tableName, String primaryColumnName, String id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from " + tableName + " where " + primaryColumnName + "=?")) {
                ps.setString(1, id);
                try (ResultSet re = ps.executeQuery()) {
                    if (re.next()) {
                        ResultSetMetaData md = re.getMetaData();
                        int columnCount = md.getColumnCount();
                        Map<String, String> rowData = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            rowData.put(md.getColumnName(i), re.getString(i));
                        }
                        return rowData;
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getRecordFromTable(String tableName, String id, Class<?> clazz) {
        return null;
    }

    @Override
    public List<?> getAllRecordFromTable(String tableName, Class<?> clazz) {
        return null;
    }

    @Override
    public List<Map<String, String>> getAllRecordsFromTable(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from " + tableName)) {
                try (ResultSet re = ps.executeQuery()) {
                    List<Map<String, String>> result = new ArrayList<>();
                    while (re.next()) {
                        ResultSetMetaData md = re.getMetaData();
                        int columnCount = md.getColumnCount();
                        Map<String, String> rowData = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            rowData.put(md.getColumnName(i), re.getString(i));
                        }
                        result.add(rowData);
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getKeys(String tableName, String columnName) {
        List<String> keys = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select " + columnName + " from " + tableName)) {
                try (ResultSet re = ps.executeQuery()) {
                    while (re.next()) {
                        keys.add(re.getString(columnName));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    @Override
    public void addColumn(String tableName, ColumnEntity columnEntity) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement("ALTER TABLE " + tableName); PreparedStatement ps2 = connection.prepareStatement("add column " + columnEntity.toString())) {
                ps.executeUpdate();
                ps2.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isColumnExists(String tableName, String columnName) {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet re = statement.executeQuery("desc " + tableName + " " + columnName)) {
                    return re.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isRecordExists(String tableName, String pivotColumnName, String id) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from " + tableName + " where " + pivotColumnName + "=?")) {
                ps.setString(1, id);
                try (ResultSet re = ps.executeQuery()) {
                    return re.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertData(String tableName, Map<String, String> dataMap) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            String[] keys = dataMap.keySet().toArray(new String[0]);
            String[] values = dataMap.values().toArray(new String[0]);
            try (PreparedStatement ps = connection.prepareStatement("insert into " + tableName + " (" + insertFields(keys) + ") values (" + insertValues(values) + ")")) {
                int a = ps.executeUpdate();
                connection.commit();
                return (a == 1);
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertData(String tableName, Class<?> dataObject) {
        return false;
    }

    @Override
    public boolean updateData(String tableName, String pivotColumnName, String primaryColumnName, String id, String value) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement("update " + tableName + " set " + pivotColumnName + "=? " + " where " + primaryColumnName + "=?")) {
                ps.setString(1, value);
                ps.setString(2, id);
                int a = ps.executeUpdate();
                connection.commit();
                return (a == 1);
            } catch (Exception e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateData(String tableName, String id, Class<?> dataObject) {
        return false;
    }

    /**
     * 将一个String[]转换为mysql插入语句的fields
     *
     * @param fields 需要转换的fields
     * @return 一个MySQL语句
     */
    private static String insertFields(String[] fields) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String arg : fields) {
            ++i;
            builder.append(arg + (i == fields.length ? "" : ","));
        }
        return builder.toString();
    }

    /**
     * 将一个String[]转换为mysql插入语句的values
     *
     * @param values 需要转换的fields
     * @return 一个MySQL语句
     */
    private static String insertValues(String[] values) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String arg : values) {
            ++i;
            builder.append("'" + arg + "'" + (i == values.length ? "" : ","));
        }
        return builder.toString();
    }
}
