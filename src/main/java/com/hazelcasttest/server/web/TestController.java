package com.hazelcasttest.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This just provides an HTTP endpoint to access some debug information
 */
@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(path="/test")
    public String test() {
        StringBuilder sb = new StringBuilder();

        for(String service : discoveryClient.getServices()) {
            for (ServiceInstance instance : discoveryClient.getInstances(service)) {
                sb.append(service).append(" -- ").append(instance.getHost()).append("<br>");
            }
        }

        return sb.toString();
    }
}
