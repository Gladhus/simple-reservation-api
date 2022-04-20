package com.gladhus.volcanocampingapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class VolcanoCampingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolcanoCampingApiApplication.class, args);
    }

    private final LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    public VolcanoCampingApiApplication(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

}
