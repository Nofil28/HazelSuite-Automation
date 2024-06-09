package com.hazelsuite.pageobjects;

import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.hazelsuite.utilities.CommonUtil.*;
import static com.hazelsuite.utilities.SQLiteUtil.compareTables;
import static com.hazelsuite.utilities.SQLiteUtil.insertDataIntoDB;

public class CompanyInfoPage
{
    private final By editBtn = By.xpath("//button[contains(@class,'btn-primary')]");

    // Company Info Fields
    private final By companyName = By.id("companyName");
    private final By companyEmail = By.id("companyContactEmail");
    private final By phoneNum = By.id("companyContactNumber");
    private final By taxNum = By.id("taxNumber");
    private final By faxNum = By.id("faxNumber");
    private final By companyWebsite = By.id("companyWebsite");

    // Company Address Fields
    private final By compAddress = By.id("companyAddress");
    private final By compCountry = By.id("country");
    private final By compCity = By.id("companyCity");
    private final By compProvince = By.id("companyProvince");
    private final By compPostalCode = By.id("companyPostalCode");

    // Contact Person Fields
    private final By contactPrsnName = By.id("contactPersonName");
    private final By contactPrsnEmail = By.id("contactPersonEmail");
    private final By contactPrsnNumber = By.id("contactPersonNumber");
    private final By adminName = By.id("adminName");
    private final By adminEmail = By.id("adminEmail");

    // Company Logo & URL
    private final By customUrl = By.id("url");
    private final By compInfoUpdateBtn = By.xpath("//button[@form='companyinformationForm']");
    private final By settingsUpdateBtn = By.xpath("//button[@form='settingsForm']");
    private final By settingsTab = By.linkText("Settings");
    private final By[] generalLocatorArr = new By[]{companyName,companyEmail,phoneNum,taxNum,faxNum,companyWebsite,customUrl,compAddress,compCountry,compCity,compProvince,compPostalCode,contactPrsnName,contactPrsnEmail,contactPrsnNumber,adminName,adminEmail};

    private final List<String> generalLocatorValues = new ArrayList<>();

    // Fields in Settings tab
    private final By employeeId = By.id("employeeIdPrefix");
    private final By autoPopId = By.id("autoPopulateEmployeeId");
    private final By timeZone = By.id("timezoneId");
    private final By currency = By.id("currency");
    private final By dateFormat = By.id("dateFormatId");
    private final By startDateMonth = By.id("month");
    private final By startDateDay = By.id("day");
    private final By[] settingsLocatorArr = new By[]{employeeId,autoPopId,timeZone,currency,dateFormat,startDateMonth,startDateDay};

    private final List<String> settingsLocatorValues = new ArrayList<>();

    private final By numOfCompGeneralFieldsLoc = By.xpath("//form[@id='companyinformationForm']//div[@class='form-group']//input");
    private final int numOfCompGeneralFields = Integer.parseInt(PropertiesFile.readKey("numOfCompGeneralFields"));
    private final By numOfCompGeneralDropdownLoc = By.xpath("//form[@id='companyinformationForm']//div[@class='form-group']//select");
    private final int numOfCompGeneralDropdown = Integer.parseInt(PropertiesFile.readKey("numOfCompGeneralDropdown"));
    private final By numOfCompSettingsFieldsLoc = By.xpath("//form[@id='settingsForm']//div[@class='form-group']//input[@type='text']");
    private final int numOfCompSettingsFields = Integer.parseInt(PropertiesFile.readKey("numOfCompSettingsFields"));
    private final By numOfCompSettingsChkboxLoc = By.xpath("//form[@id='settingsForm']//div[@class='form-group']//input[@type='checkbox']");
    private final int numOfCompSettingsChkbox = Integer.parseInt(PropertiesFile.readKey("numOfCompSettingsChkbox"));
    private final By numOfCompSettingsDropdownLoc = By.xpath(" //form[@id='settingsForm']//div[@class='form-group']//select");
    private final int numOfCompSettingsDropdown = Integer.parseInt(PropertiesFile.readKey("numOfCompSettingsDropdown"));



    public boolean isEditButtonVisible()
    {
        return isElementDisplayed(editBtn);
    }

