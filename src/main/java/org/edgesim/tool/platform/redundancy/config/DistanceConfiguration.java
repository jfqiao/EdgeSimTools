package org.edgesim.tool.platform.redundancy.config;


import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: DistanceConfiguration
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-12 18:41
 * @Version: 1.0
 */
public class DistanceConfiguration {

    public static final Map<String, Double> latencyMap = new HashMap<>();

    public static double getDistance(Integer fromEntityId, Integer dstEntityId){
        String key = genKey(fromEntityId, dstEntityId);
        Double latency = latencyMap.get(key);
        if (latency == null) {
            String fromName = MappingConfiguration.getEntityById(fromEntityId).getName();
            String toName = MappingConfiguration.getEntityById(dstEntityId).getName();
            throw new RuntimeException(String.format("No latency configuration for entity %s to %s",
                    fromName, toName));
        }
        return latency;
    }

    public static void putDistance(Integer fromEntityId, Integer dstEntity, double distance) {
        latencyMap.put(genKey(fromEntityId, dstEntity), distance);
        latencyMap.put(genKey(dstEntity, fromEntityId), distance);
    }

    private static String genKey(Integer fromEntityId, Integer dstEntityId) {

        return fromEntityId + "-" + dstEntityId;
    }

}
