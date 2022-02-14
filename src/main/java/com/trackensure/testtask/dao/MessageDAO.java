package com.trackensure.testtask.dao;

import com.trackensure.testtask.exceptions.RecordNotFoundException;
import com.trackensure.testtask.model.Message;
import com.trackensure.testtask.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MessageDAO extends DAO<Message>{
    private static final String TABLE = "messages";

    public MessageDAO() {
        super(TABLE);
    }

    @Override
    protected Message convertResult(ResultSet resultSet) throws SQLException {
        UserDAO userDAO = new UserDAO();
        try {
            User author = userDAO.findById(resultSet.getInt("user_id"));
            return new Message(
                    resultSet.getInt("id"),
                    author,
                    resultSet.getString("text"),
                    resultSet.getTimestamp("sent_time")
            );
        } catch (RecordNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected HashMap<String, Object> getInsertParams(Message message) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", message.getAuthor().getId());
        params.put("text", message.getText());
        return params;
    }
}
