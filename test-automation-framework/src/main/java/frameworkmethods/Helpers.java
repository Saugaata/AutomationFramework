package frameworkmethods;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import utilities.Configuration;
import utilities.DriverFactory;

public class Helpers {

    public WebDriver driver;
    private JavascriptExecutor js;
    public static Configuration config;
    public WebDriverWait wait;

    public Helpers() {
        config = new Configuration();
        this.driver = DriverFactory.getDriver(config.get("BROWSER"));
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.valueOf(config.get("IMPLICIT_WAIT"))));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.valueOf(config.get("EXPLICIT_WAIT"))));
        this.js = (JavascriptExecutor) driver;
    }

    public WebDriver getDriver() {
        return driver;
    }
    
    
 // *********************** Alert Helpers ************************ //

    /**
     * Gets and returns the Alert object
     */
    private Alert getAlert() {
        return driver.switchTo().alert();
    }

    /**
     * Accepts the currently active Alert
     */
    public void acceptAlert() {
        getAlert().accept();
    }

    /**
     * Dismisses the currently active Alert
     */
    public void dismissAlert() {
        getAlert().dismiss();
    }

    /**
     * Gets the text from the Alert
     */
    public String getAlertText() {
        return getAlert().getText();
    }

    /**
     * Sends text to the Alert prompt
     */
    public void sendTextToAlert(String text) {
        getAlert().sendKeys(text);
    }

    /**
     * Checks if an Alert is present
     */
    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // *********************** Drop-down Helpers ************************ //

    /**
     * Selects a drop-down value by visible text
     */
    public void selectByVisibleText(By locator, String text) {
        Select dropdown = new Select(driver.findElement(locator));
        dropdown.selectByVisibleText(text);
    }

    /**
     * Selects a drop-down value containing partial text
     */
    public void selectByContainsVisibleText(By locator, String containsText) {
        Select dropdown = new Select(driver.findElement(locator));
        dropdown.selectByVisibleText(containsText); // Note: Selenium has no built-in "contains" for dropdowns — this assumes exact match
    }

    /**
     * Selects a drop-down value by value
     */
    public void selectByValue(By locator, String value) {
        Select dropdown = new Select(driver.findElement(locator));
        dropdown.selectByValue(value);
    }

    /**
     * Selects a drop-down value by index
     */
    public void selectByIndex(By locator, int index) {
        Select dropdown = new Select(driver.findElement(locator));
        dropdown.selectByIndex(index);
    }

 // *********************** Element Helpers ************************ //

    /**
     * Sends text to a textbox
     */
    public void setValue(By locator, String text) {
        WebElement element = waitForElementToBeVisible(locator, 10);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Clicks a link by its visible text
     */
    public WebElement clickLinkByText(String linkText) {
        try {
            WebElement linkElement = waitForElementToBeClickableByLinkText(linkText, 10);
            linkElement.click();
            return linkElement;
        } catch (Exception e) {
            System.err.println("Error clicking link with text: " + linkText + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Clicks a link by partial link text
     */
    public WebElement clickLinkByPartialText(String partialLinkText) {
        try {
            WebElement linkElement = waitForElementToBeClickableByPartialLinkText(partialLinkText, 10);
            linkElement.click();
            return linkElement;
        } catch (Exception e) {
            System.err.println("Error clicking link with partial text: " + partialLinkText + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Clicks a link by XPath
     */
    public WebElement clickLinkByXPath(String xpath) {
        try {
            WebElement linkElement = waitForElementToBeClickableByXPath(xpath, 10);
            linkElement.click();
            return linkElement;
        } catch (Exception e) {
            System.err.println("Error clicking link with XPath: " + xpath + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Clicks an element using its locator
     */
    public WebElement clickElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            return element;
        } catch (NoSuchElementException e) {
            System.err.println("Element not found: " + locator);
            return null;
        } catch (TimeoutException e) {
            System.out.println("Element not clickable within timeout: " + config.get("EXPLICIT_WAIT") + " seconds: " + locator);
            return null;
        } catch (Exception e) {
            System.err.println("Error clicking element: " + locator);
            return null;
        }
    }

    /**
     * Gets the text from an element by locator
     */
    public String getText(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.getText();
        } catch (NoSuchElementException e) {
            System.err.println("Element not found: " + locator);
            return null;
        } catch (Exception e) {
            System.err.println("Error getting text: " + locator);
            return null;
        }
    }

    /**
     * Checks if an element is displayed
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Performs right-click (context click) on an element
     */
    public void rightClick(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(locator);
        actions.contextClick(element).perform();
    }
    
    /**
     * Performs a context (right) click on an element
     */
    public void rightClickElement(By locator) {
        try {
            Actions actions = new Actions(driver);
            WebElement element = driver.findElement(locator);
            actions.contextClick(element).perform();
        } catch (Exception e) {
            System.err.println("Error performing right-click: " + e.getMessage());
        }
    }

    /**
     * Double-clicks a WebElement
     */
    public void doubleClickElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();
        } catch (Exception e) {
            System.err.println("Error performing double-click: " + e.getMessage());
        }
    }

    /**
     * Checks whether a WebElement (e.g., checkbox) is selected
     */
    public boolean isElementSelected(By locator) {
        try {
            WebElement checkbox = driver.findElement(locator);
            return checkbox.isSelected();
        } catch (NoSuchElementException e) {
            System.err.println("Element not found: " + locator);
            return false;
        }
    }

    /**
     * Scrolls the page until the element is in view using JavaScript
     */
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Clicks an element using JavaScript
     */
    public void jsClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Highlights an element on the page with a red border using JavaScript
     */
    public void highlightElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    /**
     * Reads a JSON file into a List of Maps using Jackson
     */
    public List<Map<String, Object>> readJsonData(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filePath), new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Captures a screenshot and saves it to the configured path with a timestamp
     */
    public void captureScreenshot() {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = config.get("SCREENSHOT_PATH") + File.separator + "screenshot_" + timestamp + ".png";
            FileUtils.copyFile(srcFile, new File(filename));
            System.out.println("Screenshot saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Error saving screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Switches to a browser tab/window by index
     */
    public void switchToTabByIndex(int index) {
        try {
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            if (index < tabs.size()) {
                driver.switchTo().window(tabs.get(index));
            } else {
                throw new IndexOutOfBoundsException("Tab index out of range: " + index);
            }
        } catch (Exception e) {
            System.err.println("Error switching to tab: " + e.getMessage());
        }
    }

    /**
     * Closes the current browser window
     */
    public void closeWindow() {
        driver.close();
    }

    /**
     * Switches to the first (main) window
     */
    public void switchToMainWindow() {
        Set<String> windows = driver.getWindowHandles();
        if (!windows.isEmpty()) {
            driver.switchTo().window(windows.iterator().next());
        }
    }

    // *********************** Wait Helpers ************************ //

    /**
     * Returns a WebDriverWait object with a specified timeout
     */
    private WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    /**
     * Waits for an element to become clickable
     */
    public WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to become clickable by link text
     */
    public WebElement waitForElementToBeClickableByLinkText(String linkText, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(By.linkText(linkText)));
    }

    /**
     * Waits for an element to become clickable by partial link text
     */
    public WebElement waitForElementToBeClickableByPartialLinkText(String partialLinkText, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(By.partialLinkText(partialLinkText)));
    }

    /**
     * Waits for an element to become clickable by XPath
     */
    public WebElement waitForElementToBeClickableByXPath(String xpath, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    /**
     * Waits for an element to become visible
     */
    public WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        try {
            WebElement webElement = getWait(timeoutInSeconds)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return webElement;
        } catch (TimeoutException e) {
            System.out.println("Element not visible in time: " + e.getMessage());
            return null;
        }
    }

    /**
     * Waits for an element to disappear
     */
    public boolean waitForElementToDisappear(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    

    /**
     * Waits for an element to be present in the DOM (not necessarily visible)
     */
    public WebElement waitForPresenceOfElement(By locator, int timeoutInSeconds) {
    	return getWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }


    /**
     * Waits for specific text to be present in an element
     */
    public boolean waitForTextToBePresent(By locator, String text, int timeoutInSeconds) {
        return getWait(timeoutInSeconds)
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    /**
     * Waits for an alert to be present
     */
    public void waitForAlertToBePresent(int timeoutInSeconds) {
        getWait(timeoutInSeconds).until(ExpectedConditions.alertIsPresent());
    }

    // *********************** Miscellaneous Helpers ************************ //

    /**
     * Refreshes the page
     */
    public void refreshPage() {
        driver.navigate().refresh();
    }

    /**
     * Closes the current browser instance
     */
    public void closeBrowser() {
        driver.close();
    }

    /**
     * Closes all browser instances
     */
    public void quitBrowser() {
        driver.quit();
    }


}
