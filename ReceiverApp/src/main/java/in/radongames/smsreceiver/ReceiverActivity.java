package in.radongames.smsreceiver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.radongames.android.olmur.recyclerview.OlmurLayoutManager;
import com.radongames.android.olmur.recyclerview.OlmurRecyclerView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import in.radongames.smsreceiver.databinding.ActivityReceiverBinding;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ReceiverActivity extends AppCompatActivity {

    private ActivityReceiverBinding mBinding;

    @Inject
    SmsDisplayAdapter mAdapter;

    @Inject
    SmsViewModel mSmsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = ActivityReceiverBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setupMessagesView();
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

    private void setupMessagesView() {

        OlmurRecyclerView rv = mBinding.rvMessagesList;

        rv.setLayoutManager(new OlmurLayoutManager(this));
        rv.setAdapter(mAdapter);

        mSmsModel.getCount().observe(this, count -> log.debug("Loaded db with: " + count));
        mSmsModel.getMessages().observe(this, smsContents -> mAdapter.setMessages(smsContents));
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
