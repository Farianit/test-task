package com.trackensure.testtask.dao;

import java.sql.*;
import java.util.*;

public abstract class DAO<T> {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/webchat";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "root";

    private final String TABLE;

    protected abstract T convertResult(ResultSet resultSet) throws SQLException;
    protected abstract HashMap<String, Object> getInsertParams(T t);

    protected DAO(String table) {
        this.TABLE = table;
    }

    protected Connection getConnection() {
        Connection jdbcConnection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            jdbcConnection = DriverManager.getConnection(this.jdbcURL, this.jdbcUsername, this.jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return jdbcConnection;
    }

    public T findById(int id) {
        T result = null;
        Connection connection = getConnection();
        String sql = String.format("SELECT * FROM %s WHERE `id` = ?", this.TABLE);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = convertResult(resultSet);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Set<T> getAll(int limit, String orderBy) {
        Set<T> result = new LinkedHashSet<>();
        Connection connection = getConnection();
        String order = orderBy.isEmpty() ? "" : String.format( " ORDER BY %s", orderBy);
        String sql = String.format("SELECT * FROM `%s`%s LIMIT %d", this.TABLE, order, limit);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(convertResult(resultSet));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public T findOne(String column, String value) {
        T result = null;
        Connection connection = getConnection();
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", TABLE, column);

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = convertResult(resultSet);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public T insert(T object) {
        T result = null;

        HashMap<String, Object> insertParams = getInsertParams(object);
        String columns = String.join(",", insertParams.keySet());
        String[] placeholders = new String[insertParams.size()];
        Arrays.fill(placeholders, "?");
        String values = String.join(",", placeholders);
        String sql = String.format("INSERT INTO %s(%s) VALUES (%s)", TABLE, columns, values);

        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object value : insertParams.values()) {
                preparedStatement.setObject(i, value);
                i++;
            }
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                result = findById(id);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
