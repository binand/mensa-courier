package in.radongames.smsforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;
import lombok.CustomLog;

@CustomLog
public class SmsReceiver extends BroadcastReceiver {

    private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    @Override
    public void onReceive(Context ctx, Intent intent) {

        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        if (bundle == null || action == null) {

            return;
        }

        if (action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            Log.d("S2E", "Received an SMS");

            // Original processing of SMS.
            Object[] allPdus = (Object[]) bundle.get("pdus");
            String format = bundle.getString("format");

            if (allPdus != null) {

                Set<String> senderSet = new HashSet<String>();
                StringBuilder bodyBuilder = new StringBuilder();
                long minTime = Long.MAX_VALUE;
                for (int i = 0; i < allPdus.length; i++) {

                    SmsMessage smsMessage = createMessage(allPdus[i], format);
                    if (smsMessage == null) {

                        continue;
                    }

                    logMessage(smsMessage);

                    String sender = smsMessage.getOriginatingAddress();
                    if (sender != null) {

                        senderSet.add(smsMessage.getOriginatingAddress());
                    }

                    String body = smsMessage.getMessageBody();
                    bodyBuilder.append(body);

                    long thisTime = smsMessage.getTimestampMillis();
                    if (thisTime < minTime) {

                        minTime = thisTime;
                    }
                }

//                sendEmail(ctx, TextUtils.join(",", senderSet), bodyBuilder.toString(), getFormattedTime(minTime));
            }
        }
    }

    private SmsMessage createMessage(Object pdu, String format) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return SmsMessage.createFromPdu((byte[]) pdu, format);
        } else {

            return SmsMessage.createFromPdu((byte[]) pdu);
        }
    }

    private String getFormattedTime(long timestamp) {

        return DEFAULT_DATE_FORMAT.format(new Date(timestamp));
    }

    private void logMessage(SmsMessage smsMessage) {

        log.debug("Originating Address : " + smsMessage.getOriginatingAddress());
        log.debug("Display Address     : " + smsMessage.getDisplayOriginatingAddress());
        log.debug("Message Body        : " + smsMessage.getMessageBody());
        log.debug("Display Body        : " + smsMessage.getDisplayMessageBody());
        log.debug("Service Centre      : " + smsMessage.getServiceCenterAddress());
        log.debug("Email From          : " + smsMessage.getEmailFrom());
        log.debug("Email Body          : " + smsMessage.getEmailBody());
        log.debug("Pseudo Subject      : " + smsMessage.getPseudoSubject());
        log.debug("Timestamp           : " + ZonedDateTime.ofInstant(Instant.ofEpochMilli(smsMessage.getTimestampMillis()), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
    }

    private void sendEmail(Context ctx, String sender, String body, String time) {

        Log.d("S2E", "From: " + sender);

        PrefManager pm = PrefManager.getInstance(ctx);
        String email = pm.getUsername();

        MaildroidX.Builder b = new MaildroidX.Builder()
                .smtp(pm.getServer())
                .smtpUsername(email)
                .smtpPassword(pm.getPassword())
                .port("465")
                .type(MaildroidXType.PLAIN)
                .to(email)
                .from(email)
                .subject("SMS From: " + sender)
                .body("Received At: " + time + "\n===============\n" + body)
                .onCompleteCallback(new MaildroidX.onCompleteCallback() {
                    @Override
                    public void onSuccess() {

                        Log.d("S2E", "Email succeeded");
                    }

                    @Override
                    public void onFail(String s) {

                        Log.d("S2E", "Email failed");
                    }

                    @Override
                    public long getTimeout() {

                        return 60000; // 1 minute
                    }
                });
        b.mail();
    }
}
