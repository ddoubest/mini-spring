package com.test.service;

import com.minis.jdbc.core.JdbcTemplate;

public class UserJdbcImpl extends JdbcTemplate {
    // @Override
    // protected Object doInternalStatement(ResultSet resultSet) {
    //     User user = null;
    //     try {
    //         if (resultSet.next()) {
    //             user = new User();
    //             user.setId(resultSet.getInt("id"));
    //             user.setName(resultSet.getString("name"));
    //             user.setBirthday(new java.util.Date(resultSet.getTime("birthday").getTime()));
    //         }
    //     } catch (SQLException e) {
    //         throw new RuntimeException(e);
    //     }
    //     return user;
    // }
}
