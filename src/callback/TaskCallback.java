package callback;

import io.appium.java_client.android.AndroidDriver;

public interface TaskCallback {

	void onSuccess(AndroidDriver driver);
    void onRestartApp(AndroidDriver driver);
    
}
