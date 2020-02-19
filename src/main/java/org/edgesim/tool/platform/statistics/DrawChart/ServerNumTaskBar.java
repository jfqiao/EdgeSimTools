package org.edgesim.tool.platform.statistics.DrawChart;

import lombok.Data;
import org.edgesim.tool.platform.statistics.RequestData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @ClassName: ServerNumTaskBar
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-01 22:37
 * @Version: 1.0
 */
@Data
public class ServerNumTaskBar {
    public ChartPanel chartPanel;
    private DefaultCategoryDataset serverNumTaskBarDataset;

    private void initServerNumTaskBarDataset(List<RequestData> requestDataList) {
        this.serverNumTaskBarDataset = new DefaultCategoryDataset();
        Map<String, Integer> serverNumTaskMap = new TreeMap<>(new MyComparator());
        for (RequestData requestData : requestDataList) {
            String serverNameString = requestData.getHandleServerName();
            if (!serverNumTaskMap.containsKey(serverNameString)) {
                serverNumTaskMap.put(serverNameString, 0);
            }
            serverNumTaskMap.replace(serverNameString, serverNumTaskMap.get(serverNameString) + 1);
        }
        Iterator<Map.Entry<String, Integer>> serverNumTaskIterator = serverNumTaskMap.entrySet().iterator();
        while (serverNumTaskIterator.hasNext()) {
            Map.Entry<String, Integer> entry = serverNumTaskIterator.next();
            serverNumTaskBarDataset.addValue(entry.getValue(), String.valueOf(0), entry.getKey());
        }
    }

    public ServerNumTaskBar(List<RequestData> requestDataList) {
        initServerNumTaskBarDataset(requestDataList);
        JFreeChart barChart = ChartFactory.createBarChart("服务器处理服务频数统计图",
                "服务器名称", "处理服务数量", serverNumTaskBarDataset, PlotOrientation.VERTICAL,
                true, true, false);
        TextTitle textTitle = barChart.getTitle();
        textTitle.setFont(new Font("宋体", Font.BOLD, 20));
        barChart.removeLegend();

        CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        categoryPlot.setBackgroundPaint(new Color(240,255,255));
        categoryPlot.setRangeGridlinePaint(Color.DARK_GRAY);
        categoryPlot.setRangeGridlinesVisible(true);

        CategoryAxis domainAxis = categoryPlot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));
        ValueAxis valueAxis = categoryPlot.getRangeAxis();
        valueAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));
        valueAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 15));

        BarRenderer barRenderer = new BarRenderer();
        GradientPaint gradientpaint = new GradientPaint(0.0F, 0.0F, new Color(11, 23, 70),
                0.0F, 0.0F, new Color(11, 23, 70));
        barRenderer.setSeriesPaint(0, gradientpaint);
        barRenderer.setShadowVisible(false);
        barRenderer.setBarPainter(new StandardBarPainter());
        categoryPlot.setRenderer(barRenderer);

        chartPanel = new ChartPanel(barChart,true);
    }
}
