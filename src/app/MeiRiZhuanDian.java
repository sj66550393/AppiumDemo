package app;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import callback.TaskCallback;
import common.Configure;
import common.ResultDict;
import io.appium.java_client.android.AndroidDriver;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;
import utils.AdbUtils;
import utils.Log;

public class MeiRiZhuanDian {
	private static MeiRiZhuanDian meiRiZhuanDian;
	public boolean isCompleted = false;
	public boolean isExtraBonusCompleted = false;
	public boolean isLooklookCompleted = false;
	public boolean isInstallCompleted = true;
	public boolean isReadNewsCompleted = false;
	private int DEFAULT_INSTALL_COUNT = 10;
	private int currentNewsCount = 0;
	private int choujiangji = 0; // 娆箰鎶藉鏈�
	private int yangshen = 0; // 鍏荤敓涔嬮亾
	private int paihongbao = 0; // 鍏ㄦ皯娲剧孩鍖�
	private int wajinkuang = 0; //
	private int quanjiatong = 0;
	private int xianjinghongbao = 0;
	private int dahuayule = 0;
	private int yingdajiang = 0;

	AndroidDriver driver;
	DesiredCapabilities capabilities;

	private MeiRiZhuanDian() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "com.adsmobile.mrzd");
		capabilities.setCapability("appActivity", ".ui.activity.MrzdAdSplashActivity");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
	}

	public static MeiRiZhuanDian getInstance() {
		if (meiRiZhuanDian == null) {
			meiRiZhuanDian = new MeiRiZhuanDian();
		}
		return meiRiZhuanDian;
	}

	public void start(TaskCallback callback) {
		try {
			System.out.println("start");
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort + "/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}
		int result = ResultDict.COMMAND_SUCCESS;

		// 棣栭〉寮规
		if (isElementExistById("com.adsmobile.mrzd:id/window_image_dismiss")) {
			driver.findElement(By.id("com.adsmobile.mrzd:id/window_image_dismiss")).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				callback.onRestartApp(driver);
				return;
			}
		}

		// 鍗囩骇寮规
		try {
			Thread.sleep(3000);
			if (isElementExistByString("鐙犲績鎷掔粷")) {
				driver.findElement(By.name("鐙犲績鎷掔粷")).click();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}

		if (!isExtraBonusCompleted) {
			Log.log.info("寮�濮嬮澶栦换鍔�");
			result = startSigninAppTask();
			if (ResultDict.COMMAND_SUCCESS != result) {
				callback.onRestartApp(driver);
				return;
			}
		}
		if (!isInstallCompleted) {
			result = installAndGetPackage();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
				return;
			}
		}
		if (!isLooklookCompleted) {
			result = startLooklookTask();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
				return;
			}
		}

		if (!isReadNewsCompleted) {
			result = startNews();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
				return;
			}
		}
		callback.onSuccess(driver);
	}

	private int installAndGetPackage() {
		int result = ResultDict.COMMAND_SUCCESS;
		try {
			while (true) {
				// 鑾峰彇鏄惁宸茬粡棰嗗彇绾㈠寘
				driver.findElement(By.name("姣忔棩绾㈠寘")).click();
				Thread.sleep(2000);
				if (isElementExistById("com.adsmobile.mrzd:id/share_red_packed_tips")) {
					String text = driver.findElement(By.id("com.adsmobile.mrzd:id/share_red_packed_tips")).getText();
					System.out.println("text = " + text);
					if("缁х画璧氶挶鍚э綖".equals(text)) {
						// 鏈兘棰嗗彇绾㈠寘
						System.out.println("鏈兘棰嗗彇绾㈠寘");
						driver.findElement(By.id("com.adsmobile.mrzd:id/open_red_packed_close")).click();
						System.out.println("click close");
						Thread.sleep(2000);
						result = installApp(1);
						if (result == ResultDict.COMMAND_RESTART_APP) {
							return ResultDict.COMMAND_RESTART_APP;
						} else if (result == ResultDict.COMMAND_MEIZHUAN_INSTALL_NOAPP) {
							isInstallCompleted = true;
							break;
						}
						AdbUtils.back();
					}else if("鎭枩鍙戣储,澶у悏澶у埄".equals(text)) {
						System.out.println("鎭枩鍙戣储,澶у悏澶у埄");
						driver.findElement(By.id("com.adsmobile.mrzd:id/share_red_packed_open")).click();
						System.out.println("click close");
						Thread.sleep(2000);
						driver.findElement(By.id("com.adsmobile.mrzd:id/open_red_packed_close")).click();
						isInstallCompleted = true;
						break;
					}
				}else {
					// 宸查鍙栫孩鍖�
					System.out.println("宸查鍙栫孩鍖�");
					isInstallCompleted = true;
					driver.findElement(By.id("com.adsmobile.mrzd:id/open_red_packed_openrl_close"));
					System.out.println("click close");
					break;
				}
			}
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int installApp(int count) {
		try {
			int result = ResultDict.COMMAND_SUCCESS;
			driver.findElement(By.name("蹇�熶换鍔�")).click();
			Thread.sleep(5000);
			driver.findElement(By.name("鍦ㄧ嚎浠诲姟")).click();
			Thread.sleep(1000);
			int installCount = 0;
			while (installCount < count) {
				if (isElementExistById("com.adsmobile.mrzd:id/txt_status")) {
					driver.findElement(By.id("com.adsmobile.mrzd:id/limit_item")).click();
					Thread.sleep(2000);
					if (isElementExistByString("寮�濮嬩换鍔�")) {
						driver.findElement(By.name("寮�濮嬩换鍔�")).click();
						Thread.sleep(60 * 1000);
						switch (Configure.productModel) {
						case "[OPPO A37m]":
							result = installApp_OPPO(driver);
							break;
						case "[CUN-TL00]":
							result = installApp_CUN_TL(driver);
							break;
						case "[Lenovo TB3-X70N]":
							break;
						case "[CUN-AL00]":
							result = installApp_CUN_AL(driver);
							break;
						default:
							break;
						}
						if (result != ResultDict.COMMAND_SUCCESS) {
							return result;
						} else {
							AdbUtils.back();
							installCount++;
							Thread.sleep(2000);
							if (isElementExistByString("涓嶄簡")) {
								driver.findElement(By.name("涓嶄簡")).click();
								Thread.sleep(1000);
							}
						}
					} else {
						return ResultDict.COMMAND_RESTART_APP;
					}
				} else if (isElementExistById("com.adsmobile.mrzd:id/image_done")) {
					return ResultDict.COMMAND_MEIZHUAN_INSTALL_NOAPP;
				} else {
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
		} catch (Exception e) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		return ResultDict.COMMAND_SUCCESS;
	}

	private int installApp_CUN_AL(AndroidDriver driver2) {

		return 0;
	}

	private int installApp_CUN_TL(AndroidDriver driver2) {
		try {
			driver.findElement(By.name("瀹夎")).click();
			Thread.sleep(60 * 1000);
			while (AdbUtils.getCurrentPackage().equals("packageinstaller")) {
				AdbUtils.back();
				Thread.sleep(1000);
			}
			AdbUtils.back();
			Thread.sleep(3 * 1000);
			WebElement buttomButton1 = driver.findElement(By.name("鎵撳紑"));
			buttomButton1.click();
			Thread.sleep(10 * 1000);
			Log.log.info("寮�濮嬩綋楠�5鍒嗛挓銆傘�傘��");
			Thread.sleep(5 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int installApp_OPPO(AndroidDriver driver2) {
		try {
			driver.findElement(By.name("瀹夎")).click();
			Thread.sleep(30 * 1000);
			driver.findElement(By.name("瀹屾垚")).click();
			Thread.sleep(3000);
			driver.findElement(By.name("閲嶆柊浣撻獙")).click();
			for (int j = 0; j < 5; j++) {
				if (driver.getPageSource().contains("同鍚屾剰骞剁户缁�")) {
					driver.findElement(By.name("同鍚屾剰骞剁户缁�")).click();
					Thread.sleep(2000);
				}
			}
			Log.log.info("寮�濮嬩綋楠�5鍒嗛挓銆傘�傘��");
			Thread.sleep(5 * 1000);
			AdbUtils.killProcess(AdbUtils.getCurrentPackage());
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int startSigninAppTask() {
		try {
			Thread.sleep(2000);
			driver.findElement(By.name("蹇�熶换鍔�")).click();
			Thread.sleep(1000);
			driver.findElement(By.name("杞欢绛惧埌")).click();
			Thread.sleep(1000);
			String lastPackage = "";
			boolean isFirst = true;
			String taskAppName = "";
			String taskTime = "1";
			while (true) {
				AdbUtils.swipe(300, 500, 300, 1000);
				Thread.sleep(5000);
				// 棰濆濂栧姳瀹屾垚
				if (isElementExistByString("鍘诲仛浠诲姟")) {
					isExtraBonusCompleted = true;
					AdbUtils.back();
					break;
				}
				taskAppName = driver.findElement(By.xpath(
						"//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
						.getText();
				taskTime = driver.findElement(By.xpath(
						"//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.TextView[contains(@index,2)]"))
						.getText();
				int experienceTime = Integer.parseInt(taskTime.substring(2, 3));
				System.out.println("appName = " + taskAppName + "    experienceTime = " + experienceTime);
				String packageName = Configure.map.get(taskAppName);
				if (packageName != null) {
					AdbUtils.rootComandEnablePackage(packageName);
					driver.findElement(By.xpath(
							"//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.LinearLayout"))
							.click();
				} else {
					isExtraBonusCompleted = true;
					break;
				}
				// String secondAppName = driver
				// .findElement(By
				// .xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout[contains(@index,1)]/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
				// .getText();
				Thread.sleep(experienceTime * 70 * 1000);
				String name = AdbUtils.getCurrentPackage();
				AdbUtils.killProcess(name);
				Thread.sleep(3000);
				if ((!"".equals(lastPackage)) && (!lastPackage.equals(name))) {
					AdbUtils.rootComandDisablePackage(lastPackage);
				}
				lastPackage = name;
				Log.log.info("kill " + name);
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
		return ResultDict.COMMAND_SUCCESS;
	}

	private int startLooklookTask() {
		try {
			Thread.sleep(2000);
			driver.findElement(By.name("绠�鍗曡禋閽�")).click();
			Thread.sleep(4000);
			startAds("娆箰鎶藉鏈�");
			startAds("鍏ㄦ皯娲剧孩鍖�");
			startAds("璁╃孩鍖呴");
			startAds("绂忓埄澶ф斁閫�");
			startAds("鍏荤敓涔嬮亾");
			startAds("宸ㄥご鏉�");
			startAds("浠婃棩瀹滄姠绾㈠寘");
			startAds("缈荤墝璧㈠ぇ濂�");
			startAds("鎶界幇閲戠孩鍖�");
			startAds("绾㈠寘鏈�鐪熸儏");
			startAds("濞变箰鍦堢闂�");
			startAds("澶╅檷绾㈠寘");
			startAds("鍋ュ悍鍏荤敓");
			System.out.println("lookAds end");
			Thread.sleep(3000);
			AdbUtils.back();
			isLooklookCompleted = true;
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int startNews() {
		try {
			driver.findElement(By.name("闃呰璧氶挶")).click();
			Thread.sleep(4000);
			int count = 0;
			while (true) {
				String bottomText = driver.findElement(By.id("com.adsmobile.mrzd:id/new_news_task_surplus")).getText();
				if(bottomText.contains("浠婃棩鍓╀綑:0娆�")){
					isReadNewsCompleted = true;
					break;
				}
				if (count == 6) {
					return ResultDict.COMMAND_RESTART_APP;
				}
				if (!isElementExistByString("鏂伴椈闃呰")) {
					return ResultDict.COMMAND_RESTART_APP;
				}
				AdbUtils.click(300, 600);
				Thread.sleep(18 * 1000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				if (!isElementExistByString("鏂伴椈闃呰")) {
					AdbUtils.killProcess(AdbUtils.getCurrentPackage());
					Thread.sleep(3000);
				} else {
					AdbUtils.back();
					currentNewsCount++;
					Thread.sleep(80 * 1000);
				}
				AdbUtils.swipe(300, 1100, 300, 500);
				count++;
			}
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private void startAds(String name) {
		try {
			while (true) {
				driver.findElement(By.name(name)).click();
				Thread.sleep(2000);
				String str = driver.findElement(By.id("com.adsmobile.mrzd:id/news_api_task_surplus")).getText();
				if (str.contains("宸插畬鎴�")) {
					driver.findElement(By.name("鍏抽棴")).click();
					Thread.sleep(3000);
					return;
				}
				Thread.sleep(20 * 1000);
				driver.findElement(By.name("鍏抽棴")).click();
				Thread.sleep(5000);
			}
		} catch (Exception e) {
		}
	}

	private boolean isElementExist() {
		try {
			WebElement element = driver.findElement(By.id("com.huawei.systemmanager:id/btn_allow"));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isElementExistByString(String name) {
		try {
			WebElement element = driver.findElement(By.name(name));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isElementExistByXpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isElementExistById(String id) {
		try {
			WebElement element = driver.findElement(By.id(id));
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
