package com.gavin.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication(scanBasePackages = {
        "com.gavin.auth",
        "com.gavin.common.client",
        "com.gavin.common.config.redis"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = ("com.gavin.common.client"))
@EnableAuthorizationServer
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
