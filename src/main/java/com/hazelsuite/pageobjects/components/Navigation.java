package com.hazelsuite.pageobjects.components;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.*;
import org.testng.Assert;

import static com.hazelsuite.utilities.CommonUtil.*;
import static org.assertj.core.api.Assertions.assertThat;


public class Navigation
{
    private final By companyTab = By.xpath("//a[@href='/company']");
    private final By locationTab = By.xpath("//a[@href='/locations']");
    private final By companyInfoTab = By.xpath("//a[@href='/companyInformation']");
    private final By loginLoader = By.xpath("//div[@class='loader-box']");
    private final By tabLoader = By.xpath("//div[@class='pl-3']");
    private final By buttonLoader = By.xpath("(//div[@class='loader-box'])[2]");
    private final By toastContainer = By.xpath("//div[@class='Toastify__toast-container Toastify__toast-container--top-right']");
    private String toastMsg = "(//div[@class='Toastify__toast-body'])[";
    private final By toastCloseBtn = By.xpath("//button[@class='Toastify__close-button Toastify__close-button--success']");
    private final By summaryBtn = By.linkText("Summary");
    private final By emailConfirmationTitle = By.className("email-confirmation__title");
    private final By clockInBtn = By.xpath("//span[text()='Clock-In  ']");
    private final By osuName = By.id("userName");
    private final By profileTab = By.xpath("//a[@href='/myInfo']");

    // Keep in Navigation class
    private final By companySetupTab = By.id("toggler-1");
    private final By medicalSettingsTab = By.xpath("//a[@href='/medicalSettings']");
    private final By leaveTypeTab = By.xpath("//a[@href='/leaveType']");
    private final By designationTab = By.xpath("//a[@href='/designation']");
    private final By employmentTypeTab = By.xpath("//a[@href='/employmentType']");
    private final By departmentsTab = By.xpath("//a[@href='/departments']");
    private final By teamsTab = By.xpath("//a[@href='/teams']");
    private final By employeesTab = By.xpath("//a[@href='/employees']");


    public void clickOnCompaniesTab()
    {
        clickOn(companyTab);
        Log.info("Click on Companies Tab");
    }

    public void clickOnLocationsTab()
    {
        clickOn(locationTab);
        Log.info("Click on Locations Tab");
    }

    public void clickOnCompanyInfoTab()
    {
        clickOn(companyInfoTab);
        Log.info("Click on Company Information Tab");
    }

    public void waitForLoginLoaderVisible()
    {
        waitUntilElementVisible(loginLoader);
    }

    public void waitForTabLoaderVisible()
    {
        //waitUntilElementVisible(tabLoader);
        try{
            waitTillEleCountChanges(tabLoader,0);
        }
        catch(TimeoutException e){
            Log.warn("Failed to wait for Tab Loader visibility");
        }
    }


    public void waitForButtonLoaderVisible()
    {
        waitUntilElementVisible(buttonLoader);
    }

    public void waitForLoginLoaderComplete()
    {
        waitUntilElementInvisible(loginLoader);
    }

    public void waitForTabLoaderComplete()
    {
        waitUntilElementInvisible(tabLoader);
    }

    public void waitForButtonLoaderComplete()
    {
        waitUntilElementInvisible(buttonLoader);
    }

    public String getToastText(int index)
    {
        return getText(By.xpath(toastMsg + index + "]"));
    }

    public void closeToast()
    {
        clickOn(toastCloseBtn);
    }

    public boolean isSummaryBtnDisplayed()
    {
        return isElementDisplayed(summaryBtn);
    }

    public int getTotalToastsNum()
    {
        By childLoc = By.xpath("//div[contains(@class,'Toastify__toast--')]");
        //waitUntilElementVisible(toastContainer);
        waitTillEleCountChanges(toastContainer, 0);
        return getNumOfChildElements(toastContainer, childLoc);
    }

    public void waitUntilEmailTitleVisible()
    {
        // TODO: 1/3/2024 Add refresh login if upon clicking on email verification link leads to error page
        try{
            waitUntilElementVisible(emailConfirmationTitle);
        }
        catch (TimeoutException e){
            refreshBrowser();
        }
    }

    public void waitUntilOSUNameVisible()
    {
        waitUntilElementVisible(osuName);
    }

    public boolean isClockInBtnDisplayed()
    {
        return isElementDisplayed(clockInBtn);
    }

    public boolean isProfileBtnDisplayed()
    {
        return isElementDisplayed(profileTab);
    }


    public void checkTotalToastCountAndExpectedText(int count, String expectedText)
    {
        if(count == 1) {
            assertThat(getToastText(count))
                    .isEqualTo(expectedText);
            waitUntilElementToBeClickable(toastCloseBtn);
            closeToast();
            //waitUntilElementInvisible(toastContainer);
        }

        else if (count == 2) {
            for(int i=1;i<=count;i++)
            {
                if(getToastText(i).contains("media"))
                {
                    assertThat(getToastText(i))
                            .isEqualTo("Company media updated successfully");
                    closeToast();
                }
                else if(getToastText(i).contains("updated")) {
                    assertThat(getToastText(i))
                            .isEqualTo("Company has been updated.");
                    closeToast();
                }
                else {
                    Assert.fail("Wrong toast message text: " + getToastText(i) );
                }
            }
        }

        else if(count > 2){
            Assert.fail("More than 2 toast messages should not show at one time");
        }

        else{
            Assert.fail("No toast message indicating success was displayed");
        }
    }

    public void clickOnCompanySetupTab()
    {
        clickOn(companySetupTab);
        Log.info("Click on Company Setup tab");
    }

    public void clickOnMedicalSettingsTab()
    {
        clickOn(medicalSettingsTab);
        Log.info("Click on Medical Settings tab");
    }

    public void clickOnLeaveTypeTab()
    {
        clickOn(leaveTypeTab);
        Log.info("Click on Leave Type tab");
    }

    public void clickOnDesignationTab()
    {
        clickOn(designationTab);
        Log.info("Click on Designation tab");
    }

    public void clickOnEmploymentTypeTab()
    {
        clickOn(employmentTypeTab);
        Log.info("Click on Employment Type tab");
    }

    public void clickOnDepartmentsTab()
    {
        clickOn(departmentsTab);
        Log.info("Click on Departments tab");
    }

    public void clickOnTeamsTab()
    {
        clickOn(teamsTab);
        Log.info("Click on Teams tab");
    }

    public void clickOnEmployeesTab()
    {
        clickOn(employeesTab);
        Log.info("Click on Employees tab");
    }
}
