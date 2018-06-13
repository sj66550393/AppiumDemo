import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import io.appium.java_client.android.AndroidDriver;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import app.MeiRiZhuanDian;
import app.MiZhuan;
import callback.TaskCallback;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import utils.AdbUtils;
import utils.Log;
import common.Configure;
import common.ResultDict;

public class ShouZhuan {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		if(args[0] != null) {
			Configure.logDir = args[0] + ":\\";
			AdbUtils.storageDes = args[0] + ":\\";
		}
		if(args[1] != null) {
		Configure.deviceId = args[1];
		AdbUtils.deviceId = args[1];
		AdbUtils.storageDir = AdbUtils.storageDes + args[1] + "/";
		AdbUtils.adb = "adb -s " + args[1]	+" shell ";
		}
		if(args[2] != null) {
			Configure.appiumPort = args[2];
		}
		
		if(args[3] != null){
			Configure.Mizhuan_instlal_count = Integer.parseInt(args[3]);
		}
		init();	
		Timer t = new Timer();
		t.schedule(new Task1(), 1000);
	}
	
	private static void init() {
		Configure.isPad = AdbUtils.isPad();
		String productModel = AdbUtils.getProductModel();
		Configure.productModel = productModel;
		File file = new File(Configure.logDir + AdbUtils.deviceId);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
}




class Task1 extends TimerTask {
	ExecutorService fixedThreadPool;

	public Task1() {
		fixedThreadPool = Executors.newFixedThreadPool(4);
	}

	@Override
	public void run() {
		if (AdbUtils.isAwake2().equals("false")) {
			try {
				AdbUtils.clickPower();
				Thread.sleep(2000);
				AdbUtils.swipe(300, 900, 300, 300);
				Thread.sleep(2000);
				AdbUtils.swipe(100, 500, 600, 500);
				Thread.sleep(2000);
				AdbUtils.swipe(100, 500, 600, 500);
				Thread.sleep(2000);
				AdbUtils.swipe(300, 900, 300, 300);
			} catch (Exception e) {
			}
		}
		if (!MiZhuan.getInstance().isCompleted) {
			MiZhuan.getInstance().start(new TaskCallback() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onRestartApp(AndroidDriver driver) {
					driver.quit();
					restartApp();
					
				}

			});
		} else if (!MeiRiZhuanDian.getInstance().isCompleted) {
			MeiRiZhuanDian.getInstance().start();
		}
	}
	
	public void restartApp() {
		try {
			while (!AdbUtils.getCurrentPackage().contains("launcher")) {
				AdbUtils.killProcess(AdbUtils.getCurrentPackage());
			}
			Thread.sleep(3000);
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
