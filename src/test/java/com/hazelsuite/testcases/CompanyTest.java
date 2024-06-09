package com.hazelsuite.testcases;

import com.hazelsuite.pageobjects.CompanyPage;
import com.hazelsuite.pageobjects.components.*;
import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.*;
import static com.hazelsuite.utilities.EmailUtil.*;
import static com.hazelsuite.utilities.ExcelFileUtil.readExcelColumns;
import static com.hazelsuite.utilities.SQLiteUtil.deleteDataFromTables;
import static com.hazelsuite.utilities.SQLiteUtil.insertDataIntoDB;
import static org.assertj.core.api.Assertions.assertThat;

public class CompanyTest
{
    CompanyPage cmp = new CompanyPage();

    // Objects from component class
    ActionBar act = new ActionBar();
    Navigation nav = new Navigation();
    Modal modal = new Modal();
    Table table = new Table();
    PasswordSetup password = new PasswordSetup();

    Store store = null;
    Message[] messages = null;

    String absolutePathToExcel = returnAbsolutePath(PropertiesFile.readKey("relativePathToExcel"));
    int addCompColIndex = Integer.parseInt(PropertiesFile.readKey("addCompColIndex"));
    Map<String, String> data = readExcelColumns(absolutePathToExcel, "addNewCompany",addCompColIndex);
    int beforeAddPageCount;
    int afterAddPageCount;



    @BeforeClass
    public void updateCompanyDataIntoDB()
    {
        String[] tableNames = {"companyGeneral_before"};
        deleteDataFromTables(tableNames);

        List<String> locatorValues = new ArrayList<>();
        Object[] keys = data.keySet().toArray();
        for (int i = 0; i < keys.length-2; i++) {
            if(i==6)
                locatorValues.add(data.get((String) keys[i]) + ".suitestg.hazelsoft.net");
            else
                locatorValues.add(data.get((String) keys[i]));
        }

        insertDataIntoDB(locatorValues, "companyGeneral_before");
        Log.info("Company info general data is read from excel file for insertion into DB");
    }


    @Test(groups = {"Companies Tab"})
    public void isCompaniesTabAccessible()
    {
        nav.clickOnCompaniesTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/company") && act.isAddNewBtnDisplayed())
                .withFailMessage("Companies tab is not accessible")
                .isTrue();
        Log.info("Company Tab has been accessed successfully");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("companyName");
    }


    @Test(groups = {"Companies Tab"})
    public void addCompanyInfoData()
    {
        table.waitForPaginationTextUpdate();
        beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the company is: " + beforeAddPageCount);

        act.clickOnAddNewBtn();
        // TODO: 1/5/2024 Add the generic parameters for below method
        cmp.setCompanyInfoData(data.get("companyName"),data.get("companyEmail"),data.get("phoneNum"),data.get("taxNum"), data.get("faxNum"),data.get("companyWebsite"),data.get("customUrl"));
        Log.info("Company Info data has been read from excel file and successfully populated in the company info section Fields");
        modal.clickOnNextBtn();
        verifyToastMsg("Company created successfully");
        Log.info("Success toast message is shown for Add Company Info upon clicking on the next button of modal");

    }

    @Test(groups = {"Companies Tab"})
    public void addCompanyAddressData()
    {
        // TODO: 1/5/2024 Add the generic parameters for below method
        cmp.setCompanyAddressData(data.get("compAddress"),data.get("compCountry"),data.get("compCity"),data.get("compProvince"),data.get("compPostalCode"));
        Log.info("Company Address data has been read from excel File and successfully populated in the Company Address section fields");
        modal.clickOnNextBtn();
        verifyToastMsg("Company address info updated successfully");
        Log.info("Success toast message is shown for Add Company Address upon clicking on the next button of modal");
    }

    @Test(groups = {"Companies Tab"})
    public void addContactPersonData()
    {
        cmp.setContactPersonData(data.get("contactPrsnName"),data.get("contactPrsnEmail"),data.get("contactPrsnNumber"));
        Log.info("Contact Person data has been read from excel file and successfully populated in the Contact Person section Fields");
        modal.clickOnNextBtn();
        verifyToastMsg("Contact Info updated successfully");
        Log.info("Success toast message is shown for Add Contact Person upon clicking on the next button of modal");
    }

    @Test(groups = {"Companies Tab"})
    public void addSubscriptionData()
    {
        // Load the required subscription modules from the file
        List<String> modulesFromFile = getListAfterSplitString(data.get("modulesFromFile"),",");
        Log.info("The required Subscription Modules are:");
        for(String module : modulesFromFile) {
            Log.info(module);
        }

        cmp.setSubscriptionModules(modulesFromFile);
        Log.info("Subscription data has been read from excel file and successfully selected the provided subscriptions");
        modal.clickOnNextBtn();
        verifyToastMsg("Modules updated successfully");
        Log.info("Success toast message is shown for selected subscriptions upon clicking on the next button of modal");
    }

