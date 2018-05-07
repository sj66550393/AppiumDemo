package manager;

import java.util.ArrayList;

import org.openqa.selenium.By;

import app.MiZhuan;
import io.appium.java_client.android.AndroidDriver;
import utils.AdbUtils;
import utils.Log;

public class SigninManager {
	
	AndroidDriver driver;

	public SigninManager(AndroidDriver driver){
		this.driver = driver;
	
	}
	
	
	public boolean checkEnterSigninDetail() {
	 return "Ç©µ½".equals(driver.findElement(By.id("me.mizhuan:id/title")).getText());
	}
	
	public boolean checkHasSignin(){
		return false;
	}
	
}
