package service;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.jni.Time;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class PlayerInfoImpl {
	//singleton
	private static PlayerInfoImpl instance;
	private static WebDriver driver;
	private static WebDriverWait wait;
	
	private static String twitterID = "topgun0234+05@gmail.com";
	private static String twitterPW = "topgun9371!";
	
	private PlayerInfoImpl() {
		System.setProperty("webdriver.chrome.driver", "C:\\SSAFY\\gbf\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		
		wait = new WebDriverWait(driver, 20);
	}
	
	public static PlayerInfoImpl getInstance() {
		if(instance==null)instance=new PlayerInfoImpl();
		return instance;
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
		gbfTest();
	}
	
	public void gbfTest() {
		driver.get("https://connect.mobage.jp/login?post_login_redirect_uri=https%3A%2F%2Fconnect.mobage.jp%2Fconnect%2F1.0%2Fservices%2Fauthorize%3Fclient_id%3D12016007-2%26response_type%3Dcode%2520token%26scope%3Dopenid%2520common_api%26redirect_uri%3Dhttp%253A%252F%252Fgame.granbluefantasy.jp%252Fauthentication%26state%3Dmobage-connect_60897ed3d36446.25222421%26prompt%3Dconsent%26theme%3Ddefault%26client_origin_uri%3Dhttp%253A%252F%252Fgame.granbluefantasy.jp%26sdk_name%3Dmobage-jssdk%26sdk_version%3D3.7.9%26custom_theme%3Dgrbl%26appearance_version%3D1%26display%3Dpage&post_cancel_redirect_uri=https%3A%2F%2Fconnect.mobage.jp%2Fconnect%2F1.0%2Fclients%2F12016007-2%2Fjssdk%2Fredirect%3Fclient_origin_uri%3Dhttp%253A%252F%252Fgame.granbluefantasy.jp%26sdk_version%3D3.7.9%26sdk_name%3Dmobage-jssdk%23client_origin_uri%3Dhttp%253A%252F%252Fgame.granbluefantasy.jp%26error%3Daccess_denied%26error_description%3DAccess%2Bdenied%2Bby%2Bthe%2Bresource%2Bowner%2Bor%2Bthe%2Bauthorization%2Bserver%26sdk_name%3Dmobage-jssdk%26sdk_version%3D3.7.9%26state%3Dmobage-connect_60897ed3d36446.25222421%26topic%3Dauthorization_error_response&client_origin_uri=http%3A%2F%2Fgame.granbluefantasy.jp&client_id=12016007-2&sig=973ebfd8e7ccd74ccbb4aabe1f84f1d96f58f224&iat=1619623639&seed=y5cgK8hY&theme=default&display=page&custom_theme=grbl&appearance_version=1&sdk_name=mobage-jssdk");
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"mobage-connect-analytics\"]/div[1]/ul/li[2]/a")));
		
		WebElement login=driver.findElement(By.xpath("//*[@id=\"mobage-connect-analytics\"]/div[1]/ul/li[2]/a"));
		login.click();
		//여기서부턴 트위터 연동
		
		driver.get("http://game.granbluefantasy.jp/#authentication");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"mobage-login\"]")));
		login = driver.findElement(By.xpath("//*[@id=\"mobage-login\"]"));
		
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", login);
		//login.click();
		//*[@id="notify-response-button"]
		
		String currentTab = driver.getWindowHandle();
		System.out.println(currentTab);
		for (String nextTab : driver.getWindowHandles()) {
			if(!nextTab.equals(currentTab)) {
				System.out.println(nextTab);
				driver.switchTo().window(nextTab);
				//로그인 종료
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/*[@id=\"notify-response-button\"]")));
				login = driver.findElement(By.xpath("//*[@id=\"notify-response-button\"]"));
				((JavascriptExecutor)driver).executeScript("arguments[0].click();", login);
				
			}
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
