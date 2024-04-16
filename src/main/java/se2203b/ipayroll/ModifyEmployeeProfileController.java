/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.ipayroll;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;

/**
 * FXML Controller class
 *
 * @author Abdelkader
 */
public class ModifyEmployeeProfileController implements Initializable {

    //region Variable declarations
    @FXML
    private TabPane tb;
    @FXML
    private ComboBox id;
    @FXML
    private TextField fullName, city, province, phone, SIN, postalCode,
            martialStatus, jobName, skillCode, hoursPerWeek, valueTextField, ratePerHourTextField;
    @FXML
    private DatePicker DOB, DOH, DOLP, startDate, endDate;
    @FXML
    Button cancelBtn, saveBtn;

    @FXML
    ComboBox<String> payTypeComboBox, statementTypeComboBox, earningSourceComboBox, deductionSourceComboBox;

    @FXML
    private TextField deductionValueField, percentOfEarningField, upperLimitField;

    @FXML
    private DatePicker deductionStartDate, deductionEndDate;

    @FXML
    private CheckBox exemptCheckBox;

    @FXML
    private TableView<Earning> earningsTable;

    @FXML
    private TableView<Deduction> deductionsTable;

    @FXML
    private TableColumn<Earning, String> earningSourceColumn;
    @FXML
    private TableColumn<Earning, Double> valueColumn;
    @FXML
    private TableColumn<Earning, Double> ratePerHourColumn;
    @FXML
    private TableColumn<Earning, java.sql.Date> startDateColumn;
    @FXML
    private TableColumn<Earning, java.sql.Date> endDateColumn;

    @FXML
    private TableColumn<Deduction, String> deductionSourceColumn;
    @FXML
    private TableColumn<Deduction, Double> deductionValueColumn;
    @FXML
    private TableColumn<Deduction, Double> percentOfEarningColumn;
    @FXML
    private TableColumn<Deduction, Double> upperLimitColumn;
    @FXML
    private TableColumn<Deduction, java.sql.Date> deductionStartDateColumn;
    @FXML
    private TableColumn<Deduction, java.sql.Date> deductionEndDateColumn;

    /******************/


    private DataStore userAccountTable;
    private UserAccount userAccount;
    private DataStore employeeTable, earningTableAdapter, earningSourceTableAdapter, deductionTableAdapter,
    deductionSourceTableAdapter;
    private Employee employee = null;
    private IPayrollController iPAYROLLController;

    final ObservableList<String> data = FXCollections.observableArrayList();
    private ObservableList<String> earningSourceData = FXCollections.observableArrayList();

    private ObservableList<String> deductionSourceData = FXCollections.observableArrayList();

    private List<Earning> earningList = new ArrayList<>();

    private List<Deduction> deductionList = new ArrayList<>();


    private int earningID, earningSources, deductionID, deductionSources;

    //endregion

    public void setDataStore(DataStore account, DataStore profile, DataStore earning, DataStore earningSource,
                             DataStore deduction, DataStore deductionSource) {
        userAccountTable = account;
        employeeTable = profile;

        earningTableAdapter = earning;
        earningSourceTableAdapter = earningSource;

        deductionTableAdapter = deduction;
        deductionSourceTableAdapter = deductionSource;

        buildData();
    }

    public void setIPayrollController(IPayrollController controller) {
        iPAYROLLController = controller;
    }

