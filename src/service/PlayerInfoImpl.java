package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.tomcat.jni.Time;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.org.apache.xml.internal.serializer.utils.StringToIntTable;

import userinfo.UserInfo;


public class PlayerInfoImpl {
	//singleton
	private static PlayerInfoImpl instance;
	private static WebDriver driver;
	private static WebDriverWait wait;
	
	private static String twitterID = UserInfo.getInstance().getTwitterID();
	private static String twitterPW = UserInfo.getInstance().getTwitterPW();
	
	
	private PlayerInfoImpl() throws IOException {
		File f = new File(".");
		System.out.println(f.getAbsolutePath());
		System.setProperty("webdriver.chrome.driver", "WebContent/WEB-INF/chromedriver_win32/chromedriver.exe");
		
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 20);
	}
	
	public static PlayerInfoImpl getInstance() throws IOException {
		if(instance==null)instance=new PlayerInfoImpl();
		return instance;
	}
	
	public void twitterCookieTest() throws InterruptedException, IOException, ParseException {

		driver.get("https://twitter.com/");
		
		File file = new File("Twitter.data");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String readLine;
		while((readLine = br.readLine())!=null) {
			StringTokenizer st = new StringTokenizer(readLine,";");
			String name = st.nextToken();
			String value = st.nextToken();
			String domain = st.nextToken();
			String path = st.nextToken();
			Date expiry = null;
			
			String val;
			if(!(val = st.nextToken()).equals("null")){
				System.out.println(val);
				SimpleDateFormat format =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				expiry = format.parse(val);
			}
			Boolean isSecure = new Boolean(st.nextToken()).booleanValue();
			Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
			System.out.println(cookie);
			driver.manage().addCookie(cookie);
		}
		br.close();
		
		
		driver.get("https://twitter.com/");
		
		gbfCookieTest();
//		gbfTest();
	}
	
	public void gbfCookieTest() throws InterruptedException, IOException, ParseException {
		driver.get("https://connect.mobage.jp/");
		
		File file = new File("mobage.data");
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String readLine;
		while((readLine = br.readLine())!=null) {
			StringTokenizer st = new StringTokenizer(readLine,";");
			String name = st.nextToken();
			String value = st.nextToken();
			String domain = st.nextToken();
			String path = st.nextToken();
			Date expiry = null;
			
			String val;
			if(!(val = st.nextToken()).equals("null")){
				System.out.println(val);
				SimpleDateFormat format =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				expiry = format.parse(val);
			}
			Boolean isSecure = new Boolean(st.nextToken()).booleanValue();
			Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
			System.out.println(cookie);
			driver.manage().addCookie(cookie);
		}
		br.close();
		

		driver.get("http://game.granbluefantasy.jp/#authentication");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mobage-login\"]")));
		WebElement login = driver.findElement(By.xpath("//*[@id=\"mobage-login\"]"));
		
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", login);
		
		Thread.sleep(500);
		
		String currentTab = driver.getWindowHandle();
		Iterator<String> it = driver.getWindowHandles().iterator();
		
		for (;it.hasNext();) {
			String nextTab = it.next();
			if(!nextTab.equals(currentTab)) {
				driver.switchTo().window(nextTab);
				
				
				login = driver.findElement(By.xpath("//*[@id=\"notify-response-button\"]"));
				((JavascriptExecutor)driver).executeScript("arguments[0].click();", login);
				
			}
		}
		driver.switchTo().window(currentTab);
	}
	
	
	
	
	public void twitterTest() throws InterruptedException, IOException {
		driver.get("https://twitter.com/");
		//id
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div[1]/div/form/div/div[3]/div")));
		WebElement id = driver.findElement(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div[1]/div/form/div/div[1]/div/label/div/div[2]/div/input"));
		id.clear();
		id.sendKeys(twitterID);
		//pw
		WebElement pw = driver.findElement(By.name("session[password]"));
		pw.clear();
		pw.sendKeys(twitterPW);
		
		Thread.sleep(1000);
		//login
		WebElement login = driver.findElement(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div[1]/div/form/div/div[3]/div"));
		login.click();
		Thread.sleep(500);
		
		
		File file = new File("Twitter.data");
		try {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(Cookie cookie : driver.manage().getCookies()) {
				bw.write((cookie.getName() + ";" + cookie.getValue() + ";"
			+cookie.getDomain() + ";" + cookie.getPath() + ";"
			+ cookie.getExpiry() + ";" + cookie.isSecure()));
				
				bw.newLine();
			}
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
//		gbfTest();
	}
	
	public void gbfTest() throws InterruptedException {
		driver.get("https://connect.mobage.jp/login");
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"mobage-connect-analytics\"]/div[1]/ul/li[2]/a")));
		
		WebElement login=driver.findElement(By.xpath("//*[@id=\"mobage-connect-analytics\"]/div[1]/ul/li[2]/a"));
		login.click();
		
		driver.get("http://game.granbluefantasy.jp/#authentication");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mobage-login\"]")));
		login = driver.findElement(By.xpath("//*[@id=\"mobage-login\"]"));
		
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", login);
		
		Thread.sleep(500);
		
		String currentTab = driver.getWindowHandle();
		Iterator<String> it = driver.getWindowHandles().iterator();
		
		for (;it.hasNext();) {
			String nextTab = it.next();
			if(!nextTab.equals(currentTab)) {
				driver.switchTo().window(nextTab);
				

				File file = new File("mobage.data");
				try {
					file.createNewFile();
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					for(Cookie cookie : driver.manage().getCookies()) {
						bw.write((cookie.getName() + ";" + cookie.getValue() + ";"
					+cookie.getDomain() + ";" + cookie.getPath() + ";"
					+ cookie.getExpiry() + ";" + cookie.isSecure()));
						
						bw.newLine();
					}
					bw.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				login = driver.findElement(By.xpath("//*[@id=\"notify-response-button\"]"));
				((JavascriptExecutor)driver).executeScript("arguments[0].click();", login);
				
			}
		}
		driver.switchTo().window(currentTab);
		//로그인 성공!!
		
//		driver.get("http://game.granbluefantasy.jp/#mypage");
		driver.get("https://www.mbga.jp/");
		Thread.sleep(5000);
		
		File file = new File("mbga.data");
		try {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(Cookie cookie : driver.manage().getCookies()) {
				bw.write((cookie.getName() + ";" + cookie.getValue() + ";"
			+cookie.getDomain() + ";" + cookie.getPath() + ";"
			+ cookie.getExpiry() + ";" + cookie.isSecure()));
				
				bw.newLine();
			}
			bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
//		driver.get("http://game.granbluefantasy.jp/#profile");
//
//		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[3]/span")));
//		WebElement ranks = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[3]/span"));
//		System.out.println(ranks);
	}
	
	public void test() throws IOException  {
		
		//내 자신의 code를 얻는 테스트
//		Document doc = Jsoup.connect("http://game.granbluefantasy.jp/#profile").get();
		driver.get("http://game.granbluefantasy.jp/#profile");
		
		List<WebElement>elements = null;
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("txt-group1")));
		
		System.out.println(elements.get(0).getAttribute("class"));
		
//		Element id = doc.select(".txt-group1").first();
		
//		System.out.println(id.attr("class"));
		
//		Elements ranks= doc.getElementsByClass("prt-rank-value");
//		Element r = doc.select("div.prt-rank-value").first();
//		
//		if(r!=null) {
//			String rankString = r.text();
//			System.out.println("rank : "+rankString);
//			
//		}else {
//			System.out.println("trytry");
//			
//		}
		
		
	}
	
	
	
}
