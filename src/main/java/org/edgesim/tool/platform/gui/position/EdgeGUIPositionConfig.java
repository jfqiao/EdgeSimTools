package org.edgesim.tool.platform.gui.position;

import java.awt.*;

public class EdgeGUIPositionConfig {

    private static double DRAW_PANEL_WIDTH_RATE = 0.6747;
    private static double DRAW_PANEL_HEIGHT_RATE = 0.7910;

    private static double DEVICE_PANEL_HEIGHT_RATE = 0.25;

    private static double RATE = 0.9;

    private static int BORDER_GAP = 2;

    public static int FRAME_X = 10;
    public static int FRAME_Y = 10;

    public static int WIN_START_X = 0;
    public static int WIN_START_Y = 0;
    public static int WIN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * RATE);
    public static int WIN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * RATE);

    public static int MENU_HEAD_PANEL_X = WIN_START_X;
    public static int MENU_HEAD_PANEL_Y = WIN_START_Y;
    public static int MENU_HEAD_PANEL_HEIGHT = 50;
    public static int MENU_HEAD_PANEL_WIDTH = WIN_WIDTH;
    public static int MENU_HEAD_GAP = 4;

    public static int DRAW_PANEL_X = WIN_START_X;
    public static int DRAW_PANEL_Y = WIN_START_Y + MENU_HEAD_PANEL_HEIGHT + MENU_HEAD_GAP;
    public static int DRAW_PANEL_WIDTH = (int) (WIN_WIDTH * DRAW_PANEL_WIDTH_RATE);
    public static int DRAW_PANEL_HEIGHT = (int) ((WIN_HEIGHT - MENU_HEAD_PANEL_Y - MENU_HEAD_PANEL_HEIGHT
            - MENU_HEAD_GAP) * DRAW_PANEL_HEIGHT_RATE);

    public static int CONFIG_PANEL_X = DRAW_PANEL_WIDTH + DRAW_PANEL_X;
    public static int CONFIG_PANEL_Y = DRAW_PANEL_Y;
    public static int CONFIG_PANEL_WIDTH = WIN_WIDTH - DRAW_PANEL_WIDTH - BORDER_GAP;
    public static int CONFIG_PANEL_HEIGHT = DRAW_PANEL_HEIGHT;

    public static int EDGE_CONFIG_X = 0;
    public static int EDGE_CONFIG_Y = 0;
    public static int EDGE_CONFIG_WIDTH = CONFIG_PANEL_WIDTH / 2;
    public static int EDGE_CONFIG_HEIGHT = (int) (CONFIG_PANEL_HEIGHT * ( 1 - DEVICE_PANEL_HEIGHT_RATE));
    public static int EDGE_CONFIG_HEIGHT_RE = EDGE_CONFIG_HEIGHT / 2;

    public static int SERVICE_CONFIG_X = EDGE_CONFIG_X + EDGE_CONFIG_WIDTH;
    public static int SERVICE_CONFIG_Y = EDGE_CONFIG_Y;
    public static int SERVICE_CONFIG_WIDTH = CONFIG_PANEL_WIDTH - EDGE_CONFIG_WIDTH;
    public static int SERVICE_CONFIG_HEIGHT = EDGE_CONFIG_HEIGHT;
    public static int SERVICE_CONFIG_HEIGHT_RE = SERVICE_CONFIG_HEIGHT / 2;

    public static int DEVICE_CONFIG_X = 0;
    public static int DEVICE_CONFIG_Y = EDGE_CONFIG_Y + EDGE_CONFIG_HEIGHT;
    public static int DEVICE_CONFIG_Y_RE = EDGE_CONFIG_HEIGHT_RE;
    public static int DEVICE_CONFIG_WIDTH = CONFIG_PANEL_WIDTH;
    public static int DEVICE_CONFIG_HEIGHT = DRAW_PANEL_HEIGHT - EDGE_CONFIG_HEIGHT;

    public static int GENERAL_CONFIG_X = 0;
    public static int GENERAL_CONFIG_Y = DEVICE_CONFIG_HEIGHT + EDGE_CONFIG_HEIGHT_RE;
    public static int GENERAL_CONFIG_WIDTH = EDGE_CONFIG_WIDTH+SERVICE_CONFIG_WIDTH;
    public static int GENERAL_CONFIG_HEIGHT = DRAW_PANEL_HEIGHT - EDGE_CONFIG_HEIGHT_RE;


    public static int LOG_X = DRAW_PANEL_X;
    public static int LOG_Y = DRAW_PANEL_Y + DRAW_PANEL_HEIGHT;
    public static int LOG_WIDTH = DRAW_PANEL_WIDTH;
    public static int LOG_BORDER = 10;
    public static int LOG_HEIGHT = WIN_HEIGHT - LOG_Y - 2 * LOG_BORDER - BORDER_GAP;


    public static int RESULT_X = LOG_X + LOG_WIDTH;
    public static int RESULT_Y = LOG_Y;
    public static int RESULT_WIDTH = CONFIG_PANEL_WIDTH;
    public static int RESULT_HEIGHT = LOG_HEIGHT;

    public static int MENU_ICON_HEIGHT = 40;
    public static int MENU_ICON_WIDTH = 40;
}
