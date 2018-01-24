package com.uniqueid.core;

public interface LifeCycle {
    /**
     * start the id generator
     */
    public void start();

    /**
     * check does the real id generator service are work
     * 
     * @return
     */
    public boolean isAlived();

    /**
     * stop the id generator
     */
    public void shutdown();
}
