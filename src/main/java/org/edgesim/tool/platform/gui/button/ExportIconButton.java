package org.edgesim.tool.platform.gui.button;

import com.alibaba.fastjson.JSONObject;
import org.edgesim.tool.platform.gui.EdgeGUI;
import org.edgesim.tool.platform.jsoninfo.config.util.BurstLoadConfigUtil;
import org.edgesim.tool.platform.jsoninfo.config.util.ResAllocConfigUtil;
import org.edgesim.tool.platform.util.PlatformUtils;

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
                        if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM) {
                            edgeGUI.getJsonConfig().setEdges(ResAllocConfigUtil.convertToEdgeConfig(EdgeGUI.addedLabel));
                            edgeGUI.getJsonConfig().setDevices(ResAllocConfigUtil.convertToDeviceConfig(EdgeGUI.addedLabel));
                            edgeGUI.getJsonConfig().setLinks(ResAllocConfigUtil.convertToLinkConfig(EdgeGUI.connectLineInfos));
                            edgeGUI.getJsonConfig().setWirelessLinkConfigs(ResAllocConfigUtil.convertToWirelessConfig(EdgeGUI.connectLineInfos));
                        } else if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.BURST_LOAD_EVACUATION) {
                            edgeGUI.getJsonConfig().setEdgeServerBeanList(BurstLoadConfigUtil.convertIconLabelToEdge(EdgeGUI.addedLabel));
                            edgeGUI.getJsonConfig().setEdgeLocBeanList(BurstLoadConfigUtil.convertIconLabelLoc(EdgeGUI.addedLabel));
                            edgeGUI.getJsonConfig().setEdgeLinkBeanList(BurstLoadConfigUtil.convertIconLinks(EdgeGUI.connectLineInfos));
                        }
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
