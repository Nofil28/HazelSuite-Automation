package com.hazelsuite.utilities;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteUtil {

    static String absolutePathToDB = Paths.get(System.getProperty("user.dir"), PropertiesFile.readKey("relativePathToDB")).toString();
    public static Connection connectToDB() {
        String url = "jdbc:sqlite:" + absolutePathToDB;

        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertDataIntoDB(List<String> locatorValues, String tableName) {
        Connection connection = connectToDB();
        Log.info("Inserting data into DB table " + tableName);
        if (connection != null) {
            // Generate placeholders based on the length of the 'locatorValues' array
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < locatorValues.size(); i++) {
                placeholders.append("?");
                if (i < locatorValues.size() - 1) {
                    placeholders.append(", ");
                }
            }

            // Construct the SQL query with the generated placeholders
            String insertQuery = "INSERT INTO " + tableName + " VALUES (" + placeholders + ")";

            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                // Set values for each placeholder
                for (int i = 0; i < locatorValues.size(); i++) {
                    statement.setString(i + 1, locatorValues.get(i));
                }

                // Execute the query
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static void duplicateTableStructure(String existingTableName, String newTableName) {
        Connection connection = connectToDB();
        if (connection != null) {
            List<String> columnNames = getColumnNames(existingTableName);
            createTableWithColumns(newTableName, columnNames);
        }
    }

    public static List<String> getColumnNames(String tableName) {
        Connection connection = connectToDB();
        List<String> columnNames = new ArrayList<>();

        String query = "PRAGMA table_info(" + tableName + ")";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String columnName = resultSet.getString("name");
                columnNames.add(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return columnNames;
    }

    private static void createTableWithColumns(String tableName, List<String> columnNames) {
        Connection connection = connectToDB();
        if(connection != null)
        {
            StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (");

            for (String columnName : columnNames) {
                createTableQuery.append(columnName).append(" TEXT, ");
            }

            createTableQuery.delete(createTableQuery.length() - 2, createTableQuery.length());
            createTableQuery.append(")");
            try (PreparedStatement statement = connection.prepareStatement(createTableQuery.toString())) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String compareTables(String table1, String table2, List<String> columnNames) {
        Connection connection = connectToDB();
        if (connection != null) {
            return getTableDifferences(connection, table1, table2, columnNames);
        }
        return "";
    }

    private static String getTableDifferences(Connection connection, String table1, String table2, List<String> columnNames) {
        Log.info("There are differences between both tables " + table1 + " and " + table2 + " so going to print the differences");
        for (int i = 0; i < columnNames.size(); i++) {
            String query = "SELECT t1." + columnNames.get(i) +
                    " FROM " + table1 + " t1" +
                    " LEFT JOIN " + table2 + " t2 ON t2." + columnNames.get(i) +
                    " = t1." + columnNames.get(i) +
                    " WHERE t2." + columnNames.get(i) +
                    " IS NULL";

            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();
                int rowCount = resultSet.getRow();
                if (rowCount > 0) {
                    return columnNames.get(i);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    public static boolean checkIfTablesHaveSameData(String table1, String table2) {
        Log.info("Going to check if tables " + table1 + " and " + table2 + " already have the same data");
        Connection connection = connectToDB();
        int rowCount;
        // Get the count of rows with the same data in both tables
        String query = "SELECT COUNT(*) FROM (" +
                "SELECT * FROM " + table1 +
                " INTERSECT " +
                "SELECT * FROM " + table2 +
                ")";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            rowCount = resultSet.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // If rowCount is greater than 0, tables have the same data
        Log.info("Row count is: " + rowCount);
        return rowCount > 0;
    }

    public static void deleteDataFromTables(String[] tableNames)
    {
        Connection connection = connectToDB();
        if(connection != null)
        {
            for(int i=0;i<tableNames.length;i++)
            {
                String query = "DELETE FROM " + tableNames[i];

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
