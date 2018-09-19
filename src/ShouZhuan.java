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
import app.News;
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
			Configure.logDir = "e:\\";
			AdbUtils.storageDes = "e:\\";
		}
		if (args.length > 1 && args[1] != null) {
			Configure.deviceId = args[1];
			AdbUtils.deviceId = args[1];
			AdbUtils.storageDir = AdbUtils.storageDes + args[1] + "/";
			AdbUtils.adb = "adb -s " + args[1] + " shell ";
		} else {
			Configure.deviceId = "84549a97";
			AdbUtils.deviceId = "84549a97";
			AdbUtils.storageDir = AdbUtils.storageDes + "84549a97" + "/";
			AdbUtils.adb = "adb -s " + "84549a97" + " shell ";
		}
		if (args.length > 2 && args[2] != null) {
			Configure.appiumPort = args[2];
		} else {
			Configure.appiumPort = "4731";
		}

		if (args.length > 3 && args[3] != null) {
			if ("-1".equals(args[3])) {
				MiZhuan.getInstance().isGetInstallCount = true;
			} else {
				MiZhuan.getInstance().isGetInstallCount = false;
				Configure.Mizhuan_instlal_count = Integer.parseInt(args[3]);
			}
		} else {
			MiZhuan.getInstance().isGetInstallCount = true;
			Configure.Mizhuan_instlal_count = 3;
		}

		if (args.length > 4 && args[4] != null) {
			Configure.appConfig = Integer.parseInt(args[4]);
		} else {
			Configure.appConfig = 1;
		}
		
		if (args.length > 5 && args[5] != null) {
			Configure.mizhuanStartTime = Integer.parseInt(args[5]);
		} else {
			Configure.mizhuanStartTime = 8;
		}

		init();
		// MiZhuan.getInstance().checkAppList();
		// MiZhuan.getInstance().checkAllApp();
		Timer t = new Timer();
		t.schedule(new Task1(), 1000);
	}

	private static void init() {
		if ((Configure.appConfig & 1) == 1) {
			MiZhuan.getInstance().reset();
		}
		if ((Configure.appConfig >> 1 & 1) == 1) {
			MeiRiZhuanDian.getInstance().reset();
		}
		if ((Configure.appConfig >> 2 & 1) == 1) {
			News.getInstance().reset();
		}
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
					Log.log.info("mizhuan onSuccess");
					MiZhuan.getInstance().isCompleted = true;
					if (((Configure.appConfig >> 1) & 1) == 0 && ((Configure.appConfig >> 2) & 1) == 0) {
						MiZhuan.getInstance().reset();
					}
					checkWhenRestartApp(driver);
				}

				@Override
				public void onRestartApp(AndroidDriver driver) {
					checkWhenRestartApp(driver);
				}
			});
		} else if (!MeiRiZhuanDian.getInstance().isCompleted) {
			MeiRiZhuanDian.getInstance().start(new TaskCallback() {

				@Override
				public void onSuccess(AndroidDriver driver) {
					Log.log.info("meizhuan onSuccess");
					MeiRiZhuanDian.getInstance().isCompleted = true;
					if (((Configure.appConfig >> 2) & 1) == 0) {
						if ((Configure.appConfig & 1) == 1) {
							MiZhuan.getInstance().reset();
						}
						if ((Configure.appConfig >> 1 & 1) == 1) {
							MeiRiZhuanDian.getInstance().reset();
						}
					}

					checkWhenRestartApp(driver);
				}

				@Override
				public void onRestartApp(AndroidDriver driver) {
					checkWhenRestartApp(driver);
				}
			});
		} else if (!News.getInstance().isCompleted) {
			News.getInstance().start(new TaskCallback() {

				@Override
				public void onSuccess(AndroidDriver driver) {
					Log.log.info("News onSuccess");
					News.getInstance().isCompleted = true;
					if (((Configure.appConfig >> 3) & 1) == 0) {
						if ((Configure.appConfig & 1) == 1) {
							MiZhuan.getInstance().reset();
						}
						if ((Configure.appConfig >> 1 & 1) == 1) {
							MeiRiZhuanDian.getInstance().reset();
						}
						if ((Configure.appConfig >> 2 & 1) == 1) {
							News.getInstance().reset();
						}
					}
					checkWhenRestartApp(driver);
				}

				@Override
				public void onRestartApp(AndroidDriver driver) {
					checkWhenRestartApp(driver);
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
			run();
		}
	}

	private void checkWhenRestartApp(AndroidDriver driver) {
		try {
			if (isElementExistByString(driver, "确定")) {
				driver.findElement(By.name("确定")).click();
			}
			if (isElementExistByString(driver, "允许")) {
				driver.findElement(By.name("允许")).click();
			}
			AdbUtils.setScreenTimeout(1800 * 1000);
			driver.quit();
		} catch (Exception e) {
			restartApp();
			return;
		}
		restartApp();
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

	private void resetMizhuan() {
		MiZhuan.getInstance().isCompleted = false;
		MiZhuan.getInstance().isExtraBonusCompleted = false;
		// MiZhuan.getInstance().isInstallCompleted = false;
	}

	private void resetMeizhuan() {
		MeiRiZhuanDian.getInstance().isCompleted = false;
		MeiRiZhuanDian.getInstance().isExtraBonusCompleted = false;
		MeiRiZhuanDian.getInstance().isLooklookCompleted = false;
		MeiRiZhuanDian.getInstance().isReadNewsCompleted = false;
	}
}
