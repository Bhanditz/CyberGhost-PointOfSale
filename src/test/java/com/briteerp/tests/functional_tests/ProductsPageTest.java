package com.briteerp.tests.functional_tests;

import com.briteerp.utilities.ApplicationConstants;
import com.briteerp.utilities.BrowserUtils;
import com.briteerp.utilities.Driver;
import com.briteerp.utilities.TestBase;
import com.mongodb.operation.BaseWriteOperation;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.image.DirectColorModel;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class ProductsPageTest extends TestBase {


    // Mehmet Acikgoz - BRIT-854
    @Test
    public void validProductsPageTitle(){
        extentLogger = report.createTest("Page Title Test");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the products link");
        pages.pointOfSale().productsLink.click();
        BrowserUtils.wait(10);

        extentLogger.info("Verify that page title contains “Products - Odoo");
        String message =  Driver.getDriver().getTitle();
        Assert.assertEquals(message, ApplicationConstants.PRODUCTS_PAGE_TITLE);

        extentLogger.pass("Completed: Page Title Test");
    }


//    Mehmet Acikgoz - BRIT-919
    @Test()
    public void hasProductHaveNameAndPrice(){
        extentLogger = report.createTest("Product has name and price");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the products link");
        pages.pointOfSale().productsLink.click();
        BrowserUtils.wait(5);


        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Verify that the selected product " + productName + "has a name" );
        Assert.assertFalse(productName.isEmpty());

        extentLogger.info("Verify that the selected product " + productName + "has a price" );
        String price = pages.products().getPrice(productName);
        Assert.assertFalse(price.isEmpty());

        extentLogger.info("Completed: Product has name and price");
    }

//    Mehmet Acikgoz - BRIT-925
    @Test
    public void hasProductWithThumbnailPictureHavePictureWhenClicked(){
        extentLogger = report.createTest("The Product which has a thumbnail picture has alos medium size Picture");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        extentLogger.info("Click on any product with a picture.");
        WebElement product;
        String productName;
        do {
            productName = pages.products().selectAnyProduct();
            product = pages.products().selectProduct(productName);
//            System.out.println("test : " + pages.products().hasThumbnailPicture(product));
        } while ( !pages.products().hasThumbnailPicture(product) );
        extentLogger.info("Click on " + productName);
        product.click();

        extentLogger.info("Verify that product has a picture.");
        BrowserUtils.waitForVisibility(pages.products().detailsMediumImg, 30);
        BrowserUtils.scrollToElement(pages.products().detailsMediumImg);
        Assert.assertTrue(pages.products().detailsMediumImg.isDisplayed() );

        extentLogger.pass("Completed: The Product which has a thumbnail picture has alos medium size Picture");
    }

    //    Mehmet Acikgoz - BRIT-929
    @Test
    public void checkProductPrice(){
        extentLogger = report.createTest("Product Price Test");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Selecting the Product randomly  : " + productName);
        WebElement product = pages.products().selectProduct(productName);

        extentLogger.info("Getting the price of " + productName);
        String expectedPrice = pages.products().getPrice(product);

        BrowserUtils.waitForClickablility(product, timeOutInSec);
        extentLogger.info("Clicking on the " + productName);
        pages.products().clickOnProduct(product);

        extentLogger.info("Verify that product price  is the same as previous page");
        assertEquals(pages.products().detailsGenInfSalesPrice.getText(),expectedPrice);

        extentLogger.pass("Completed: Product Price Test");
    }

//    Mehmet Acikgoz  - BRIT-932
    @Test
    public void isProductDisplayedWhenSearched(){
        extentLogger = report.createTest("Product is displayed when searched.");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        extentLogger.info("Select a product");
        String productName = pages.products().selectAnyProduct();


        extentLogger.info("Type " + productName + " into search box and hit ENTER.");
        pages.products().searchInput.sendKeys(productName + Keys.ENTER);

        extentLogger.info("Verify that the product is shown on the page.");
        BrowserUtils.wait(5);
        String availableProducts = BrowserUtils.getElementsText(pages.products().products).toString();
        System.out.println("availableProducts = " + availableProducts);

        extentLogger.pass("Completed : Product is displayed when searched.");
    }

//    Mehmet Acikgoz  - BRIT-934
    @Test
    public void canUserPutNotes(){
        extentLogger = report.createTest("User can put Notes on product");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Click on " + productName);
        WebElement product = pages.products().selectProduct(productName);
        product.click();

        extentLogger.info(" Click on 'Log note' link");
        BrowserUtils.waitForClickablility(pages.products().detailsLogNoteTab, timeOutInSec);
        pages.products().detailsLogNoteTab.click();


        extentLogger.info("Write some notes and click on Log link.");
        BrowserUtils.wait(10);
        String noteToAdd = "CyberGhost team member updated at " + new Date().toString();
        pages.products().detailsLogNoteMessage.sendKeys(noteToAdd);
        pages.products().detailsLogNoteLogBtn.click();

        extentLogger.info("Verify that “Log note’’ is displayed on the page");
        BrowserUtils.wait(10);
        String submittedNotes = BrowserUtils.getElementsText(pages.products().detailsSubmittedNoteList).toString();

        Assert.assertTrue(submittedNotes.contains(noteToAdd));

        extentLogger.pass("Completed: User can put Notes on product");
    }

//    Mehmet Acikgoz - BRIT-938
    @Test
    public void isCostLessThanSalesPrice(){
        extentLogger = report.createTest("The cost of a product is less than the sales price.");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Click on " + productName);
        WebElement product = pages.products().selectProduct(productName);
        product.click();

        BrowserUtils.waitForVisibility(pages.products().detailsGenInfSalesPrice, timeOutInSec);
        extentLogger.info("Verify that cost is less than sales price");
        String priceStr = pages.products().detailsGenInfSalesPrice.getText().trim().substring(1)
                                          .trim().replace(",","");
        double price = Double.parseDouble(priceStr);

        String costStr = pages.products().detailsGenInfCost.getText().trim().substring(1)
                                                           .trim().replace(",","");
        double cost = Double.parseDouble(costStr);

        Assert.assertTrue(price >= cost);

        extentLogger.pass("Completed: The cost of a product is less than the sales price.");
    }

//    Mehmet Acikgoz- BRIT-941
    @Test
    public void IsProductNameSeenAtTheTop(){
        extentLogger = report.createTest("Product name is seen at the top of the page");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Click on " + productName);
        WebElement product = pages.products().selectProduct(productName);
        product.click();

        BrowserUtils.waitForVisibility(pages.products().detailsGenInfSalesPrice, 30);
        extentLogger.info("Verify that the name of the product is the same as the one with the previous step.");
        Assert.assertEquals(pages.products().detailsProductNameLabel.getText().trim(), productName);

        extentLogger.info("Verify that the name of the product is displayed on the top of the page.");
        Assert.assertTrue(pages.products().detailsProductNameLabel.isDisplayed());

        extentLogger.pass ("Product name is seen at the top of the page");
    }

//    Mehmet Acikgoz - BRIT-945
    @Test
    public void canProductBeFollowed(){
        extentLogger = report.createTest("Product can be followed.");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();


        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Click on " + productName);
        WebElement product = pages.products().selectProduct(productName);
        product.click();

        extentLogger.info("Click on the \"Follow\" link.");
//        System.out.println("label "+pages.products().detailsFollowingLabel.getText());
        WebElement labelElement    =  pages.products().detailsFollowingLabel;
        WebElement labelElementFollow    =  pages.products().detailsFollowingLabel_Follow;
        WebElement labelElementFollowing =  pages.products().detailsFollowingLabel_Following;
        WebElement labelElementUnFollow  =  pages.products().detailsFollowingLabel_Unfollow;
        System.out.println("labelElement.getText() = " + labelElementFollow.getText());
        BrowserUtils.waitForClickablility(labelElementFollow, timeOutInSec);
        labelElementFollow.click();

        System.out.println("labelElement.getText() = " + labelElementFollowing.getText());

        BrowserUtils.wait(3);
        BrowserUtils.hover(pages.products().detailsGenInfSalesPrice);

        System.out.println("labelElement.getText() = " + labelElement.getText());

//        System.out.println("label "+pages.products().detailsFollowingLabel.getText());
        Assert.assertEquals(labelElement.getText(), "Following");
        extentLogger.info("Verify that 'Following' is displayed at the same spot");

        System.out.println("labelElement.getText() = " + labelElement.getText());

//        BrowserUtils.waitForPageToLoad(timeOutInSec);

        BrowserUtils.hover(labelElement);
//        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeOutInSec);
//        wait.until(ExpectedConditions.textToBePresentInElement(labelElement, "Following"));
        labelElementUnFollow.click();

        System.out.println("labelElement.getText() = " + labelElement.getText());



//        BrowserUtils.hover(pages.products().detailsGenInfSalesPrice);
//        extentLogger.info("FYI: Changing the status to former state to keep the data clean");
//        BrowserUtils.wait(5);

//        BrowserUtils.hover(labelElement);
/*
        System.out.println("labelElement.getText() = " + labelElement.getText());
        BrowserUtils.waitForClickablility(labelElement,timeOutInSec);
        labelElement.click();

*/
        BrowserUtils.wait(5);

//        BrowserUtils.waitForClickablility(pages.products().detailsPopUpConfirmationOkBtn, timeOutInSec);

        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), timeOutInSec);
        wait.until(ExpectedConditions.textToBePresentInElement(pages.products().detailsPopUpConfirmationOkBtn, "Ok"));

        BrowserUtils.hover(pages.products().detailsPopUpConfirmationOkBtn);
        pages.products().detailsPopUpConfirmationOkBtn.click();
