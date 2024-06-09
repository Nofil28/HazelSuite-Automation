package com.hazelsuite.base;

import com.hazelsuite.utilities.Log;
import com.hazelsuite.utilities.PropertiesFile;
import com.hazelsuite.utilities.ScreenRecorderUtil;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.util.Map;

import static com.hazelsuite.utilities.CommonUtil.*;
import static com.hazelsuite.utilities.ExcelFileUtil.readExcelColumns;
import static com.hazelsuite.utilities.SQLiteUtil.deleteDataFromTables;

public class Setup
{
    @BeforeSuite
    @Parameters({"url"})
    public void initializeBrowser(String url) {
        launchBrowser(url);
        Log.info("Navigating to the following Url: " + url);

        try {
            ScreenRecorderUtil.startRecording("Video");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterSuite
    public void stopRecording() {
       try {
           ScreenRecorderUtil.stopRecording();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       closeBrowser();
    }

}
