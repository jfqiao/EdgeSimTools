package org.edgesim.tool.platform.gui.button;

import org.edgesim.tool.platform.statistics.DrawStatisticGraph;
import org.edgesim.tool.platform.util.PlatformUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StatisticsButton extends EdgeIconButton {

    public StatisticsButton() {
        super(STATISTICS_BUTTON, "", "statistics.png");
        setAction();
    }

    public void setAction() {
        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.RESOURCE_ALLOC_AND_SCHEDULING_PROBLEM) {
                    try {
                        DrawStatisticGraph.showStatistics(PlatformUtils.PATH + "/result/requests/requests_1.txt");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else if (PlatformUtils.PROBLEM_TYPE == PlatformUtils.BURST_LOAD_EVACUATION) {
                    // to be implemented.
                }
            }
        });
    }
}
