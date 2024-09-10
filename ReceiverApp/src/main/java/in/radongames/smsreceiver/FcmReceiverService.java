package in.radongames.smsreceiver;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lombok.CustomLog;

@CustomLog
public class FcmReceiverService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        log.debug("Message From: " + message.getFrom());
        if (!message.getData().isEmpty()) {

            log.debug("Message data payload: " + message.getData());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {

        super.onNewToken(token);
    }
}
