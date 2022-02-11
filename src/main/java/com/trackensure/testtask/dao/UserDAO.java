package com.trackensure.testtask.dao;

import com.trackensure.testtask.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserDAO extends DAO<User> {
    public static final String TABLE = "users";

    public UserDAO() {
        super(TABLE);
    }

    @Override
    protected User convertResult(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getTimestamp("created_time")
        );
    }

    @Override
    protected HashMap<String, Object> getInsertParams(User user) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        return params;
    }

    public User createOrGetUserByName(String name) {
        User result = findOne("name", name);
        if (result == null) {
            result = insert(new User(name));
        }
        return result;
    }
}
