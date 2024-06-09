package com.hazelsuite.testcases;

import com.hazelsuite.pageobjects.LoginPage;
import com.hazelsuite.pageobjects.components.Navigation;
import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.getPageUrl;
import static com.hazelsuite.utilities.CommonUtil.returnAbsolutePath;
import static com.hazelsuite.utilities.ExcelFileUtil.readExcelColumns;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest
{
    LoginPage login = new LoginPage();
    Navigation nav = new Navigation();

    // Construct the absolute path based on the current working directory
    String absolutePathToExcel = returnAbsolutePath(PropertiesFile.readKey("relativePathToExcel"));
    int addCompColIndex = Integer.parseInt(PropertiesFile.readKey("addCompColIndex"));

    @Test(groups = {"Login"})
    @Parameters({"loginColIndex"})
    public void loginValidCredentials(int loginColIndex)
    {
        Map<String, String> loginData = readExcelColumns(absolutePathToExcel,"login",loginColIndex);
        login.loginUser(loginData.get("email"), loginData.get("password"));
        nav.waitForLoginLoaderVisible();
        nav.waitForLoginLoaderComplete();

        Map<String, String> companyData = readExcelColumns(absolutePathToExcel,"addNewCompany",addCompColIndex);

        if(loginData.get("role").equalsIgnoreCase("ASU")){

            verifyLogin(getPageUrl(), "http://suitestg.hazelsoft.net/dashboard",nav.isSummaryBtnDisplayed()
                    ,"ASU Core","Dashboard");
        }

        else if(loginData.get("role").equalsIgnoreCase("OSU")){

            verifyLogin(getPageUrl(), "http://" + companyData.get("customUrl") + ".suitestg.hazelsoft.net/dashboard"
                    ,nav.isSummaryBtnDisplayed(), "OSU Core","Dashboard");
        }

        else if(loginData.get("role").equalsIgnoreCase("HR")) {

            verifyLogin(getPageUrl(), "http://" + companyData.get("customUrl") + "-hr.suitestg.hazelsoft.net/myInfo"
                    ,nav.isProfileBtnDisplayed(), "HR","My Profile");
        }
    }
    public void verifyLogin(String url, String expected, boolean isBtnDisplayed, String role, String page)
    {
        assertThat(url)
                .isEqualTo(expected);
        Log.info("URL is correct after login to " + role + " module");

        assertThat(isBtnDisplayed)
                .withFailMessage(page + " Page is not accessible")
                .isTrue();
        Log.info("Login is successful and " + page + " Page is accessible");
    }
}
