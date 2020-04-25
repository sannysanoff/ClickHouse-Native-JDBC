package com.github.housepower.jdbc.wrapper;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public abstract class SQLConnection {


    public void setAutoCommit(boolean autoCommit) throws SQLException {
    }


    public boolean getAutoCommit() throws SQLException {
        return false;
    }


    public void commit() throws SQLException {
    }


    public void rollback() throws SQLException {
    }


    public void setReadOnly(boolean readOnly) throws SQLException {
    }


    public boolean isReadOnly() throws SQLException {
        return false;
    }


    public void setTransactionIsolation(int level) throws SQLException {
    }


    public int getTransactionIsolation() throws SQLException {
        return Connection.TRANSACTION_NONE;
    }


    public SQLWarning getWarnings() throws SQLException {
        return null;
    }


    public void clearWarnings() throws SQLException {
    }


    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return null;
    }


    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
    }


    public void setHoldability(int holdability) throws SQLException {
    }


    public int getHoldability() throws SQLException {
        return 0;
    }


    public void abort(Executor executor) throws SQLException {
        this.close();
    }

    public void close() {

    }


    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    }


    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

}
