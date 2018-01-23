package com.uniqueid.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class IDGeneratorConfiguration {
    private String YAML_CLASSPATH = "/id_generator.yaml";
    private String GENERTOR_TYPE_DB = "db";
    private String GENERTOR_TYPE_REDIS = "redis";
    private String GENERTOR_TYPE_LOCAL = "local";

    public DistributedIdGenerator buildDitributedIdGenerator() {
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        java.util.LinkedHashMap<String, List<Map<String, String>>> objects =
                (LinkedHashMap<String, List<Map<String, String>>>) yaml
                        .load(IDGeneratorConfiguration.class.getResourceAsStream(YAML_CLASSPATH));

        List<Map<String, String>> configs = objects.get("id.generator");

        List<IdGenerator> children = new ArrayList<IdGenerator>();
        for (Map<String, String> config : configs) {
            IdGenerator idGenerator = null;
            String type = config.get("type");
            if (type.equals(GENERTOR_TYPE_DB)) {
                idGenerator = buildDBGenerator(config);
            } else if (type.equals(GENERTOR_TYPE_LOCAL)) {
                idGenerator = buildLocalGenerator(config);
            } else if (type.equals(GENERTOR_TYPE_REDIS)) {
                idGenerator = buildRedisGenerator(config);
            }
            children.add(idGenerator);
        }

        DistributedIdGenerator distributedIdGenerator = new DistributedIdGenerator(children);
        return distributedIdGenerator;
    }

    private IdGenerator buildDBGenerator(Map<String, String> config) {
        String url = config.get("url");
        String username = config.get("username");
        String password = config.get("password");
        String driverClass = config.get("driverClass");

        SingleDBIDGenerator generator =
                new SingleDBIDGenerator(url, driverClass, username, password);
        return generator;
    }

    private IdGenerator buildRedisGenerator(Map<String, String> config) {
        throw new RuntimeException("Not Supported Redis GloalID generator.");
    }

    private IdGenerator buildLocalGenerator(Map<String, String> config) {
        LocalIdGenerator generator = new LocalIdGenerator();
        return generator;
    }
}
