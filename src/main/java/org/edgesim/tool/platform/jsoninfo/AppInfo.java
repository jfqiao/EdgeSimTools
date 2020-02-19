package org.edgesim.tool.platform.jsoninfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInfo {
    private String name;
    private Node[] nodes;
    private Edge[] edges;

    @Getter @Setter
    public static class Edge {
        private String from;
        private String to;
    }

    @Getter @Setter
    public static class Node {
        private String name;
        private double inputSize;
        private double outputSize;
    }
}
