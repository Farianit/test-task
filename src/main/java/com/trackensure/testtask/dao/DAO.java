package com.trackensure.testtask.dao;

import java.sql.*;
import java.util.*;

public abstract class DAO<T> {
    /**
     * Database connection credentials
     */
    private final String jdbcURL = "jdbc:mysql://localhost:3306/webchat?createDatabaseIfNotExist=true";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "root";

    // Table name in database
    private final String TABLE;

    /**
     * Converts received from database ResultSet to new object
     * @param resultSet Received ResultSet
     * @return New Object
     */
    protected abstract T convertResult(ResultSet resultSet) throws SQLException;

    /**
     * Returns object properties that should be stored to the database
     * @param object Object
     * @return HashMap that contains object's properties names and their values
     */
    protected abstract HashMap<String, Object> getInsertParams(T object);

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

    /**
     * Returns object from database by it's id
     * @param id Object id
     * @return Found object or null if it doesn't exist
     */
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


    /**
     * Returns all objects from database
     *
     * @param limit Maximum amount of objects
     * @param orderBy Column name to order objects by
     * @return Found objects
     */
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

    /**
     * Returns one object by provided column and it's value
     *
     * @param column Column name
     * @param value Column value
     * @return Found object
     */
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

    /**
     * Inserts new object to the database
     * @param object Object to insert
     * @return Inserted object
     */
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
