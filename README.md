# PCF Hazelcast

This repository provides a POC example of running Hazelcast OSS in Pivotal 
Application Service, leveraging the service registry component of Spring 
Cloud Services for cluster discovery.

## Preparation

You must be running an instance of Pivotal Application Service with the Spring 
Cloud Services tile installed and available.

The application manifest included with the example expects a Spring Cloud 
Services service registry named `hazelcast` to have been created that it can
bind to:

```cf create-service p-service-registry standard hazelcast```

## Deploying

Assuming that the service registry is present, the application can be deployed  
with

```cf push```

Once the application is running you can access a test page at:

```http://<app route>/test```

This will output the container IP addresses of the members of the Hazelcast 
cluster.

If you scale up the application:

```cf scale hazelcast-server -i 3```

And then check the `/test` endpoint again it will output 3 entries reflecting 
the new application instances.