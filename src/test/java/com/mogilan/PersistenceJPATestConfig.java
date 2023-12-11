package com.mogilan;

import com.mogilan.config.PersistenceJPAConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static com.mogilan.config.PersistenceJPAConfig.*;

@Configuration
@EnableJpaRepositories(basePackages = "com.mogilan.repository")
public class PersistenceJPATestConfig {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (var inputStream = PersistenceJPATestConfig.class.getClassLoader().getResourceAsStream("test-application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(get(PersistenceJPAConfig.PACKAGES_TO_SCAN_KEY));

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(get(DB_DRIVER_KEY));
        dataSource.setUrl(get(DB_URL_KEY));
        dataSource.setUsername(get(DB_USER_KEY));
        dataSource.setPassword(get(DB_PASSWORD_KEY));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.pool_size", get(PersistenceJPAConfig.HIBERNATE_CONNECTION_POOL_SIZE_KEY));
        properties.setProperty("hibernate.hbm2ddl.auto", get(PersistenceJPAConfig.HIBERNATE_HBM_2_DDL_AUTO_KEY));
        properties.setProperty("hibernate.dialect", get(PersistenceJPAConfig.HIBERNATE_DIALECT_KEY));
        properties.setProperty("hibernate.show_sql", get(PersistenceJPAConfig.HIBERNATE_SHOW_SQL_KEY));
        properties.setProperty("hibernate.format_sql", get(PersistenceJPAConfig.HIBERNATE_FORMAT_SQL_KEY));

        return properties;
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
