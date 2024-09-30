package in.radongames.smsreceiver;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.radongames.smslib.SmsContents;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.CustomLog;

@CustomLog
@AndroidEntryPoint
public class FcmReceiverService extends FirebaseMessagingService {

    @Inject
    SmsRepository mRepository;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage rm) {

        log.debug("Message From: " + rm.getFrom());
        if (rm.getData().isEmpty()) {

            log.debug("Data is empty. Quitting.");
        }

        SmsContents message = new SmsContents();
        log.debug("Incoming payload: " + rm.getData().get("sms_payload"));
        message.applyJson(rm.getData().get("sms_payload"));
        log.debug("Message retrieved: " + message);

        mRepository.save(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);
    }
}
