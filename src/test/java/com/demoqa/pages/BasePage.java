package com.demoqa.pages;

import com.demoqa.utilities.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/* The BasePage class is made abstract to emphasize its role as a template for deriving other page classes, promoting inheritance.
This approach enhances code reusability and encourages the creation of specialized page classes through extension,
eliminating the necessity of declaring and initializing a BasePage within the BaseTest class.*/

public abstract class BasePage {

    // Declared Class member variables for interacting with the web page (initialized in the constructor):
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }


    // Helper method for waiting until an element is visible
    protected void waitUntilElementIsVisible(WebElement webElement) {
        try {
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(webElement)));
        } catch (StaleElementReferenceException e) {
            LoggerUtil.warning("Stale element reference encountered. Refreshing and retrying...");
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(webElement)));
        }
    }


    // region 1.    Basic Browser Operations (Get Methods and Navigation)
    public void getUrl(String url) {
        driver.get(url);
    }

    // Retrieves and returns the title of the current web page.
    public void getTitle() {
        driver.getTitle();
    }

    // Retrieves and returns the current URL of the web page.
    public void getCurrentUrl() {
        driver.getCurrentUrl();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void navigateToPage(String url) {
        driver.navigate().to(url);
    }

    // @formatter:off
            // Refreshes the current page. This method navigates back to the same page, effectively reloading it.
            public void refreshPage() {
                driver.navigate().refresh();
            }

            // Navigates back to the previous page in the browser's history.

            public void navigateBack() {
                driver.navigate().back();
            }

            // Navigates forward to the next page in the browser's history.
            public void navigateForward() {
                driver.navigate().forward();
            }
            // @formatter:on
    // endregion


    // region 2.    Basic Elements Operations (click, submit, clear, sendKeys, getText)
    public void clickElement(WebElement webElement) {
        try {
            waitUntilElementIsVisible(webElement);
            webElement.click();
        } catch (TimeoutException e) {
            throw new TimeoutException(e);
        }
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void submitForm(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        webElement.submit();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void clearElement(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        webElement.clear();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void sendKeysToElement(WebElement webElement, String text) {
        waitUntilElementIsVisible(webElement);
        webElement.sendKeys(text);
    }
    // -----------------------------------------------------------------------------------------------------------------

    public String getTextFromElement(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        return webElement.getText();
    }
    // endregion


    // region 3.    Actions (doubleClick, rightClick, mouseHover, clickAndHold,dragAndDrop)
    public void doubleClick(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        actions.doubleClick(webElement).perform(); // Double-click the element
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void rightClick(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        actions.contextClick(webElement).perform();  // Right-clicks the element (context menu)
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void mouseHover(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        actions.moveToElement(webElement).perform(); // Hovers the mouse over the element
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void clickAndHold(WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        actions.clickAndHold(webElement).perform();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
        WebElement source = wait.until(ExpectedConditions.visibilityOf(sourceElement));
        WebElement target = wait.until(ExpectedConditions.visibilityOf(targetElement));
        actions.dragAndDrop(source, target).perform();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public void keyPress(WebElement webElement, Keys key, String text) {
        waitUntilElementIsVisible(webElement);
        actions.keyDown(key).sendKeys(text).keyUp(key).perform();
        // Holds down the Control key, types 'a', and releases the Control key (select all)
    }
    // endregion


    // region 4.    Element validation: (isEnabled, isDisplayed, isSelected, isClickable, isChecked, isTextPresentInElement)

    public boolean isElementEnabled(@NotNull WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        return webElement.isEnabled();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public boolean isElementDisplayed(@NotNull WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        return webElement.isDisplayed();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public boolean isElementChecked(@NotNull WebElement webElement) {
        waitUntilElementIsVisible(webElement);
        return webElement.isSelected();
    }
    // -----------------------------------------------------------------------------------------------------------------

    public boolean isElementClickable(@NotNull WebElement webElement) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
            return true; // The element is clickable
        } catch (org.openqa.selenium.TimeoutException e) {
            return false; // The element is not clickable
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public boolean isCurrentPageUrlEqualTo(String expectedUrl) {
        try {
            // Get the actual URL
            wait.until(ExpectedConditions.urlToBe(expectedUrl));
            String actualUrl = driver.getCurrentUrl();

            // Compare the actual URL with the expected URL
            boolean isOnExpectedPage = actualUrl.equals(expectedUrl);

            if (!isOnExpectedPage) {
                // Log the expected and actual URLs if the test fails
                System.out.println("Test failed. Expected URL: " + expectedUrl);
                System.out.println("Actual URL: " + actualUrl);
            }

            return isOnExpectedPage;
        } catch (TimeoutException e) {
            // Handle the exception if the URL doesn't match the expected URL within the specified timeout
            System.out.println("Test failed. URL did not match the expected URL: " + expectedUrl);
            return false;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    public boolean isTextPresentInElement(WebElement webElement, String text) {
        waitUntilElementIsVisible(webElement);
        String elementText = webElement.getText();
        return elementText.contains(text);
    }
    // -----------------------------------------------------------------------------------------------------------------

    public boolean isOptionPresentInDropdown(WebElement dropdownElement, String optionText) {
        // Wait for the dropdown element to be visible
        WebElement selectFromDropDown = wait.until(ExpectedConditions.visibilityOf(dropdownElement));

        // Create a Select object
        Select select = new Select(selectFromDropDown);

        // Initialize a flag to track if the option is present
        boolean isOptionPresent = false;

        // Loop through the options and check if "North Macedonia" is listed
        for (WebElement option : select.getOptions()) {
            if (option.getText().equals(optionText)) {
                isOptionPresent = true;
                break;
            }
        }
        // Return the result
        return isOptionPresent;
    }
    // -----------------------------------------------------------------------------------------------------------------

    // - areElementsPresent(By selector): Check if a list of elements matching a selector is present.
    // - areElementsVisible(List<WebElement> elements): Check if a list of elements is visible.
    // - isAlertPresent(): Check if an alert dialog is present.

    // endregion


    // region 5.    Dropdown Handling (selectByIndex/ByVisibleText/ByValue, deselectAllI/ByIndex/ByVisibleText/ByValue)
    // Selects the specified option by its Text from the given dropdown, handling potential exceptions and providing detailed error reporting.
    public void selectOptionByVisibleText(WebElement dropdown, String text) {
        try {
            Select select = new Select(dropdown);
            select.selectByVisibleText(text);
        } catch (NoSuchElementException e) {
            // Log the exception message for better error reporting
            LoggerUtil.error("Element not found in the dropdown: " + e.getMessage(), e);
            // Rethrow a more specific exception to indicate the nature of the issue
            throw new RuntimeException("Failed to select option by value: " + text, e);
        } catch (Exception e) {
            // Log the exception message for better error reporting
            LoggerUtil.error("An unexpected error occurred: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }

    // Selects the specified option by its value from the given dropdown, handling potential exceptions and providing detailed error reporting.
    public void selectOptionByValue(WebElement dropdown, String value) {
        try {
            Select select = new Select(dropdown);
            select.selectByValue(value);
        } catch (NoSuchElementException e) {
            // Log the exception message for better error reporting
            LoggerUtil.error("Element not found in the dropdown: " + e.getMessage(), e);
            // Rethrow a more specific exception to indicate the nature of the issue
            throw new RuntimeException("Failed to select option by value: " + value, e);
        } catch (Exception e) {
            // Log the exception message for better error reporting
            LoggerUtil.error("An unexpected error occurred: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }

    // Selects the specified option by its Index from the given dropdown, handling potential exceptions and providing detailed error reporting.
    public void selectOptionByIndex(WebElement dropdown, int index) {
        try {
            Select select = new Select(dropdown);
            select.selectByIndex(index);
        } catch (NoSuchElementException e) {
            // Log the exception message for better error reporting
            LoggerUtil.error("Element not found in the dropdown: " + e.getMessage(), e);
            // Rethrow a more specific exception to indicate the nature of the issue
            throw new RuntimeException("Failed to select option by value: " + index, e);
        } catch (Exception e) {
            // Log the exception message for better error reporting
            LoggerUtil.error("An unexpected error occurred: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }

    // - deselectAllOptionsInDropdown(Select select): Deselect all selected options in the dropdown.
    // - deselectOptionInDropdownByIndex(Select select, int index): Deselect an option by index (1).
    // - deselectOptionInDropdownByVisibleText(Select select, String visibleText): Deselect an option by visible text ("Ford").
    // - deselectOptionInDropdownByValue(Select select, String value): Deselect an option by value ("ford").

    // endregion


    // region 6.    Mouse Hover Actions:


    //endregion


    // region 7.    Scroll Handling:
    public void scrollToElementIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToTopOfPage() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }
    //endregion


    // region   8.    Frame Handling:
    // included Error Handling in case the frame switching operation fails.
    // Added logging statements to indicate when the frame switching occurs.
    public void switchToFrameByIndex(int index) {
        try {
            driver.switchTo().frame(index);
            LoggerUtil.info("Switched to frame with index: " + index);
        } catch (NoSuchFrameException e) {
            LoggerUtil.error("Frame with index " + index + " not found", e);
            // Handle or throw an exception as appropriate for your scenario
        }
    }

    public void switchToFrameByStringName(String name) {
        try {
            driver.switchTo().frame(name);
            LoggerUtil.info("Switched to frame with name: " + name);
        } catch (NoSuchFrameException e) {
            LoggerUtil.error("Frame with name " + name + " not found: ", e);
            // Handle or throw an exception as appropriate for your scenario
        }
    }

    public void switchToFrameByWebElement(WebElement webElement) {
        try {
            driver.switchTo().frame(webElement);
            LoggerUtil.info("Switched to frame with WebElement: " + webElement);
        } catch (NoSuchFrameException e) {
            LoggerUtil.error("Frame with WebElement " + webElement + " not found: ", e);
            // Handle or throw an exception as appropriate for your scenario
        }
    }

    public void switchToDefaultFrameContent() {
        driver.switchTo().defaultContent();
        LoggerUtil.info("Switched to default content");
    }

// endregion


    // region 9.    Alert Handling:
    public String alertGetText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            // Log the exception message and stack trace for better error reporting
            LoggerUtil.error("Failed to retrieve text from alert: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }

    public void alertAccept() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Log the exception message and stack trace for better error reporting
            LoggerUtil.error("Failed to accept the alert: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }

    // Dismiss an alert dialog
    public void alertDismiss() {
        try {
            driver.switchTo().alert().dismiss();
        } catch (Exception e) {
            // Log the exception message and stack trace for better error reporting
            LoggerUtil.error("Failed to dismiss the alert: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }

    public void alertSendKeys(String keysToSend) {
        try {
            driver.switchTo().alert().sendKeys(keysToSend);
        } catch (Exception e) {
            // Log the exception message and stack trace for better error reporting
            LoggerUtil.error("Failed to send Keys the alert: " + e.getMessage(), e);
            // Rethrow the original exception
            throw new RuntimeException(e);
        }
    }
    //endregion


    // region 10.   Windows and Tabs Handling


    //endregion
}

