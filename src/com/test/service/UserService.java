package com.test.service;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.core.Component;
import com.minis.jdbc.core.JdbcTemplate;
import com.test.entity.User;

import java.sql.ResultSet;
import java.util.Date;

@Component
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUserInfo(int userId) {
        String sql = "select id,name,birthday from user where id=" + userId;
        Object user = jdbcTemplate.query(stmt -> {
            ResultSet resultSet = stmt.executeQuery(sql);
            User rtnUser = null;
            if (resultSet.next()) {
                rtnUser = new User();
                rtnUser.setId(resultSet.getInt("id"));
                rtnUser.setName(resultSet.getString("name"));
                rtnUser.setBirthday(new Date(resultSet.getTimestamp("birthday").getTime()));
            }
            resultSet.close();
            return rtnUser;
        });
        return (User) user;
    }

    public User getUserInfoPrepared(int userId) {
        String sql = "select id,name,birthday from user where id=?";
        Object user = jdbcTemplate.query(sql, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            User rtnUser = null;
            if (resultSet.next()) {
                rtnUser = new User();
                rtnUser.setId(resultSet.getInt("id"));
                rtnUser.setName(resultSet.getString("name"));
                rtnUser.setBirthday(new Date(resultSet.getTimestamp("birthday").getTime()));
            }
            resultSet.close();
            return rtnUser;
        }, userId);
        return (User) user;
    }
}