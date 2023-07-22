package com.jaelse.acc.configurations;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
@EnableR2dbcRepositories(basePackages = {"com.jaelse.acc.resources"},
        basePackageClasses = {})
public class DBConfiguration extends AbstractR2dbcConfiguration {

    @Value("${spring.flyway.url}")
    private String flywayURl;

    @Value("${spring.r2dbc.host}")
    private String host;

    @Value("${spring.r2dbc.port}")
    private int port;

    @Value("${spring.r2dbc.name}")
    private String database;

    @Value("${spring.r2dbc.username}")
    private String username;
    @Value("${spring.r2dbc.password}")
    private String password;

    @Autowired
    public DBConfiguration() {

    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {

        return ConnectionFactories.get(builder()
                .option(DRIVER, "pool")
                .option(PROTOCOL, "postgresql")
                .option(MAX_SIZE, 30)// optional
                .option(HOST, host)
                .option(PORT, 5432)  // optional, defaults to 5432
                .option(USER, username)
                .option(PASSWORD, password)
                .option(DATABASE, database)  //
                .build());
    }


    @Bean(initMethod = "migrate")
    public Flyway flyway() {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(flywayURl, username, password));
    }
}
