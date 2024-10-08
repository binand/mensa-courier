package in.radongames.smsforwarder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.radongames.android.platform.Toaster;
import com.radongames.core.interfaces.EncoderDecoder;
import com.radongames.core.string.CharNames;
import com.radongames.core.string.Padder;
import com.radongames.core.string.TextUtils;
import com.radongames.smslib.SharedPreferencesBag;
import com.radongames.smslib.SmsContents;

import java.io.Serializable;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import in.radongames.smsforwarder.databinding.ActivityForwarderBinding;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ForwarderActivity extends AppCompatActivity {

    private static final String SMS_PERMISSION_NAME = "android.permission.RECEIVE_SMS";
    private static final int SMS_PERMISSION_REQ_CODE = 1;

    private static final long THREE_HOURS_IN_MILLIS = (60 * 60 * 3 * 1000);

    private ActivityForwarderBinding mBinding;

    @Inject
    Toaster mToaster;

    @Inject
    SharedPreferencesBag mTokensBag;

    @Inject
    MessageForwarder mForwarder;

    @Inject
    EncoderDecoder<Serializable> mEncoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = ActivityForwarderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mTokensBag.store(BagKeyNames.Key_OWN_NUMBER, "+917760992800");

        if (checkCallingOrSelfPermission(SMS_PERMISSION_NAME) != PackageManager.PERMISSION_GRANTED) {

            log.debug("Runtime permission required");
            requestPermissions(new String[]{SMS_PERMISSION_NAME}, SMS_PERMISSION_REQ_CODE);
        }

        mBinding.tvToken.setText(mTokensBag.retrieve(BagKeyNames.KEY_FCM_TOKEN, mBinding.tvToken.getText().toString()));

        mBinding.bPaste.setOnClickListener(v -> pasteFcmTokenFromClipboard());
        mBinding.bClear.setOnClickListener(v -> clearStoredToken());

        mBinding.tvSentSeqs.setText(mTokensBag.retrieve("test-sms-seq-list"));

        mBinding.bSendTest.setOnClickListener(v -> {

            long now = System.currentTimeMillis();

            String seq = Padder.pad3((int)(now % 1000));
            SmsContents sms = new SmsContents();
            sms.setOriginatingAddress("+919876543210");
            sms.setDisplayOriginatingAddress("+919876543210");
            sms.setMessageBody(mEncoder.encode("Test " + seq + " " + "https://www.google.com"));
            sms.setDisplayMessageBody(mEncoder.encode("Test " + seq + " " + "https://www.google.com"));
            sms.setSentAt(now - THREE_HOURS_IN_MILLIS);
            sms.setForwardedAt(now);

            mForwarder.forward(sms);
            mToaster.show("Test: " + seq);
            mTokensBag.merge("test-sms-seq-list", seq);
            mBinding.tvSentSeqs.setText(mTokensBag.retrieve("test-sms-seq-list"));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Note: From https://developer.android.com/reference/android/app/Activity
        // It is possible that the permissions request interaction with the user is interrupted.
        // In this case you will receive empty permissions and results arrays which should be treated as a cancellation.

        String msg = "Permission: " + permissions[0] + CharNames.SPACE + (grantResults[0] == PackageManager.PERMISSION_GRANTED ? "granted" : "denied");
        log.debug(msg);
        mToaster.show(msg);
    }

    private void pasteFcmTokenFromClipboard() {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (!clipboardManager.hasPrimaryClip()) {

            mToaster.show("Nothing in clipboard.");
            return;
        }

        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) {

            mToaster.show("Nothing in clipboard.");
            return;
        }

        String token = clipData.getItemAt(0).getText().toString();
        if (!isValidToken(token)) {

            mToaster.show("Clipboard does not contain a token.");
            return;
        }

        log.debug("Clipboard Token: [" + token + "]");

        mBinding.tvToken.setText(token);

        if (mTokensBag.has(BagKeyNames.KEY_FCM_TOKEN)) {

            log.debug("A token is already stored. Clear it first.");
        } else {

            mTokensBag.store(BagKeyNames.KEY_FCM_TOKEN, token);
        }

        mTokensBag.dump(log);
    }

    private void clearStoredToken() {

        mTokensBag.discard(BagKeyNames.KEY_FCM_TOKEN);
        mBinding.tvToken.setText(R.string.activity_forwarder_no_token);

    }

    private boolean isValidToken(String token) {

        return !TextUtils.isEmpty(token);
    }
}
