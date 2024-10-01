package com.radongames.smslib;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

import lombok.CustomLog;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/*
 * Save logcat on a per-run (Application class instantiation) basis.
 * Logs are saved in Context.getFilesDir() [/data/data/in.radongames.smsreceiver/files]
 * A subdirectory logs/ is created and the file within is named logcat.log.
 * This does not require any additional permissions.
 * But there is no check on the file size.
 */
@CustomLog
@NoArgsConstructor
public class FileLogger {

    // We will use: Context.getFilesDir() = /data/data/in.radongames.smsreceiver/files
    @SneakyThrows
    public void init(Context ctx) {

        File logFile = getLogfile(ctx);
        if (logFile == null) {

            log.debug("Unable to log to file.");
            return;
        }

        // A trick to truncate the file.
        try (FileOutputStream fo = new FileOutputStream(logFile)) {

            fo.flush();
        }

        Thread t = new Thread(() -> runLogcat(logFile));
        t.start();
    }

    @SneakyThrows
    private File getLogfile(Context ctx) {

        File base = ctx.getFilesDir();
        if (!base.exists()) {

            log.debug("Path: " + base.getCanonicalPath() + " does not exist.");
            return null;
        }

        File logDir = new File(base, "/logs");
        if (!logDir.exists()) {

            if (!logDir.mkdir()) {

                log.debug("Creating: " + logDir.getCanonicalPath() + " failed.");
                return null;
            }
        }

        return new File(logDir, "/logcat.log");
    }

    @SneakyThrows
    private void runLogcat(File logDest) {

        Runtime.getRuntime().exec("logcat -f " + logDest);
    }
}