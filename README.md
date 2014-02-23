global-registry-java-client
===========================

To create this client on your machine:

Requirements:
 - java and maven installed

Run:
 `mvn clean package`
 
Library will be created at
 - {project_dir}/target/global-registry-client.jar



To include this client in your project, add this dependency in your pom.xml file

Requirements:
 - must be connected to Cru's VPN
 
```xml
<dependency>
    <groupId>org.cru</groupId>
    <artifactId>global-registry-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

TODOs: see issue log.
