package com.uniqueid.core;

public interface IdGenerator extends Shutdown {
    /**
     * 
     * @param snName
     * @return
     */
    public long getId(String snName);
}
