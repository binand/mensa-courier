package in.radongames.smsforwarder;

import android.content.Context;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.radongames.android.platform.Threader;
import com.radongames.json.interfaces.JsonSerializable;
import com.radongames.smslib.SharedPreferencesBag;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.CustomLog;
import lombok.SneakyThrows;

@Singleton
@CustomLog
public class FcmMessageForwarder implements MessageForwarder {

    private final SharedPreferencesBag mTokensBag;
    private final Threader mThreader;

    @Inject
    FcmMessageForwarder(@ApplicationContext Context ctx, SharedPreferencesBag bag, Threader t) {

        GoogleCredentials creds;
        try (InputStream ins = ctx.getResources().openRawResource(R.raw.mensa_firebase_key)) {

            creds = GoogleCredentials.fromStream(ins);
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(creds).build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        mTokensBag = bag;
        mThreader = t;
    }

    public void forward(JsonSerializable<?> msg) {

        log.debug("Message: " + msg.toJson());

        String token = mTokensBag.retrieve(Constants.FCM_TOKEN_HOLDING_KEY);
        if (token == null) {

            log.debug("Nowhere to forward to, skipping this one.");
            return;
        }

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("sms_payload", msg.toJson())
                .setToken(token)
                .build();

        mThreader.runOnNewThread(() -> doForward(message));
    }

    @SneakyThrows
    private void doForward(Message message) {

        String response = FirebaseMessaging.getInstance().send(message);
        log.debug("Sent message with ID: " + response);
    }
}
