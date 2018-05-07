package app;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.util.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import manager.ExtraBonusManager;
import manager.InstallAppManager;
import manager.LooklookManager;
import manager.SigninManager;
import utils.DateUtils;
import utils.AdbUtils;
import utils.SwipeScreen;
import common.Configure;
import common.Contants;
import common.ResultDict;

public class MiZhuan {
	
	AndroidDriver driver;
	DesiredCapabilities capabilities;
	private int redPackageNum = 0; // ���� 5ƪ
	private int todayMustNum = 0; // ���ձؿ� 6ƪ
	private int entertainmentNews = 0; // ���ֱ��� 5ƪ
	private int ThreeSixZeroNewsNum = 0; // 360���� ÿ��6ƪ
	private int HotNewsNum = 0; // �ȵ����� ÿ��5��
	private int turnturnNum = 0; // ������ 9ƪ
	private int tuituiNum = 0; // ������ 5ƪ
	private int goldNewsNum = 0; // ���ͷ�� 7ƪ
	private int eighteenNum = 0; // 18ͷ��
	private int loveNewsNum = 0; // �Ұ�ͷ�� 9ƪ
	private int DEFAULT_EXTRABONUS_TIME = 1;
	private int INSTALL_EXPERIWNCE_TIME = 5;
	private int DEFAULT_INSTALL_COUNT  = 28;
	private boolean isExtraBonusCompleted = false;
	private boolean isLooklookCompleted = false;
	private boolean isInstallCompleted = false;
	private boolean isClickAdsCompleted = false;
	private boolean isSigninCompleted = true;
	private boolean isSigninMorning = true;
	private boolean isSigninNoon = true;
	private boolean isSigninAfternoon =true;
	private boolean isSigninNight = true;
	private int installCount = 0;

	ExtraBonusManager extraBonusManager;
	LooklookManager looklookManager;
	InstallAppManager installAppManager;
	SigninManager signinManager;
	
