package org.battleshipgame.reporter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Report {
    private Author user;
    private Error error;
    private String message;
    private Date time;

    private Report(String name, String ip) {
        user = new Author();
        user.setName(name);
        user.setIp(ip);
        message = "";
        time = new Date(System.currentTimeMillis());
    }

    public Report(String name, String ip, String message) {
        this(name, ip);
        this.message = message;
    }

    public Report(String name, String ip, Throwable throwable) {
        this(name, ip);
        this.message = throwable.getMessage();
        error = new Error();

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            StackTraceElement stackTrace = stackTraceElements[0];
            error.setLine(stackTrace.getLineNumber());
            error.setClassName(stackTrace.getClassName());
            error.setMethod(stackTrace.getMethodName());

            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            error.setStackTrace(stringWriter.toString());
        }
    }

    public Author getUser() {
        return user;
    }

    public Error getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }
}
