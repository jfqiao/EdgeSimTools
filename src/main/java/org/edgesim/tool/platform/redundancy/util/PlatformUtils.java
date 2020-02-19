package org.edgesim.tool.platform.redundancy.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PlatformUtils {
    private static int ENTITY_CNT = 2;
    private static int REQUEST_CNT = 1;
    private static int APPLICATION_CNT = 1;
    private static int APPLICATION_SERVICE_CNT = 1;

    private static int PROBLEM_CNT = 0;

    public static int RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM = PROBLEM_CNT++;

    public static int BURST_LOAD_EVACUATION = PROBLEM_CNT++;

    public static int PROBLEM_TYPE = RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM;
    public static String PATH;

    static {
        File file = new File("abc.txt");
        String path = file.getAbsolutePath();
        PATH = path.substring(0, path.lastIndexOf("abc.txt"));
    }

    public static int generateEntityId() {
        return ENTITY_CNT++;
    }

    public static int generateRequestId() {
        return REQUEST_CNT++;
    }

    public static int generateApplicationId() {
        return APPLICATION_CNT++;
    }

    public static int generateApplicationServiceId() {
        return APPLICATION_SERVICE_CNT++;
    }

    public static String getPathInResources(String fileName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return cl.getResource(fileName).getPath();
    }

    public static ImageIcon getImageIconByPath(String path) {
        return new ImageIcon(
                Toolkit.getDefaultToolkit()
                        .getImage(PlatformUtils.getPathInResources(path))
                        .getScaledInstance(40, 40, Image.SCALE_DEFAULT));
    }

    public static String buildProbabilityKey(int i, int j, int k) {
        return String.valueOf(i) + "-" + j + "-" + k;
    }

    public static void writeNum(String path, double num) {
        File file = new File(path);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            pw.println(num);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String formatDoubleData(double data) {
        return String.format("%.4f", data);
    }

}
