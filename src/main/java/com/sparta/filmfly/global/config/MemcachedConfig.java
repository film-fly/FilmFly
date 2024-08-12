package com.sparta.filmfly.global.config;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

@Configuration
public class MemcachedConfig {

    @Value("${memcached.host}")
    private String memcachedHost;

    @Value("${memcached.port}")
    private int memcachedPort;

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();

        List<InetSocketAddress> addresses = Collections.singletonList(new InetSocketAddress(memcachedHost, memcachedPort));

        return new MemcachedClient(builder.build(), addresses);
    }
}
