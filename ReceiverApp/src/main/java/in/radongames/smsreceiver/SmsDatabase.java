package in.radongames.smsreceiver;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {SmsEntity.class}, exportSchema = false)
public abstract class SmsDatabase extends RoomDatabase {

    public abstract SmsDao getDao();
}
