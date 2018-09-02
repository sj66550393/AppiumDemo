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
	public boolean isCompleted = true;
	public boolean isExtraBonusCompleted = true;
	public boolean isLooklookCompleted = false;
	public boolean isInstallCompleted = false;
	public boolean isReadNewsCompleted = false;
	private int DEFAULT_INSTALL_COUNT = 10;
	private int currentNewsCount = 0;
	private int choujiangji = 0;
	private int yangshen = 0;
	private int paihongbao = 0;
	private int wajinkuang = 0;
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

		// 
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

		// 升级
		try {
			Thread.sleep(3000);
			if (isElementExistByString("狠心拒绝")) {
				driver.findElement(By.name("狠心拒绝")).click();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}

		if (!isExtraBonusCompleted) {
			Log.log.info("开始额外任务");
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

		if (!isReadNewsCompleted && Configure.appConfig >> 2 == 0) {
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

				driver.findElement(By.name("每日红包")).click();
				Thread.sleep(2000);
				if (isElementExistById("com.adsmobile.mrzd:id/share_red_packed_tips")) {
					String text = driver.findElement(By.id("com.adsmobile.mrzd:id/share_red_packed_tips")).getText();
					System.out.println("text = " + text);
					if ("继续赚钱吧～".equals(text)) {

						System.out.println("未能领取红包");
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
					} else if ("恭喜发财,大吉大利".equals(text)) {
						System.out.println("恭喜发财,大吉大利");
						driver.findElement(By.id("com.adsmobile.mrzd:id/share_red_packed_open")).click();
						System.out.println("click close");
						Thread.sleep(2000);
						driver.findElement(By.id("com.adsmobile.mrzd:id/open_red_packed_close")).click();
						isInstallCompleted = true;
						break;
					}
				} else {
					// 已领取红包
					System.out.println("已领取红包");
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
			driver.findElement(By.name("快速任务")).click();
			Thread.sleep(5000);
			driver.findElement(By.name("在线任务")).click();
			Thread.sleep(1000);
			int installCount = 0;
			while (installCount < count) {
				if (isElementExistById("com.adsmobile.mrzd:id/txt_status")) {
					driver.findElement(By.id("com.adsmobile.mrzd:id/limit_item")).click();
					Thread.sleep(2000);
					if (isElementExistByString("开始任务")) {
						driver.findElement(By.name("开始任务")).click();
						Thread.sleep(60 * 1000);
						System.out.println("product model = " + Configure.productModel);
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
						case "[M5]":
							result = installApp_M5(driver);
							break;
						default:
							break;
						}
						if (result != ResultDict.COMMAND_SUCCESS) {
							return result;
						} else {
							installCount++;
							Log.log.info("kill " + AdbUtils.getCurrentPackage());
							AdbUtils.killProcess(AdbUtils.getCurrentPackage());
							Thread.sleep(2000);
							if (isElementExistByString("不了")) {
								driver.findElement(By.name("不了")).click();
								Thread.sleep(1000);
							}
							AdbUtils.back();
							Thread.sleep(2000);
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

	private int installApp_M5(AndroidDriver driver2) {
		try {
			while (true) {
				WebElement installButton = driver.findElement(By.id("com.android.packageinstaller:id/action_positive"));
				if ("下一步".equals(installButton.getText())) {
					installButton.click();
					Thread.sleep(1000);
				} else {
					installButton.click();
					Thread.sleep(1000);
					break;
				}
			}
			Thread.sleep(10 * 1000);
			Log.log.info("开始体验5分钟");
			Thread.sleep(60 * 1000);
			while (AdbUtils.getCurrentPackage().contains("packageinstaller")) {
				AdbUtils.back();
				Thread.sleep(1000);
			}
			Thread.sleep(3 * 1000);
			WebElement buttomButton1 = driver.findElement(By.name("打开"));
			buttomButton1.click();
			Thread.sleep(6 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int installApp_CUN_AL(AndroidDriver driver2) {
		try {
			while (true) {
				WebElement installButton = driver.findElement(By.id("com.android.packageinstaller:id/ok_button"));
				if ("下一步".equals(installButton.getText())) {
					installButton.click();
					Thread.sleep(1000);
				} else {
					installButton.click();
					Thread.sleep(1000);
					break;
				}
			}
			Thread.sleep(60 * 1000);
			while (AdbUtils.getCurrentPackage().contains("packageinstaller")) {
				AdbUtils.back();
				Thread.sleep(1000);
			}
			Thread.sleep(3 * 1000);
			WebElement buttomButton1 = driver.findElement(By.name("打开"));
			buttomButton1.click();
			Log.log.info("开始体验5分钟");
			Thread.sleep(6 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int installApp_CUN_TL(AndroidDriver driver2) {
		try {
			driver.findElement(By.name("安装")).click();
			Thread.sleep(60 * 1000);
			while (AdbUtils.getCurrentPackage().contains("packageinstaller")) {
				AdbUtils.back();
				Thread.sleep(1000);
			}
			Thread.sleep(3 * 1000);
			WebElement buttomButton1 = driver.findElement(By.name("打开"));
			buttomButton1.click();
			Log.log.info("开始体验5分钟。。。");
			Thread.sleep(6 * 70 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int installApp_OPPO(AndroidDriver driver2) {
		try {
			driver.findElement(By.name("安装")).click();
			Thread.sleep(30 * 1000);
			driver.findElement(By.name("完成")).click();
			Thread.sleep(3000);
			driver.findElement(By.name("打开")).click();
			for (int j = 0; j < 5; j++) {
				if (driver.getPageSource().contains("同意并继续")) {
					driver.findElement(By.name("同意并继续")).click();
					Thread.sleep(2000);
				}
			}
			Log.log.info("开始体验5分钟。。。");
			Thread.sleep(6 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int startSigninAppTask() {
		try {
			Thread.sleep(2000);
			driver.findElement(By.name("快速任务")).click();
			Thread.sleep(1000);
			driver.findElement(By.name("软件签到")).click();
			Thread.sleep(1000);
			String lastPackage = "";
			boolean isFirst = true;
			String taskAppName = "";
			String taskTime = "1";
			while (true) {
				AdbUtils.swipe(300, 500, 300, 1000);
				Thread.sleep(5000);
				// 额外奖励完成
				if (isElementExistByString("去做任务")) {
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
					Log.log.info("taskAppName = " + taskAppName + "can not find packageName");
					isExtraBonusCompleted = true;
					break;
				}
				// String secondAppName = driver
				// .findElement(By
				// .xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.ListView/android.widget.LinearLayout/android.widget.RelativeLayout[contains(@index,1)]/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
				// .getText();
				Thread.sleep((experienceTime+1) * 70 * 1000);
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
			driver.findElement(By.name("简单赚钱")).click();
			Thread.sleep(4000);
			startAds("欢乐抽奖机");
			startAds("全民派红包");
			startAds("让红包飞");
			startAds("福利大放送");
			startAds("养生之道");
//			startAds("巨头条");
			startAds("今日宜抢红包");
			startAds("翻牌赢大奖");
			startAds("抽现金红包");
			startAds("红包最真情");
//			startAds("娱乐圈秘闻");
			startAds("天降红包");
			startAds("健康养生");
			startAds("优惠全家桶");
			startAds("娱乐一夏");
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
			driver.findElement(By.name("阅读赚钱")).click();
			Thread.sleep(4000);
			int count = 0;
			while (true) {
				String bottomText = driver.findElement(By.id("com.adsmobile.mrzd:id/new_news_task_surplus")).getText();
				if (bottomText.contains("今日剩余:0次")) {
					isReadNewsCompleted = true;
					break;
				}
				if (count == 6) {
					return ResultDict.COMMAND_RESTART_APP;
				}
				if (!isElementExistByString("新闻阅读")) {
					return ResultDict.COMMAND_RESTART_APP;
				}
				AdbUtils.click(300, 600);
				Thread.sleep(18 * 1000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				if (!isElementExistByString("新闻阅读")) {
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
				if (str.contains("已完成")) {
					driver.findElement(By.name("关闭")).click();
					Thread.sleep(3000);
					return;
				}
				Thread.sleep(20 * 1000);
				driver.findElement(By.name("关闭")).click();
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

	public void reset() {
		Log.log.info("meizhuan reset");
		isCompleted = false;
		isExtraBonusCompleted = false;
		isLooklookCompleted = false;
		isReadNewsCompleted = false;
	}

}
