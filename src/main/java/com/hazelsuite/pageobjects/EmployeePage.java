package com.hazelsuite.pageobjects;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;

import java.util.List;

import static com.hazelsuite.utilities.CommonUtil.*;

public class EmployeePage
{
    // Employee Details locators
    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By phoneNumber = By.id("phoneNumber"); // must be unique
    private final By genderId = By.id("genderId");
    private final By companyEmail = By.id("companyEmail"); // must be unique
    private final By locationId = By.id("locationId");
    private final By timezoneId = By.id("timezoneId");
    private final By doj = By.id("doj");
    private final By employmentTypesId = By.id("employmentTypesId");
    private final By employmentTypeEndDate = By.id("employmentTypeEndDate");
    private final By designationId = By.id("designationId");
    private final By departmentId = By.id("departmentId");
    private final By team = By.xpath("//div[@class=' css-16go340-control']");
    private final By createBtn = By.xpath("//button[@form='EmployeeFormEmployeeInfo']");
    private final By[] locatorArr = new By[]{firstName,lastName,phoneNumber,genderId,companyEmail,locationId,timezoneId,doj
            ,employmentTypesId,employmentTypeEndDate,designationId,departmentId,team};


    public void clickOnCreateBtn()
    {
        clickOn(createBtn);
        Log.info("Click on the Create button");
    }
    public void setEmployeeData(List<String> valueList)
    {
        for(int i=0; i<valueList.size(); i++)
        {
            if(i==3 || i==5 || i==6 || i==8 || i==10 || i==11)
                selectOptionFromDropdown(locatorArr[i], valueList.get(i));
            else if(i==12){
                clickOn(locatorArr[i]);
                sendKeysActionClass(valueList.get(i));
                clickOnEnterKey();
            }
            else
                sendText(locatorArr[i], valueList.get(i));
        }
    }

}
