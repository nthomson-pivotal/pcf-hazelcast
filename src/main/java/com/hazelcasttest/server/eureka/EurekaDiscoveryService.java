package com.hazelcasttest.server.eureka;

import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;
import com.hazelcast.spi.discovery.integration.DiscoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hazelcast.spi.partitiongroup.PartitionGroupMetaData.PARTITION_GROUP_ZONE;

@Component
@Slf4j
public class EurekaDiscoveryService implements DiscoveryService {
    private static final String YML_SEPARATOR = ".";

    @Value("${spring.application.name}")
    public String applicationName;

    private final static int PORT = 5701;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public Map<String, Object> discoverLocalMetadata() {
        return new HashMap<>();
    }

    /**
     * Provide a way to discover the other nodes in the cluster, that this
     * instance should connect to.
     * <p>
     * Using the {@link DiscoveryClient} injected by Spring, we can connect
     * to Eureka and find all the nodes <b><u>currently</u></b> registered
     * with Eureka.
     * <p>
     * When this instance starts this method is run before this node has
     * registered itself with Eureka, so the list is essentially the nodes
     * already in the cluster. If it is an empty list, this instance is the
     * first, and when it gets to the registration step it will record itself
     * for other servers to find.
     * <p>
     * The ordering here is out of our control. Registering with Eureka
     * happens when the application is fully up (has connected with cluster
     * members).
     * <p>
     * As a consequence of this, there is a race condition. If the first
     * two servers start at roughly the same time, they will run this method
     * at roughly the same time, and <i>before</i> each other has run the
     * registration step. So each will get an empty list from Eureka.
     *
     * @return A list of {@code host}:{@code port} pairs.
     */
    @Override
    public Iterable<DiscoveryNode> discoverNodes() {
        List<DiscoveryNode> nodes = new ArrayList<>();

        log.info("\n--------------------------------------------------------------------------------");
        log.info("discoverNodes(): Hazelcast lookup to Eureka : start");

        // A mildly unnecessary lambda, to ensure we're not on the dark ages of Java 7
        discoveryClient.getInstances(applicationName).forEach(
                (ServiceInstance serviceInstance) -> {
                    try {
                        String host = serviceInstance.getHost();

                        if (host != null) {
                            log.info("discoverNodes():  -> found {}:{}", host, PORT);
                            Address address = new Address(host, Integer.valueOf(PORT));
                            DiscoveryNode discoveryNode = new SimpleDiscoveryNode(address);
                            nodes.add(discoveryNode);
                        }
                    } catch (Exception e) {
                        log.error("discoverNodes()", e);
                    }
                });

        log.info("discoverNodes(): Hazelcast lookup to Eureka : end. Found {} item{}",
                nodes.size(), (nodes.size() == 1 ? "" : "s"));
        log.info("--------------------------------------------------------------------------------\n");

        return nodes;
    }

    /**
     * Part of the interface, but not used.
     */
    @Override
    public void start() {
    }

    /**
     * Part of the interface, but not used.
     */
    @Override
    public void destroy() {
    }
}
