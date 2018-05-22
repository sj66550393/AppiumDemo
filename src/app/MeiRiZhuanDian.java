package app;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;

public class MeiRiZhuanDian {
	private boolean isLooklookCompleted = false;
//	private int choujiangji = 
	
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	public MeiRiZhuanDian() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "me.mizhuan");
		capabilities.setCapability("appActivity", ".ActCover");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", "0123456789ABCDEF");
//		extraBonusManager = new ExtraBonusManager(driver);
//		looklookManager = new LooklookManager(driver);
//		installAppManager = new InstallAppManager(driver);
//		signinManager = new SigninManager(driver);
	}
	
//	public int start() {
//		
//	}

}
