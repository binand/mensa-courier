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
import com.radongames.core.string.CharNames;
import com.radongames.core.string.TextUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import in.radongames.smsforwarder.databinding.ActivityForwarderBinding;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ForwarderActivity extends AppCompatActivity {

    private static final String SMS_PERMISSION_NAME = "android.permission.RECEIVE_SMS";
    private static final int SMS_PERMISSION_REQ_CODE = 1;

    private ActivityForwarderBinding mBinding;

    @Inject
    Toaster mToaster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = ActivityForwarderBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (checkCallingOrSelfPermission(SMS_PERMISSION_NAME) != PackageManager.PERMISSION_GRANTED) {

            log.debug("Runtime permission required");
            requestPermissions(new String[]{SMS_PERMISSION_NAME}, SMS_PERMISSION_REQ_CODE);
        }

        mBinding.bPaste.setOnClickListener(v -> pasteFcmTokenFromClipboard());
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
        doPaste(token);
    }

    private boolean isValidToken(String token) {

        return !TextUtils.isEmpty(token);
    }

    private void doPaste(String token) {

        mBinding.tvPastedToken.setText(token);
    }
}
