package org.edgesim.tool.platform.redundancy.log;

import org.edgesim.tool.platform.redundancy.gui.EdgeGUI;

public class Logger {
    public static EdgeGUI edgeGUI;

    public static void log(String info) {
        if (edgeGUI != null) {
            edgeGUI.getTextArea().append(info + "\n");
        }
        System.out.println(info);
    }
}
