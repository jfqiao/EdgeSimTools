package org.edgesim.tool.platform.redundancy.jsoninfo;

import lombok.Data;

@Data
public class AppInfo {
    private String name;
    private Node[] nodes;

    @Data
    public static class Node {
        private int id;
        private String name;
        private double inputSize;
        private double outputSize;
    }
}
