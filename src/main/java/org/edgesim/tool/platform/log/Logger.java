package org.edgesim.tool.platform.log;

import org.edgesim.tool.platform.gui.EdgeGUI;

public class Logger {
    public static EdgeGUI edgeGUI;

    public static void log(String info) {
        if (edgeGUI != null) {
            edgeGUI.getTextArea().append(info + "\n");
        }
        System.out.println(info);
    }
}
