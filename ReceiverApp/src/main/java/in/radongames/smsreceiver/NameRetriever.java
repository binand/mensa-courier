package in.radongames.smsreceiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class NameRetriever {

    private final ContentResolver mResolver;

    @Inject
    public NameRetriever(@ApplicationContext Context ctx) {

        mResolver = ctx.getContentResolver();
    }

    public String retrieve(String phoneNumber) {

        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = mResolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);

        String contactName = null;
        if (cursor != null && cursor.moveToFirst()) {

            int colIdx = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
            if (colIdx < 0) {

                return null;
            }

            contactName = cursor.getString(colIdx);
            if (!cursor.isClosed()) {

                cursor.close();
            }
        }

        return contactName;
    }
}
