package com.hazelsuite.pageobjects;

import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.*;

public class LocationPage {
    // fields in General tab
    private final By locName = By.id("name");
    private final By contactPrsn = By.id("contactPerson");
    private final By phoneNum = By.id("phoneNumber");
    private final By address = By.id("address");
    private final By country = By.id("country");
    private final By city = By.id("city");
    private final By province = By.id("province");
    private final By postCode = By.id("postalCode");
    private final By timeZone = By.id("timezoneId");

    // fields in Working Days tab
    private final By workDaysTab = By.xpath("//a[text()='Working Days']");
    private final By editBtn = By.xpath("//button[@class='table-action-btn']");
    private String workDayChkBox = "//input[@id='checkbox-criteriaValues-%s']";
    private final By weekDays = By.xpath("//label[@class='form-check-label']");
    private String fromTime = "//input[@id='checkbox-criteriaValues-%s']//following::input[1]";
    private String toTime = "//input[@id='checkbox-criteriaValues-%s']//following::input[2]";
    private final int numOfWorkDaysFields = Integer.parseInt(PropertiesFile.readKey("numOfWorkDaysFields"));
    private final By numOfWorkDaysFieldsLoc = By.xpath("//input[@class='date-picker-wrapper']");
    private final By[] locatorArr = new By[]{locName,contactPrsn,phoneNum,address,country,city,province,postCode,timeZone};
    private static final String VAL_ATTR = "value";



    public void setLocationGeneralData(String locName, String contactPrsn, String phoneNum, String address, String country, String city, String province, String postCode, String timeZone)
    {
        Log.info("Accessing the General tab of Add New Location modal");
        sendText(this.locName, locName);
        sendText(this.contactPrsn, contactPrsn);
        sendText(this.phoneNum, phoneNum);
        sendText(this.address, address);
        selectOptionFromDropdown(this.country,country);
        sendText(this.city, city);
        sendText(this.province, province);
        sendText(this.postCode, postCode);
        scrollElementToView(this.timeZone, ScrollDirection.TOP);
        selectOptionFromDropdown(this.timeZone,timeZone);
    }

    public void setLocationWorkDaysData(List<String> workDaysFromFile, String fromTime, String toTime)
    {
        clickOn(workDaysTab);
        Log.info("Accessing the Working Days tab of Add New Location modal");

        Log.info("Checking if all the checkboxes are disabled by default");
        List<WebElement> weekdaysListBeforeEdit = findWebElements(weekDays);
        Assert.assertFalse(areAllCheckboxesDisabled(weekdaysListBeforeEdit), "Expected all checkboxes to be disabled before click on edit button");
        Log.info("All the checkboxes are in disabled state before click on edit");

        waitUntilElementToBeClickable(editBtn);
        clickOn(editBtn);
        Log.info("Click on Edit button");

        List<WebElement> weekdaysListAfterEdit = findWebElements(weekDays);
        Log.info("Checking if all the checkboxes are enabled after clicking on edit button");
        Assert.assertFalse(areAllCheckboxesEnabled(weekdaysListAfterEdit), "Expected all checkboxes to be enabled after click on edit button");

        Log.info("All the checkboxes are in enabled state after click on edit button");

        Log.info("Checking the total number of time fields after clicking on edit button");
        Assert.assertEquals(findWebElements(numOfWorkDaysFieldsLoc).size(),numOfWorkDaysFields,"The number of time fields shown to the user after click on edit button are not as expected: " + numOfWorkDaysFields);
        Log.info("The number of time fields shown after clicking on edit button are as expected");


        for(int i=0;i<weekdaysListAfterEdit.size();i++)
        {
            if(workDaysFromFile.contains(weekdaysListAfterEdit.get(i).getText()))
            {
                clickOn(By.xpath(String.format(workDayChkBox,i+1)));
                sendText(By.xpath(String.format(this.fromTime,i+1)), fromTime);
                sendText(By.xpath(String.format(this.toTime,i+1)), toTime);
            }
        }
    }

