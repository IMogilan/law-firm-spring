package com.mogilan.util;

import com.mogilan.config.PersistenceJPAConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PropertiesUtilTest {

    @ParameterizedTest
    @MethodSource("getSuccessArguments")
    void getSuccess(String key, String expectedResult) {
        var actualResult = PropertiesUtil.get(key);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    static Stream<Arguments> getSuccessArguments(){
        return Stream.of(
                Arguments.of(PersistenceJPAConfig.DB_URL_KEY, "jdbc:postgresql://localhost:5432/law_firm"),
                Arguments.of(PersistenceJPAConfig.DB_USER_KEY, "postgres"),
                Arguments.of(PersistenceJPAConfig.DB_PASSWORD_KEY, "postgres"),
                Arguments.of(PersistenceJPAConfig.DB_DRIVER_KEY, "org.postgresql.Driver"),
                Arguments.of(PersistenceJPAConfig.HIBERNATE_CONNECTION_POOL_SIZE_KEY, "10")
        );
    }
}