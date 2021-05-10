package com.wisdomme.plugin.mysqlforspigot.beans;

import com.wisdomme.plugin.mysqlforspigot.enums.DataType;

public class DataEntity {
    DataType dataType;
    String suffix = "";

    public DataEntity(DataType dataType) {
        this.dataType = dataType;
    }

    public DataEntity(DataType dataType, String suffix) {
        this.dataType = dataType;
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return dataType.toString() + suffix;
    }
}
