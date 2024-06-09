package com.hazelsuite.pageobjects.components;

import com.hazelsuite.utilities.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

import static com.hazelsuite.utilities.CommonUtil.*;

public class Table
{
    private final By valueAtFirstRowColumnOne = By.xpath("//table/tbody/tr[1]/td[2]");
    private final By tableHeaderNames = By.xpath("//span[@class='d-flex space-wrap']");
    private final By pageTotalNum = By.xpath("//ul[@class='pagination']//li[@class='pagination__item pagination-info mx-2 page-item']");
    private static final String HEADER_ROW = "(//th[@class='table-row-border wordbreak-audit'])[";
    private final By headerAtLeftMostPos = By.xpath("(//th[@class='table-row-border wordbreak-audit']//span)[2]");
    private final By numOfTableRows = By.xpath("//tbody[@role='rowgroup']//tr");
    private final By numOfTableCols = By.xpath("//th[@role='columnheader' and position() > 1]");

    public boolean matchValueAtFirstRowColumnOne(String value)
    {
        scrollElementToView(valueAtFirstRowColumnOne, ScrollDirection.BOTTOM);
        return value.equals(getText(valueAtFirstRowColumnOne));
    }

    public void dragColumnHeaderToLeftMost(String colHeader)
    {
        if (!getText(headerAtLeftMostPos).equals(colHeader))
        {
            List<WebElement> tableHeaderNamesList = findWebElements(tableHeaderNames);
            for (int i=1; i<tableHeaderNamesList.size();i++)
            {
                if(Objects.equals(tableHeaderNamesList.get(i).getText(), colHeader))
                {
                    Log.info(HEADER_ROW + (i+1) + "]");
                    Log.info(HEADER_ROW + 2 + "]");
                    dragAndDropElement(findWebElement(By.xpath(HEADER_ROW + (i+1) + "]")), findWebElement(By.xpath(HEADER_ROW + 2 + "]")));
                    break;
                }
            }
            Log.info(colHeader +  " table header is dragged to left most successfully");
        }
        else
            Log.info(colHeader +  " table header is already located at left most position");
    }

    public void waitForPaginationTextUpdate()
    {
        waitTillTextSizeChanges(pageTotalNum, 0);
    }

    public int getPaginationNumber()
    {
        String totalPage = getText(pageTotalNum);
        String[] arrOfStr = totalPage.split(" ");
        int lastIndex = arrOfStr.length - 1;
        return Integer.parseInt(arrOfStr[lastIndex]);
    }

    public void doubleClickOnTableFirstRow()
    {
        scrollElementToView(valueAtFirstRowColumnOne, ScrollDirection.BOTTOM);
        doubleClickOnElement(valueAtFirstRowColumnOne);
        Log.info("Double click on table row containing value: " + getText(valueAtFirstRowColumnOne));
    }

    public void waitForTableRowUpdate(int beforeAddPageCount)
    {
        waitTillEleCountChanges(numOfTableRows, beforeAddPageCount);
    }

    public int getNumOfTableCols()
    {
        return findWebElements(numOfTableCols).size();
    }

}
