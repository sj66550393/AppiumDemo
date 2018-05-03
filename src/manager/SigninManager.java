package manager;

import java.util.ArrayList;

import app.MiZhuan;
import io.appium.java_client.android.AndroidDriver;
import utils.AdbUtils;
import utils.Log;

public class SigninManager {
	
	AndroidDriver driver;

	public SigninManager(AndroidDriver driver){
		this.driver = driver;
	
	}
	
	public boolean checkClickBottomRecommand(){
	return false;
	}
	
	public boolean checkEnterSigninDetail() {
		return false;
	}
	
	public boolean checkHasSignin(){
		return false;
	}
	
}
