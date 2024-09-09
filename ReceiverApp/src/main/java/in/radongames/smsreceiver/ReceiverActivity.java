package in.radongames.smsreceiver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.radongames.android.platform.Toaster;
import com.radongames.core.string.CharNames;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ReceiverActivity extends AppCompatActivity {

    private static final String SMS_PERMISSION_NAME = "android.permission.RECEIVE_SMS";
    private static final int SMS_PERMISSION_REQ_CODE = 1;

    @Inject
    Toaster mToaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_receiver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_copy_token) {

            copyMyFcmTokenToClipboard();
        }

        return true;
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

    protected void copyMyFcmTokenToClipboard() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            String token = task.getResult();
            log.debug("FCM Token: [" + token + "]");
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FCM Token", token);
            clipboardManager.setPrimaryClip(clip);
        });
    }

    protected void pastePeersFcmTokenFromClipboard() {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {

            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            String token = item.getText().toString();
            log.debug("Remote Token: [" + token + "]");
        }
    }
}
