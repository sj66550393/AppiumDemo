package app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import callback.TaskCallback;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;
import utils.DateUtils;
import utils.Log;
import utils.AdbUtils;
import utils.SwipeScreen;
import common.Configure;
import common.Contants;
import common.ResultDict;

public class MiZhuan {

	private static MiZhuan mizhuan;

	AndroidDriver driver;
	DesiredCapabilities capabilities;
	private int redPackageNum = 0; // 拆红包
	private int todayMustNum = 0; // 今日必看
	private int entertainmentNews = 0; // 娱乐爆料
	private int ThreeSixZeroNewsNum = 0; // 360新闻
	private int HotNewsNum = 0; // 热点新闻
	private int turnturnNum = 0; // 翻翻乐
	private int tuituiNum = 0; // 推推乐
	private int goldNewsNum = 0; // 点金头条
	private int eighteenNum = 0; // 18头条
	private int loveNewsNum = 0; // 我爱头条
	private int DEFAULT_EXTRABONUS_TIME = 1;
	private int INSTALL_EXPERIWNCE_TIME = 5;
	private boolean isLooklookCompleted = true;
	private boolean isSigninCompleted = false;
	private boolean isSigninMorning = false;
	private boolean isSigninNoon = false;
	private boolean isSigninAfternoon = false;
	private boolean isSigninNight = false;
	private boolean is360Completed = true;
	private boolean isTuituiComleted = true;
	private boolean isTurnturnComleted = true;
	private boolean isPackageCompleted = true;

	public boolean isCompleted = true;
	public boolean isGetInstallCount = false;
	public boolean isInstallNoApp = false;
	private int installCount = 0;
	public boolean isExtraBonusCompleted = false;
	public boolean isInstallCompleted = false;
	private boolean isClickAdsCompleted = true;

	ExtraBonusManager extraBonusManager;
	LooklookManager looklookManager;
	InstallAppManager installAppManager;
	SigninManager signinManager;

