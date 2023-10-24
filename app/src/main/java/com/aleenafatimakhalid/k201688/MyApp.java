package com.aleenafatimakhalid.k201688;


import android.app.Application;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.onesignal.Continue;

public class MyApp extends Application {

    // NOTE: Replace the below with your own ONESIGNAL_APP_ID
    private static final String ONESIGNAL_APP_ID = "fb82942e-96a8-4848-b174-6bb24fb74520";

    @Override
    public void onCreate() {
        super.onCreate();

        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
            if (r.isSuccess()) {
                if (r.getData()) {
                    // `requestPermission` completed successfully and the user has accepted permission
                }
                else {
                    // `requestPermission` completed successfully but the user has rejected permission
                }
            }
            else {
                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
            }
        }));
    }
}