    public void clickOnEditBtn(String pageName)
    {
        scrollElementToView(editBtn, ScrollDirection.BOTTOM);
        clickOn(editBtn);
        Log.info("Click on Edit button of " + pageName);

        if(pageName.equalsIgnoreCase("general")){
            Log.info("Checking the total number of fields after clicking on edit button");
            Assert.assertEquals(findWebElements(numOfCompGeneralFieldsLoc).size(),numOfCompGeneralFields,"The number of fields shown to the user after click on edit button are not as expected: " + numOfCompGeneralFields);
            Log.info("The number of fields shown after clicking on edit button are as expected");

            Log.info("Checking the total number of dropdowns after clicking on edit button");
            Assert.assertEquals(findWebElements(numOfCompGeneralDropdownLoc).size(),numOfCompGeneralDropdown,"The number of dropdowns shown to the user after click on edit button are not as expected: " + numOfCompGeneralDropdown);
            Log.info("The number of dropdowns shown after clicking on edit button are as expected");
        }

        else if(pageName.equalsIgnoreCase("settings")){
            Log.info("Checking the total number of fields after clicking on edit button");
            Assert.assertEquals(findWebElements(numOfCompSettingsFieldsLoc).size(),numOfCompSettingsFields,"The number of fields shown to the user after click on edit button are not as expected: " + numOfCompSettingsFields);
            Log.info("The number of fields shown after clicking on edit button are as expected");

            Log.info("Checking the total number of checkboxes after clicking on edit button");
            Assert.assertEquals(findWebElements(numOfCompSettingsChkboxLoc).size(),numOfCompSettingsChkbox,"The number of checkboxes shown to the user after click on edit button are not as expected: " + numOfCompSettingsChkbox);
            Log.info("The number of checkboxes shown after clicking on edit button are as expected");

            Log.info("Checking the total number of dropdowns after clicking on edit button");
            Assert.assertEquals(findWebElements(numOfCompSettingsDropdownLoc).size(),numOfCompSettingsDropdown,"The number of dropdowns shown to the user after click on edit button are not as expected: " + numOfCompSettingsDropdown);
            Log.info("The number of dropdowns shown after clicking on edit button are as expected");
        }
    }

    public void clickOnCompInfoUpdateBtn()
    {
       clickOn(compInfoUpdateBtn);
       Log.info("Click on Update button of Company Info form");
    }

    public void clickOnSettingsUpdateBtn()
    {
        clickOn(settingsUpdateBtn);
        Log.info("Click on Update button of Settings form");
    }

    public void clickOnSettingsTab()
    {
        scrollElementToView(settingsTab, ScrollDirection.BOTTOM);
        clickOn(settingsTab);
        Log.info("Click on Settings tab");
    }

    public List<String> getGeneralLocatorsData()
    {
        for(int i=0;i<generalLocatorArr.length;i++)
        {
            if(i==8) {
                scrollElementToView(generalLocatorArr[i], ScrollDirection.TOP);
                generalLocatorValues.add(getSelectedOptionTextFromDropdown(generalLocatorArr[i]));
            }
            else
                generalLocatorValues.add(getAttributeValue("value",generalLocatorArr[i]));
        }

        return generalLocatorValues;
    }



    public void setCompanyInformationSettingsData(String employeeId, String autoPopId, String timeZone, String currency, String dateFormat, String startDateMonth, String startDateDay)
    {
        Log.info("Accessing the Settings tab of Company Information page");
        sendText(this.employeeId, employeeId);
        if(autoPopId.equalsIgnoreCase("Yes"))
            clickOnChkboxUsingJS(this.autoPopId);
        selectOptionFromDropdown(this.timeZone, timeZone);
        if(!getSelectedOptionTextFromDropdown(this.currency).equals(currency))
            selectOptionFromDropdown(this.currency, currency);
        selectOptionFromDropdown(this.dateFormat, dateFormat);
        selectOptionFromDropdown(this.startDateMonth, startDateMonth);
        selectOptionFromDropdown(this.startDateDay,startDateDay);
    }

    public List<String> getSettingsLocatorsData()
    {
        for(int i=0;i<settingsLocatorArr.length;i++)
        {
            if(i == 0)
                settingsLocatorValues.add(getAttributeValue("value",settingsLocatorArr[i]));
            else if(i == 1) {
                if(!isCheckboxSelected(settingsLocatorArr[i]))
                    settingsLocatorValues.add("no");
                else
                    settingsLocatorValues.add("yes");
            }
            else {
                if(i == 4)
                    scrollElementToView(settingsLocatorArr[i], ScrollDirection.TOP);
                settingsLocatorValues.add(getSelectedOptionTextFromDropdown(settingsLocatorArr[i]));
            }
        }

        return settingsLocatorValues;
    }


}
