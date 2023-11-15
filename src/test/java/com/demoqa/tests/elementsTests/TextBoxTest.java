package com.demoqa.tests.elementsTests;

import com.demoqa.tests.BaseTest;
import com.demoqa.utilities.DateTimeManagementUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

public class TextBoxTest extends BaseTest {

    @Test (groups = {"smoke", "regression"}, priority = 2)
    // Expected Result: The information provided (Full name, Email, Current address, Permanent address) is displayed correctly in the output element.
    public void testValidDataSubmission(){
        homePage.clickElementsNavigationBar();
        textBoxPage.clickOnTextBoxSubCategory();
        Assert.assertTrue(textBoxPage.isOnTextBoxSubCategory(), "Text Box Subcategory is not displayed.");
        Assert.assertEquals(textBoxPage.getCurrentPageTitle(), "DEMOQA");


        // Input valid data into the form fields
        textBoxPage.enterFullName(getRandomFirstName);
        textBoxPage.enterEmail(getRandomEmail);
        textBoxPage.enterCurrentAddress("3 MUB 45");
        textBoxPage.enterPermanentAddress("3 MUB 45");

        textBoxPage.clickSubmitButton();
        Assert.assertTrue(textBoxPage.isOutputMessageDisplayed());
        textBoxPage.scrollToOutputBlockDisplayed();

        // Check the displayed information in the output fields
        Assert.assertEquals(textBoxPage.getNameTextFromOutputField(), "Name:"+getRandomFirstName,
                "The displayed Full Name in the output field doesn't match the expected value.");
        Assert.assertEquals(textBoxPage.getEmailTextFromOutputField(), "Email:"+getRandomEmail,
                "The displayed Email address in the output Email field doesn't match the expected value.");
        Assert.assertEquals(textBoxPage.getCurrentAddressOutputField(), "Current Address :3 MUB 45",
                "The displayed Current Address in the output Current Address field doesn't match the expected value.");
        Assert.assertEquals(textBoxPage.getPermanentAddressOutputField(), "Permananet Address :3 MUB 45",
                "The displayed Permanent Address in the output Permanent Address field doesn't match the expected value.");

        // Call the formatTimestamp method and print the result
        System.out.println("Timestamp of Test Execution: " + DateTimeManagementUtils.formatTimestamp(new Date()));
    }

    @Test (groups = {"regression"}, priority = 1)
    // Expected Result: Checking if special characters are accepted in the form submission.
    public void testSpecialCharsFormAcceptance() {
        homePage.clickElementsNavigationBar();
        textBoxPage.clickOnTextBoxSubCategory();

        // Test special characters in Full Name field
        textBoxPage.enterFullName("!@!&^%%^#$@#$^#!");
        textBoxPage.clickSubmitButton();
        Assert.assertEquals(textBoxPage.getNameTextFromOutputField(), "Name:!@!&^%%^#$@#$^#!",
                "The displayed Full Name in the output field doesn't match the expected value.");
        textBoxPage.clearTextFromFullNameField();

        // Test special characters in Current Address field
        textBoxPage.enterCurrentAddress("!@!&^%%^#$@#$^#!");
        textBoxPage.clickSubmitButton();
        Assert.assertEquals(textBoxPage.getCurrentAddressOutputField(), "Current Address :!@!&^%%^#$@#$^#!",
                "The displayed Current Address in the output Current Address field doesn't match the expected value.");
        textBoxPage.clearTextFromCurrentAddressField();

        // Test special characters in Permanent Address field
        textBoxPage.enterPermanentAddress("!@!&^%%^#$@#$^#!");
        textBoxPage.clickSubmitButton();
        Assert.assertEquals(textBoxPage.getPermanentAddressOutputField(), "Permananet Address :!@!&^%%^#$@#$^#!",
                "The displayed Permanent Address in the output Permanent Address field doesn't match the expected value.");
    }

    @Test (groups = {"smoke"}, priority = 3)
    // Expected Result: Ensure that no visible change occurs after an empty submission.
    public void testNoVisibleChangeAfterEmptySubmit(){
        homePage.clickElementsNavigationBar();
        Assert.assertTrue(textBoxPage.isOnExpectedPage("https://demoqa.com/elements"));

        textBoxPage.clickOnTextBoxSubCategory();
        Assert.assertTrue(textBoxPage.isOnExpectedPage("https://demoqa.com/text-box"));

        // Capture the initial page source
        String initialPageSource = driver.getPageSource();

        textBoxPage.clickSubmitButton();

        // Capture the updated page source
        String updatedPageSource = driver.getPageSource();

        // Assert that the page source has changed (differences between initial and updated)
        Assert.assertNotEquals(initialPageSource, updatedPageSource,
                "There should be changes in the page source after clicking the 'Submit' button.");
    }

    @Test (groups = {"smoke"}, priority = 4)
    public void testValidAndInvalidEmailSubmission(){
        // Expected Result: Verify the behavior of email submission with valid and invalid email formats.
        homePage.clickElementsNavigationBar();
        textBoxPage.clickOnTextBoxSubCategory();

        // Enter a valid email and submit
        textBoxPage.enterEmail(getRandomEmail);
        textBoxPage.clickSubmitButton();

        // Assert that the displayed email in the output field matches the expected value
        Assert.assertEquals(textBoxPage.getEmailTextFromOutputField(), "Email:"+getRandomEmail,
                "The displayed Email address in the output Email field doesn't match the expected value.");

        // Clear the email field and submit
        textBoxPage.clearTextFromEmailField();
        textBoxPage.clickSubmitButton();

        // Enter an invalid email format and submit
        textBoxPage.enterEmail("example.gmail.com");
        textBoxPage.clickSubmitButton();

        // Assert that the element is not displayed (invisibility of the element)
        Assert.assertTrue(textBoxPage.isEmailNotDisplayedInOutputBlock(),
                "The element should not be displayed.");
    }
}