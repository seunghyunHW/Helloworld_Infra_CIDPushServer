package com.hellowd.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-29
 * Time : 오후 6:16
 * To change this template use File | Settings | File and Code Templates.
 */
@Configuration
@ComponentScan("com.hellowd.server")
@PropertySource("classpath:server.properties")
public class CIDPushServerConfig {

    @Value("${boss.thread.count}")
    private int bossThreadCount;

    @Value("${worker.thread.count}")
    private int workerThreadCount;

    @Value("${server.serviceName}")
    private String serviceName;

    @Value("${server.servicePort}")
    private int servicePort;

    @Value("${server.ctrlPort}")
    private int ctrlPort;

    @Value("${server.sessionTimeout}")
    private int sessionTimeout;

    @Value("${call.id.watch.host}")
    private String cidwatchHost;

    @Value("${call.id.watch.port}")
    private int cidwatchPort;

    @Value("${zookeeper.hostPort}")
    private String zookeeperHostPort;

    @Value("${zookeeper.sessionTimeout}")
    private int zookeeperSessionTimeout;

    @Value("${zookeeper.znode}")
    private String zookeeperZnode;

    @Value("${zookeeper.znodeData}")
    private String zookeeperZnodeData;

    @Value("${database.driverClassName}")
    private String databaseDriverClassName;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.uname}")
    private String databaseUname;

    @Value("${database.passwd}")
    private String databasePasswd;


    @Bean(name="bossThreadCount")
    public int getBossThreadCount(){
        return bossThreadCount;
    }

    @Bean(name="workerThreadCount")
    public int getWorkerThreadCount(){
        return workerThreadCount;
    }

    @Bean(name="serviceName")
    public String getServiceName(){
        return serviceName;
    }

    @Bean(name="servicePort")
    public int getServicePort(){
        return servicePort;
    }

    @Bean(name="ctrlPort")
    public int getCtrlPort(){
        return ctrlPort;
    }

    @Bean(name="sessionTimeout")
    public int getSessionTimeout(){
        return sessionTimeout;
    }

    @Bean(name="cidwatchHost")
    public String getCidwatchHost(){
        return cidwatchHost;
    }

    @Bean(name="cidwatchPort")
    public int getCidwatchPort(){
        return cidwatchPort;
    }

    @Bean(name="zookeeperHostPort")
    public String getZookeeperHostPort(){
        return zookeeperHostPort;
    }

    @Bean(name="zookeeperSessionTimeout")
    public int getZookeeperSessionTimeout(){
        return zookeeperSessionTimeout;
    }

    @Bean(name="zookeeperZnode")
    public String getZookeeperZnode(){
        return zookeeperZnode;
    }

    @Bean(name="zookeeperZnodeData")
    public String getZookeeperZnodeData(){
        return zookeeperZnodeData;
    }

    @Bean(name="databaseDriverClassName")
    public String getDatabaseDriverClassName(){
        return databaseDriverClassName;
    }

    @Bean(name="databaseUrl")
    public String getDatabaseUrl(){
        return databaseUrl;
    }

    @Bean(name="databaseUname")
    public String getDatabaseUname(){
        return databaseUname;
    }

    @Bean(name="databasePasswd")
    public String getDatabasePasswd(){
        return databasePasswd;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
