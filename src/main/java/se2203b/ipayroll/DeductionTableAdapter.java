package se2203b.ipayroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeductionTableAdapter implements DataStore {
    private Connection connection;
    private String DB_URL = "jdbc:derby:iPAYROLLDB";

    public DeductionTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();

        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE Deduction");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }

        try {
            String command = "CREATE TABLE Deduction ("
                    + "deductionID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
                    + "amount DOUBLE,"
                    + "percentagePerEarning DOUBLE,"
                    + "upperLimit DOUBLE,"
                    + "startDate DATE,"
                    + "endDate DATE,"
                    + "deductionSource VARCHAR(10) NOT NULL REFERENCES DeductionSource(name),"
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
        Deduction deduction = (Deduction) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        try {
            String command = "INSERT INTO Deduction(amount, percentagePerEarning, upperLimit, startDate, endDate, deductionSource, employee) "
                    + "VALUES ("
                    + deduction.getAmount() + ", "
                    + deduction.getPercentage() + ", "
                    + deduction.getUpperLimit() + ", '"
                    + deduction.getStartDate() + "', '"
                    + deduction.getEndDate() + "', '"
                    + deduction.getDeductionSource() + "', '"
                    + deduction.getEmployeeID() + "'"
                    + ")";

            stmt.executeUpdate(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public void updateRecord(Object data) throws SQLException {
        Deduction deduction = (Deduction) data;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        try {
            String command = "UPDATE Deduction "
                    + "SET amount = " + deduction.getAmount() + ", "
                    + "percentagePerEarning = " + deduction.getPercentage() + ", "
                    + "upperLimit = " + deduction.getUpperLimit() + ", "
                    + "startDate = '" + deduction.getStartDate() + "', "
                    + "endDate = '" + deduction.getEndDate() + "', "
                    + "deductionSource = '" + deduction.getDeductionSource() + "', "
                    + "employee = '" + deduction.getEmployeeID() + "' "
                    + "WHERE deductionID = " + deduction.getDeductionID();

            stmt.executeUpdate(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public Object findOneRecord(String key) throws SQLException {
        Deduction deduction = new Deduction();
        ResultSet rs;
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        String command = "SELECT * FROM Deduction WHERE deductionID = " + key;

        rs = stmt.executeQuery(command);
        if (rs.next()) {
            deduction.setDeductionID(rs.getInt("deductionID"));
            deduction.setAmount(rs.getDouble("amount"));
            deduction.setPercentage(rs.getDouble("percentagePerEarning"));
            deduction.setUpperLimit(rs.getDouble("upperLimit"));
            deduction.setStartDate(rs.getDate("startDate"));
            deduction.setEndDate(rs.getDate("endDate"));
            deduction.setDeductionSource(rs.getString("deductionSource"));
            deduction.setEmployeeID(rs.getString("employee"));
        }
        rs.close();
        stmt.close();
        connection.close();
        return deduction;
    }

    @Override
    public Object findOneRecord(Object referencedObject) throws SQLException {
        // Implement as needed
        return null;
    }

    @Override
    public void deleteOneRecord(String key) throws SQLException {
        int deleteID = Integer.parseInt(key);
        connection = DriverManager.getConnection(DB_URL);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Deduction WHERE deductionID = " + deleteID );
        connection.close();
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {
        // Implement as needed
    }

    @Override
    public List<String> getKeys() throws SQLException {
        // Get a String list of matches to populate the ComboBox used in Task #4.
        List<String> list = new ArrayList<>();
        ResultSet result;

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT * FROM Deduction";


        // Execute the statement and return the result
        result = stmt.executeQuery(command);


        // Loop the entire rows of rs and set the string values of list
        while (result.next()) {
            list.add("" + result.getInt("deductionID"));
        }

        return list;
    };
    @Override
    public List<Object> getAllRecords() throws SQLException {
        // Implement as needed
        return null;
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        List<Object> deductions = new ArrayList<>();
        Employee employee = (Employee) referencedObject;
        ResultSet rs;
        try {
            connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            String command = "SELECT * FROM Deduction WHERE employee = '" + employee.getID() + "'";
            rs = stmt.executeQuery(command);
            while (rs.next()) {
                Deduction deduction = new Deduction();
                deduction.setDeductionID(rs.getInt("deductionID"));
                deduction.setAmount(rs.getDouble("amount"));
                deduction.setPercentage(rs.getDouble("percentagePerEarning"));
                deduction.setUpperLimit(rs.getDouble("upperLimit"));
                deduction.setStartDate(rs.getDate("startDate"));
                deduction.setEndDate(rs.getDate("endDate"));
                deduction.setDeductionSource(rs.getString("deductionSource"));
                deduction.setEmployeeID(rs.getString("employee"));
                deductions.add(deduction);
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return deductions;
    }
}
