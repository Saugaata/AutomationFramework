package utilities;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import frameworkmethods.Helpers;

public class ReportManager extends Helpers {

    public static ExtentReports extent = new ExtentReports();
    public static ExtentTest test;
    private static ThreadLocal<ExtentTest> testT = new ThreadLocal<>();

    public void initReports() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String dateTime = currentTime.format(dtFormatter);

        String reportName = Helpers.config.get("EXTENTREPORTPATH") + "OBR Extent Report - " + dateTime + ".html";

        try {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportName);

            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("MyReport");
            sparkReporter.config().setTimeStampFormat("YYYY dd, MMMM, HH:mm:ss");
            sparkReporter.config().setReportName("Extent Report Demo");
            sparkReporter.config().setEncoding("UTF-8");

            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flushReport() {
        extent.flush();
    }

    public void startTest(String methodName) {
        test = extent.createTest("TEST: " + methodName);
        test.assignAuthor(System.getProperty("user.name"));
        test.assignDevice(System.getProperty("os.name"));

        logInfo("Test started: " + methodName);
    }

    public void getResult(Method method, ITestResult result) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = Helpers.config.get("SCREENSHOTPATH");
            takeScreenshot(driver, result.getMethod().getMethodName());
            //takeScreenshot(driver, result.getMethod().getMethodName());
            test.fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(
                    screenshotPath + method.getName() + "_" + timeStamp + ".png").build());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass(method.getName() + " is passed.");
        }
    }

    public static void logInfo(String message) {
        test.info(message);
    }

    public static void testPass(String message) {
        test.pass(message);
    }

    public static void testFail(String message) {
        test.fail(message);
    }

    public static void testFail(Throwable message) {
        test.fail(message);
    }

    public static void testFail(String message, Media media) {
        test.fail(message, media);
    }

    public static void logMethodName() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        test.info("Executing method: " + methodName);
    }

    public static void logMethodName(String message) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        test.info("Executing method: " + methodName + " > " + message);
    }

    // Below set of code can be used when ThreadLocal is used for ExtentTest

    public static ExtentReports getReporter() {
        return extent;
    }

    public static void setExtentTest(ExtentTest test) {
        testT.set(test);
    }
}
