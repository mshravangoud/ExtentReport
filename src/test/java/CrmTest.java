import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class CrmTest {
	
	WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;
	
	@BeforeTest
	public void setExtent(){
		extent=new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentReport.html", true);
		extent.addSystemInfo("Host Name", "shravan-PC");
		extent.addSystemInfo("User Name", "shravan");
		extent.addSystemInfo("OS", "Windows 7");
		
	}
	
	@AfterTest
	public void endReport(){
		extent.flush();
		extent.close();
		
	}
	public static String getScreenshot(WebDriver driver,String screenshotName) throws IOException{
		String dateName=new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
	TakesScreenshot ts= (TakesScreenshot) driver;
	File source=ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir")+"/FailedTestScreenshots/" +screenshotName +dateName+".png";
		File finalDestination=new File(destination);
		FileUtils.copyFile(source,finalDestination);
		return destination;
		
		
	}
	
	@BeforeMethod
	public void setUp()
	{
		System.setProperty("webdriver.chrome.driver", "L:\\eclipse\\java space\\chromedriver.exe");
     driver=new ChromeDriver();
	driver.get("https://classic.crmpro.com/index.html");
	driver.manage().window().maximize();
	driver.manage().deleteAllCookies();
	driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	@Test
	public void   TitleTest(){
		
		extentTest=extent.startTest("TitleTest");
		
		String title=driver.getTitle();
		
		System.out.println(title);

		Assert.assertEquals(title,"CRMPRO - CRM software for customer relationship management, sales,");// and support.
	}
	
	@Test
	public void crmLogoTest(){
		extentTest=extent.startTest("crmLogoTest");
		boolean b= driver.findElement(By.xpath("//img[@class='img-responsive111']")).isDisplayed();
		Assert.assertTrue(b);
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) throws IOException { //throws InterruptedException{
		//Thread.sleep(2000);
		if(result.getStatus()==ITestResult.FAILURE){
			
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS"+result.getName());//to add name in extent report
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS"+result.getThrowable());//to add error description to ER
			
			String screenshotPath=CrmTest.getScreenshot(driver, result.getName());
			//extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath));//add screenshot in extent report
			extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));
			
			
		}else if(result.getStatus()==ITestResult.SKIP){
			
			extentTest.log(LogStatus.SKIP, "TEST CASE SKIPPED IS"+result.getName());
					
		}else if((result.getStatus()== ITestResult.SUCCESS)) {
			
			extentTest.log(LogStatus.PASS, "TEST CASE PASSED IS"+result.getName());
			
		}
		extent.endTest(extentTest);//ending test and ends the current test and prepare to create html report
		
		driver.quit();
	}
}
