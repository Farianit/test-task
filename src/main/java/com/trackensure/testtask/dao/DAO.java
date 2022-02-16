package com.trackensure.testtask.dao;

import com.trackensure.testtask.controller.MessagesController;
import com.trackensure.testtask.exceptions.RecordNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public abstract class DAO<T> {
    private static final Logger LOGGER = Logger.getLogger(MessagesController.class.getName());

    /**
     * Database connection credentials
     */
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;

    // Table name in database
    private final String TABLE;

    protected abstract String getSelectQuery();

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
        LOGGER.info("Reading database connection credentials from database.properties");
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            jdbcURL = properties.getProperty("URL");
            jdbcUsername = properties.getProperty("USER");
            jdbcPassword = properties.getProperty("PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Connection getConnection() {
        LOGGER.info("Connecting to database");
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
    public T findById(int id) throws RecordNotFoundException {
        T result = null;
        Connection connection = getConnection();
        String sql = String.format("%s WHERE %s.id = ?", this.getSelectQuery(), this.TABLE);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            LOGGER.info(String.format("Executing %s query", sql));
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.info("Query executed, processing received data");
            while (resultSet.next()) {
                result = convertResult(resultSet);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == null) {
            throw new RecordNotFoundException();
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
        String sql = String.format("%s %s LIMIT %s", this.getSelectQuery(), order, limit);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            LOGGER.info(String.format("Executing %s query", sql));
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.info("Query executed, processing received data");
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
    public T findOne(String column, String value) throws RecordNotFoundException {
        T result = null;
        Connection connection = getConnection();
        String sql = String.format("%s WHERE %s = ?", this.getSelectQuery(), column);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, value);
            LOGGER.info(String.format("Executing %s query", sql));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result = convertResult(resultSet);
            }
            connection.close();
            if (result == null) throw new RecordNotFoundException();
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
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object value : insertParams.values()) {
                preparedStatement.setObject(i, value);
                i++;
            }
            LOGGER.info(String.format("Executing %s query", sql));
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                result = findById(id);
            }
            connection.close();
        } catch (SQLException | RecordNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
