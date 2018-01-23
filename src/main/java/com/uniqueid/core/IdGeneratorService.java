package com.uniqueid.core;

public class IdGeneratorService implements IdGenerator {
    private DistributedIdGenerator distributedGenerator;
    private static IdGeneratorService instance;
    static {
        instance = new IdGeneratorService();
    }

    public static IdGeneratorService getInstance() {
        return instance;
    }

    private IdGeneratorService() {
        IDGeneratorConfiguration config = new IDGeneratorConfiguration();
        distributedGenerator = config.buildDitributedIdGenerator();
    }

    public long getId(String snName) {
        return distributedGenerator.getId(snName);
    }

    public void shutdown() {
        distributedGenerator.shutdown();
    }
}
