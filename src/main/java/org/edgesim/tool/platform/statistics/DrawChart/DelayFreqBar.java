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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: DelayFreqBar
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-01 17:10
 * @Version: 1.0
 */
@Data
public class DelayFreqBar {
    public ChartPanel chartPanel;
    private DefaultCategoryDataset delayFreqCategoryDataSet;

    private void initDelayFreqBarDataset(List<RequestData> requestDataList) {
        this.delayFreqCategoryDataSet = new DefaultCategoryDataset();
        double minDelay = Double.MAX_VALUE, maxDelay = Double.MIN_VALUE;
        for (RequestData requestData : requestDataList) {
            if (requestData.getDelay() < minDelay) {
                minDelay = requestData.getDelay();
            }
            if (requestData.getDelay() > maxDelay) {
                maxDelay = requestData.getDelay();
            }
        }
        List<Double> binDelayLow = new ArrayList<Double>(),
                binDelayHigh = new ArrayList<Double>();
        double binWidth = (maxDelay - minDelay) / 10;
        for (int i = 0; i < 10; ++i) {
            binDelayLow.add(i * binWidth + minDelay);
            binDelayHigh.add((i + 1) * binWidth + minDelay);
        }
        HashMap<Integer, Integer> delayFreqCountHashMap = new HashMap<>(16);
        for (int i = 0; i < 10; ++i) {
            delayFreqCountHashMap.put(i, 0);
        }
        for (RequestData requestData : requestDataList) {
            double delay = requestData.getDelay();
            int idx = 0;
            while (idx < 9 && delay > binDelayHigh.get(idx)) {
                idx++;
            }
            delayFreqCountHashMap.replace(idx, delayFreqCountHashMap.get(idx) + 1);
        }
        for (int i = 0; i < 10; ++i) {
            delayFreqCategoryDataSet.addValue
                    (delayFreqCountHashMap.get(i), String.valueOf(0), String.valueOf((binDelayHigh.get(i) + binDelayLow.get(i)) / 2));
        }
    }

    public DelayFreqBar(List<RequestData> requestDataList){

        initDelayFreqBarDataset(requestDataList);

        JFreeChart barChart = ChartFactory.createBarChart("延迟频数统计图",
                "延迟时间", "服务数量", delayFreqCategoryDataSet, PlotOrientation.VERTICAL,
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
        domainAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 15));
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
