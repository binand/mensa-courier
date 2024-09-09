package in.radongames.smsforwarder;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.SneakyThrows;

public class FcmSender {

    public FcmSender(@ApplicationContext Context ctx) {

        FirebaseApp.initializeApp();
    }

    @SneakyThrows
    public void send() {

        // This registration token comes from the client FCM SDKs.
        String registrationToken = "YOUR_REGISTRATION_TOKEN";

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);
    }
}
