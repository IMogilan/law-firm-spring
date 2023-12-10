package com.mogilan.repository;

import com.mogilan.TestData;
import com.mogilan.config.PersistenceJPAConfig;
import com.mogilan.config.WebConfig;
import com.mogilan.model.Client;
import com.mogilan.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class, ClientRepositoryTest.PersistenceJPATestConfig.class})
@WebAppConfiguration()
@Transactional
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TaskRepository taskRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private Client client1;
    private Client client2;
    private Client client3;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void beforeEach() {
        saveThreeClients();
    }

    @AfterEach
    void afterEach() {
        clientRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void findAllSuccess() {
        var actualResult = clientRepository.findAll();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
    }

    @Test
    void findAllShouldReturnEmptyListIfTableEmpty() {
        clientRepository.deleteAll();
        var actualResult = clientRepository.findAll();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }


    @Test
    void findByIdSuccess() {
        var actualResult = clientRepository.findById(client1.getId());

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isPresent();

        assertThat(actualResult.get().getName()).isEqualTo(client1.getName());

        assertThat(actualResult.get().getTasks()).isNotNull();
        assertThat(actualResult.get().getTasks()).hasSize(client1.getTasks().size());
    }

    @Test
    void testFindByIdShouldReturnEmptyOptionalIfElementAbsent() {
        var maxId = List.of(client1, client2, client3).stream()
                .map(Client::getId)
                .mapToLong(Long::valueOf)
                .max().getAsLong();
        var id = maxId + 10;
        var actualResult = clientRepository.findById(id);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }

    @Test
    void saveSuccess() {
        var prevListSize = clientRepository.findAll().size();

        Client newClient = getNewClient();
        var actualResult = clientRepository.save(newClient);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getTasks().size()).isEqualTo(2);

        newClient.setId(actualResult.getId());
        assertThat(actualResult).isEqualTo(newClient);

        var newListSize = clientRepository.findAll().size();
        assertThat(prevListSize + 1).isEqualTo(newListSize);
    }

    @Test
    void updateSuccess() {
        Long id = client1.getId();

        var prevListSize = clientRepository.findAll().size();

        var clientById = clientRepository.findById(id);
        assertThat(clientById).isPresent();

        var newDescription = "New description";
        var newClient = new Client(id, client1.getName(), newDescription, null);

        var updatedValue = clientRepository.save(newClient);
        assertThat(updatedValue).isNotNull();
        assertThat(updatedValue.getId()).isEqualTo(id);
        assertThat(updatedValue.getDescription()).isEqualTo(newDescription);
        assertThat(updatedValue.getTasks()).isEqualTo(null);

        var newListSize = clientRepository.findAll().size();
        assertThat(prevListSize).isEqualTo(newListSize);
    }

    @Test
    void deleteByIdSuccess() {
        Long id = client1.getId();

        var prevListSize = clientRepository.findAll().size();

        var clientById = clientRepository.findById(id);
        assertThat(clientById).isPresent();

        clientRepository.deleteById(id);

        var optional = clientRepository.findById(id);
        assertThat(optional).isEmpty();

        var newListSize = clientRepository.findAll().size();
        assertThat(prevListSize - 1).isEqualTo(newListSize);
    }

    @Test
    void existsByIdSuccess(){
        var actualResult = clientRepository.existsById(client1.getId());

        Assertions.assertTrue(actualResult);
    }

    private void saveThreeClients() {
        var task1 = taskRepository.save(TestData.getTask1());
        var task2 = taskRepository.save(TestData.getTask2());
        var task3 = taskRepository.save(TestData.getTask3());

        client1 = TestData.getClient1();
        client2 = TestData.getClient2();
        client3 = TestData.getClient3();

        client1.setTasks(List.of(task1));
        client2.setTasks(List.of(task2));
        client3.setTasks(List.of(task3));

        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
    }

    @NotNull
    private Client getNewClient() {
        var task1 = taskRepository.save(TestData.getTask1());
        var task2 = taskRepository.save(TestData.getTask2());
        var newClient = new Client("New Client", "New Client", List.of(task1, task2));
        return newClient;
    }

    @Configuration
    @EnableJpaRepositories(basePackages = "com.mogilan.repository")
    @EnableTransactionManagement
    static class PersistenceJPATestConfig {


        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
                    = new LocalContainerEntityManagerFactoryBean();
            entityManagerFactoryBean.setDataSource(dataSource());
            entityManagerFactoryBean.setPackagesToScan(PropertiesUtil.get(PersistenceJPAConfig.PACKAGES_TO_SCAN_KEY));

            JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
            entityManagerFactoryBean.setJpaProperties(additionalProperties());

            return entityManagerFactoryBean;
        }

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(PropertiesUtil.get(PersistenceJPAConfig.DB_DRIVER_KEY));
            dataSource.setUrl(postgres.getJdbcUrl());
            dataSource.setUsername(postgres.getUsername());
            dataSource.setPassword(postgres.getPassword());
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
            properties.setProperty("hibernate.connection.pool_size", PropertiesUtil.get(PersistenceJPAConfig.HIBERNATE_CONNECTION_POOL_SIZE_KEY));
            properties.setProperty("hibernate.hbm2ddl.auto", PropertiesUtil.get(PersistenceJPAConfig.HIBERNATE_HBM_2_DDL_AUTO_KEY));
            properties.setProperty("hibernate.dialect", PropertiesUtil.get(PersistenceJPAConfig.HIBERNATE_DIALECT_KEY));
            properties.setProperty("hibernate.show_sql", PropertiesUtil.get(PersistenceJPAConfig.HIBERNATE_SHOW_SQL_KEY));
            properties.setProperty("hibernate.format_sql", PropertiesUtil.get(PersistenceJPAConfig.HIBERNATE_FORMAT_SQL_KEY));

            return properties;
        }
    }

}