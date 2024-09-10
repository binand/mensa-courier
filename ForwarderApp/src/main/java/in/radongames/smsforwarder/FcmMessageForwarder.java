package in.radongames.smsforwarder;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.radongames.json.interfaces.JsonSerializable;
import com.radongames.smslib.SharedPreferencesBag;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.CustomLog;
import lombok.SneakyThrows;

@Singleton
@CustomLog
public class FcmMessageForwarder implements MessageForwarder {

    @Inject
    SharedPreferencesBag mTokensBag;

    @Inject
    public FcmMessageForwarder(@ApplicationContext Context ctx) {

        FirebaseApp.initializeApp();
    }

    @SneakyThrows
    public void forward(JsonSerializable<?> msg) {

        log.debug("Message: " + msg);
        log.debug("Message: " + msg.toJson());

        return;

        // This registration token comes from the client FCM SDKs.
//        String registrationToken = "YOUR_REGISTRATION_TOKEN";

        // See documentation on defining a message payload.
//        Message message = Message.builder()
//                .putData("score", "850")
//                .putData("time", "2:45")
//                .setToken(registrationToken)
//                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
//        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
//        System.out.println("Successfully sent message: " + response);
    }
}