	public MiZhuan() {
		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "CUN AL00");
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");
		capabilities.setCapability("appPackage", "me.mizhuan");
		capabilities.setCapability("appActivity", ".ActCover");
		capabilities.setCapability("newCommandTimeout", 600);
		capabilities.setCapability("udid", "UCZHUGEU99999999");
		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:4731/wd/hub"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		extraBonusManager = new ExtraBonusManager(driver);
		looklookManager = new LooklookManager(driver);
		installAppManager = new InstallAppManager(driver);
		signinManager = new SigninManager(driver);
		
	}
	
	public int start(){		
		if (!AdbUtils.getTopActivity().contains("me.mizhuan/.TabFragmentActivity")) {
			Activity activity = new Activity("me.mizhuan" , ".ActCover");
			driver.startActivity(activity);
			try {
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(isElementExistById("me.mizhuan:id/start_button")){
			System.out.println("��ʼ׬");
			driver.findElement(By.id("me.mizhuan:id/start_button")).click();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			   e.printStackTrace();
			}
		}
		
		if(DateUtils.getHour() == 1){
			isExtraBonusCompleted = false;
			isLooklookCompleted = false;
			isSigninCompleted = false;
			isSigninMorning = false;
			isSigninNoon = false;
			isSigninAfternoon = false;
			isSigninNight = false;
		}
		int result = ResultDict.COMMAND_SUCCESS;
		if(isElementExistByString("ǩ��")){
			result  = clickSignin();
			if (ResultDict.COMMAND_SUCCESS != result)
				return result;
		}
		if(isElementExistByString("����ǩ��") || isElementExistByString("����ǩ��") || isElementExistByString("����ǩ��") || isElementExistByString("����ǩ��")) {
			
		}
		if (!isExtraBonusCompleted) {
			result = startSigninAppTask();
			if (ResultDict.COMMAND_SUCCESS != result)
				return result;
		}
		if(!isClickAdsCompleted){
			result = startClickAds();
			if (ResultDict.COMMAND_SUCCESS != result)
				return result;
		}
		
		if (!isLooklookCompleted) {
			result = startLooklookTaskFromBottomGame();
			if (ResultDict.COMMAND_SUCCESS != result)
				return result;
		}
		if(!isInstallCompleted){
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
			}
		}
		if(result != ResultDict.COMMAND_SUCCESS){
			return result;
		}
		return ResultDict.COMMAND_SUCCESS;
	}
	
	private int startLooklookTaskFromBottomGame() {

		try {
			driver.findElement(By.name("��Ϸ׬"))
			.click();
			Thread.sleep(8000);
			for (int i = 0; i < 5; i++) {
				AdbUtils.swipe(500, 700, 500, 300);
				Thread.sleep(1000);
			}
//			if (!clickEntertainmentNews()) {
//				return ResultDict.COMMAND_RESTART_APP;
//			}
			if (!clickThreeSixZeroNews()) {
				return ResultDict.COMMAND_RESTART_APP;
			}
			if (!clickTurnturn()) {
				return ResultDict.COMMAND_RESTART_APP;
			}
			clickTuitui();
			clickRedPackage();
//			if (!clickGoldNews()) {
//				return ResultDict.COMMAND_RESTART_APP;
//			}
//			if (!clickEighteenNews()) {
//				return ResultDict.COMMAND_RESTART_APP;
//			}
//			if (!clickLoveNews()) {
//				return ResultDict.COMMAND_RESTART_APP;
//			}
			return ResultDict.COMMAND_SUCCESS;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}

	// ������
	public void clickTuitui() {
		try {
			Thread.sleep(10000);
			if(!isElementExistByString("������")){
				return ;
			}
			for (; tuituiNum < Contants.TUITUI_NUM; tuituiNum++) {
				driver.findElement(By.name("������")).click();
				Thread.sleep(2000);
				if (isElementExistByString("�������������")) {
					return;
				}
				Thread.sleep(10 * 1000);
				driver.findElement(By.name("����")).click();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// ������
	public boolean clickTurnturn() {
		try {
			if(isElementExistByString("������")){
				return true;
			}
			for (; tuituiNum < Contants.TURNTURN_NUM; tuituiNum++) {
				driver.findElement(By.name("������")).click();
				Thread.sleep(2000);
				if (isElementExistByString("�������������")) {
					return true;
				}
				Thread.sleep(10 * 1000);
				if (!looklookManager.checkClickTurnturn()) {
					return false;
				}
				AdbUtils.back();
				AdbUtils.back();
			}
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ����
	public void clickRedPackage() {
		try {
			if(!isElementExistByString("����")){
				return ;
			}
			for (; redPackageNum < Contants.RED_PACKAGES_NUM; redPackageNum++) {
				driver.findElement(By.name("����")).click();
				Thread.sleep(2000);
				if (isElementExistByString("�������������")) {
					return;
				}
				Thread.sleep(2 * 60 * 1000);
				AdbUtils.back();
				AdbUtils.back();
				Thread.sleep(1000);
				AdbUtils.click(180, 90);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

	// ���ͷ��
	public boolean clickGoldNews() {
		try {
			AdbUtils.click(216,695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickGoldNews()) {
				return false;
			}
			for (; goldNewsNum < Contants.GOLD_NEWS_NUM ; goldNewsNum++) {
				Thread.sleep(5000);
				for(int i=0;i<goldNewsNum;i++){
				AdbUtils.swipe(300, 1100, 300, 500);
				}
				AdbUtils.click(300, 600);
				Thread.sleep(10000);
				if(!looklookManager.checkEnterGoldNews()){
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
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 18ͷ��
	public boolean clickEighteenNews() {
		try {
			AdbUtils.click(216,695);
			Thread.sleep(2000);
			if (!looklookManager.checkClickEighteenNews()) {
				return false;
			}
			for (; eighteenNum < Contants.EIGHTEEN_NEWS_NUM; eighteenNum++) {
				Thread.sleep(5000);
				for(int i=0;i<eighteenNum;i++){
				AdbUtils.swipe(300, 1100, 300, 500-20*i);
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
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	// �Ұ�ͷ��
	public boolean clickLoveNews() {
		try {
			AdbUtils.click(648,695);
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
			return false;
		}
	}

	// ���ձؿ�
	public void clickTodayMustNews() {
		// TODO
	}

	// ���ֱ���
	public boolean clickEntertainmentNews() {
		try {
			driver.findElement(By.name("���ֱ���"));
			Thread.sleep(2000);
			if (!looklookManager.checkClickEntertainNews()) {
				return false;
			}
			for (; entertainmentNews < Contants.ENTERTAINMENT_NEWS ; entertainmentNews++) {
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
				while(looklookManager.checkEnterEntertainNews()){
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
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	

	private boolean clickThreeSixZeroNews() {
		try {
			driver.findElement(By.name("360����")).click();
			Thread.sleep(10000);
			if (!looklookManager.checkClick360News()) {
				return false;
			}
			if(isElementExistByString("�������������")){
				AdbUtils.back();
				return true;
			}
			for (; ThreeSixZeroNewsNum < Contants.THREE_SIX_ZERO_NEWS_NUM + 3; ThreeSixZeroNewsNum++) {
				swipeAndClickNews();
				if (!looklookManager.checkClick360News()) {
					return false;
				}
			}
			Thread.sleep(2000);
			AdbUtils.back();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ����������� ���Ž���ͣ��20S
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
	
	// ����������� ���Ž��滬��
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

	private int clickSignin() {
		try {
			driver.findElement(By.name("ǩ��")).click();
			Thread.sleep(1000);
			if (!signinManager.checkClickBottomRecommand()) {
				return ResultDict.COMMAND_RESTART_APP;
			}
			AdbUtils.click(90, 224);
			Thread.sleep(2000);
			if (!signinManager.checkEnterSigninDetail()) {
				return ResultDict.COMMAND_RESTART_APP;
			}
			AdbUtils.click(180,1139);
			Thread.sleep(5000);
			AdbUtils.back();
			Thread.sleep(1000);
			AdbUtils.back(); 
			isSigninCompleted = true;
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	private int startClickAds() {

        try {
    		driver.findElement(By.name("�Ƽ�"))
    		.click();
			Thread.sleep(2000);
			driver.findElement(By.name("����׬"))
			.click();
			Thread.sleep(4000);
			driver.findElement(By.name("����"))
			.click();
			Thread.sleep(2000);
			driver.findElement(By.name("��ʼ׬Ǯ"))
			.click();
			Thread.sleep(2000);
			if(isElementExistByString("����һ��")){
				driver.findElement(By.name("����һ��"))
				.click();
			}
			Thread.sleep(70 * 1000);
			isClickAdsCompleted = true;
			AdbUtils.killProcess(AdbUtils.getCurrentPackage());
			Thread.sleep(8000);
			AdbUtils.back();
			Thread.sleep(1000);
			if("����".equals(driver.findElement(By.id("me.mizhuan:id/title")).getText())){
				AdbUtils.back();
			}
			Thread.sleep(2000);
			AdbUtils.back();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
        return ResultDict.COMMAND_SUCCESS;
	}

	private int startSigninAppTask() {
		try {
			String lastPackage = "";
			int appUseTime = 1;
			boolean leftSwipe = false;
			while (!((DateUtils.getHour() > 8) || ((DateUtils.getHour() == 8) && (DateUtils.getMinute() > 30)))) {
				if (leftSwipe) {
					AdbUtils.swipe(100, 500, 400, 500);
				} else {
					AdbUtils.swipe(400, 500, 100, 500);
				}
				Thread.sleep(1 * 60 * 1000);
				leftSwipe = !leftSwipe;
			}
			System.out.println("���Ӧ��׬");
			// ���Ӧ��׬
			driver.findElement(By.name("Ӧ��׬")).click();
			Thread.sleep(1000);
			// ���⽱��
			driver.findElement(By.name("���⽱��")).click();
			Thread.sleep(8000);
			while (true) {
				Thread.sleep(500);
				AdbUtils.swipe(300, 500, 300, 1000);
				Thread.sleep(5000);
				if (isElementExistById("me.mizhuan:id/mituo_status")) {
					driver.findElement(By.id("me.mizhuan:id/mituo_status")).click();
				} else {
					if (!((DateUtils.getHour() > 10)
							|| ((DateUtils.getHour() == 10) && (DateUtils.getMinute() > 32)))) {
						Thread.sleep(2 * 60 * 1000);
						continue;
					} else {
						isExtraBonusCompleted = true;
						return ResultDict.COMMAND_SUCCESS;
					}
				}
				if(lastPackage.equals(AdbUtils.getCurrentPackage())){
					appUseTime++;
				}else{
					appUseTime = 2;
				}
				Thread.sleep(appUseTime * 70 * 1000);
				String name = AdbUtils.getCurrentPackage();
				lastPackage = AdbUtils.getCurrentPackage();
				AdbUtils.killProcess(AdbUtils.getCurrentPackage());
				Thread.sleep(5000);
				if (!extraBonusManager.checkKillApp(name)) {
					return ResultDict.COMMAND_RESTART_APP;
				}
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	public int installApp_CUN_AL(AndroidDriver driver) {
		try {
			Thread.sleep(10000);
			driver.findElement(By.xpath("//android.widget.TabWidget/android.widget.LinearLayout[contains(@index,1)]"))
					.click();
			while (installCount < DEFAULT_INSTALL_COUNT) {
				Thread.sleep(3000);
				System.out.println("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.id("me.mizhuan:id/mituo_app_size"))
						.getText();
				System.out.println("appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				System.out.println(text);
				if (("ע��".equals(text) || "����".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("������װ".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(30 * 1000);
						while (true) {
							WebElement installButton = driver
									.findElement(By.id("com.android.packageinstaller:id/ok_button"));
							if ("��һ��".equals(installButton.getText())) {
								installButton.click();
								Thread.sleep(1000);
							} else {
								installButton.click();
								Thread.sleep(1000);
								break;
							}
						}
						Thread.sleep(20* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("����")){
								driver.findElement(By.name("����")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(20* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);					 
					} else if ("��������".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("����")){
								System.out.println("click allow");
								driver.findElement(By.name("����")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 800, 300, 665);
			}
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	private int installApp_CUN_TL(AndroidDriver driver){
		try {
			Thread.sleep(10000);
			driver.findElement(By.name("Ӧ��׬")).click();
			Thread.sleep(1000);
			driver.findElement(By.name("Ӧ��")).click();
			Thread.sleep(2000);
			while (installCount < DEFAULT_INSTALL_COUNT) {
				System.out.println("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.id("me.mizhuan:id/mituo_app_size"))
						.getText();
				System.out.println("app size = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				System.out.println(text);
				if (("ע��".equals(text) || "����".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("������װ".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(60 * 1000);
						driver.findElement(By.name("��װ")).click();
						Thread.sleep(60* 1000);
						driver.findElement(By.name("��")).click();
						Thread.sleep(1000);
						driver.findElement(By.name("ɾ��")).click();
						Thread.sleep(10 * 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("����")){
								driver.findElement(By.name("����")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("����")){					
								driver.findElement(By.name("����")).click();
								Thread.sleep(2000);
							}
						}
						installCount++;
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else if ("��������".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("����")){
								driver.findElement(By.name("����")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("����")){					
								driver.findElement(By.name("����")).click();
								Thread.sleep(2000);
							}
						}
						installCount++;
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 800, 300, 665);
			}
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	
	
	
	public int installApp_OPPO(AndroidDriver driver) {
		try {
			Thread.sleep(10000);
			driver.findElement(By.name("Ӧ��׬")).click();
			while (installCount < DEFAULT_INSTALL_COUNT) {
				Thread.sleep(3000);
				System.out.println("installCount = " + installCount);
				String text = driver
						.findElement(By
								.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]/android.widget.Button"))
						.getText().substring(0, 2);
				String appSizeStr = driver
						.findElement(By
								.id("me.mizhuan:id/mituo_app_size"))
						.getText();
				System.out.println("appSize = " + appSizeStr);
				double appSize = Double.parseDouble(appSizeStr.substring(0, appSizeStr.length() - 1));
				System.out.println(text);
				if (("ע��".equals(text) || "����".equals(text)) && appSize < 40) {
					driver.findElement(
							By.xpath("//android.widget.ListView/android.widget.RelativeLayout[contains(@index,1)]"))
							.click();
					Thread.sleep(2000);
					WebElement buttomButton = driver.findElement(By.id("me.mizhuan:id/mituo_linearLayoutBottom"));
					if ("������װ".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(60 * 1000);
						driver.findElement(By.name("��װ")).click();
						Thread.sleep(30 * 1000);
						for (int j = 0; j < 5; j++) {
							if(driver.getPageSource().contains("ͬ�Ⲣ����")){
								driver.findElement(By.name("ͬ�Ⲣ����")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
						driver.findElement(By.name("���")).click();
						Thread.sleep(2000);
					} else if ("��������".equals(buttomButton.getText())) {
						buttomButton.click();
						Thread.sleep(20 *1000);
						for(int j=0;j<5;j++){
							if(driver.getPageSource().contains("ͬ�Ⲣ����")){
								System.out.println("click allow");
								driver.findElement(By.name("ͬ�Ⲣ����")).click();
								Thread.sleep(2000);
							}
						}
						Thread.sleep(5*60* 1000);
						System.out.println("currentPackage = " + AdbUtils.getCurrentPackage());
						installCount++;
						AdbUtils.killProcess(AdbUtils.getCurrentPackage());
						Thread.sleep(2000);
					} else {

					}
					driver.pressKeyCode(AndroidKeyCode.BACK);
					Thread.sleep(2000);
				}
				SwipeScreen.swipe(driver, 300, 636, 300, 500);
			}
			Thread.sleep(2000);
			return ResultDict.COMMAND_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultDict.COMMAND_RESTART_APP;
		}
	}
	
	
	private boolean isElementExist(){
		try{
			WebElement element = driver.findElement(By.id("com.huawei.systemmanager:id/btn_allow"));
			element.isDisplayed();
			return true;
		}catch(Exception e) {
			return false;
		}
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
	
	private boolean isElementExistById(String id){
		try{
			WebElement element = driver.findElement(By.id(id));
			element.isDisplayed();
			return true;
		}catch(Exception e) {
			return false;
		}
	}

}