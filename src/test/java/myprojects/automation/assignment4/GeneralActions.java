package myprojects.automation.assignment4;


import myprojects.automation.assignment4.model.ProductData;
import myprojects.automation.assignment4.utils.Properties;
import myprojects.automation.assignment4.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
        actions = new Actions(driver);
    }

    /**
     * Logs in to Admin Panel.
     *
     * @param login - user login
     * @param password - user password
     */
    public void login(String login, String password) {
        driver.get(Properties.getBaseAdminUrl());

        WebElement loginInput = driver.findElement(By.id("email"));
        loginInput.sendKeys(login);
        CustomReporter.logAction("Type email as a login");

        WebElement passwordInput = driver.findElement(By.id("passwd"));
        passwordInput.sendKeys(password);
        CustomReporter.logAction("Type password");

        WebElement submitButton = driver.findElement(By.name("submitLogin"));
        submitButton.click();
        CustomReporter.logAction("Submit login form");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("imgm")));
    }

    public void createProduct(ProductData newProduct) {
        WebElement catalog = driver.findElement(By.id("subtab-AdminCatalog"));
        WebElement product = driver.findElement(By.id("subtab-AdminProducts"));
        actions.moveToElement(catalog)
                .pause(1000)
                .click(product)
                .build()
                .perform();
        CustomReporter.logAction("Open products submenu");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-header-desc-configuration-add")));
        WebElement buttonNewProduct = driver.findElement(By.id("page-header-desc-configuration-add"));
        buttonNewProduct.click();

        WebElement productNameInput = driver.findElement(By.id("form_step1_name_1"));
        productNameInput.sendKeys(newProduct.getName());
        CustomReporter.logAction("Type name of new product");

        WebElement qtyProductInput = driver.findElement(By.id("form_step1_qty_0_shortcut"));
        qtyProductInput.clear();
        qtyProductInput.sendKeys(Integer.toString(newProduct.getQty()));
        CustomReporter.logAction("Type the product quantity");

        WebElement priceProductInput = driver.findElement(By.id("form_step1_price_shortcut"));
        priceProductInput.clear();
        priceProductInput.sendKeys(newProduct.getPrice());
        CustomReporter.logAction("Type price of new product");

        WebElement switchActive = driver.findElement(By.className("switch-input"));
        switchActive.click();
        CustomReporter.logAction("Activate the product by switch");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl")));
        WebElement closeSwitchAlert = driver.findElement(By.xpath("//*[@class='growl-close'][last()]"));
        closeSwitchAlert.click();

        WebElement saveButton = driver.findElement(By.className("js-btn-save"));
        saveButton.submit();
        CustomReporter.logAction("Save the product");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("growl")));
        WebElement closeSaveAlert = driver.findElement(By.xpath("//*[@class='growl-close'][last()]"));
        closeSaveAlert.click();
    }

    public WebElement findElementSafe(By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException exception) {
            return null;
        }
    }

}
