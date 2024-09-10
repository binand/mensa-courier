package in.radongames.smsreceiver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.radongames.android.platform.Toaster;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ReceiverActivity extends AppCompatActivity {

    @Inject
    Toaster mToaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        processLayout();
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
}
