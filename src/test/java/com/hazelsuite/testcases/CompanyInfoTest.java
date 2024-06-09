package com.hazelsuite.testcases;

import com.hazelsuite.pageobjects.CompanyInfoPage;
import com.hazelsuite.pageobjects.components.Navigation;
import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.doesUrlContain;
import static com.hazelsuite.utilities.CommonUtil.returnAbsolutePath;
import static com.hazelsuite.utilities.ExcelFileUtil.readExcelColumns;
import static com.hazelsuite.utilities.SQLiteUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CompanyInfoTest
{
    CompanyInfoPage compInfo = new CompanyInfoPage();
    Navigation nav = new Navigation();

    String absolutePathToExcel = returnAbsolutePath(PropertiesFile.readKey("relativePathToExcel"));


    @BeforeClass
    public void deleteCompanyInfoTables()
    {
        String[] tableNames = {"companyGeneral_after","companySettings_before","companySettings_after"};
        deleteDataFromTables(tableNames);
    }

    @Test(groups = {"Company Info General Tab"})
    public void isCompanyInfoGeneralTabAccessible()
    {
        nav.clickOnCompanyInfoTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/companyInformation") && compInfo.isEditButtonVisible())
                .withFailMessage("Company Info General Tab is not accessible")
                .isTrue();
        Log.info("Company Info General Tab is accessible");
    }


    @Test(groups = {"Company Info General Tab"})
    public void verifyCompanyInformationGeneralTabData() {
        //TODO: base64 image comparison of company logo is pending
        compInfo.clickOnEditBtn("general");
        List<String> locatorValues = compInfo.getGeneralLocatorsData();
        Log.info("Company info general data is read from form locators for insertion into DB");
        insertDataIntoDB(locatorValues,"companyGeneral_after");
        List<String> columnNames = getColumnNames("companyGeneral_before");

        if(!checkIfTablesHaveSameData("companyGeneral_before", "companyGeneral_after"))
        {
            String editResult = compareTables("companyGeneral_before", "companyGeneral_after", columnNames);
            assertThat(editResult.isEmpty())
                    .withFailMessage("The data entered during company creation and data shown under company information general tab don't match for field " + editResult)
                    .isTrue();
        }
        else
            Log.info("The data entered during company creation and data shown under company information general tab match");
    }

    @Test(groups = {"Company Info Settings Tab"})
    public void addDataInSettingsTab()
    {
        compInfo.clickOnSettingsTab();

        assertThat(doesUrlContain("/companyInformation") && compInfo.isEditButtonVisible())
                .withFailMessage("Company Info Settings Tab is not accessible")
                .isTrue();
        Log.info("Company Info Settings Tab is accessible");


        List<String> locatorValues = new ArrayList<>();
        Map<String, String> dataMap = readExcelColumns(absolutePathToExcel, "companyInfoSettings",1);
        Object[] keys = dataMap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            locatorValues.add(dataMap.get((String) keys[i]));
        }
        Log.info("Company info settings data is read from excel file for insertion into DB");
        insertDataIntoDB(locatorValues, "companySettings_before");;
        compInfo.clickOnEditBtn("settings");
        compInfo.setCompanyInformationSettingsData(dataMap.get("employeeId"), dataMap.get("autoPopId"), dataMap.get("timeZone"), dataMap.get("currency"), dataMap.get("dateFormat"), dataMap.get("startDateMonth"), dataMap.get("startDateDay"));
        Log.info("Data has been successfully populated in the Company Information Settings form");
        compInfo.clickOnSettingsUpdateBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Company Settings have been updated.");
        Log.info("Success toast message is shown for Settings Tab of Company Information upon clicking on the update button");
    }

    @Test(groups = {"Company Info Settings Tab"})
    public void verifyCompanyInformationSettingsTabData() {
        compInfo.clickOnEditBtn("settings");
        List<String> locatorValues = compInfo.getSettingsLocatorsData();
        Log.info("Company info settings data is read from form locators for insertion into DB");
        insertDataIntoDB(locatorValues,"companySettings_after");
        List<String> columnNames = getColumnNames("companySettings_before");

        if(!checkIfTablesHaveSameData("companySettings_before", "companySettings_after"))
        {
            String editResult = compareTables("companySettings_before", "companySettings_after", columnNames);
            assertThat(editResult.isEmpty())
                    .withFailMessage("The data shown under company information settings tab after updating don't match for field " + editResult)
                    .isTrue();
        }
        else
            Log.info("The data shown under company information settings tab after updating matches for all fields");
    }

    // TODO: 1/3/2024 Add HR as OSU test and try to run HR module tests with HR role user

}
