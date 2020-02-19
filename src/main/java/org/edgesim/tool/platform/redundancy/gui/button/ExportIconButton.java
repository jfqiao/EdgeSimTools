package org.edgesim.tool.platform.redundancy.gui.button;

import com.alibaba.fastjson.JSONObject;
import org.edgesim.tool.platform.redundancy.gui.EdgeGUI;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.ConfigUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportIconButton extends EdgeIconButton {

    private static final String DEFAULT_EXPORT_FILE_NAME = "sim_config.json";

    private EdgeGUI edgeGUI;

    public ExportIconButton(EdgeGUI edgeGUI) {
        super(EXPORT_CONFIG_BUTTON, "", "dir.png");
        this.edgeGUI = edgeGUI;
        setExportAction();
    }

    private void setExportAction() {
        this.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retVal = jFileChooser.showSaveDialog(jFileChooser);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    String filePath = jFileChooser.getSelectedFile().getAbsolutePath() + File.separator + DEFAULT_EXPORT_FILE_NAME;
                    FileWriter fw = null;
                    try {
                        File file = new File(filePath);
                        if (!file.exists())
                            file.createNewFile();
                        fw = new FileWriter(file);
                        edgeGUI.getJsonConfig().setEdges(ConfigUtil.convertToEdgeConfig(EdgeGUI.addedLabel));
                        edgeGUI.getJsonConfig().setDevices(ConfigUtil.convertToDeviceConfig(EdgeGUI.addedLabel));
                        edgeGUI.getJsonConfig().setLinks(ConfigUtil.convertToLinkConfig(EdgeGUI.connectLineInfos));
                        edgeGUI.getJsonConfig().setWirelessLinkConfigs(ConfigUtil.convertToWirelessConfig(EdgeGUI.connectLineInfos));
                        // 其他配置如果本来有，就直接导出，没有需要用户自己添加
                        fw.write(JSONObject.toJSONString(edgeGUI.getJsonConfig()));
                        fw.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        if (fw != null) {
                            try {
                                fw.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    System.out.printf("保存的路径为：[%s]", filePath);
                }
            }
        });
    }
}
