package com.hazelsuite.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {


    private Log() {
        throw new UnsupportedOperationException("Log class cannot be instantiated");
    }


    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    //Info Level Logs
    public static void info(String message) {
        logger.info(message);
    }

    //Warn Level Logs
    public static void warn(String message) {
        logger.warn(message);
    }

    //Error Level Logs
    public static void error (String message) {
        logger.error(message);
    }

}

