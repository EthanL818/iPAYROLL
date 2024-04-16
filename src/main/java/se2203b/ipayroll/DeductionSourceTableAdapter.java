package se2203b.ipayroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeductionSourceTableAdapter implements DataStore {
    private Connection connection;
    private String DB_URL = "jdbc:derby:iPAYROLLDB";

    public DeductionSourceTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE DeductionSource");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }

        try {
            String command = "CREATE TABLE DeductionSource ("
                    + "code INTEGER NOT NULL PRIMARY KEY,"
                    + "name VARCHAR(10) NOT NULL UNIQUE"
                    + ")";
            // Create the table
            stmt.execute(command);
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
            // We will use it instead of creating a new one.
        }
    }

    // Add new data record
    public void addNewRecord(Object data) throws SQLException {
        DeductionSource deductionSource = (DeductionSource) data;
        connection = DriverManager.getConnection(DB_URL);

        Statement stmt = connection.createStatement();
        try {
            String command = "INSERT INTO DeductionSource(code, name) "
                    + "VALUES ("
                    + deductionSource.getCode() + ", '"
                    + deductionSource.getName() + "'"
                    + ")";
            stmt.executeUpdate(command);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    // Update the existing record
    public void updateRecord(Object data) throws SQLException {

    };

    // Get one specific record
    public Object findOneRecord(String key) throws SQLException {
        return null;
    };

    // Get one specific record by referenced table data
    public Object findOneRecord(Object referencedObject) throws SQLException {
        return null;
    };

    // Delete one specific record
    public void deleteOneRecord(String key) throws SQLException {

    };

    // Delete a group of specific records by referenced table data
    public void deleteRecords(Object referencedObject) throws SQLException {

    };

    // Get list of all data keys
    public List<String> getKeys() throws SQLException {
        // Get a String list of matches to populate the ComboBox used in Task #4.
        List<String> list = new ArrayList<>();
        ResultSet result;

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT * FROM DeductionSource";


        // Execute the statement and return the result
        result = stmt.executeQuery(command);


        // Loop the entire rows of rs and set the string values of list
        while (result.next()) {
            list.add("" + result.getInt("code"));
        }


        return list;
    };

    // Get a list of all data records
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();

        // Add your code here for Task #2
        // TASK #2.1: Write an SQL statement to select all columns from the Match table
        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT * FROM DeductionSource";

        // TASK #2.2: Execute the query by sending the SQL statement to the DBMS
        ResultSet result = stmt.executeQuery(command);

        // TASK #2.3: Use a while loop to add the contents of the result set to match List
        int newCode = 1;
        while (result.next()) {
            DeductionSource deductionSource = new DeductionSource(result.getInt("code"),
                    result.getString("name"));

            deductionSource.setCode(newCode);
            newCode++;
            list.add(deductionSource);
        }

        return list;
    };

    // Get a list of specific records by referenced table data
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    };
}
