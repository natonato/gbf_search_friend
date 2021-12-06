package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.config.UserInfo;
import com.gbf.gbf_ff.dto.PlayerDto;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
//@ComponentScan
public class PlayerInfoImpl implements PlayerInfo {

	private final UserInfo userinfo;

	private final GBFResource gbfResource;

	private String twitterID;
	private String twitterPW;

	//singleton
	WebDriver driver;
	WebDriverWait wait;

	@Autowired
	public PlayerInfoImpl(UserInfo userinfo, GBFResource gbfResource) throws Exception {
		this.userinfo = userinfo;
		this.gbfResource = gbfResource;

		twitterID = userinfo.getTwitterID();
		twitterPW = userinfo.getTwitterPW();

		initChromeDriver(); // move this to make it parallel
		Thread.sleep(1000);
		initTwitter();
	}

	private void initChromeDriver() {
		//setup selenium
		System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 20);
	}
	private void initTwitter() throws Exception {
		twitterCookieTest();
		gbfCookieTest();
	}

	@Override
	public void twitterCookieTest() throws InterruptedException, IOException, ParseException {

		driver.get("https://twitter.com/");

		File file = new File("src/main/resources/static/cookie/Twitter.data");
		BufferedReader br = new BufferedReader(new FileReader(file));

		String readLine;
		while ((readLine = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(readLine, ";");
			String name = st.nextToken();
			String value = st.nextToken();
			String domain = st.nextToken();
			String path = st.nextToken();
			Date expiry = null;

			String val;
			if (!(val = st.nextToken()).equals("null")) {
				SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				expiry = format.parse(val);
			}
			Boolean isSecure = new Boolean(st.nextToken()).booleanValue();
			Cookie cookie = new Cookie(name, value, domain, path, expiry, isSecure);
			driver.manage().addCookie(cookie);
		}
		br.close();

		driver.get("https://twitter.com/");

	}

	@Override
	public void gbfCookieTest() throws InterruptedException, IOException, ParseException {
		driver.get("https://connect.mobage.jp/");

		File file = new File("src/main/resources/static/cookie/mobage.data");
		BufferedReader br = new BufferedReader(new FileReader(file));

		String readLine;
		while ((readLine = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(readLine, ";");
			String name = st.nextToken();
			String value = st.nextToken();
			String domain = st.nextToken();
			String path = st.nextToken();
			Date expiry = null;

			String val;
			if (!(val = st.nextToken()).equals("null")) {
				SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
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

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", login);

		Thread.sleep(500);

		String currentTab = driver.getWindowHandle();
		Iterator<String> it = driver.getWindowHandles().iterator();

		for (; it.hasNext(); ) {
			String nextTab = it.next();
			if (!nextTab.equals(currentTab)) {
				driver.switchTo().window(nextTab);
				login = driver.findElement(By.xpath("//*[@id=\"notify-response-button\"]"));
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", login);

			}
		}
		driver.switchTo().window(currentTab);

		Thread.sleep(500);

		driver.get("http://game.granbluefantasy.jp/#profile");

	}

	@Override
	public PlayerDto resourceTest(String profileId) throws IOException {
		if (profileId == null || profileId.equals("")) return null;

		PlayerDto playerDto = new PlayerDto();

		playerDto.setId(profileId);
		driver.get("http://game.granbluefantasy.jp/#profile/" + profileId);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[3]")));

		WebElement name = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[1]/div[2]/span"));
		playerDto.setName(name.getAttribute("innerHTML"));

		WebElement ranks = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[1]/div[1]/div[2]"));
		StringTokenizer st = new StringTokenizer(ranks.getAttribute("innerHTML"));
		String rank = "";
		while (st.hasMoreTokens()) rank = st.nextToken();
		playerDto.setRank(rank);

		List<WebElement> summon = driver.findElements(By.className("img-fix-summon"));
		List<WebElement> summonLevel = driver.findElements(By.cssSelector(".prt-fix-spec div:first-child"));

		for (int x = 0; x < 7; x++) {

			for (int y = 0; y < 2; y++) {
				st = new StringTokenizer(summonLevel.get(x * 2 + y).getAttribute("innerHTML"));
				String no = st.nextToken();//throw `lvl` text

				playerDto.setSummon(summon.get(x * 2 + y).getAttribute("src"), x, y);

				if (no.equals("No")) {
					playerDto.setSummonLevel(0, x, y);
					playerDto.setSummonName(null, x, y);
				} else {
					playerDto.setSummonLevel(Integer.parseInt(st.nextToken()), x, y);
					playerDto.setSummonName(st.nextToken(), x, y);
				}
			}
		}
		WebElement favorite = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[4]/div[7]/div[2]/div[1]/img"));

		playerDto.setFavorite(favorite.getAttribute("src"));

		return playerDto;
	}
}