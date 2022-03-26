package com.twa.flights.api.clusters.service;

import com.twa.flights.api.clusters.configuration.ZooKeeperCuratorConfiguration;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ZookeeperService {

   private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperService.class);

   private static final int MAX_WAIT = 10000;

   private final ZooKeeperCuratorConfiguration zkcConf;

   public ZookeeperService(ZooKeeperCuratorConfiguration zkcConf) {
      this.zkcConf = zkcConf;
   }

   DistributedBarrier getBarrier(String barrierName) {
      var client = zkcConf.getClient();

      return new DistributedBarrier(zkcConf.getClient(), barrierName) {
         @Override
         public synchronized void setBarrier() throws Exception {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(barrierName);
         }
      };
   }

   boolean createBarrier(String barrierName) {
      try {
         getBarrier(barrierName).setBarrier();
      } catch (Exception e) {
         LOGGER.error(e.getMessage(), e);
      }

      return true;
   }

   boolean checkIfBarrierExists(String barrierName) {
      var client = zkcConf.getClient();
      var result = false;

      try {
         result = Objects.nonNull(client.checkExists().forPath(barrierName));
      } catch (Exception e) {
         LOGGER.error(e.getMessage(), e);
      }

      return result;
   }

   void deleteBarrier(String barrierName) {
      var client = zkcConf.getClient();

      if (checkIfBarrierExists(barrierName)) {
         try {
            client.delete().quietly().forPath(barrierName);
         } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
         }
      }
   }

   void waitOnBarrier(String barrierName) {
      try {
         getBarrier(barrierName).waitOnBarrier(MAX_WAIT, TimeUnit.MILLISECONDS);
      } catch (Exception e) {
         LOGGER.error(e.getMessage(), e);
      }
   }

}
