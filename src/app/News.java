package app;

import callback.TaskCallback;
import io.appium.java_client.android.AndroidDriver;

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
		System.out.println("news start and count = " + current);
		while(current < 10){
			if(!kandiantoutiao.isCompleted){
				System.out.println("before kankan start");
				kandiantoutiao.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			if(!qutoutiao.isCompleted){
				qutoutiao.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			
			if(!taoxinwen.isCompleted){
				taoxinwen.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			if(!yuetoutiao.isCompleted){
				yuetoutiao.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						return;
					}
				});
			}
			
			if(!meizhuanNews.isCompleted){
				meizhuanNews.start(new TaskCallback() {
					
					@Override
					public void onSuccess(AndroidDriver driver) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onRestartApp(AndroidDriver driver) {
						callback.onRestartApp(driver);
						
					}
				});
			}
			
			kandiantoutiao.reset();
			qutoutiao.reset();
			taoxinwen.reset();
			yuetoutiao.reset();
			meizhuanNews.reset();
		}
	}
	
	
	public void reset(){
		isCompleted = false;
		current = 0;
		kandiantoutiao.reset();
		qutoutiao.reset();
		taoxinwen.reset();
		yuetoutiao.reset();
	}

}
