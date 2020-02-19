package org.edgesim.tool.platform.jsoninfo.config.util;

import org.edgesim.tool.platform.gui.ConnectLineInfo;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLinkBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeLocBean;
import org.edgesim.tool.platform.gui.config.burstloadbeans.EdgeServerBean;
import org.edgesim.tool.platform.gui.IconLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * 将IconLabel以及ConnectLineInfo转换为对应了配置。
 *
 * @author lichang
 * @since 2020/01/13
 */
public class BurstLoadConfigUtil {
    public static List<EdgeServerBean> convertIconLabelToEdge(List<IconLabel> iconLabels) {
        List<EdgeServerBean> edgeServerBeans = new ArrayList<>();
        for (int i = 0; i < iconLabels.size(); ++i) {
            IconLabel iconLabel = iconLabels.get(i);
            EdgeServerBean edgeServerBean = new EdgeServerBean();
            edgeServerBean.setName(iconLabel.getBurstLoadEdgeConfig().getName());
            edgeServerBean.setFreq(iconLabel.getBurstLoadEdgeConfig().getEdgeCpuCycle());
            edgeServerBeans.add(edgeServerBean);
        }
        return edgeServerBeans;
    }

    public static List<EdgeLocBean> convertIconLabelLoc(List<IconLabel> iconLabels) {
        List<EdgeLocBean> edgeLocBeanList = new ArrayList<>();
        for (int i = 0; i < iconLabels.size(); ++i) {
            IconLabel iconLabel = iconLabels.get(i);
            EdgeLocBean locBean = new EdgeLocBean();
            locBean.setName(iconLabel.getBurstLoadEdgeConfig().getName());
            locBean.setX(iconLabel.getX());
            locBean.setY(iconLabel.getY());
            edgeLocBeanList.add(locBean);
        }
        return edgeLocBeanList;
    }

    public static List<EdgeLinkBean> convertIconLinks(List<ConnectLineInfo> connectLineInfoList) {
        List<EdgeLinkBean> linkBeanList = new ArrayList<>();
        for (int i = 0; i < connectLineInfoList.size(); ++i) {
            ConnectLineInfo lineInfo = connectLineInfoList.get(i);
            EdgeLinkBean linkBean = new EdgeLinkBean();

            linkBean.setName(lineInfo.getBurstLoadLinkConfig().getName());
            linkBean.setStartEdge(lineInfo.getBurstLoadLinkConfig().getStartEdgeName());
            linkBean.setEndEdge(lineInfo.getBurstLoadLinkConfig().getEndEdgeName());
            linkBean.setMaxCapacity(lineInfo.getBurstLoadLinkConfig().getParallelWidth());
            linkBean.setTravelTime((int) lineInfo.getBurstLoadLinkConfig().getTransRate());
            linkBeanList.add(linkBean);
        }
        return linkBeanList;
    }

}
