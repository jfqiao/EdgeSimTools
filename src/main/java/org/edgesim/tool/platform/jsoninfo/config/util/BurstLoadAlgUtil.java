package org.edgesim.tool.platform.jsoninfo.config.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.edgesim.tool.platform.gui.config.burstloadbeans.BurstLoadStageTimeBean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 用于提供BurstLoad算法相关使用接口
 * 必须信息：python编写的算法的位置，算法需要输出指定的文件，输出的文件中包含仿真以及绘制统计图所需要的数据
 * 该类用于运行算法、转换数据并提供数据的访问与获取
 */
@Getter
@Setter
@ToString
public class BurstLoadAlgUtil {

    private String algPathStr; // 待运行算法的目录
    // 统计图数据最终转换的格式
    private List<Float> algIterTimeSeriesList;  // 算法的迭代情况
    private List<BurstLoadStageTimeBean> taskStageTimeList; // 每个任务在各个阶段的运行时长

    /**
     * 运行算法
     */
    public int runAlg() throws IOException, InterruptedException {
        String[] argStrs = {"python", algPathStr};
        Process process = Runtime.getRuntime().exec(argStrs);
        return process.waitFor();
    }

    /**
     * 读取算法运行的统计信息
     */
    public void loadStatisticInfo() throws IOException {
        Path dirPath = Paths.get(algPathStr).getParent(); //目录路径
        final String iterInfoPath = Paths.get(dirPath.toString(), "iter_info.txt").toString();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(iterInfoPath));
        algIterTimeSeriesList.clear();
        float val = 0;
        String lineStr = "";
        do {
            lineStr = bufferedReader.readLine().trim();
            if (!lineStr.equals("")) {
                val = Float.parseFloat(lineStr);
                algIterTimeSeriesList.add(val);
            }
        } while (!lineStr.equals(""));
        bufferedReader.close();
        // 读取每个任务各个阶段的时间长度
        final String tasksTimeSeriesStr = Paths.get(dirPath.toString(), "tasks_tm_series.txt").toString();
        bufferedReader = new BufferedReader(new FileReader(tasksTimeSeriesStr));
        taskStageTimeList.clear();
        do {
            lineStr = bufferedReader.readLine().trim();
            if (!lineStr.equals("")) {
                String[] strs = lineStr.split(" ");
                BurstLoadStageTimeBean burstLoadStageTimeBean = new BurstLoadStageTimeBean();
                burstLoadStageTimeBean.setWaitAtSrcf(Float.parseFloat(strs[0]));
                burstLoadStageTimeBean.setMigratef(Float.parseFloat(strs[1]));
                burstLoadStageTimeBean.setWaitExecf(Float.parseFloat(strs[2]));
                burstLoadStageTimeBean.setExecf(Float.parseFloat(strs[3]));
                taskStageTimeList.add(burstLoadStageTimeBean);
            }
        } while (!lineStr.equals(""));
    }
}
