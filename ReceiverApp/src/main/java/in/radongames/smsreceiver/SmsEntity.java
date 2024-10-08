package in.radongames.smsreceiver;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "messages")
@Getter
@Setter
@ToString
public class SmsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long mId;
    @ColumnInfo(name = "orig_addr")
    private String mOriginatingAddress;
    @ColumnInfo(name = "orig_daddr")
    private String mDisplayOriginatingAddress;
    @ColumnInfo(name = "dest_addr")
    private String mDestinationAddress;
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
    @ColumnInfo(name = "ts_sent")
    private long mSentAt;
    @ColumnInfo(name = "ts_fwded")
    private long mForwardedAt;
    @ColumnInfo(name = "ts_rcvd")
    private long mReceivedAt;
}
