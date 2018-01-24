package com.uniqueid.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author liuyi
 *
 */
public class DistributedIdGenerator implements IdGenerator, Runnable {
    private List<IdGenerator> deadIdGenerators;
    private List<IdGenerator> availableIdGenerators;
    private List<IdGenerator> allIdGenerators;
    private Thread heartbeatThread;
    int recoveryStopSeconds = 5;
    private int currentIdx = 0;
    private boolean lived = true;

    public DistributedIdGenerator(List<IdGenerator> idGenerators, Integer recoveryStopSeconds) {
        this.allIdGenerators = idGenerators;
        availableIdGenerators = allIdGenerators;
        deadIdGenerators = new ArrayList<IdGenerator>();
        this.recoveryStopSeconds = recoveryStopSeconds;
    }

    public Object getId(String snName) throws NoIdReturnException {
        // round-robin
        Object id = null;
        IdGenerator idg = null;
        while ((idg = this.getNext()) != null) {
            try {
                id = idg.getId(snName);
                if (id != null) break;
            } catch (NoIdReturnException e) {
                unavailable(idg);
            }
        }
        if (id == null) {
            throw new NoIdReturnException(snName,
                    "No id generator are avialable after round-robin all the generators");
        }
        return id;
    }

    public IdGenerator getNext() {
        int totalAvaSize = availableIdGenerators.size();
        if (totalAvaSize == 0) {
            return null;
        }
        if (currentIdx >= totalAvaSize) {
            // the next roud
            currentIdx = 0;
        }
        return availableIdGenerators.get(currentIdx++);
    }

    public void shutdown() {
        for (IdGenerator idGenerator : deadIdGenerators) {
            try {
                idGenerator.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lived = false;
        heartbeatThread.interrupt();// exit thread immediately
    }

    public void unavailable(IdGenerator idGenerator) {
        if (deadIdGenerators.contains(idGenerator)) {
            // for concurrency call check
            return;
        }
        availableIdGenerators.remove(idGenerator);
        deadIdGenerators.add(idGenerator);
        idGenerator.shutdown();
    }

    public void start() {
        for (IdGenerator idGenerator : allIdGenerators) {
            try {
                idGenerator.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // start the recovery thread
        heartbeatThread = new Thread(this);
        heartbeatThread.setName("Global Unique Id Id generator Hearbeat thread");
        heartbeatThread.start();
    }

    public void run() {
        while (lived) {
            // do not use use list to avoid occuring unModified exception while do loop with
            // iterator
            IdGenerator[] unModifiedDeadList = deadIdGenerators.toArray(new IdGenerator[] {});
            for (IdGenerator idg : unModifiedDeadList) {
                try {
                    // restart the id generator
                    idg.start();
                    if (idg.isAlived()) {
                        deadIdGenerators.remove(idg);
                        availableIdGenerators.add(idg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(recoveryStopSeconds * 1000);
            } catch (InterruptedException e) {}
        }
    }

    public boolean isAlived() {
        return lived && !availableIdGenerators.isEmpty();
    }

}
