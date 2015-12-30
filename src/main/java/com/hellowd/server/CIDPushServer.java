package com.hellowd.server;

import com.hellowd.server.config.CIDPushServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-29
 * Time : 오후 6:12
 *
 * 이것이 바로 CIDWatcher & PushServer Main Class 입니다.
 */
public class CIDPushServer {

    public static void main(String[] args) {

        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(CIDPushServerConfig.class)) {
            springContext.registerShutdownHook();

            NettyServer server = springContext.getBean(NettyServer.class);
            server.start();
        }
    }
}
