package org.subinium.smstoemail;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.radongames.android.platform.Toaster;
import com.radongames.core.string.CharNames;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class SmsToEmailActivity extends AppCompatActivity {

    private static final String SMS_PERMISSION_NAME = "android.permission.RECEIVE_SMS";
    private static final int SMS_PERMISSION_REQ_CODE = 1;

    @Inject
    Toaster mToaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms2email);

        processLayout();

        if (checkCallingOrSelfPermission(SMS_PERMISSION_NAME) != PackageManager.PERMISSION_GRANTED) {

            log.debug("Runtime permission required");
            requestPermissions(new String[]{SMS_PERMISSION_NAME}, SMS_PERMISSION_REQ_CODE);
        }
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

    private void processLayout() {

        final EditText etServer = findViewById(R.id.et_server);
        final EditText etUsername = findViewById(R.id.et_username);
        final EditText etPassword = findViewById(R.id.et_password);

        PrefManager pm = PrefManager.getInstance(this);
        etServer.setText(pm.getServer());
        etUsername.setText(pm.getUsername());
        etPassword.setText(pm.getPassword());

        Button b = findViewById(R.id.b_save);
        b.setOnClickListener(v -> {

            PrefManager pm1 = PrefManager.getInstance(getApplicationContext());
            pm1.setServer(etServer.getText().toString());
            pm1.setUsername(etUsername.getText().toString());
            pm1.setPassword(etPassword.getText().toString());
        });
    }
}
