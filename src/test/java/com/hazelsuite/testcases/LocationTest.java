package com.hazelsuite.testcases;
import com.hazelsuite.pageobjects.LocationPage;
import com.hazelsuite.pageobjects.components.ActionBar;
import com.hazelsuite.pageobjects.components.Modal;
import com.hazelsuite.pageobjects.components.Navigation;
import com.hazelsuite.pageobjects.components.Table;
import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.*;
import static com.hazelsuite.utilities.ExcelFileUtil.readExcelColumns;
import static org.assertj.core.api.Assertions.assertThat;

public class LocationTest {
    LocationPage loc = new LocationPage();

    // Objects from component class
    ActionBar act = new ActionBar();
    Navigation nav = new Navigation();
    Modal modal = new Modal();
    Table table = new Table();

    String absolutePathToExcel = returnAbsolutePath(PropertiesFile.readKey("relativePathToExcel"));
    int locColIndex = Integer.parseInt(PropertiesFile.readKey("locColIndex"));


    @Test(groups = {"Locations Tab"})
    public void isLocationsTabAccessible()
    {
        nav.clickOnLocationsTab();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();

        assertThat(doesUrlContain("/locations") && act.isAddNewBtnDisplayed())
                .withFailMessage("Locations Tab is not accessible")
                .isTrue();
        Log.info("Locations Tab is accessible");

        act.clickOnChangeViewBtn();
        act.uncheckAllCheckboxesExcept("name");
    }

    @Test(groups = {"Locations Tab"})
    public void addNewLocation()
    {
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "addNewLocation",locColIndex);
        int beforeAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count before adding the location is: " + beforeAddPageCount);
        act.clickOnAddNewBtn();

        loc.setLocationGeneralData(data.get("locName"),data.get("contactPrsn"),data.get("phoneNum"),data.get("address"), data.get("country"),data.get("city"),data.get("province"),data.get("postCode"),data.get("timeZone"));
        Log.info("Location General tab data has been read from excel file and successfully populated in the fields");
        modal.clickOnSubmitBtn();

        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Locations has been added.");
        Log.info("Success toast message is shown for General Tab of Add new Location modal upon clicking on the submit button");
        table.waitForTableRowUpdate(beforeAddPageCount);

        assertThat(table.matchValueAtFirstRowColumnOne(data.get("locName")))
                .withFailMessage("After add location, the record in the table is not updated")
                .isTrue();

        int afterAddPageCount = table.getPaginationNumber();
        Log.info("The total number of records count after adding the location is: " + afterAddPageCount);

        assertThat(beforeAddPageCount != afterAddPageCount - 1)
                .withFailMessage("After add location, pagination count is not updated")
                .isFalse();
        Log.info("Location is added successfully in the table");

        table.doubleClickOnTableFirstRow();
        // Load the provided working days from the file
        List<String> workDaysFromFile = getListAfterSplitString(data.get("workDays"),",");
        Log.info("The provided working days are:");
        for(String workDay : workDaysFromFile) {
            Log.info(workDay);
        }

        loc.setLocationWorkDaysData(workDaysFromFile, data.get("fromTime"), data.get("toTime"));
        Log.info("Location working days and timings are successfully read from excel file and populated in the form");
        modal.clickOnUpdateBtn();
        nav.checkTotalToastCountAndExpectedText(nav.getTotalToastsNum(), "Locations has been updated.");
        Log.info("Success toast message is shown for Workings Days Tab of Add new Location modal upon clicking on the update button");
    }

    @Test(groups = {"Locations Tab"})
    public void editLocationGeneralTab()
    {
        table.doubleClickOnTableFirstRow();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "addNewLocation",locColIndex);
        Assert.assertFalse(loc.verifyEditLocationGeneralTabData(data), "After adding the location, the field values in General Tab are not saved properly in location modal");
        Log.info("After adding the location, the field values in General Tab are saved properly in location modal");

        modal.closeModal();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();
    }

    @Test(groups = {"Locations Tab"})
    public void editLocationWorkDaysTab()
    {
        table.doubleClickOnTableFirstRow();
        Map<String, String> data = readExcelColumns(absolutePathToExcel, "addNewLocation",locColIndex);
        List<String> workDaysFromFile = getListAfterSplitString(data.get("workDays"),",");
        Assert.assertFalse(loc.verifyEditLocationWorkDaysTabData(workDaysFromFile, data), "After adding the location, the field values in WorkDays Tab are not saved properly in location modal");
        Log.info("After adding the location, the field values in WorkDays Tab are saved properly in location modal");

        modal.closeModal();
        nav.waitForTabLoaderVisible();
        nav.waitForTabLoaderComplete();
    }

    @Test(groups = {"Locations Tab"})
    public void verifyChangeViewLocations() {
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

}
