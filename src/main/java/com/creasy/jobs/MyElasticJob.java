package com.creasy.jobs;

import com.creasy.MainClass;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.apache.log4j.Logger;

import java.lang.management.ManagementFactory;

/**
 * @author creasylai19@gmail.com
 * @version 1.0.0
 * @ClassName MyElasticJob.java
 * @Description TODO
 * @createTime 2020年04月13日 00:38:00
 */
public class MyElasticJob implements SimpleJob {


    private static final Logger logger = Logger.getLogger(MyElasticJob.class);

    @Override
    public void execute(ShardingContext context) {
        logger.debug(ManagementFactory.getRuntimeMXBean().getName()+":"+context.getShardingItem()+":"+context.getJobName()+":"+context.getJobParameter()+":"+context.getShardingParameter());
        switch (context.getShardingItem()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
        }
    }
}