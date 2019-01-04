package com.hazelcasttest.server.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcasttest.server.eureka.Constants;
import com.hazelcasttest.server.eureka.EurekaDiscoveryServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    @Bean
    public Config config(EurekaDiscoveryServiceProvider discoveryServiceProvider) {

        Config config = new Config();

        // Naming
        config.getGroupConfig().setName(Constants.CLUSTER_NAME);

        // Discovery
        config.setProperty("hazelcast.discovery.enabled", Boolean.TRUE.toString());
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig
                .getMulticastConfig()
                .setEnabled(false);

        joinConfig.getDiscoveryConfig().setDiscoveryServiceProvider(discoveryServiceProvider);

        // Maps
        config.getMapConfigs()
                .put(Constants.MAP_NAME_SAFE, new MapConfig(Constants.MAP_NAME_SAFE).setBackupCount(1));
        config.getMapConfigs()
                .put(Constants.MAP_NAME_UNSAFE, new MapConfig(Constants.MAP_NAME_UNSAFE).setBackupCount(0));

        return config;
    }
}
