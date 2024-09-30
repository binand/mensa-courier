package in.radongames.smsreceiver;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.radongames.android.data.ListenableFutureConverter;
import com.radongames.android.data.LiveDataConverter;
import com.radongames.core.converters.ListConverter;
import com.radongames.smslib.SmsContents;

import org.mapstruct.factory.Mappers;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ApplicationModule {

    @Provides
    SmsMapper provideSmsMapper() {

        return Mappers.getMapper(SmsMapper.class);
    }

    @Provides
    SmsListMapper provideSmsListMapper() {

        return Mappers.getMapper(SmsListMapper.class);
    }

    @Provides
    ListenableFutureConverter<SmsEntity, SmsContents> provideListenableFutureConverter(SmsMapper mapper) {

        return new ListenableFutureConverter<>(mapper);
    }

    @Provides
    ListConverter<SmsEntity, SmsContents> provideListConverter(SmsMapper mapper) {

        return new ListConverter<>(mapper);
    }

    @Provides
    ListenableFutureConverter<List<SmsEntity>, List<SmsContents>> provideListenableListFutureConverter(ListConverter<SmsEntity, SmsContents> converter) {

        return new ListenableFutureConverter<>(converter);
    }

    @Provides
    LiveDataConverter<List<SmsEntity>, List<SmsContents>> provideLiveDataListConverter(ListConverter<SmsEntity, SmsContents> converter) {

        return new LiveDataConverter<>(converter);
    }

    @Provides
    SmsDatabase provideSmsDatabase(@ApplicationContext Context ctx) {

        RoomDatabase.Builder<SmsDatabase> builder = Room.databaseBuilder(ctx, SmsDatabase.class, "messagesdb").fallbackToDestructiveMigration();
        return builder.build();
    }

    @Provides
    SmsDao provideSmsDao(SmsDatabase db) {

        return db.getDao();
    }
}
