/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se2203b.ipayroll;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AddNewEmployeeProfileController implements Initializable {

    //region Variable Declarations
    @FXML
    private TabPane tb;
    @FXML
    private TextField id;
    @FXML
    private TextField fullName;
    @FXML
    private TextField city;
    @FXML
    private TextField province;
    @FXML
    private TextField phone;
    @FXML
    private TextField SIN;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField martialStatus;
    @FXML
    private TextField jobName;
    @FXML
    private TextField skillCode;
    @FXML
    private TextField hoursPerWeek, valueTextField, ratePerHourTextField;

    @FXML
    private DatePicker DOB;
    @FXML
    private DatePicker DOH;
    @FXML
    private DatePicker DOLP;
    @FXML
    Button cancelBtn;
    @FXML
    Button saveBtn, addBtn;

    @FXML
    private ComboBox<String> payTypeComboBox, statementTypeComboBox, earningSourceComboBox;

    @FXML
    private DatePicker startDate, endDate;

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
    private TableColumn<Earning, Date> startDateColumn;
    @FXML
    private TableColumn<Earning, Date> endDateColumn;

    @FXML
    private TableColumn<Deduction, String> deductionSourceColumn;
    @FXML
    private TableColumn<Deduction, Double> deductionValueColumn;
    @FXML
    private TableColumn<Deduction, Double> percentOfEarningColumn;
    @FXML
    private TableColumn<Deduction, Double> upperLimitColumn;
    @FXML
    private TableColumn<Deduction, Date> deductionStartDateColumn;
    @FXML
    private TableColumn<Deduction, Date> deductionEndDateColumn;

    @FXML
    private CheckBox exemptCheckBox;

    @FXML
    private ComboBox<String> deductionSourceComboBox;

    @FXML
    private TextField deductionValueField, percentOfEarningField, upperLimitField;

    @FXML
    private DatePicker deductionStartDate, deductionEndDate;

    private DataStore userAccountAdapter;
    private DataStore employeeTableAdapter, earningTableAdapter, earningSourceTableAdapter, deductionTableAdapter,
            deductionSourceTableAdapter;
    private IPayrollController iPAYROLLController;

    private ObservableList<String> earningSourceData = FXCollections.observableArrayList();
    private ObservableList<String> deductionSourceData = FXCollections.observableArrayList();

    private List<Earning> earningList = new ArrayList<>();
    private List<Deduction> deductionList = new ArrayList<>();

    private int earningID, earningSources, deductionID, deductionSources;

    //endregion

    public void setDataStore(DataStore account, DataStore profile, DataStore earning, DataStore earningSource,
                             DataStore deduction, DataStore deductionSource) {

        userAccountAdapter = account;
        employeeTableAdapter = profile;

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
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void save() {
        try {
            UserAccount account = new UserAccount();
            Employee employee = new Employee();
            employee.setID(this.id.getText());
            employee.setFullName(this.fullName.getText());
            employee.setCity(this.city.getText());
            employee.setProvince(this.province.getText());
            employee.setPostalCode(this.postalCode.getText());
            employee.setPhone(this.phone.getText());
            employee.setSIN(this.SIN.getText());
            employee.setMartialStatus(this.martialStatus.getText());
            employee.setJobName(this.jobName.getText());
            employee.setSkillCode(this.skillCode.getText());
            employee.setDOB(java.sql.Date.valueOf(this.DOB.getValue()));
            employee.setDOH(java.sql.Date.valueOf(this.DOH.getValue()));
            employee.setDOLP(java.sql.Date.valueOf(this.DOLP.getValue()));
            employee.setPayType(payTypeComboBox.getValue());
            employee.setWorkHours(Double.parseDouble(hoursPerWeek.getText()));
            employee.setEarningStatementType(statementTypeComboBox.getValue());
            employee.setExempt(exemptCheckBox.isSelected());
            employee.setUserAccount(account);

            employeeTableAdapter.addNewRecord(employee);

            for (int i = 0; i < earningList.size(); i++) {
                earningTableAdapter.addNewRecord(earningList.get(i));
            }

            for (int i = 0; i < deductionList.size(); i++) {
                deductionTableAdapter.addNewRecord(deductionList.get(i));
            }


        } catch (SQLException ex) {
            iPAYROLLController.displayAlert("ERROR: " + ex.getMessage());
        }

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
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


    public void buildData() {
        try {
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
            throw new RuntimeException(ex);
        }
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
        earning.setEmployeeID(id.getText());
        earning.setStartDate(Date.valueOf(startDate.getValue()));
        earning.setEndDate(Date.valueOf(endDate.getValue()));

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
        deduction.setEmployeeID(id.getText());
        deduction.setStartDate(Date.valueOf(deductionStartDate.getValue()));
        deduction.setEndDate(Date.valueOf(deductionEndDate.getValue()));

        // add all the earning source, value, rate per hour, start date, and end date into a table called deductionsTable
        deductionsTable.getItems().add(deduction);
        deductionID += 1;
        deductionList.add(deduction);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populatePayType();
        populateStatementType();
        earningSourceComboBox.setItems(earningSourceData);
        deductionSourceComboBox.setItems(deductionSourceData);

        // Set cell value factories for each column
        earningSourceColumn.setCellValueFactory(new PropertyValueFactory<>("earningSourceName"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        ratePerHourColumn.setCellValueFactory(new PropertyValueFactory<>("ratePerHour"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        deductionSourceColumn.setCellValueFactory(new PropertyValueFactory<>("deductionSource"));
        deductionValueColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        percentOfEarningColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        upperLimitColumn.setCellValueFactory(new PropertyValueFactory<>("upperLimit"));
        deductionStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        deductionEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
    }
}
