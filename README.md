# AppDynamics gRPC Correlation

## Use Case
Enables AppDynamics correlation between gRPC servers and clients 

## Installation

1. Copy https://github.com/appdynamicsdh/grpc-correlation/blob/master/target/grpc-correlation-1.0-SNAPSHOT.jar to /opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins
2. Copy the src/custom-activity-correlation.xml file to /opt/appdynamics/javaagent/verx.x.x.x/conf
3. Restart the Java Agent process.
4. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).


## To add this to a running Docker container

1. docker cp grpc-correlation-1.0-SNAPSHOT.jar {instanceId}:/opt/appdynamics/javaagent/verx.x.x.x/sdk-plugins
2. docker exec -it {instanceId} /bin/sh and add -Dallow.unsigned.sdk.extension.jars=true to the java command line. 
3. docker stop {instanceId}
4. docker start {instanceId}
5. Watch as you see the correlation in the AppDynamics controller (Can take up to 5 minutes).
