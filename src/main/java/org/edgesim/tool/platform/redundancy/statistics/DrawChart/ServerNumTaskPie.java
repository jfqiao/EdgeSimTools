package org.edgesim.tool.platform.redundancy.statistics.DrawChart;

import lombok.Data;
import org.edgesim.tool.platform.redundancy.statistics.RequestData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.SortOrder;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ServerNumTaskPie
 * @Description: TODO
 * @Author: Zijie Liu
 * @Date: 2020-01-01 17:01
 * @Version: 1.0
 */
@Data
public class ServerNumTaskPie {

    public ChartPanel chartPanel;
    private DefaultPieDataset serverNumTaskPieDataSet;

    private void initServerNumTaskPieDataSet(List<RequestData> requestDataList){
        this.serverNumTaskPieDataSet = new DefaultPieDataset();
        Map<String, Integer> serverNumTaskMap = new HashMap<String, Integer>();
        for (RequestData requestData : requestDataList) {
            String serverNameString = requestData.getHandleServerName();
            if (!serverNumTaskMap.containsKey(serverNameString))
                serverNumTaskMap.put(serverNameString, 0);
            serverNumTaskMap.replace(serverNameString, serverNumTaskMap.get(serverNameString) + 1);
        }
        for (Map.Entry<String, Integer> eleEntry : serverNumTaskMap.entrySet()) {
            serverNumTaskPieDataSet.setValue(eleEntry.getKey(), eleEntry.getValue());
        }
    }

    public ServerNumTaskPie(List<RequestData> requestDataList){
        initServerNumTaskPieDataSet(requestDataList);
        //通过片区的值进行降序排序
        serverNumTaskPieDataSet.sortByValues(SortOrder.DESCENDING);
        JFreeChart chart = ChartFactory.createPieChart("服务器处理服务比例统计图", serverNumTaskPieDataSet, true, false, false);

        PiePlot piePlot = (PiePlot) chart.getPlot();
        piePlot.setOutlineVisible(true);
        piePlot.setBackgroundPaint(new Color(240,255,255));
        piePlot.setExplodePercent("cloud-server", 0.1);
        piePlot.setSectionPaint("cloud-server", new Color(31,49,101));
        piePlot.setSectionPaint("edge-server-2", new Color(0,201,127));
        piePlot.setSectionPaint("edge-server-3", new Color(160,102,127));
        piePlot.setSectionPaint("edge-server-5", new Color(227,207,87));
        piePlot.setSectionPaint("edge-server-10", new Color(61,89,171));

        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        StandardPieSectionLabelGenerator standardPieSectionLabelGenerator =
                new StandardPieSectionLabelGenerator("{0}\n{2}", numberFormat, decimalFormat);
        piePlot.setLabelGenerator(standardPieSectionLabelGenerator);
        piePlot.setIgnoreNullValues(true);
        piePlot.setIgnoreZeroValues(true);

        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));
        piePlot.setLabelFont(new Font("宋体", Font.BOLD, 10));
        chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 14));

        chartPanel = new ChartPanel(chart,true);
    }

}
