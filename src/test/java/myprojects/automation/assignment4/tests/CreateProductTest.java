package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    @DataProvider(name = "products")
    public static Object[][] getProducts() {
        return new Object[][]{
                {ProductData.generate()}
        };
    }

    @BeforeTest
    @Parameters({"login", "password"})
    public void login(String login, String password) {
        actions.login(login, password);
    }

    @Test(dataProvider = "products")
    public void createNewProduct(ProductData productData) {
        actions.createProduct(productData);

        driver.get(Properties.getBaseUrl());
        WebElement allProductsButton = driver.findElement(By.className("all-product-link"));
        allProductsButton.click();
        CustomReporter.logAction("Open the list of products");

        WebElement product = findProductInPaginationList(productData);
        Assert.assertNotNull(product);
        product.click();
        CustomReporter.logAction("Open information product window");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("add-to-cart")));

        WebElement productName = driver.findElement(By.cssSelector("h1[itemprop=\"name\"]"));
        Assert.assertEquals(productName.getText(), productData.getName().toUpperCase());
        CustomReporter.logAction("Check product name");

        WebElement productPrice = driver.findElement(By.cssSelector(".current-price span"));
        Assert.assertEquals(productPrice.getText(), productData.getPrice() + " ₴");
        CustomReporter.logAction("Check product quantity");

        WebElement productQty = driver.findElement(By.cssSelector(".product-quantities span"));
        Assert.assertEquals(productQty.getText(), productData.getQty() + " Товары");
        CustomReporter.logAction("Check product price");
    }

    private WebElement findProductInPaginationList(ProductData productData) {
        WebElement product = actions.findElementSafe(By.linkText(productData.getName()));
        if (product != null) {
            return product;
        }

        WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("next")));
        if (nextButton.getAttribute("class").contains("disabled")) {
            return null;
        }

        nextButton.click();
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(nextButton)));
        CustomReporter.logAction("Search the product on the next page ");
        return findProductInPaginationList(productData);
    }

}
