package in.radongames.smsforwarder;

import android.app.Application;

import com.radongames.smslib.FileLogger;

import dagger.hilt.android.HiltAndroidApp;
import lombok.CustomLog;

@HiltAndroidApp
@CustomLog
public class SmsForwarderApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        log.debug("Created: " + getClass().getSimpleName());
        new FileLogger().init(this);
        log.debug("Logging initialised.");
    }
}
