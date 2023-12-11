package com.mogilan.repository;

import com.mogilan.TestData;
import com.mogilan.config.PersistenceJPAConfig;
import com.mogilan.config.WebConfig;
import com.mogilan.dto.JobTitle;
import com.mogilan.dto.TaskPriority;
import com.mogilan.dto.TaskStatus;
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
@ContextConfiguration(classes = {WebConfig.class, LawyerRepositoryTest.PersistenceJPATestContainerConfig.class})
@WebAppConfiguration()
@Transactional
class LawyerRepositoryTest {


    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    LawyerRepository lawyerRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    private Lawyer lawyer1;
    private Lawyer lawyer2;
    private Lawyer lawyer3;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void beforeEach() {
        saveThreeLawyers();
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
        var actualResult = lawyerRepository.findAll();
        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
    }

    @Test
    void findAllShouldReturnEmptyListIfTableEmpty() {
        lawyerRepository.deleteAll();
        var actualResult = lawyerRepository.findAll();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }


    @Test
    void findByIdSuccess() {
        var actualResult = lawyerRepository.findById(lawyer1.getId());

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isPresent();

        var lawyer = actualResult.get();

        assertThat(lawyer.getFirstName()).isEqualTo(lawyer1.getFirstName());
        assertThat(lawyer.getLastName()).isEqualTo(lawyer1.getLastName());
        assertThat(lawyer.getJobTitle()).isEqualTo(lawyer1.getJobTitle());

        assertThat(lawyer.getTasks()).isNotNull();
        assertThat(lawyer.getTasks()).hasSize(lawyer1.getTasks().size());
    }

    @Test
    void testFindByIdShouldReturnEmptyOptionalIfElementAbsent() {
        var maxId = List.of(lawyer1, lawyer2, lawyer3).stream()
                .map(Lawyer::getId)
                .mapToLong(Long::valueOf)
                .max().getAsLong();
        var id = maxId + 10;
        var actualResult = lawyerRepository.findById(id);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEmpty();
    }

    @Test
    void saveSuccess() {
        var prevListSize = lawyerRepository.findAll().size();

        Lawyer newLawyer = getNewLawyer();

        var actualResult = lawyerRepository.save(newLawyer);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getTasks().size()).isEqualTo(2);

        newLawyer.setId(actualResult.getId());
        assertThat(actualResult).isEqualTo(newLawyer);

        var newListSize = lawyerRepository.findAll().size();
        assertThat(prevListSize + 1).isEqualTo(newListSize);
    }

    @Test
    void updateSuccess() {
        Long id = lawyer1.getId();

        var prevListSize = lawyerRepository.findAll().size();

        var lawyerById = lawyerRepository.findById(id);
        assertThat(lawyerById).isPresent();
        var lawyer = lawyerById.get();

        var newLastname = "New lastname";
        lawyer.setLastName(newLastname);
        lawyer.setTasks(null);

        var updatedValue = lawyerRepository.save(lawyer);
        assertThat(updatedValue).isNotNull();
        assertThat(updatedValue.getId()).isEqualTo(id);
        assertThat(updatedValue.getLastName()).isEqualTo(newLastname);
        assertThat(updatedValue.getTasks()).isEqualTo(null);

        var newListSize = lawyerRepository.findAll().size();
        assertThat(prevListSize).isEqualTo(newListSize);
    }

    @Test
    void deleteByIdSuccess() {
        Long id = lawyer1.getId();

        var prevListSize = lawyerRepository.findAll().size();

        var lawyerById = lawyerRepository.findById(id);
        assertThat(lawyerById).isPresent();

        lawyerRepository.deleteById(id);

        var optional = lawyerRepository.findById(id);
        assertThat(optional).isEmpty();

        var newListSize = lawyerRepository.findAll().size();
        assertThat(prevListSize - 1).isEqualTo(newListSize);
    }

    @Test
    void existsByIdSuccess(){
        var actualResult = lawyerRepository.existsById(lawyer1.getId());

        Assertions.assertTrue(actualResult);
    }

    private void saveThreeLawyers() {
        var task1 = taskRepository.save(TestData.getTask1());
        var task2 = taskRepository.save(TestData.getTask1());
        var task3 = taskRepository.save(TestData.getTask1());

        lawyer1 = TestData.getLawyer1();
        lawyer2 = TestData.getLawyer2();
        lawyer3 = TestData.getLawyer3();

        lawyer1.setTasks(List.of(task1));
        lawyer2.setTasks(List.of(task2));
        lawyer3.setTasks(List.of(task3));

        lawyerRepository.save(lawyer1);
        lawyerRepository.save(lawyer2);
        lawyerRepository.save(lawyer3);
    }

    @NotNull
    private Lawyer getNewLawyer() {

        var task1 = new Task("New Task 1", "New Task 1", TaskPriority.LOW, TaskStatus.ACCEPTED,
                LocalDate.of(2023, 12, 12),LocalDate.of(2023, 12, 22),
                null, 0);;
        var task2 = new Task("New Task 2", "New Task 2", TaskPriority.LOW, TaskStatus.ACCEPTED,
                LocalDate.of(2023, 12, 12),LocalDate.of(2023, 12, 22),
                null, 0);
        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);

        var newLawyer = new Lawyer("Lawyer1", "Lawyer1", JobTitle.ASSOCIATE,100.0);
        newLawyer.setTasks(List.of(task1, task2));

        return newLawyer;
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