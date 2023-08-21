//package com.example.websocketprac.config;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import redis.embedded.RedisServer;
//
///*
//    로컬 환경에서 실행될 경우 내장 레디스 실행
// */
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//
//    @Value("${spring.data.redis.port}")
//    private int redisPort;
//
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void redisServer() {
//        redisServer = new RedisServer(redisPort);
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        if(redisServer != null) {
//            redisServer.stop();
//        }
//    }
//}
