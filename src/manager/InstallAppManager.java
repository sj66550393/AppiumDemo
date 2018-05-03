package manager;

import java.util.ArrayList;

import app.MiZhuan;
import common.ResultDict;
import io.appium.java_client.android.AndroidDriver;
import utils.AdbUtils;
import utils.Log;

public class InstallAppManager {
	
	AndroidDriver driver;
	
	public InstallAppManager(AndroidDriver driver){
		this.driver = driver;
	}
	
	public boolean checkEnterAppDetail(){
		return false;
	}
	
	public boolean checkClickApplicationButton(){
		return false;
	}
	
	public boolean checkTL00Install() {
		return false;
	}
	
	public boolean checkTL00Install2() {
		return false;
	}
	
	public boolean checkTL00InstallComplete(){
		return false;
	}
	
	
	public boolean checkBottomInstantInstall(){
		return false;
	}
	
	public boolean checkBottomContinueExperience(){
		return false;
	}

	public boolean checkTL00DeletePackage() {
		return false;
	}
	
	public boolean checkEnterApp(){
		if(AdbUtils.getTopActivity().equals("me.mizhuan/.TabFragmentActivity")){
			return false;
		}
		return true;
	}
	
	public boolean isInApplicationDetail(){
		return true;
	}
	
	public boolean checkKillApp() {
	return false;
	}
	
}
