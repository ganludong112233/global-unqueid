package com.uniqueid.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.CallableStatement;

public class SingleDBIDGenerator implements IdGenerator {
    private String url;
    private String driverClass;
    private String userName;
    private String password;
    private Connection connection;
    String sql = "{call sn_meter_gen(?)}";
    CallableStatement st;

    public SingleDBIDGenerator(String url, String driverClass, String userName, String password) {
        this.url = url;
        this.driverClass = driverClass;
        this.userName = userName;
        this.password = password;
        init();
    }

    private void init() {
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, userName, password);
            st = (CallableStatement) connection.prepareCall(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long getId(String snName) {
        try {
            st.setString(1, snName);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new NoIdReturnException(snName);
    }

    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {}
    }
}
