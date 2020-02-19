package org.edgesim.tool.platform.redundancy.jsoninfo.generator;

import lombok.Getter;
import lombok.Setter;
import org.edgesim.tool.platform.redundancy.jsoninfo.config.BurstLoadTaskConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class BurstLoadTaskGenerator {
    private int numTasks;
    private int[] taskPacketSizeRange;
    private int[] taskExecTimeRange;

    /**
     * @param numTasks            the number of tasks generated
     * @param taskPacketSizeRange the range of the packet size of tasks for random, array, size 2
     * @param taskExecTimeRange   the range of the execution time of tasks for random, array, size 2
     */
    public BurstLoadTaskGenerator(int numTasks, int[] taskPacketSizeRange, int[] taskExecTimeRange) {
        assert taskExecTimeRange.length == 2;
        assert taskPacketSizeRange.length == 2;

        this.numTasks = numTasks;
        this.taskPacketSizeRange = taskPacketSizeRange;
        this.taskExecTimeRange = taskExecTimeRange;
    }

    public List<BurstLoadTaskConfig> generateTask() {
        List<BurstLoadTaskConfig> taskConfigList = new ArrayList<>(numTasks);
        Random random = new Random();
        for (int i = 0; i < numTasks; ++i) {
            BurstLoadTaskConfig burstLoadTaskConfig = new BurstLoadTaskConfig();
            burstLoadTaskConfig.setTaskSize(random.nextInt(taskPacketSizeRange[1] - taskPacketSizeRange[0]) + taskPacketSizeRange[0]);
            burstLoadTaskConfig.setExecTime(random.nextInt(taskExecTimeRange[1] - taskExecTimeRange[0]) + taskExecTimeRange[0]);
            taskConfigList.add(burstLoadTaskConfig);
        }
        return taskConfigList;
    }

//    public static void main(String[] args) {
//        BurstLoadTaskGenerator taskGenerator=new BurstLoadTaskGenerator(10,new int[]{10,20},new int[]{5,10});
//        List<BurstLoadTaskConfig> taskConfigList=taskGenerator.generateTask();
//        for(BurstLoadTaskConfig taskConfig : taskConfigList){
//            System.out.println(taskConfig);
//        }
//    }
}
