package com.minis.jdbc.core;

import com.minis.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;

public class JdbcTemplate {
    @Autowired
    private DataSource dataSource;

    public Object query(StatementCallback statementCallback) {
        Connection connection = null;
        Statement statement = null;
        Object returnObj;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            returnObj = statementCallback.doInStatement(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception ignored) {
            }
        }

        return returnObj;
    }

    public Object query(String sql, PreparedStatementCallback preparedStatementCallback, Object... args) {
        Connection connection = null;
        PreparedStatement statement = null;
        Object returnObj;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            returnObj = preparedStatementCallback.doInPreparedStatement(statement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception ignored) {
            }
        }

        return returnObj;
    }
}
