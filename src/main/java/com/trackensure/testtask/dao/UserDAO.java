package com.trackensure.testtask.dao;

import com.trackensure.testtask.exceptions.RecordNotFoundException;
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


    /**
     * Find user from database or create if it doesn't exist
     * @param name Username
     * @return Found or created user
     */
    public User createOrGetUserByName(String name) {
        User result = null;
        try {
            result = findOne("name", name);
        } catch (RecordNotFoundException e) {
            result = insert(new User(name));
        }
        return result;
    }
}
