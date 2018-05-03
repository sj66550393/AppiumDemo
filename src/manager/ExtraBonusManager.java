package manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import app.MiZhuan;
import common.ResultDict;
import io.appium.java_client.android.AndroidDriver;
import utils.AdbUtils;
import utils.Log;

public class ExtraBonusManager {
	
	AndroidDriver driver;

	public ExtraBonusManager(AndroidDriver driver){
		this.driver = driver;

	}

	public boolean checkClickBottomApplication() {
		return false;

	}
	
	public boolean checkClickExtraBonus(){
		return false;
	}
	
	public int checkEnterApp(){
		return 1;
	}
	
	public boolean checkKillApp(String name){
		return false;
	}
	
	public boolean checkFinishExtraBonus(){
		return false;
	}
	
}
