package se2203b.ipayroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EarningTableAdapter implements DataStore {

    private Connection connection;
    private String DB_URL = "jdbc:derby:iPAYROLLDB";

    public EarningTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE Earning");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }

        try {
            String command = "CREATE TABLE Earning ("
                    + "earningID INTEGER NOT NULL PRIMARY KEY,"
                    + "amount DOUBLE,"
                    + "ratePerHour DOUBLE,"
                    + "startDate DATE,"
                    + "endDate DATE,"
                    + "earningSource VARCHAR(10) NOT NULL REFERENCES EarningSource(name),"
                    + "employee VARCHAR(9) NOT NULL REFERENCES Employee(id)"
                    + ")";

            // Create the table
            stmt.execute(command);
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
            // We will use it instead of creating a new one.
        }
    }

    @Override
    public void addNewRecord(Object data) throws SQLException {
        Earning earning = (Earning) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        try {

            String command = "INSERT INTO Earning(earningID, amount, ratePerHour, startDate, endDate, earningSource, employee) "
                    + "VALUES ("
                    + earning.getEarningID() + ", "
                    + earning.getAmount() + ", "
                    + earning.getRatePerHour() + ", '"
                    + earning.getStartDate() + "', '"
                    + earning.getEndDate() + "', '"
                    + earning.getEarningSourceName() + "', '"
                    + earning.getEmployeeID() + "'"
                    + ")";

            stmt.executeUpdate(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        Earning earning = (Earning) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        String command = "UPDATE Earning "
                + "SET amount = " + earning.getAmount() + ", "
                + "ratePerHour = " + earning.getRatePerHour() + ", "
                + "startDate = '" + earning.getStartDate() + "', "
                + "endDate = '" + earning.getEndDate() + "', "
                + "earningSource = '" + earning.getEarningSourceName() + "', "
                + "employee = '" + earning.getEmployeeID() + "' "
                + "WHERE earningID = " + earning.getEarningID();

        stmt.executeUpdate(command);
        connection.close();
    }


    @Override
    public Object findOneRecord(String key) throws SQLException {
        Earning earning = new Earning();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        String command = "SELECT * FROM Earning WHERE earningID = " + key;

        rs = stmt.executeQuery(command);
        while (rs.next()) {
            earning.setEarningID(rs.getInt("earningID"));
            earning.setAmount(rs.getDouble("amount"));
            earning.setRatePerHour(rs.getDouble("ratePerHour"));
            earning.setStartDate(rs.getDate("startDate"));
            earning.setEndDate(rs.getDate("endDate"));
            earning.setEarningSourceName(rs.getString("earningSource"));
            earning.setEmployeeID(rs.getString("employee"));
        }
        connection.close();
        return earning;
    }

    // Get one specific record by referenced table data
    public Object findOneRecord(Object referencedObject) throws SQLException {
        Employee employee = (Employee) referencedObject;

        Earning earning = new Earning();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        String command = "SELECT * FROM Earning WHERE employee = '" + employee.getID() + "'";

        rs = stmt.executeQuery(command);
        while (rs.next()) {
            earning.setEarningID(rs.getInt("earningID"));
            earning.setAmount(rs.getDouble("amount"));
            earning.setRatePerHour(rs.getDouble("ratePerHour"));
            earning.setStartDate(rs.getDate("startDate"));
            earning.setEndDate(rs.getDate("endDate"));
            earning.setEarningSourceName(rs.getString("earningSource"));
            earning.setEmployeeID(rs.getString("employee"));
        }
        connection.close();
        return earning;
    };

    // Delete one specific record
    public void deleteOneRecord(String key) throws SQLException {
        int deleteID = Integer.parseInt(key);
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Earning WHERE earningID = " + deleteID );
        connection.close();
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
        String command = "SELECT * FROM Earning";


        // Execute the statement and return the result
        result = stmt.executeQuery(command);


        // Loop the entire rows of rs and set the string values of list
        while (result.next()) {
            list.add("" + result.getInt("earningID"));
        }

        return list;
    };

    // Get a list of all data records
    public List<Object> getAllRecords() throws SQLException {
        return null;
    };

    // Get a list of specific records by referenced table data
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        List<Object> earnings = new ArrayList<>();
        Employee employee = (Employee) referencedObject;
        ResultSet rs;
        try {
            connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            String command = "SELECT * FROM Earning WHERE employee = '" + employee.getID() + "'";
            rs = stmt.executeQuery(command);
            while (rs.next()) {
                Earning earning = new Earning();
                earning.setEarningID(rs.getInt("earningID"));
                earning.setAmount(rs.getDouble("amount"));
                earning.setRatePerHour(rs.getDouble("ratePerHour"));
                earning.setStartDate(rs.getDate("startDate"));
                earning.setEndDate(rs.getDate("endDate"));
                earning.setEarningSourceName(rs.getString("earningSource"));
                earning.setEmployeeID(rs.getString("employee"));
                earnings.add(earning);
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return earnings;
    }
}
