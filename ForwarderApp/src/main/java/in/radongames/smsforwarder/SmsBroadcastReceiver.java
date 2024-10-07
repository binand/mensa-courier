package in.radongames.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;

import com.radongames.core.interfaces.EncoderDecoder;
import com.radongames.smslib.SharedPreferencesBag;
import com.radongames.smslib.SmsContents;

import java.io.Serializable;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Inject
    MessageForwarder mForwarder;

    @Inject
    EncoderDecoder<Serializable> mEncoder;

    @Inject
    SharedPreferencesBag mBag;

    @Override
    public void onReceive(Context ctx, Intent intent) {

        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        if (bundle == null || action == null || !action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            return;
        }

        log.debug("Received an SMS");

        Object[] allPdus = (Object[]) bundle.get("pdus");
        String format = bundle.getString("format");

        if (allPdus != null) {

            int pduCount = 0;
            SmsContents message = null;
            StringBuilder msgBuilder = new StringBuilder();
            StringBuilder dMsgBuilder = new StringBuilder();
            for (Object pdu : allPdus) {

                SmsMessage smsMessage = createMessage(pdu, format);
                if (pduCount == 0) {

                    if (smsMessage == null) {

                        continue;
                    }

                    /*
                     * Map the metadata of the message only once.
                     */
                    message = mapContents(smsMessage);
                }

                msgBuilder.append(smsMessage.getMessageBody());
                dMsgBuilder.append(smsMessage.getDisplayMessageBody());
                pduCount++;
            }

            if (message != null) {

                /*
                 * At least one PDU had usable data. We combine all PDUs with usable data into one long message.
                 * But then we send across only the first 80 characters of it.
                 */
                int transmitLen = 80;
                String msg = mEncoder.encode(msgBuilder.length() >= transmitLen ? msgBuilder.substring(0, transmitLen - 3) + "..." : msgBuilder.toString());
                String dMsg = mEncoder.encode(msgBuilder.length() >= transmitLen ? msgBuilder.substring(0, transmitLen - 3) + "..." : msgBuilder.toString());
                message.setMessageBody(msg);
                message.setDisplayMessageBody(dMsg);
                mForwarder.forward(message);
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
        sms.setDestinationAddress(mBag.retrieve(BagKeyNames.Key_OWN_NUMBER));
        /*
         * These two are set in the caller itself.
         */
//        sms.setMessageBody(mEncoder.encode(smsMessage.getMessageBody()));
//        sms.setDisplayMessageBody(mEncoder.encode(smsMessage.getDisplayMessageBody()));
        sms.setServiceCentreAddress(smsMessage.getServiceCenterAddress());
        sms.setEmailFrom(smsMessage.getEmailFrom());
        sms.setEmailBody(smsMessage.getEmailBody());
        sms.setPseudoSubject(smsMessage.getPseudoSubject());
        sms.setSentAt(smsMessage.getTimestampMillis());
        sms.setForwardedAt(System.currentTimeMillis());

        return sms;
    }
}
