package com.mogilan.config;

import com.mogilan.util.PropertiesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.mogilan.repository")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class PersistenceJPAConfig {

    private static final String PACKAGES_TO_SCAN_KEY = "model.package";
    private static final String DB_DRIVER_KEY = "db.driver";
    private static final String DB_URL_KEY = "db.url";
    private static final String DB_USER_KEY = "db.user";
    private static final String DB_PASSWORD_KEY = "db.password";
    private static final String HIBERNATE_CONNECTION_POOL_SIZE_KEY = "hibernate.connection.pool_size";
    private static final String HIBERNATE_HBM_2_DDL_AUTO_KEY = "hibernate.hbm2ddl.auto";
    private static final String HIBERNATE_DIALECT_KEY = "hibernate.dialect";
    private static final String HIBERNATE_SHOW_SQL_KEY = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL_KEY = "hibernate.format_sql";

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan(PropertiesUtil.get(PACKAGES_TO_SCAN_KEY));

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(PropertiesUtil.get(DB_DRIVER_KEY));
        dataSource.setUrl(PropertiesUtil.get(DB_URL_KEY));
        dataSource.setUsername(PropertiesUtil.get(DB_USER_KEY));
        dataSource.setPassword(PropertiesUtil.get(DB_PASSWORD_KEY));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.pool_size", PropertiesUtil.get(HIBERNATE_CONNECTION_POOL_SIZE_KEY));
        properties.setProperty("hibernate.hbm2ddl.auto", PropertiesUtil.get(HIBERNATE_HBM_2_DDL_AUTO_KEY));
        properties.setProperty("hibernate.dialect", PropertiesUtil.get(HIBERNATE_DIALECT_KEY));
        properties.setProperty("hibernate.show_sql", PropertiesUtil.get(HIBERNATE_SHOW_SQL_KEY));
        properties.setProperty("hibernate.format_sql", PropertiesUtil.get(HIBERNATE_FORMAT_SQL_KEY));

        return properties;
    }


}
