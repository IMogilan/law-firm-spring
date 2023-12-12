package com.mogilan.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppInitializerTest {

    @Mock
    ServletContext servletContext;
    @Mock
    ServletRegistration.Dynamic dispatcher;

    AppInitializer appInitializer = new AppInitializer();

    @Test
    void onStartupSuccess() throws ServletException {
        doReturn(dispatcher).when(servletContext).addServlet(any(), any(DispatcherServlet.class));

        appInitializer.onStartup(servletContext);

        verify(servletContext, times(1)).addListener(any(ContextLoaderListener.class));
        verify(dispatcher, times(1)).setLoadOnStartup(1);
        verify(dispatcher, times(1)).addMapping("/");
    }
}