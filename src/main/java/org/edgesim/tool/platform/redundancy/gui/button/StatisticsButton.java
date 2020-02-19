package org.edgesim.tool.platform.redundancy.gui.button;

import org.edgesim.tool.platform.redundancy.statistics.DrawStatisticGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StatisticsButton extends EdgeIconButton {

    public StatisticsButton() {
        super(EdgeIconButton.STATISTICS_BUTTON, "", "statistics.png");
        setAction();
    }

    public void setAction() {
        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                    DrawStatisticGraph.showStatistics(PlatformUtils.PATH + "/result/requests/requests_1.txt");
                    DrawStatisticGraph.showStatistics("/Users/zijie/Documents/ZJU/Test/edgeSim/data/requests_1.txt");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
