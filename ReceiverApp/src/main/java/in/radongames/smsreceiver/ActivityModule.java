package in.radongames.smsreceiver;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class ActivityModule {

    @Provides
    public SmsViewModel provideSmsViewModel(Activity activity) {

        if (!(activity instanceof ViewModelStoreOwner)) {

            /*
             * We can do this because ReceiverApp has only one Activity.
             * If we add more it is best to do what Pippin does - instead of throwing an Exception, instantiate a ViewModelStoreOwner.
             */
            throw new IllegalStateException("[" + activity.getComponentName().flattenToString() + "] is not a ViewModelStoreOwner");
        }
        return new ViewModelProvider((ViewModelStoreOwner) activity).get(SmsViewModel.class);
    }
}
