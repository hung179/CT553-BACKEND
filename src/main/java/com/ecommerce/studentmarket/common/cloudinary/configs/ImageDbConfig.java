package com.ecommerce.studentmarket.common.cloudinary.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.ecommerce.studentmarket.common.cloudinary.repositories",
        entityManagerFactoryRef = "imageEntityManagerFactory",
        transactionManagerRef = "imageTransactionManager"
)
public class ImageDbConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.image-db")
    public DataSource imageDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "imageEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean imageEntityManagerFactory(
            @Qualifier("imageDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.ecommerce.studentmarket.common.cloudinary.domains");
        em.setPersistenceUnitName("image");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "imageTransactionManager")
    public PlatformTransactionManager imageTransactionManager(
            @Qualifier("imageEntityManagerFactory") EntityManagerFactory factory
    ) {
        return new JpaTransactionManager(factory);
    }
}
