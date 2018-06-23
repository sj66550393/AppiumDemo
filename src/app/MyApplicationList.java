package app;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import callback.TaskCallback;
import common.Configure;
import io.appium.java_client.android.AndroidDriver;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;

public class MyApplicationList {
	
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	
	private MyApplicationList() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "com.jiesong.myapplicationlist");
		capabilities.setCapability("appActivity", ".MainActivity");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
	}
	
	private static MyApplicationList myApplicationList;
	
	public static MyApplicationList getInstance() {
		if(myApplicationList ==  null) {
			myApplicationList = new MyApplicationList();
		}
		return myApplicationList;
	}
	
	public void getApplicationList() {
		try {
			System.out.println("start");
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort +"/wd/hub"), capabilities);
			Thread.sleep(10 * 1000);
			driver.findElement(By.id("com.jiesong.myapplicationlist:id/btn_app")).click();
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			driver.quit();
			e.printStackTrace();
			return;
		}
		
	}

}
