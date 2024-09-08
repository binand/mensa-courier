package org.subinium.smstoemail;

import com.radongames.android.logger.Level;
import com.radongames.android.logger.LoggerConfig;
import com.radongames.android.logger.MensaLogger;

public class S2ELogger {

    private static final LoggerConfig sLoggerConfig = new LoggerConfig() {
        @Override
        public boolean isRunningTests() {
            return false;
        }

        @Override
        public boolean isDebugBuild() {
            return true;
        }
    };
    public static MensaLogger getLogger(final Class<?> type) {

        MensaLogger logger = new MensaLogger(sLoggerConfig.getLogtag(23, "S2E/", type.getSimpleName()), sLoggerConfig.isDebugBuild(), sLoggerConfig.isRunningTests());
        logger.setLevel(Level.DEBUG);
        return logger;
    }
}
