package com.mogilan.mapper;

import com.mogilan.TestData;
import com.mogilan.config.WebConfig;
import com.mogilan.PersistenceJPATestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class, PersistenceJPATestConfig.class})
@WebAppConfiguration()
class ClientMapperTest {

    @Autowired
    ClientMapper clientMapper;

    @Test
    void toDtoSuccess() {
        var givenEntity = TestData.getClient1();
        var task1 = TestData.getTask1();
        var task2 = TestData.getTask2();
        var task3 = TestData.getTask3();
        givenEntity.setTasks(List.of(task1, task2, task3));

        var expectedResult = TestData.getClientDto1();
        var simpleTask1 = TestData.getSimpleTask1();
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();
        expectedResult.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));


        var actualResult = clientMapper.toDto(givenEntity);
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getTasks()).isNotEmpty();
        assertThat(actualResult.getTasks()).hasSize(3);
        assertThat(actualResult.getTasks()).isEqualTo(expectedResult.getTasks());
    }

    @Test
    void toEntitySuccess() {
        var givenDto = TestData.getClientDto1();
        var simpleTask1 = TestData.getSimpleTask1();
        var simpleTask2 = TestData.getSimpleTask2();
        var simpleTask3 = TestData.getSimpleTask3();
        givenDto.setTasks(List.of(simpleTask1, simpleTask2, simpleTask3));

        var expectedResult = TestData.getClient1();
        var task1 = TestData.getTask1();
        var task2 = TestData.getTask2();
        var task3 = TestData.getTask3();
        expectedResult.setTasks(List.of(task1, task2, task3));

        var actualResult = clientMapper.toEntity(givenDto);
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getTasks()).isNotEmpty();
        assertThat(actualResult.getTasks()).hasSize(3);
        assertThat(actualResult.getTasks()).isEqualTo(expectedResult.getTasks());
    }

    @Test
    void toDtoListSuccess() {
        var givenList = List.of(TestData.getClient1(), TestData.getClient2(), TestData.getClient3());
        var expectedResult = List.of(TestData.getClientDto1(), TestData.getClientDto2(), TestData.getClientDto3());

        var actualResult = clientMapper.toDtoList(givenList);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void toEntityListSuccess() {
        var givenList = List.of(TestData.getClientDto1(), TestData.getClientDto2(), TestData.getClientDto3());
        var expectedResult = List.of(TestData.getClient1(), TestData.getClient2(), TestData.getClient3());

        var actualResult = clientMapper.toEntityList(givenList);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}