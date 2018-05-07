package utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdbUtils {
	public static String deviceId = "GEQBBBE675433987";
	public static String storageDes = "e:/";
	public static String storageDir = storageDes + deviceId + "/";
	private static String adb =  "adb -s " + deviceId	+" shell ";
    public static String getTopActivity(){
    	String execResult = printf(adb + "dumpsys activity activities | grep mFocusedActivity");
    	String[] str = execResult.split(" ");
    	return  execResult.split(" ")[5];
    }
    
    public static String isAwake(){
    	String execResult = printf(adb + "dumpsys window policy | grep mAwake");
    	return execResult.substring(11, 16);
    }
    
    public static String isAwake2(){
    	String execResult = printf(adb + "dumpsys window policy | grep mScreenOnFully");
    	String[] substr = execResult.split("mScreenOnFully=");
    	System.out.println(substr[1].substring(0, 5));
    	return substr[1].substring(0, 5);
    }
    
    
    public static void changeAdb(String str){
    	adb  = "D:\\AndroidSDK\\platform-tools\\adb.exe " + "-s " + str + " shell";
    }
    
    public static String getAdb(String deviceId){
    	return "D:\\AndroidSDK\\platform-tools\\adb.exe " + "-s " + deviceId + " shell";
    }
    
    public static String getCurrentPackage(){
    	  return getTopActivity().split("/")[0];
    }
    
    public static void killProcess(String pkg){
    	 printf(adb + "am force-stop " + pkg);
    }
    
    public static void startActivity(String activity){
    	 printf(adb + "am start -n " + activity);
    }
    
    public static void exec(String cmd) throws Exception {
        Process ps = null;
        try {
            ps = Runtime.getRuntime().exec(cmd);
            int code = ps.waitFor();
            if (code != 0) throw new Exception("exec error(code=" + code + "): " + cmd);
        } finally {
            if (ps != null) ps.destroy();
        }
    }
    
    public static String printf( String cmd ){
    	BufferedReader reader = null;
    	String content = "";
    	try {
    		Process process = Runtime.getRuntime().exec(cmd);
    	    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	    StringBuffer output = new StringBuffer();
    	    int read;
    	    char[] buffer = new char[4096];
    	    while ((read = reader.read(buffer)) > 0) {
    	        output.append(buffer, 0, read);
    	    }
    	    reader.close();
    	    content = output.toString();
    	    return content;
    	} catch (IOException e) {
    	    e.printStackTrace();
    		return "";
    	}
    }
    
    public static void click(int x ,int y ){
    	try {
			exec(adb + "input tap " + x + " " + y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public  static void back(){
    	try {
    		exec(adb + "input keyevent 4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void swipe(int x1 , int y1 , int x2 , int y2){
    	try {
    		exec(adb + "input swipe " + x1 + " " + y1 + " " + x2 + " " + y2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void ScreenCap(){
    	try {
    		exec(adb + "screencap -p /sdcard/1.png");
    		Thread.sleep(3000);
    		exec("adb -s "+  deviceId + " pull sdcard/1.png e:/");
    		Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static String ScreenCapAndCut(int x, int y , int width , int height){
    	try {
    		String desPath = storageDes + "/" + deviceId;
    		File file = new File(desPath);
    		if(!file.exists()){
    			file.mkdirs();
    		}
    		exec(adb + "screencap -p /sdcard/1.png");
    		Thread.sleep(3000);
    		exec("adb -s " + deviceId + " pull sdcard/1.png " + desPath);
    		Thread.sleep(3000);
    		String path  = CutImageUtil.cutLocalImage(desPath + "/1.png", desPath + "/", x, y, width, height);
    		Log.log.info(path);
    		return path;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    }
    
    public static void cleanApp(){
    	String[] defaultPackages=  {""};
    	try {
			String str = printf(adb + "pm list package");
			String[] strs = str.split("\n");
			for(int i=0;i<strs.length;i++){
				strs[i] = strs[i].substring(8);
				String[] splitStr = strs[i].split("\\.");
				if(splitStr.length > 1){
					if((!splitStr[1].equals("android")) && (!splitStr[1].equals("huawei")) && (!splitStr[1].equals("google")) && (!splitStr[1].equals("mediatek")) &&(!splitStr[1].equals(""))){
						Log.log.info(strs[i].trim());
						killProcess(strs[i].trim());
					}
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void clickPower(){
    	try {
    		exec(adb + "input keyevent 26");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static String getProductModel(){
    	try {
    		String str = printf(adb + "getprop | grep ro.product.model");
    		String[] splitStr = str.split(":");
    		return splitStr[1].trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
    
    public static boolean isPad(){
    	try {
    		String str = printf(adb + "wm size");
    		String[] splitStr = str.split(":")[1].split("x");
    		int width = Integer.parseInt(splitStr[0].trim());
    		int height = Integer.parseInt(splitStr[1].trim());
    		return width > height;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}
