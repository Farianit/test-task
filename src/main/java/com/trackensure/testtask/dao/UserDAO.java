package com.trackensure.testtask.dao;

import com.trackensure.testtask.controller.MessagesController;
import com.trackensure.testtask.exceptions.RecordNotFoundException;
import com.trackensure.testtask.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class UserDAO extends DAO<User> {
    private static final Logger LOGGER = Logger.getLogger(MessagesController.class.getName());

    private static final String TABLE = "users";
    private static final String SELECT_QUERY = "SELECT * FROM `users`";


    public UserDAO() {
        super(TABLE);
    }

    @Override
    protected String getSelectQuery() {
        return SELECT_QUERY;
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
        LOGGER.info("Trying to find user with name " + name);
        User result = null;
        try {
            result = findOne("name", name);
            LOGGER.info("User found");
        } catch (RecordNotFoundException e) {
            LOGGER.info("User not found, creating a new one");
            result = insert(new User(name));
            LOGGER.info("New user was created successfully");
        }
        return result;
    }
}
