import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import io.appium.java_client.android.AndroidDriver;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gson.Gson;

import app.MeiRiZhuanDian;
import app.MiZhuan;
import app.MyApplicationList;
import callback.TaskCallback;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import utils.AdbUtils;
import utils.Log;
import utils.TextUtil;
import common.Configure;
import common.ResultDict;

public class ShouZhuan {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		if (args.length > 0 && args[0] != null) {
			Configure.logDir = args[0];
			AdbUtils.storageDes = args[0];
		} else {
			Configure.logDir = "d:\\";
			AdbUtils.storageDes = "d:\\";
		}
		if (args.length > 1 && args[1] != null) {
			Configure.deviceId = args[1];
			AdbUtils.deviceId = args[1];
			AdbUtils.storageDir = AdbUtils.storageDes + args[1] + "/";
			AdbUtils.adb = "adb -s " + args[1] + " shell ";
		} else {
			Configure.deviceId = "UCZHUGEU99999999";
			AdbUtils.deviceId = "UCZHUGEU99999999";
			AdbUtils.storageDir = AdbUtils.storageDes + "UCZHUGEU99999999" + "/";
			AdbUtils.adb = "adb -s " + "UCZHUGEU99999999" + " shell ";
		}
		if (args.length > 2 && args[2] != null) {
			Configure.appiumPort = args[2];
		} else {
			Configure.appiumPort = "4811";
		}

		if (args.length > 3 && args[3] != null) {
			Configure.Mizhuan_instlal_count = Integer.parseInt(args[3]);
		} else {
			Configure.Mizhuan_instlal_count = 21;
		}
		
		if(args.length > 4 && args[4] != null && "1".equals(args[4])) {
			System.out.println("set isGetInstallCount true");
		   MiZhuan.getInstance().isGetInstallCount = true;
		} else {
		   MiZhuan.getInstance().isGetInstallCount = false;
		}
		
		
		init();
		// MiZhuan.getInstance().checkAppList();
		// MiZhuan.getInstance().checkAllApp();
		Timer t = new Timer();
		t.schedule(new Task1(), 1000);
	}

	private static void init() {
		Configure.isPad = AdbUtils.isPad();
		String productModel = AdbUtils.getProductModel();
		Configure.productModel = productModel;
		File file = new File(Configure.logDir + AdbUtils.deviceId);
		if (AdbUtils.isRoot()) {
			try {
				MyApplicationList.getInstance().getApplicationList();
				AdbUtils.pull("sdcard/appInfo.txt", AdbUtils.storageDes + AdbUtils.deviceId);
				File appInfoFile = new File(AdbUtils.storageDir + "appInfo.txt");
				if (appInfoFile.exists()) {
					String info = TextUtil.txt2StringUTF8(appInfoFile);
					Configure.map = new Gson().fromJson(info, HashMap.class);
					AdbUtils.rootComandDisablePackage();
				}
			} catch (Exception e) {
				System.out.println("error = " + e.getMessage());
				e.printStackTrace();
			}
		}
		if (!file.exists()) {
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
				public void onSuccess(AndroidDriver driver) {
					MiZhuan.getInstance().isCompleted = true;
					driver.quit();
					restartApp();
				}

				@Override
				public void onRestartApp(AndroidDriver driver) {
					if (isElementExistByString(driver, "确定")) {
						driver.findElement(By.name("确定")).click();
					}
					driver.quit();
					restartApp();

				}

			});
		} else if (!MeiRiZhuanDian.getInstance().isCompleted) {
			MeiRiZhuanDian.getInstance().start(new TaskCallback() {

				@Override
				public void onSuccess(AndroidDriver driver) {
					MeiRiZhuanDian.getInstance().isCompleted = true;
					driver.quit();
//					restartApp();
				}

				@Override
				public void onRestartApp(AndroidDriver driver) {
					if (isElementExistByString(driver, "确定")) {
						driver.findElement(By.name("确定")).click();
					}
					driver.quit();
					restartApp();
				}
			});
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

	private boolean isElementExistByString(AndroidDriver driver, String name) {
		try {
			WebElement element = driver.findElement(By.name(name));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
