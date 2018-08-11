package app;

import callback.TaskCallback;
import common.Configure;
import io.appium.java_client.android.AndroidDriver;
import utils.Log;

public class News {
	
	public boolean isCompleted = false;
	public int current = 0;
	private KanDianTouTiao kandiantoutiao;
	private QueTouTiao qutoutiao;
	private TaoXinWen taoxinwen;
	private YueTouTiao yuetoutiao;
	private MeiZhuanNews meizhuanNews;
	
	private static News news;
	private News(){
		kandiantoutiao = KanDianTouTiao.getInstance();
		qutoutiao = QueTouTiao.getInstance();
		taoxinwen = TaoXinWen.getInstance();
		yuetoutiao = YueTouTiao.getInstance();
		meizhuanNews = MeiZhuanNews.getInstance();
	}
	
	public static News getInstance(){
		if(news == null){
			news = new News();
		}
		return news;
	}
	
	public void start(TaskCallback callback){
		
		while(current < 5){
			Log.log.info("news count = " + current);
			Log.log.info("kantoutiao start");
			if(!kandiantoutiao.isCompleted && Configure.appConfig >> 2 == 1){
				kandiantoutiao.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						Log.log.info("kantoutiao finish");
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			Log.log.info("qutoutiao start");
			if(!qutoutiao.isCompleted && Configure.appConfig >> 2 == 1){
				qutoutiao.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						Log.log.info("qutoutiao finish");
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			
//			if(!taoxinwen.isCompleted){
//				taoxinwen.start(new TaskCallback() {
//					
//					@Override
//					public void onSuccess(AndroidDriver driver) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onRestartApp(AndroidDriver driver) {
//						callback.onRestartApp(driver);
//						return;
//					}
//				});
//			}
			
			Log.log.info("yuetoutiao start");
			if(!yuetoutiao.isCompleted && Configure.appConfig >> 2 == 1){
				yuetoutiao.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						Log.log.info("yuetoutiao finish");
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			Log.log.info("meizhuan start");
			if(!meizhuanNews.isCompleted){
				meizhuanNews.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						Log.log.info("meizhuan finish");
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
//						callback.onRestartApp(driver);
					}
				});
			}
			current++;
			kandiantoutiao.reset();
			qutoutiao.reset();
			taoxinwen.reset();
			yuetoutiao.reset();
			meizhuanNews.reset();
		}
	}
	
	
	public void reset(){
		Log.log.info("News reset");
		isCompleted = false;
		current = 0;
		kandiantoutiao.reset();
		qutoutiao.reset();
		taoxinwen.reset();
		yuetoutiao.reset();
	}

}
