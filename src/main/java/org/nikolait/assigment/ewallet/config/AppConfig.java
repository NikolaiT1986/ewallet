package org.nikolait.assigment.ewallet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
public class AppConfig {

    @Value("${app.locale:en}")
    private String fixedLocaleTag;

    @Value("${app.jdbc.query-timeout-seconds:1}")
    private int queryTimeoutSeconds;

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.forLanguageTag(fixedLocaleTag));
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setQueryTimeout(queryTimeoutSeconds);
        return jdbcTemplate;
    }

}
