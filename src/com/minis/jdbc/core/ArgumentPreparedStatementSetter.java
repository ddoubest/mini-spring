package com.minis.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementSetter {
    private final Object[] args; //参数数组

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    //设置SQL参数
    public void setValues(PreparedStatement pstmt) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                doSetValue(pstmt, i + 1, arg);
            }
        }
    }

    private void doSetValue(PreparedStatement pstmt, int parameterPosition, Object argValue) throws SQLException {
        pstmt.setObject(parameterPosition, argValue);
    }
}