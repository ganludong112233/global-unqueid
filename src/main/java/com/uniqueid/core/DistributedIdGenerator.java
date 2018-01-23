package com.uniqueid.core;

import java.util.List;

public class DistributedIdGenerator implements IdGenerator {
    List<IdGenerator> idGenerators = null;
    int currentIdx = 0;
    int total = 0;

    public DistributedIdGenerator(List<IdGenerator> idGenerators) {
        this.idGenerators = idGenerators;
        total = this.idGenerators.size();
    }

    public long getId(String snName) {
        // round-robin
        return this.getNext().getId(snName);
    }

    public IdGenerator getNext() {
        if (currentIdx == total) {
            // the next roud
            currentIdx = 0;
        }
        return idGenerators.get(currentIdx++);
    }

    public void shutdown() {
        for (IdGenerator idGenerator : idGenerators) {
            idGenerator.shutdown();
        }
    }

}
