package amazonTestPackage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmazonTest {
	
	public WebDriver driver;
	public String baseURL="http://www.amazon.com";
	public String expectedURL="https://www.amazon.com/";
	public String keyword="samsung";
	public String productCode; //the "asin" value for selected product (3rd product on Page2)
	
	@BeforeClass
	public void setUp() {
		//launching chrome browser
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	
	@Test (priority=0)
	public void openAmazonWebPage() {
		
		driver.navigate().to(baseURL);
		driver.manage().window().maximize();
		
		//verify the web page
		try{
			Assert.assertEquals(expectedURL, driver.getCurrentUrl());
			System.out.println("Navigated to correct webpage");
		}
		catch(Throwable pageNavigationError){
			System.out.println("Did not navigate to correct webpage");
		}

	}
	@Test (priority=1)
	public void login() {
		
		//user account info
		final String USERNAME = "seleniumtest058";
		final String EMAIL = "seleniumtest058@gmail.com";
		final String PASSWORD = "insider123";

		driver.findElement(By.xpath("//*[@id=\"nav-link-accountList\"]/span[1]")).click();
		
		driver.findElement(By.cssSelector("#ap_email")).sendKeys(EMAIL);
		driver.findElement(By.xpath("//*[@id=\"continue\"]")).click();
		driver.findElement(By.cssSelector("#ap_password")).sendKeys(PASSWORD);
		
		driver.findElement(By.xpath("//*[@id=\"signInSubmit\"]")).click();
		
	}
	
	@Test (priority=2)
	public void search() {
		
		driver.findElement(By.cssSelector("#twotabsearchtextbox")).sendKeys(keyword);
		driver.findElement(By.xpath("//*[@id=\"nav-search\"]/form/div[2]/div/input")).click();

	}
	
	@Test (priority=3)
	public void verifySearchResult() {
		
		try{
			driver.findElement(By.cssSelector("#quartsPagelet")).getText();
			System.out.println("Showing most relevant results for samsung.");
		}
		catch(NoSuchElementException e){
			System.out.println("No results for keyword "+keyword+" is found.");
		}

	}
	
	@Test (priority=4)
	public void openPage2AndVerify() {
		driver.findElement(By.xpath("//*[@id=\"pagn\"]/span[3]/a")).click();
		if(driver.findElements(By.cssSelector("#pagn > span.pagnCur")).equals(null)) {
			System.out.println("fail");
		}
		else {
			System.out.println("Page is loaded.");
		}
	}
	
	@Test (priority=5)
	public void addToListThirdProduct() {
		productCode=driver.findElement(By.xpath("//*[@id=\"result_18\"]")).getAttribute("data-asin");
		//System.out.println(productCode);
		
		driver.findElement(By.xpath("//*[@id=\"result_18\"]/div/div/div/div[2]/div[1]/div[1]/a/h2")).click();
		driver.findElement(By.xpath("//*[@id=\"add-to-wishlist-button-submit\"]")).click();
	
		driver.navigate().refresh();
	
	}

	
	@Test (priority=6)
	public void goToWishList() {
		
		WebElement accountsAndLists = driver.findElement(By.xpath("//*[@id=\"nav-link-accountList\"]"));
		By wishList = By.xpath("//*[@id=\"nav-flyout-wl-items\"]/div/a[1]/span");
		Actions action = new Actions(driver); 
		action.moveToElement(accountsAndLists).clickAndHold().build().perform();
	
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(wishList));
		driver.findElement(wishList).click();	
	}
	
	@Test (priority=7)
	
	public void verifyItemIsAddedToList() {
		List<WebElement> allElements = driver.findElements(By.xpath("//div[@class='a-section a-spacing-none']/ul[@id='g-items']/li")); 
		String productParams;
		String asin="";
		Boolean boo=true;
		int s=allElements.size();
		
		for(int i=0;i<s;i++){
		    allElements = driver.findElements(By.xpath("//div[@class='a-section a-spacing-none']/ul[@id='g-items']/li"));
		    productParams=allElements.get(i).getAttribute("data-reposition-action-params").toString();
		    asin=productParams.substring(24, 34);
		    
		    if(asin.equals(productCode)) {
		    		System.out.println("\nSucces Msg: The selected item has been placed in the wish list.");
		    		boo=false;
		    		break;
		    }
		}
		
		if(boo) {
			System.out.println("\nFail Msg: The selected item could NOT been placed in the wish list!");
			System.out.println("productCode= "+ productCode + " asin= "+ asin);
		}
	}
	
	@Test (priority=8)
	
	public void removeFromFavorites() {
		List<WebElement> allElements = driver.findElements(By.xpath("//div[@class='a-section a-spacing-none']/ul[@id='g-items']/li")); 
		String productParams;
		String asin="";
		Boolean boo=true;
		int s=allElements.size();
		
		for(int i=0;i<s;i++){
		    allElements = driver.findElements(By.xpath("//div[@class='a-section a-spacing-none']/ul[@id='g-items']/li"));
		    productParams=allElements.get(i).getAttribute("data-reposition-action-params").toString();
		    asin=productParams.substring(24, 34);
		    
		    if(asin.equals(productCode)) {
		    		WebElement element = allElements.get(i);
		    		driver.findElement(By.xpath("//*[@id=\"a-autoid-5\"]/span/input")).click();
		    		System.out.println("Item with productCode="+asin+" is deleted.");
		    		boo=false;
		    }    	
		}
		if(boo){
	    		System.out.println("There is already no item with productCode="+asin+".");
	    }
	
	}
	
	@Test (priority=9)
	
	public void verifyDeletion() {
		List<WebElement> allElements = driver.findElements(By.xpath("//div[@class='a-section a-spacing-none']/ul[@id='g-items']/li")); 
		String productParams;
		String asin="";
		Boolean boo=true;
		int s=allElements.size();
		
		for(int i=0;i<s;i++){
		    allElements = driver.findElements(By.xpath("//div[@class='a-section a-spacing-none']/ul[@id='g-items']/li"));
		    productParams=allElements.get(i).getAttribute("data-reposition-action-params").toString();
		    asin=productParams.substring(24, 34);
		    
		    if(asin.equals(productCode)) {
		    		System.out.println("\nFail Msg: The selected item had NOT been deleted and still exists in the list.");
		    		boo=false;
		    		break;
		    }
		}
		
		if(boo) {
			System.out.println("\nSucces Msg: The selected item has been deleted successfully!");
		}
	}
	@AfterClass
	public void tearDown() {
		if(driver!=null) {
			System.out.println("\nClosing chrome browser\n\nTest results:");
			System.out.println();
			driver.quit();
		}
	}
	
}