package com.mogilan.mapper;

import com.mogilan.TestData;

import com.mogilan.config.WebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration()
class TaskMapperTest {

    @Autowired
    TaskMapper taskMapper;

    @Test
    void toDtoSuccess() {
        var givenEntity = TestData.getTask1();
        var client = TestData.getClient1();
        givenEntity.setClient(client);
        var lawyer1 = TestData.getLawyer1();
        var lawyer2 = TestData.getLawyer2();
        var lawyer3 = TestData.getLawyer3();
        givenEntity.setLawyers(List.of(lawyer1, lawyer2, lawyer3));

        var expectedResult = TestData.getTaskDto1();
        var simpleClient = TestData.getSimpleClient1();
        expectedResult.setClient(simpleClient);
        var simpleLawyer1 = TestData.getSimpleLawyer1();
        var simpleLawyer2 = TestData.getSimpleLawyer2();
        var simpleLawyer3 = TestData.getSimpleLawyer3();
        expectedResult.setLawyers(List.of(simpleLawyer1, simpleLawyer2, simpleLawyer3));

        var actualResult = taskMapper.toDto(givenEntity);

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getClient()).isEqualTo(expectedResult.getClient());
        assertThat(actualResult.getLawyers()).isNotEmpty();
        assertThat(actualResult.getLawyers()).hasSize(3);
        assertThat(actualResult.getLawyers()).isEqualTo(expectedResult.getLawyers());
    }

    @Test
    void toEntitySuccess() {

        var givenDto = TestData.getTaskDto1();
        var simpleClient = TestData.getSimpleClient1();
        givenDto.setClient(simpleClient);
        var simpleLawyer1 = TestData.getSimpleLawyer1();
        var simpleLawyer2 = TestData.getSimpleLawyer2();
        var simpleLawyer3 = TestData.getSimpleLawyer3();
        givenDto.setLawyers(List.of(simpleLawyer1, simpleLawyer2, simpleLawyer3));

        var expectedResult = TestData.getTask1();
        var client = TestData.getClient1();
        expectedResult.setClient(client);
        var lawyer1 = TestData.getLawyer1();
        var lawyer2 = TestData.getLawyer2();
        var lawyer3 = TestData.getLawyer3();
        expectedResult.setLawyers(List.of(lawyer1, lawyer2, lawyer3));

        var actualResult = taskMapper.toEntity(givenDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(actualResult.getClient()).isEqualTo(expectedResult.getClient());
        assertThat(actualResult.getLawyers()).isNotEmpty();
        assertThat(actualResult.getLawyers()).hasSize(3);
        assertThat(actualResult.getLawyers()).isEqualTo(expectedResult.getLawyers());
    }

    @Test
    void toDtoListSuccess() {
        var givenList = List.of(TestData.getTask1(), TestData.getTask2(), TestData.getTask3());
        var expectedResult = List.of(TestData.getTaskDto1(), TestData.getTaskDto2(), TestData.getTaskDto3());

        var actualResult = taskMapper.toDtoList(givenList);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void toEntityListSuccess() {
        var givenList = List.of(TestData.getTaskDto1(), TestData.getTaskDto2(), TestData.getTaskDto3());
        var expectedResult = List.of(TestData.getTask1(), TestData.getTask2(), TestData.getTask3());

        var actualResult = taskMapper.toEntityList(givenList);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}