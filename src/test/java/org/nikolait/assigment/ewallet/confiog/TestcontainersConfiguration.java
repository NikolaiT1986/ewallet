package org.nikolait.assigment.ewallet.confiog;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@RequiredArgsConstructor
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));
    }

    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redisContainer(ConfigurableEnvironment env) {
        GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:8"));
        redisContainer.withExposedPorts(6379);
        return redisContainer;
    }

}
