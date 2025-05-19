package projecthds.herodotusutils.util;

import projecthds.herodotusutils.HerodotusUtils;
import projecthds.herodotusutils.config.HDSUConfig;

public class LogHelper {

    public static void debug(String message, Object... params) {
        if (HDSUConfig.debug) {
            HerodotusUtils.logger.info(message, params);
        }
    }

    public static void info(String message, Object... params) {
        HerodotusUtils.logger.info(message, params);
    }

    public static void warn(String message, Object... params) {
        HerodotusUtils.logger.warn(message, params);
    }

    public static void error(String message, Object... params) {
        HerodotusUtils.logger.error(message, params);
    }
}