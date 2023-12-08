package com.minis.batis;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

public class DefaultSqlSession implements SqlSession {
    private JdbcTemplate jdbcTemplate;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object selectOne(String sqlId, PreparedStatementCallback psCallback, Object... args) {
        MapperNode mapperNode = sqlSessionFactory.getMapperNode(sqlId);
        return jdbcTemplate.query(mapperNode.getSql(), psCallback, args);
    }
}
