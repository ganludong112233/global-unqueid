package com.uniqueid.core;

public class NoIdReturnException extends RuntimeException {
    private String snName;
    private static final long serialVersionUID = 271879222514692414L;

    public NoIdReturnException(String snName) {
        super();
        this.snName = snName;
    }

    public String getSnName() {
        return snName;
    }

    public void setSnName(String snName) {
        this.snName = snName;
    }

}
