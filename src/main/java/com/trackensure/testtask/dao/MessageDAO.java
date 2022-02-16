package com.trackensure.testtask.dao;

import com.trackensure.testtask.exceptions.RecordNotFoundException;
import com.trackensure.testtask.model.Message;
import com.trackensure.testtask.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MessageDAO extends DAO<Message>{
    private static final String TABLE = "messages";
    private static final String SELECT_QUERY = "SELECT messages.id AS message_id, messages.text AS message_text, messages.sent_time AS message_sent_time, users.id AS user_id, users.name AS user_name, users.created_time AS user_created_time FROM messages INNER JOIN users ON messages.user_id = users.id";

    public MessageDAO() {
        super(TABLE);
    }

    @Override
    protected String getSelectQuery() {
        return SELECT_QUERY;
    }

    @Override
    protected Message convertResult(ResultSet rs) throws SQLException {
        User author = new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getTimestamp("user_created_time"));
        return new Message(
                rs.getInt("message_id"),
                author,
                rs.getString("message_text"),
                rs.getTimestamp("message_sent_time")
        );
    }

    @Override
    protected HashMap<String, Object> getInsertParams(Message message) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("user_id", message.getAuthor().getId());
        params.put("text", message.getText());
        return params;
    }
}
