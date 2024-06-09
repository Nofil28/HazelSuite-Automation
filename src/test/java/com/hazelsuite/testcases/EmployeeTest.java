package com.hazelsuite.testcases;

import com.hazelsuite.pageobjects.EmployeePage;
import com.hazelsuite.pageobjects.components.ActionBar;
import com.hazelsuite.pageobjects.components.Modal;
import com.hazelsuite.pageobjects.components.Navigation;
import com.hazelsuite.pageobjects.components.Table;
import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.*;
import static com.hazelsuite.utilities.ExcelFileUtil.readExcelColumns;
import static com.hazelsuite.utilities.SQLiteUtil.deleteDataFromTables;
import static com.hazelsuite.utilities.SQLiteUtil.insertDataIntoDB;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest
{
    EmployeePage emp = new EmployeePage();
    Navigation nav = new Navigation();
    ActionBar act = new ActionBar();
    Table table = new Table();

    String absolutePathToExcel = returnAbsolutePath(PropertiesFile.readKey("relativePathToExcel"));
    int empColIndex = Integer.parseInt(PropertiesFile.readKey("empColIndex"));

    @BeforeClass
    public void deleteEmployeeDBTables()
    {
        String[] tableNames = {"addEmp_before"};
        deleteDataFromTables(tableNames);
    }

    @Test(groups = {"Employees Tab"})
    public void isEmployeesTabAccessible()
    {
        nav.clickOnEmployeesTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/employees") && act.isAddNewBtnDisplayed())
                .withFailMessage("Employees Tab is not accessible")
                .isTrue();
        Log.info("Employees Tab is accessible");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("fullName");
    }

    @Test(groups = {"Employees Tab"})
    public void addNewEmployee()
    {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> dataMap = readExcelColumns(absolutePathToExcel, "employee",empColIndex);
        Object[] keys = dataMap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(dataMap.get((String) keys[i]));
        }
        Log.info("Employee data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "addEmp_before");

        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the Employee is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();
        List<String> valueList = new ArrayList<>(dataMap.values());

        emp.setEmployeeData(valueList);
        Log.info("Data has been successfully populated in the Add Employee form");
        emp.clickOnCreateBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Employeeshas_been_created");
        Log.info("Success toast message is shown for Add Employee form upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        String fullName = dataMap.get("firstName") + " " + dataMap.get("lastName");
        assertThat(table.matchValueAtFirstRowColumnOne(fullName))
                .withFailMessage("After add employee, the record in the table is not updated")
                .isTrue();

        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the Employee is: " + afterAddPageCount);
        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add employee, pagination count is not updated")
                .isFalse();

        Log.info("Employee is added successfully in the table");
    }

    @Test(groups = {"Employees Tab"})
    public void verifyChangeViewEmployees() {
        checkAllCheckboxesAndVerifyTableCols();
    }

    private void checkAllCheckboxesAndVerifyTableCols() {
        act.clickOnChangeViewBtn();
        int checkboxes = act.getChangeViewChkBoxes();
        act.checkAllCheckboxes();
        int tableCols = table.getNumOfTableCols() - 1;
        assertThat(checkboxes).isEqualTo(tableCols);
        Log.info("The number of checkboxes checked in change view modal and number of table columns match");
    }

}
