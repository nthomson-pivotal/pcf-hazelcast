package com.hazelcasttest.server.eureka;

import com.hazelcast.spi.discovery.integration.DiscoveryService;
import com.hazelcast.spi.discovery.integration.DiscoveryServiceProvider;
import com.hazelcast.spi.discovery.integration.DiscoveryServiceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EurekaDiscoveryServiceProvider implements DiscoveryServiceProvider {

    @Autowired
    private EurekaDiscoveryService eurekaDiscoveryService;

    public DiscoveryService newDiscoveryService(DiscoveryServiceSettings unused) {
        return eurekaDiscoveryService;
    }
}