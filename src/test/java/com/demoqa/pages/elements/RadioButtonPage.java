package com.demoqa.pages.elements;

import com.demoqa.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RadioButtonPage extends BasePage {

    @FindBy(id = "item-2")
    private WebElement radioButtonSectionLink;
    @FindBy(xpath = "//label[@for='yesRadio']")
    WebElement yesRadioButton;
    @FindBy(xpath = "//label[@for='impressiveRadio']")
    WebElement impressiveButton;
    @FindBy(xpath = "//input[@id='noRadio']")
    WebElement noRadioButton;

    @FindBy(xpath = "//span[contains(text(),'Yes')]")
    WebElement YesSuccessMessage;

    @FindBy(xpath = "//span[contains(text(),'Impressive')]")
    WebElement impressiveSuccessMessage;

    // Constructor
    public RadioButtonPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isOnCheckBoxSubCategory() {
        return isCurrentPageUrlEqualTo("https://demoqa.com/radio-button");
    }

    public void clickOnRadioButtonSubCategory() {
        clickElement(radioButtonSectionLink);
    }

    public void clickOnYesRadioButton() {
        clickElement(yesRadioButton);
    }

    public void clickOnImpressiveRadioButton() {
        clickElement(impressiveButton);
    }

    public boolean isRadioButtonNoEnabled() {
       return noRadioButton.isEnabled();
    }



    public Object isImpressiveRadioButtonSelected(){
       return true ;
    }


    public String getSuccessMessageForYesSelection(){
        return getTextFromElement(YesSuccessMessage);
    }

    public String getSuccessMessageForImpressiveSelection(){
        return getTextFromElement(impressiveSuccessMessage);
    }


}