package com.mogilan.config;

import com.mogilan.PersistenceJPATestConfig;
import com.mogilan.util.PropertiesUtil;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {WebConfig.class, PersistenceJPAConfig.class})
@WebAppConfiguration()
class PersistenceJPAConfigTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final Properties PROPERTIES = new Properties();

    static {
        overridePropertiesByTestProperties();
    }

    @AfterAll
    static void afterAll(){
        returnDefaultProperties();
    }

    private static void overridePropertiesByTestProperties() {
        try (var inputStream = PersistenceJPAConfigTest.class.getClassLoader().getResourceAsStream("test-application.properties")) {
            PROPERTIES.load(inputStream);
            Field field = PropertiesUtil.class.getDeclaredField("PROPERTIES");
            field.setAccessible(true);

            field.set(PropertiesUtil.class, PROPERTIES);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void returnDefaultProperties() {
        try (var inputStream = PersistenceJPAConfigTest.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
            Field field = PropertiesUtil.class.getDeclaredField("PROPERTIES");
            field.setAccessible(true);

            field.set(PropertiesUtil.class, PROPERTIES);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void configureSuccess(){
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertThat(servletContext).isNotNull();
        assertThat(servletContext).isInstanceOf(MockServletContext.class);
        assertThat(webApplicationContext.getBean("entityManagerFactory")).isNotNull();
    }

}