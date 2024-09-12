package in.radongames.smsreceiver;

import org.mapstruct.factory.Mappers;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ReceiverModule {

    @Provides
    SmsMapper providePinMapper() {

        return Mappers.getMapper(SmsMapper.class);
    }
}
