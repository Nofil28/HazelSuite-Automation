package com.hazelsuite.utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class CommonUtil {
    private static WebDriver driver;
    private static Actions act;
    private static final int waitTime = Integer.parseInt(PropertiesFile.readKey("waitTime"));

    public static void launchBrowser(String url) {
        WebDriverManager.chromedriver().setup();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("autofill.profile_enabled", false);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        driver.get(url);
        driver.manage().window().maximize();
        act = new Actions(driver);
    }

    public static WebElement findWebElement(By locator) {
        return driver.findElement(locator);
    }

    public static List<WebElement> findWebElements(By locator) {
        return driver.findElements(locator);
    }

    public static void sendText(By locator, String text) {
        clearText(locator);
        driver.findElement(locator).sendKeys(text);
    }

    public static void clearText(By locator) {
        driver.findElement(locator).clear();
    }

    public static void clickOn(By locator) {
        waitUntilElementToBeClickable(locator);
        driver.findElement(locator).click();
    }

    public static String getPageUrl() {
        return driver.getCurrentUrl();
    }

    public static void waitUntilUrlToBe(String url) {
        explicitWait().until(ExpectedConditions.urlToBe(url));
    }

    public static void closeBrowser() {
        driver.close();
    }

    public static void quitBrowser() {
        driver.quit();
    }


    public static String getText(By locator) {
        return driver.findElement(locator).getText();
    }

    public static String getSelectedOptionTextFromDropdown(By locator) {
        Select select = new Select(driver.findElement(locator));
        return select.getFirstSelectedOption().getText();
    }

    public static void waitUntilElementInvisible(By locator) {
        explicitWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitUntilElementVisible(By locator) {
        explicitWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static boolean isElementDisplayed(By locator) {
        waitUntilElementVisible(locator);
        return driver.findElement(locator).isDisplayed();
    }

    public static void selectOptionFromDropdown(By locator, String opt) {
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(opt);
    }

    public static String getSubStringFromStart(String s, char c, int startIndex)
    {
        int index = s.indexOf(c);
        return s.substring(startIndex,index);
    }

    public static String getVerificationLinkSubString(String s)
    {
        int startIndex = s.indexOf("<a href=\"");
        int endIndex = s.indexOf("class=\"btn btn-primary\"");
        return "http://" + s.substring(startIndex + 16,endIndex-2);
    }

    public static String getEmailContentSubString(String s)
    {
        int startIndex = s.indexOf("<h2>");
        int endIndex = s.indexOf("</h2>");
        return s.substring(startIndex + 4,endIndex);
    }

    public static String getSubStringFromEnd(String s, char c, int endIndex)
    {
        int index = s.indexOf(c);
        return s.substring(index, endIndex);
    }

    public static boolean doesElementExist(By locator)
    {
        return !driver.findElements(locator).isEmpty();
    }

    public static void waitUntilElementPresence(By locator)
    {
        explicitWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void implicitWaitUntil(int secs)
    {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(secs));
    }

    public static void waitUntilElementToBeClickable(By locator)
    {
        explicitWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitUntilTextToBePresent(WebElement ele, String text)
    {
        explicitWait().until(ExpectedConditions.textToBePresentInElement(ele, text));
    }

    public static WebDriverWait explicitWait()
    {
        return new WebDriverWait(driver, Duration.ofSeconds(waitTime));
    }

    public static int getNumOfChildElements(By parent, By childLoc)
    {
        WebElement parentEle = findWebElement(parent);
        return parentEle.findElements(childLoc).size();
    }

    public static List<String> getListAfterSplitString (String str, String regex)
    {
        String[] arrOfStr = str.split(regex);
        List<String> listOfStr = new ArrayList<>();
        listOfStr.addAll(Arrays.asList(arrOfStr));
        return listOfStr;
    }

    public static void openUrlInNewTab(String url)
    {
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(url);
    }

    public static String getAttributeValue(String attr, By locator) {
        return driver.findElement(locator).getAttribute(attr);
    }

    public static void dragAndDropElement(WebElement sourceEle, WebElement destEle)
    {
        Action dragAndDrop = act.clickAndHold(sourceEle)
                .moveToElement(destEle)
                .release(destEle)
                .build();

        dragAndDrop.perform();
    }

    public static boolean doesUrlContain(String text)
    {
        return driver.getCurrentUrl().contains(text);
    }

    //TOP(true) means the scroll will be from top to bottom and BOTTOM(false) means the scroll will be from bottom to top
    public enum ScrollDirection {
        TOP(true),
        BOTTOM(false);

        private final boolean value;

        ScrollDirection(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }
    }

    public static void scrollElementToView(By locator, ScrollDirection direction) {
        String directionStr = direction == ScrollDirection.TOP ? "true" : "false";
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(" + directionStr + ");", findWebElement(locator));

    }

    public static void clickOnChkboxUsingJS(By locator)
    {
        if(!isCheckboxSelected(locator)){
            WebElement element = findWebElement(locator);
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
        }
    }

    public static void doubleClickOnElement(By locator)
    {
        act.doubleClick(driver.findElement(locator)).perform();
    }

    public static void clickOnTabKey()
    {
        act.sendKeys(Keys.TAB).build().perform();
    }

    public static void clickOnBackSpaceKey()
    {
        act.sendKeys(Keys.BACK_SPACE).build().perform();
    }

    public static void sendKeysActionClass(String value)
    {
        act.sendKeys(value);
        act.perform();
    }

    public static void clickOnEnterKey()
    {
        act.sendKeys(Keys.ENTER).build().perform();
    }

    public static boolean isCheckboxSelected(By locator)
    {
        return driver.findElement(locator).isSelected();
    }

    public static String getTextFromActiveElement()
    {
        return driver.switchTo().activeElement().getText();
    }

    public static void refreshBrowser()
    {
        driver.navigate().refresh();
    }

    public static void waitTillTextSizeChanges(By locator,int initialSize){
        explicitWait().until((ExpectedCondition<Boolean>) input -> {

            int currentSize = getText(locator).length();
            return currentSize != initialSize;
        });
    }

    public static String returnAbsolutePath(String relativePath)
    {
        return Paths.get(System.getProperty("user.dir"), relativePath).toString();
    }

    public static void waitTillEleCountChanges(By locator,int initialCount) {
        explicitWait().until((ExpectedCondition<Boolean>) input -> {
            int currentCount = findWebElements(locator).size();
            return currentCount != initialCount;
        });
    }

    public static WebDriver getDriver()
    {
        return driver;
    }

    public static void switchBetweenTabs() {
        ArrayList<String> tab = new ArrayList<>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tab.get(1));
        getDriver().close();
        getDriver().switchTo().window(tab.get(0));
    }

}
