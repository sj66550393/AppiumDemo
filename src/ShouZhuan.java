import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import app.MiZhuan;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import utils.AdbUtils;
import utils.Log;
import common.Configure;
import common.ResultDict;

public class ShouZhuan {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		init();
		Timer t = new Timer();
		t.schedule(new Task1(), 1000);
	}

	private static void init() {
		Configure.isPad = AdbUtils.isPad();
		String productModel = AdbUtils.getProductModel();
		Configure.productModel = productModel;
	}
}

class Task1 extends TimerTask {
	private MiZhuan mizhuan;
	ExecutorService fixedThreadPool;

	public Task1() {
		mizhuan = new MiZhuan();
		fixedThreadPool = Executors.newFixedThreadPool(4);
	}

	@Override
	public void run() {
		switch (mizhuan.start()) {
		case ResultDict.COMMAND_RESTART_APP:
			fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					Log.log.info("restart app");
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
					restartApp();
				}
			});

			break;
		case ResultDict.COMMAND_SUCCESS:
			Log.log.info("success");
			break;
		default:
			break;
		}
	}

	public void restartApp() {
		try {
			while (!AdbUtils.getCurrentPackage().contains("launcher")) {
				AdbUtils.killProcess(AdbUtils.getCurrentPackage());
			}
			Thread.sleep(3000);
//			AdbUtils.startActivity("me.mizhuan/.ActCover");
//			Thread.sleep(30000);
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
