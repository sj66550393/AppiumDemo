package utils;
import java.time.Duration;  
import io.appium.java_client.TouchAction;  
import io.appium.java_client.android.AndroidDriver;  

public class SwipeScreen {
	static Duration duration=Duration.ofSeconds(1);  
    public static void swipeUp(AndroidDriver driver)  
     {  
          int width = driver.manage().window().getSize().width;  
          int height = driver.manage().window().getSize().height;  
         TouchAction action1 = new TouchAction(driver).press(width / 2,height * 4/ 5).waitAction(duration).moveTo(width /2, height /4).release();  
         action1.perform();  
     }  
     public static void swipeDown(AndroidDriver driver)// scroll down to refresh  
     {  
          int width = driver.manage().window().getSize().width;  
          int height = driver.manage().window().getSize().height;  
         TouchAction action1 = new TouchAction(driver).press(width / 2,height/4).waitAction(duration).moveTo(width /2, height* 3/4).release();  
            action1.perform();  
     }  
     public static void swipeLeft(AndroidDriver driver)  
     {  
          int width = driver.manage().window().getSize().width;  
          int height = driver.manage().window().getSize().height;  
         TouchAction action1 = new TouchAction(driver).press(width -10,height/2).waitAction(duration).moveTo(width /4, height /2).release();  
            action1.perform();  
     }  
     public static void swipeRight(AndroidDriver driver)  
     {  
          int width = driver.manage().window().getSize().width;  
          int height = driver.manage().window().getSize().height;  
         TouchAction action1 = new TouchAction(driver).press(10,height/2).waitAction(duration).moveTo(width *3/4+10, height /2).release();  
            action1.perform();  
     }  
     
     public static void swipe(AndroidDriver driver , int startX ,int startY , int endX , int endY)  
     {  
          int width = driver.manage().window().getSize().width;  
          int height = driver.manage().window().getSize().height;  
         TouchAction action1 = new TouchAction(driver).press(startX,startY).waitAction(duration).moveTo(endX, endY).release();  
         action1.perform();  
     }
}
