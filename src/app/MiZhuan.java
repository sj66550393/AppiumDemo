package app;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;
import utils.DateUtils;
import utils.Log;
import utils.AdbUtils;
import utils.SwipeScreen;
import common.Configure;
import common.Contants;
import common.ResultDict;

public class MiZhuan {
	
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	private int redPackageNum = 0; // 拆红包 
	private int todayMustNum = 0; // 今日必看 
	private int entertainmentNews = 0; // 娱乐爆料 
	private int ThreeSixZeroNewsNum = 0; // 360新闻 
	private int HotNewsNum = 0; // 热点新闻 
	private int turnturnNum = 0; // 翻翻乐 
	private int tuituiNum = 0; // 推推乐 
	private int goldNewsNum = 0; // 点金头条 
	private int eighteenNum = 0; // 18头条
	private int loveNewsNum = 0; // 我爱头条 
	private int DEFAULT_EXTRABONUS_TIME = 1;
	private int INSTALL_EXPERIWNCE_TIME = 5;
	private int DEFAULT_INSTALL_COUNT  = 0;
	private boolean isExtraBonusCompleted = false;
	private boolean isLooklookCompleted = false;
	private boolean isInstallCompleted = false;
	private boolean isClickAdsCompleted = false;
	private boolean isSigninCompleted = false;
	private boolean isSigninMorning = false;
	private boolean isSigninNoon = false;
	private boolean isSigninAfternoon =false;
	private boolean isSigninNight = false;
	private boolean is360Completed = false;
	private boolean isTuituiComleted = false;
	private boolean isTurnturnComleted = false;
	private boolean isPackageCompleted = false;
	private int installCount = 0;

	ExtraBonusManager extraBonusManager;
	LooklookManager looklookManager;
	InstallAppManager installAppManager;
	SigninManager signinManager;
	
