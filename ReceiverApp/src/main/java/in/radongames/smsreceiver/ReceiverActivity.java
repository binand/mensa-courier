package in.radongames.smsreceiver;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.radongames.android.olmur.OlmurDriver;
import com.radongames.android.olmur.components.SwipeContextMenu;
import com.radongames.android.olmur.property.ItemViewDecorator;
import com.radongames.android.olmur.recyclerview.OlmurLayoutManager;
import com.radongames.android.olmur.recyclerview.OlmurRecyclerView;
import com.radongames.android.platform.Threader;
import com.radongames.android.platform.Toaster;
import com.radongames.core.string.CharNames;
import com.radongames.smslib.SmsContents;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import in.radongames.smsreceiver.databinding.ActivityReceiverBinding;
import lombok.CustomLog;

@AndroidEntryPoint
@CustomLog
public class ReceiverActivity extends AppCompatActivity {

    private static final String CONTACTS_PERMISSION_NAME = "android.permission.READ_CONTACTS";
    private static final int CONTACTS_PERMISSION_REQ_CODE = 1;

    private ActivityReceiverBinding mBinding;

    @Inject
    SmsDisplayAdapter mAdapter;

    @Inject
    SmsViewModel mSmsModel;

    @Inject
    Threader mThreader;

    @Inject
    Toaster mToaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mBinding = ActivityReceiverBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (checkCallingOrSelfPermission(CONTACTS_PERMISSION_NAME) != PackageManager.PERMISSION_GRANTED) {

            log.debug("Runtime permission required");
            requestPermissions(new String[]{CONTACTS_PERMISSION_NAME}, CONTACTS_PERMISSION_REQ_CODE);
        }

        setupMessagesView();
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

            copyFcmTokenToClipboard();
        }

        return true;
    }

    private void setupMessagesView() {

        OlmurRecyclerView rv = mBinding.rvMessagesList;

        rv.setLayoutManager(new OlmurLayoutManager(this));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new ItemViewDecorator() {
            @Override
            public void setViewProperties(View view, OlmurRecyclerView parent) {

                int position = parent.getChildAdapterPosition(view);
                view.setBackgroundResource(position % 2 == 0 ? R.color.message_even : R.color.message_odd);
            }
        });

        OlmurDriver driver = new OlmurDriver.Builder()
                .withSwipeRightAction(this::handleSwipe)
                .withSwipeLeftAction(this::handleSwipe)
                .withSwipeContextMenuDrawer(new SwipeContextMenu.Builder(this)
                        .withBackgroundsColorsRes(R.color.colorPrimary, R.color.colorPrimary)
                        .build())
                .build();
        driver.bind(rv);

        mSmsModel.getCount().observe(this, count -> log.debug("Loaded db with: " + count));
        mSmsModel.getMessages().observe(this, smsContents -> mAdapter.setMessages(smsContents));
    }

    private void handleSwipe(int position) {

        log.debug("handleSwipe(): Deleting: " + position);
        new AlertDialog.Builder(this)
                .setTitle("Delete message #" + (position + 1) + "?")
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> removeMessage(position))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> handleCancel(position))
                .setOnCancelListener(dialog -> handleCancel(position))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void removeMessage(int position) {

        final SmsContents message = mAdapter.removeMessage(position);
        mThreader.runOnNewThread(() -> mSmsModel.deleteMessage(message));
        log.debug("removeMessage(): Deleted: " + message);
    }

    private void handleCancel(int position) {

        mAdapter.notifyItemChanged(position);
        log.debug("handleCancel(): Not deleted: " + position);
    }

    private void copyFcmTokenToClipboard() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            String token = task.getResult();
            log.debug("FCM Token: [" + token + "]");
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FCM Token", token);
            clipboardManager.setPrimaryClip(clip);
        });
    }
}
