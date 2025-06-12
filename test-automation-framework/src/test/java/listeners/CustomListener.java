package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;

import frameworkmethods.Helpers;
import utilities.ReportManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;

// This is the Custom listener class. It
public class CustomListener extends ReportManager implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object currentClass = result.getInstance();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        try {
            WebDriver driver = ((Helpers) currentClass).getDriver();

            System.out.println("Your test has failed. Check screenshot!");
            //takeScreenshot(driver, result.getName());

            String screenshotPath = Helpers.config.get("SCREENSHOTPATH");
            takeScreenshot(driver, result.getTestName()); 
            
            testFail(result.getThrowable());
            
            testFail(result.getName(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath + result.getName() + "_" + timeStamp + ".png").build());
            testFail(result.getName() + " has failed!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // To remove this method after listener development.
    @Override
    public void onTestSuccess(ITestResult result) {
        Object currentClass = result.getInstance();

        try {
            WebDriver driver = ((Helpers) currentClass).getDriver();
            takeScreenshot(driver, result.getName());

            testPass(result.getName() + " has passed. ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        // ReportManager.startTest(result.getMethod().getMethodName(), result.getMethod().getMethodName());
        // ReportManager.logInfo("Test started: " + result.getMethod().getMethodName());
    }
}
