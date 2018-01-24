package com.uniqueid.core;

public class IdGeneratorService {
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
        distributedGenerator.start();
    }

    public Object getId(String snName) throws NoIdReturnException {
        return distributedGenerator.getId(snName);
    }

    public void shutdown() {
        distributedGenerator.shutdown();
    }
}
