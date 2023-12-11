package com.mogilan.repository;

import com.mogilan.TestData;
import com.mogilan.config.PersistenceJPAConfig;
import com.mogilan.config.WebConfig;
import com.mogilan.dto.JobTitle;
import com.mogilan.dto.TaskPriority;
import com.mogilan.dto.TaskStatus;
import com.mogilan.model.Client;
import com.mogilan.model.Lawyer;
import com.mogilan.model.Task;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class, TaskRepositoryTest.PersistenceJPATestContainerConfig.class})
@WebAppConfiguration()
@Transactional
class TaskRepositoryTest {


    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    LawyerRepository lawyerRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private Task task1;
    private Task task2;
    private Task task3;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void beforeEach() {
        saveThreeTasks();
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
        clientRepository.deleteAll();
        lawyerRepository.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void findAllSuccess() {
        var actualResult = taskRepository.findAll();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
    }

    @Test
    void findAllShouldReturnEmptyListIfTableEmpty() {
        taskRepository.deleteAll();
        var actualResult = taskRepository.findAll();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }

    @Test
    void findByIdSuccess() {
        var actualResult = taskRepository.findById(task1.getId());

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isPresent();

        var task = actualResult.get();

        assertThat(task.getTitle()).isEqualTo(task1.getTitle());
        assertThat(task.getDescription()).isEqualTo(task1.getDescription());

        assertThat(task.getClient()).isNotNull();
        assertThat(task.getClient()).isEqualTo(task1.getClient());

        assertThat(task.getLawyers()).isNotNull();
        assertThat(task.getLawyers()).hasSize(task1.getLawyers().size());
    }

    @Test
    void testFindByIdShouldReturnEmptyOptionalIfElementAbsent() {
        var maxId = List.of(task1, task2, task3).stream()
                .map(Task::getId)
                .mapToLong(Long::valueOf)
                .max().getAsLong();
        var id = maxId + 10;
        var actualResult = taskRepository.findById(id);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }

    @Test
    void saveSuccess() {
        var prevListSize = taskRepository.findAll().size();

        Task newTask = getNewTask();

        var actualResult = taskRepository.save(newTask);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getClient()).isEqualTo(newTask.getClient());
        assertThat(actualResult.getLawyers().size()).isEqualTo(2);

        newTask.setId(actualResult.getId());
        assertThat(actualResult).isEqualTo(newTask);

        var newListSize = taskRepository.findAll().size();
        assertThat(prevListSize + 1).isEqualTo(newListSize);
    }


    @Test
    void updateSuccess() {
        Long id = task1.getId();

        var prevListSize = taskRepository.findAll().size();

        var taskById = taskRepository.findById(id);
        assertThat(taskById).isPresent();
        var task = taskById.get();

        var newDescription = "New description";
        task.setDescription(newDescription);
        task.setLawyers(null);

        var updatedValue = taskRepository.save(task);
        assertThat(updatedValue).isNotNull();
        assertThat(updatedValue.getId()).isEqualTo(id);
        assertThat(updatedValue.getDescription()).isEqualTo(newDescription);
        assertThat(updatedValue.getLawyers()).isEqualTo(null);

        var newListSize = taskRepository.findAll().size();
        assertThat(prevListSize).isEqualTo(newListSize);
    }

    @Test
    void deleteByIdSuccess() {
        Long id = task1.getId();

        var prevListSize = taskRepository.findAll().size();

        var taskById = taskRepository.findById(id);
        assertThat(taskById).isPresent();

        taskRepository.deleteById(id);

        var optional = taskRepository.findById(id);
        assertThat(optional).isEmpty();

        var newListSize = taskRepository.findAll().size();
        assertThat(prevListSize - 1).isEqualTo(newListSize);
    }

    @Test
    void existsByIdSuccess(){
        var actualResult = taskRepository.existsById(task1.getId());

        Assertions.assertTrue(actualResult);
    }

    private void saveThreeTasks() {
        var client1 =  clientRepository.save(TestData.getClient1());
        var client2 =  clientRepository.save(TestData.getClient2());
        var client3 =  clientRepository.save(TestData.getClient3());

        var lawyer1 = lawyerRepository.save(TestData.getLawyer1());
        var lawyer2 = lawyerRepository.save(TestData.getLawyer2());
        var lawyer3 = lawyerRepository.save(TestData.getLawyer3());

        task1 = TestData.getTask1();
        task2 = TestData.getTask2();
        task3 = TestData.getTask3();

        task1.setClient(client1);
        task2.setClient(client2);
        task3.setClient(client3);

        task1.setLawyers(List.of(lawyer1));
        task2.setLawyers(List.of(lawyer2));
        task3.setLawyers(List.of(lawyer3));

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
    }

    @NotNull
    private Task getNewTask() {

        var newClient = clientRepository.save(new Client("New Client", "New Client", null));

        var lawyer1 = new Lawyer("Lawyer1", "Lawyer1", JobTitle.ASSOCIATE,100.0);
        var lawyer2 = new Lawyer("Lawyer2", "Lawyer2", JobTitle.ASSOCIATE,100.0);
        lawyer1 = lawyerRepository.save(lawyer1);
        lawyer2 = lawyerRepository.save(lawyer2);

        var newTask = new Task("New Task", "New Task", TaskPriority.LOW, TaskStatus.ACCEPTED,
                LocalDate.of(2023, 12, 12),LocalDate.of(2023, 12, 22),
                null, 0);
        newTask.setClient(newClient);
        newTask.setLawyers(List.of(lawyer1, lawyer2));
        return newTask;
    }

    @Configuration
    @EnableJpaRepositories(basePackages = "com.mogilan.repository")
    @EnableTransactionManagement
    static class PersistenceJPATestContainerConfig {

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