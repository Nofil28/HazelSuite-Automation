package com.hazelsuite.pageobjects;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.hazelsuite.utilities.CommonUtil.*;

public class CompanySetupPage {

    // Locators for Medical Settings Tab
    private final By editBtn = By.xpath("//button[contains(@class,'btn-primary')]");
    private final By inPatientLimit = By.id("inpatientMedicalLimit");
    private final By outPatientLimit = By.id("outpatientMedicalLimit");
    private final By employeeType = By.xpath("//div[@class=' css-16go340-control']");
    private final By saveBtn = By.xpath("//button[@form='medicalSettingsForm']");
    private final By[] medicalLocatorArr = new By[]{inPatientLimit,outPatientLimit,employeeType};
    private final List<String> medicalLocatorValues = new ArrayList<>();
    private final By numOfMultiSelects = By.xpath("//div[@class='css-12jo7m5']");

    // Locators for Leave Type Tab
    private final By leaveTypeName = By.id("leaveTypeName");
    private final By description = By.id("description");
    private final By creditLeaves = By.id("creditLeaves");
    private final By carryForwardLeaves = By.id("carryForwardLeaves");

    // Locators for Designation Tab
    private final By designationName = By.id("designationName");
    private final By grade = By.id("grade");

    // Locators for Employment Type Tab
    private final By employmentType = By.id("employmentType");

    // Locators for Departments tab
    private final By deptName = By.id("departmentName");
    private final By deptHead = By.xpath("//div[@class='react-select__control css-3lch9h-control']");
    private final By customDropdownMenu = By.xpath("//div[@class='react-select__menu css-26l3qy-menu']");

    // Locators for Teams Tab
    private final By teamName = By.id("teamName");
    private final By dept = By.xpath("(//div[@class='react-select__control css-3lch9h-control'])[1]");
    private final By teamLead = By.xpath("(//div[@class='react-select__control css-3lch9h-control'])[2]");

    public boolean isEditButtonVisible()
    {
        return isElementDisplayed(editBtn);
    }

    public void clickOnSaveBtn()
    {
        clickOn(saveBtn);
        Log.info("Click on Save button");
    }

    public void clickOnEditBtn()
    {
        clickOn(editBtn);
        Log.info("Click on Edit button");
    }

    public void setMedicalSettingsData(String inPatientLimit, String outPatientLimit, List<String> employeeTypeList)
    {
        sendText(this.inPatientLimit,inPatientLimit);
        sendText(this.outPatientLimit,outPatientLimit);
        clickOn(employeeType);
        try {
            int count = findWebElements(numOfMultiSelects).size();
            for(int i=0;i<count;i++) {
                clickOnBackSpaceKey();
            }
        }
        catch (NoSuchElementException e){
            Log.info("Employee type field is already empty");
        }

        for(String empType : employeeTypeList)
        {
            sendKeysActionClass(empType);
            clickOnEnterKey();
        }
    }

    public List<String> getMedicalLocatorsData()
    {
        for(int i=0;i<medicalLocatorArr.length;i++)
        {
            medicalLocatorValues.add(getAttributeValue("value",medicalLocatorArr[i]));
        }
        return medicalLocatorValues;
    }

    public void setLeaveTypeData(String leaveTypeName, String description, String creditLeaves, List<String> employeeTypeList, String carryForwardLeaves)
    {
        sendText(this.leaveTypeName,leaveTypeName);
        sendText(this.description,description);
        sendText(this.creditLeaves, creditLeaves);
        clickOn(employeeType);

        for(String empType : employeeTypeList)
        {
            sendKeysActionClass(empType);
            clickOnEnterKey();
        }
        sendText(this.carryForwardLeaves, carryForwardLeaves);
    }

    public void setDesignationData(String designationName, String grade)
    {
        sendText(this.designationName, designationName);
        sendText(this.grade, grade);
    }

    public void setEmploymentTypeData(String empType)
    {
        sendText(employmentType, empType);
    }

    public void setDepartmentNameData(String deptName)
    {
        sendText(this.deptName, deptName);
    }

    public void setDepartmentHeadData(String deptHead)
    {
        // TODO: 1/15/2024 Remove the hardcoded sleep code and locate actual dropdown option locator 
        clickOn(this.deptHead);
        //waitUntilElementVisible(customDropdownMenu);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendKeysActionClass(deptHead);
        clickOnEnterKey();
    }

    public void setTeamNameAndDeptNameData(String teamName, String dept)
    {
        sendText(this.teamName, teamName);
        clickOn(this.dept);
        sendKeysActionClass(dept);
        clickOnEnterKey();
    }

    public void setTeamLeadData(String teamLead)
    {
        clickOn(this.teamLead);
        sendKeysActionClass(teamLead);
        clickOnEnterKey();
    }

}
