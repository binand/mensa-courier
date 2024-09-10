package in.radongames.smsreceiver;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;
import lombok.CustomLog;

@HiltAndroidApp
@CustomLog
public class SmsReceiverApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        log.debug("Created: " + getClass().getSimpleName());
    }
}
