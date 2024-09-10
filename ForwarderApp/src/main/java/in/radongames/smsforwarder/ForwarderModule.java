package in.radongames.smsforwarder;

import android.content.Context;

import com.radongames.android.platform.Threader;
import com.radongames.smslib.SharedPreferencesBag;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ForwarderModule {

    @Provides
    MessageForwarder provideForwarder(@ApplicationContext Context ctx, SharedPreferencesBag bag, Threader t) {

        return new FcmMessageForwarder(ctx, bag, t);
    }
}
