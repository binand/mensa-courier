package com.radongames.smslib;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.radongames.core.debug.MapDumper;
import com.radongames.core.interfaces.Bag;
import com.radongames.core.string.CharNames;

import java.io.PrintStream;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.NonNull;

@Singleton
public class SharedPreferencesBag implements Bag<String> {

    private final SharedPreferences mPrefs;

    @Inject
    public SharedPreferencesBag(@ApplicationContext Context ctx) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    public void store(@NonNull String key, @NonNull String val) {

        if (!has(key)) {

            mPrefs.edit().putString(key, val).apply();
        }
    }

    @Override
    public void replace(@NonNull String key, @NonNull String val) {

        mPrefs.edit().putString(key, val).apply();
    }

    @Override
    public String retrieve(@NonNull String key, String val) {

        return mPrefs.getString(key, val);
    }

    @Override
    public String extract(@NonNull String key, String val) {

        String retVal = val;
        if (has(key)) {

            retVal = mPrefs.getString(key, val);
            discard(key);
        }

        return retVal;
    }

    @Override
    public void discard(@NonNull String key) {

        mPrefs.edit().remove(key).apply();
    }

    @Override
    public void merge(@NonNull String key, @NonNull String val) {

        if (has(key)) {

            String storedVal = retrieve(key);
            if (storedVal != null) {

                replace(key, storedVal + CharNames.COMMA + val);
                return;
            }
        }

        store(key, val);
    }

    @Override
    public Iterator<String> keys() {

        return mPrefs.getAll().keySet().iterator();
    }

    @Override
    public boolean has(@NonNull String key) {

        return mPrefs.getAll().containsKey(key);
    }

    @Override
    public int size() {

        return mPrefs.getAll().size();
    }

    @Override
    public void clear() {

        mPrefs.edit().clear().apply();
    }

    public void dump(PrintStream str) {

        new MapDumper().dump(mPrefs.getAll(), str);
    }
}
