package manager;

import app.MiZhuan;
import io.appium.java_client.android.AndroidDriver;
import utils.AdbUtils;
import utils.Log;

public class LooklookManager {

	AndroidDriver driver;

	public LooklookManager(AndroidDriver driver){
		this.driver = driver;
	}
	public boolean checkClickBottomGame(){
		return false;
	}
	public boolean checkClickEntertainNews(){
		return false;
	}
	
	public boolean checkEnterEntertainNews(){
		return false;
	}
	
	public boolean checkEnterHotNews(){
		return false;
	}
	
	public boolean checkEnterGoldNews(){
		return false;
	}
	
	public boolean checkEnterEighteenNews(){
		return false;
	}
	
	public boolean checkClickHotNews(){
		return false;
	}
	
	public boolean checkClickTurnturn(){
		return false;
	}

	public boolean checkClick360News(){
		return false;
	}
	
	public boolean checkClickTuituiLe(){
		return true;
	}
	
	public boolean checkClickGoldNews(){
		return false;
	}
	
	public boolean checkClickEighteenNews(){
		return false;
	}
	
	public boolean checkClickLoveNews() {
		return false;
	}
}
