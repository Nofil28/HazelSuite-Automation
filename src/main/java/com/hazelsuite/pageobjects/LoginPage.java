package com.hazelsuite.pageobjects;

import static com.hazelsuite.utilities.CommonUtil.*;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;


public class LoginPage
{
    private final By email = By.name("email");
    private final By password = By.name("password");
    private final By loginBtn = By.xpath("//button[text()='Login']");
    private final By testBtn =

    public void loginUser(String userEmail, String userPass)
    {
        boolean reload = false;
        try{
            sendText(email, userEmail);
        }
        catch (NoSuchElementException e){
            Log.error("Page was not loaded correctly, so refreshing");
            reload = true;
            refreshBrowser();
        }

        if(reload)
            sendText(email, userEmail);
        sendText(password, userPass);
        clickOn(loginBtn);
        Log.info("Login with valid email and valid password");
    }

}
