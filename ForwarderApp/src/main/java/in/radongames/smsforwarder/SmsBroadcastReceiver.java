package in.radongames.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;

import com.radongames.smslib.SmsContents;
import com.radongames.smslib.SmsTimestampConverter;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Inject
    MessageForwarder mForwarder;

    @Inject
    SmsTimestampConverter mConverter;

    @Override
    public void onReceive(Context ctx, Intent intent) {

        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        if (bundle == null || action == null) {

            return;
        }

        if (action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            log.debug("Received an SMS");

            Object[] allPdus = (Object[]) bundle.get("pdus");
            String format = bundle.getString("format");

            if (allPdus != null) {

                for (Object pdu : allPdus) {

                    SmsMessage smsMessage = createMessage(pdu, format);
                    if (smsMessage == null) {

                        continue;
                    }

                    SmsContents sms = mapContents(smsMessage);
                    mForwarder.forward(sms);
                }
            }
        }
    }

    private SmsMessage createMessage(Object pdu, String format) {

        return SmsMessage.createFromPdu((byte[]) pdu, format);
    }

    @NonNull
    private SmsContents mapContents(@NonNull SmsMessage smsMessage) {

        SmsContents sms = new SmsContents();
        sms.setOriginatingAddress(smsMessage.getOriginatingAddress());
        sms.setDisplayOriginatingAddress(smsMessage.getDisplayOriginatingAddress());
        sms.setMessageBody(smsMessage.getMessageBody());
        sms.setDisplayMessageBody(smsMessage.getDisplayMessageBody());
        sms.setServiceCentreAddress(smsMessage.getServiceCenterAddress());
        sms.setEmailFrom(smsMessage.getEmailFrom());
        sms.setEmailBody(smsMessage.getEmailBody());
        sms.setPseudoSubject(smsMessage.getPseudoSubject());
        sms.setTimestamp(mConverter.forward(smsMessage.getTimestampMillis()));
        return sms;
    }
}