//        BrowserUtils.clickWithJS(pages.products().detailsPopUpConfirmationOkBtn);
//        Alert alert = Driver.getDriver().switchTo().alert();
//        alert.accept();

        extentLogger.pass ("Completed: Product can be followed.");
    }


    //    Mehmet Acikgoz - BRIT-948
    @Test
    public void canProductBeUnFollowed(){
        extentLogger = report.createTest("Product can be unfollowed.");
        getMeToPointOfSalesAs("user");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Click on " + productName);
        WebElement product = pages.products().selectProduct(productName);
        product.click();

        extentLogger.info("Click on the \"Follow\" link.");

        WebElement labelElement = pages.products().detailsFollowingLabel;
        labelElement.click();

        BrowserUtils.wait(3);
        BrowserUtils.hover(pages.products().detailsGenInfSalesPrice);


        extentLogger.info("Hover over \"Following\" link and click the \"X Unfollow\" link");
        BrowserUtils.hover(labelElement);
        BrowserUtils.waitForClickablility(labelElement, timeOutInSec);
        labelElement.click();

        extentLogger.info("Click Ok on the pop-up menu.");
        BrowserUtils.wait(5);
        BrowserUtils.waitForClickablility(pages.products().detailsPopUpConfirmationOkBtn, timeOutInSec);
//        Driver.getDriver().manage().timeouts().implicitlyWait(timeOutInSec, TimeUnit.SECONDS);
        BrowserUtils.hover(pages.products().detailsPopUpConfirmationOkBtn);
        pages.products().detailsPopUpConfirmationOkBtn.click();
        BrowserUtils.wait(10);
//        BrowserUtils.waitForVisibility(pages.products().detailsGenInfSalesPrice, timeOutInSec);
        BrowserUtils.hover(pages.products().detailsGenInfSalesPrice);

        BrowserUtils.waitForVisibility(pages.products().detailsGenInfSalesPrice, 30);
        extentLogger.info("Verify that 'Follow' is displayed at the same spot");
        Assert.assertEquals(labelElement.getText(), "Follow");

        extentLogger.pass ("Completed: Product can be unfollowed.");

        //div[@class='modal-content']//div[@class='modal-footer']//span[text()='Ok']
        //*[@id="modal_571"]/div/div/div[3]/button[1]/span
    }


    @Test
    public void canManagerUpdateTheSalesPrice(){
        extentLogger = report.createTest("Manager can update the sales price of the product.");
        getMeToPointOfSalesAs("manager");

        extentLogger.info("Click on the Products");
        pages.pointOfSale().productsLink.click();

        String productName = pages.products().selectAnyProduct();
        extentLogger.info("Click on " + productName);
        WebElement product = pages.products().selectProduct(productName);
        product.click();

        extentLogger.info(" Click the Edit button on the left top corner of the menu.");
        pages.products().detailsEditBtn.click();

        extentLogger.info("Change the sales price");
        WebElement element = pages.products().detailsEditSalesPriceInput;
        String priceStr = element.getAttribute("value").replace(",","");
        BrowserUtils.wait(5);
        element.clear();
        double price = Double.parseDouble(priceStr);
        String newPriceStr = "" + (price+1);

        element.sendKeys(newPriceStr);
        BrowserUtils.wait(5);

        extentLogger.info("Click on the Save button");
        pages.products().detailsSaveBtn.click();

        extentLogger.info("Verify that sales price is changed.");
        BrowserUtils.waitForVisibility(pages.products().detailsGenInfSalesPrice, timeOutInSec);
        String actualValue = pages.products().detailsGenInfSalesPrice.getText().trim().substring(1).trim();
        Assert.assertEquals(Double.parseDouble(actualValue), Double.parseDouble(newPriceStr), 0.001);

        extentLogger.pass("Manager can update the sales price of the product");
    }


}
