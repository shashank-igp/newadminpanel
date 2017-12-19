package com.igp.config.instance;

import com.igp.config.DatabaseProperties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum Database {
    INSTANCE;
    
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    
    private static BasicDataSource readOnlySource;
    private static BasicDataSource readWriteSource;
    
    private synchronized void createReadOnlySource() throws Exception {
        try {
            String type = DatabaseProperties.getDBType();
            String username = DatabaseProperties.getDBUser(true);
            String password = DatabaseProperties.getDBPass(true);
            String host = DatabaseProperties.getDBHOST(true);
            String port = DatabaseProperties.getDBPort(true);
            String driverClass = DatabaseProperties.getDBDriver();
            String dbName = DatabaseProperties.getDBName(true);
            readOnlySource = new BasicDataSource();
            readOnlySource.setDriverClassName(driverClass);
            readOnlySource.setUsername(username);
            readOnlySource.setPassword(password);
            readOnlySource.setUrl("jdbc:" + type + "://" + host + ":" + port + "/" + dbName + "");
            readOnlySource.setInitialSize(DatabaseProperties.getConnectionInitialSize(true));
            readOnlySource.setMinIdle(DatabaseProperties.getConnectionMinIdle(true));
            readOnlySource.setMaxIdle(DatabaseProperties.getConnectionMaxIdle(true));
            readOnlySource.setMaxTotal(DatabaseProperties.getConnectionMaxTotal(true));
            readOnlySource.setMaxWaitMillis(DatabaseProperties.getConnectionMaxWaitMillis(true));
            readOnlySource.setTestOnBorrow(true);
            readOnlySource.setTestOnReturn(true);
        } catch (Exception exception) {
            logger.error("Connection failed: ", exception);
        }
    }
    
    private synchronized void createReadWriteSource() throws Exception {
        try {
            String type = DatabaseProperties.getDBType();
            String username = DatabaseProperties.getDBUser(false);
            String password = DatabaseProperties.getDBPass(false);
            String host = DatabaseProperties.getDBHOST(false);
            String port = DatabaseProperties.getDBPort(false);
            String driverClass = DatabaseProperties.getDBDriver();
            String dbName = DatabaseProperties.getDBName(false);
            readWriteSource = new BasicDataSource();
            readWriteSource.setDriverClassName(driverClass);
            readWriteSource.setUsername(username);
            readWriteSource.setPassword(password);
            readWriteSource.setUrl("jdbc:" + type + "://" + host + ":" + port + "/" + dbName + "");
            readWriteSource.setInitialSize(DatabaseProperties.getConnectionInitialSize(true));
            readWriteSource.setMinIdle(DatabaseProperties.getConnectionMinIdle(true));
            readWriteSource.setMaxIdle(DatabaseProperties.getConnectionMaxIdle(true));
            readWriteSource.setMaxTotal(DatabaseProperties.getConnectionMaxTotal(true));
            readWriteSource.setMaxWaitMillis(DatabaseProperties.getConnectionMaxWaitMillis(true));
            readWriteSource.setTestOnBorrow(true);
            readWriteSource.setTestOnReturn(true);
        } catch (Exception exception) {
            logger.error("Connection failed: ", exception);
        }
    }
    
    public Connection getReadOnlyConnection() throws SQLException, Exception {
        try {
            if (readOnlySource == null) {
                createReadOnlySource();
            }
            return readOnlySource.getConnection();
        } catch (Exception exception) {
            logger.error("Connection to basic dataSource failed: ", exception);
            throw exception;
        }
    }
    
    public Connection getReadWriteConnection() throws SQLException, Exception {
        try {
            if (readWriteSource == null) {
                createReadWriteSource();
            }
            return readWriteSource.getConnection();
        } catch (Exception exception) {
            logger.error("Connection to basic dataSource failed: ", exception);
            throw exception;
        }
    }
    
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException exception) {
                logger.error("Error closing connection", exception);
            } catch (Throwable throwable) {
                logger.error("Error thrown while closing connection", throwable);
            }
        }
    }
    
    public void closeStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException sqlException) {
                logger.error("Error terminating preparedstatement", sqlException);
            } catch (Throwable throwable) {
                logger.error("Error thrown while closing statement", throwable);
            }
        }
    }
    
    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlException) {
                logger.error("Error terminating resultset", sqlException);
            } catch (Throwable throwable) {
                logger.error("Error thrown while closing resultset", throwable);
            }
        }
    }
}
