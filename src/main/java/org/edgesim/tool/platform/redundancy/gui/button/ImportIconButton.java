package org.edgesim.tool.platform.redundancy.gui.button;

import com.alibaba.fastjson.JSONObject;
import org.edgesim.tool.platform.redundancy.gui.EdgeGUI;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.JsonConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportIconButton extends EdgeIconButton{

    private EdgeGUI edgeGUI;

    public ImportIconButton(EdgeGUI edgeGUI) {
        super(IMPORT_CONFIG_BUTTON, "", "file.png");
        this.edgeGUI = edgeGUI;
        setImportAction();
    }

    private void setImportAction() {
        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int retVal = jFileChooser.showOpenDialog(jFileChooser);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    String configFilePathString = jFileChooser.getSelectedFile().getAbsolutePath();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br;
                    try {
                        br = new BufferedReader(new FileReader(configFilePathString));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        edgeGUI.setJsonConfig(JSONObject.parseObject(sb.toString(), JsonConfig.class));
                        edgeGUI.convertJsonConfig();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    System.out.println("选择配置文件的路径为: [" + configFilePathString + "]");
                }
            }
        });
    }
}
