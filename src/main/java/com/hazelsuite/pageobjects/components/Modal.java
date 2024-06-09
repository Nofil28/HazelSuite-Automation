package com.hazelsuite.pageobjects.components;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;

import static com.hazelsuite.utilities.CommonUtil.*;

public class Modal
{
    private final By modalTitle = By.className("modal-title");
    private final By modalCloseIcon = By.xpath("//button[@class='close modal-close-button-size modal-cross-icon-color']");
    private final By submitBtn = By.xpath("(//button[@class='submit-button btn btn-primary' and text()='Submit'])[1]");
    private final By nextBtn = By.xpath("//button[@class='btn btn-next btn-primary btn-lg pull-right btn btn-primary']");
    private final By prevBtn = By.xpath("//button[@class='btn btn-prev btn-primary btn-lg pull-left btn btn-primary']");
    private final By updateBtn = By.xpath("//button[@class='submit-button btn btn-primary' and text()='Update']");

    public String getModalTitle()
    {
        return getText(modalTitle);
    }

    public void closeModal()
    {
        clickOn(modalCloseIcon);
    }

    public void clickOnSubmitBtn()
    {
        clickOn(submitBtn);
        Log.info("Click on the submit button of modal");
    }

    public void clickOnNextBtn()
    {
        clickOn(nextBtn);
        Log.info("Click on the next button of modal");
    }

    public void clickOnPrevBtn()
    {
        clickOn(prevBtn);
    }

    public void clickOnUpdateBtn()
    {
        clickOn(updateBtn);
        Log.info("Click on the update button of modal");
    }

}



