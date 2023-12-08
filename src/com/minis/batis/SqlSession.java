package com.minis.batis;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);
    Object selectOne(String sqlId, PreparedStatementCallback pstmtcallback, Object... args);
}
