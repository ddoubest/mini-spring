package com.minis.jdbc.datasource;

import com.minis.jdbc.pool.PooledConnection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource {
    public static int DEFAULT_POOL_SIZE = 2;

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private int poolSize;
    private List<PooledConnection> connections;

    public PooledDataSource() {
        this(DEFAULT_POOL_SIZE);
    }

    public PooledDataSource(int poolSize) {
        this.poolSize = poolSize;
    }

    private void initPool() throws SQLException {
        this.connections = new ArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            Connection connection = DriverManager.getConnection(url, username, password);
            PooledConnection pooledConnection = new PooledConnection(connection);
            connections.add(pooledConnection);
        }
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (connections == null) {
            initPool();
        }
        Connection connection = getAvailableConnection();
        while (connection == null) {
            connection = getAvailableConnection();
            if (connection == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return connection;
    }

    private Connection getAvailableConnection() {
        for (PooledConnection connection : connections) {
            if (!connection.isActive()) {
                connection.setActive(true);
                return connection;
            }
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
