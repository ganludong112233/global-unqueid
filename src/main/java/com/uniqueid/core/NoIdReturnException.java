package com.uniqueid.core;

public class NoIdReturnException extends Exception {
    private String snName;
    private static final long serialVersionUID = 271879222514692414L;

    public NoIdReturnException(String snName) {
        super();
        this.snName = snName;
    }

    public NoIdReturnException(String snName, String message) {
        super(message);
        this.snName = snName;
    }

    public NoIdReturnException(String snName, Exception exception) {
        super(exception);
        this.snName = snName;
    }

    public String getSnName() {
        return snName;
    }

    public void setSnName(String snName) {
        this.snName = snName;
    }

}
