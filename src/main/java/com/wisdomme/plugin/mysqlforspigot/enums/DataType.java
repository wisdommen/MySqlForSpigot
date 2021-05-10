package com.wisdomme.plugin.mysqlforspigot.enums;

public enum DataType {
    TINYINT("TINYINT"),
    SMALLINT("SMALLINT"),
    MEDIUMINT("MEDIUMINT"),
    INTEGER("INTEGER"),
    BIGINT("BIGINT"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    DECIMAL("DECIMAL"),

    DATE("DATE"),
    TIME("TIME"),
    YEAR("YEAR"),
    DATETIME("DATETIME"),
    TIMESTAMP("TIMESTAMP"),

    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    TINYBLOB("TINYBLOB"),
    TINYTEXT("TINYTEXT"),
    BLOB("BLOB"),
    TEXT("TEXT"),
    MEDIUMBLOB("MEDIUMBLOB"),
    MEDIUMTEXT("MEDIUMTEXT"),
    LONGBLOB("LONGBLOB"),
    LONGTEXT("LONGTEXT");

    String name;

    DataType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
