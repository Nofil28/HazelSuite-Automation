package com.hazelsuite.pageobjects;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;

import static com.hazelsuite.utilities.CommonUtil.*;
import static com.hazelsuite.utilities.SQLiteUtil.insertDataIntoDB;

import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.util.List;

public class CompanyPage {
    // Company Info Fields
    private final By companyName = By.id("companyName");
    private final By companyEmail = By.id("companyContactEmail");
    private final By phoneNum = By.id("companyContactNumber");
    private final By taxNum = By.id("taxNumber");
    private final By faxNum = By.id("faxNumber");
    private final By companyWebsite = By.id("companyWebsite");
    private final By customUrl = By.id("url");

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


    // Subscription Modules
    private final By modulesNum = By.xpath("//div[@class='row border-bottom']//div[@class='col-6 p-1 pl-3']");
    private String dynamicModuleToggle = "(//span[@class='css-1wits42'])[";

    // Company Logo
    private final By chooseFileBtn = By.id("logo");


    public void setCompanyInfoData(String compName, String compEmail, String compPhone, String compTaxNo, String compFaxNo, String compWebsite, String custUrl)
    {
        Log.info("Accessing the Company Info Section of Add New Company Modal");
        sendText(companyName, compName); // should be unique
        sendText(companyEmail, compEmail); // should be unique
        sendText(phoneNum, compPhone);
        sendText(taxNum, compTaxNo);
        sendText(faxNum, compFaxNo);
        sendText(companyWebsite, compWebsite);
        sendText(customUrl, custUrl); // should be unique
    }

    public void setCompanyAddressData(String address, String country, String city, String province, String postalCode)
    {
        Log.info("Accessing the Company Address section of Add New Company modal");
        sendText(compAddress, address);
        selectOptionFromDropdown(compCountry, country);
        sendText(compCity, city);
        sendText(compProvince, province);
        sendText(compPostalCode, postalCode);
    }

    public void setContactPersonData(String prsnName, String persnEmail, String prsnNumber)
    {
        Log.info("Accessing the Contact Person section of Add New Company modal");
        sendText(contactPrsnName, prsnName);
        sendText(contactPrsnEmail, persnEmail);
        sendText(contactPrsnNumber, prsnNumber);
    }

    public void setSubscriptionModules(List<String> modulesFromFile)
    {
        Log.info("Accessing the Subscription section of Add New Company modal");
        List<WebElement> modulesList = findWebElements(modulesNum);
        for(int i=1;i<=modulesList.size();i++)
        {
            if(!modulesFromFile.contains(modulesList.get(i-1).getText()))
            {
                clickOn(By.xpath(dynamicModuleToggle + i*2 + "]"));
            }
        }
    }

    public void setCompanyLogo(String pathToLogo)
    {
        Log.info("Accessing the Company Logo section of Add New Company modal");
        sendText(chooseFileBtn, pathToLogo);
    }

}
