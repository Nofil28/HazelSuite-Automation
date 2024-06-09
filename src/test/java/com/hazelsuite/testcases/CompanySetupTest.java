package com.hazelsuite.testcases;

import com.hazelsuite.pageobjects.CompanySetupPage;
import com.hazelsuite.pageobjects.components.ActionBar;
import com.hazelsuite.pageobjects.components.Modal;
import com.hazelsuite.pageobjects.components.Navigation;
import com.hazelsuite.pageobjects.components.Table;
import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
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

public class CompanySetupTest {

    CompanySetupPage compSetup = new CompanySetupPage();
    Navigation nav = new Navigation();
    ActionBar act = new ActionBar();
    Table table = new Table();
    Modal modal = new Modal();

    String absolutePathToExcel = returnAbsolutePath(PropertiesFile.readKey("relativePathToExcel"));
    int numOfMedSettingsFields = Integer.parseInt(PropertiesFile.readKey("numOfMedSettingsFields"));
    private final By numOfMedSettingsFieldsLoc = By.xpath("//form[@id='medicalSettingsForm']//div[@class='form-group']//input");
    int numOfMedSettingsDropdown = Integer.parseInt(PropertiesFile.readKey("numOfMedSettingsDropdown"));
    private final By numOfMedSettingsDropdownLoc = By.xpath("//div[@class=' css-16go340-control']");
    int leaveTypeColIndex = Integer.parseInt(PropertiesFile.readKey("leaveTypeColIndex"));
    int designationColIndex = Integer.parseInt(PropertiesFile.readKey("designationColIndex"));
    int empTypeColIndex = Integer.parseInt(PropertiesFile.readKey("empTypeColIndex"));
    int deptColIndex = Integer.parseInt(PropertiesFile.readKey("deptColIndex"));
    int teamColIndex = Integer.parseInt(PropertiesFile.readKey("teamColIndex"));

    // TODO: 1/3/2024 Add the verification tests after add operations

    @BeforeClass
    @Parameters({"skipTableDeletion"})
    public void deleteCompanySetupDBTables(String skipTableDeletion)
    {
        if(skipTableDeletion.equals("false"))
        {
            String[] tableNames = {"medicalSettings_before","leaveType_before","designation_before","empType_before","dept_before","team_before"};
            deleteDataFromTables(tableNames);
        }
    }

    @Test(groups = {"Medical Settings Tab"})
    public void isMedicalSettingsTabAccessible() {
        nav.clickOnMedicalSettingsTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/medicalSettings") && compSetup.isEditButtonVisible())
                .withFailMessage("Medical Settings Tab is not accessible")
                .isTrue();
        Log.info("Medical Settings Tab is accessible");

    }

