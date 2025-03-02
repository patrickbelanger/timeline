// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package io.github.patrickbelanger.timeline.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisCacheConfig {

    private final Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);
    public final static String BLACKLIST_TOKEN_CACHE_NAME = "blacklist-token-cache";
    public final static String REFRESH_TOKEN_CACHE_NAME = "refresh-token";

    @Value("${jwt.expiry.ttl:0}")
    private int expiryTtl;

    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        logger.info("Creating Redis/LettuceConnectionFactory at {}:{}", host, port);
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder -> {
           Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
           configMap.put(BLACKLIST_TOKEN_CACHE_NAME, RedisCacheConfiguration
               .defaultCacheConfig()
               .entryTtl(Duration.ofSeconds(expiryTtl))
           );
           configMap.put(REFRESH_TOKEN_CACHE_NAME, RedisCacheConfiguration
               .defaultCacheConfig()
               .entryTtl(Duration.ofSeconds(expiryTtl * 2L))
           );
           builder.withInitialCacheConfigurations(configMap);
           logger.info("Configuring Redis/LettuceCacheManagerBuilderCustomizer {}", configMap.keySet());
        });
    }
}
