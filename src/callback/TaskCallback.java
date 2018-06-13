package callback;

import io.appium.java_client.android.AndroidDriver;

public interface TaskCallback {

	void onSuccess();
    void onRestartApp(AndroidDriver driver);
    
}