    @Test(groups = {"Companies Tab"})
    public void addCompanyLogoAndCreateCompany()
    {
        cmp.setCompanyLogo(returnAbsolutePath(data.get("pathToLogo")));
        Log.info("Company Logo has been read from excel file and successfully uploaded the provided logo");
        modal.clickOnNextBtn();
        verifyToastMsg("");
        Log.info("Success toast message is shown for uploaded logo upon clicking on the next button of modal");

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("companyName")))
                .withFailMessage("After add company, the record in the table is not updated")
                .isTrue();

        afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the company is: " + afterAddPageCount);
        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add company, pagination count is not updated")
                .isFalse();
        Log.info("Company is added successfully in the table");
    }

    @Test(groups = {"Companies Tab"})
    public void verifyChangeViewCompanies() {
        checkAllCheckboxesAndVerifyTableCols();
    }

    private void checkAllCheckboxesAndVerifyTableCols() {
        act.clickOnChangeViewBtn();
        int checkboxes = act.getChangeViewChkBoxes();
        act.checkAllCheckboxes();
        int tableCols = table.getNumOfTableCols();
        assertThat(checkboxes).isEqualTo(tableCols);
        Log.info("The number of checkboxes checked in change view modal and number of table columns match");
    }

    @Test(groups = {"Email Verification"})
    public void verifyIsEmailUnreadAndSubject() throws MessagingException {
        verifyEmailAuthentication();
        messages = checkUnreadEmailSubject(store,"taimoorafzal9402@gmail.com");
        Log.info("The Email Subject is: " + messages[0].getSubject());
        Assert.assertEquals(messages[0].getSubject(), "Confirm your email address","Email subject does not match expected result.");
        Log.info("Email Subject has been verified successfully and is about Confirmation Email");
    }

    @Test(groups = {"Email Verification"})
    public void verifyEmailBodyTextPresence() throws MessagingException, IOException {
        String emailContent = extractEmailContentFromBody(messages);
        Log.info("The Fetched Email Body is: " + emailContent);
        Assert.assertEquals(emailContent, "Confirm your email address","Email message body does not contain email confirmation text.");
        Log.info("Email Body has been verified successfully and contains Email Confirmation Text");

        archiveEmail(messages, store);
    }

    @Test(groups = {"Email Verification"})
    public void verifyClickOnEmailVerificationBtn() throws MessagingException, IOException {
        String verificationLink = extractVerificationLinkFromBody(messages);
        Log.info("The Email Verification Link for OSU is: " + verificationLink);
        openUrlInNewTab(verificationLink);
        Log.info("Opening the Email Verification Link in the new tab");
        nav.waitUntilEmailTitleVisible();
        Log.info("Email Verification Link is loaded successfully");
        store.close();
    }

    // TODO: 1/3/2024 Create separate test for ASU Creation

    @Test(groups = {"OSU Password Setup"})
    public void osuEmailVerificationProcess()
    {
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "setOSUPass",addCompColIndex);
        String emailTitle = password.getEmailConfirmationText();
        Assert.assertEquals(emailTitle,"Your e-mail address has been successfully verified.","Email confirmation text shown is not matching with the expected result.");
        Log.info("The OSU Email is verified successfully");

        String osuName = password.getUserNameDuringVerification();
        Assert.assertEquals(osuName,data.get("Name"));
        Log.info("The OSU Name auto-fetched in the Set Password Page is correct");

        password.setUserPassword(data.get("password"),data.get("confirmPass"));
        Log.info("Setting the OSU password and confirm password");

        assertThat(nav.isClockInBtnDisplayed())
                .withFailMessage("The OSU home page is not accessible")
                .isTrue();
        Log.info("The OSU Home Page is accessed after setting the password");


    }

    private void verifyToastMsg(String expectedText)
    {
        nav.waitForButtonLoaderVisible();
        nav.waitForButtonLoaderComplete();
        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), expectedText);
    }

    private void verifyEmailAuthentication() {
        Log.info("Starting the Email Verification sent to the inbox of provided email: " + data.get("contactPrsnEmail") + " of OSU");
        store = emailAuthentication("imap.gmail.com", data.get("contactPrsnEmail"), "zvod seut akse zedw");

        assertThat(store.isConnected())
                .withFailMessage("Email authentication has been failed")
                .isTrue();
        Log.info("Email has been successfully authenticated");
    }

}
