package com.hazelsuite.pageobjects.components;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;

import static com.hazelsuite.utilities.CommonUtil.*;

public class PasswordSetup
{
    private final By emailConfirmationTitle = By.className("email-confirmation__title");
    private final By createPassBtn = By.linkText("Create Password");
    private final By userName = By.id("userName");
    private final By userPass = By.id("password");
    private final By userConfirmPass = By.id("confirmPassword");
    private final By submitBtn = By.xpath("//button[text()='Submit']");

    Navigation nav = new Navigation();

    public String getEmailConfirmationText()
    {
        return getText(emailConfirmationTitle);
    }

    public String getUserNameDuringVerification()
    {
        clickOn(createPassBtn);
        Log.info("Click on Create Password Button");
        nav.waitUntilOSUNameVisible();
        Log.info("Proceeding to the Set Password Page");
        return getAttributeValue("value",userName);
    }

    public void setUserPassword(String pass, String confirmPass)
    {
        sendText(userPass, pass);
        sendText(userConfirmPass, confirmPass);
        clickOn(submitBtn);
        nav.waitForLoginLoaderVisible();
        nav.waitForLoginLoaderComplete();
    }
}
