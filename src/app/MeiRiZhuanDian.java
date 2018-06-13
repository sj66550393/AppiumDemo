package app;

import org.openqa.selenium.remote.DesiredCapabilities;

import callback.TaskCallback;
import common.Configure;
import io.appium.java_client.android.AndroidDriver;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;

public class MeiRiZhuanDian {
	private static MeiRiZhuanDian meiRiZhuanDian;
	public boolean isCompleted;
	private boolean isLooklookCompleted = false;
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
		
	}

}
