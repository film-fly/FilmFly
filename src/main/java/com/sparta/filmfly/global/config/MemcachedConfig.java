package com.sparta.filmfly.global.config;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class MemcachedConfig {

    @Value("${memcached.host}")
    private String memcachedHost;

    @Value("${memcached.port}")
    private int memcachedPort;

    long memcachedTimeout = 7000L;  // 7초로 타임아웃 설정

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        log.info("Setting Memcached timeout to {} ms", memcachedTimeout);

        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder()
                .setOpTimeout(memcachedTimeout); // 타임아웃 설정

        List<InetSocketAddress> addresses = Collections.singletonList(new InetSocketAddress(memcachedHost, memcachedPort));

        return new MemcachedClient(builder.build(), addresses);
    }
}