	private MiZhuan() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "me.mizhuan");
		capabilities.setCapability("appActivity", ".ActCover");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("udid", Configure.deviceId);
		extraBonusManager = new ExtraBonusManager(driver);
		looklookManager = new LooklookManager(driver);
		installAppManager = new InstallAppManager(driver);
		signinManager = new SigninManager(driver);
	}

	public static MiZhuan getInstance() {
		if (mizhuan == null) {
			mizhuan = new MiZhuan();
		}
		return mizhuan;
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
		if (DateUtils.getHour() == 1) {
			isExtraBonusCompleted = false;
			isLooklookCompleted = false;
			isSigninCompleted = false;
			isSigninMorning = false;
			isSigninNoon = false;
			isSigninAfternoon = false;
			isSigninNight = false;
		}
		int result = ResultDict.COMMAND_SUCCESS;
		if (isElementExistById("me.mizhuan:id/close")) {
			driver.findElement(By.id("me.mizhuan:id/close")).click();
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
				callback.onRestartApp(driver);
				return;
			}
		}

		try {
			driver.findElement(By.id("me.mizhuan:id/left_back")).click();
			Thread.sleep(2000);
			if (isElementExistById("me.mizhuan:id/check_btn")) {
				driver.findElement(By.id("me.mizhuan:id/check_btn")).click();
			}
			Thread.sleep(3000);
			AdbUtils.back();
		} catch (Exception e) {
			e.printStackTrace();
			callback.onRestartApp(driver);
			return;
		}

		// if (isElementExistByString("签到")) {
		// try {
		// driver.findElement(By.name("签到")).click();
		// Thread.sleep(5000);
		// driver.findElement(By.id("me.mizhuan:id/btnaction_one")).click();
		// Thread.sleep(5000);
		// AdbUtils.back();
		// Thread.sleep(2000);
		// AdbUtils.back();
		// } catch (Exception e) {
		// e.printStackTrace();
		// callback.onRestartApp(driver);
		// return;
		// }
		// }

		// if (isElementExistByString("上午探班") || isElementExistByString("中午探班")
		// || isElementExistByString("下午探班")
		// || isElementExistByString("晚上探班")) {
		// try {
		// Log.log.info("点击探班");
		// //
		// driver.findElement(By.xpath("//android.widget.ListView/android.widget.LinearLayout/android.view.View/android.widget.LinearLayout[contains(@index,0)]")).click();
		// if (isElementExistByString("上午探班")) {
		// driver.findElement(By.name("上午探班")).click();
		// isSigninMorning = true;
		// Thread.sleep(4000);
		// AdbUtils.back();
		// } else if (isElementExistByString("中午探班")) {
		// driver.findElement(By.name("中午探班")).click();
		// isSigninNoon = true;
		// Thread.sleep(4000);
		// AdbUtils.back();
		// } else if (isElementExistByString("下午探班")) {
		// driver.findElement(By.name("下午探班")).click();
		// isSigninAfternoon = true;
		// Thread.sleep(4000);
		// AdbUtils.back();
		// } else if (isElementExistByString("晚上探班")) {
		// driver.findElement(By.name("晚上探班")).click();
		// isSigninNight = true;
		// Thread.sleep(4000);
		// AdbUtils.back();
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// callback.onRestartApp(driver);
		// return;
		// }
		// }
		 if (!isExtraBonusCompleted) {
		 Log.log.info("开始额外任务");
		 result = startSigninAppTask();
		 if (ResultDict.COMMAND_SUCCESS != result) {
		 callback.onRestartApp(driver);
		 return;
		 }
		 }
		// if (!isClickAdsCompleted) {
		// Log.log.info("开始看广告任务");
		// result = startClickAds();
		// if (ResultDict.COMMAND_SUCCESS != result) {
		// callback.onRestartApp(driver);
		// return;
		// }
		// }
		if (!isInstallCompleted) {
			Log.log.info("开始安装任务");
			result = universalInstall();
			if (result != ResultDict.COMMAND_SUCCESS) {
				callback.onRestartApp(driver);
				return;
			}
		}
		if (!isLooklookCompleted) {
			result = startLooklookTaskFromBottomGame();
			if (ResultDict.COMMAND_SUCCESS != result) {
				callback.onRestartApp(driver);
				return;
			}
		}
		callback.onSuccess(driver);
	}

	private int startLooklookTaskFromBottomGame() {

		try {
			Log.log.info("点击游戏赚");
			driver.findElement(By.name("游戏赚")).click();
			Thread.sleep(8000);
			for (int i = 0; i < 5; i++) {
				AdbUtils.swipe(500, 700, 500, 300);
				Thread.sleep(1000);
			}
			// if (!clickEntertainmentNews()) {
			// return ResultDict.COMMAND_RESTART_APP;
			// }
			if (!is360Completed) {
				if (!clickThreeSixZeroNews()) {
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			if (!isTurnturnComleted) {
				if (!clickTurnturn()) {
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			if (!isTuituiComleted) {
				if (!clickTuitui()) {
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			if (!isPackageCompleted) {
				if (!clickRedPackage()) {
					return ResultDict.COMMAND_RESTART_APP;
				}
			}
			// if (!clickGoldNews()) {
			// driver.quit();
			// return ResultDict.COMMAND_RESTART_APP;
			// }
			// if (!clickEighteenNews()) {
			// driver.quit();
			// return ResultDict.COMMAND_RESTART_APP;
			// }
			// if (!clickLoveNews()) {
			// driver.quit();
			// return ResultDict.COMMAND_RESTART_APP;
			// }
			driver.quit();
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private void getInstallCount() {
		try {
			driver.findElement(By.name("推荐")).click();
			Thread.sleep(5000);
			driver.findElement(By.name("领奖励")).click();
			Thread.sleep(2000);
			String str = driver.findElement(By.id("me.mizhuan:id/status")).getText();
			if ("领取".equals(str)) {
				driver.findElement(By.id("me.mizhuan:id/status")).click();
				Thread.sleep(5000);
				AdbUtils.back();
				isGetInstallCount = false;
				isInstallCompleted = true;
			} else if ("已领取".equals(str)) {
				isGetInstallCount = false;
				isInstallCompleted = true;
			} else {
				str = str.split("/")[1];
				Configure.Mizhuan_instlal_count = Integer.parseInt(str);
				System.out.println("install count = " + str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
			AdbUtils.back();
		} catch (Exception e) {
		}
	}

	private void getBonus() {
		try {
			driver.findElement(By.name("领奖励")).click();
			Thread.sleep(2000);
			driver.findElement(By.name("领取")).click();
			Thread.sleep(5000);
			AdbUtils.back();
			Thread.sleep(2000);
			AdbUtils.back();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������
	public boolean clickTuitui() {
		try {
			Thread.sleep(3000);
			if (isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				isTuituiComleted = true;
				return true;
			}
			if (!isElementExistByString("推推乐")) {
				return false;
			}
			for (; tuituiNum < Contants.TUITUI_NUM; tuituiNum++) {
				if (timeSwitcher() != ResultDict.COMMAND_SUCCESS) {
					return false;
				}
				driver.findElement(By.name("推推乐")).click();
				Thread.sleep(10 * 1000);
				if (isElementExistByString("今日任务已完成")) {
					AdbUtils.back();
					return true;
				}
				driver.findElement(By.name("返回")).click();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// ������
	public boolean clickTurnturn() {
		try {
			Thread.sleep(3000);
			if (isElementExistByString("֪知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("֪知道了")).click();
				isTurnturnComleted = true;
				return true;
			}
			if (!isElementExistByString("翻翻乐")) {
				return true;
			}
			for (; tuituiNum < Contants.TURNTURN_NUM; tuituiNum++) {
				if (timeSwitcher() != ResultDict.COMMAND_SUCCESS) {
					return false;
				}
				driver.findElement(By.name("翻翻乐")).click();
				Thread.sleep(10 * 1000);
				if (isElementExistByString("今日任务已完成")) {
					AdbUtils.back();
					AdbUtils.back();
					return true;
				}
				AdbUtils.back();
				AdbUtils.back();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 拆红包
	public boolean clickRedPackage() {
		try {
			Thread.sleep(3000);
			if (isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				isPackageCompleted = true;
				return true;
			}
			if (!isElementExistByString("拆红包")) {
				return true;
			}
			for (; redPackageNum < Contants.RED_PACKAGES_NUM; redPackageNum++) {
				if (timeSwitcher() != ResultDict.COMMAND_SUCCESS) {
					return false;
				}
				driver.findElement(By.name("拆红包")).click();
				Thread.sleep(10000);
				if (isElementExistByString("今日任务已完成")) {
					driver.findElement(By.name("返回")).click();
					return true;
				}
				driver.findElement(By.name("返回")).click();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 点金头条
	public boolean clickGoldNews() {
		try {
			AdbUtils.click(216, 695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickGoldNews()) {
				return false;
			}
			for (; goldNewsNum < Contants.GOLD_NEWS_NUM; goldNewsNum++) {
				Thread.sleep(5000);
				for (int i = 0; i < goldNewsNum; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
				}
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				if (!looklookManager.checkEnterGoldNews()) {
					AdbUtils.click(300, 800);
				}
				Thread.sleep(10000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				AdbUtils.back();
				Thread.sleep(3000);
				if (!looklookManager.checkClickGoldNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 18头条
	public boolean clickEighteenNews() {
		try {
			AdbUtils.click(216, 695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickEighteenNews()) {
				return false;
			}
			for (; eighteenNum < Contants.EIGHTEEN_NEWS_NUM; eighteenNum++) {
				Thread.sleep(5000);
				for (int i = 0; i < eighteenNum; i++) {
					AdbUtils.swipe(300, 1100, 300, 500 - 20 * i);
				}
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				AdbUtils.back();
				Thread.sleep(3000);
				if (!looklookManager.checkClickEighteenNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 我爱头条
	public boolean clickLoveNews() {
		try {
			AdbUtils.click(648, 695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickLoveNews()) {
				return false;
			}
			for (; loveNewsNum < Contants.LOVE_NEWS_NUM + 4; loveNewsNum++) {
				swipeAndClickNews2();
				if (!looklookManager.checkClickLoveNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 今日必看
	public void clickTodayMustNews() {
		// TODO
	}

	// 娱乐爆料
	public boolean clickEntertainmentNews() {
		try {
			driver.findElement(By.name("娱乐爆料"));
			Thread.sleep(2000);
			if (isElementExistByString("今日任务已完成")) {
				AdbUtils.back();
				return true;
			}
			for (; entertainmentNews < Contants.ENTERTAINMENT_NEWS; entertainmentNews++) {
				Thread.sleep(5000);
				AdbUtils.swipe(300, 1100, 300, 500);
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				if (!looklookManager.checkEnterEntertainNews()) {
					AdbUtils.click(300, 800);
					Thread.sleep(10000);
				}
				for (int i = 0; i < 10; i++) {
					AdbUtils.swipe(300, 1100, 300, 500);
					Thread.sleep(1000);
				}
				AdbUtils.back();
				Thread.sleep(3000);
				while (looklookManager.checkEnterEntertainNews()) {
					AdbUtils.back();
					Thread.sleep(8000);
				}
				if (!looklookManager.checkClickEntertainNews()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	private boolean clickThreeSixZeroNews() {
		try {
			Log.log.info("点击360新闻");
			driver.findElement(By.name("360新闻")).click();
			Thread.sleep(10000);
			if (isElementExistByString("知道了")) {
				System.out.println("find know");
				driver.findElement(By.name("知道了")).click();
				is360Completed = true;
				return true;
			}
			// if (!looklookManager.checkClick360News()) {
			// return false;
			// }
			if (isElementExistByString("今日任务已完成")) {
				AdbUtils.back();
				return true;
			}
			for (; ThreeSixZeroNewsNum < Contants.THREE_SIX_ZERO_NEWS_NUM + 3; ThreeSixZeroNewsNum++) {
				if (timeSwitcher() != ResultDict.COMMAND_SUCCESS) {
					return false;
				}
				swipeAndClickNews();
				// if (!looklookManager.checkClick360News()) {
				// return false;
				// }
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return false;
		}
	}

	// 滑动点击新闻 新闻界面停留20S
	public void swipeAndClickNews() {
		try {
			Thread.sleep(5000);
			AdbUtils.swipe(300, 1100, 300, 500);
			AdbUtils.click(300, 600);
			Thread.sleep(20000);
			AdbUtils.back();
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	// 滑动点击新闻 新闻界面滑动
	public void swipeAndClickNews2() {
		try {
			Thread.sleep(5000);
			AdbUtils.swipe(300, 1100, 300, 500);
			AdbUtils.click(300, 600);
			Thread.sleep(10000);
			for (int i = 0; i < 10; i++) {
				AdbUtils.swipe(300, 1100, 300, 500);
				Thread.sleep(1000);
			}
			AdbUtils.back();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private int startClickAds() {

		try {
			driver.findElement(By.name("推荐")).click();
			Thread.sleep(2000);
			driver.findElement(By.name("看看赚")).click();
			Thread.sleep(4000);
			driver.findElement(By.name("点广告")).click();
			Thread.sleep(2000);
			driver.findElement(By.name("开始赚钱")).click();
			Thread.sleep(2000);
			if (isElementExistByString("仅此一次")) {
				driver.findElement(By.name("仅此一次")).click();
			}
			Thread.sleep(70 * 1000);
			isClickAdsCompleted = true;
			AdbUtils.killProcess(AdbUtils.getCurrentPackage());
			Thread.sleep(8000);
			AdbUtils.back();
			Thread.sleep(1000);
			if ("点广告".equals(driver.findElement(By.id("me.mizhuan:id/title")).getText())) {
				AdbUtils.back();
			}
			Thread.sleep(2000);
			AdbUtils.back();
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			isClickAdsCompleted = true;
			return ResultDict.COMMAND_RESTART_APP;
		}
		isClickAdsCompleted = true;
		return ResultDict.COMMAND_SUCCESS;
	}

	private int startSigninAppTask() {
		try {
			String lastPackage = "";
			int appUseTime = 1;
			boolean leftSwipe = false;
			while (!((DateUtils.getHour() > 8) || ((DateUtils.getHour() == 8) && (DateUtils.getMinute() > 30)))) {
				if (leftSwipe) {
					driver.findElement(By.name("应用赚")).click();
				} else {
					driver.findElement(By.name("推荐")).click();
				}
				Thread.sleep(1 * 60 * 1000);
				leftSwipe = !leftSwipe;
			}
			// 点击应用赚
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			// 额外奖励
			driver.findElement(By.name("额外奖励")).click();
			Thread.sleep(8000);
			boolean isFirst = true;
			int position = 1;
			String taskType = "";
			String taskTime = "1";
			String buttonText = "";
			String taskName = "";
			while (true) {
				AdbUtils.swipe(300, 500, 300, 1000);
				Thread.sleep(5000);
				WebElement button;
				WebElement taskDesc;
				WebElement taskNameDesc;
				// 系统出现停止运行
				if (isElementExistByString("确定")) {
					driver.findElement(By.name("确定")).click();
					Thread.sleep(1000);
				}
				// pad 用xpath会找不到路径
				if (!AdbUtils.isRoot()
				// || Configure.isPad
				) {
					button = driver.findElement(By.id("me.mizhuan:id/mituo_status"));
					taskDesc = driver.findElement(By.id("me.mizhuan:id/mituo_textViewPromo"));
					taskNameDesc = driver.findElement(By.id("me.mizhuan:id/mituo_textViewName"));
				} else {
					button = driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
									+ position + ")]/android.widget.LinearLayout/android.widget.Button"));
					taskDesc = driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
									+ position + ")]/android.widget.TextView[contains(@index,2)]"));
					taskNameDesc = driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
									+ position + ")]/android.widget.TextView[contains(@index,1)]"));
				}
				buttonText = button.getText();
				taskType = taskDesc.getText().substring(1, 3);
				taskName = taskNameDesc.getText();
				if (taskDesc.getText().length() >= 9)
					taskTime = taskDesc.getText().substring(8, 9);
				System.out.println("buttonText = " + buttonText + "    taskType = " + taskType + "    taskName = "
						+ taskName + "    taskTime = " + taskTime);
				if ("已抢完".equals(buttonText) || "未到时间".equals(buttonText)
				// || "0万".equals(buttonText.substring(buttonText.length() - 2))
						|| "深度".equals(taskType)) {
					Log.log.info("额外任务完成");
					isExtraBonusCompleted = true;
					break;
				}
				if (Configure.isPad || !AdbUtils.isRoot()) {
					button.click();
				} else {
					String pkgName = Configure.map.get(taskName);
					if (pkgName != null) {
						System.out.println("pkgName = " + pkgName);
						AdbUtils.rootComandEnablePackage(pkgName);
						button.click();
					} else {
						position++;
						continue;
					}
				}
				Log.log.info("额外奖励开始计时。。。");
				Thread.sleep(Integer.parseInt(taskTime) * 70 * 1000);
				String name = AdbUtils.getCurrentPackage();
				if ((!"".equals(lastPackage)) && (!lastPackage.equals(name)) && AdbUtils.isRoot()) {
					AdbUtils.rootComandDisablePackage(lastPackage);
				}
				lastPackage = AdbUtils.getCurrentPackage();
				AdbUtils.killProcess(AdbUtils.getCurrentPackage());
				Log.log.info("kill " + name);
				Thread.sleep(5000);
			}
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall() {
		try {
			Thread.sleep(10000);
			// 获取安装数量
			System.out.println("isGetInstallCount = " + isGetInstallCount);
			if (isGetInstallCount) {
				driver.findElement(By.id("me.mizhuan:id/left_back")).click();
				Thread.sleep(2000);
				while (!isElementExistByString("完成3个应用赚任务")) {
					AdbUtils.swipe(300, 800, 300, 665);
				}
				AdbUtils.swipe(300, 800, 300, 665);
				Thread.sleep(1000);
				if (!isElementExistByString("领奖励")) {
					System.out.println("not exist 奖励");
					driver.findElement(By.name("完成3个应用赚任务")).click();
					Thread.sleep(1000);
					if(isElementExistByString("已完成")){
						System.out.println("exist completed");
						isInstallCompleted = true;
						return ResultDict.COMMAND_SUCCESS;
					}
					Configure.Mizhuan_instlal_count = 1;
					installCount = 0;
					AdbUtils.back();
					Thread.sleep(3000);
				} else {
					driver.findElement(By.name("领奖励")).click();
					isInstallCompleted = true;
				}
			}
			driver.findElement(By.name("推荐")).click();
			Thread.sleep(2000);
			Log.log.info("点击应用赚");
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			Log.log.info("点击应用");
			driver.findElement(By.name("应用")).click();
			Thread.sleep(2000);
			String taskType = "";
			String taskAppSize = "";
			String taskAppName = "";
			String lastAppName = "";
			int repeatCount = 0;
			while (installCount < Configure.Mizhuan_instlal_count) {
				taskType = driver.findElement(By.xpath(
						"//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				taskAppSize = driver.findElement(By.xpath(
						"//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.LinearLayout/android.widget.TextView[contains(@index,1)]"))
						.getText();
				taskAppName = driver.findElement(By.xpath(
						"//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.TextView[contains(@index,1)]"))
						.getText();
				Log.log.info("taskType = " + taskType + "   " + "    taskAppSize = " + taskAppSize + "   taskAppName = "
						+ taskAppName);

				System.out.println("taskAppName = " + taskAppName);
				System.out.println("lastAppName = " + lastAppName);
				if (taskAppName.equals(lastAppName)) {
					repeatCount++;
					System.out.println("count = " + repeatCount);
					if (repeatCount == 5) {
						isInstallCompleted = true;
						isInstallNoApp = true;
						Configure.mizhuanInstallNoAppTime = System.currentTimeMillis();
						break;
					}
				} else {
					repeatCount = 0;
				}

				double appSize = Double.parseDouble(taskAppSize.split("M")[0]);
				lastAppName = taskAppName;
				if (("注册".equals(taskType) || "体验".equals(taskType)) && appSize < 100) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton;
					if (isElementExistById("me.mizhuan:id/mituo_linearLayoutBottom")) {
						buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					} else {
						buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_btnaction_one"));
					}
					if ("立即安装".equals(buttomButton.getText())) {
						if (isElementExistById("me.mizhuan:id/mituo_rowTextOne")) {
							String str = driver.findElement(By.id("me.mizhuan:id/mituo_rowTextOne")).getText()
									.substring(0, 2);
							if (!str.equals("首次")) {
								AdbUtils.back();
								Thread.sleep(2 * 1000);
								SwipeScreen.swipe(driver, 300, 800, 300, 665);
								// SwipeScreen.swipe(driver, 300, 400, 300, 265);
								continue;
							}
						} else {
							AdbUtils.back();
							Thread.sleep(2 * 1000);
							SwipeScreen.swipe(driver, 300, 800, 300, 665);
							// SwipeScreen.swipe(driver, 300, 400, 300, 265);
							continue;
						}
						String size = driver.findElement(By.id("me.mizhuan:id/mituo_app_view1")).getText();
						double DetailAppSize = Double.parseDouble(taskAppSize.split("M")[0]);
						if (DetailAppSize > 100) {
							AdbUtils.back();
							Thread.sleep(2 * 1000);
							SwipeScreen.swipe(driver, 300, 800, 300, 665);
							// SwipeScreen.swipe(driver, 300, 400, 300, 265);
							continue;
						}
						Log.log.info("点击立即安装");
						buttomButton.click();
						Thread.sleep(3 * 1000);
						if (isElementExistByString("立即安装")) {
							AdbUtils.back();
							Thread.sleep(2 * 1000);
							SwipeScreen.swipe(driver, 300, 800, 300, 665);
							// SwipeScreen.swipe(driver, 300, 400, 300, 265);
							continue;
						}
						Thread.sleep(60 * 1000);
						while (isElementExistByString("应用详情")) {
							WebElement buttomButton1 = driver
									.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
							String buttonText = buttomButton1.getText();
							if (buttonText.substring(buttonText.length() - 1).equals("%")) {
								Thread.sleep(30 * 1000);
								continue;
							} else {
								break;
							}
						}
						int result = ResultDict.COMMAND_SUCCESS;
						switch (Configure.productModel) {
						case "[OPPO A37m]":
							result = universalInstall_OPPO(driver);
							break;
						case "[CUN-TL00]":
							result = universalInstall_CUN_TL(driver);
							break;
						case "[Lenovo TB3-X70N]":
							result = universalInstall_TB3(driver);
							break;
						case "[CUN-AL00]":
							result = universalInstall_CUN_AL(driver);
							break;
						case "[Coolpad 5270]":
							result = universalInstall_CUN_AL(driver);
							break;
						case "[M5]":
							result = universalInstall_M5(driver);
							break;
						case "[ZTE BV0701]":
							result = universalInstall_CUN_AL(driver);
							break;
						case "[M653]":
							result = universalInstall_CUN_A3S(driver);
							break;
						case "[MX4]":
							result = universalInstall_CUN_MX4(driver);
							break;
						default:
							break;
						}
						if (result != ResultDict.COMMAND_SUCCESS) {
							return result;
						} else {
							installCount++;
							Log.log.info("已安装" + installCount + "个应用");
							Log.log.info("kill " + AdbUtils.getCurrentPackage());
							AdbUtils.killProcess(AdbUtils.getCurrentPackage());
							Thread.sleep(2000);
							switch (Configure.productModel) {
							case "[OPPO A37m]":
								if (isElementExistByString("完成")) {
									driver.findElement(By.name("完成")).click();
									Thread.sleep(2000);
								}
								break;
							case "[CUN-TL00]":

								break;
							case "[Lenovo TB3-X70N]":
								break;
							case "[CUN-AL00]":
								while (isElementExistByString("打开")) {
									driver.pressKeyCode(AndroidKeyCode.BACK);
									Thread.sleep(2000);
								}
								break;
							case "[ZTE BV0701]":
								while (isElementExistByString("打开")) {
									driver.pressKeyCode(AndroidKeyCode.BACK);
									Thread.sleep(2000);
								}
								break;
							case "[M653]":
								while (isElementExistByString("打开")) {
									driver.pressKeyCode(AndroidKeyCode.BACK);
									Thread.sleep(2000);
								}
								break;
							default:
								break;
							}
						}
					} else if ("继续体验".equals(buttomButton.getText())) {
						Log.log.info("点击继续体验");
						if (AdbUtils.isRoot()) {
							String name = driver.findElement(By.id("me.mizhuan:id/mituo_tvTitle")).getText();
							if (Configure.map != null) {
								String packageName = Configure.map.get(name);
								System.out.println("packageName = " + packageName);
								;
								AdbUtils.rootComandEnablePackage(packageName);
								Thread.sleep(3000);
							}
						}
						buttomButton.click();
						Thread.sleep(20 * 1000);
						Log.log.info("开始体验5分钟。。。");
						Thread.sleep(7 * 60 * 1000);
						installCount++;
						Log.log.info("已安装" + installCount + "个应用");
						Log.log.info("kill " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
						while (isElementExistByString("打开")) {
							driver.pressKeyCode(AndroidKeyCode.BACK);
							Thread.sleep(2000);
						}
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 800, 300, 665);
			}
			Thread.sleep(2000);
			if (isGetInstallCount) {
				driver.findElement(By.id("me.mizhuan:id/left_back")).click();
				Thread.sleep(2000);
				while (!isElementExistByString("完成3个应用赚任务")) {
					AdbUtils.swipe(300, 800, 300, 665);
				}
				driver.findElement(By.name("完成3个应用赚任务")).click();
				Thread.sleep(1000);
				if (!isElementExistByString("领奖励")) {
					Configure.Mizhuan_instlal_count = 1;
					return ResultDict.COMMAND_RESTART_APP;
				} else {
					driver.findElement(By.name("领奖励")).click();
					isInstallCompleted = true;
				}
			} else {
				isInstallCompleted = true;
			}

			// if (isGetInstallCount) {
			// driver.findElement(By.name("推荐")).click();
			// Thread.sleep(5000);
			// driver.findElement(By.name("领奖励")).click();
			// Thread.sleep(2000);
			// driver.findElement(By.name("领取")).click();
			// Thread.sleep(5000);
			// } else {
			// isInstallCompleted = true;
			// }
			AdbUtils.back();
			Thread.sleep(2000);
			AdbUtils.back();
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Log.log.info(e.getMessage());
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall_TB3(AndroidDriver driver) {
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
			Thread.sleep(10 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall_OPPO(AndroidDriver driver) {
		try {
			driver.findElement(By.name("安装")).click();
			Thread.sleep(30 * 1000);
			for (int j = 0; j < 5; j++) {
				if (driver.getPageSource().contains("ͬ同意并继续")) {
					driver.findElement(By.name("ͬ同意并继续")).click();
					Thread.sleep(2000);
				}
			}
			Log.log.info("开始体验5分钟。。。");
			Thread.sleep(5 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall_CUN_AL(AndroidDriver driver) {
		try {
			if(isElementExistByString("安装")) {
				driver.findElement(By.name("安装")).click();
				Thread.sleep(3000);
			}
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
			Thread.sleep(10 * 1000);
			Log.log.info("开始体验5分钟");
			Thread.sleep(5 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	private int universalInstall_CUN_MX4(AndroidDriver driver) {
		try {
			if(isElementExistById("com.android.packageinstaller:id/action_positive")) {
				driver.findElement(By.id("com.android.packageinstaller:id/action_positive")).click();
				Thread.sleep(3000);
			}
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
			Thread.sleep(70 * 1000);
			AdbUtils.back();
			WebElement buttomButton1 = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
			buttomButton1.click();
			Thread.sleep(10 * 1000);
			Log.log.info("开始体验5分钟");
			Thread.sleep(5 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall_CUN_A3S(AndroidDriver driver) {
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
			Thread.sleep(10 * 1000);
			if (isElementExistByString("打开")) {
				driver.findElement(By.name("打开")).click();
			}
			Log.log.info("开始体验5分钟");
			Thread.sleep(5 * 70 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall_M5(AndroidDriver driver) {
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
			Thread.sleep(6 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	private int universalInstall_CUN_TL(AndroidDriver driver) {
		try {
			driver.findElement(By.name("安装")).click();
			Thread.sleep(60 * 1000);
			while (AdbUtils.getCurrentPackage().contains("packageinstaller")) {
				AdbUtils.back();
				Thread.sleep(1000);
			}
			AdbUtils.back();
			Thread.sleep(3 * 1000);
			WebElement buttomButton1 = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
			buttomButton1.click();
			Thread.sleep(10 * 1000);
			Log.log.info("开始体验5分钟。。。");
			Thread.sleep(5 * 60 * 1000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	public void checkAppList() {
		try {
			System.out.println("start");
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort + "/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			driver.findElement(By.name("额外奖励")).click();
			Thread.sleep(8000);
			int position = 1;
			String lastAppName = "";
			ArrayList<String> appList = new ArrayList<>();
			int repeatCount = 0;
			while (true) {
				String appName = driver
						.findElement(By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
								+ position + ")]/android.widget.TextView[contains(@index,1)]"))
						.getText();
				if (appName.equals(lastAppName)) {
					SwipeScreen.swipe(driver, 300, 800, 300, 665);
					if (repeatCount < 5) {
						repeatCount++;
						continue;
					}
				} else {
					repeatCount = 0;
					appList.add(appName);
					lastAppName = appName;
				}
				String packageName = Configure.map.get(appName);
				System.out.println("appName = " + appName);
				if (packageName == null) {
					System.out
							.println("unhandle application name = " + appName + "       packageName = " + packageName);
				}
				if (repeatCount >= 5) {
					for (int i = 2; i < 20; i++) {
						try {
							String appName1 = driver.findElement(
									By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
											+ i + ")]/android.widget.TextView[contains(@index,1)]"))
									.getText();
							System.out.println("appName = " + appName1);
						} catch (Exception e) {
							System.out.println("end");
							break;
						}
						String packageName1 = Configure.map.get(appName);
						if (packageName == null) {
							System.out.println(
									"unhandle application name = " + appName + "       packageName = " + packageName);
						}
					}
				}
				if (repeatCount >= 5) {
					break;
				} else {
					SwipeScreen.swipe(driver, 300, 800, 300, 665);
				}
			}
			System.out.println("getApllicationList end");
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			driver.quit();
			e.printStackTrace();
			return;
		}
	}

	public void checkAllApp() {
		try {
			System.out.println("start");
			driver = new AndroidDriver(new URL("http://127.0.0.1:" + Configure.appiumPort + "/wd/hub"), capabilities);
			Thread.sleep(20 * 1000);
			driver.findElement(By.name("应用赚")).click();
			Thread.sleep(1000);
			// driver.findElement(By.name("额外奖励")).click();
			// Thread.sleep(8000);
			// int position = 1;
			// String lastAppName = "";
			// ArrayList<String> appList = new ArrayList<>();
			// int repeatCount = 0;
			// while(true) {
			// String appName =
			// driver.findElement(By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"+
			// position+")]/android.widget.TextView[contains(@index,1)]")).getText();
			// if(appName.equals(lastAppName)) {
			// SwipeScreen.swipe(driver, 300, 800, 300, 665);
			// if(repeatCount <5) {
			// repeatCount++;
			// continue;
			// }
			// } else {
			// repeatCount = 0;
			// appList.add(appName);
			// lastAppName = appName;
			// }
			// String packageName = Configure.map.get(appName);
			// System.out.println("appName = " + appName);
			// if(packageName == null) {
			// System.out.println("unhandle application name = " + appName + "
			// packageName = " + packageName);
			// }
			// if(repeatCount >= 5) {
			// String appName1 = "";
			// for(int i=2;i<20;i++) {
			// try {
			// appName1 =
			// driver.findElement(By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"+
			// i+")]/android.widget.TextView[contains(@index,1)]")).getText();
			// System.out.println("appName = " + appName1);
			// }catch(Exception e) {
			// System.out.println("end");
			// break;
			// }
			// String packageName1 = Configure.map.get(appName1);
			// if(packageName1 == null) {
			// System.out.println("unhandle application name = " + appName1 + "
			// packageName = " + packageName1);
			// }
			// }
			// }
			// if(repeatCount >= 5) {
			// break;
			// }else {
			// SwipeScreen.swipe(driver, 300, 800, 300, 665);
			// }
			// }
			// System.out.println("getApllicationList end");
			driver.findElement(By.name("应用")).click();
			Thread.sleep(3000);
			int repeatCount = 0;
			int position = 1;
			String lastAppName = "";
			ArrayList appList = new ArrayList();
			while (true) {
				String appName = driver
						.findElement(By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
								+ position + ")]/android.widget.TextView[contains(@index,1)]"))
						.getText();
				String text = driver.findElement(By.xpath(
						"//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				System.out.println(text);
				if ((!"注册".equals(text)) && (!"体验".equals(text))) {
					SwipeScreen.swipe(driver, 300, 800, 300, 665);
					continue;
				}

				if (appName.equals(lastAppName)) {
					SwipeScreen.swipe(driver, 300, 800, 300, 665);
					if (repeatCount < 5) {
						repeatCount++;
						continue;
					}
				} else {
					repeatCount = 0;
					appList.add(appName);
					lastAppName = appName;
				}
				if (repeatCount >= 5) {
					for (int i = 2; i < 20; i++) {
						String text1 = driver.findElement(By.xpath(
								"//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
								.getText().substring(0, 2);
						if ((!"注册".equals(text1)) && (!"体验".equals(text1))) {
							continue;
						}
						try {
							String appName1 = driver.findElement(
									By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,"
											+ i + ")]/android.widget.TextView[contains(@index,1)]"))
									.getText();
							System.out.println("appName = " + appName1);
						} catch (Exception e) {
							System.out.println("end");
							break;
						}
					}
				}
				if (repeatCount >= 5) {
					break;
				} else {
					SwipeScreen.swipe(driver, 300, 800, 300, 665);
				}
			}
			System.out.println("getApllicationList end");
		} catch (Exception e) {
			System.out.println("error" + e.getMessage());
			driver.quit();
			e.printStackTrace();
			return;
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

	private boolean isElementExistById(String id) {
		try {
			WebElement element = driver.findElement(By.id(id));
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
			e.printStackTrace();
			return false;
		}
	}

	public void reset() {
		Log.log.info("mizhuan reset");
		isCompleted = false;
		isExtraBonusCompleted = false;
		isInstallCompleted = false;
		// isClickAdsCompleted = false;
	}

	private int timeSwitcher() {
		if (DateUtils.getHour() == 8 && isSigninMorning == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if (DateUtils.getHour() == 11 && isSigninNoon == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if (DateUtils.getHour() == 13 && isSigninNoon == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if (DateUtils.getHour() == 19 && isSigninNight == false) {
			return ResultDict.COMMAND_RESTART_APP;
		}
		if (DateUtils.getHour() == 8 && DateUtils.getMinute() >= 30 && DateUtils.getMinute() <= 35) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		if (DateUtils.getHour() == 10 && DateUtils.getMinute() >= 30 && DateUtils.getMinute() <= 35) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		if (DateUtils.getHour() == 12 && DateUtils.getMinute() >= 0 && DateUtils.getMinute() <= 5) {
			isExtraBonusCompleted = false;
			return ResultDict.COMMAND_RESTART_APP;
		}
		return ResultDict.COMMAND_SUCCESS;
	}

}