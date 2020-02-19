package org.edgesim.tool.platform.test;


import com.alibaba.fastjson.JSONObject;
import org.edgesim.tool.platform.jsoninfo.ParaBuilder;
import org.edgesim.tool.platform.jsoninfo.config.JsonConfig;
import org.edgesim.tool.platform.jsoninfo.config.util.ResAllocConfigUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigUtilTest {
    public static void main(String[] args) throws IOException {
        String path = "/Users/jfqiao/Desktop/sim_config.json";
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        JsonConfig jsonConfig = JSONObject.parseObject(sb.toString(), JsonConfig.class);
        ParaBuilder pb = ResAllocConfigUtil.convertJsonConfigToParaBuilder(jsonConfig);
        System.out.println(pb.calculateDt());
        System.out.println(pb.calculateCt());
    }
}
