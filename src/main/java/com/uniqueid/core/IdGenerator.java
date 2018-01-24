package com.uniqueid.core;

public interface IdGenerator extends LifeCycle {
    /**
     * 
     * @param snName
     * @return
     */
    public Object getId(String snName) throws NoIdReturnException;
}
