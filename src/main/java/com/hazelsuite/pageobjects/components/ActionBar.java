package com.hazelsuite.pageobjects.components;

import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static com.hazelsuite.utilities.CommonUtil.*;

public class ActionBar {
    private final By addNewBtn = By.xpath("//span[text()='Add New']");
    private final By actionsBtn = By.xpath("//span[text()='Actions']");
    private final By changeViewBtn = By.xpath("//button[text()='Change View']");
    private final By changeViewChkBoxes = By.xpath("//div[@class='pl-3']//div//input");
    private final By submitBtn = By.xpath("//button[text()='Submit']");
    private final By chkBoxNames = By.xpath("//div[@class='mb-2']//input");

    public boolean isAddNewBtnDisplayed() {
        return isElementDisplayed(addNewBtn);
    }

    public void clickOnAddNewBtn() {
        clickOn(addNewBtn);
        Log.info("Clicking on Add New button");
    }

    public void clickOnChangeViewBtn()
    {
        scrollElementToView(actionsBtn, ScrollDirection.BOTTOM);
        clickOn(actionsBtn);
        clickOn(changeViewBtn);
        Log.info("Opening the Change View modal");
    }
    public void checkAllCheckboxes()
    {
        List<WebElement> changeViewChkBoxesList = findWebElements(changeViewChkBoxes);
        for (WebElement chkbox : changeViewChkBoxesList)
        {
            if (!chkbox.isSelected()) {
                explicitWait().until(ExpectedConditions.elementToBeClickable(chkbox));
                chkbox.click();
            }
        }
        clickOn(submitBtn);
        Log.info("Checking all of the checkboxes in Change View modal");
    }

    public void uncheckAllCheckboxesExcept(String name)
    {
        List<WebElement> checkboxes = findWebElements(chkBoxNames);
        for (WebElement checkbox : checkboxes)
        {
            if(!checkbox.getAttribute("value").equals(name)){
                if(checkbox.isSelected()) {
                    explicitWait().until(ExpectedConditions.elementToBeClickable(checkbox));
                    checkbox.click();
                }
            }
        }
        clickOn(submitBtn);
        Log.info("Uncheck all checkboxes except: " + name);
    }

    public int getChangeViewChkBoxes()
    {
        return findWebElements(chkBoxNames).size();
    }

}
