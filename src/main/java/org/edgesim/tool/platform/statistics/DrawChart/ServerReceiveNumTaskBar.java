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
 * @ClassName: ServerReceiveNumTaskBar
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-02 12:06
 * @Version: 1.0
 */
@Data
public class ServerReceiveNumTaskBar {

    public ChartPanel chartPanel;
    public DefaultCategoryDataset defaultCategoryDataset;

    private void initServerReceiveNumTaskBarDataset(List<RequestData> requestDataList){
        this.defaultCategoryDataset = new DefaultCategoryDataset();
        Map<String, Integer> serverReceiveNumTaskMap = new TreeMap<>(new MyComparator());
        for (RequestData request:
             requestDataList) {
            String serverName = request.getAccessServerName();
            if (!serverReceiveNumTaskMap.containsKey(serverName)){
                serverReceiveNumTaskMap.put(serverName,0);
            }
            serverReceiveNumTaskMap.replace(serverName, serverReceiveNumTaskMap.get(serverName) + 1);
        }
        Iterator<Map.Entry<String, Integer>> iterator = serverReceiveNumTaskMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            defaultCategoryDataset.addValue(entry.getValue(), "0", entry.getKey());
        }
    }

    public ServerReceiveNumTaskBar(List<RequestData> requestDataList){
        initServerReceiveNumTaskBarDataset(requestDataList);
        JFreeChart barChart = ChartFactory.createBarChart("服务器收到服务请求数量统计图",
                "服务器名称", "接受服务数量", defaultCategoryDataset, PlotOrientation.VERTICAL,
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

class MyComparator implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
}
