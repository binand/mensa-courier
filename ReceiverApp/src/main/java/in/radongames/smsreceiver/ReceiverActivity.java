package in.radongames.smsreceiver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import dagger.hilt.android.AndroidEntryPoint;
import in.radongames.smsreceiver.databinding.ActivityReceiverBinding;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ReceiverActivity extends AppCompatActivity {

    private ActivityReceiverBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = ActivityReceiverBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

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

            copyFcmTokenToClipboard();
        }

        return true;
    }

    private void processLayout() {

        PrefManager pm = PrefManager.getInstance(this);
        mBinding.etServer.setText(pm.getServer());
        mBinding.etUsername.setText(pm.getUsername());
        mBinding.etPassword.setText(pm.getPassword());

        Button b = findViewById(R.id.b_save);
        b.setOnClickListener(v -> {

            PrefManager pm1 = PrefManager.getInstance(getApplicationContext());
            pm1.setServer(mBinding.etServer.getText().toString());
            pm1.setUsername(mBinding.etUsername.getText().toString());
            pm1.setPassword(mBinding.etPassword.getText().toString());
        });
    }

    protected void copyFcmTokenToClipboard() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            String token = task.getResult();
            log.debug("FCM Token: [" + token + "]");
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FCM Token", token);
            clipboardManager.setPrimaryClip(clip);
        });
    }
}
