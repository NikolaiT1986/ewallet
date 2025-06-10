package org.nikolait.assigment.ewallet.config;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final RedissonClient redissonClient;

    @Value("${app.locale:en}")
    private String fixedLocaleTag;

    @Value("${app.jdbc.query-timeout-seconds:1}")
    private int queryTimeoutSeconds;

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.forLanguageTag(fixedLocaleTag));
    }

    @Bean
    public RMap<UUID, Long> balanceCache() {
        return redissonClient.getMap("balanceCache");
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(queryTimeoutSeconds);
        return jdbcTemplate;
    }

}
