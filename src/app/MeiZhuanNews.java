package app;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import callback.TaskCallback;
import common.Configure;
import common.ResultDict;
import io.appium.java_client.android.AndroidDriver;
import utils.AdbUtils;

public class MeiZhuanNews {

	
	static MeiZhuanNews meiZhuanNews;
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	public boolean isCompleted = true;
	private int currentNewsCount = 0;
	
	
	private MeiZhuanNews() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "com.adsmobile.mrzd");
		capabilities.setCapability("appActivity", ".ui.activity.MrzdAdSplashActivity");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
	}

	public static MeiZhuanNews getInstance() {
		if (meiZhuanNews == null) {
			meiZhuanNews = new MeiZhuanNews();
		}
		return meiZhuanNews;
	}
	
	public void start(TaskCallback callback) {
		try {			
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort + "/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
		} catch (Exception e) {			
			e.printStackTrace();
			isCompleted = true;
			callback.onRestartApp(driver);
			return;
		}
		int result = ResultDict.COMMAND_SUCCESS;

		//
		if (isElementExistById("com.adsmobile.mrzd:id/window_image_dismiss")) {
			driver.findElement(By.id("com.adsmobile.mrzd:id/window_image_dismiss")).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				callback.onRestartApp(driver);
				return;
			}
		}

		// 升级
		try {
			Thread.sleep(3000);
			if (isElementExistByString("狠心拒绝")) {
				driver.findElement(By.name("狠心拒绝")).click();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}
		if (!isCompleted) {
			result = startNews();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
				return;
			}
		}
		isCompleted = true;
		callback.onSuccess(driver);
		
		
		
	}
	
	
	private boolean isElementExistByString(String name) {
		try {
			WebElement element = driver.findElement(By.name(name));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isElementExistByXpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isElementExistById(String id) {
		try {
			WebElement element = driver.findElement(By.id(id));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	private int startNews() {
		try {
			driver.findElement(By.name("阅读赚钱")).click();
			Thread.sleep(4000);
			int count = 0;
			while (true) {
				String bottomText = driver.findElement(By.id("com.adsmobile.mrzd:id/new_news_task_surplus")).getText();
				if(bottomText.contains("今日剩余:0次")){
					isCompleted = true;
					break;
				}
				if (count == 6) {
					isCompleted = true;
					return ResultDict.COMMAND_SUCCESS;
				}
				if (!isElementExistByString("新闻阅读")) {
					return ResultDict.COMMAND_RESTART_APP;
				}
				AdbUtils.click(300, 600);
				Thread.sleep(18 * 1000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				if (!isElementExistByString("新闻阅读")) {
					AdbUtils.killProcess(AdbUtils.getCurrentPackage());
					Thread.sleep(3000);
				} else {
					AdbUtils.back();
					currentNewsCount++;
					Thread.sleep(80 * 1000);
				}
				AdbUtils.swipe(300, 1100, 300, 500);
				count++;
			}
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	public void reset(){
		isCompleted = false;
	}
	
	
}
