package org.battleshipgame.reporter;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("class")
    private String className;
    @SerializedName("trace")
    private String stackTrace;
    private int line;
    private String method;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
