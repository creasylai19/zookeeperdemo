package com.creasy;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * sample from :https://curator.apache.org/getting-started.html
 */
public class TestZnode {

    private static final Logger logger = LoggerFactory.getLogger(TestZnode.class);


    String IPs = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    CuratorFramework client;
    String LOCK = "/Locks";
    String LEADER_ELECTION = "/ELECTION";

    @Before
    public void init(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(IPs, retryPolicy);
        client.start();
    }

    @Test
    public void testCreate() throws Exception {
        client.create().creatingParentsIfNeeded().forPath("/my/path", "myData".getBytes());
        logger.debug("Finish。。。");
        Thread.sleep(100000);
    }

    @Test
    public void tsetDistributedLock() throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, LOCK);
        if ( lock.acquire(10, TimeUnit.SECONDS) )
        {
            try
            {
                // do some work inside of the critical section here
                System.out.println("获得锁啦。。。");
                logger.debug("获得锁啦。。。");
                Thread.sleep(10000);
            }
            finally
            {
                lock.release();
            }
        }
    }

    @Test
    public void testLeaderElection(){
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter()
        {
            public void takeLeadership(CuratorFramework client)
            {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
                System.out.println("成为Leader");
            }
        };

        LeaderSelector selector = new LeaderSelector(client, LEADER_ELECTION, listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
    }


    @After
    public void destory(){
        client.close();
    }

}
