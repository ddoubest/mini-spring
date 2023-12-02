package com.minis.jdbc.core;

import com.minis.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

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
            ArgumentPreparedStatementSetter statementSetter = new ArgumentPreparedStatementSetter(args);
            statementSetter.setValues(statement);
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

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        RowMapperResultSetExtractor<T> resultSetExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<T> returnObj;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            ArgumentPreparedStatementSetter statementSetter = new ArgumentPreparedStatementSetter(args);
            statementSetter.setValues(statement);
            resultSet = statement.executeQuery();
            returnObj = resultSetExtractor.extractData(resultSet);
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
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception ignored) {
            }
        }

        return returnObj;
    }
}