    public boolean areAllCheckboxesDisabled(List<WebElement> weekdayList)
    {
        for(int i=0;i<weekdayList.size();i++)
        {
            if(findWebElement(By.xpath(String.format(workDayChkBox,i+1))).isEnabled()){
                Log.info(String.format(workDayChkBox,i+1));
                return true;
            }

        }

        return false;

    }

    public boolean areAllCheckboxesEnabled(List<WebElement> weekdayList)
    {
        for(int i=0;i<weekdayList.size();i++)
        {
            if(!findWebElement(By.xpath(String.format(workDayChkBox,i+1))).isEnabled()){
                Log.info(String.format(workDayChkBox,i+1));
                return true;
            }

        }

        return false;
    }


    public boolean verifyEditLocationGeneralTabData(Map<String, String> dataMap)
    {
        String[] fieldNames = {"locName","contactPrsn","phoneNum","address", "country","city","province","postCode","timeZone"};
        for (int i=0; i<locatorArr.length; i++)
        {
            if(i==4 || i==8) {
                if(i==8)
                    scrollElementToView(locatorArr[i],ScrollDirection.TOP);

                if (!dataMap.get(fieldNames[i]).equals(getSelectedOptionTextFromDropdown(locatorArr[i]))) {
                    Log.error("Edit test is failed for field: " + fieldNames[i] + " because of below reason:");
                    Log.error("Value read from file is: " + dataMap.get(fieldNames[i]));
                    Log.error("Value read from modal is: " + getSelectedOptionTextFromDropdown(locatorArr[i]));
                    return true;
                }
            }

            else {
                if (!dataMap.get(fieldNames[i]).equals(getAttributeValue(VAL_ATTR, locatorArr[i]))) {
                    Log.error("Edit test is failed for field " + fieldNames[i] + "because of below reason:");
                    Log.error("Value read from file is: " + dataMap.get(fieldNames[i]));
                    Log.error("Value read from modal is: " + getAttributeValue(VAL_ATTR, locatorArr[i]));
                    return true;
                }
            }
        }

        return false;
    }

    public boolean verifyEditLocationWorkDaysTabData(List<String> workDaysFromFile, Map<String, String> dataMap)
    {
        clickOn(workDaysTab);
        Log.info("Click on Work Days Tab");
        waitUntilElementToBeClickable(editBtn);
        clickOn(editBtn);
        Log.info("Click on Edit button");

        List<WebElement> weekdaysList = findWebElements(weekDays);


        for(int i=0;i<weekdaysList.size();i++)
        {
            if(workDaysFromFile.contains(weekdaysList.get(i).getText()))
            {
                if(!isCheckboxSelected(By.xpath(String.format(workDayChkBox,i+1)))) {
                    Log.error("The checkbox for workday " + weekdaysList.get(i).getText() + " is not selected in the edit modal but was provided in file");
                    return true;
                }
                else if(!getAttributeValue(VAL_ATTR, By.xpath(String.format(this.fromTime,i+1))).equals(dataMap.get("fromTime"))){
                    Log.error("Edit test is failed for field FromTime for workday " + weekdaysList.get(i).getText() + " because of below reason:");
                    Log.error("Value read from file is: " + dataMap.get("fromTime"));
                    Log.error("Value read from modal is: " + getAttributeValue(VAL_ATTR, By.xpath(String.format(this.fromTime,i+1))));
                    return true;
                }
                else if(!getAttributeValue(VAL_ATTR, By.xpath(String.format(this.toTime,i+1))).equals(dataMap.get("toTime"))){
                    Log.error("Edit test is failed for field ToTime for workday " + weekdaysList.get(i).getText() + " because of below reason:");
                    Log.error("Value read from file is: " + dataMap.get("toTime"));
                    Log.error("Value read from modal is: " + getAttributeValue(VAL_ATTR, By.xpath(String.format(this.toTime,i+1))));
                    return true;
                }
            }
        }

        return false;
    }

}
