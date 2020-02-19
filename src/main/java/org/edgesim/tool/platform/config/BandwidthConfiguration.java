package org.edgesim.tool.platform.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Save all latency configuration of entities.
 * Note: latency between two entity a -> b is the same as b -> a.
 * @author jfqiao
 * @since 2010/10/20
 */
public class BandwidthConfiguration {

    private static final Map<String, Double> latencyMap = new HashMap<>();
    private static final Map<Integer, Double> accessServerTransmission = new HashMap<>();

    public static double getBandwidth(Integer fromEntityId, Integer dstEntityId) {
        String key = genKey(fromEntityId, dstEntityId);
        Double latency = latencyMap.get(key);
        if (latency == null) {
            String fromName = MappingConfiguration.getEntityById(fromEntityId).getName();
//            String toName = MappingConfiguration.getEntityById(dstEntityId).getName();
            throw new RuntimeException(String.format("No latency configuration for entity %s",
                    fromName));
        }
        return latency;
    }

    public static void putBandwidth(Integer fromEntityId, Integer dstEntity, double bandwidth) {
        latencyMap.put(genKey(fromEntityId, dstEntity), bandwidth);
    }

    public static double getTransmissionRate(Integer edgeServerId) {
        return accessServerTransmission.get(edgeServerId);
    }

    public static void putTransmissionRate(Integer edgeServerId, double transmissionRate) {
        accessServerTransmission.putIfAbsent(edgeServerId, transmissionRate);
    }

    /**
     *
     * @param fromEntityId
     * @param dstEntityId
     * @return
     */
    private static String genKey(Integer fromEntityId, Integer dstEntityId) {
//        if (dstEntityId < fromEntityId) {
//            int tmp = dstEntityId;
//            dstEntityId = fromEntityId;
//            fromEntityId = tmp;
//        }
        return String.valueOf(fromEntityId) + "-" + dstEntityId;
    }
}

