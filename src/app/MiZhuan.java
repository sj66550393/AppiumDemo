package app;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.util.log.Log;
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
import utils.AdbUtils;
import utils.SwipeScreen;
import common.ResultDict;

public class MiZhuan {
	
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	private int redPackageNum = 0; // 拆红包 5篇
	private int todayMustNum = 0; // 今日必看 6篇
	private int entertainmentNews = 0; // 娱乐爆料 5篇
	private int ThreeSixZeroNewsNum = 0; // 360新闻 每天6篇
	private int HotNewsNum = 0; // 热点新闻 每天5条
	private int turnturnNum = 0; // 翻翻乐 9篇
	private int tuituiNum = 0; // 推推乐 5篇
	private int goldNewsNum = 0; // 点金头条 7篇
	private int eighteenNum = 0; // 18头条
	private int loveNewsNum = 0; // 我爱头条 9篇
	private int DEFAULT_EXTRABONUS_TIME = 1;
	private int INSTALL_EXPERIWNCE_TIME = 5;
	private int DEFAULT_INSTALL_COUNT  =5;
	private boolean isExtraBonusCompleted = false;
	private boolean isLooklookCompleted = false;
	private boolean isInstallCompleted = false;
	private boolean isSigninCompleted = false;
	private boolean isSigninMorning = false;
	private boolean isSigninNoon = false;
	private boolean isSigninAfternoon =false;
	private boolean isSigninNight = false;
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
		capabilities.setCapability("udid", "UCZHUGEU99999999");
		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:4726/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		extraBonusManager = new ExtraBonusManager(driver);
		looklookManager = new LooklookManager(driver);
		installAppManager = new InstallAppManager(driver);
		signinManager = new SigninManager(driver);
		
	}
	
	public int start(){		
		if (!AdbUtils.getTopActivity().contains("me.mizhuan/.TabFragmentActivity")) {
			Activity activity = new Activity("me.mizhuan" , ".ActCover");
			driver.startActivity(activity);
			try {
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		int result = ResultDict.COMMAND_SUCCESS;
		if(DateUtils.getHour() == 1){
			isExtraBonusCompleted = false;
			isLooklookCompleted = false;
			isSigninCompleted = false;
			isSigninMorning = false;
			isSigninNoon = false;
			isSigninAfternoon = false;
			isSigninNight = false;
		}
//		if(!isSigninCompleted){
////			result = signIn();
//			if (ResultDict.COMMAND_SUCCESS != result)
//				return result;
//		}
		int result = installApp_OPPO(driver);
		if(result != ResultDict.COMMAND_SUCCESS){
			return result;
		}
		return ResultDict.COMMAND_SUCCESS;
	}
	
	public int installApp_CUN_AL(AndroidDriver driver) {
		try {
			Thread.sleep(10000);
			driver.findElement(By.xpath("//android.widget.TabWidget/android.widget.LinearLayout[contains(@index,1)]"))
					.click();
			while (installCount < DEFAULT_INSTALL_COUNT) {
				Thread.sleep(3000);
				System.out.println("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.id("me.mizhuan:id/mituo_app_size"))
						.getText();
				System.out.println("appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				System.out.println(text);
				if (("注册".equals(text) || "体验".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("立即安装".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(30 * 1000);
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
						Thread.sleep(20* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(20* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);					 
					} else if ("继续体验".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								System.out.println("click allow");
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
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
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	private int installApp_CUN_TL(){
		try {
			Thread.sleep(10000);
			driver.findElement(By.xpath("//android.widget.TabWidget/android.widget.LinearLayout[contains(@index,1)]"))
					.click();
			Thread.sleep(3000);
			while (installCount < DEFAULT_INSTALL_COUNT) {
				System.out.println("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.id("me.mizhuan:id/mituo_app_size"))
						.getText();
				System.out.println("app size = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				System.out.println(text);
				if (("注册".equals(text) || "体验".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("立即安装".equals(buttomButton.getText())) {
						buttomButton.click();
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
						Thread.sleep(60* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						installCount++;
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						while(isElementExistByString("软件包安装程序")){
							
						}
						Thread.sleep(2000);
					} else if ("继续体验".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("允许")){
								System.out.println("click allow");
								driver.findElement(By.name("允许")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						installCount++;
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
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
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	
	
	
	public int installApp_OPPO(AndroidDriver driver) {
		try {
			Thread.sleep(10000);
			driver.findElement(By.xpath("//android.widget.TabWidget/android.widget.LinearLayout[contains(@index,1)]"))
					.click();
			while (installCount < DEFAULT_INSTALL_COUNT) {
				Thread.sleep(3000);
				System.out.println("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.id("me.mizhuan:id/mituo_app_size"))
						.getText();
				System.out.println("appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				System.out.println(text);
				if (("注册".equals(text) || "体验".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("立即安装".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(60 * 1000);
						driver.findElement(By.name("安装")).click();
						Thread.sleep(30 * 1000);
						for (int j = 0; j < 5; j++) {
							if(driver.getPageSource().contains("同意并继续")){
								driver.findElement(By.name("同意并继续")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
						driver.findElement(By.name("完成")).click();
						Thread.sleep(2000);
					} else if ("继续体验".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("同意并继续")){
								System.out.println("click allow");
								driver.findElement(By.name("同意并继续")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
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

}