    @Test(groups = {"Medical Settings Tab"})
    public void addDataInMedicalSettingsTab() {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "medicalSettings", 1);
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(data.get((String) keys[i]));
        }
        Log.info("Medical settings data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "medicalSettings_before");

        List<String> employeeTypeList = getListAfterSplitString(data.get("employeeType"), ",");
        compSetup.clickOnEditBtn();
        Assert.assertEquals(findWebElements(numOfMedSettingsFieldsLoc).size(), numOfMedSettingsFields, "The number of fields shown in medical settings form after click on edit button are not as expected: " + numOfMedSettingsFields);
        Log.info("The number of fields shown in medical settings form after click on edit button are as expected");
        Assert.assertEquals(findWebElements(numOfMedSettingsDropdownLoc).size(), numOfMedSettingsDropdown, "The number of dropdown shown in medical settings form after click on edit button are not as expected: " + numOfMedSettingsDropdown);
        Log.info("The number of dropdown shown in medical settings form after click on edit button are as expected");

        compSetup.setMedicalSettingsData(data.get("inPatientLimit"), data.get("outPatientLimit"), employeeTypeList);
        Log.info("Data has been successfully populated in the Medical Settings form");
        compSetup.clickOnSaveBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Medical Settings has been added.");
        Log.info("Success toast message is shown for Medical Settings tab upon clicking on the edit button");
    }

    /*@Test(groups = {"Medical Settings Tab"})
    public void verifyMedicalSettingsTabData() {
        compSetup.clickOnEditBtn();
        List<String> locatorValues = compSetup.getMedicalLocatorsData();
        Log.info("Medical settings data is read from form locators for insertion into DB");
        insertDataIntoDB(locatorValues,"companySettings_after");
        List<String> columnNames = getColumnNames("companySettings_before");
    }*/

    @Test(groups = {"Leave Type Tab"})
    public void isLeaveTypeTabAccessible() {
        nav.clickOnLeaveTypeTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/leaveType") && act.isAddNewBtnDisplayed())
                .withFailMessage("Leave Type Tab is not accessible")
                .isTrue();
        Log.info("Leave Type Tab is accessible");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("leaveTypeName");
    }

    @Test(groups = {"Leave Type Tab"})
    public void addNewLeaveType() {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "leaveType", leaveTypeColIndex);
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(data.get((String) keys[i]));
        }
        Log.info("Leave Type data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "leaveType_before");

        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the leave type is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();
        List<String> employeeTypeList = getListAfterSplitString(data.get("employeeType"), ",");
        compSetup.setLeaveTypeData(data.get("leaveTypeName"), data.get("description"), data.get("creditLeaves"), employeeTypeList, data.get("carryForwardLeaves"));
        Log.info("Data has been successfully populated in the Add Leave Type modal");
        modal.clickOnSubmitBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Leave Type has been added.");
        Log.info("Success toast message is shown for Add Leave Type Type modal upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("leaveTypeName")))
                .withFailMessage("After add leave type, the record in the table is not updated")
                .isTrue();

        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the leave type is: " + afterAddPageCount);

        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add leave type, pagination count is not updated")
                .isFalse();
        Log.info("Leave Type is added successfully in the table");

    }

    @Test(groups = {"Leave Type Tab"})
    public void verifyChangeViewLeaveType() {
        checkAllCheckboxesAndVerifyTableCols();
    }

    @Test(groups = {"Designation Tab"})
    public void isDesignationTabAccessible() {
        nav.clickOnDesignationTab();

        assertThat(doesUrlContain("/designation") && act.isAddNewBtnDisplayed())
                .withFailMessage("Designation Tab is not accessible")
                .isTrue();
        Log.info("Designation Tab is accessible");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("designationName");
    }

    @Test(groups = {"Designation Tab"})
    public void addNewDesignation() {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "designation", designationColIndex);
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(data.get((String) keys[i]));
        }
        Log.info("Designation data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "designation_before");

        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the designation is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();
        compSetup.setDesignationData(data.get("designationName"), data.get("grade"));
        Log.info("Data has been successfully populated in the Add Designation modal");
        modal.clickOnSubmitBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Designation has been added.");
        Log.info("Success toast message is shown for Add Designation modal upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("designationName")))
                .withFailMessage("After add designation, the record in the table is not updated")
                .isTrue();

        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the designation is: " + afterAddPageCount);

        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add designation, pagination count is not updated")
                .isFalse();
        Log.info("Designation is added successfully in the table");

    }

    @Test(groups = {"Designation Tab"})
    public void verifyChangeViewDesignation() {
        checkAllCheckboxesAndVerifyTableCols();
    }

    @Test(groups = {"Employment Type Tab"})
    public void isEmploymentTypeTabAccessible() {
        nav.clickOnCompanySetupTab();
        nav.clickOnEmploymentTypeTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/employmentType") && act.isAddNewBtnDisplayed())
                .withFailMessage("Employment Type Tab is not accessible")
                .isTrue();
        Log.info("Employment Type Tab is accessible");
    }

    @Test(groups = {"Employment Type Tab"})
    public void addNewEmploymentType() {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "employmentType", empTypeColIndex);
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(data.get((String) keys[i]));
        }
        Log.info("Employment Type data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "empType_before");

        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the employment Type is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();
        compSetup.setEmploymentTypeData(data.get("empType"));
        Log.info("Data has been successfully populated in the Add Employment Type modal");
        modal.clickOnSubmitBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Employment Type has been added.");
        Log.info("Success toast message is shown for Add Employment Type modal upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("empType")))
                .withFailMessage("After add Employment Type, the record in the table is not updated")
                .isTrue();


        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the Employment Type is: " + afterAddPageCount);

        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add Employment Type, pagination count is not updated")
                .isFalse();
        Log.info("Employment Type is added successfully in the table");

    }

    @Test(groups = {"Departments Tab"})
    public void isDepartmentsTabAccessible() {
        //nav.clickOnCompanySetupTab();
        nav.clickOnDepartmentsTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/departments") && act.isAddNewBtnDisplayed())
                .withFailMessage("Departments Tab is not accessible")
                .isTrue();
        Log.info("Departments Tab is accessible");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("departmentName");

    }

    @Test(groups = {"Departments Tab"})
    public void addNewDepartmentName() {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "department", deptColIndex);
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(data.get((String) keys[i]));
        }
        Log.info("Department data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "dept_before");

        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the Department is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();
        compSetup.setDepartmentNameData(data.get("deptName"));
        Log.info("Data has been successfully populated in the Add Department modal");
        modal.clickOnSubmitBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Departments has been added.");
        Log.info("Success toast message is shown for Add Department modal upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("deptName")))
                .withFailMessage("After add Department, the record in the table is not updated")
                .isTrue();

        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the Department is: " + afterAddPageCount);

        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add Department, pagination count is not updated")
                .isFalse();
        Log.info("Department is added successfully in the table");

    }

    @Test(groups = {"Departments Tab"})
    public void verifyChangeViewDepartments() {
        checkAllCheckboxesAndVerifyTableCols();
    }

    @Test(groups = {"Teams Tab"})
    public void isTeamsTabAccessible() {
        nav.clickOnTeamsTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/teams") && act.isAddNewBtnDisplayed())
                .withFailMessage("Teams Tab is not accessible")
                .isTrue();
        Log.info("Teams Tab is accessible");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("teamName");

    }

    @Test(groups = {"Teams Tab"})
    public void addNewTeamNameAndDeptName() {
        List<String> locatorValues = new ArrayList<>();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "teams", teamColIndex);
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(data.get((String) keys[i]));
        }
        Log.info("Teams data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "team_before");

        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the Team is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();
        compSetup.setTeamNameAndDeptNameData(data.get("teamName"), data.get("dept"));
        Log.info("Data has been successfully populated in the Add Teams modal");
        modal.clickOnSubmitBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Teams has been added.");
        Log.info("Success toast message is shown for Add Teams modal upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("teamName")))
                .withFailMessage("After add Team, the record in the table is not updated")
                .isTrue();

        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the Team is: " + afterAddPageCount);

        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add Team, pagination count is not updated")
                .isFalse();
        Log.info("Team is added successfully in the table");

    }

    @Test(groups = {"Teams Tab"})
    public void verifyChangeViewTeams() {
        checkAllCheckboxesAndVerifyTableCols();
    }

    @Test(groups = {"Departments Tab"})
    public void updateDeptWithDeptHead() {
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "department", deptColIndex);
        nav.clickOnCompanySetupTab();
        nav.clickOnDepartmentsTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        table.doubleClickOnTableFirstRow();
        compSetup.setDepartmentHeadData(data.get("deptHead"));
        modal.clickOnUpdateBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Departments has been updated.");
        Log.info("Success toast message is shown for Update Department modal upon clicking on the update button");
    }

    @Test(groups = {"Teams Tab"})
    public void updateTeamWithDeptNameAndTeamLead() {
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "teams", teamColIndex);
        nav.clickOnTeamsTab();

        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        table.doubleClickOnTableFirstRow();
        compSetup.setTeamLeadData(data.get("teamLead"));
        modal.clickOnUpdateBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Teams has been updated.");
        Log.info("Success toast message is shown for Update Teams modal upon clicking on the update button");

    }

    private void checkAllCheckboxesAndVerifyTableCols() {
        act.clickOnChangeViewBtn();
        int checkboxes = act.getChangeViewChkBoxes();
        act.checkAllCheckboxes();
        int tableCols = table.getNumOfTableCols();
        assertThat(checkboxes).isEqualTo(tableCols);
        Log.info("The number of checkboxes checked in change view modal and number of table columns match");
    }

}
