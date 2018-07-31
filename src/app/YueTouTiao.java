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

public class YueTouTiao {

	private static YueTouTiao yuetoutiao;
	public boolean isCompleted = false;
	public boolean isLookNewCompleted = false;
	public boolean isLookVideoCompleted = false;

	AndroidDriver driver;
	DesiredCapabilities capabilities;

	public YueTouTiao() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "com.expflow.reading");
		capabilities.setCapability("appActivity", ".activity.SplashActivity");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
	}

	public static YueTouTiao getInstance() {
		if (yuetoutiao == null) {
			yuetoutiao = new YueTouTiao();
		}
		return yuetoutiao;
	}

	public void start(TaskCallback callback) {
		int result = ResultDict.COMMAND_SUCCESS;
		try {
			System.out.println("start");
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort + "/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			e.printStackTrace();
			isCompleted = true;
			callback.onRestartApp(driver);
			return;
		}
		
		// 点击领取
		try {
			if (isElementExistById("com.expflow.reading:id/ivCountDownTime")) {
				driver.findElement(By.id("com.expflow.reading:id/ivCountDownTime")).click();
				Thread.sleep(1000);
				AdbUtils.back();
			}
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}
		
		
		if (!isLookNewCompleted) {
			result = lookNews();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
			}
		}

		if (!isLookVideoCompleted) {
			result = lookVideo();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
			}
		}
		isCompleted = true;
		callback.onSuccess(driver);
	}

	private int lookNews() {
		try {
			driver.findElement(By.name("刷新")).click();
			for (int i = 0; i < 5; i++) {
				AdbUtils.swipe(300, 500, 300, 1000);
				Thread.sleep(3000);
				AdbUtils.click(300, 600);
				Thread.sleep(2000);
				for (int j = 0; j < 5; j++) {
					AdbUtils.swipe(300, 700, 300, 500);
					Thread.sleep(500);
				}
				for (int j = 0; j < 5; j++) {
					AdbUtils.swipe(300, 500, 300, 700);
					Thread.sleep(500);
				}
				for (int j = 0; j < 5; j++) {
					AdbUtils.swipe(300, 700, 300, 500);
					Thread.sleep(500);
				}
				for (int j = 0; j < 5; j++) {
					AdbUtils.swipe(300, 500, 300, 700);
					Thread.sleep(500);
				}
				driver.findElement(By.id("com.expflow.reading:id/iv_close")).click();
				Thread.sleep(2000);
				AdbUtils.swipe(600, 700, 300, 700);
			}
			isLookNewCompleted = true;
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int lookVideo() {
		try {
			driver.findElement(By.name("视频")).click();
			for (int i = 0; i < 5; i++) {
				AdbUtils.swipe(300, 500, 300, 1000);
				Thread.sleep(3000);
				AdbUtils.click(300, 600);
				Thread.sleep(35 * 1000);
				if(isElementExistById("com.expflow.reading:id/iv_close")) {
					driver.findElement(By.id("com.expflow.reading:id/iv_close")).click();
				}else {
					AdbUtils.back();
				}
				Thread.sleep(2000);
			}
			isLookVideoCompleted = true;
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
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
	
	public void reset(){
		isCompleted = false;
		isLookNewCompleted = false;
		isLookVideoCompleted = false;
	}

}