    @FXML
    public void getProfile() {
        try {

            //region Populating personal information
            employee = (Employee) employeeTable.findOneRecord(this.id.getValue().toString());
            this.fullName.setText(employee.getFullName());
            this.city.setText(employee.getCity());
            this.province.setText(employee.getProvince());
            this.phone.setText(employee.getPhone());
            this.postalCode.setText(employee.getPhone());
            this.SIN.setText(employee.getSIN());
            this.martialStatus.setText(employee.getMartialStatus());
            this.jobName.setText(employee.getJobName());
            this.skillCode.setText(employee.getSkillCode());
            Date utilDOB = new Date(employee.getDOB().getTime());
            this.DOB.setValue(utilDOB.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date utilDOH = new Date(employee.getDOH().getTime());
            this.DOH.setValue(utilDOH.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            Date utilDOLP = new Date(employee.getDOLP().getTime());
            this.DOLP.setValue(utilDOLP.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            payTypeComboBox.setValue(employee.getPayType());
            statementTypeComboBox.setValue(employee.getEarningStatementType());
            hoursPerWeek.setText("" + employee.getWorkHours());
            userAccount = (UserAccount) userAccountTable.findOneRecord(employee.getUserAccount().getUserAccountName());
            //endregion

            //region Populating earnings data
            earningsTable.getItems().clear();
            List<Object> earnings = earningTableAdapter.getAllRecords(employee);

            for (Object earning : earnings) {
                earningsTable.getItems().add((Earning) earning);
            }
            //endregion

            // region Populating deduction data
            deductionsTable.getItems().clear();

            List<Object> deductions = deductionTableAdapter.getAllRecords(employee);

            for (Object deduction : deductions) {
                deductionsTable.getItems().add((Deduction) deduction);
            }
            //endregion

        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }
    }

    @FXML
    public void populatePayType() {
        ArrayList<String> payTypes = new ArrayList<>();
        payTypes.add("Hourly with card");
        payTypes.add("Hourly without card");
        payTypes.add("Salaried");
        payTypeComboBox.getItems().addAll(payTypes);
    }

    @FXML
    public void populateStatementType() {
        ArrayList<String> statementTypes = new ArrayList<>();
        statementTypes.add("Weekly");
        statementTypes.add("Bi-weekly");
        statementTypes.add("Semi-monthly");
        statementTypes.add("Monthly");
        statementTypes.add("Special");
        statementTypeComboBox.getItems().addAll(statementTypes);
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void update() {
        try {
            Employee oneEmployee = new Employee();
            oneEmployee.setID(employee.getID());
            oneEmployee.setFullName(this.fullName.getText());
            oneEmployee.setCity(this.city.getText());
            oneEmployee.setProvince(this.province.getText());
            oneEmployee.setPostalCode(this.postalCode.getText());
            oneEmployee.setPhone(this.phone.getText());
            oneEmployee.setSIN(this.SIN.getText());
            oneEmployee.setMartialStatus(this.martialStatus.getText());
            oneEmployee.setJobName(this.jobName.getText());
            oneEmployee.setSkillCode(this.skillCode.getText());
            oneEmployee.setDOB(java.sql.Date.valueOf(this.DOB.getValue()));
            oneEmployee.setDOH(java.sql.Date.valueOf(this.DOH.getValue()));
            oneEmployee.setDOLP(java.sql.Date.valueOf(this.DOLP.getValue()));
            oneEmployee.setWorkHours(Double.parseDouble(hoursPerWeek.getText()));
            oneEmployee.setExempt(exemptCheckBox.isSelected());
            oneEmployee.setPayType(payTypeComboBox.getValue());
            oneEmployee.setEarningStatementType(statementTypeComboBox.getValue());

            oneEmployee.setUserAccount(userAccount);

            updateEarnings();
            updateDeductions();
            employeeTable.updateRecord(oneEmployee);
        } catch (SQLException ex) {
            //iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
            System.out.println(ex);
        }

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void updateSources() {
        earningSourceComboBox.setItems(earningSourceData);
        deductionSourceComboBox.setItems(deductionSourceData);
    }

    @FXML
    public void addEarning() throws SQLException {

        // Check if the selected earning source already exists in the database
        boolean earningSourceExists = false;
        for (int i = 0; i < earningSourceData.size(); i++) {
            if (earningSourceComboBox.getValue().equals(earningSourceData.get(i))) {
                earningSourceExists = true;
                break;
            }
        }

        // If the earning source doesn't exist, add it to the database
        if (!earningSourceExists) {
            earningSources += 1;
            EarningSource earningSource = new EarningSource(earningSources, earningSourceComboBox.getValue());
            earningSourceData.add(earningSourceComboBox.getValue());
            earningSourceTableAdapter.addNewRecord(earningSource);
        }

        Earning earning = new Earning();

        earning.setEarningID(earningID);
        earning.setAmount(Double.valueOf(valueTextField.getText()));
        earning.setRatePerHour(Double.valueOf(ratePerHourTextField.getText()));
        earning.setEarningSourceName(earningSourceComboBox.getValue());
        earning.setEmployeeID(employee.getID());
        earning.setStartDate(java.sql.Date.valueOf(startDate.getValue()));
        earning.setEndDate(java.sql.Date.valueOf(endDate.getValue()));

        // add all the earning source, value, rate per hour, start date, and end date into a table called earningsTable
        earningsTable.getItems().add(earning);
        earningID += 1;
        earningList.add(earning);
    }

    @FXML
    public void addDeduction() throws SQLException {
        // Check if the selected earning source already exists in the database
        boolean deductionSourceExists = false;

        for (int i = 0; i < deductionSourceData.size(); i++) {
            if (deductionSourceComboBox.getValue().equals(deductionSourceData.get(i))) {
                deductionSourceExists = true;
                break;
            }
        }

        // If the earning source doesn't exist, add it to the database
        if (!deductionSourceExists) {
            deductionSources += 1;
            DeductionSource deductionSource = new DeductionSource(deductionSources, deductionSourceComboBox.getValue());
            deductionSourceData.add(deductionSourceComboBox.getValue());
            deductionSourceTableAdapter.addNewRecord(deductionSource);
        }

        Deduction deduction = new Deduction();

        deduction.setDeductionID(deductionID);
        deduction.setAmount(Double.parseDouble(deductionValueField.getText()));
        deduction.setPercentage(Double.parseDouble(percentOfEarningField.getText()));
        deduction.setUpperLimit(Double.parseDouble(upperLimitField.getText()));
        deduction.setDeductionSource(deductionSourceComboBox.getValue());
        deduction.setEmployeeID(employee.getID());
        deduction.setStartDate(java.sql.Date.valueOf(deductionStartDate.getValue()));
        deduction.setEndDate(java.sql.Date.valueOf(deductionEndDate.getValue()));

        // add all the earning source, value, rate per hour, start date, and end date into a table called deductionsTable
        deductionsTable.getItems().add(deduction);
        deductionID += 1;
        deductionList.add(deduction);
    }

    @FXML
    public void delete() {
        // Check if there is a related user account
        if (employee.getUserAccount().getUserAccountName() == null) {
            try {
                employeeTable.deleteOneRecord(employee.getID());
            } catch (SQLException ex) {
                iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
            }
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        } else {
            iPAYROLLController.displayAlert("Please delete the associated user account first");
        }
    }

    @FXML
    public void deleteEarning() throws SQLException {
        Earning selectedEarning = earningsTable.getSelectionModel().getSelectedItem();

        if (selectedEarning != null) {
            earningTableAdapter.deleteOneRecord(String.valueOf(selectedEarning.getEarningID()));
            earningsTable.getItems().remove(selectedEarning);
            earningsTable.refresh();
        }
    }

    @FXML
    public void deleteDeduction() throws SQLException {
        Deduction selectedDeduction = deductionsTable.getSelectionModel().getSelectedItem();

        if (selectedDeduction != null) {
            deductionTableAdapter.deleteOneRecord(String.valueOf(selectedDeduction.getDeductionID()));
            deductionsTable.getItems().remove(selectedDeduction);
            deductionsTable.refresh();
        }
    }

    public void updateEarnings() throws SQLException {
        ObservableList<Earning> earnings = earningsTable.getItems();

        for (Earning earning : earnings) {
            earningTableAdapter.updateRecord(earning);
        }
    }

    public void updateDeductions() throws SQLException {
        ObservableList<Deduction> deductions = deductionsTable.getItems();

        for (Deduction deduction : deductions) {
            deductionTableAdapter.updateRecord(deduction);
        }
    }

    public void buildData() {
        try {
            data.addAll(employeeTable.getKeys());
            earningID = earningTableAdapter.getKeys().size() + 1;
            earningSources = earningSourceTableAdapter.getKeys().size() + 1;
            deductionID = deductionTableAdapter.getKeys().size() + 1;
            deductionSources = deductionSourceTableAdapter.getKeys().size() + 1;

            List<Object> list = earningSourceTableAdapter.getAllRecords();
            ObservableList<Object> observableArrayList = FXCollections.observableArrayList(list);

            ArrayList<String> earningSourceNames = new ArrayList<>();

            for (int i = 0; i < observableArrayList.size(); i++) {
                earningSourceNames.add(((EarningSource)observableArrayList.get(i)).getName());
            }

            earningSourceData.addAll(earningSourceNames);

            list = deductionSourceTableAdapter.getAllRecords();
            observableArrayList = FXCollections.observableArrayList(list);

            ArrayList<String> deductionSourceNames = new ArrayList<>();

            for (int i = 0; i < observableArrayList.size(); i++) {
                deductionSourceNames.add(((DeductionSource)observableArrayList.get(i)).getName());
            }

            deductionSourceData.addAll(deductionSourceNames);

        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setItems(data);
        earningSourceComboBox.setItems(earningSourceData);
        deductionSourceComboBox.setItems(deductionSourceData);
        populatePayType();
        populateStatementType();

        //region Deal with cells and cell input for earningsTable TableView
        // Set cell value factories for each column
        earningSourceColumn.setCellValueFactory(new PropertyValueFactory<>("earningSourceName"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        ratePerHourColumn.setCellValueFactory(new PropertyValueFactory<>("ratePerHour"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        // Make all columns editable, handle new input
        earningSourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }, earningSourceData));

        earningSourceColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Earning, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Earning, String> event) {
                updateSources();
                Earning earning = event.getRowValue();
                earning.setEarningSourceName(event.getNewValue());
            }
        });

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        valueColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Earning, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Earning, Double> event) {
                Earning earning = event.getRowValue();
                earning.setAmount(event.getNewValue());
            }
        });

        ratePerHourColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ratePerHourColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Earning, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Earning, Double> event) {
                Earning earning = event.getRowValue();
                earning.setRatePerHour(event.getNewValue());
            }
        });

        startDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<java.sql.Date>() {
            @Override
            public String toString(java.sql.Date object) {
                // Convert the java.sql.Date object to a string representation
                if (object != null) {
                    return object.toString();
                } else {
                    return "";
                }
            }

            @Override
            public java.sql.Date fromString(String string) {
                // Convert the string back to a java.sql.Date object
                if (string != null && !string.isEmpty()) {
                    try {
                        return java.sql.Date.valueOf(string);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid input gracefully
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }));
        startDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Earning, java.sql.Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Earning, java.sql.Date> event) {
                Earning earning = event.getRowValue();
                earning.setStartDate(event.getNewValue());
            }
        });

        endDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<java.sql.Date>() {
            @Override
            public String toString(java.sql.Date object) {
                // Convert the java.sql.Date object to a string representation
                if (object != null) {
                    return object.toString();
                } else {
                    return "";
                }
            }

            @Override
            public java.sql.Date fromString(String string) {
                // Convert the string back to a java.sql.Date object
                if (string != null && !string.isEmpty()) {
                    try {
                        return java.sql.Date.valueOf(string);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid input gracefully
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }));
        endDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Earning, java.sql.Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Earning, java.sql.Date> event) {
                Earning earning = event.getRowValue();
                earning.setEndDate(event.getNewValue());
            }
        });

        // Enable editing for the earnings table
        earningsTable.setEditable(true);
        earningsTable.getSelectionModel().setCellSelectionEnabled(true);

        //endregion

        //region Deal with cells and cell input for deductionsTable TableView
        deductionSourceColumn.setCellValueFactory(new PropertyValueFactory<>("deductionSource"));
        deductionValueColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        percentOfEarningColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        upperLimitColumn.setCellValueFactory(new PropertyValueFactory<>("upperLimit"));
        deductionStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        deductionEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        deductionSourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }, deductionSourceData));

        deductionSourceColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Deduction, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Deduction, String> event) {
                updateSources();
                Deduction deduction = event.getRowValue();
                deduction.setDeductionSource(event.getNewValue());
            }
        });

        deductionValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        deductionValueColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Deduction, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Deduction, Double> event) {
                Deduction deduction = event.getRowValue();
                deduction.setAmount(event.getNewValue());
            }
        });

        percentOfEarningColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        percentOfEarningColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Deduction, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Deduction, Double> event) {
                Deduction deduction = event.getRowValue();
                deduction.setPercentage(event.getNewValue());
            }
        });

        upperLimitColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        upperLimitColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Deduction, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Deduction, Double> event) {
                Deduction deduction = event.getRowValue();
                deduction.setUpperLimit(event.getNewValue());
            }
        });

        // Set cell factory for start date column
        deductionStartDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<java.sql.Date>() {
            @Override
            public String toString(java.sql.Date object) {
                // Convert the java.sql.Date object to a string representation
                if (object != null) {
                    return object.toString();
                } else {
                    return "";
                }
            }

            @Override
            public java.sql.Date fromString(String string) {
                // Convert the string back to a java.sql.Date object
                if (string != null && !string.isEmpty()) {
                    try {
                        return java.sql.Date.valueOf(string);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid input gracefully
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }));

        // Set cell factory for end date column
        deductionEndDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<java.sql.Date>() {
            @Override
            public String toString(java.sql.Date object) {
                // Convert the java.sql.Date object to a string representation
                if (object != null) {
                    return object.toString();
                } else {
                    return "";
                }
            }

            @Override
            public java.sql.Date fromString(String string) {
                // Convert the string back to a java.sql.Date object
                if (string != null && !string.isEmpty()) {
                    try {
                        return java.sql.Date.valueOf(string);
                    } catch (IllegalArgumentException e) {
                        // Handle invalid input gracefully
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }));

        deductionStartDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Deduction, java.sql.Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Deduction, java.sql.Date> event) {
                Deduction deduction = event.getRowValue();
                deduction.setStartDate(event.getNewValue());
            }
        });

        deductionEndDateColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Deduction, java.sql.Date>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Deduction, java.sql.Date> event) {
                Deduction deduction = event.getRowValue();
                deduction.setEndDate(event.getNewValue());
            }
        });

        deductionsTable.setEditable(true);
        deductionsTable.getSelectionModel().setCellSelectionEnabled(true);

        //endregion
    }
}
