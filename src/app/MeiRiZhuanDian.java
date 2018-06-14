package app;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import callback.TaskCallback;
import common.Configure;
import common.ResultDict;
import io.appium.java_client.android.AndroidDriver;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;
import utils.AdbUtils;
import utils.Log;

public class MeiRiZhuanDian {
	private static MeiRiZhuanDian meiRiZhuanDian;
	public boolean isCompleted;
	private boolean isExtraBonusCompleted = true;
	private boolean isLooklookCompleted = false;
	private boolean isInstallCompleted = true;
	private int choujiangji = 0; //欢乐抽奖机
	private int yangshen = 0; //养生之道
	private int paihongbao = 0; //全民派红包
	private int wajinkuang = 0; //
	private int quanjiatong = 0;
	private int xianjinghongbao = 0;
	private int dahuayule = 0;
	private int yingdajiang = 0;
	
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	private MeiRiZhuanDian() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "com.adsmobile.mrzd");
		capabilities.setCapability("appActivity", ".ActCover");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
//		extraBonusManager = new ExtraBonusManager(driver);
//		looklookManager = new LooklookManager(driver);
//		installAppManager = new InstallAppManager(driver);
//		signinManager = new SigninManager(driver);
	}
	
	public static MeiRiZhuanDian getInstance() {
		if(meiRiZhuanDian != null) {
			meiRiZhuanDian = new MeiRiZhuanDian();
		}
		return meiRiZhuanDian;
	}
	
	public void start(TaskCallback callback) {
		try {
			System.out.println("start");
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort +"/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			driver.quit();
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}
		int result = ResultDict.COMMAND_SUCCESS;
		if(isElementExistById("com.adsmobile.mrzd:id/window_image_dismiss")) {
			driver.findElement(By.id("com.adsmobile.mrzd:id/window_image_dismiss")).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				callback.onRestartApp(driver);
				return;
			}
		}
		if (!isExtraBonusCompleted) {
			Log.log.info("开始额外任务");
			result = startSigninAppTask();
			if (ResultDict.COMMAND_SUCCESS != result) {
				callback.onRestartApp(driver);
				return;
			}
		}
	}
	
	private int startSigninAppTask() {
		try {
		// 点击应用赚
		driver.findElement(By.name("快速任务")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("软件签到")).click();
		Thread.sleep(1000);
		boolean isFirst = true;
		while(isElementExistById("com.adsmobile.mrzd:id/tm_item")) {
			String appName = driver.findElement(By.xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]")).getText();
			String time = driver.findElement(By.xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.TextView[contains(@index,2)]")).getText();
		    
			System.out.println("appName = " + appName);
		    System.out.println("time = " + time);
		    if(isFirst) {
		    	String packageName2 = Configure.map.get(appName);
				AdbUtils.rootComandEnablePackage(packageName2);
				isFirst = false;
		    }
		    driver.findElement(By.xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout")).click();
		    if(isElementExistByXpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout[contains(@index,1)]")) {
				String secondAppName = driver.findElement(By.xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout[contains(@index,1)]/android.widget.TextView[contains(@index,1)]")).getText();
				String packageName = Configure.map.get(secondAppName);
				
				if(packageName != null){
					System.out.println("packageName = " + packageName);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							AdbUtils.rootComandEnablePackage(packageName);
						}
					}).start();
				}
		    }
		}
		} catch(Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
		return 0;
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
	
	private boolean isElementExistByXpath(String xpath) {
		try{
			WebElement element = driver.findElement(By.xpath(xpath));
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

}
