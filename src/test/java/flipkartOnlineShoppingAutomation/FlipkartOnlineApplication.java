package flipkartOnlineShoppingAutomation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartOnlineApplication {
	
	public static WebDriver driver;
	public String parentId;
	int firstprice,secondprice,totalprice;
	public static Properties prop;
	public void propertyFile()
	{
		File file = new File("C:\\Users\\2308823\\eclipse-workspace\\OnlineShoppingAutomation\\src\\test\\java\\flipkartOnlineShoppingAutomation\\TestData.properties");
		FileInputStream fileInput=null;
		try {
			 fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		prop = new Properties();
		
		//load properties file
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void createDriver() throws Exception
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter 1 for Chrome Browser and Enter 2 for Edge Browser");
		int a=sc.nextInt();
		if(a==1)
		{
			driver=new ChromeDriver();
		}
		
		else if(a==2)
		{
			driver=new EdgeDriver();
		}
		else {
			throw new Exception("Incorrect Browser");
			}
		
        driver.manage().window().maximize();//Maximizing the chrome browser
        propertyFile();
        driver.get(prop.getProperty("Url"));//Navigating to flipkart Website
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));//Implementing implicit wait
        System.out.println("Homepage of the flipkart loaded successfull");
        driver.findElement(By.name("q")).sendKeys(prop.getProperty("input"));//Entering home appliances in search box
		//Thread.sleep(1000);
		
        driver.findElement(By.cssSelector("button[type='submit']")).click();//clicking the search button
        //clicking the popularity button
        driver.findElement(By.xpath("//div[text()='Popularity']")).click();
        Thread.sleep(3000);
	}
	
	public void firstProduct() throws InterruptedException
	{
        //clicking the first product in the given page
        driver.findElement(By.className("CXW8mj")).click();
        //getting windowIds in set using windowhandler
        Set<String>windowId=driver.getWindowHandles();
        //storing windows id in list
		List<String> windowList=new ArrayList<String>(windowId);
		parentId=windowList.get(0);//getting parent window id in string 
		String childId=windowList.get(1);//getting child window id in string
		driver.switchTo().window(childId);//switching to child window using switching concept
        //Thread.sleep(3000); 
        JavascriptExecutor js=(JavascriptExecutor) driver;//creating javascriptExecutor object
        //finding add to cart button to click
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(20));
		WebElement ele=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[class='_2KpZ6l _2U9uOA _3v1-ww']")));
		//scrolling the page to find add to cart button
		js.executeScript("arguments[0].scrollIntoView();", ele);
		ele.click();//clicking the add to cart button
        //Thread.sleep(3000);

	}
	
	public void firstProductCost() throws InterruptedException
	{
		//storing first product price in string
        String cost=driver.findElement(By.xpath("//div[@class='Ob17DV _3X7Jj1']/div[2]/span")).getText();
		String price1=cost.replace("₹","").replace(",","");//Removing rupee symbol in price
	    firstprice=Integer.parseInt(price1);//converting string to integer
	    //printing price of first product in console
	    System.out.println("The cost of First product is:"+firstprice);
        driver.close();//closing the current window(child window)
        driver.switchTo().window(parentId);//switching to parent window
        //Thread.sleep(3000);
	}
	
	public void secondProduct() throws InterruptedException
	{
		//clicking one more product to add to cart
        driver.findElement(By.cssSelector("div[data-id='CPRFMGEZWPQDG5UM']")).click();
      //getting windowIds in set using windowhandler
        Set<String> windows1 = driver.getWindowHandles();
        //storing windows id in list
		List<String> windowList1=new ArrayList<String>(windows1);
		String subchildId1=windowList1.get(0);//getting parent window id in string
		String subchildId2=windowList1.get(1);//getting child window id
        driver.switchTo().window(subchildId2);//switching to child window
        //Thread.sleep(3000);
        JavascriptExecutor jse=(JavascriptExecutor) driver;
      //finding add to cart button to click
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(20));
        WebElement next=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='_2KpZ6l _2U9uOA _3v1-ww']")));
      //scrolling the page to find add to cart button
        jse.executeScript("arguments[0].scrollIntoView();", next);
        next.click();//clicking add to cart button
	}
	public void secondProductCost()
	{
		//getting price of second product and storing in variable
        String Price2=driver.findElement(By.xpath("//span[@class='_2-ut7f _1WpvJ7']")).getText();
        //Removing rupee symbol in price
        String sprice=Price2.replace("₹","").replace(",","");
        secondprice=Integer.parseInt(sprice);//converting string format to integer
        //printing second product in console
        System.out.println("The cost of second product is:"+secondprice);
	}
	public void totalCost()
	{
		//getting total price from cart
        String total=driver.findElement(By.xpath("//div[contains(@class,'Ob17DV _3X7Jj1')]")).getText();
        //Removing rupee symbol from total
        String validate=total.replace("₹","").replace(",","");
        totalprice=Integer.parseInt(validate);//converting string format to integer
        System.out.println("The Expected price is:"+totalprice);//printing expected price in console
	}
	public void validate()
	{
		int actual_price=firstprice+secondprice;//calculating actual price
        System.out.println("The actual price is:"+actual_price);//printing actual price
        
        if(actual_price==totalprice)//validating the calculated amount
        {
        	System.out.println("Amount is properly Calculated");

        }
        else
        {
        	System.out.println("Amount is properly not Calculated");

        }
	}
	
	public void excelData() throws IOException
	{
		FileOutputStream file=new FileOutputStream("C:\\Users\\2308823\\eclipse-workspace\\OnlineShoppingAutomation\\TestData\\outputs.xlsx");
		XSSFWorkbook workbook=new XSSFWorkbook();
		XSSFSheet sheet=workbook.createSheet();
		
		XSSFRow r=sheet.createRow(0);
		r.createCell(0).setCellValue("The first product price ");
		r.createCell(1).setCellValue(firstprice);
		XSSFRow r1=sheet.createRow(1);
		r1.createCell(0).setCellValue("The second product price ");
		r1.createCell(1).setCellValue(secondprice);
		XSSFRow r2=sheet.createRow(2);
		r2.createCell(0).setCellValue("The total price is");
		r2.createCell(1).setCellValue(totalprice);
		workbook.write(file);
		workbook.close();
		file.close();
			
	}
	public void closeBrowser()
	{
		driver.quit();
	}
	
	public static void main(String[] args) throws Exception {
		FlipkartOnlineApplication object=new FlipkartOnlineApplication();
		object.createDriver();
		object.firstProduct();
		object.firstProductCost();
		object.secondProduct();
		object.secondProductCost();
		object.totalCost();
		object.validate();
		object.excelData();
		object.closeBrowser();

	
		

	}

}
