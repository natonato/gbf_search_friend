package com.gbf.gbf_ff_1030.service;

import com.gbf.gbf_ff_1030.config.UserInfo;
import com.gbf.gbf_ff_1030.dto.PlayerDto;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@ComponentScan
public class PlayerInfoImpl implements PlayerInfo{

	private final UserInfo userinfo;

	private final GBFResource gbfResource;

	private String twitterID;
	private String twitterPW;

	//singleton
	WebDriver driver;
	WebDriverWait wait;
	
	@Autowired
	public PlayerInfoImpl(UserInfo userinfo, GBFResource gbfResource) throws IOException {
		this.userinfo = userinfo;
		this.gbfResource = gbfResource;
		twitterID = userinfo.getTwitterID();
		twitterPW = userinfo.getTwitterPW();
		System.out.println("ON!");
		//setup selenium
		System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 20);
	}

	@Override
	public void twitterCookieTest() throws InterruptedException, IOException, ParseException {

		driver.get("https://twitter.com/");
		
		File file = new File("src/main/resources/static/cookie/Twitter.data");
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
				SimpleDateFormat format =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				expiry = format.parse(val);
			}
			Boolean isSecure = new Boolean(st.nextToken()).booleanValue();
			Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
			driver.manage().addCookie(cookie);
		}
		br.close();
		
		driver.get("https://twitter.com/");
		
		gbfCookieTest();
	}

	@Override
	public void gbfCookieTest() throws InterruptedException, IOException, ParseException {
		driver.get("https://connect.mobage.jp/");
		
		File file = new File("src/main/resources/static/cookie/mobage.data");
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
				SimpleDateFormat format =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				expiry = format.parse(val);
			}
			Boolean isSecure = new Boolean(st.nextToken()).booleanValue();
			Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
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

		Thread.sleep(500);

		driver.get("http://game.granbluefantasy.jp/#profile");
		
	}

	@Override
	public PlayerDto resourceTest(String profileId, String message, int imgType) throws  IOException {
		if(profileId==null || profileId.equals(""))return null;
		
		PlayerDto playerDto=new PlayerDto();

		playerDto.setId(profileId);
		driver.get("http://game.granbluefantasy.jp/#profile/"+profileId);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[3]")));
		
		WebElement name = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[1]/div[2]/span"));
		playerDto.setName(name.getAttribute("innerHTML"));
		
		WebElement ranks = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[1]/div[2]"));
		StringTokenizer st = new StringTokenizer(ranks.getAttribute("innerHTML"));
		String rank="";
		while(st.hasMoreTokens())rank=st.nextToken();
		playerDto.setRank(rank);
		
//		WebElement bgimg = driver.findElement(By.className("img-pc"));
//		playerDto.setBgimg(bgimg.getAttribute("src"));
		
		List<WebElement> summon = driver.findElements(By.className("img-fix-summon"));
		List<WebElement> summonLevel = driver.findElements(By.cssSelector(".prt-fix-spec div:first-child"));
		System.out.println(summonLevel.size());

		for (int x = 0; x < 7; x++) {

			for (int y = 0; y < 2; y++) {
				st = new StringTokenizer(summonLevel.get(x*2+y).getAttribute("innerHTML"));
				String no = st.nextToken();//throw `lvl` text
				
				playerDto.setSummon(summon.get(x*2+y).getAttribute("src"), x, y);
				
				if(no.equals("No")) {
					playerDto.setSummonLevel(0, x, y);
					playerDto.setSummonName(null, x, y);
				}
				else {
					playerDto.setSummonLevel(Integer.parseInt(st.nextToken()), x, y);
					playerDto.setSummonName(st.nextToken(), x, y);
				}
			}
		}
		WebElement favorite = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[4]/div[7]/div[2]/div[1]/img"));

		playerDto.setFavorite(favorite.getAttribute("src"));

		gbfResource.makeProfileImg(playerDto, message, imgType);
		return playerDto;
	}


	@Override
	public void twitterTest() throws InterruptedException, IOException {
		driver.get("https://twitter.com/");
		//id
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div[1]/div/form/div/div[3]/div")));
		WebElement id = driver.findElement(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div[1]/div/form/div/div[1]/div/label/div/div[2]/div/input"));
		id.clear();	id.sendKeys(twitterID);
		//pw
		WebElement pw = driver.findElement(By.name("session[password]"));
		pw.clear();	pw.sendKeys(twitterPW);
		
		Thread.sleep(500);
		//click login
		WebElement login = driver.findElement(By.xpath("//*[@id=\"react-root\"]/div/div/div/main/div/div/div/div[1]/div[1]/div/form/div/div[3]/div"));
		login.click(); Thread.sleep(500);
		
		//save cache
		File file = new File("src/main/resources/static/cookie/Twitter.data");
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
		
		gbfTest();
	}

	@Override
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
				
				//save cache
				File file = new File("src/main/resources/static/cookie/mobage.data");
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

		
		driver.get("https://www.mbga.jp/");
		Thread.sleep(5000);
		
		//save cache
		File file = new File("src/main/resources/static/cookie/mbga.data");
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
		
	}
	
}
