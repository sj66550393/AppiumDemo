package manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
		if (isElementExistByString("应用详情")) {
			return ResultDict.COMMAND_BACK;
		}
		if(isHuaweiUpdateActivity()){
			return ResultDict.COMMAND_BACK;
		}
		if(AdbUtils.getTopActivity().contains("me.mizhuan/.TabFragmentActivity")){
			return ResultDict.COMMAND_RESTART_APP;
		}
		return ResultDict.COMMAND_SUCCESS;
	}
	
	public boolean checkKillApp(String name){
		Log.log.info("checkKillApp");
		if(!AdbUtils.getTopActivity().contains("me.mizhuan/.TabFragmentActivity")){
			return false;
		}
		return true;
	}
	
	public boolean checkFinishExtraBonus(){
		return !isElementExistByString("打开");
	}
	
	private boolean isHuaweiUpdateActivity(){
		return "com.huawei.android.hwouc/.ui.activities.firmware.FirmwareNewVersionDetailsActivity".equals(AdbUtils.getTopActivity());
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
