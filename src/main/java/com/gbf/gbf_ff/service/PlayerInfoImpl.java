package com.gbf.gbf_ff.service;

import com.gbf.gbf_ff.Exception.DuplicatedUserException;
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
import java.time.LocalDate;
import java.util.*;

@Service
//@ComponentScan
public class PlayerInfoImpl implements PlayerInfo {

	private HashMap<String, String[]> saveDate;
	private HashMap<String, String> twitterMessage;

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

		saveDate = new HashMap<>();
		twitterMessage = new HashMap<>();

		twitterID = userinfo.getTwitterID();
		twitterPW = userinfo.getTwitterPW();

		initChromeDriver();
	}


	private void initChromeDriver() throws Exception{
		//setup selenium
		System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 20);

		Thread.sleep(1000);
		initTwitter();
	}

	private void initTwitter() throws Exception {
		twitterCookieTest(driver,wait);
		Thread.sleep(1000);
		gbfCookieTest(driver,wait);
	}

	@Override
	public void twitterCookieTest(WebDriver driver,WebDriverWait wait) throws InterruptedException, IOException, ParseException {

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

//		driver.get("https://twitter.com/");

	}

	@Override
	public void gbfCookieTest(WebDriver driver,WebDriverWait wait) throws InterruptedException, IOException, ParseException {
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

//		driver.get("http://game.granbluefantasy.jp/#profile");

	}

	@Override
	public synchronized PlayerDto resourceTest(String profileId) throws Exception {
		if (profileId == null || profileId.equals("")) return null;


		//remove duplicated message / once a day
		String today = LocalDate.now().toString();
		if(saveDate.containsKey(profileId) && today.equals(saveDate.get(profileId)[0])){
			saveDate.put(profileId, new String[]{today, "Yes"});
			throw new DuplicatedUserException();
		}

//		//parallel
//		WebDriver driver = new ChromeDriver();
//		WebDriverWait wait = new WebDriverWait(driver, 20);
//		Thread.sleep(1000);
//		initTwitter(driver,wait); // move this to make it parallel
//		Thread.sleep(1000);
//		System.out.println("Test Start : "+ profileId);

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
					StringBuffer sb = new StringBuffer();
					while(st.hasMoreTokens()){
						String next = st.nextToken();
						if("Omega".equals(next))continue;
						sb.append(next).append(" ");
					}
					sb.setLength(sb.length()-1);
					playerDto.setSummonName(sb.toString(), x, y);
				}
			}
		}
		WebElement favorite = driver.findElement(By.xpath("//*[@id=\"wrapper\"]/div[3]/div[2]/div[4]/div[7]/div[2]/div[1]/img"));

		playerDto.setFavorite(favorite.getAttribute("src"));

		//create twitter string / save it
		String profileMessage = createProfileString(playerDto);
		twitterMessage.put(profileId, profileMessage);
		playerDto.setProfileMessage(profileMessage);

		saveDate.put(profileId, new String[]{today, "No"});

		return playerDto;
	}

	private String createProfileString(PlayerDto playerDto){

		String[] summonElement = new String[]{"Free","Fire","Water","Earth","Wind","Light","Dark"};
		StringBuffer msg = new StringBuffer("ID:"+playerDto.getId()+"\n");
//				+ "Name:"+playerDto.getName() +"\n");

		for(int i=0;i<7; i++){
			int idx = (i+1)%7;
			String sumName = playerDto.getSummonName()[idx][0];
			msg.append(summonElement[idx]).append(":");
			if(sumName!=null){
				msg.append("Lv")
						.append(playerDto.getSummonLevel()[idx][0])
						.append(" ")
						.append(sumName)
						.append("/");
			}
			else{
				msg.append("No Summon/");
			}
			sumName = playerDto.getSummonName()[idx][1];
			if(sumName!=null){
				msg.append("Lv")
						.append(playerDto.getSummonLevel()[idx][1])
						.append(" ")
						.append(sumName)
						.append("\n");
			}
			else {
				msg.append("No Summon\n");
			}
		}

		if(msg.length()>279){
			msg.setLength(278);
			msg.append("..");
		}

		return msg.toString();
	}

	@Override
	public String[] getTwitterMessage(String code){
		return new String[] {twitterMessage.get(code), saveDate.get(code)[1]};
	}
}