	public MiZhuan() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "me.mizhuan");
		capabilities.setCapability("appActivity", ".ActCover");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
		extraBonusManager = new ExtraBonusManager(driver);
		looklookManager = new LooklookManager(driver);
		installAppManager = new InstallAppManager(driver);
		signinManager = new SigninManager(driver);
		
	}
	
	public int start(){		
//		if (!AdbUtils.getTopActivity().contains("me.mizhuan/.TabFragmentActivity")) {
//			Activity activity = new Activity("me.mizhuan" , ".ActCover");
//			driver.startActivity(activity);
//			try {
//				Thread.sleep(20 * 1000);
//			} catch (InterruptedException e) {
//				driver.quit();
//				e.printStackTrace();
//		        return ResultDict.COMMAND_RESTART_APP;	
//			}
//		}
		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort +"/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
		} catch (Exception e) {
			driver.quit();
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
//		try {
//			if (isElementExistById("me.mizhuan:id/start_button")) {
//				driver.findElement(By.id("me.mizhuan:id/start_button")).click();
//				Thread.sleep(10000);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			driver.quit();
//			return ResultDict.COMMAND_RESTART_APP;
//		}
		
		if(DateUtils.getHour() == 1){
			isExtraBonusCompleted = false;
			isLooklookCompleted = false;
			isSigninCompleted = false;
			isSigninMorning = false;
			isSigninNoon = false;
			isSigninAfternoon = false;
			isSigninNight = false;
		}
		int result = ResultDict.COMMAND_SUCCESS;
		if(isElementExistByString("签到")){
			Log.log.info("开始签到任务");
			result  = clickSignin();
			if (ResultDict.COMMAND_SUCCESS != result)
				return result;
		} else {
			isSigninCompleted = true;
		}
		if(isElementExistByString("上午探班") || isElementExistByString("中午探班") || isElementExistByString("下午探班") || isElementExistByString("晚上探班")) {
			try {
				Log.log.info("点击探班");
//				driver.findElement(By.xpath("//android.widget.ListView/android.widget.LinearLayout/android.view.View/android.widget.LinearLayout[contains(@index,0)]")).click();
				if(isElementExistByString("上午探班")){
					driver.findElement(By.name("上午探班")).click();
					isSigninMorning = true;
					Thread.sleep(4000);
					AdbUtils.back();
				} else if(isElementExistByString("中午探班")) {
					driver.findElement(By.name("中午探班")).click();
					isSigninNoon = true;
					Thread.sleep(4000);
					AdbUtils.back();
				} else if(isElementExistByString("下午探班")) {
					driver.findElement(By.name("下午探班")).click();
					isSigninAfternoon = true;
					Thread.sleep(4000);
					AdbUtils.back();
				} else if(isElementExistByString("晚上探班")) {
					driver.findElement(By.name("晚上探班")).click();
					isSigninNight = true;
					Thread.sleep(4000);
					AdbUtils.back();
				} 
			} catch (Exception e) {
				driver.quit();
				e.printStackTrace();
				Log.log.info(e.getMessage());
				return ResultDict.COMMAND_RESTART_APP;
			}
		}
		if (!isExtraBonusCompleted) {
			Log.log.info("开始额外任务");
			result = startSigninAppTask();
			if (ResultDict.COMMAND_SUCCESS != result) {
				driver.quit();
				return result;
			}
		}
		if (!isClickAdsCompleted) {
			Log.log.info("开始看广告任务");
			result = startClickAds();
			if (ResultDict.COMMAND_SUCCESS != result) {
				driver.quit();
				return result;
			}
		}
		if(!isInstallCompleted){
			Log.log.info("安装任务开始");
			switch (Configure.productModel) {
			case "[OPPO A37m]":
				result = installApp_OPPO(driver);
				break;
			case "[CUN-TL00]":
				result = installApp_CUN_TL(driver);
				break;
			case "[Lenovo TB3-X70N]":
				break;
			case "[CUN-AL00]":
				result = installApp_CUN_AL(driver);
				break;
			default:
				break;
			}
			if (result != ResultDict.COMMAND_SUCCESS) {
				driver.quit();
				return result;
			}
		}
		if (!isLooklookCompleted) {
			result = startLooklookTaskFromBottomGame();
			if (ResultDict.COMMAND_SUCCESS != result) {
				driver.quit();
				return result;
			}
		}
		driver.quit();
		if(DateUtils.getHour() <= 12) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		return ResultDict.COMMAND_SUCCESS;
	}
	
	private int startLooklookTaskFromBottomGame() {

		try {
			Log.log.info("点击游戏赚");
			driver.findElement(By.name("游戏赚"))
			.click();
			Thread.sleep(8000);
			for (int i = 0; i < 5; i++) {
				AdbUtils.swipe(500, 700, 500, 300);
				Thread.sleep(1000);
			}
//			if (!clickEntertainmentNews()) {
//				return ResultDict.COMMAND_RESTART_APP;
//			}
			if (!is360Completed) {
				if (!clickThreeSixZeroNews()) {
					driver.quit();
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			if (!isTurnturnComleted) {
				if (!clickTurnturn()) {
					driver.quit();
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			if (!isTuituiComleted) {
				if (!clickTuitui()) {
					driver.quit();
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			if (!isPackageCompleted) {
				if (!clickRedPackage()) {
					driver.quit();
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
//			if (!clickGoldNews()) {
//			driver.quit();
//				return ResultDict.COMMAND_RESTART_APP;
//			}
//			if (!clickEighteenNews()) {
//			driver.quit();
//				return ResultDict.COMMAND_RESTART_APP;
//			}
//			if (!clickLoveNews()) {
//			driver.quit();
//				return ResultDict.COMMAND_RESTART_APP;
//			}
			driver.quit();
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();	
			Log.log.info(e.getMessage());
			driver.quit();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	// 推推乐
	public boolean clickTuitui() {
		try {
			Thread.sleep(3000);
			if(isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				isTuituiComleted = true;
				return true;
			}
			if(!isElementExistByString("推推乐")){
				return false;
			}
			for (; tuituiNum < Contants.TUITUI_NUM; tuituiNum++) {
				if(timeSwitcher() != ResultDict.COMMAND_SUCCESS){
					return false;
				}
				driver.findElement(By.name("推推乐")).click();
				Thread.sleep(10 * 1000);
				if (isElementExistByString("今日任务已完成")) {
					AdbUtils.back();
					return true;
				}
				driver.findElement(By.name("返回")).click();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 翻翻乐
	public boolean clickTurnturn() {
		try {
			Thread.sleep(3000);
			if(isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				isTurnturnComleted = true;
				return true;
			}
			if(!isElementExistByString("翻翻乐")){
				return true;
			}
			for (; tuituiNum < Contants.TURNTURN_NUM; tuituiNum++) {
				if(timeSwitcher() != ResultDict.COMMAND_SUCCESS){
					return false;
				}
				driver.findElement(By.name("翻翻乐")).click();
				Thread.sleep(10 * 1000);
				if (isElementExistByString("今日任务已完成")) {
					AdbUtils.back();
					AdbUtils.back();
					return true;
				}
				AdbUtils.back();
				AdbUtils.back();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 拆红包
	public boolean clickRedPackage() {
		try {
			Thread.sleep(3000);
			if(isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				isPackageCompleted = true;
				return true;
			}
			if(!isElementExistByString("拆红包")){
				return true;
			}
			for (; redPackageNum < Contants.RED_PACKAGES_NUM; redPackageNum++) {
				if(timeSwitcher() != ResultDict.COMMAND_SUCCESS){
					return false;
				}
				driver.findElement(By.name("拆红包")).click();
				Thread.sleep(10000);
				if (isElementExistByString("今日任务已完成")) {
					driver.findElement(By.name("返回")).click();
					return true;
				}
				driver.findElement(By.name("返回")).click();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 点金头条
	public boolean clickGoldNews() {
		try {
			AdbUtils.click(216,695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickGoldNews()) {
				return false;
			}
			for (; goldNewsNum < Contants.GOLD_NEWS_NUM ; goldNewsNum++) {
				Thread.sleep(5000);
				for(int i=0;i<goldNewsNum;i++){
				AdbUtils.swipe(300, 1100, 300, 500);
				}
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				if(!looklookManager.checkEnterGoldNews()){
					AdbUtils.click(300, 800);
				}
				Thread.sleep(10000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				AdbUtils.back();
				Thread.sleep(3000);
				if (!looklookManager.checkClickGoldNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 18头条
	public boolean clickEighteenNews() {
		try {
			AdbUtils.click(216,695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickEighteenNews()) {
				return false;
			}
			for (; eighteenNum < Contants.EIGHTEEN_NEWS_NUM; eighteenNum++) {
				Thread.sleep(5000);
				for(int i=0;i<eighteenNum;i++){
				AdbUtils.swipe(300, 1100, 300, 500-20*i);
				}
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				AdbUtils.back();
				Thread.sleep(3000);
				if (!looklookManager.checkClickEighteenNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 我爱头条
	public boolean clickLoveNews() {
		try {
			AdbUtils.click(648,695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickLoveNews()) {
				return false;
			}
			for (; loveNewsNum < Contants.LOVE_NEWS_NUM + 4; loveNewsNum++) {
				swipeAndClickNews2();
				if (!looklookManager.checkClickLoveNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 今日必看
	public void clickTodayMustNews() {
		// TODO
	}

	// 娱乐爆料
	public boolean clickEntertainmentNews() {
		try {
			driver.findElement(By.name("娱乐爆料"));
			Thread.sleep(2000);
			if(isElementExistByString("今日任务已完成")){
				AdbUtils.back();
				return true;
			}
			for (; entertainmentNews < Contants.ENTERTAINMENT_NEWS ; entertainmentNews++) {
				Thread.sleep(5000);
				AdbUtils.swipe(300, 1100, 300, 500);
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				if (!looklookManager.checkEnterEntertainNews()) {
					AdbUtils.click(300, 800);
					Thread.sleep(10000);
				}
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				AdbUtils.back();
				Thread.sleep(3000);
				while(looklookManager.checkEnterEntertainNews()){
					AdbUtils.back();
					Thread.sleep(8000);
				}
				if (!looklookManager.checkClickEntertainNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}
	

	private boolean clickThreeSixZeroNews() {
		try {
			Log.log.info("点击360新闻");
			driver.findElement(By.name("360新闻")).click();
			Thread.sleep(10000);
			if(isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				is360Completed = true;
				return true;
			}
//			if (!looklookManager.checkClick360News()) {
//				return false;
//			}
			if(isElementExistByString("今日任务已完成")){
				AdbUtils.back();
				return true;
			}
			for (; ThreeSixZeroNewsNum < Contants.THREE_SIX_ZERO_NEWS_NUM + 3; ThreeSixZeroNewsNum++) {
				if(timeSwitcher() != ResultDict.COMMAND_SUCCESS){
					return false;
				}
				swipeAndClickNews();
//				if (!looklookManager.checkClick360News()) {
//					return false;
//				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 滑动点击新闻 新闻界面停留20S
	public void swipeAndClickNews() {
		try {
			Thread.sleep(5000);
			AdbUtils.swipe(300, 1100, 300, 500);
			AdbUtils.click(300, 600);
			Thread.sleep(20000);
			AdbUtils.back();
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	// 滑动点击新闻 新闻界面滑动
	public void swipeAndClickNews2() {
		try {
			Thread.sleep(5000);
			AdbUtils.swipe(300, 1100, 300, 500);
			AdbUtils.click(300, 600);
			Thread.sleep(10000);
			for (int i = 0; i < 10; i++) {
				AdbUtils.swipe(300, 1100, 300, 500);
				Thread.sleep(1000);
			}
			AdbUtils.back();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int clickSignin() {
		try {
			Log.log.info("点击签到");
			driver.findElement(By.name("签到")).click();
			Thread.sleep(5000);
			Log.log.info("点击普通签到");
			driver.findElement(By.id("me.mizhuan:id/btnaction_one")).click();
			Thread.sleep(5000);
			AdbUtils.back();
			Thread.sleep(2000);
			AdbUtils.back(); 
			Log.log.info("签到完成");
			isSigninCompleted = true;
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			driver.quit();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	private int startClickAds() {

        try {
    		driver.findElement(By.name("推荐"))
    		.click();
			Thread.sleep(2000);
			driver.findElement(By.name("看看赚"))
			.click();
			Thread.sleep(4000);
			driver.findElement(By.name("点广告"))
			.click();
			Thread.sleep(2000);
			driver.findElement(By.name("开始赚钱"))
			.click();
			Thread.sleep(2000);
			if(isElementExistByString("仅此一次")){
				driver.findElement(By.name("仅此一次"))
				.click();
			}
			Thread.sleep(70 * 1000);
			isClickAdsCompleted = true;
			AdbUtils.killProcess(AdbUtils.getCurrentPackage());
			Thread.sleep(8000);
			AdbUtils.back();
			Thread.sleep(1000);
			if("点广告".equals(driver.findElement(By.id("me.mizhuan:id/title")).getText())){
				AdbUtils.back();
			}
			Thread.sleep(2000);
			AdbUtils.back();
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
        return ResultDict.COMMAND_SUCCESS;
	}

	private int startSigninAppTask() {
		try {
			String lastPackage = "";
			int appUseTime = 1;
			boolean leftSwipe = false;
			while (!((DateUtils.getHour() > 8) || ((DateUtils.getHour() == 8) && (DateUtils.getMinute() > 30)))) {
				if (leftSwipe) {
					AdbUtils.swipe(100, 500, 400, 500);
				} else {
					AdbUtils.swipe(400, 500, 100, 500);
				}
				Thread.sleep(1 * 60 * 1000);
				leftSwipe = !leftSwipe;
			}
			// 点击应用赚
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			// 额外奖励
			driver.findElement(By.name("额外奖励")).click();
			Thread.sleep(8000);
			AdbUtils.swipe(300, 500, 300, 1000);
			Thread.sleep(5000);
			while (true) {
				Thread.sleep(1000);
				if (isElementExistById("me.mizhuan:id/mituo_status")) {
					String mituo = driver.findElement(By.id("me.mizhuan:id/mituo_status")).getText();
					String type = driver.findElement(By.id("me.mizhuan:id/mituo_textViewPromo")).getText().substring(1, 3);
					System.out.println("type = " + type);
					if("已抢完".equals(mituo) || "未到时间".equals(mituo) || "深度".equals(type)) {
						Log.log.info("额外任务完成");
						isExtraBonusCompleted = true;
						break;
					} else {
						driver.findElement(By.id("me.mizhuan:id/mituo_status")).click();
					}
				} else {
					continue;
				}
				Log.log.info("额外奖励开始计时。。。");
				Thread.sleep(appUseTime * 70 * 1000);
				String name = AdbUtils.getCurrentPackage();
				lastPackage = AdbUtils.getCurrentPackage();
				AdbUtils.killProcess(AdbUtils.getCurrentPackage());
				Log.log.info("kill " + name);
				Thread.sleep(5000);
				if (!extraBonusManager.checkKillApp(name)) {
					driver.quit();
					return ResultDict.COMMAND_RESTART_APP;
				}
				Thread.sleep(5000);
			}
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	public int installApp_CUN_AL(AndroidDriver driver) {
		try {
			Thread.sleep(10000);
			Log.log.info("点击应用赚");
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			Log.log.info("点击应用");
			driver.findElement(By.name("应用")).click();
			Thread.sleep(2000);
//			driver.findElement(By.xpath("//android.widget.TabWidget/android.widget.LinearLayout[contains(@index,1)]"))
//					.click();
			while (installCount < Configure.Mizhuan_instlal_count) {
				Thread.sleep(3000);				
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
						.getText();
				Log.log.info("appTyep = " + text + "   " + "appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));			
				if (("注册".equals(text) || "体验".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("立即安装".equals(buttomButton.getText())) {
						Log.log.info("点击立即安装");
						buttomButton.click();
						Thread.sleep(3 * 1000);
						if(isElementExistByString("立即安装")) {
							AdbUtils.back();
							Thread.sleep(2 * 1000);
							SwipeScreen.swipe(driver, 300, 800, 300, 665);
							continue;
						}
						Thread.sleep(60 * 1000);
						while (true) {
							WebElement installButton = driver
									.findElement(By.id("com.android.packageinstaller:id/ok_button"));
							if ("下一步".equals(installButton.getText())) {
								installButton.click();
								Thread.sleep(1000);
							} else {
								installButton.click();
								Thread.sleep(1000);
								break;
							}
						}
						Thread.sleep(10 * 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(5 * 60* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
						if (isElementExistByString("软件包安装程序")) {
							driver.pressKeyCode(AndroidKeyCode.BACK);
							Thread.sleep(2000);
						}
					} else if ("继续体验".equals(buttomButton.getText())) {
						Log.log.info("点击继续体验");
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								Log.log.info("click allow");
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(5*60* 1000);						
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 800, 300, 665);
			}
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	private int installApp_CUN_TL(AndroidDriver driver){
		try {
			Thread.sleep(10000);
			Log.log.info("点击应用赚");
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			Log.log.info("点击应用");
			driver.findElement(By.name("应用")).click();
			Thread.sleep(2000);
			while (installCount < Configure.Mizhuan_instlal_count) {
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
						.getText();
				Log.log.info("appTyep = " + text + "   " + "appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				if (("注册".equals(text) || "体验".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("立即安装".equals(buttomButton.getText())) {
						Log.log.info("点击立即安装");
						buttomButton.click();
						Thread.sleep(3 * 1000);
						if(isElementExistByString("立即安装")) {
							AdbUtils.back();
							Thread.sleep(2 * 1000);
							SwipeScreen.swipe(driver, 300, 800, 300, 665);
							continue;
						}
						Thread.sleep(60 * 1000);
						driver.findElement(By.name("安装")).click();
						Thread.sleep(60* 1000);
//						driver.findElement(By.name("打开")).click();
//						Thread.sleep(1000);
//						driver.findElement(By.name("删除")).click();
//						Thread.sleep(5 * 1000);
//						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						AdbUtils.back();
						Thread.sleep(3 * 1000);
						buttomButton.click();
						Thread.sleep(10 * 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(5*60* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){					
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else if ("继续体验".equals(buttomButton.getText())) {
						Log.log.info("点击继续体验");
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(5*60* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){					
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 800, 300, 665);
			}
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	
	
	
	public int installApp_OPPO(AndroidDriver driver) {
		try {
			Thread.sleep(10000);
			Log.log.info("点击应用赚");
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			Log.log.info("点击应用");
			driver.findElement(By.name("应用")).click();
			Thread.sleep(2000);
			while (installCount < Configure.Mizhuan_instlal_count) {
				Thread.sleep(3000);
				Log.log.info("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
						.getText();
				Log.log.info("appTyep = " + text + "   " + "appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				Log.log.info(text);
				if (("注册".equals(text) || "体验".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("立即安装".equals(buttomButton.getText())) {
						Log.log.info("点击立即安装");
						buttomButton.click();
						Thread.sleep(3 * 1000);
						if(isElementExistByString("立即安装")) {
							AdbUtils.back();
							Thread.sleep(2 * 1000);
							SwipeScreen.swipe(driver, 300, 800, 300, 665);
							continue;
						}
						Thread.sleep(60 * 1000);
						driver.findElement(By.name("安装")).click();
						Thread.sleep(30 * 1000);
						for (int j = 0; j < 5; j++) {
							if(driver.getPageSource().contains("同意并继续")){
								driver.findElement(By.name("同意并继续")).click();
								Thread.sleep(2000);
							}
						}
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(5*60* 1000);
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
						driver.findElement(By.name("完成")).click();
						Thread.sleep(2000);
					} else if ("继续体验".equals(buttomButton.getText())) {
						Log.log.info("点击继续体验");
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("同意并继续")){
								Log.log.info("click allow");
								driver.findElement(By.name("同意并继续")).click();
								Thread.sleep(2000);
							}
						}
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(5*60* 1000);					
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 636, 300, 500);
			}
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	
	private boolean isElementExist(){
		try{
			WebElement element = driver.findElement(By.id("com.huawei.systemmanager:id/btn_allow"));
			element.isDisplayed();
			return true;
		}catch(Exception e) {		
			return false;
		}
	}
	
	private boolean isElementExistByString(String name){
		try{
			WebElement element = driver.findElement(By.name(name));
			element.isDisplayed();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private boolean isElementExistById(String id){
		try{
			WebElement element = driver.findElement(By.id(id));
			element.isDisplayed();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private int timeSwitcher(){
		if(DateUtils.getHour() == 8 && isSigninMorning == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if(DateUtils.getHour() == 11 && isSigninNoon == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if(DateUtils.getHour() == 13 && isSigninNoon == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if(DateUtils.getHour() == 19 && isSigninNight == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if(DateUtils.getHour() == 8 && DateUtils.getMinute() >= 30 && DateUtils.getMinute() <= 35) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		if(DateUtils.getHour() == 10 && DateUtils.getMinute() >= 30 && DateUtils.getMinute() <= 35) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		if(DateUtils.getHour() == 12 && DateUtils.getMinute() >= 0 && DateUtils.getMinute() <= 5) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		return ResultDict.COMMAND_SUCCESS;
	}

}