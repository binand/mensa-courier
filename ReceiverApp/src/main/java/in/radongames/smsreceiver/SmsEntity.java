package in.radongames.smsreceiver;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "messages")
@Getter
@Setter
public class SmsEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long mId = 0L;

    @ColumnInfo(name = "orig_addr")
    private String mOriginatingAddress;
    @ColumnInfo(name = "orig_daddr")
    private String mDisplayOriginatingAddress;
    @ColumnInfo(name = "msg_body")
    private String mMessageBody;
    @ColumnInfo(name = "msg_dbody")
    private String mDisplayMessageBody;
    @ColumnInfo(name = "svc_addr")
    private String mServiceCentreAddress;
    @ColumnInfo(name = "email_from")
    private String mEmailFrom;
    @ColumnInfo(name = "email_body")
    private String mEmailBody;
    @ColumnInfo(name = "pseudo_subj")
    private String mPseudoSubject;
    @ColumnInfo(name = "timestamp")
    @Ignore
    private long mTimestamp;
}
