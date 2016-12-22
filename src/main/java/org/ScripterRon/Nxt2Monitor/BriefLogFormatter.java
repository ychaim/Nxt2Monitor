/*
 * Copyright 2016 Ronald W Hoffman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ScripterRon.Nxt2Monitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A Java logging formatter that writes more compact output than the default.
 */
public class BriefLogFormatter extends Formatter {

    /** Format used for log messages */
    private static final MessageFormat messageFormat = new MessageFormat("{3,date,hh:mm:ss} {0} {1}.{2}: {4}\n{5}");

    /** We need to keep a reference to our custom logger */
    private static final Logger logger = Logger.getLogger("");

    /**
     * Configures JDK logging to use this class for everything
     */
    public static void init() {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers)
            handler.setFormatter(new BriefLogFormatter());
    }

    /**
     * Format the log record as follows:
     *
     *     Date ThreadID Class Method Message ExceptionTrace
     *
     * The class name is shortened to the portion after the last period
     *
     * @param       logRecord       The log record
     * @return                      The formatted string
     */
    @Override
    public String format(LogRecord logRecord) {
        Object[] arguments = new Object[6];
        arguments[0] = logRecord.getLevel().getName();
        String fullClassName = logRecord.getSourceClassName();
        int lastDot = fullClassName.lastIndexOf('.');
        String className = fullClassName.substring(lastDot + 1);
        arguments[1] = className;
        arguments[2] = logRecord.getSourceMethodName();
        arguments[3] = new Date(logRecord.getMillis());
        arguments[4] = logRecord.getMessage();
        if (logRecord.getThrown() != null) {
            Writer result = new StringWriter();
            logRecord.getThrown().printStackTrace(new PrintWriter(result));
            arguments[5] = result.toString();
        } else {
            arguments[5] = "";
        }

        return messageFormat.format(arguments);
    }
}
